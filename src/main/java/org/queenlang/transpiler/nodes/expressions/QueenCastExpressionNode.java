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
import com.github.javaparser.ast.expr.CastExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.type.IntersectionType;
import com.github.javaparser.ast.type.ReferenceType;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.QueenNode;
import org.queenlang.transpiler.nodes.types.ReferenceTypeNode;
import org.queenlang.transpiler.nodes.types.TypeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Queen Casting, AST Node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenCastExpressionNode implements CastExpressionNode {

    private final Position position;
    private final QueenNode parent;
    private final TypeNode primitiveType;
    private final List<ReferenceTypeNode> referenceTypes;
    private final ExpressionNode expression;

    public QueenCastExpressionNode(
        final Position position,
        final List<ReferenceTypeNode> referenceTypes,
        final ExpressionNode expression
    ) {
        this(position, null, referenceTypes, expression);
    }

    public QueenCastExpressionNode(
        final Position position,
        final TypeNode primitiveType,
        final List<ReferenceTypeNode> referenceTypes,
        final ExpressionNode expression
    ) {
        this(position, null, primitiveType, referenceTypes, expression);
    }

    private QueenCastExpressionNode(
        final Position position,
        final QueenNode parent,
        final TypeNode primitiveType,
        final List<ReferenceTypeNode> referenceTypes,
        final ExpressionNode expression
    ) {
        this.position = position;
        this.parent = parent;
        this.primitiveType = primitiveType != null ? (TypeNode) primitiveType.withParent(this) : null;
        this.referenceTypes = referenceTypes != null ? referenceTypes.stream().map(
            r -> (ReferenceTypeNode) r.withParent(this)
        ).collect(Collectors.toList()) : null;
        this.expression = expression != null ? (ExpressionNode) expression.withParent(this) : null;
    }

    @Override
    public Expression toJavaExpression() {
        if(this.primitiveType != null) {
            return new CastExpr(
                this.primitiveType.toType(),
                this.expression.toJavaExpression()
            );
        } else {
            final List<ReferenceType> javaTypes = new ArrayList<>();
            this.referenceTypes.forEach(
                rt -> javaTypes.add((ReferenceType) rt.toType())
            );
            final IntersectionType type = new IntersectionType(
                new NodeList<>(javaTypes)
            );
            return new CastExpr(
                type,
                this.expression.toJavaExpression()
            );
        }
    }

    @Override
    public Position position() {
        return this.position;
    }

    @Override
    public List<QueenNode> children() {
        final List<QueenNode> children = new ArrayList<>();
        children.add(this.primitiveType);
        children.add(this.expression);
        if(this.referenceTypes != null) {
            children.addAll(this.referenceTypes);
        }
        return children;
    }

    @Override
    public QueenNode withParent(final QueenNode parent) {
        return new QueenCastExpressionNode(
            this.position,
            parent,
            this.primitiveType,
            this.referenceTypes,
            this.expression
        );
    }

    @Override
    public QueenNode parent() {
        return this.parent;
    }

    @Override
    public TypeNode primitiveType() {
        return this.primitiveType;
    }

    @Override
    public List<ReferenceTypeNode> referenceTypes() {
        return this.referenceTypes;
    }

    @Override
    public ExpressionNode expression() {
        return this.expression;
    }
}
