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
package org.queenlang.transpiler;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.printer.configuration.DefaultPrinterConfiguration;
import org.antlr.v4.runtime.*;
import org.queenlang.generated.antlr4.QueenLexer;
import org.queenlang.generated.antlr4.QueenParser;
import org.queenlang.transpiler.nodes.body.CompilationUnitNode;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Queen-to-Java transpiler, implemented with JavaParser.
 * DEPRECATED: will be removed as the architecture of queenc is put in place.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
@Deprecated
public final class JavaQueenTranspiler implements QueenTranspiler {
    @Override
    public void transpile(List<Path> files) throws QueenTranspilationException, IOException {
        System.out.println("TRANSPILING FILES: ");
        files.forEach(
            f -> System.out.println(f.toString())
        );
        Path output = Path.of(".");
        if(files.size() > 0) {
            String fileName = files.get(0).getFileName().toString();
            InputStream stream = new FileInputStream(files.get(0).toFile());
            final String transpiled = this.transpile(
                stream, fileName
            );
            final String javaFileName = fileName.replaceAll("\\.queen", "\\.java");
            Path javaFile = Path.of(output.toString(), javaFileName);
            if(Files.exists(javaFile)) {
                Files.delete(javaFile);
            }
            javaFile = Files.createFile(javaFile);
            Files.writeString(javaFile, transpiled);
        }
    }

    @Override
    public String transpile(final InputStream clazz, final String fileName) throws IOException, QueenTranspilationException {
        final String input = this.inputToString(clazz);

        final QueenLexer lexer = new QueenLexer(
            CharStreams.fromString(input)
        );
        final QueenParser parser = new QueenParser(
            new CommonTokenStream(lexer)
        );
        final QueenAntlrErrorListener parsingErrorListener = new QueenAntlrErrorListener(fileName);
        parser.addErrorListener(parsingErrorListener);

        final QueenParseTreeVisitor visitor = new QueenParseTreeVisitor();
        final CompilationUnitNode queenCompilationUnitNode = visitor.visitCompilationUnit(
            parser.compilationUnit()
        );

        if(parsingErrorListener.errors().size() > 0) {
            throw new QueenTranspilationException(parsingErrorListener.errors());
        } else {
            final QueenASTSemanticValidationVisitor validator = new QueenASTSemanticValidationVisitor();
            final List<SemanticProblem> problems = validator.visitCompilationUnit(queenCompilationUnitNode);
            if(problems.size() > 0) {//&& problems.stream().anyMatch(p -> p.type().equalsIgnoreCase("error"))) {
                throw new QueenTranspilationException(problems.stream().map(SemanticProblem::toString).collect(Collectors.toList()));
            } else {
                final CompilationUnit javaCompilationUnit  = new CompilationUnit();
                queenCompilationUnitNode.addToJavaNode(javaCompilationUnit);
                final String javaClass = javaCompilationUnit.toString(new DefaultPrinterConfiguration());
                this.reparseJavaClass(javaClass);
                return javaClass;
            }
        }
    }

    private String inputToString(final InputStream stream) throws IOException {
        final StringBuilder builder = new StringBuilder();
        try (
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(stream)
            )
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append('\n');
            }
            return builder.toString();
        }
    }

    /**
     * Extra contextual validation, in case we missed something ourselves.
     * @param javaClass Parsed Java class.
     * @throws QueenTranspilationException Containing any problems.
     */
    private void reparseJavaClass(final String javaClass) throws QueenTranspilationException {
        JavaParser parser1 = new JavaParser();
        ParseResult<CompilationUnit> res = parser1.parse(javaClass);
        res.getProblems().forEach(
            p -> {
                System.out.println(p.toString());
                p.getLocation().ifPresent(
                    t -> System.out.println(t.toString())
                );
            }
        );
    }
}
