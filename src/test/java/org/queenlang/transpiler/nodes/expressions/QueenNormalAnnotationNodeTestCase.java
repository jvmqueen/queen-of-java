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
package org.queenlang.transpiler.nodes.expressions;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.queenlang.transpiler.nodes.NameNode;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.QueenNameNode;
import org.queenlang.transpiler.nodes.QueenNode;
import org.queenlang.transpiler.nodes.body.ElementValuePairNode;
import org.queenlang.transpiler.util.QueenMockito;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for {@link QueenNormalAnnotationNode}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenNormalAnnotationNodeTestCase {

    @Test
    public void returnsPosition() {
        final Position position = QueenMockito.mock(Position.class);
        final NormalAnnotationNode normalAnnotation = new QueenNormalAnnotationNode(
            position,
            new QueenNameNode(QueenMockito.mock(Position.class), "MyAnnotation"),
            new ArrayList<>()
        );
        MatcherAssert.assertThat(
            normalAnnotation.position(),
            Matchers.is(position)
        );
    }

    @Test
    public void returnsName() {
        final Position position = QueenMockito.mock(Position.class);
        final NormalAnnotationNode normalAnnotation = new QueenNormalAnnotationNode(
            position,
            new QueenNameNode(QueenMockito.mock(Position.class), "MyAnnotation"),
            new ArrayList<>()
        );
        MatcherAssert.assertThat(
            normalAnnotation.name(),
            Matchers.equalTo("MyAnnotation")
        );
    }

    @Test
    public void returnsElements() {
        final Position position = QueenMockito.mock(Position.class);
        final List<ElementValuePairNode> elements = new ArrayList<>();
        elements.add(QueenMockito.mock(ElementValuePairNode.class));
        elements.add(QueenMockito.mock(ElementValuePairNode.class));

        final NormalAnnotationNode normalAnnotation = new QueenNormalAnnotationNode(
            position,
            new QueenNameNode(QueenMockito.mock(Position.class), "MyAnnotation"),
            elements
        );
        MatcherAssert.assertThat(
            normalAnnotation.elementValuePairs(),
            Matchers.is(elements)
        );
    }

    @Test
    public void returnsChildren() {
        final Position position = QueenMockito.mock(Position.class);
        final List<ElementValuePairNode> elements = new ArrayList<>();
        elements.add(QueenMockito.mock(ElementValuePairNode.class));
        elements.add(QueenMockito.mock(ElementValuePairNode.class));
        final NameNode nameNode = QueenMockito.mock(NameNode.class);
        final NormalAnnotationNode normalAnnotation = new QueenNormalAnnotationNode(
            position,
            nameNode,
            elements
        );

        final List<QueenNode> children = normalAnnotation.children();
        MatcherAssert.assertThat(
            children.size(),
            Matchers.is(3)
        );
        MatcherAssert.assertThat(
            children.containsAll(
                List.of(
                    elements.get(0),
                    elements.get(1),
                    nameNode
                )
            ),
            Matchers.is(true)
        );
    }
}
