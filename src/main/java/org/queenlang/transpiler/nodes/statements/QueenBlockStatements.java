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
import com.github.javaparser.symbolsolver.resolution.typesolvers.AarTypeSolver;
import org.queenlang.transpiler.nodes.*;
import org.queenlang.transpiler.nodes.body.LocalVariableDeclarationNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Queen BlockStatements AST Node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenBlockStatements implements BlockStatements {
    private final Position position;
    private QueenNode parent;
    private final List<StatementNode> blockStatements;

    public QueenBlockStatements(
        final Position position
    ) {
        this(position, new ArrayList<>());
    }

    public QueenBlockStatements(
        final Position position,
        final List<StatementNode> blockStatements
    ) {
        this(position, null, blockStatements);
    }

    private QueenBlockStatements(
        final Position position,
        final QueenNode parent,
        final List<StatementNode> blockStatements
    ) {
        this.position = position;
        this.parent = parent;
        this.blockStatements = blockStatements.stream().map(
            statement -> (StatementNode) statement.withParent(this)
        ).collect(Collectors.toList());
    }


    @Override
    public List<StatementNode> blockStatements() {
        return this.blockStatements;
    }

    @Override
    public void addToJavaNode(final Node java) {
        this.blockStatements.forEach(
            bs -> bs.addToJavaNode(java)
        );
    }

    @Override
    public Position position() {
        return this.position;
    }

    @Override
    public List<QueenNode> children() {
        final List<QueenNode> children = new ArrayList<>();
        if(this.blockStatements != null) {
            children.addAll(this.blockStatements);
        }
        return children;
    }

    @Override
    public List<QueenNode> resolve(final QueenReferenceNode reference) {
        final List<QueenNode> resolved = new ArrayList<>();
        if(reference instanceof NameNode) {
            for(final StatementNode stmt : this.blockStatements) {
                if(stmt instanceof LocalVariableDeclarationNode) {
                    final LocalVariableDeclarationNode localVariableDeclaration = (LocalVariableDeclarationNode) stmt;
                    final String variableName = localVariableDeclaration.variables().get(0).variableDeclaratorId().name();
                    if(variableName.equals(((NameNode) reference).name())) {
                        System.out.println("RESOLVED " + variableName + " as LVD at position " + localVariableDeclaration.position());
                        resolved.add(localVariableDeclaration);
                    }
                }
            }
        }
        if(this.parent != null) {
            resolved.addAll(this.parent.resolve(reference));
        }
        return resolved;
    }

    @Override
    public QueenBlockStatements withParent(final QueenNode parent) {
        return new QueenBlockStatements(
            this.position,
            parent,
            this.blockStatements
        );
    }

    @Override
    public QueenNode parent() {
        return this.parent;
    }
}
