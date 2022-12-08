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

import org.apache.commons.io.IOUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * QueenTranspiler test case.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenTranspilerTestCase {

    @ParameterizedTest
    @CsvSource(
        value = {
            "ClassWithFields.queen,ClassWithFields.java",
            "EmptyAnnotatedClass.queen,EmptyAnnotatedClass.java",
            "EmptyInterface.queen,EmptyInterface.java",
            "EmptyAnnotatedInterface.queen,EmptyAnnotatedInterface.java",
            "EmptyInterfaceWithImports.queen,EmptyInterfaceWithImports.java",
            "EmptyInterfaceWithNoPackage.queen,EmptyInterfaceWithNoPackage.java"
        }
    )
    public void testTranspiler(final String queenInput, final String javaOuput) throws Exception {
        final String queenClass = this.readTestResource(queenInput);
        final String javaClass = this.readTestResource(javaOuput);
        final QueenTranspiler transpiler = new JavaQueenTanspiler();
        MatcherAssert.assertThat(
            transpiler.transpile(queenClass),
            Matchers.equalTo(javaClass)
        );
        //@todo #10:40min Also assert that javaClass compiles.
    }

    /**
     * Read a test resource file's contents.
     * @param fileName File to read.
     * @return File's contents as String.
     * @throws FileNotFoundException If something is wrong.
     * @throws IOException If something is wrong.
     */
    private String readTestResource(final String fileName) throws IOException {
        return new String(
            IOUtils.toByteArray(
                new FileInputStream(
                    "src/test/resources/queenToJava/"
                    + fileName
                )
            )
        );
    }
}
