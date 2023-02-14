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

import org.queenlang.transpiler.nodes.*;
import org.queenlang.transpiler.nodes.body.*;
import org.queenlang.transpiler.nodes.expressions.QueenExpressionNode;
import org.queenlang.transpiler.nodes.expressions.QueenTextExpressionNode;
import org.queenlang.transpiler.nodes.statements.QueenBlockStatements;
import org.queenlang.transpiler.nodes.statements.QueenStatementNode;
import org.queenlang.transpiler.nodes.statements.QueenTextStatementNode;

/**
 * Queen abstract syntax tree visitor.
 * @param <T> Type param.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public interface QueenASTVisitor<T> {
    T visitQueenAnnotationElementDeclarationNode(final QueenAnnotationElementDeclarationNode node);
    T visitQueenAnnotationNode(final QueenAnnotationNode node);
    T visitQueenAnnotationTypeBodyNode(final QueenAnnotationTypeBodyNode node);
    T visitQueenAnnotationTypeDeclarationNode(final QueenAnnotationTypeDeclarationNode node);
    T visitQueenAnnotationTypeMemberDeclarationNode(final QueenAnnotationTypeMemberDeclarationNode node);
    T visitQueenBlockStatementsNode(final QueenBlockStatements node);
    T visitQueenClassBodyDeclarationNode(final QueenClassBodyDeclarationNode node);
    T visitQueenClassBodyNode(final QueenClassBodyNode node);
    T visitQueenClassDeclarationNode(final QueenClassDeclarationNode node);
    T visitQueenClassMemberDeclarationNode(final QueenClassMemberDeclarationNode node);
    T visitQueenCompilationUnitNode(final QueenCompilationUnitNode node);
    T visitQueenConstantDeclarationNode(final QueenConstantDeclarationNode node);
    T visitQueenConstructorDeclarationNode(final QueenConstructorDeclarationNode node);
    T visitQueenExplicitConstructorInvocationNode(final QueenExplicitConstructorInvocationNode node);
    T visitQueenExpressionNode(final QueenExpressionNode node);
    T visitQueenFieldDeclarationNode(final QueenFieldDeclarationNode node);
    T visitQueenImportDeclarationNode(final QueenImportDeclarationNode node);
    T visitQueenInstanceInitializerNode(final QueenInstanceInitializerNode node);
    T visitQueenInterfaceBodyNode(final QueenInterfaceBodyNode node);
    T visitQueenInterfaceDeclarationNode(final QueenInterfaceDeclarationNode node);
    T visitQueenInterfaceMemberDeclarationNode(final QueenInterfaceMemberDeclarationNode node);
    T visitQueenInterfaceMethodDeclarationNode(final QueenInterfaceMethodDeclarationNode node);
    T visitQueenMarkerAnnotationNode(final QueenMarkerAnnotationNode node);
    T visitQueenMethodDeclarationNode(final QueenMethodDeclarationNode node);
    T visitQueenModifierNode(final QueenModifierNode node);
    T visitQueenNode(final QueenNode node);
    T visitQueenNormalAnnotationNode(final QueenNormalAnnotationNode node);
    T visitQueenNormalInterfaceDeclarationNode(final QueenNormalInterfaceDeclarationNode node);
    T visitQueenPackageDeclarationNode(final QueenPackageDeclarationNode node);
    T visitQueenParameterNode(final QueenParameterNode node);
    T visitQueenSingleMemberAnnotationNode(final QueenSingleMemberAnnotationNode node);
    T visitQueenStatementNode(final QueenStatementNode node);
    T visitQueenTextExpressionNode(final QueenTextExpressionNode node);
    T visitQueenTextStatementNode(final QueenTextStatementNode node);
    T visitQueenTypeDeclarationNode(final QueenTypeDeclarationNode node);
    T visitQueenTypeParameterNode(final QueenTypeParameterNode node);
    T visitQueenNodeWithTypeParameters(final QueenNodeWithTypeParameters node);
    T visitQueenTypeNode(final QueenTypeNode node);
    T visitQueenReferenceTypeNode(final QueenReferenceTypeNode node);
    T visitQueenClassOrInterfaceTypeNode(final QueenClassOrInterfaceTypeNode node);
    T visitQueenWildcardNode(final QueenWildcardNode node);
    //todo #49:Add visitor methods for each and every kind of Node. Last added was WildcardNode#d869ce8f
}
