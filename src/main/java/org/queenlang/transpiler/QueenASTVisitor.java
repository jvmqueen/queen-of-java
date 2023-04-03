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

import org.queenlang.transpiler.nodes.NameNode;
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
    default T visitCompilationUnit(final CompilationUnitNode node) {
        return this.visitChildren(node);
    }
    default T visitAnnotationElementDeclarationNode(final AnnotationElementDeclarationNode node) {
        return this.visitChildren(node);
    }
    default T visitAnnotationTypeBodyNode(final AnnotationTypeBodyNode node) {
        return this.visitChildren(node);
    }
    default T visitAnnotationTypeDeclarationNode(final AnnotationTypeDeclarationNode node) {
        return this.visitChildren(node);
    }
    default T visitAnnotationTypeMemberDeclarationNode(final AnnotationTypeMemberDeclarationNode node) {
        return this.visitChildren(node);
    }
    default T visitClassBodyDeclarationNode(final ClassBodyDeclarationNode node) {
        return this.visitChildren(node);
    }
    default T visitClassBodyNode(final ClassBodyNode node) {
        return this.visitChildren(node);
    }
    default T visitClassDeclarationNode(final ClassDeclarationNode node) {
        return this.visitChildren(node);
    }
    default T visitClassMemberDeclarationNode(final ClassMemberDeclarationNode node) {
        return this.visitChildren(node);
    }
    default T visitConstantDeclarationNode(final ConstantDeclarationNode node) {
        return this.visitChildren(node);
    }
    default T visitConstructorDeclarationNode(final ConstructorDeclarationNode node) {
        return this.visitChildren(node);
    }
    default T visitFieldDeclarationNode(final FieldDeclarationNode node) {
        return this.visitChildren(node);
    }
    default T visitImportDeclarationNode(final ImportDeclarationNode node) {
        return this.visitChildren(node);
    }
    default T visitInstanceInitializerNode(final InstanceInitializerNode node) {
        return this.visitChildren(node);
    }
    default T visitInterfaceBodyNode(final InterfaceBodyNode node) {
        return this.visitChildren(node);
    }
    default T visitInterfaceDeclarationNode(final InterfaceDeclarationNode node) {
        return this.visitChildren(node);
    }
    default T visitInterfaceMemberDeclarationNode(final InterfaceMemberDeclarationNode node) {
        return this.visitChildren(node);
    }
    default T visitInterfaceMethodDeclarationNode(final InterfaceMethodDeclarationNode node) {
        return this.visitChildren(node);
    }
    default T visitLocalVariableDeclarationNode(final LocalVariableDeclarationNode node) {
        return this.visitChildren(node);
    }
    default T visitMethodDeclarationNode(final MethodDeclarationNode node) {
        return this.visitChildren(node);
    }
    default T visitModifierNode(final ModifierNode node) {
        return this.visitChildren(node);
    }
    default T visitNormalInterfaceDeclarationNode(final NormalInterfaceDeclarationNode node) {
        return this.visitChildren(node);
    }
    default T visitPackageDeclarationNode(final PackageDeclarationNode node) {
        return this.visitChildren(node);
    }
    default T visitParameterNode(final ParameterNode node) {
        return this.visitChildren(node);
    }
    default T visitTypeDeclarationNode(final TypeDeclarationNode node) {
        return this.visitChildren(node);
    }
    default T visitVariableDeclaratorId(final VariableDeclaratorId node) {
        return this.visitChildren(node);
    }
    default T visitAnnotationNode(final AnnotationNode node) {
        return this.visitChildren(node);
    }
    default T visitArrayAccessExpressionNode(final ArrayAccessExpressionNode node) {
        return this.visitChildren(node);
    }
    default T visitArrayCreationExpressionNode(final ArrayCreationExpressionNode node) {
        return this.visitChildren(node);
    }
    default T visitArrayDimensionNode(final ArrayDimensionNode node) {
        return this.visitChildren(node);
    }
    default T visitArrayInitializerExpressionNode(final ArrayInitializerExpressionNode node) {
        return this.visitChildren(node);
    }
    default T visitAssignmentExpressionNode(final AssignmentExpressionNode node) {
        return this.visitChildren(node);
    }
    default T visitBinaryExpressionNode(final BinaryExpressionNode node) {
        return this.visitChildren(node);
    }
    default T visitBooleanLiteralExpressionNode(final BooleanLiteralExpressionNode node) {
        return this.visitChildren(node);
    }
    default T visitBracketedExpressionNode(final BracketedExpressionNode node) {
        return this.visitChildren(node);
    }
    default T visitCastExpressionNode(final CastExpressionNode node) {
        return this.visitChildren(node);
    }
    default T visitCharLiteralExpressionNode(final CharLiteralExpressionNode node) {
        return this.visitChildren(node);
    }
    default T visitConditionalExpressionNode(final ConditionalExpressionNode node) {
        return this.visitChildren(node);
    }
    default T visitDoubleLiteralExpressionNode(final DoubleLiteralExpressionNode node) {
        return this.visitChildren(node);
    }
    default T visitExpressionNode(final ExpressionNode node) {
        return this.visitChildren(node);
    }
    default T visitFieldAccessExpressionNode(final FieldAccessExpressionNode node) {
        return this.visitChildren(node);
    }
    default T visitInstanceOfExpressionNode(final InstanceOfExpressionNode node) {
        return this.visitChildren(node);
    }
    default T visitIntegerLiteralExpressionNode(final IntegerLiteralExpressionNode node) {
        return this.visitChildren(node);
    }
    default T visitLambdaExpressionNode(final LambdaExpressionNode node) {
        return this.visitChildren(node);
    }
    default T visitLiteralStringValueExpressionNode(final LiteralStringValueExpressionNode node) {
        return this.visitChildren(node);
    }
    default T visitLongLiteralExpressionNode(final LongLiteralExpressionNode node) {
        return this.visitChildren(node);
    }
    default T visitMarkerAnnotationNode(final MarkerAnnotationNode node) {
        return this.visitChildren(node);
    }
    default T visitMethodInvocationExpressionNode(final MethodInvocationExpressionNode node) {
        return this.visitChildren(node);
    }
    default T visitMethodReferenceExpressionNode(final MethodReferenceExpressionNode node) {
        return this.visitChildren(node);
    }
    default T visitNormalAnnotationNode(final NormalAnnotationNode node) {
        return this.visitChildren(node);
    }
    default T visitNullLiteralExpressionNode(final NullLiteralExpressionNode node) {
        return this.visitChildren(node);
    }
    default T visitObjectCreationExpressionNode(final ObjectCreationExpressionNode node) {
        return this.visitChildren(node);
    }
    default T visitSingleMemberAnnotationNode(final SingleMemberAnnotationNode node) {
        return this.visitChildren(node);
    }
    default T visitStringLiteralExpressionNode(final StringLiteralExpressionNode node) {
        return this.visitChildren(node);
    }
    default T visitSuperExpressionNode(final SuperExpressionNode node) {
        return this.visitChildren(node);
    }
    default T visitThisExpressionNode(final ThisExpressionNode node) {
        return this.visitChildren(node);
    }
    default T visitTypeImplementationExpressionNode(final TypeImplementationExpressionNode node) {
        return this.visitChildren(node);
    }
    default T visitUnaryExpressionNode(final UnaryExpressionNode node) {
        return this.visitChildren(node);
    }
    default T visitAssertStatementNode(final AssertStatementNode node) {
        return this.visitChildren(node);
    }
    default T visitBlockStatements(final BlockStatements node) {
        return this.visitChildren(node);
    }
    default T visitBreakStatementNode(final BreakStatementNode node) {
        return this.visitChildren(node);
    }
    default T visitCatchClauseNode(final CatchClauseNode node) {
        return this.visitChildren(node);
    }
    default T visitCatchFormalParameterNode(final CatchFormalParameterNode node) {
        return this.visitChildren(node);
    }
    default T visitContinueStatementNode(final ContinueStatementNode node) {
        return this.visitChildren(node);
    }
    default T visitDoStatementNode(final DoStatementNode node) {
        return this.visitChildren(node);
    }
    default T visitEmptyStatementNode(final EmptyStatementNode node) {
        return this.visitChildren(node);
    }
    default T visitExplicitConstructorInvocationNode(final ExplicitConstructorInvocationNode node) {
        return this.visitChildren(node);
    }
    default T visitExpressionStatementNode(final ExpressionStatementNode node) {
        return this.visitChildren(node);
    }
    default T visitForEachStatementNode(final ForEachStatementNode node) {
        return this.visitChildren(node);
    }
    default T visitForStatementNode(final ForStatementNode node) {
        return this.visitChildren(node);
    }
    default T visitIfStatementNode(final IfStatementNode node) {
        return this.visitChildren(node);
    }
    default T visitLabeledStatementNode(final LabeledStatementNode node) {
        return this.visitChildren(node);
    }
    default T visitReturnStatementNode(final ReturnStatementNode node) {
        return this.visitChildren(node);
    }
    default T visitStatementNode(final StatementNode node) {
        return this.visitChildren(node);
    }
    default T visitSwitchEntryNode(final SwitchEntryNode node) {
        return this.visitChildren(node);
    }
    default T visitSwitchLabelNode(final SwitchLabelNode node) {
        return this.visitChildren(node);
    }
    default T visitSwitchStatementNode(final SwitchStatementNode node) {
        return this.visitChildren(node);
    }
    default T visitSynchronizedStatementNode(final SynchronizedStatementNode node) {
        return this.visitChildren(node);
    }
    default T visitThrowStatementNode(final ThrowStatementNode node) {
        return this.visitChildren(node);
    }
    default T visitTryStatementNode(final TryStatementNode node) {
        return this.visitChildren(node);
    }
    default T visitWhileStatementNode(final WhileStatementNode node) {
        return this.visitChildren(node);
    }
    default T visitArrayTypeNode(final ArrayTypeNode node) {
        return this.visitChildren(node);
    }
    default T visitClassOrInterfaceTypeNode(final ClassOrInterfaceTypeNode node) {
        return this.visitChildren(node);
    }
    default T visitExceptionTypeNode(final ExceptionTypeNode node) {
        return this.visitChildren(node);
    }

    default T visitPrimitiveTypeNode(final PrimitiveTypeNode node) {
        return this.visitChildren(node);
    }
    default T visitReferenceTypeNode(final ReferenceTypeNode node) {
        return this.visitChildren(node);
    }
    default T visitTypeNode(final TypeNode node) {
        return this.visitChildren(node);
    }
    default T visitTypeParameterNode(final TypeParameterNode node) {
        return this.visitChildren(node);
    }
    default T visitVoidTypeNode(final VoidTypeNode node) {
        return this.visitChildren(node);
    }
    default T visitWildcardTypeNode(final WildcardTypeNode node) {
        return this.visitChildren(node);
    }

    default T visitVariableDeclaratorNode(final VariableDeclaratorNode node) {
        return this.visitChildren(node);
    }
    default T visitNameNode(final NameNode node) {
        return this.visitChildren(node);
    }

    default T visitQueenNode(final QueenNode node) {
        return this.visitChildren(node);
    }

    T visitNodeWithParameters(final NodeWithParameters node);

    T visitNodeWithTypeParameters(final NodeWithTypeParameters node);

    T visitNodeWithThrows(final NodeWithThrows node);

    T visitNodeWithModifiers(final NodeWithModifiers node);
    T visitNodeWithAnnotations(final NodeWithAnnotations node);

    default T visitChildren(final QueenNode node) {
        T result = defaultResult();
        for (final QueenNode child : node.children()) {
            if(child != null) {
                T childResult = child.accept(this);
                result = aggregateResult(result, childResult);
            }
        }
        return result;
    }

    T defaultResult();
    T aggregateResult(T aggregate, T nextResult);
}
