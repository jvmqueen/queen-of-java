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

import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.LabeledStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.expressions.ExpressionNode;

/**
 * Unit tests for {@link QueenWhileStatementNode}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenWhileStatementNodeTestCase {

    @Test
    public void returnsPosition() {
        final Position position = Mockito.mock(Position.class);
        final WhileStatementNode whileExpression = new QueenWhileStatementNode(
            position,
            Mockito.mock(ExpressionNode.class),
            Mockito.mock(BlockStatements.class)
        );
        MatcherAssert.assertThat(
            whileExpression.position(),
            Matchers.is(position)
        );
    }

    @Test
    public void returnsBlockStatements() {
        final Position position = Mockito.mock(Position.class);
        final BlockStatements blockStatements = Mockito.mock(BlockStatements.class);
        final WhileStatementNode whileExpression = new QueenWhileStatementNode(
            position,
            Mockito.mock(ExpressionNode.class),
            blockStatements
        );
        MatcherAssert.assertThat(
            whileExpression.blockStatements(),
            Matchers.is(blockStatements)
        );
    }

    @Test
    public void returnsCondition() {
        final Position position = Mockito.mock(Position.class);
        final BlockStatements blockStatements = Mockito.mock(BlockStatements.class);
        final ExpressionNode expression = Mockito.mock(ExpressionNode.class);
        final WhileStatementNode whileExpression = new QueenWhileStatementNode(
            position,
            expression,
            blockStatements
        );
        MatcherAssert.assertThat(
            whileExpression.expression(),
            Matchers.is(expression)
        );
    }

    @Test
    public void addsToBlockJavaNode() {
        final Position position = Mockito.mock(Position.class);
        final BlockStatements blockStatements = Mockito.mock(BlockStatements.class);
        final ExpressionNode expression = Mockito.mock(ExpressionNode.class);

        final WhileStatementNode whileExpression = new QueenWhileStatementNode(
            position,
            expression,
            blockStatements
        );

        final BlockStmt blockStmt = new BlockStmt();
        whileExpression.addToJavaNode(blockStmt);

        Mockito.verify(blockStatements, Mockito.times(1)).addToJavaNode(
            Mockito.any(BlockStmt.class)
        );
        Mockito.verify(expression, Mockito.times(1)).addToJavaNode(
            Mockito.any(WhileStmt.class)
        );
        MatcherAssert.assertThat(
            blockStmt.getStatement(0).asWhileStmt(),
            Matchers.notNullValue()
        );
    }

    @Test
    public void addsToLabeledJavaNode() {
        final Position position = Mockito.mock(Position.class);
        final BlockStatements blockStatements = Mockito.mock(BlockStatements.class);
        final ExpressionNode expression = Mockito.mock(ExpressionNode.class);

        final WhileStatementNode whileExpression = new QueenWhileStatementNode(
            position,
            expression,
            blockStatements
        );

        final LabeledStmt labeled = new LabeledStmt();
        whileExpression.addToJavaNode(labeled);

        Mockito.verify(blockStatements, Mockito.times(1)).addToJavaNode(
            Mockito.any(BlockStmt.class)
        );
        Mockito.verify(expression, Mockito.times(1)).addToJavaNode(
            Mockito.any(WhileStmt.class)
        );
        MatcherAssert.assertThat(
            labeled.getStatement().asWhileStmt(),
            Matchers.notNullValue()
        );
    }
}
