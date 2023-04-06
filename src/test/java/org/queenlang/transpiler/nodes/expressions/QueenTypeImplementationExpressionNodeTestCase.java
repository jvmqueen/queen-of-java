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

import com.github.javaparser.ast.type.ClassOrInterfaceType;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.QueenNode;
import org.queenlang.transpiler.nodes.types.TypeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for {@link QueenTypeImplementationExpressionNode}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenTypeImplementationExpressionNodeTestCase {

    @Test
    public void returnsPosition() {
        final Position position = Mockito.mock(Position.class);
        final TypeImplementationExpressionNode typeImplementation = new QueenTypeImplementationExpressionNode(
            position,
            Mockito.mock(TypeNode.class),
            new ArrayList<>()
        );
        MatcherAssert.assertThat(
            typeImplementation.position(),
            Matchers.is(position)
        );
    }

    @Test
    public void returnsType() {
        final Position position = Mockito.mock(Position.class);
        final TypeNode type = Mockito.mock(TypeNode.class);
        final TypeImplementationExpressionNode typeImplementation = new QueenTypeImplementationExpressionNode(
            position,
            type,
            new ArrayList<>()
        );
        MatcherAssert.assertThat(
            typeImplementation.type(),
            Matchers.is(type)
        );
    }

    @Test
    public void returnsDims() {
        final Position position = Mockito.mock(Position.class);
        final TypeNode type = Mockito.mock(TypeNode.class);
        final List<ArrayDimensionNode> dims = new ArrayList<>();
        dims.add(Mockito.mock(ArrayDimensionNode.class));
        final TypeImplementationExpressionNode typeImplementation = new QueenTypeImplementationExpressionNode(
            position,
            type,
            dims
        );
        MatcherAssert.assertThat(
            typeImplementation.dims(),
            Matchers.is(dims)
        );
    }

    @Test
    public void returnsJavaNodeNoDims() {
        final Position position = Mockito.mock(Position.class);
        final TypeNode type = Mockito.mock(TypeNode.class);
        Mockito.when(type.toType()).thenReturn(new ClassOrInterfaceType("Student"));
        final List<ArrayDimensionNode> dims = new ArrayList<>();
        final TypeImplementationExpressionNode typeImplementation = new QueenTypeImplementationExpressionNode(
            position,
            type,
            dims
        );
        MatcherAssert.assertThat(
            typeImplementation.toJavaExpression().toString(),
            Matchers.equalTo("Student.class")
        );
    }

    @Test
    public void returnsJavaNodeWithDims() {
        final Position position = Mockito.mock(Position.class);
        final TypeNode type = Mockito.mock(TypeNode.class);
        Mockito.when(type.toType()).thenReturn(new ClassOrInterfaceType("SomeArray"));
        final List<ArrayDimensionNode> dims = new ArrayList<>();
        dims.add(Mockito.mock(ArrayDimensionNode.class));
        dims.add(Mockito.mock(ArrayDimensionNode.class));
        final TypeImplementationExpressionNode typeImplementation = new QueenTypeImplementationExpressionNode(
            position,
            type,
            dims
        );
        MatcherAssert.assertThat(
            typeImplementation.toJavaExpression().toString(),
            Matchers.equalTo("SomeArray[][].class")
        );
    }

    @Test
    public void returnsChildren() {
        final Position position = Mockito.mock(Position.class);
        final TypeNode type = Mockito.mock(TypeNode.class);
        final List<ArrayDimensionNode> dims = new ArrayList<>();
        dims.add(Mockito.mock(ArrayDimensionNode.class));
        dims.add(Mockito.mock(ArrayDimensionNode.class));
        final TypeImplementationExpressionNode typeImplementation = new QueenTypeImplementationExpressionNode(
            position,
            type,
            dims
        );

        final List<QueenNode> children = typeImplementation.children();
        MatcherAssert.assertThat(
            children.size(),
            Matchers.is(3)
        );
        MatcherAssert.assertThat(
            children.containsAll(
                List.of(
                    type,
                    dims.get(0),
                    dims.get(1)
                )
            ),
            Matchers.is(true)
        );
    }
}
