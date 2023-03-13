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

import org.queenlang.transpiler.nodes.QueenNode;
import org.queenlang.transpiler.nodes.body.*;
import org.queenlang.transpiler.nodes.expressions.*;
import org.queenlang.transpiler.nodes.statements.*;
import org.queenlang.transpiler.nodes.types.*;

/**
 * Queen abstract syntax tree visitor.
 * @param <T> Type param.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public interface QueenASTVisitor<T> {
    T visitCompilationUnit(final CompilationUnitNode node);
    T visitAnnotationElementDeclarationNode(final AnnotationElementDeclarationNode node);
    T visitAnnotationTypeBodyNode(final AnnotationTypeBodyNode node);
    T visitAnnotationTypeDeclarationNode(final AnnotationTypeDeclarationNode node);
    T visitAnnotationTypeMemberDeclarationNode(final AnnotationTypeMemberDeclarationNode node);
    T visitClassBodyDeclarationNode(final ClassBodyDeclarationNode node);
    T visitClassBodyNode(final ClassBodyNode node);
    T visitClassDeclarationNode(final ClassDeclarationNode node);
    T visitClassMemberDeclarationNode(final ClassMemberDeclarationNode node);
    T visitConstantDeclarationNode(final ConstantDeclarationNode node);
    T visitConstructorDeclarationNode(final ConstructorDeclarationNode node);
    T visitFieldDeclarationNode(final FieldDeclarationNode node);
    T visitImportDeclarationNode(final ImportDeclarationNode node);
    T visitInstanceInitializerNode(final InstanceInitializerNode node);
    T visitInterfaceBodyNode(final InterfaceBodyNode node);
    T visitInterfaceDeclarationNode(final InterfaceDeclarationNode node);
    T visitInterfaceMemberDeclarationNode(final InterfaceMemberDeclarationNode node);
    T visitInterfaceMethodDeclarationNode(final InterfaceMethodDeclarationNode node);
    T visitLocalVariableDeclarationNode(final LocalVariableDeclarationNode node);
    T visitMethodDeclarationNode(final MethodDeclarationNode node);
    T visitModifierNode(final ModifierNode node);
    T visitNormalInterfaceDeclarationNode(final NormalInterfaceDeclarationNode node);
    T visitPackageDeclarationNode(final PackageDeclarationNode node);
    T visitParameterNode(final ParameterNode node);
    T visitTypeDeclarationNode(final TypeDeclarationNode node);
    T visitVariableDeclaratorId(final VariableDeclaratorId node);
    T visitAnnotationNode(final AnnotationNode node);
    T visitArrayAccessExpressionNode(final ArrayAccessExpressionNode node);
    T visitArrayCreationExpressionNode(final ArrayCreationExpressionNode node);
    T visitArrayDimensionNode(final ArrayDimensionNode node);
    T visitArrayInitializerExpressionNode(final ArrayInitializerExpressionNode node);
    T visitAssignmentExpressionNode(final AssignmentExpressionNode node);
    T visitBinaryExpressionNode(final BinaryExpressionNode node);
    T visitBooleanLiteralExpressionNode(final BooleanLiteralExpressionNode node);
    T visitBracketedExpressionNode(final BracketedExpressionNode node);
    T visitCastExpressionNode(final CastExpressionNode node);
    T visitCharLiteralExpressionNode(final CharLiteralExpressionNode node);
    T visitConditionalExpressionNode(final ConditionalExpressionNode node);
    T visitDoubleLiteralExpressionNode(final DoubleLiteralExpressionNode node);
    T visitExpressionNode(final ExpressionNode node);
    T visitFieldAccessExpressionNode(final FieldAccessExpressionNode node);
    T visitInstanceOfExpressionNode(final InstanceOfExpressionNode node);
    T visitIntegerLiteralExpressionNode(final IntegerLiteralExpressionNode node);
    T visitLambdaExpressionNode(final LambdaExpressionNode node);
    T visitLiteralStringValueExpressionNode(final LiteralStringValueExpressionNode node);
    T visitLongLiteralExpressionNode(final LongLiteralExpressionNode node);
    T visitMarkerAnnotationNode(final MarkerAnnotationNode node);
    T visitMethodInvocationExpressionNode(final MethodInvocationExpressionNode node);
    T visitMethodReferenceExpressionNode(final MethodReferenceExpressionNode node);
    T visitNormalAnnotationNode(final NormalAnnotationNode node);
    T visitNullLiteralExpressionNode(final NullLiteralExpressionNode node);
    T visitObjectCreationExpressionNode(final ObjectCreationExpressionNode node);
    T visitSingleMemberAnnotationNode(final SingleMemberAnnotationNode node);
    T visitStringLiteralExpressionNode(final StringLiteralExpressionNode node);
    T visitSuperExpressionNode(final SuperExpressionNode node);
    T visitThisExpressionNode(final ThisExpressionNode node);
    T visitTypeImplementationExpressionNode(final TypeImplementationExpressionNode node);
    T visitUnaryExpressionNode(final UnaryExpressionNode node);
    T visitAssertStatementNode(final AssertStatementNode node);
    T visitBlockStatements(final BlockStatements node);
    T visitBreakStatementNode(final BreakStatementNode node);
    T visitCatchClauseNode(final CatchClauseNode node);
    T visitCatchFormalParameterNode(final CatchFormalParameterNode node);
    T visitContinueStatementNode(final ContinueStatementNode node);
    T visitDoStatementNode(final DoStatementNode node);
    T visitEmptyStatementNode(final EmptyStatementNode node);
    T visitExplicitConstructorInvocationNode(final ExplicitConstructorInvocationNode node);
    T visitExpressionStatementNode(final ExpressionStatementNode node);
    T visitForEachStatementNode(final ForEachStatementNode node);
    T visitForStatementNode(final ForStatementNode node);
    T visitIfStatementNode(final IfStatementNode node);
    T visitLabeledStatementNode(final LabeledStatementNode node);
    T visitReturnStatementNode(final ReturnStatementNode node);
    T visitStatementNode(final StatementNode node);
    T visitSwitchEntryNode(final SwitchEntryNode node);
    T visitSwitchLabelNode(final SwitchLabelNode node);
    T visitSwitchStatementNode(final SwitchStatementNode node);
    T visitSynchronizedStatementNode(final SynchronizedStatementNode node);
    T visitThrowStatementNode(final ThrowStatementNode node);
    T visitTryStatementNode(final TryStatementNode node);
    T visitWhileStatementNode(final WhileStatementNode node);
    T visitArrayTypeNode(final ArrayTypeNode node);
    T visitClassOrInterfaceTypeNode(final ClassOrInterfaceTypeNode node);
    T visitExceptionTypeNode(final ExceptionTypeNode node);
    T visitNodeWithParameters(final NodeWithParameters node);

    T visitNodeWithTypeParameters(final NodeWithTypeParameters node);

    T visitNodeWithThrows(final NodeWithThrows node);

    T visitPrimitiveTypeNode(final PrimitiveTypeNode node);
    T visitReferenceTypeNode(final ReferenceTypeNode node);
    T visitTypeNode(final TypeNode node);
    T visitTypeParameterNode(final TypeParameterNode node);
    T visitVoidTypeNode(final VoidTypeNode node);
    T visitWildcardTypeNode(final WildcardTypeNode node);
    T visitQueenNode(final QueenNode node);
}
