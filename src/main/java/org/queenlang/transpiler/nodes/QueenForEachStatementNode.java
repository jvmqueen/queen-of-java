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
package org.queenlang.transpiler.nodes;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.stmt.LabeledStmt;
import com.github.javaparser.ast.stmt.Statement;

/**
 * Queen For Statement AST Node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenForEachStatementNode implements QueenStatementNode {

    /**
     * Position of this for statement in the original source code.
     */
    private final Position position;

    /**
     * Variable.
     */
    private final QueenLocalVariableDeclarationNode variable;

    /**
     * Iterable.
     */
    private final QueenExpressionNode iterable;

    /**
     * Statements inside the for-each statement.
     */
    private final QueenBlockStatements blockStatements;

    public QueenForEachStatementNode(
        final Position position,
        final QueenLocalVariableDeclarationNode variable,
        final QueenExpressionNode iterable,
        final QueenBlockStatements blockStatements
    ) {
        this.position = position;
        this.variable = variable;
        this.iterable = iterable;
        this.blockStatements = blockStatements;
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
        final ForEachStmt forEachStmt = new ForEachStmt();
        forEachStmt.setVariable((VariableDeclarationExpr) this.variable.toJavaExpression());
        forEachStmt.setIterable(this.iterable.toJavaExpression());

        if(this.blockStatements != null) {
            final BlockStmt block = new BlockStmt();
            this.blockStatements.addToJavaNode(block);
            forEachStmt.setBody(block);
        }
        return forEachStmt;
    }

    @Override
    public Position position() {
        return this.position;
    }
}
