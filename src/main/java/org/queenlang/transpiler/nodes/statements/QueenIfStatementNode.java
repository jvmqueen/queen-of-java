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

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.LabeledStmt;
import com.github.javaparser.ast.stmt.Statement;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.expressions.ExpressionNode;

/**
 * Queen IfStatement AST Node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenIfStatementNode implements StatementNode {
    /**
     * Position in the original source code.
     */
    private final Position position;

    /**
     * Condition expression.
     */
    private final ExpressionNode condition;

    /**
     * Statements inside the if.
     */
    private final QueenBlockStatements thenBlockStatements;

    /**
     * Statements inside the else.
     */
    private final QueenBlockStatements elseBlockStatements;

    public QueenIfStatementNode(
        final Position position,
        final ExpressionNode condition,
        final QueenBlockStatements thenBlockStatements
    ) {
        this(position, condition, thenBlockStatements, null);
    }

    public QueenIfStatementNode(
        final Position position,
        final ExpressionNode condition,
        final QueenBlockStatements thenBlockStatements,
        final QueenBlockStatements elseBlockStatements
    ) {
        this.position = position;
        this.condition = condition;
        this.thenBlockStatements = thenBlockStatements;
        this.elseBlockStatements = elseBlockStatements;
    }

    @Override
    public void addToJavaNode(final Node java) {
        if(java instanceof BlockStmt) {
            ((BlockStmt) java).addStatement(this.toJavaStatement());
        } else if(java instanceof LabeledStmt) {
            ((LabeledStmt) java).setStatement(this.toJavaStatement());
        }
    }

    /**
     * Turn it into a JavaParser Statement.
     * @return Statement, never null.
     */
    private Statement toJavaStatement() {
        final IfStmt ifStatement = new IfStmt();
        this.condition.addToJavaNode(ifStatement);
        final BlockStmt thenBlockStmt = new BlockStmt();
        if(this.thenBlockStatements != null) {
            this.thenBlockStatements.addToJavaNode(thenBlockStmt);
        }
        ifStatement.setThenStmt(thenBlockStmt);
        if(this.elseBlockStatements != null) {
            final BlockStmt elseBlockStmt = new BlockStmt();
            this.elseBlockStatements.addToJavaNode(elseBlockStmt);
            ifStatement.setElseStmt(elseBlockStmt);
        }
        return ifStatement;
    }

    @Override
    public Position position() {
        return this.position;
    }
}
