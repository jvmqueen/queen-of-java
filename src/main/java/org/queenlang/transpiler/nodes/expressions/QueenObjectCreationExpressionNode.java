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

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.QueenNode;
import org.queenlang.transpiler.nodes.body.ClassBodyNode;
import org.queenlang.transpiler.nodes.body.QueenClassBodyNode;
import org.queenlang.transpiler.nodes.types.ClassOrInterfaceTypeNode;
import org.queenlang.transpiler.nodes.types.QueenClassOrInterfaceTypeNode;
import org.queenlang.transpiler.nodes.types.TypeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Queen object creation/class instantiation expression, AST Node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenObjectCreationExpressionNode implements ObjectCreationExpressionNode {

    private final Position position;
    private final QueenNode parent;
    private final ExpressionNode scope;
    private final ClassOrInterfaceTypeNode type;

    private final List<TypeNode> typeArguments;

    private final List<ExpressionNode> arguments;

    private final ClassBodyNode anonymousBody; //check only class member declaration (e.g. no constructors allowed!).

    public QueenObjectCreationExpressionNode(
        final Position position,
        final ExpressionNode scope,
        final ClassOrInterfaceTypeNode type,
        final List<TypeNode> typeArguments,
        final List<ExpressionNode> arguments,
        final ClassBodyNode anonymousBody
    ) {
        this(
            position,
            null,
            scope,
            type,
            typeArguments,
            arguments,
            anonymousBody
        );
    }

    private QueenObjectCreationExpressionNode(
        final Position position,
        final QueenNode parent,
        final ExpressionNode scope,
        final ClassOrInterfaceTypeNode type,
        final List<TypeNode> typeArguments,
        final List<ExpressionNode> arguments,
        final ClassBodyNode anonymousBody
    ) {
        this.position = position;
        this.parent = parent;
        this.scope = scope != null ? (ExpressionNode) scope.withParent(this) : null;
        this.type = (ClassOrInterfaceTypeNode) type.withParent(this);
        this.typeArguments = typeArguments != null ? typeArguments.stream().map(
            ta -> (TypeNode) ta.withParent(this)
        ).collect(Collectors.toList()) : null;
        this.arguments = arguments != null ? arguments.stream().map(
            a -> (ExpressionNode) a.withParent(this)
        ).collect(Collectors.toList()) : null;
        this.anonymousBody = anonymousBody != null ? (ClassBodyNode) anonymousBody.withParent(this) : null;
    }
    @Override
    public Expression toJavaExpression() {
        final ObjectCreationExpr objectCreationExpr = new ObjectCreationExpr();
        if(this.scope != null) {
            objectCreationExpr.setScope(this.scope.toJavaExpression());
        }
        objectCreationExpr.setType((ClassOrInterfaceType) this.type.toType());
        if(this.typeArguments != null) {
            this.typeArguments.forEach(ta -> ta.addToJavaNode(objectCreationExpr));
        }
        final List<Expression> args = new ArrayList<>();
        if(this.arguments != null) {
            this.arguments.forEach(
                a -> args.add(a.toJavaExpression())
            );
        }
        objectCreationExpr.setArguments(new NodeList<>(args));
        if(this.anonymousBody != null) {
            if(!this.anonymousBody.isEmpty()) {
                this.anonymousBody.addToJavaNode(objectCreationExpr);
            } else {
                objectCreationExpr.setAnonymousClassBody(new NodeList<>());
            }
        }
        return objectCreationExpr;
    }

    @Override
    public Position position() {
        return this.position;
    }

    @Override
    public List<QueenNode> children() {
        final List<QueenNode> children = new ArrayList<>();
        children.add(this.scope);
        children.add(this.type);
        if(this.typeArguments != null) {
            children.addAll(this.typeArguments);
        }
        if(this.arguments != null) {
            children.addAll(this.arguments);
        }
        children.add(this.anonymousBody);
        return children;
    }

    @Override
    public QueenNode withParent(final QueenNode parent) {
        return new QueenObjectCreationExpressionNode(
            this.position,
            parent,
            this.scope,
            this.type,
            this.typeArguments,
            this.arguments,
            this.anonymousBody
        );
    }

    @Override
    public QueenNode parent() {
        return this.parent;
    }

    @Override
    public ExpressionNode scope() {
        return this.scope;
    }

    @Override
    public ClassOrInterfaceTypeNode type() {
        return this.type;
    }

    @Override
    public List<TypeNode> typeArguments() {
        return this.typeArguments;
    }

    @Override
    public List<ExpressionNode> arguments() {
        return this.arguments;
    }

    @Override
    public ClassBodyNode anonymousBody() {
        return this.anonymousBody;
    }
}
