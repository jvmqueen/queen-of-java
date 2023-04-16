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
package org.queenlang.transpiler.nodes.body;

import com.github.javaparser.ast.Node;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.QueenNode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Queen ClassBody AST node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenClassBodyNode implements ClassBodyNode {

    private final Position position;
    private final QueenNode parent;
    private final List<ClassBodyDeclarationNode> classBodyDeclarations;

    public QueenClassBodyNode(
        final Position position,
        final List<ClassBodyDeclarationNode> classBodyDeclarations
    ) {
        this(position, null, classBodyDeclarations);
    }

    private QueenClassBodyNode(
        final Position position,
        final QueenNode parent,
        final List<ClassBodyDeclarationNode> classBodyDeclarations
    ) {
        this.position = position;
        this.parent = parent;
        this.classBodyDeclarations = classBodyDeclarations != null ? classBodyDeclarations.stream().map(
            cbd -> (ClassBodyDeclarationNode) cbd.withParent(this)
        ).collect(Collectors.toList()) : null;
    }

    @Override
    public void addToJavaNode(final Node java) {
        this.classBodyDeclarations.forEach(
            cbd -> cbd.addToJavaNode(java)
        );
    }

    @Override
    public Position position() {
        return this.position;
    }

    @Override
    public List<ClassBodyDeclarationNode> classBodyDeclarations() {
        return this.classBodyDeclarations;
    }

    @Override
    public List<QueenNode> children() {
        final List<QueenNode> children = new ArrayList<>();
        if(this.classBodyDeclarations != null) {
            children.addAll(this.classBodyDeclarations);
        }
        return children;
    }

    @Override
    public QueenNode withParent(final QueenNode parent) {
        return new QueenClassBodyNode(
            this.position,
            parent,
            this.classBodyDeclarations
        );
    }

    @Override
    public QueenNode parent() {
        return this.parent;
    }
}
