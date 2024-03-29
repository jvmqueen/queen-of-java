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

import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.QueenNode;
import org.queenlang.transpiler.nodes.expressions.ExpressionNode;

import java.util.Arrays;
import java.util.List;

/**
 * Queen IfStatement AST Node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenIfStatementNode implements IfStatementNode {
    /**
     * Position in the original source code.
     */
    private final Position position;

    private final QueenNode parent;

    /**
     * Condition expression.
     */
    private final ExpressionNode condition;

    /**
     * Statements inside the if.
     */
    private final StatementNode thenBlockStatements;

    /**
     * Statements inside the else.
     */
    private final StatementNode elseBlockStatements;

    public QueenIfStatementNode(
        final Position position,
        final ExpressionNode condition,
        final StatementNode thenBlockStatements
    ) {
        this(position, condition, thenBlockStatements, null);
    }

    public QueenIfStatementNode(
        final Position position,
        final ExpressionNode condition,
        final StatementNode thenBlockStatements,
        final StatementNode elseBlockStatements
    ) {
        this(position, null, condition, thenBlockStatements, elseBlockStatements);
    }

    private QueenIfStatementNode(
        final Position position,
        final QueenNode parent,
        final ExpressionNode condition,
        final StatementNode thenBlockStatements,
        final StatementNode elseBlockStatements
    ) {
        this.position = position;
        this.parent = parent;
        this.condition = condition != null ? (ExpressionNode) condition.withParent(this) : null;
        this.thenBlockStatements = thenBlockStatements != null ? (StatementNode) thenBlockStatements.withParent(this) : null;
        this.elseBlockStatements = elseBlockStatements != null ? (StatementNode) elseBlockStatements.withParent(this) : null;
    }

    @Override
    public Position position() {
        return this.position;
    }

    @Override
    public List<QueenNode> children() {
        return Arrays.asList(this.condition, this.thenBlockStatements, this.elseBlockStatements);
    }

    @Override
    public QueenNode withParent(final QueenNode parent) {
        return new QueenIfStatementNode(
            this.position,
            parent,
            this.condition,
            this.thenBlockStatements,
            this.elseBlockStatements
        );
    }

    @Override
    public QueenNode parent() {
        return this.parent;
    }

    @Override
    public ExpressionNode condition() {
        return this.condition;
    }

    @Override
    public StatementNode thenBlockStatements() {
        return this.thenBlockStatements;
    }

    @Override
    public StatementNode elseBlockStatements() {
        return this.elseBlockStatements;
    }
}
