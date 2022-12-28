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

import java.util.ArrayList;
import java.util.List;

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
    public List<SemanticProblem> visitQueenCompilationUnitNode(QueenCompilationUnitNode node) {
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
    public List<SemanticProblem> visitQueenTypeDeclarationNode(QueenTypeDeclarationNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitQueenAnnotationElementDeclarationNode(QueenAnnotationElementDeclarationNode node) {
        return null;
    }

    @Override
    public List<SemanticProblem> visitQueenAnnotationElementModifierNode(QueenAnnotationElementModifierNode node) {
        return null;
    }

    @Override
    public List<SemanticProblem> visitQueenAnnotationNode(QueenAnnotationNode node) {
        return null;
    }

    @Override
    public List<SemanticProblem> visitQueenAnnotationTypeBodyNode(QueenAnnotationTypeBodyNode node) {
        return null;
    }

    @Override
    public List<SemanticProblem> visitQueenAnnotationTypeDeclarationNode(QueenAnnotationTypeDeclarationNode node) {
        return null;
    }

    @Override
    public List<SemanticProblem> visitQueenAnnotationTypeMemberDeclarationNode(QueenAnnotationTypeMemberDeclarationNode node) {
        return null;
    }

    @Override
    public List<SemanticProblem> visitQueenBlockStatementNode(QueenBlockStatementNode node) {
        return null;
    }

    @Override
    public List<SemanticProblem> visitQueenBlockStatementsNode(QueenBlockStatements node) {
        return null;
    }

    @Override
    public List<SemanticProblem> visitQueenClassAccessModifierNode(QueenClassAccessModifierNode node) {
        return null;
    }

    @Override
    public List<SemanticProblem> visitQueenClassBodyDeclarationNode(QueenClassBodyDeclarationNode node) {
        return null;
    }

    @Override
    public List<SemanticProblem> visitQueenClassBodyNode(QueenClassBodyNode node) {
        return null;
    }

    @Override
    public List<SemanticProblem> visitQueenClassDeclarationNode(QueenClassDeclarationNode node) {
        return null;
    }

    @Override
    public List<SemanticProblem> visitQueenClassExtensionModifierNode(QueenClassExtensionModifierNode node) {
        return null;
    }

    @Override
    public List<SemanticProblem> visitQueenClassMemberDeclarationNode(QueenClassMemberDeclarationNode node) {
        return null;
    }

    @Override
    public List<SemanticProblem> visitQueenConstantDeclarationNode(QueenConstantDeclarationNode node) {
        return null;
    }

    @Override
    public List<SemanticProblem> visitQueenConstantModifierNode(QueenConstantModifierNode node) {
        return null;
    }

    @Override
    public List<SemanticProblem> visitQueenConstructorDeclarationNode(QueenConstructorDeclarationNode node) {
        return null;
    }

    @Override
    public List<SemanticProblem> visitQueenConstructorModifierNode(QueenConstructorModifierNode node) {
        return null;
    }

    @Override
    public List<SemanticProblem> visitQueenExplicitConstructorInvocationNode(QueenExplicitConstructorInvocationNode node) {
        return null;
    }

    @Override
    public List<SemanticProblem> visitQueenExpressionNode(QueenExpressionNode node) {
        return null;
    }

    @Override
    public List<SemanticProblem> visitQueenFieldDeclarationNode(QueenFieldDeclarationNode node) {
        return null;
    }

    @Override
    public List<SemanticProblem> visitQueenFieldModifierNode(QueenFieldModifierNode node) {
        return null;
    }

    @Override
    public List<SemanticProblem> visitQueenImportDeclarationNode(QueenImportDeclarationNode node) {
        return null;
    }

    @Override
    public List<SemanticProblem> visitQueenInitializerExpressionNode(QueenInitializerExpressionNode node) {
        return null;
    }

    @Override
    public List<SemanticProblem> visitQueenInstanceInitializerNode(QueenInstanceInitializerNode node) {
        return null;
    }

    @Override
    public List<SemanticProblem> visitQueenInterfaceBodyNode(QueenInterfaceBodyNode node) {
        return null;
    }

    @Override
    public List<SemanticProblem> visitQueenInterfaceDeclarationNode(QueenInterfaceDeclarationNode node) {
        return null;
    }

    @Override
    public List<SemanticProblem> visitQueenInterfaceMemberDeclarationNode(QueenInterfaceMemberDeclarationNode node) {
        return null;
    }

    @Override
    public List<SemanticProblem> visitQueenInterfaceMethodDeclarationNode(QueenInterfaceMethodDeclarationNode node) {
        return null;
    }

    @Override
    public List<SemanticProblem> visitQueenInterfaceMethodModifierNode(QueenInterfaceMethodModifierNode node) {
        return null;
    }

    @Override
    public List<SemanticProblem> visitQueenInterfaceModifierNode(QueenInterfaceModifierNode node) {
        return null;
    }

    @Override
    public List<SemanticProblem> visitQueenMarkerAnnotationNode(QueenMarkerAnnotationNode node) {
        return null;
    }

    @Override
    public List<SemanticProblem> visitQueenMethodDeclarationNode(QueenMethodDeclarationNode node) {
        return null;
    }

    @Override
    public List<SemanticProblem> visitQueenMethodModifierNode(QueenMethodModifierNode node) {
        return null;
    }

    @Override
    public List<SemanticProblem> visitQueenModifierNode(QueenModifierNode node) {
        return null;
    }

    @Override
    public List<SemanticProblem> visitQueenNode(QueenNode node) {
        return null;
    }

    @Override
    public List<SemanticProblem> visitQueenNormalAnnotationNode(QueenNormalAnnotationNode node) {
        return null;
    }

    @Override
    public List<SemanticProblem> visitQueenNormalInterfaceDeclarationNode(QueenNormalInterfaceDeclarationNode node) {
        return null;
    }

    @Override
    public List<SemanticProblem> visitQueenPackageDeclarationNode(QueenPackageDeclarationNode node) {
        return null;
    }

    @Override
    public List<SemanticProblem> visitQueenParameterModifierNode(QueenParameterModifierNode node) {
        return null;
    }

    @Override
    public List<SemanticProblem> visitQueenParameterNode(QueenParameterNode node) {
        return null;
    }

    @Override
    public List<SemanticProblem> visitQueenSingleMemberAnnotationNode(QueenSingleMemberAnnotationNode node) {
        return null;
    }

    @Override
    public List<SemanticProblem> visitQueenStatementNode(QueenStatementNode node) {
        return null;
    }

    @Override
    public List<SemanticProblem> visitQueenTextExpressionNode(QueenTextExpressionNode node) {
        return null;
    }

    @Override
    public List<SemanticProblem> visitQueenTextStatementNode(QueenTextStatementNode node) {
        return null;
    }
}
