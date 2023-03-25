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
 * Abstract Queen AST Visitor which simply visits the children of each node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 * @param <T>
 */
public abstract class QueenAbstractASTVisitor<T> implements QueenASTVisitor<T> {
    @Override
    public T visitCompilationUnit(CompilationUnitNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitQueenNode(QueenNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitAnnotationElementDeclarationNode(AnnotationElementDeclarationNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitAnnotationTypeBodyNode(AnnotationTypeBodyNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitAnnotationTypeDeclarationNode(AnnotationTypeDeclarationNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitAnnotationTypeMemberDeclarationNode(AnnotationTypeMemberDeclarationNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitClassBodyDeclarationNode(ClassBodyDeclarationNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitClassBodyNode(ClassBodyNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitClassDeclarationNode(ClassDeclarationNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitClassMemberDeclarationNode(ClassMemberDeclarationNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitConstantDeclarationNode(ConstantDeclarationNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitConstructorDeclarationNode(ConstructorDeclarationNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitFieldDeclarationNode(FieldDeclarationNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitImportDeclarationNode(ImportDeclarationNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitInstanceInitializerNode(InstanceInitializerNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitInterfaceBodyNode(InterfaceBodyNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitInterfaceDeclarationNode(InterfaceDeclarationNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitInterfaceMemberDeclarationNode(InterfaceMemberDeclarationNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitInterfaceMethodDeclarationNode(InterfaceMethodDeclarationNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitLocalVariableDeclarationNode(LocalVariableDeclarationNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitMethodDeclarationNode(MethodDeclarationNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitModifierNode(ModifierNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitNormalInterfaceDeclarationNode(NormalInterfaceDeclarationNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitPackageDeclarationNode(PackageDeclarationNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitParameterNode(ParameterNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitTypeDeclarationNode(TypeDeclarationNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitVariableDeclaratorId(VariableDeclaratorId node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitAnnotationNode(AnnotationNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitArrayAccessExpressionNode(ArrayAccessExpressionNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitArrayCreationExpressionNode(ArrayCreationExpressionNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitArrayDimensionNode(ArrayDimensionNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitArrayInitializerExpressionNode(ArrayInitializerExpressionNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitAssignmentExpressionNode(AssignmentExpressionNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitBinaryExpressionNode(BinaryExpressionNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitBooleanLiteralExpressionNode(BooleanLiteralExpressionNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitBracketedExpressionNode(BracketedExpressionNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitCastExpressionNode(CastExpressionNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitCharLiteralExpressionNode(CharLiteralExpressionNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitConditionalExpressionNode(ConditionalExpressionNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitDoubleLiteralExpressionNode(DoubleLiteralExpressionNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitExpressionNode(ExpressionNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitFieldAccessExpressionNode(FieldAccessExpressionNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitInstanceOfExpressionNode(InstanceOfExpressionNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitIntegerLiteralExpressionNode(IntegerLiteralExpressionNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitLambdaExpressionNode(LambdaExpressionNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitLiteralStringValueExpressionNode(LiteralStringValueExpressionNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitLongLiteralExpressionNode(LongLiteralExpressionNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitMarkerAnnotationNode(MarkerAnnotationNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitMethodInvocationExpressionNode(MethodInvocationExpressionNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitMethodReferenceExpressionNode(MethodReferenceExpressionNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitNormalAnnotationNode(NormalAnnotationNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitNullLiteralExpressionNode(NullLiteralExpressionNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitObjectCreationExpressionNode(ObjectCreationExpressionNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitSingleMemberAnnotationNode(SingleMemberAnnotationNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitStringLiteralExpressionNode(StringLiteralExpressionNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitSuperExpressionNode(SuperExpressionNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitThisExpressionNode(ThisExpressionNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitTypeImplementationExpressionNode(TypeImplementationExpressionNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitUnaryExpressionNode(UnaryExpressionNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitAssertStatementNode(AssertStatementNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitBlockStatements(BlockStatements node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitBreakStatementNode(BreakStatementNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitCatchClauseNode(CatchClauseNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitCatchFormalParameterNode(CatchFormalParameterNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitContinueStatementNode(ContinueStatementNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitDoStatementNode(DoStatementNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitEmptyStatementNode(EmptyStatementNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitExplicitConstructorInvocationNode(ExplicitConstructorInvocationNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitExpressionStatementNode(ExpressionStatementNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitForEachStatementNode(ForEachStatementNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitForStatementNode(ForStatementNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitIfStatementNode(IfStatementNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitLabeledStatementNode(LabeledStatementNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitReturnStatementNode(ReturnStatementNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitStatementNode(StatementNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitSwitchEntryNode(SwitchEntryNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitSwitchLabelNode(SwitchLabelNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitSwitchStatementNode(SwitchStatementNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitSynchronizedStatementNode(SynchronizedStatementNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitThrowStatementNode(ThrowStatementNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitTryStatementNode(TryStatementNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitWhileStatementNode(WhileStatementNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitArrayTypeNode(ArrayTypeNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitClassOrInterfaceTypeNode(ClassOrInterfaceTypeNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitExceptionTypeNode(ExceptionTypeNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitNodeWithParameters(NodeWithParameters node) {
        return null;
    }

    @Override
    public T visitNodeWithTypeParameters(NodeWithTypeParameters node) {
        return null;
    }

    @Override
    public T visitNodeWithThrows(NodeWithThrows node) {
        return null;
    }

    @Override
    public T visitNodeWithModifiers(NodeWithModifiers node) {
        return null;
    }

    @Override
    public T visitNodeWithAnnotations(NodeWithAnnotations node) {
        return null;
    }

    @Override
    public T visitPrimitiveTypeNode(PrimitiveTypeNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitReferenceTypeNode(ReferenceTypeNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitTypeNode(TypeNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitTypeParameterNode(TypeParameterNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitVoidTypeNode(VoidTypeNode node) {
        return this.visitChildren(node);
    }

    @Override
    public T visitWildcardTypeNode(WildcardTypeNode node) {
        return this.visitChildren(node);
    }
}
