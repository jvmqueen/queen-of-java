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
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.SwitchEntry;
import com.github.javaparser.ast.stmt.SwitchStmt;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.expressions.ExpressionNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Queen Switch Statement AST Node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenSwitchStatementNode implements SwitchStatementNode {

    private final Position position;
    private final ExpressionNode expression;
    private final List<SwitchEntryNode> entries;

    public QueenSwitchStatementNode(
        final Position position,
        final ExpressionNode expression,
        final List<SwitchEntryNode> entries
    ) {
        this.position = position;
        this.expression = expression;
        this.entries = entries;
    }

    @Override
    public void addToJavaNode(final Node java) {
        final SwitchStmt switchStmt = new SwitchStmt();
        if(this.expression != null) {
            this.expression.addToJavaNode(switchStmt);
        }
        if(this.entries != null) {
            final List<SwitchEntry> entries = new ArrayList<>();
            this.entries.forEach(
                e -> {
                    final SwitchEntry entry = new SwitchEntry();
                    e.addToJavaNode(entry);
                    entries.add(entry);
                }

            );
            switchStmt.setEntries(new NodeList<>(entries));
        }
        ((BlockStmt) java).addStatement(switchStmt);

    }

    @Override
    public Position position() {
        return this.position;
    }

    @Override
    public ExpressionNode expression() {
        return this.expression;
    }

    @Override
    public List<SwitchEntryNode> entries() {
        return this.entries;
    }
}
