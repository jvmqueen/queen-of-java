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
 * Unit tests for {@link QueenFieldAccessExpressionNode}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenFieldAccessExpressionNodeTestCase {

    @Test
    public void returnsPosition() {
        final Position position = QueenMockito.mock(Position.class);
        final FieldAccessExpressionNode fieldAccess = new QueenFieldAccessExpressionNode(
            position,
            QueenMockito.mock(ExpressionNode.class),
            "x"
        );
        MatcherAssert.assertThat(
            fieldAccess.position(),
            Matchers.is(position)
        );
    }

    @Test
    public void returnsScope() {
        final Position position = QueenMockito.mock(Position.class);
        final ExpressionNode scope = QueenMockito.mock(ExpressionNode.class);
        final FieldAccessExpressionNode fieldAccess = new QueenFieldAccessExpressionNode(
            position,
            scope,
            "x"
        );
        MatcherAssert.assertThat(
            fieldAccess.scope(),
            Matchers.is(scope)
        );
    }

    @Test
    public void returnsName() {
        final Position position = QueenMockito.mock(Position.class);
        final ExpressionNode scope = QueenMockito.mock(ExpressionNode.class);
        final FieldAccessExpressionNode fieldAccess = new QueenFieldAccessExpressionNode(
            position,
            scope,
            "x"
        );
        MatcherAssert.assertThat(
            fieldAccess.name(),
            Matchers.equalTo("x")
        );
    }

    @Test
    public void returnsJavaExpression() {
        final Position position = QueenMockito.mock(Position.class);
        final ExpressionNode scope = QueenMockito.mock(ExpressionNode.class);
        Mockito.when(scope.toJavaExpression()).thenReturn(
            new NameExpr("SomeClass")
        );
        final FieldAccessExpressionNode fieldAccess = new QueenFieldAccessExpressionNode(
            position,
            scope,
            "x"
        );
        final Expression javaExpression = fieldAccess.toJavaExpression();
        MatcherAssert.assertThat(
            javaExpression.toString(),
            Matchers.equalTo("SomeClass.x")
        );
        MatcherAssert.assertThat(
            javaExpression.toFieldAccessExpr().get().getScope().asNameExpr().getName().toString(),
            Matchers.equalTo("SomeClass")
        );
        MatcherAssert.assertThat(
            javaExpression.toFieldAccessExpr().get().getName().asString(),
            Matchers.equalTo("x")
        );
    }

    @Test
    public void returnsChildren() {
        final Position position = QueenMockito.mock(Position.class);
        final ExpressionNode scope = QueenMockito.mock(ExpressionNode.class);
        Mockito.when(scope.toJavaExpression()).thenReturn(
            new NameExpr("SomeClass")
        );
        final FieldAccessExpressionNode fieldAccess = new QueenFieldAccessExpressionNode(
            position,
            scope,
            "x"
        );

        final List<QueenNode> children = fieldAccess.children();
        MatcherAssert.assertThat(
            children.size(),
            Matchers.is(1)
        );
        MatcherAssert.assertThat(
            children.containsAll(
                List.of(
                    scope
                )
            ),
            Matchers.is(true)
        );
    }
}
