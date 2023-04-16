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
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.LabeledStmt;
import com.github.javaparser.ast.stmt.Statement;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.QueenNode;
import org.queenlang.transpiler.nodes.expressions.ExpressionNode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Queen For Statement AST Node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenForStatementNode implements ForStatementNode {

    /**
     * Position of this for statement in the original source code.
     */
    private final Position position;

    private final QueenNode parent;

    /**
     * Initialization expressions.
     */
    private final List<ExpressionNode> initialization;

    /**
     * Comparison expression.
     */
    private final ExpressionNode comparison;

    /**
     * Update expressions.
     */
    private final List<ExpressionNode> update;

    /**
     * Statements inside the for statement.
     */
    private final BlockStatements blockStatements;

    public QueenForStatementNode(
        final Position position,
        final List<ExpressionNode> initialization,
        final ExpressionNode comparison,
        final List<ExpressionNode> update,
        final BlockStatements blockStatements
    ) {
        this(
            position,
            null,
            initialization,
            comparison,
            update,
            blockStatements
        );
    }

    private QueenForStatementNode(
        final Position position,
        final QueenNode parent,
        final List<ExpressionNode> initialization,
        final ExpressionNode comparison,
        final List<ExpressionNode> update,
        final BlockStatements blockStatements
    ) {
        this.position = position;
        this.parent = parent;
        this.initialization = initialization != null ? initialization.stream().map(
            init -> (ExpressionNode) init.withParent(this)
        ).collect(Collectors.toList()) : null;
        this.comparison = comparison != null ? (ExpressionNode) comparison.withParent(this) : null;
        this.update = update != null ? update.stream().map(
            u -> (ExpressionNode) u.withParent(this)
        ).collect(Collectors.toList()) : null;
        this.blockStatements = blockStatements != null ? (BlockStatements) blockStatements.withParent(this) : null;
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
     * Turn into a JavaParser Statement.
     * @return Statement, never null.
     */
    private Statement toJavaStatement() {
        final ForStmt forStatement = new ForStmt();

        if(this.initialization != null) {
            List<Expression> init = new ArrayList<>();
            this.initialization.forEach(i -> init.add(i.toJavaExpression()));
            forStatement.setInitialization(
                new NodeList<>(init)
            );
        }

        if(this.comparison != null) {
            forStatement.setCompare(this.comparison.toJavaExpression());
        } else {
            forStatement.setCompare(new BooleanLiteralExpr(true));
        }

        if(this.update != null) {
            List<Expression> upd = new ArrayList<>();
            this.update.forEach(u -> upd.add(u.toJavaExpression()));
            forStatement.setUpdate(
                new NodeList<>(upd)
            );
        }

        if(this.blockStatements != null) {
            final BlockStmt block = new BlockStmt();
            this.blockStatements.addToJavaNode(block);
            forStatement.setBody(block);
        }
        return forStatement;
    }

    @Override
    public Position position() {
        return this.position;
    }

    @Override
    public List<QueenNode> children() {
        final List<QueenNode> children = new ArrayList<>();
        if(this.initialization != null) {
            children.addAll(this.initialization);
        }
        children.add(this.comparison);
        if(this.update != null) {
            children.addAll(this.update);
        }
        children.add(this.blockStatements);
        return children;
    }

    @Override
    public QueenNode withParent(final QueenNode parent) {
        return new QueenForStatementNode(
            this.position,
            parent,
            this.initialization,
            this.comparison,
            this.update,
            this.blockStatements
        );
    }

    @Override
    public QueenNode parent() {
        return this.parent;
    }

    @Override
    public List<ExpressionNode> initialization() {
        return this.initialization;
    }

    @Override
    public ExpressionNode comparison() {
        return this.comparison;
    }

    @Override
    public List<ExpressionNode> update() {
        return this.update;
    }

    @Override
    public BlockStatements blockStatements() {
        return this.blockStatements;
    }
}
