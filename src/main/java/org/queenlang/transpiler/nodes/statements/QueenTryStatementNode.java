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
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.TryStmt;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.expressions.ExpressionNode;

import java.util.List;

/**
 * Queen Try Statement AST Node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenTryStatementNode implements TryStatementNode {

    private final Position position;
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
        this.position = position;
        this.resources = resources;
        this.tryBlockStatements = tryBlockStatements;
        this.catchClauses = catchClauses;
        this.finallyBlockStatements = finallyBlockStatements;
    }

    @Override
    public void addToJavaNode(final Node java) {

        ((BlockStmt) java).addStatement(this.toJavaStatement());
    }

    /**
     * Turn it into a JavaParser Statement.
     * @return Statement, never null.
     */
    private Statement toJavaStatement() {
        final TryStmt tryStmt = new TryStmt();
        if(this.resources != null) {
            this.resources.forEach(r -> r.addToJavaNode(tryStmt));
        }

        if(this.tryBlockStatements != null) {
            final BlockStmt tryBlockStmt = new BlockStmt();
            this.tryBlockStatements.addToJavaNode(tryBlockStmt);
            tryStmt.setTryBlock(tryBlockStmt);
        }

        if(this.catchClauses != null) {
            this.catchClauses.forEach(c -> c.addToJavaNode(tryStmt));
        }

        if(this.finallyBlockStatements != null) {
            final BlockStmt finallyBlockStmt = new BlockStmt();
            this.finallyBlockStatements.addToJavaNode(finallyBlockStmt);
            tryStmt.setFinallyBlock(finallyBlockStmt);
        }
        return tryStmt;
    }

    @Override
    public Position position() {
        return this.position;
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
