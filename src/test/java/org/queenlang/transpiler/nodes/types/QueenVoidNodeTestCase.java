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

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.VoidType;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.QueenNode;
import org.queenlang.transpiler.nodes.expressions.AnnotationNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for {@link QueenVoidNode}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenVoidNodeTestCase {

    @Test
    public void returnsPosition() {
        final Position position = Mockito.mock(Position.class);
        final VoidTypeNode voidTypeNode = new QueenVoidNode(
            position,
            new ArrayList<>()
        );
        MatcherAssert.assertThat(
            voidTypeNode.position(),
            Matchers.is(position)
        );
    }

    @Test
    public void returnsAnnotations() {
        final Position position = Mockito.mock(Position.class);
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(Mockito.mock(AnnotationNode.class));
        final VoidTypeNode voidTypeNode = new QueenVoidNode(
            position,
            annotations
        );
        MatcherAssert.assertThat(
            voidTypeNode.annotations(),
            Matchers.is(annotations)
        );
    }

    @Test
    public void addsToJavaNode() {
        final Position position = Mockito.mock(Position.class);
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(Mockito.mock(AnnotationNode.class));
        final VoidTypeNode voidTypeNode = new QueenVoidNode(
            position,
            annotations
        );

        final MethodDeclaration method = new MethodDeclaration();
        voidTypeNode.addToJavaNode(method);

        MatcherAssert.assertThat(
            method.getType().asVoidType(),
            Matchers.notNullValue()
        );
        annotations.forEach(
            a -> Mockito.verify(a, Mockito.times(1)).addToJavaNode(
                Mockito.any(VoidType.class)
            )
        );
    }

    @Test
    public void returnsChildren() {
        final Position position = Mockito.mock(Position.class);
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(Mockito.mock(AnnotationNode.class));
        final VoidTypeNode wildcard = new QueenVoidNode(
            position,
            annotations
        );

        final List<QueenNode> children = wildcard.children();
        MatcherAssert.assertThat(
            children.size(),
            Matchers.is(1)
        );
        MatcherAssert.assertThat(
            children.containsAll(
                List.of(annotations.get(0))
            ),
            Matchers.is(true)
        );
    }

}
