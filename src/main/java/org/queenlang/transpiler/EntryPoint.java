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

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.printer.configuration.PrettyPrinterConfiguration;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.queenlang.generated.antlr4.QueenLexer;
import org.queenlang.generated.antlr4.QueenParser;
import org.queenlang.generated.antlr4.QueenParserBaseListener;
import org.queenlang.transpiler.nodes.QueenNode;

import java.io.*;

/**
 * The transpiler's entry point.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class EntryPoint {
    public static void run(String path) throws IOException {
        final StringBuilder builder = new StringBuilder();
        try (
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                    EntryPoint.class.getClassLoader()
                        .getResourceAsStream(path)
                )
            )
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append('\n');
            }
        }
        System.out.println("[DEBUG] Queen file contents: ");
        System.out.println("------------------------------");
        System.out.println(builder);
        System.out.println("------------------------------");
        System.out.println("[DEBUG] End of Queen file contents.");

        QueenLexer lexer = new QueenLexer(
            CharStreams.fromString(builder.toString())
        );
        QueenParser parser = new QueenParser(
            new CommonTokenStream(lexer)
        );
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(
            new QueenParserBaseListener() {
                int i = 1;
                public void enterPackageName(QueenParser.PackageNameContext ctx) {
                    System.out.println("ENTER PACKAGE NAME: " + i + " " + ctx.getText());
                    i++;
                }
            },
            parser.compilationUnit()
        );

        System.out.println("ERRORS: " + parser.getNumberOfSyntaxErrors());

        QueenParser parser2 = new QueenParser(
            new CommonTokenStream(
                new QueenLexer(CharStreams.fromString(builder.toString()))
            )
        );
        QueenVisitor visitor = new QueenVisitor();
        QueenNode queenCompilationUnitNode = visitor.visitCompilationUnit(parser2.compilationUnit());
        CompilationUnit javaCompilationUnit  = new CompilationUnit();
        queenCompilationUnitNode.addToJavaNode(javaCompilationUnit);

        System.out.println("[DEBUG] Transpiled Java Class:");
        System.out.println("------------------------------");

        System.out.println(
            javaCompilationUnit.toString(new PrettyPrinterConfiguration())
        );

        System.out.println("------------------------------");
        System.out.println("[DEBUG] End of transpiled java class.");

    }

    public static void main(String[] args) {
//        if(args == null || args.length !=1) {
//            System.out.println("[ERROR] Expecting exactly 1 argument.");
//        } else {
//            System.out.println("[DEBUG] Received arg: " + args[0]);
            try {
                //run(args[0]);
                run("HelloWorld.queen");
            } catch (IOException e) {
                e.printStackTrace();
            }
//        }
    }
}
