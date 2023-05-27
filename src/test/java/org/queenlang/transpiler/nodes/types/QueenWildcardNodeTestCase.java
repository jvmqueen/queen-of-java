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
package org.queenlang.transpiler.nodes.types;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.QueenNode;
import org.queenlang.transpiler.nodes.expressions.AnnotationNode;
import org.queenlang.transpiler.util.QueenMockito;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for {@link QueenWildcardNode}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenWildcardNodeTestCase {

    @Test
    public void returnsPosition() {
        final Position position = QueenMockito.mock(Position.class);
        final WildcardTypeNode wildcard = new QueenWildcardNode(
            position,
            new ArrayList<>(),
            QueenMockito.mock(ReferenceTypeNode.class),
            QueenMockito.mock(ReferenceTypeNode.class)
        );
        MatcherAssert.assertThat(
            wildcard.position(),
            Matchers.is(position)
        );
    }

    @Test
    public void returnsAnnotations() {
        final Position position = QueenMockito.mock(Position.class);
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(QueenMockito.mock(AnnotationNode.class));
        final WildcardTypeNode wildcard = new QueenWildcardNode(
            position,
            annotations,
            QueenMockito.mock(ReferenceTypeNode.class),
            QueenMockito.mock(ReferenceTypeNode.class)
        );
        MatcherAssert.assertThat(
            wildcard.annotations(),
            Matchers.is(annotations)
        );
    }

    @Test
    public void returnsExtendedType() {
        final Position position = QueenMockito.mock(Position.class);
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(QueenMockito.mock(AnnotationNode.class));
        final ReferenceTypeNode extendedType = QueenMockito.mock(ReferenceTypeNode.class);
        final WildcardTypeNode wildcard = new QueenWildcardNode(
            position,
            annotations,
            extendedType,
            QueenMockito.mock(ReferenceTypeNode.class)
        );
        MatcherAssert.assertThat(
            wildcard.extendedType(),
            Matchers.is(extendedType)
        );
    }

    @Test
    public void returnsSuperType() {
        final Position position = QueenMockito.mock(Position.class);
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(QueenMockito.mock(AnnotationNode.class));
        final ReferenceTypeNode extendedType = QueenMockito.mock(ReferenceTypeNode.class);
        final ReferenceTypeNode superType = QueenMockito.mock(ReferenceTypeNode.class);
        final WildcardTypeNode wildcard = new QueenWildcardNode(
            position,
            annotations,
            extendedType,
            superType
        );
        MatcherAssert.assertThat(
            wildcard.superType(),
            Matchers.is(superType)
        );
    }

    @Test
    public void returnsChildren() {
        final Position position = QueenMockito.mock(Position.class);
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(QueenMockito.mock(AnnotationNode.class));
        final ReferenceTypeNode extendedType = QueenMockito.mock(ReferenceTypeNode.class);
        final ReferenceTypeNode superType = QueenMockito.mock(ReferenceTypeNode.class);
        final WildcardTypeNode wildcard = new QueenWildcardNode(
            position,
            annotations,
            extendedType,
            superType
        );

        final List<QueenNode> children = wildcard.children();
        MatcherAssert.assertThat(
            children.size(),
            Matchers.is(3)
        );
        MatcherAssert.assertThat(
            children.containsAll(
                List.of(
                    annotations.get(0),
                    extendedType,
                    superType
                )
            ),
            Matchers.is(true)
        );
    }

}
