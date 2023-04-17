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
import com.github.javaparser.ast.expr.MethodReferenceExpr;
import com.github.javaparser.ast.expr.TypeExpr;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.QueenNode;
import org.queenlang.transpiler.nodes.types.TypeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Queen Method Reference Expression, AST Node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenMethodReferenceExpressionNode implements MethodReferenceExpressionNode {

    private final Position position;
    private final QueenNode parent;
    private final TypeNode type;
    private final ExpressionNode scope;
    private final List<TypeNode> typeArguments;
    private final String identifier;

    public QueenMethodReferenceExpressionNode(
        final Position position,
        final TypeNode type,
        final ExpressionNode scope,
        final List<TypeNode> typeArguments,
        final String identifier
    ) {
        this(
            position,
            null,
            type,
            scope,
            typeArguments,
            identifier
        );
    }

    private QueenMethodReferenceExpressionNode(
        final Position position,
        final QueenNode parent,
        final TypeNode type,
        final ExpressionNode scope,
        final List<TypeNode> typeArguments,
        final String identifier
    ) {
        this.position = position;
        this.parent = parent;
        this.type = type != null ? (TypeNode) type.withParent(this) : null;
        this.scope = scope != null ? (ExpressionNode) scope.withParent(this) : null;
        this.typeArguments = typeArguments != null ? typeArguments.stream().map(
            ta -> (TypeNode) ta.withParent(this)
        ).collect(Collectors.toList()) : null;
        this.identifier = identifier;
    }

    @Override
    public Expression toJavaExpression() {
        final MethodReferenceExpr methodReferenceExpr = new MethodReferenceExpr();
        if(this.type != null) {
            methodReferenceExpr.setScope(
                new TypeExpr(this.type.toType())
            );
        } else {
            methodReferenceExpr.setScope(
                this.scope.toJavaExpression()
            );
        }
        if(this.typeArguments != null) {
            this.typeArguments.forEach(ta -> ta.addToJavaNode(methodReferenceExpr));
        }
        methodReferenceExpr.setIdentifier(this.identifier);
        return methodReferenceExpr;
    }

    @Override
    public Position position() {
        return this.position;
    }

    @Override
    public List<QueenNode> children() {
        final List<QueenNode> children = new ArrayList<>();
        children.add(this.type);
        children.add(this.scope);
        if(this.typeArguments != null) {
            children.addAll(this.typeArguments);
        }
        return children;
    }

    @Override
    public QueenNode withParent(final QueenNode parent) {
        return new QueenMethodReferenceExpressionNode(
            this.position,
            parent,
            this.type,
            this.scope,
            this.typeArguments,
            this.identifier
        );
    }

    @Override
    public QueenNode parent() {
        return this.parent;
    }

    @Override
    public TypeNode type() {
        return this.type;
    }

    @Override
    public ExpressionNode scope() {
        return this.scope;
    }

    @Override
    public List<TypeNode> typeArguments() {
        return this.typeArguments;
    }

    @Override
    public String identifier() {
        return this.identifier;
    }
}
