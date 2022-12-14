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

import java.util.List;

/**
 * Queen CompilationUnit (highest) AST node. This is the node you want to start
 * with when traversing/visiting this tree.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenCompilationUnitNode implements QueenNode {
    private final Position position;
    private final QueenPackageDeclarationNode packageDeclaration;
    private final List<QueenImportDeclarationNode> importDeclarations;
    private final List<QueenTypeDeclarationNode> typeDeclarations;

    public QueenCompilationUnitNode(
        final Position position,
        final QueenPackageDeclarationNode packageDeclaration,
        final List<QueenImportDeclarationNode> importDeclarations,
        final List<QueenTypeDeclarationNode> typeDeclarations
    ) {
        this.position = position;
        this.packageDeclaration = packageDeclaration;
        this.importDeclarations = importDeclarations;
        this.typeDeclarations = typeDeclarations;
    }

    @Override
    public void addToJavaNode(Node java) {
        this.packageDeclaration.addToJavaNode(java);
        this.importDeclarations.forEach(i -> i.addToJavaNode(java));
        this.typeDeclarations.forEach(t -> t.addToJavaNode(java));
    }

    public List<QueenImportDeclarationNode> importDeclarations() {
        return this.importDeclarations;
    }

    public QueenTypeDeclarationNode typeDeclaration() {
        return this.typeDeclarations.get(0);
    }

    @Override
    public Position position() {
        return this.position;
    }
}
