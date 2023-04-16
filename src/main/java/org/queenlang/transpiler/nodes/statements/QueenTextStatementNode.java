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

import com.github.javaparser.ParseProblemException;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.LabeledStmt;
import com.github.javaparser.ast.stmt.Statement;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.QueenNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Queen Statment AST Node from text.
 * To be used for debugging or quick PoCs only!
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenTextStatementNode implements StatementNode {

    private final Position position;
    private final QueenNode parent;
    private final String statement;

    public QueenTextStatementNode(final Position position, final String statement) {
        this(position, null, statement);
    }

    private QueenTextStatementNode(final Position position, final QueenNode parent, final String statement) {
        this.position = position;
        this.parent = parent;
        this.statement = statement;
    }

    @Override
    public void addToJavaNode(final Node java) {
        Statement stmt = null;
        try {
            stmt = StaticJavaParser.parseStatement(this.statement);
        } catch (ParseProblemException pbd) {
            stmt = new ExpressionStmt(
                StaticJavaParser.parseExpression(this.statement)
            );
        }
        if(stmt != null) {
            if(java instanceof BlockStmt) {
                ((BlockStmt) java).addStatement(stmt);
            } else if(java instanceof LabeledStmt) {
                ((LabeledStmt) java).setStatement(stmt);
            }
        }
    }

    @Override
    public Position position() {
        return this.position;
    }

    @Override
    public List<QueenNode> children() {
        return new ArrayList<>();
    }

    @Override
    public QueenNode withParent(final QueenNode parent) {
        return new QueenTextStatementNode(
            this.position,
            parent,
            this.statement
        );
    }

    @Override
    public QueenNode parent() {
        return this.parent;
    }

    @Override
    public String toString() {
        final Statement stmt = StaticJavaParser.parseStatement(this.statement);
        return stmt.toString();
    }
}
