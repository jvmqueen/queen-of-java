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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Queen Try Statement AST Node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenTryStatementNode implements TryStatementNode {

    private final Position position;
    private final QueenNode parent;
    private final List<ExpressionNode> resources;
    private final BlockStatements tryBlockStatements;
    private final List<CatchClauseNode> catchClauses;
    private final BlockStatements finallyBlockStatements;

    public QueenTryStatementNode(
        final Position position,
        final List<ExpressionNode> resources,
        final BlockStatements tryBlockStatements,
        final List<CatchClauseNode> catchClauses,
        final BlockStatements finallyBlockStatements
    ) {
        this(
            position,
            null,
            resources,
            tryBlockStatements,
            catchClauses,
            finallyBlockStatements
        );
    }

    private QueenTryStatementNode(
        final Position position,
        final QueenNode parent,
        final List<ExpressionNode> resources,
        final BlockStatements tryBlockStatements,
        final List<CatchClauseNode> catchClauses,
        final BlockStatements finallyBlockStatements
    ) {
        this.position = position;
        this.parent = parent;
        this.resources = resources != null ? resources.stream().map(
            r -> (ExpressionNode) r.withParent(this)
        ).collect(Collectors.toList()) : null;
        this.tryBlockStatements = tryBlockStatements != null ? (BlockStatements) tryBlockStatements.withParent(this) : null;
        this.catchClauses = catchClauses != null ? catchClauses.stream().map(cc -> (CatchClauseNode) cc.withParent(this)).collect(Collectors.toList()) : null;
        this.finallyBlockStatements = finallyBlockStatements != null ? (BlockStatements) finallyBlockStatements.withParent(this) : null;
    }

    @Override
    public Position position() {
        return this.position;
    }

    @Override
    public List<QueenNode> children() {
        final List<QueenNode> children = new ArrayList<>();
        if(this.resources != null) {
            children.addAll(this.resources);
        }
        children.add(this.tryBlockStatements);
        if(this.catchClauses != null) {
            children.addAll(this.catchClauses);
        }
        children.add(this.finallyBlockStatements);
        return children;
    }

    @Override
    public QueenNode withParent(final QueenNode parent) {
        return new QueenTryStatementNode(
            this.position,
            parent,
            this.resources,
            this.tryBlockStatements,
            this.catchClauses,
            this.finallyBlockStatements
        );
    }

    @Override
    public QueenNode parent() {
        return this.parent;
    }

    @Override
    public List<ExpressionNode> resources() {
        return this.resources;
    }

    @Override
    public BlockStatements tryBlockStatements() {
        return this.tryBlockStatements;
    }

    @Override
    public List<CatchClauseNode> catchClauses() {
        return this.catchClauses;
    }

    @Override
    public BlockStatements finallyBlockStatements() {
        return this.finallyBlockStatements;
    }
}
