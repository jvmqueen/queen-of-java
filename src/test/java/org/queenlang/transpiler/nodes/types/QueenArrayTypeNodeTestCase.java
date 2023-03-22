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
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.PrimitiveType;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.expressions.ArrayDimensionNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for {@link QueenArrayTypeNode}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenArrayTypeNodeTestCase {

    @Test
    public void returnsPosition() {
        final Position position = Mockito.mock(Position.class);
        final ArrayTypeNode arrayTypeNode = new QueenArrayTypeNode(
            position,
            Mockito.mock(TypeNode.class),
            new ArrayList<>()
        );
        MatcherAssert.assertThat(
            arrayTypeNode.position(),
            Matchers.is(position)
        );
    }

    @Test
    public void returnsType() {
        final Position position = Mockito.mock(Position.class);
        final TypeNode type = Mockito.mock(TypeNode.class);
        final ArrayTypeNode arrayTypeNode = new QueenArrayTypeNode(
            position,
            type,
            new ArrayList<>()
        );
        MatcherAssert.assertThat(
            arrayTypeNode.type(),
            Matchers.is(type)
        );
    }

    @Test
    public void returnsDims() {
        final Position position = Mockito.mock(Position.class);
        final TypeNode type = Mockito.mock(TypeNode.class);
        final List<ArrayDimensionNode> dims = new ArrayList<>();
        dims.add(Mockito.mock(ArrayDimensionNode.class));
        final ArrayTypeNode arrayTypeNode = new QueenArrayTypeNode(
            position,
            type,
            dims
        );
        MatcherAssert.assertThat(
            arrayTypeNode.dims(),
            Matchers.is(dims)
        );
    }

    @Test
    public void addsToJavaVariableDeclaratorNode() {
        final Position position = Mockito.mock(Position.class);
        final TypeNode type = Mockito.mock(TypeNode.class);
        Mockito.when(type.toType()).thenReturn(PrimitiveType.intType());
        final List<ArrayDimensionNode> dims = new ArrayList<>();
        dims.add(Mockito.mock(ArrayDimensionNode.class));
        dims.add(Mockito.mock(ArrayDimensionNode.class));
        final ArrayTypeNode arrayTypeNode = new QueenArrayTypeNode(
            position,
            type,
            dims
        );

        final VariableDeclarator variableDeclarator = new VariableDeclarator();
        arrayTypeNode.addToJavaNode(variableDeclarator);

        MatcherAssert.assertThat(
            variableDeclarator.getType().toString(),
            Matchers.equalTo("int[][]")
        );
    }

    @Test
    public void addsToJavaMethodDeclaratorNode() {
        final Position position = Mockito.mock(Position.class);
        final TypeNode type = Mockito.mock(TypeNode.class);
        Mockito.when(type.toType()).thenReturn(PrimitiveType.longType());
        final List<ArrayDimensionNode> dims = new ArrayList<>();
        dims.add(Mockito.mock(ArrayDimensionNode.class));
        dims.add(Mockito.mock(ArrayDimensionNode.class));
        dims.add(Mockito.mock(ArrayDimensionNode.class));
        final ArrayTypeNode arrayTypeNode = new QueenArrayTypeNode(
            position,
            type,
            dims
        );

        final MethodDeclaration methodDeclaration = new MethodDeclaration();
        arrayTypeNode.addToJavaNode(methodDeclaration);

        MatcherAssert.assertThat(
            methodDeclaration.getType().toString(),
            Matchers.equalTo("long[][][]")
        );
    }

    @Test
    public void addsToJavaParameterNode() {
        final Position position = Mockito.mock(Position.class);
        final TypeNode type = Mockito.mock(TypeNode.class);
        Mockito.when(type.toType()).thenReturn(PrimitiveType.doubleType());
        final List<ArrayDimensionNode> dims = new ArrayList<>();
        dims.add(Mockito.mock(ArrayDimensionNode.class));
        final ArrayTypeNode arrayTypeNode = new QueenArrayTypeNode(
            position,
            type,
            dims
        );

        final Parameter parameter = new Parameter();
        arrayTypeNode.addToJavaNode(parameter);

        MatcherAssert.assertThat(
            parameter.getType().toString(),
            Matchers.equalTo("double[]")
        );
    }
}
