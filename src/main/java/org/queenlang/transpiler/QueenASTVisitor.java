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
import org.queenlang.transpiler.nodes.project.FileNode;
import org.queenlang.transpiler.nodes.project.ProjectNode;
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

    default T visitProject(final ProjectNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }

    default T visitFile(final FileNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }

    default T visitCompilationUnit(final CompilationUnitNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitAnnotationElementDeclarationNode(final AnnotationElementDeclarationNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitAnnotationTypeBodyNode(final AnnotationTypeBodyNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitAnnotationTypeDeclarationNode(final AnnotationTypeDeclarationNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitAnnotationTypeMemberDeclarationNode(final AnnotationTypeMemberDeclarationNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitClassBodyDeclarationNode(final ClassBodyDeclarationNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitClassBodyNode(final ClassBodyNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitClassDeclarationNode(final ClassDeclarationNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitClassMemberDeclarationNode(final ClassMemberDeclarationNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitConstantDeclarationNode(final ConstantDeclarationNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitConstructorDeclarationNode(final ConstructorDeclarationNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitFieldDeclarationNode(final FieldDeclarationNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitImportDeclarationNode(final ImportDeclarationNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitInstanceInitializerNode(final InstanceInitializerNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitInterfaceBodyNode(final InterfaceBodyNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitInterfaceDeclarationNode(final InterfaceDeclarationNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitInterfaceMemberDeclarationNode(final InterfaceMemberDeclarationNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitInterfaceMethodDeclarationNode(final InterfaceMethodDeclarationNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitLocalVariableDeclarationNode(final LocalVariableDeclarationNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitMethodDeclarationNode(final MethodDeclarationNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitModifierNode(final ModifierNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitNormalInterfaceDeclarationNode(final NormalInterfaceDeclarationNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitPackageDeclarationNode(final PackageDeclarationNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitParameterNode(final ParameterNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitTypeDeclarationNode(final TypeDeclarationNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitVariableDeclaratorId(final VariableDeclaratorId node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitAnnotationNode(final AnnotationNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitArrayAccessExpressionNode(final ArrayAccessExpressionNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitArrayCreationExpressionNode(final ArrayCreationExpressionNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitArrayDimensionNode(final ArrayDimensionNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitArrayInitializerExpressionNode(final ArrayInitializerExpressionNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitAssignmentExpressionNode(final AssignmentExpressionNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitBinaryExpressionNode(final BinaryExpressionNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitBooleanLiteralExpressionNode(final BooleanLiteralExpressionNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitBracketedExpressionNode(final BracketedExpressionNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitCastExpressionNode(final CastExpressionNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitCharLiteralExpressionNode(final CharLiteralExpressionNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitConditionalExpressionNode(final ConditionalExpressionNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitDoubleLiteralExpressionNode(final DoubleLiteralExpressionNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitExpressionNode(final ExpressionNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitFieldAccessExpressionNode(final FieldAccessExpressionNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitInstanceOfExpressionNode(final InstanceOfExpressionNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitIntegerLiteralExpressionNode(final IntegerLiteralExpressionNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitLambdaExpressionNode(final LambdaExpressionNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitLiteralStringValueExpressionNode(final LiteralStringValueExpressionNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitLongLiteralExpressionNode(final LongLiteralExpressionNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitMarkerAnnotationNode(final MarkerAnnotationNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitMethodInvocationExpressionNode(final MethodInvocationExpressionNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitMethodReferenceExpressionNode(final MethodReferenceExpressionNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitNormalAnnotationNode(final NormalAnnotationNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitNullLiteralExpressionNode(final NullLiteralExpressionNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitObjectCreationExpressionNode(final ObjectCreationExpressionNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitSingleMemberAnnotationNode(final SingleMemberAnnotationNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitStringLiteralExpressionNode(final StringLiteralExpressionNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitSuperExpressionNode(final SuperExpressionNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitThisExpressionNode(final ThisExpressionNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitTypeImplementationExpressionNode(final TypeImplementationExpressionNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitUnaryExpressionNode(final UnaryExpressionNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitAssertStatementNode(final AssertStatementNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitBlockStatements(final BlockStatements node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitBreakStatementNode(final BreakStatementNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitCatchClauseNode(final CatchClauseNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitCatchFormalParameterNode(final CatchFormalParameterNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitContinueStatementNode(final ContinueStatementNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitDoStatementNode(final DoStatementNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitEmptyStatementNode(final EmptyStatementNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitExplicitConstructorInvocationNode(final ExplicitConstructorInvocationNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitExpressionStatementNode(final ExpressionStatementNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitForEachStatementNode(final ForEachStatementNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitForStatementNode(final ForStatementNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitIfStatementNode(final IfStatementNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitLabeledStatementNode(final LabeledStatementNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitReturnStatementNode(final ReturnStatementNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitStatementNode(final StatementNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitSwitchEntryNode(final SwitchEntryNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitSwitchLabelNode(final SwitchLabelNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitSwitchStatementNode(final SwitchStatementNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitSynchronizedStatementNode(final SynchronizedStatementNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitThrowStatementNode(final ThrowStatementNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitTryStatementNode(final TryStatementNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitWhileStatementNode(final WhileStatementNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitArrayTypeNode(final ArrayTypeNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitClassOrInterfaceTypeNode(final ClassOrInterfaceTypeNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitExceptionTypeNode(final ExceptionTypeNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }

    default T visitPrimitiveTypeNode(final PrimitiveTypeNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitReferenceTypeNode(final ReferenceTypeNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitTypeNode(final TypeNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitTypeParameterNode(final TypeParameterNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitVoidTypeNode(final VoidTypeNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }
    default T visitWildcardTypeNode(final WildcardTypeNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }

    default T visitVariableDeclaratorNode(final VariableDeclaratorNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }

    default T visitElementValuePairNode(final ElementValuePairNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }

    default T visitNameNode(final NameNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }

    default T visitQueenNode(final QueenNode node) {
        if(node != null) {System.out.println("VISITING: " + node.getClass() + " at " + node.position());}
        return this.visitChildren(node);
    }

    T visitNodeWithParameters(final NodeWithParameters node);

    T visitNodeWithTypeParameters(final NodeWithTypeParameters node);

    T visitNodeWithThrows(final NodeWithThrows node);

    T visitNodeWithModifiers(final NodeWithModifiers node);
    T visitNodeWithAnnotations(final NodeWithAnnotations node);

    default T visitChildren(final QueenNode node) {
        T result = defaultResult();
        if(node != null) {
            for (final QueenNode child : node.children()) {
                if(child != null) {
                    T childResult = child.accept(this);
                    result = aggregateResult(result, childResult);
                }
            }
        }
        return result;
    }

    T defaultResult();
    T aggregateResult(T aggregate, T nextResult);
}
