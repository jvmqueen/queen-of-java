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

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.QueenNameNode;
import org.queenlang.transpiler.nodes.QueenNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for {@link QueenArrayAccessExpressionNode}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenArrayAccessExpressionNodeTestCase {

    @Test
    public void returnsPosition() {
        final Position position = Mockito.mock(Position.class);
        final ArrayAccessExpressionNode arrAccess = new QueenArrayAccessExpressionNode(
            position,
            Mockito.mock(ExpressionNode.class),
            new ArrayList<>()
        );
        MatcherAssert.assertThat(
            arrAccess.position(),
            Matchers.is(position)
        );
    }

    @Test
    public void returnsName() {
        final ExpressionNode name = Mockito.mock(ExpressionNode.class);
        final ArrayAccessExpressionNode arrAccess = new QueenArrayAccessExpressionNode(
            Mockito.mock(Position.class),
            name,
            new ArrayList<>()
        );
        MatcherAssert.assertThat(
            arrAccess.name(),
            Matchers.is(name)
        );
    }

    @Test
    public void returnsDims() {
        final List<ArrayDimensionNode> dims = new ArrayList<>();
        dims.add(Mockito.mock(ArrayDimensionNode.class));
        final ArrayAccessExpressionNode arrAccess = new QueenArrayAccessExpressionNode(
            Mockito.mock(Position.class),
            Mockito.mock(ExpressionNode.class),
            dims
        );
        MatcherAssert.assertThat(
            arrAccess.dims(),
            Matchers.is(dims)
        );
    }

    @Test
    public void returnsJavaExpression() {
        final ExpressionNode name = new QueenNameNode(
            Mockito.mock(Position.class),
            "arrayName"
        );
        final List<ArrayDimensionNode> dims = new ArrayList<>();
        final ArrayDimensionNode dim1 = Mockito.mock(ArrayDimensionNode.class);
        Mockito.when(dim1.expression()).thenReturn(
            new QueenNameNode(
                Mockito.mock(Position.class),
                "i"
            )
        );
        final ArrayDimensionNode dim2 = Mockito.mock(ArrayDimensionNode.class);
        Mockito.when(dim2.expression()).thenReturn(
            new QueenNameNode(
                Mockito.mock(Position.class),
                "j"
            )
        );
        dims.add(dim1);
        dims.add(dim2);

        final ArrayAccessExpressionNode arrayAccessExpressionNode = new QueenArrayAccessExpressionNode(
            Mockito.mock(Position.class),
            name,
            dims
        );

        final Expression expression = arrayAccessExpressionNode.toJavaExpression();
        MatcherAssert.assertThat(
            expression.asArrayAccessExpr().getName()
                .asArrayAccessExpr().getName().asNameExpr().getName().asString(),
            Matchers.equalTo("arrayName")
        );
        MatcherAssert.assertThat(
            expression.asArrayAccessExpr().getIndex().asNameExpr().getName().asString(),
            Matchers.equalTo("j")
        );
        MatcherAssert.assertThat(
            expression.asArrayAccessExpr().getName().asArrayAccessExpr().getIndex().asNameExpr().getName().asString(),
            Matchers.equalTo("i")
        );
    }

    @Test
    public void returnsJavaStatement() {
        final ExpressionNode name = new QueenNameNode(
            Mockito.mock(Position.class),
            "arrayName"
        );
        final List<ArrayDimensionNode> dims = new ArrayList<>();
        final ArrayDimensionNode dim1 = Mockito.mock(ArrayDimensionNode.class);
        Mockito.when(dim1.expression()).thenReturn(
            new QueenNameNode(
                Mockito.mock(Position.class),
                "i"
            )
        );
        final ArrayDimensionNode dim2 = Mockito.mock(ArrayDimensionNode.class);
        Mockito.when(dim2.expression()).thenReturn(
            new QueenNameNode(
                Mockito.mock(Position.class),
                "j"
            )
        );
        dims.add(dim1);
        dims.add(dim2);

        final ArrayAccessExpressionNode arrayAccessExpressionNode = new QueenArrayAccessExpressionNode(
            Mockito.mock(Position.class),
            name,
            dims
        );

        final Statement stmt = arrayAccessExpressionNode.toJavaStatement();
        MatcherAssert.assertThat(
            stmt,
            Matchers.allOf(
                Matchers.notNullValue(),
                Matchers.instanceOf(ExpressionStmt.class)
            )
        );
        final Expression expression = stmt.asExpressionStmt().getExpression();
        MatcherAssert.assertThat(
            expression.asArrayAccessExpr().getName()
                .asArrayAccessExpr().getName().asNameExpr().getName().asString(),
            Matchers.equalTo("arrayName")
        );
        MatcherAssert.assertThat(
            expression.asArrayAccessExpr().getIndex().asNameExpr().getName().asString(),
            Matchers.equalTo("j")
        );
        MatcherAssert.assertThat(
            expression.asArrayAccessExpr().getName().asArrayAccessExpr().getIndex().asNameExpr().getName().asString(),
            Matchers.equalTo("i")
        );
    }

    @Test
    public void returnsChildren() {
        final ExpressionNode expressionNode = Mockito.mock(ExpressionNode.class);
        final List<ArrayDimensionNode> dims = new ArrayList<>();
        dims.add(Mockito.mock(ArrayDimensionNode.class));
        dims.add(Mockito.mock(ArrayDimensionNode.class));

        final ArrayAccessExpressionNode arrayAccessExpressionNode = new QueenArrayAccessExpressionNode(
            Mockito.mock(Position.class),
            expressionNode,
            dims
        );

        final List<QueenNode> children = arrayAccessExpressionNode.children();
        MatcherAssert.assertThat(
            children.size(),
            Matchers.is(3)
        );
        MatcherAssert.assertThat(
            children.containsAll(
                List.of(
                    expressionNode,
                    dims.get(0),
                    dims.get(1)
                )
            ),
            Matchers.is(true)
        );
    }
}
