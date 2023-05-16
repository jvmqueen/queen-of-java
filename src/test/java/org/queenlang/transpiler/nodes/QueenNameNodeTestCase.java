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

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.queenlang.transpiler.util.QueenMockito;

/**
 * Unit tests for {@link QueenNameNode}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenNameNodeTestCase {

    @Test
    public void returnsIdentifier() {
        final NameNode name = new QueenNameNode(
            QueenMockito.mock(Position.class),
            "x"
        );
        MatcherAssert.assertThat(
            name.identifier(),
            Matchers.equalTo("x")
        );
    }

    @Test
    public void returnsQualifier() {
        final NameNode qualifier = QueenMockito.mock(NameNode.class);
        final NameNode name = new QueenNameNode(
            QueenMockito.mock(Position.class),
            qualifier,
            "x"
        );
        MatcherAssert.assertThat(
            name.qualifier(),
            Matchers.is(qualifier)
        );
    }

    @Test
    public void returnsFullNameString() {
        final NameNode name = new QueenNameNode(
            QueenMockito.mock(Position.class),
            new QueenNameNode(QueenMockito.mock(Position.class), "Util"),
            "x"
        );
        MatcherAssert.assertThat(
            name.name(),
            Matchers.equalTo("Util.x")
        );
    }

    @Test
    public void returnsParent() {
        final QueenNode parent = QueenMockito.mock(QueenNode.class);
        final NameNode name = new QueenNameNode(
            QueenMockito.mock(Position.class),
            new QueenNameNode(QueenMockito.mock(Position.class), "Util"),
            "x"
        );
        MatcherAssert.assertThat(
            name.withParent(parent).parent(),
            Matchers.is(parent)
        );
    }

    @Test
    public void resolvesWithoutQualifierInParent() {
        final QueenNode parent = QueenMockito.mock(QueenNode.class);
        final NameNode name = new QueenNameNode(
            QueenMockito.mock(Position.class),
            "x"
        ).withParent(parent);
        final QueenNode resolved = QueenMockito.mock(QueenNode.class);
        Mockito.when(parent.resolve(name, true)).thenReturn(resolved);

        MatcherAssert.assertThat(
            name.resolve(),
            Matchers.is(resolved)
        );
        Mockito.verify(parent, Mockito.times(1)).resolve(name, true);
    }

    @Test
    public void resolvesNullWithQualifierAndNullParent() {
        final NameNode name = new QueenNameNode(
            QueenMockito.mock(Position.class),
            "x"
        );

        MatcherAssert.assertThat(
            name.resolve(),
            Matchers.nullValue()
        );
    }

    @Test
    public void resolvesInQualifier() {
        final NameNode qualifier = QueenMockito.mock(NameNode.class);
        final NameNode name = new QueenNameNode(
            QueenMockito.mock(Position.class),
            qualifier,
            "x"
        );
        final QueenNode resolvedQualifier = QueenMockito.mock(QueenNode.class);
        final QueenNode resolved = QueenMockito.mock(QueenNode.class);
        Mockito.when(qualifier.resolve()).thenReturn(resolvedQualifier);
        Mockito.when(resolvedQualifier.resolve(name, false)).thenReturn(resolved);


        MatcherAssert.assertThat(
            name.resolve(),
            Matchers.is(resolved)
        );
        Mockito.verify(qualifier, Mockito.times(1)).resolve();
        Mockito.verify(resolvedQualifier, Mockito.times(1)).resolve(name, false);
    }

    @Test
    public void resolvesNullWhenQualifierResolvesToNull() {
        final NameNode qualifier = QueenMockito.mock(NameNode.class);
        final NameNode name = new QueenNameNode(
            QueenMockito.mock(Position.class),
            qualifier,
            "x"
        );
        final QueenNode resolved = QueenMockito.mock(QueenNode.class);
        Mockito.when(qualifier.resolve()).thenReturn(null);

        MatcherAssert.assertThat(
            name.resolve(),
            Matchers.nullValue()
        );
        Mockito.verify(qualifier, Mockito.times(1)).resolve();
    }

}
