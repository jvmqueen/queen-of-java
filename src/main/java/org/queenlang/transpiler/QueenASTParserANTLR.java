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

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.queenlang.generated.antlr4.QueenLexer;
import org.queenlang.generated.antlr4.QueenParser;
import org.queenlang.transpiler.nodes.body.CompilationUnitNode;

import java.io.*;
import java.nio.file.Path;

/**
 * QueenASTParser implemented with ANTLR.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenASTParserANTLR implements QueenASTParser {

    @Override
    public CompilationUnitNode parse(final Path file) throws IOException, QueenTranspilationException {
        final String input = this.inputToString(new FileInputStream(file.toFile()));
        final QueenParser parser = new QueenParser(
            new CommonTokenStream(
                new QueenLexer(
                    CharStreams.fromString(input)
                )
            )
        );
        final QueenAntlrErrorListener parsingErrorListener = new QueenAntlrErrorListener(file.getFileName().toString());
        parser.addErrorListener(parsingErrorListener);

        final QueenParser.CompilationUnitContext compilationUnitContext = parser.compilationUnit();
        if (parsingErrorListener.errors().size() > 0) {
            throw new QueenTranspilationException(parsingErrorListener.errors());
        }

        final QueenParseTreeVisitor visitor = new QueenParseTreeVisitor();
        final CompilationUnitNode queenCompilationUnitNode = visitor.visitCompilationUnit(compilationUnitContext);

        return queenCompilationUnitNode;
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
}
