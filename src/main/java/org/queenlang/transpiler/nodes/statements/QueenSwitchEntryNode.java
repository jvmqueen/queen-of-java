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
import com.github.javaparser.ast.stmt.SwitchEntry;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.QueenNode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An entry in a Switch Statement, AST Node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenSwitchEntryNode implements SwitchEntryNode {

    private final Position position;
    private final List<SwitchLabelNode> labels;
    private final BlockStatements blockStatements;

    public QueenSwitchEntryNode(
        final Position position,
        final List<SwitchLabelNode> labels,
        final BlockStatements blockStatements
    ) {
        this.position = position;
        this.labels = labels != null ? labels.stream().map(
            l -> (SwitchLabelNode) l.withParent(this)
        ).collect(Collectors.toList()) : null;
        this.blockStatements = blockStatements != null ? (BlockStatements) blockStatements.withParent(this) : null;
    }


    @Override
    public void addToJavaNode(final Node java) {
        if(java instanceof SwitchEntry) {
            final SwitchEntry entry = (SwitchEntry) java;
            if(this.labels != null) {
                this.labels.forEach(l -> l.addToJavaNode(entry));
            }
            if(this.blockStatements != null) {
                final BlockStmt blockStmt = new BlockStmt();
                this.blockStatements.addToJavaNode(blockStmt);
                entry.setStatements(blockStmt.getStatements());
            }
        }
    }

    @Override
    public Position position() {
        return this.position;
    }

    @Override
    public List<QueenNode> children() {
        final List<QueenNode> children = new ArrayList<>();
        if(this.labels != null) {
            children.addAll(this.labels);
        }
        children.add(this.blockStatements);
        return children;
    }

    @Override
    public List<SwitchLabelNode> labels() {
        return this.labels;
    }

    @Override
    public BlockStatements blockStatements() {
        return this.blockStatements;
    }
}
