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

import com.github.javaparser.ast.expr.ArrayCreationExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.type.PrimitiveType;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.QueenNameNode;
import org.queenlang.transpiler.nodes.QueenNode;
import org.queenlang.transpiler.nodes.types.TypeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for {@link QueenArrayCreationExpressionNode}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenArrayCreationExpressionNodeTestCase {

    @Test
    public void returnsPosition() {
        final Position position = Mockito.mock(Position.class);
        final ArrayCreationExpressionNode arrCreation = new QueenArrayCreationExpressionNode(
            position,
            Mockito.mock(TypeNode.class),
            new ArrayList<>(),
            Mockito.mock(ArrayInitializerExpressionNode.class)
        );
        MatcherAssert.assertThat(
            arrCreation.position(),
            Matchers.is(position)
        );
    }

    @Test
    public void returnsType() {
        final TypeNode type = Mockito.mock(TypeNode.class);
        final ArrayCreationExpressionNode arrCreation = new QueenArrayCreationExpressionNode(
            Mockito.mock(Position.class),
            type,
            new ArrayList<>(),
            Mockito.mock(ArrayInitializerExpressionNode.class)
        );
        MatcherAssert.assertThat(
            arrCreation.type(),
            Matchers.is(type)
        );
    }

    @Test
    public void returnsDims() {
        final List<ArrayDimensionNode> dims = new ArrayList<>();
        dims.add(Mockito.mock(ArrayDimensionNode.class));
        final ArrayCreationExpressionNode arrCreation = new QueenArrayCreationExpressionNode(
            Mockito.mock(Position.class),
            Mockito.mock(TypeNode.class),
            dims,
            Mockito.mock(ArrayInitializerExpressionNode.class)
        );
        MatcherAssert.assertThat(
            arrCreation.dims(),
            Matchers.is(dims)
        );
    }

    @Test
    public void returnsInitializer() {
        final ExpressionNode initializer = Mockito.mock(ExpressionNode.class);
        final ArrayCreationExpressionNode arrCreation = new QueenArrayCreationExpressionNode(
            Mockito.mock(Position.class),
            Mockito.mock(TypeNode.class),
            new ArrayList<>(),
            initializer
        );
        MatcherAssert.assertThat(
            arrCreation.arrayInitializer(),
            Matchers.is(initializer)
        );
    }

    @Test
    public void returnsJavaExpression(){
        final TypeNode type = Mockito.mock(TypeNode.class);
        Mockito.when(type.toType()).thenReturn(PrimitiveType.intType());
        final List<ArrayDimensionNode> dims = new ArrayList<>();
        final ArrayDimensionNode dim1 = Mockito.mock(ArrayDimensionNode.class);
        Mockito.when(dim1.expression()).thenReturn(
            new QueenNameNode(
                Mockito.mock(Position.class),
                "5"
            )
        );
        final ArrayDimensionNode dim2 = Mockito.mock(ArrayDimensionNode.class);
        Mockito.when(dim2.expression()).thenReturn(
            new QueenNameNode(
                Mockito.mock(Position.class),
                "10"
            )
        );
        dims.add(dim1);
        dims.add(dim2);
        final ArrayCreationExpressionNode arrCreation = new QueenArrayCreationExpressionNode(
            Mockito.mock(Position.class),
            type,
            dims,
            null
        );
        final ArrayCreationExpr expression = (ArrayCreationExpr) arrCreation.toJavaExpression();
        System.out.println(expression);

        MatcherAssert.assertThat(
            expression.getElementType().asPrimitiveType(),
            Matchers.equalTo(PrimitiveType.intType())
        );
        MatcherAssert.assertThat(
            expression.getLevels().get(0).getDimension().get().asNameExpr().getName().asString(),
            Matchers.equalTo("5")
        );
        MatcherAssert.assertThat(
            expression.getLevels().get(1).getDimension().get().asNameExpr().getName().asString(),
            Matchers.equalTo("10")
        );
    }

    @Test
    public void returnsJavaStatement(){
        final TypeNode type = Mockito.mock(TypeNode.class);
        Mockito.when(type.toType()).thenReturn(PrimitiveType.intType());
        final List<ArrayDimensionNode> dims = new ArrayList<>();
        final ArrayDimensionNode dim1 = Mockito.mock(ArrayDimensionNode.class);
        Mockito.when(dim1.expression()).thenReturn(
            new QueenNameNode(
                Mockito.mock(Position.class),
                "1"
            )
        );
        dims.add(dim1);
        final ArrayCreationExpressionNode arrCreation = new QueenArrayCreationExpressionNode(
            Mockito.mock(Position.class),
            type,
            dims,
            null
        );
        final ExpressionStmt stmt = (ExpressionStmt) arrCreation.toJavaStatement();
        final ArrayCreationExpr expression = (ArrayCreationExpr) stmt.getExpression();
        System.out.println(expression);

        MatcherAssert.assertThat(
            expression.getElementType().asPrimitiveType(),
            Matchers.equalTo(PrimitiveType.intType())
        );
        MatcherAssert.assertThat(
            expression.getLevels().get(0).getDimension().get().asNameExpr().getName().asString(),
            Matchers.equalTo("1")
        );
    }

    @Test
    public void returnsChildren() {
        final TypeNode type = Mockito.mock(TypeNode.class);
        final List<ArrayDimensionNode> dims = new ArrayList<>();
        dims.add(Mockito.mock(ArrayDimensionNode.class));
        dims.add(Mockito.mock(ArrayDimensionNode.class));
        final ExpressionNode init = Mockito.mock(ExpressionNode.class);
        final ArrayCreationExpressionNode arrCreation = new QueenArrayCreationExpressionNode(
            Mockito.mock(Position.class),
            type,
            dims,
            init
        );

        final List<QueenNode> children = arrCreation.children();
        MatcherAssert.assertThat(
            children.size(),
            Matchers.is(4)
        );
        MatcherAssert.assertThat(
            children.containsAll(
                List.of(
                    type,
                    dims.get(0),
                    dims.get(1),
                    init
                )
            ),
            Matchers.is(true)
        );
    }
}
