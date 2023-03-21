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
package org.queenlang.transpiler.nodes.statements;

import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.stmt.AssertStmt;
import com.github.javaparser.ast.stmt.BlockStmt;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.expressions.ExpressionNode;

/**
 * Unit tests for {@link QueenAssertStatementNode}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenAssertStatementNodeTestCase {

    @Test
    public void returnsPosition() {
        final Position position = Mockito.mock(Position.class);
        final AssertStatementNode assertStatement = new QueenAssertStatementNode(
            position,
            Mockito.mock(ExpressionNode.class),
            Mockito.mock(ExpressionNode.class)
        );
        MatcherAssert.assertThat(
            assertStatement.position(),
            Matchers.is(position)
        );
    }

    @Test
    public void returnsCheck() {
        final Position position = Mockito.mock(Position.class);
        final ExpressionNode check = Mockito.mock(ExpressionNode.class);
        final AssertStatementNode assertStatement = new QueenAssertStatementNode(
            position,
            check,
            Mockito.mock(ExpressionNode.class)
        );
        MatcherAssert.assertThat(
            assertStatement.check(),
            Matchers.is(check)
        );
    }

    @Test
    public void returnsMessage() {
        final Position position = Mockito.mock(Position.class);
        final ExpressionNode check = Mockito.mock(ExpressionNode.class);
        final ExpressionNode message = Mockito.mock(ExpressionNode.class);
        final AssertStatementNode assertStatement = new QueenAssertStatementNode(
            position,
            check,
            message
        );
        MatcherAssert.assertThat(
            assertStatement.message(),
            Matchers.is(message)
        );
    }

    @Test
    public void addsToJavaNode() {
        final Position position = Mockito.mock(Position.class);
        final ExpressionNode check = Mockito.mock(ExpressionNode.class);
        Mockito.when(check.toJavaExpression()).thenReturn(
            new BinaryExpr(
                new NameExpr("a"),
                new NameExpr("b"),
                BinaryExpr.Operator.EQUALS
            )
        );
        final ExpressionNode message = Mockito.mock(ExpressionNode.class);
        Mockito.when(message.toJavaExpression()).thenReturn(new StringLiteralExpr("a and b aren't equal!"));

        final AssertStatementNode assertStatement = new QueenAssertStatementNode(
            position,
            check,
            message
        );

        final BlockStmt blockStmt = new BlockStmt();
        assertStatement.addToJavaNode(blockStmt);
        final AssertStmt assertJavaStatement = (AssertStmt) blockStmt.getStatements().get(0);
        MatcherAssert.assertThat(
            assertJavaStatement.toString(),
            Matchers.equalTo("assert a == b : \"a and b aren't equal!\";")
        );
    }

}
