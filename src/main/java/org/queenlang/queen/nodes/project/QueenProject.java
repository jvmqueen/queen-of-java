/**
 * Copyright (c) 2022-2032, Extremely Distributed Technologies S.R.L. Romania,
 *                          Silvia Maxima et Co.
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
package org.queenlang.queen.nodes.project;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.printer.configuration.DefaultPrinterConfiguration;
import org.queenlang.classpath.Classpath;
import org.queenlang.queen.*;
import org.queenlang.queen.nodes.names.NameNode;
import org.queenlang.queen.nodes.QueenNode;
import org.queenlang.queen.nodes.QueenReferenceNode;
import org.queenlang.java.nodes.ClassCompilationUnitNode;
import org.queenlang.queen.nodes.body.ImportDeclarationNode;
import org.queenlang.queen.visitors.QueenASTSemanticValidationVisitor;
import org.queenlang.queen.visitors.QueenToJavaVisitor;
import org.queenlang.transpiler.JavaFileOutput;
import org.queenlang.transpiler.Output;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger LOG = LoggerFactory.getLogger(QueenProject.class);

    private final QueenASTParser parser;
    private final Classpath classpath;
    /**
     * Input queen files.
     */
    private final List<FileNode> input = new ArrayList<>();

    /**
     * Referenced Queen files.
     */
    private final List<FileNode> references = new ArrayList<>();


    public QueenProject(final QueenASTParser parser, final Classpath classpath) throws QueenTranspilationException, IOException {
        this.parser = parser;
        this.classpath = classpath;
        for(final Path inputFile : classpath.findAll()) {
            LOG.info("Creating the AST for Queen file {}, parent nodes need to be woven by AspectJ!", inputFile.getFileName());
            this.input.add(
                new QueenFileNode(
                    this,
                    inputFile.getFileName().toString(),
                    this.parser.parse(inputFile)
                )
            );
        }
    }

    @Override
    public List<QueenNode> children() {
        final List<QueenNode> children = new ArrayList<>();
        children.addAll(this.input);
        return children;
    }

    @Override
    public QueenNode parent() {
        return null;
    }

    @Override
    public void transpileTo(final Path outputDirectory) throws IOException, QueenTranspilationException {
        final Output output = new JavaFileOutput(outputDirectory);
        for(final FileNode queenFile : this.input) {
            write(queenFile, output);
        }
    }

    private static void write(final FileNode queenFile, final Output output) throws IOException {
        final CompilationUnit javaCompilationUnit  = new QueenToJavaVisitor().visitCompilationUnit(queenFile.compilationUnit());
        final String javaClass = javaCompilationUnit.toString(new DefaultPrinterConfiguration());
        reparseJavaClass(javaClass);
        LOG.info("Writing transpiled Queen file {} to its corresponding Java file, {}.", queenFile.fileName(), queenFile.fileName().toString().replaceAll("\\.queen", ".java"));
        output.write(javaCompilationUnit);
    }

    //TODO rethink/redesign semantic validation.
    private static void validate(final FileNode queenFile) throws QueenTranspilationException {
        final QueenASTSemanticValidationVisitor validator = new QueenASTSemanticValidationVisitor(queenFile.parent());
        final List<SemanticProblem> problems = validator.visitFile(queenFile);
        if(problems.size() > 0 && problems.stream().anyMatch(p -> p.type().equalsIgnoreCase("error"))) {
            throw new QueenTranspilationException(queenFile.fullTypeName(), problems.stream().map(SemanticProblem::toString).collect(Collectors.toList()));
        }
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
                this,
                path.getFileName().toString(),
                this.parser.parse(path)
            );
            this.references.add(parsed);
            return parsed;
        } catch (IOException | QueenTranspilationException e) {
            throw new IllegalStateException(e);
        }
    }
}
