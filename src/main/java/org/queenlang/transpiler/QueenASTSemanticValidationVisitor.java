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
import org.queenlang.transpiler.nodes.expressions.*;
import org.queenlang.transpiler.nodes.statements.QueenBlockStatements;
import org.queenlang.transpiler.nodes.statements.QueenExplicitConstructorInvocationNode;
import org.queenlang.transpiler.nodes.statements.QueenStatementNode;
import org.queenlang.transpiler.nodes.statements.QueenTextStatementNode;
import org.queenlang.transpiler.nodes.types.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * AST Visitor for semantix/contextual validation of Queen code.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 * @todo #33:60min Continue implementing semantic validation for Queen.
 * @todo #33:60min Continue implementing AST nodes such as Statements and expressions.
 */
public final class QueenASTSemanticValidationVisitor implements QueenASTVisitor<List<SemanticProblem>>{

    /**
     * Name of the Queen file.
     */
    private final String fileName;

    public QueenASTSemanticValidationVisitor(final String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<SemanticProblem> visitQueenCompilationUnitNode(final QueenCompilationUnitNode node) {
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
            this.visitQueenTypeDeclarationNode(node.typeDeclaration())
        );
        return problems;
    }

    @Override
    public List<SemanticProblem> visitQueenTypeDeclarationNode(final TypeDeclarationNode node) {
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
        if(node instanceof QueenClassDeclarationNode) {
            problems.addAll(
                this.visitQueenClassDeclarationNode(
                    (QueenClassDeclarationNode) node
                )
            );
        } else if(node instanceof InterfaceDeclarationNode) {
            problems.addAll(
                this.visitQueenInterfaceDeclarationNode(
                    (InterfaceDeclarationNode) node
                )
            );
        }
        return problems;
    }

    @Override
    public List<SemanticProblem> visitQueenTypeParameterNode(final QueenTypeParameterNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitQueenNodeWithTypeParameters(final QueenNodeWithTypeParameters node) {
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
    public List<SemanticProblem> visitQueenTypeNode(final QueenTypeNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitQueenReferenceTypeNode(final QueenReferenceTypeNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitQueenClassOrInterfaceTypeNode(final QueenClassOrInterfaceTypeNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitQueenWildcardNode(final QueenWildcardNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitQueenAnnotationElementDeclarationNode(final QueenAnnotationElementDeclarationNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitQueenAnnotationNode(final QueenAnnotationNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitQueenAnnotationTypeBodyNode(final QueenAnnotationTypeBodyNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitQueenAnnotationTypeDeclarationNode(final QueenAnnotationTypeDeclarationNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitQueenAnnotationTypeMemberDeclarationNode(final AnnotationTypeMemberDeclarationNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitQueenBlockStatementsNode(final QueenBlockStatements node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitQueenClassBodyDeclarationNode(final ClassBodyDeclarationNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitQueenClassBodyNode(final QueenClassBodyNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitQueenClassDeclarationNode(final QueenClassDeclarationNode node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        problems.addAll(this.visitQueenNodeWithTypeParameters(node));

        return problems;
    }

    @Override
    public List<SemanticProblem> visitQueenClassMemberDeclarationNode(final ClassMemberDeclarationNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitQueenConstantDeclarationNode(final QueenConstantDeclarationNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitQueenConstructorDeclarationNode(final QueenConstructorDeclarationNode node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        problems.addAll(this.visitQueenNodeWithTypeParameters(node));

        return problems;
    }

    @Override
    public List<SemanticProblem> visitQueenExplicitConstructorInvocationNode(final QueenExplicitConstructorInvocationNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitQueenExpressionNode(final ExpressionNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitQueenFieldDeclarationNode(final QueenFieldDeclarationNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitQueenImportDeclarationNode(final QueenImportDeclarationNode node) {
        return new ArrayList<>();
    }


    @Override
    public List<SemanticProblem> visitQueenInstanceInitializerNode(final QueenInstanceInitializerNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitQueenInterfaceBodyNode(final QueenInterfaceBodyNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitQueenInterfaceDeclarationNode(final InterfaceDeclarationNode node) {
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
        if(node instanceof QueenNormalInterfaceDeclarationNode) {
            problems.addAll(
                this.visitQueenNormalInterfaceDeclarationNode(
                    (QueenNormalInterfaceDeclarationNode) node
                )
            );
        } else if(node instanceof QueenAnnotationTypeDeclarationNode) {
            problems.addAll(
                this.visitQueenAnnotationTypeDeclarationNode(
                    (QueenAnnotationTypeDeclarationNode) node
                )
            );
        }
        return problems;
    }

    @Override
    public List<SemanticProblem> visitQueenInterfaceMemberDeclarationNode(final InterfaceMemberDeclarationNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitQueenInterfaceMethodDeclarationNode(final QueenInterfaceMethodDeclarationNode node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        problems.addAll(this.visitQueenNodeWithTypeParameters(node));

        return problems;
    }

    @Override
    public List<SemanticProblem> visitQueenMarkerAnnotationNode(final QueenMarkerAnnotationNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitQueenMethodDeclarationNode(final QueenMethodDeclarationNode node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        problems.addAll(this.visitQueenNodeWithTypeParameters(node));

        return problems;
    }

    @Override
    public List<SemanticProblem> visitQueenModifierNode(final QueenModifierNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitQueenNode(final QueenNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitQueenNormalAnnotationNode(final QueenNormalAnnotationNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitQueenNormalInterfaceDeclarationNode(final QueenNormalInterfaceDeclarationNode node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        problems.addAll(this.visitQueenNodeWithTypeParameters(node));

        return problems;
    }

    @Override
    public List<SemanticProblem> visitQueenPackageDeclarationNode(final QueenPackageDeclarationNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitQueenParameterNode(final QueenParameterNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitQueenSingleMemberAnnotationNode(final QueenSingleMemberAnnotationNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitQueenStatementNode(final QueenStatementNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitQueenTextExpressionNode(final QueenTextExpressionNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitQueenTextStatementNode(final QueenTextStatementNode node) {
        return new ArrayList<>();
    }
}
