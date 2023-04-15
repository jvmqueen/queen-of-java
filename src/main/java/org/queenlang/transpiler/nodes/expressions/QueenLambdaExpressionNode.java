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
package org.queenlang.transpiler.nodes.expressions;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.QueenNode;
import org.queenlang.transpiler.nodes.body.ParameterNode;
import org.queenlang.transpiler.nodes.statements.BlockStatements;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Queen Lambda Expression, AST Node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenLambdaExpressionNode implements LambdaExpressionNode {

    private final Position position;
    private final boolean enclosedParameters;
    private final List<ParameterNode> parameters;
    private final ExpressionNode expression;
    private final BlockStatements blockStatements;

    public QueenLambdaExpressionNode(
        final Position position,
        final boolean enclosedParameters,
        final List<ParameterNode> parameters,
        final ExpressionNode expression,
        final BlockStatements blockStatements
    ) {
        this.position = position;
        this.enclosedParameters = enclosedParameters;
        this.parameters = parameters != null ? parameters.stream().map(
            p -> (ParameterNode) p.withParent(this)
        ).collect(Collectors.toList()) : null;
        this.expression = expression != null ? (ExpressionNode) expression.withParent(this) : null;
        this.blockStatements = blockStatements != null ? (BlockStatements) blockStatements.withParent(this) : null;
    }

    @Override
    public Expression toJavaExpression() {
        final LambdaExpr lambdaExpr = new LambdaExpr();
        lambdaExpr.setEnclosingParameters(this.enclosedParameters);
        if(this.parameters != null && !this.parameters.isEmpty()) {
            this.parameters.forEach(
                p -> p.addToJavaNode(lambdaExpr)
            );
            if(parameters.size() > 1) {
                lambdaExpr.setEnclosingParameters(true);
            }
        } else {
            lambdaExpr.setEnclosingParameters(true);
        }
        if(this.blockStatements != null) {
            final BlockStmt blockStmt = new BlockStmt();
            this.blockStatements.addToJavaNode(blockStmt);
            lambdaExpr.setBody(blockStmt);
        } else {
            lambdaExpr.setBody(new ExpressionStmt(this.expression.toJavaExpression()));
        }
        return lambdaExpr;
    }

    @Override
    public Position position() {
        return this.position;
    }

    @Override
    public List<QueenNode> children() {
        final List<QueenNode> children = new ArrayList<>();
        if(this.parameters != null) {
            children.addAll(this.parameters);
        }
        children.add(this.expression);
        children.add(this.blockStatements);
        return children;
    }

    @Override
    public boolean enclosedParameters() {
        return this.enclosedParameters;
    }

    @Override
    public List<ParameterNode> parameters() {
        return this.parameters;
    }

    @Override
    public ExpressionNode expression() {
        return this.expression;
    }

    @Override
    public BlockStatements blockStatements() {
        return this.blockStatements;
    }
}
