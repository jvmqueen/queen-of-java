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
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.LabeledStmt;
import com.github.javaparser.ast.stmt.Statement;
import org.queenlang.transpiler.nodes.Named;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.QueenNode;

import java.util.Arrays;
import java.util.List;

/**
 * Queen For Statement AST Node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenLabeledStatementNode implements LabeledStatementNode {

    /**
     * Position of this for statement in the original source code.
     */
    private final Position position;

    private final QueenNode parent;

    /**
     * Name/label of the statement.
     */
    private final String name;

    /**
     * Statements inside this labeled statement.
     */
    private final BlockStatements blockStatements;

    public QueenLabeledStatementNode(
        final Position position,
        final String name,
        final BlockStatements blockStatements
    ) {
        this(position, null, name, blockStatements);
    }

    private QueenLabeledStatementNode(
        final Position position,
        final QueenNode parent,
        final String name,
        final BlockStatements blockStatements
    ) {
        this.position = position;
        this.parent = parent;
        this.name = name;
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
     * Turn it into a JavaParser Statement.
     * @return Statement, never null.
     */
    private Statement toJavaStatement() {
        final LabeledStmt labeledStmt = new LabeledStmt();
        labeledStmt.setLabel(new SimpleName(this.name));
        if(this.blockStatements != null) {
            final BlockStmt block = new BlockStmt();
            this.blockStatements.addToJavaNode(block);
            labeledStmt.setStatement(block);
        }
        return labeledStmt;
    }

    @Override
    public Position position() {
        return this.position;
    }

    @Override
    public List<QueenNode> children() {
        return Arrays.asList(this.blockStatements);
    }

    @Override
    public QueenNode withParent(final QueenNode parent) {
        return new QueenLabeledStatementNode(
            this.position,
            parent,
            this.name,
            this.blockStatements
        );
    }

    @Override
    public QueenNode parent() {
        return this.parent;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public BlockStatements blockStatements() {
        return this.blockStatements;
    }
}
