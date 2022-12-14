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
package org.queenlang.transpiler.nodes;

import com.github.javaparser.ast.CompilationUnit;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

/**
 * Unit tests for {@link QueenImportDeclarationNode}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenImportDeclarationNodeTestCase {

    /**
     * It can add the import data to the Java node.
     */
    @Test
    public void addsImportToJavaNode() {
        final CompilationUnit java = Mockito.mock(CompilationUnit.class);
        final QueenNode importDeclarationNode = new QueenImportDeclarationNode(
            Mockito.mock(Position.class),
            "com.example.web",
            true,
            false
        );

        importDeclarationNode.addToJavaNode(java);

        Mockito.verify(java, Mockito.times(1)).addImport(
            "com.example.web", true, false
        );
    }

    /**
     * It can return false if this declaration is not contained by the
     * given declaration.
     */
    @ParameterizedTest
    @CsvSource(
        value = {
            "com.example.web,false,false,com.example.other,false,false",
            "com.example.web.A,false,false,com.example.web.B,false,false",
            "com.example.web,true,true,com.example.other,true,true",
            "com.example.web,true,false,com.example.other,true,false",
            "com.example.web,false,true,com.example.other,false,true",
            "com.example.web,false,false,java.util.List,false,false",
            "com.example.web.ts.A,false,false,com.example.web,false,true"
        }
    )
    public void isContainedByReturnsFalse(
        String first, boolean firstStatic, boolean firstAsterisk,
        String second, boolean secondStatic, boolean secondAsterisk
    ) {
        final QueenImportDeclarationNode firstImport = new QueenImportDeclarationNode(
            Mockito.mock(Position.class),
            first,
            firstStatic,
            firstAsterisk
        );

        final QueenImportDeclarationNode secondImport = new QueenImportDeclarationNode(
            Mockito.mock(Position.class),
            second,
            secondStatic,
            secondAsterisk
        );
        MatcherAssert.assertThat(
            firstImport.isContainedBy(secondImport),
            Matchers.is(false)
        );
    }

    /**
     * It can return true if this declaration is contained by the
     * given declaration.
     */
    @ParameterizedTest
    @CsvSource(
        value = {
            "com.example.web,false,false,com.example.web,false,false",
            "com.example.web.A,false,false,com.example.web.A,false,false",
            "com.example.web,true,true,com.example.web,true,true",
            "com.example.web.A,false,false,com.example.web,false,true"
        }
    )
    public void isContainedByReturnsTrue(
        String first, boolean firstStatic, boolean firstAsterisk,
        String second, boolean secondStatic, boolean secondAsterisk
    ) {
        final QueenImportDeclarationNode firstImport = new QueenImportDeclarationNode(
            Mockito.mock(Position.class),
            first,
            firstStatic,
            firstAsterisk
        );

        final QueenImportDeclarationNode secondImport = new QueenImportDeclarationNode(
            Mockito.mock(Position.class),
            second,
            secondStatic,
            secondAsterisk
        );
        MatcherAssert.assertThat(
            firstImport.isContainedBy(secondImport),
            Matchers.is(true)
        );
    }

}
