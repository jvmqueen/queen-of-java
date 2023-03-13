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

import org.checkerframework.checker.units.qual.A;
import org.queenlang.transpiler.nodes.QueenNode;
import org.queenlang.transpiler.nodes.body.*;
import org.queenlang.transpiler.nodes.expressions.*;
import org.queenlang.transpiler.nodes.statements.*;
import org.queenlang.transpiler.nodes.types.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * AST Visitor for semantic/contextual validation of Queen code.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 * @todo #64:60min Continue implementing semantic validation for Queen.
 */
public final class QueenASTSemanticValidationVisitor implements QueenASTVisitor<List<SemanticProblem>> {

    /**
     * Name of the Queen file.
     */
    private final String fileName;

    public QueenASTSemanticValidationVisitor(final String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<SemanticProblem> visitCompilationUnit(CompilationUnitNode node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        if(!node.typeDeclaration().name().equals(this.fileName) && !(node.typeDeclaration().name() + ".queen").equals(this.fileName)) {
            problems.add(
                new QueenSemanticError(
                    "Declared type (" + node.typeDeclaration().name() + ") does not match the file's name (" + this.fileName + "). ",
                    node.typeDeclaration().position()
                )
            );
        }
        final List<QueenImportDeclarationNode> imports = node.importDeclarations();
        for(int i=imports.size() - 1; i>=0; i--) {
            for(int j=0; j<imports.size(); j++) {
                if(i!= j && imports.get(i).isContainedBy(imports.get(j))) {
                    problems.add(
                        new QueenSemanticWarning(
                            "Type already imported. ",
                            imports.get(i).position()
                        )
                    );
                }
            }
        }
        problems.addAll(
            this.visitTypeDeclarationNode(node.typeDeclaration())
        );
        return problems;
    }

    @Override
    public List<SemanticProblem> visitAnnotationElementDeclarationNode(AnnotationElementDeclarationNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitAnnotationTypeBodyNode(AnnotationTypeBodyNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitAnnotationTypeDeclarationNode(AnnotationTypeDeclarationNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitAnnotationTypeMemberDeclarationNode(AnnotationTypeMemberDeclarationNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitClassBodyDeclarationNode(ClassBodyDeclarationNode node) {
        if(node instanceof ClassMemberDeclarationNode) {
            return this.visitClassMemberDeclarationNode((ClassMemberDeclarationNode) node);
        } else if(node instanceof InstanceInitializerNode) {
            return this.visitInstanceInitializerNode((InstanceInitializerNode) node);
        } else if(node instanceof ConstructorDeclarationNode) {
            return this.visitConstructorDeclarationNode((ConstructorDeclarationNode) node);
        }
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitClassBodyNode(ClassBodyNode node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        for(final ClassBodyDeclarationNode cbd : node.classBodyDeclarations()) {
            problems.addAll(this.visitClassBodyDeclarationNode(cbd));
        }
        return problems;
    }

    @Override
    public List<SemanticProblem> visitClassDeclarationNode(ClassDeclarationNode node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        problems.addAll(this.visitNodeWithTypeParameters(node));

        final List<QueenClassOrInterfaceTypeNode> interfaces = node.of();
        final Set<String> unique = new HashSet<>();
        for(final QueenClassOrInterfaceTypeNode implementedInterface : interfaces) {
            final String fullName = implementedInterface.fullName();
            if(!unique.add(fullName)) {
                problems.add(
                    new QueenSemanticError(
                        "Interface '" + fullName + "' already present in of list.",
                        implementedInterface.position()
                    )
                );
            }
        }

        problems.addAll(this.visitClassBodyNode(node.body()));
        return problems;
    }

    @Override
    public List<SemanticProblem> visitClassMemberDeclarationNode(ClassMemberDeclarationNode node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        if(node instanceof FieldDeclarationNode) {
            problems.addAll(this.visitFieldDeclarationNode((FieldDeclarationNode) node));
        } else if(node instanceof MethodDeclarationNode) {
            problems.addAll(this.visitMethodDeclarationNode((MethodDeclarationNode) node));
        } else if(node instanceof ClassDeclarationNode) {
            problems.addAll(this.visitClassDeclarationNode((ClassDeclarationNode) node));
        } else if(node instanceof InterfaceDeclarationNode) {
            problems.addAll(this.visitInterfaceDeclarationNode((InterfaceDeclarationNode) node));
        }
        return problems;
    }

    @Override
    public List<SemanticProblem> visitConstantDeclarationNode(ConstantDeclarationNode node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        final Set<String> unique = new HashSet<>();
        node.modifiers().forEach(m -> {
            final String modifierString = m.modifier();
            if(!unique.add(modifierString)) {
                problems.add(
                    new QueenSemanticError(
                        "Modifier '" + modifierString + "' already present.",
                        m.position()
                    )
                );
            }
        });
        return problems;
    }

    @Override
    public List<SemanticProblem> visitConstructorDeclarationNode(ConstructorDeclarationNode node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        problems.addAll(this.visitNodeWithParameters(node));
        problems.addAll(this.visitNodeWithTypeParameters(node));
        problems.addAll(this.visitNodeWithThrows(node));

        return problems;
    }

    @Override
    public List<SemanticProblem> visitFieldDeclarationNode(FieldDeclarationNode node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        final Set<String> unique = new HashSet<>();
        node.modifiers().forEach(m -> {
            final String modifierString = m.modifier();
            if(!unique.add(modifierString)) {
                problems.add(
                    new QueenSemanticError(
                        "Modifier '" + modifierString + "' already present.",
                        m.position()
                    )
                );
            }
        });
        return problems;
    }

    @Override
    public List<SemanticProblem> visitImportDeclarationNode(ImportDeclarationNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitInstanceInitializerNode(InstanceInitializerNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitInterfaceBodyNode(InterfaceBodyNode node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        for(final InterfaceMemberDeclarationNode imdn : node.interfaceMemberDeclarations()) {
            problems.addAll(this.visitInterfaceMemberDeclarationNode(imdn));
        }
        return problems;
    }

    @Override
    public List<SemanticProblem> visitInterfaceDeclarationNode(InterfaceDeclarationNode node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        final List<QueenModifierNode> modifiers = node.modifiers();
        modifiers.forEach(
            m -> {
                final String modifierString = m.modifier();
                if("abstract".equalsIgnoreCase(modifierString)) {
                    problems.add(
                        new QueenSemanticWarning(
                            "Modifier '" + modifierString + "' is redundant here.",
                            m.position()
                        )
                    );
                }
            }
        );
        if(node instanceof NormalInterfaceDeclarationNode) {
            problems.addAll(
                this.visitNormalInterfaceDeclarationNode(
                    (NormalInterfaceDeclarationNode) node
                )
            );
        } else if(node instanceof AnnotationTypeDeclarationNode) {
            problems.addAll(
                this.visitAnnotationTypeDeclarationNode(
                    (AnnotationTypeDeclarationNode) node
                )
            );
        }
        return problems;
    }

    @Override
    public List<SemanticProblem> visitInterfaceMemberDeclarationNode(InterfaceMemberDeclarationNode node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        if(node instanceof ConstantDeclarationNode) {
            problems.addAll(this.visitConstantDeclarationNode((ConstantDeclarationNode) node));
        } else if(node instanceof InterfaceMethodDeclarationNode) {
            problems.addAll(this.visitInterfaceMethodDeclarationNode((InterfaceMethodDeclarationNode) node));
        } else if(node instanceof ClassDeclarationNode) {
            problems.addAll(this.visitClassDeclarationNode((ClassDeclarationNode) node));
        } else if(node instanceof InterfaceDeclarationNode) {
            problems.addAll(this.visitInterfaceDeclarationNode((InterfaceDeclarationNode) node));
        }
        return problems;
    }

    @Override
    public List<SemanticProblem> visitInterfaceMethodDeclarationNode(InterfaceMethodDeclarationNode node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        problems.addAll(this.visitNodeWithParameters(node));
        problems.addAll(this.visitNodeWithTypeParameters(node));
        problems.addAll(this.visitNodeWithThrows(node));

        return problems;
    }

    @Override
    public List<SemanticProblem> visitLocalVariableDeclarationNode(LocalVariableDeclarationNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitMethodDeclarationNode(MethodDeclarationNode node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        problems.addAll(this.visitNodeWithParameters(node));
        problems.addAll(this.visitNodeWithTypeParameters(node));
        problems.addAll(this.visitNodeWithThrows(node));

        return problems;
    }

    @Override
    public List<SemanticProblem> visitModifierNode(ModifierNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitNormalInterfaceDeclarationNode(NormalInterfaceDeclarationNode node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        problems.addAll(this.visitNodeWithTypeParameters(node));
        problems.addAll(this.visitInterfaceBodyNode(node.body()));

        final List<QueenClassOrInterfaceTypeNode> extendsInterfaces = node.extendsTypes();
        final Set<String> unique = new HashSet<>();
        for(final QueenClassOrInterfaceTypeNode extendsInterface : extendsInterfaces) {
            final String fullName = extendsInterface.fullName();
            if(!unique.add(fullName)) {
                problems.add(
                    new QueenSemanticError(
                        "Interface '" + fullName + "' already present in extends list.",
                        extendsInterface.position()
                    )
                );
            }
        }

        return problems;
    }

    @Override
    public List<SemanticProblem> visitPackageDeclarationNode(PackageDeclarationNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitParameterNode(ParameterNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitTypeDeclarationNode(TypeDeclarationNode node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        final List<QueenModifierNode> modifiers = node.modifiers();
        if(modifiers != null && modifiers.size() > 0) {
            final List<String> allowedModifiers = List.of("public", "abstract", "strictfp");
            final Set<QueenModifierNode> unique = new HashSet<>();
            modifiers.forEach(m -> {
                final String modifierString = m.modifier();
                if(!allowedModifiers.contains(modifierString)) {
                    problems.add(
                        new QueenSemanticError(
                            "Modifier '" + modifierString + "' not allowed here.",
                            m.position()
                        )
                    );
                } else {
                    final boolean added = unique.add(m);
                    if(!added) {
                        problems.add(
                            new QueenSemanticError(
                                "Modifier '" + modifierString + "' already present.",
                                m.position()
                            )
                        );
                    }
                }
            });
        }
        if(node instanceof ClassDeclarationNode) {
            problems.addAll(
                this.visitClassDeclarationNode(
                    (ClassDeclarationNode) node
                )
            );
        } else if(node instanceof InterfaceDeclarationNode) {
            problems.addAll(
                this.visitInterfaceDeclarationNode(
                    (InterfaceDeclarationNode) node
                )
            );
        }
        return problems;
    }

    @Override
    public List<SemanticProblem> visitVariableDeclaratorId(VariableDeclaratorId node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitAnnotationNode(AnnotationNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitArrayAccessExpressionNode(ArrayAccessExpressionNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitArrayCreationExpressionNode(ArrayCreationExpressionNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitArrayDimensionNode(ArrayDimensionNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitArrayInitializerExpressionNode(ArrayInitializerExpressionNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitAssignmentExpressionNode(AssignmentExpressionNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitBinaryExpressionNode(BinaryExpressionNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitBooleanLiteralExpressionNode(BooleanLiteralExpressionNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitBracketedExpressionNode(BracketedExpressionNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitCastExpressionNode(CastExpressionNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitCharLiteralExpressionNode(CharLiteralExpressionNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitConditionalExpressionNode(ConditionalExpressionNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitDoubleLiteralExpressionNode(DoubleLiteralExpressionNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitExpressionNode(ExpressionNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitFieldAccessExpressionNode(FieldAccessExpressionNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitInstanceOfExpressionNode(InstanceOfExpressionNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitIntegerLiteralExpressionNode(IntegerLiteralExpressionNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitLambdaExpressionNode(LambdaExpressionNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitLiteralStringValueExpressionNode(LiteralStringValueExpressionNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitLongLiteralExpressionNode(LongLiteralExpressionNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitMarkerAnnotationNode(MarkerAnnotationNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitMethodInvocationExpressionNode(MethodInvocationExpressionNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitMethodReferenceExpressionNode(MethodReferenceExpressionNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitNormalAnnotationNode(NormalAnnotationNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitNullLiteralExpressionNode(NullLiteralExpressionNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitObjectCreationExpressionNode(ObjectCreationExpressionNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitSingleMemberAnnotationNode(SingleMemberAnnotationNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitStringLiteralExpressionNode(StringLiteralExpressionNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitSuperExpressionNode(SuperExpressionNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitThisExpressionNode(ThisExpressionNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitTypeImplementationExpressionNode(TypeImplementationExpressionNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitUnaryExpressionNode(UnaryExpressionNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitAssertStatementNode(AssertStatementNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitBlockStatements(BlockStatements node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitBreakStatementNode(BreakStatementNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitCatchClauseNode(CatchClauseNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitCatchFormalParameterNode(CatchFormalParameterNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitContinueStatementNode(ContinueStatementNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitDoStatementNode(DoStatementNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitEmptyStatementNode(EmptyStatementNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitExplicitConstructorInvocationNode(ExplicitConstructorInvocationNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitExpressionStatementNode(ExpressionStatementNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitForEachStatementNode(ForEachStatementNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitForStatementNode(ForStatementNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitIfStatementNode(IfStatementNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitLabeledStatementNode(LabeledStatementNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitReturnStatementNode(ReturnStatementNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitStatementNode(StatementNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitSwitchEntryNode(SwitchEntryNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitSwitchLabelNode(SwitchLabelNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitSwitchStatementNode(SwitchStatementNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitSynchronizedStatementNode(SynchronizedStatementNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitThrowStatementNode(ThrowStatementNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitTryStatementNode(TryStatementNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitWhileStatementNode(WhileStatementNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitArrayTypeNode(ArrayTypeNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitClassOrInterfaceTypeNode(ClassOrInterfaceTypeNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitExceptionTypeNode(ExceptionTypeNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitNodeWithParameters(final NodeWithParameters node) {
        final List<SemanticProblem> problems = new ArrayList<>();

        final List<QueenParameterNode> parameters = node.parameters();
        final Set<String> unique = new HashSet<>();
        for(final QueenParameterNode parameter : parameters) {
            final String name = parameter.variableDeclaratorId().name();
            if(!unique.add(name)) {
                problems.add(
                    new QueenSemanticError(
                        "Parameter '" + name + "' already present.",
                        parameter.position()
                    )
                );
            }
            if(parameter.modifiers() != null && parameter.modifiers().size() > 1) {
                for(int i = 1; i< parameter.modifiers().size(); i++) {
                    problems.add(
                        new QueenSemanticError(
                            "Modifier '" + parameter.modifiers().get(i).modifier() + "' already present.",
                            parameter.modifiers().get(i).position()
                        )
                    );
                }
            }
        }

        return problems;
    }

    @Override
    public List<SemanticProblem> visitNodeWithTypeParameters(final NodeWithTypeParameters node) {
        final List<SemanticProblem> problems = new ArrayList<>();

        final List<QueenTypeParameterNode> typeParameters = node.typeParameters();
        if(typeParameters != null && typeParameters.size() > 0) {
            final Set<String> unique = new HashSet<>();
            typeParameters.forEach(
                tp -> {
                    if(!unique.add(tp.name())) {
                        problems.add(
                            new QueenSemanticError(
                                "Type parameter '" + tp.name() + "' already present.",
                                tp.position()
                            )
                        );
                    }
                }
            );
        }
        //@todo #49:60min Validate boundTypes of each type parameter.
        return problems;
    }

    @Override
    public List<SemanticProblem> visitNodeWithThrows(final NodeWithThrows node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        final List<QueenExceptionTypeNode> exceptions = node.throwsList();

        final Set<String> unique = new HashSet<>();
        for(final QueenExceptionTypeNode exception : exceptions) {
            final String fullName = exception.exceptionType().fullName();
            if(!unique.add(fullName)) {
                problems.add(
                    new QueenSemanticError(
                        "Exception '" + fullName + "' already present in throws list.",
                        exception.position()
                    )
                );
            }
        }

        return problems;
    }

    @Override
    public List<SemanticProblem> visitPrimitiveTypeNode(PrimitiveTypeNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitReferenceTypeNode(ReferenceTypeNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitTypeNode(TypeNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitTypeParameterNode(TypeParameterNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitVoidTypeNode(VoidTypeNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitWildcardTypeNode(WildcardTypeNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitQueenNode(QueenNode node) {
        return new ArrayList<>();
    }
}
