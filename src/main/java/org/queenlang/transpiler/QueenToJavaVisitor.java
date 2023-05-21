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
package org.queenlang.transpiler;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import org.queenlang.transpiler.nodes.body.*;
import org.queenlang.transpiler.nodes.types.NodeWithTypeParameters;

/**
 * Turn a Queen AST into Java AST.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenToJavaVisitor implements QueenASTVisitor<Node> {

    @Override
    public CompilationUnit visitCompilationUnit(final CompilationUnitNode node) {
        final CompilationUnit cu = new CompilationUnit();
        cu.setPackageDeclaration((PackageDeclaration) this.visitPackageDeclarationNode(node.packageDeclaration()));
        node.importDeclarations().forEach(
            i -> cu.addImport((ImportDeclaration) this.visitImportDeclarationNode(i))
        );
        cu.setType(0, (TypeDeclaration<?>) this.visitTypeDeclarationNode(node.typeDeclaration()));
        return cu;
    }

    @Override
    public Node visitNodeWithParameters(NodeWithParameters node) {
        return null;
    }

    @Override
    public Node visitNodeWithTypeParameters(NodeWithTypeParameters node) {
        return null;
    }

    @Override
    public Node visitNodeWithThrows(NodeWithThrows node) {
        return null;
    }

    @Override
    public Node visitNodeWithModifiers(NodeWithModifiers node) {
        return null;
    }

    @Override
    public Node visitNodeWithAnnotations(NodeWithAnnotations node) {
        return null;
    }

    @Override
    public Node defaultResult() {
        return null;
    }

    @Override
    public Node aggregateResult(Node aggregate, Node nextResult) {
        return aggregate;
    }
}
