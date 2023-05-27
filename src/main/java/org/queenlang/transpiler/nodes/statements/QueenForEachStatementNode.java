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
import org.queenlang.transpiler.nodes.body.LocalVariableDeclarationNode;
import org.queenlang.transpiler.nodes.expressions.ExpressionNode;

import java.util.Arrays;
import java.util.List;

/**
 * Queen For Statement AST Node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenForEachStatementNode implements ForEachStatementNode {

    /**
     * Position of this for statement in the original source code.
     */
    private final Position position;

    private final QueenNode parent;

    /**
     * Variable.
     */
    private final LocalVariableDeclarationNode variable;

    /**
     * Iterable.
     */
    private final ExpressionNode iterable;

    /**
     * Statements inside the for-each statement.
     */
    private final StatementNode blockStatements;

    public QueenForEachStatementNode(
        final Position position,
        final LocalVariableDeclarationNode variable,
        final ExpressionNode iterable,
        final StatementNode blockStatements
    ) {
        this(
            position,
            null,
            variable,
            iterable,
            blockStatements
        );
    }

    private QueenForEachStatementNode(
        final Position position,
        final QueenNode parent,
        final LocalVariableDeclarationNode variable,
        final ExpressionNode iterable,
        final StatementNode blockStatements
    ) {
        this.position = position;
        this.parent = parent;
        this.variable = variable != null ? (LocalVariableDeclarationNode) variable.withParent(this) : null;
        this.iterable = iterable != null ? (ExpressionNode) iterable.withParent(this) : null;
        this.blockStatements = blockStatements != null ? (StatementNode) blockStatements.withParent(this) : null;
    }

    @Override
    public Position position() {
        return this.position;
    }

    @Override
    public List<QueenNode> children() {
        return Arrays.asList(this.variable, this.iterable, this.blockStatements);
    }

    @Override
    public QueenNode withParent(final QueenNode parent) {
        return new QueenForEachStatementNode(
            this.position,
            parent,
            this.variable,
            this.iterable,
            this.blockStatements
        );
    }

    @Override
    public QueenNode parent() {
        return this.parent;
    }

    @Override
    public LocalVariableDeclarationNode variable() {
        return this.variable;
    }

    @Override
    public ExpressionNode iterable() {
        return this.iterable;
    }

    @Override
    public StatementNode blockStatements() {
        return this.blockStatements;
    }
}
