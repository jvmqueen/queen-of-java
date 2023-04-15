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

import com.github.javaparser.ast.expr.NameExpr;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.QueenNode;
import org.queenlang.transpiler.util.QueenMockito;

import java.util.List;

/**
 * Unit tests for {@link QueenUnaryExpressionNode}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenUnaryExpressionNodeTestCase {

    @Test
    public void returnsPosition() {
        final Position position = QueenMockito.mock(Position.class);
        final UnaryExpressionNode unary = new QueenUnaryExpressionNode(
            position,
            "++",
            false,
            QueenMockito.mock(ExpressionNode.class)
        );
        MatcherAssert.assertThat(
            unary.position(),
            Matchers.is(position)
        );
    }

    @Test
    public void returnsOperator() {
        final Position position = QueenMockito.mock(Position.class);
        final UnaryExpressionNode unary = new QueenUnaryExpressionNode(
            position,
            "++",
            false,
            QueenMockito.mock(ExpressionNode.class)
        );
        MatcherAssert.assertThat(
            unary.operator(),
            Matchers.equalTo("++")
        );
    }

    @Test
    public void returnsIsPrefix() {
        final Position position = QueenMockito.mock(Position.class);
        final UnaryExpressionNode unary = new QueenUnaryExpressionNode(
            position,
            "++",
            true,
            QueenMockito.mock(ExpressionNode.class)
        );
        MatcherAssert.assertThat(
            unary.isPrefix(),
            Matchers.is(true)
        );
    }

    @Test
    public void returnsExpression() {
        final Position position = QueenMockito.mock(Position.class);
        final ExpressionNode expression = QueenMockito.mock(ExpressionNode.class);
        final UnaryExpressionNode unary = new QueenUnaryExpressionNode(
            position,
            "++",
            true,
            expression
        );
        MatcherAssert.assertThat(
            unary.expression(),
            Matchers.is(expression)
        );
    }

    @Test
    public void returnsPrefixedJavaExpression() {
        final Position position = QueenMockito.mock(Position.class);
        final ExpressionNode expression = QueenMockito.mock(ExpressionNode.class);
        Mockito.when(expression.toJavaExpression()).thenReturn(new NameExpr("i"));
        final UnaryExpressionNode unary = new QueenUnaryExpressionNode(
            position,
            "++",
            true,
            expression
        );
        MatcherAssert.assertThat(
            unary.toJavaExpression().toString(),
            Matchers.equalTo("++i")
        );
    }

    @Test
    public void returnsPostfixedJavaExpression() {
        final Position position = QueenMockito.mock(Position.class);
        final ExpressionNode expression = QueenMockito.mock(ExpressionNode.class);
        Mockito.when(expression.toJavaExpression()).thenReturn(new NameExpr("i"));
        final UnaryExpressionNode unary = new QueenUnaryExpressionNode(
            position,
            "++",
            false,
            expression
        );
        MatcherAssert.assertThat(
            unary.toJavaExpression().toString(),
            Matchers.equalTo("i++")
        );
    }

    @Test
    public void returnsChildren() {
        final Position position = QueenMockito.mock(Position.class);
        final ExpressionNode expression = QueenMockito.mock(ExpressionNode.class);
        final UnaryExpressionNode unary = new QueenUnaryExpressionNode(
            position,
            "++",
            false,
            expression
        );

        List<QueenNode> children = unary.children();
        MatcherAssert.assertThat(
            children.size(),
            Matchers.is(1)
        );
        MatcherAssert.assertThat(
            children.containsAll(
                List.of(expression)
            ),
            Matchers.is(true)
        );
    }
}
