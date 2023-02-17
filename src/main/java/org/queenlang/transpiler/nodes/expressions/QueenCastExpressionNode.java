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
import org.queenlang.transpiler.nodes.types.QueenReferenceTypeNode;
import org.queenlang.transpiler.nodes.types.QueenTypeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Queen Casting, AST Node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenCastExpressionNode implements ExpressionNode {

    private final Position position;
    private final QueenTypeNode primitiveType;
    private final List<QueenReferenceTypeNode> referenceTypes;
    private final ExpressionNode expression;

    public QueenCastExpressionNode(
        final Position position,
        final List<QueenReferenceTypeNode> referenceTypes,
        final ExpressionNode expression
    ) {
        this(position, null, referenceTypes, expression);
    }

    public QueenCastExpressionNode(
        final Position position,
        final QueenTypeNode primitiveType,
        final List<QueenReferenceTypeNode> referenceTypes,
        final ExpressionNode expression
    ) {
        this.position = position;
        this.primitiveType = primitiveType;
        this.referenceTypes = referenceTypes;
        this.expression = expression;
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
}
