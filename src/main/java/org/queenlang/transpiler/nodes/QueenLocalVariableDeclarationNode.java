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
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Queen LocalVariableDeclaration AST Node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenLocalVariableDeclarationNode implements QueenBlockStatementNode, QueenExpressionNode{

    /**
     * Position in the original source code.
     */
    private final Position position;

    /**
     * Annotations on top of this local variable.
     */
    private final List<QueenAnnotationNode> annotations;

    /**
     * Access modifiers of this local variable.
     */
    private final List<QueenModifierNode> modifiers;

    /**
     * Type of the local variable.
     */
    private final QueenTypeNode type;

    /**
     * Variable names and initializer expressions.
     */
    private final Map<String, QueenExpressionNode> variables;

    public QueenLocalVariableDeclarationNode(
        final Position position,
        final List<QueenAnnotationNode> annotations,
        final List<QueenModifierNode> modifiers,
        final QueenTypeNode type,
        final Map<String, QueenExpressionNode> variables
    ) {
        this.position = position;
        this.annotations = annotations;
        this.modifiers = modifiers;
        this.type = type;
        this.variables = variables;
    }

    @Override
    public void addToJavaNode(Node java) {
        ((BlockStmt) java).addStatement(this.toJavaStatement());
    }

    /**
     * Turn it into a JavaParser Statement.
     * @return Statement, never null.
     */
    public Statement toJavaStatement() {
        return new ExpressionStmt(this.toJavaExpression());
    }

    @Override
    public Position position() {
        return this.position;
    }

    @Override
    public Expression toJavaExpression() {
        VariableDeclarationExpr vde = new VariableDeclarationExpr();
        this.annotations.forEach(a -> a.addToJavaNode(vde));
        this.modifiers.forEach(m -> m.addToJavaNode(vde));

        final List<VariableDeclarator> variableDeclarators = new ArrayList<>();
        this.variables.entrySet().forEach(
            vn -> {
                final VariableDeclarator vd = new VariableDeclarator();
                this.type.addToJavaNode(vd);
                vd.setName(vn.getKey());
                if(vn.getValue() != null) {
                    vn.getValue().addToJavaNode(vd);
                }
                variableDeclarators.add(vd);
            }
        );

        vde.setVariables(new NodeList<>(variableDeclarators));
        return vde;
    }
}
