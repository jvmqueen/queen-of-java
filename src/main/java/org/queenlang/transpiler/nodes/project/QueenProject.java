/**
 * Copyright (c) 2022-2023, Extremely Distributed Technologies S.R.L. Romania
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 * Neither the name of the copyright holder nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */
package org.queenlang.transpiler.nodes.project;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.printer.configuration.DefaultPrinterConfiguration;
import org.queenlang.transpiler.*;
import org.queenlang.transpiler.nodes.NameNode;
import org.queenlang.transpiler.nodes.QueenNode;
import org.queenlang.transpiler.nodes.QueenReferenceNode;
import org.queenlang.transpiler.nodes.java.ClassCompilationUnitNode;
import org.queenlang.transpiler.nodes.body.ImportDeclarationNode;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A Queen project, AST Node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenProject implements ProjectNode {
    private final Classpath classpath;
    private final QueenASTParser parser;

    /**
     * Input queen files.
     */
    private final List<FileNode> input = new ArrayList<>();

    /**
     * References from the input queen files.
     */
    private final List<FileNode> references = new ArrayList<>();

    public QueenProject(final Classpath classpath, final QueenASTParser parser, final List<Path> inputFiles) throws QueenTranspilationException, IOException {
        this.classpath = classpath;
        this.parser = parser;
        for(final Path inputFile : inputFiles) {
            this.input.add(
                new QueenFileNode(
                    inputFile.getFileName().toString(),
                    this.parser.parse(inputFile)
                ).withParent(this)
            );
        }
    }

    @Override
    public List<QueenNode> children() {
        return new ArrayList<>();
    }

    @Override
    public QueenNode withParent(QueenNode parent) {
        throw new IllegalStateException("The ProjectNode has no parent, it's the root of the AST.");
    }

    @Override
    public QueenNode parent() {
        return null;
    }

    @Override
    public void transpileTo(final Output output) throws IOException, QueenTranspilationException {
        for(final FileNode queenFile : this.input) {
            validateAndWrite(queenFile, output);
        }
        for(int i=0; i < this.references.size(); i++) {
            final FileNode ref = this.references.get(i);
            final boolean alreadyTranspiled = this.input.stream().filter(
                in -> in.fullTypeName().equals(ref.fullTypeName())
            ).findFirst().orElse(null) != null;
            if(!alreadyTranspiled) {
                validateAndWrite(ref, output);
            }
        }
    }

    private static void validateAndWrite(final FileNode queenFile, final Output output) throws QueenTranspilationException, IOException {
//        final PrintQueenASTVisitor print = new PrintQueenASTVisitor();
//        String toPrint = print.visitFile(queenFile);
//        System.out.println("AST FOR " + queenFile.fullTypeName() + " HERE: " );
//        System.out.println(toPrint);
//        System.out.println("END AST");
        final QueenASTSemanticValidationVisitor validator = new QueenASTSemanticValidationVisitor();
        final List<SemanticProblem> problems = validator.visitFile(queenFile);
        if(problems.size() > 0) {//&& problems.stream().anyMatch(p -> p.type().equalsIgnoreCase("error"))) {
            throw new QueenTranspilationException(queenFile.fullTypeName(), problems.stream().map(SemanticProblem::toString).collect(Collectors.toList()));
        }
        final CompilationUnit javaCompilationUnit  = new QueenToJavaVisitor().visitCompilationUnit(queenFile.compilationUnit());
        final String javaClass = javaCompilationUnit.toString(new DefaultPrinterConfiguration());
        reparseJavaClass(javaClass);
        output.write(javaCompilationUnit);
    }

    /**
     * Extra contextual validation, in case we missed something ourselves.
     * @param javaClass Parsed Java class.
     * @throws QueenTranspilationException Containing any problems.
     */
    private static void reparseJavaClass(final String javaClass) {
        final JavaParser parser = new JavaParser();
        final ParseResult<CompilationUnit> res = parser.parse(javaClass);
        res.getProblems().forEach(
            p -> {
                System.out.println(p.toString());
                p.getLocation().ifPresent(
                    System.out::println
                );
            }
        );
    }

    @Override
    public QueenNode resolve(final QueenReferenceNode reference, boolean goUp) {
        if(reference instanceof ImportDeclarationNode) {
            final ImportDeclarationNode importDeclaration = (ImportDeclarationNode) reference;
            final NameNode importName = importDeclaration.importDeclarationName();
            if(importDeclaration.asteriskImport()) {
                return this.resolveName(importName, false);
            }
            return this.resolveName(importName, true);
        } else if(reference instanceof NameNode) {
            return this.resolveName((NameNode) reference, false);
        }
        return null;
    }

    private QueenNode resolveName(final NameNode reference, boolean lookingOnlyForClass) {
        QueenNode resolved = this.references.stream().filter(
            r -> r.fullTypeName().equals(reference.name())
        ).findFirst().orElse(null);
        if(resolved != null) {
            return resolved;
        }
        final Path foundPackageOrClass = this.classpath.find(reference);
        if(foundPackageOrClass != null) {
            boolean isDirectory = Files.isDirectory(foundPackageOrClass);
            if(isDirectory && lookingOnlyForClass) {
                return null;
            }
            if(isDirectory) {
                resolved = new QueenPackageNode(this, foundPackageOrClass);
            } else {
                resolved = this.parsePath(foundPackageOrClass);
            }
        } else {
            try {
                final Class clazz = Class.forName(reference.name());
                resolved = new ClassCompilationUnitNode(this, clazz);
            } catch (ClassNotFoundException e) {
                for(Package pack : Package.getPackages()) {
                    if(pack.getName().equals(reference.name()) || pack.getName().startsWith(reference.name())) {
                        resolved = new QueenPackageNode(this, Path.of(String.join(FileSystems.getDefault().getSeparator(), pack.getName().split("\\."))));
                        break;
                    }
                }
            }
        }
        return resolved;
    }

    private FileNode parsePath(final Path path) {
        try {
            final FileNode parsed = new QueenFileNode(
                path.getFileName().toString(),
                this.parser.parse(path)
            ).withParent(this);
            this.references.add(parsed);
            return parsed;
        } catch (IOException | QueenTranspilationException e) {
            throw new IllegalStateException(e);
        }
    }
}
