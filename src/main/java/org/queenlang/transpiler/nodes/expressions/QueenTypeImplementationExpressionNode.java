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

import com.github.javaparser.ast.expr.ClassExpr;
import com.github.javaparser.ast.expr.Expression;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.QueenNode;
import org.queenlang.transpiler.nodes.types.QueenArrayTypeNode;
import org.queenlang.transpiler.nodes.types.TypeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Defines an expression that accesses the class of a type.
 * Object.implementation, int.implementation, void.implementation etc.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenTypeImplementationExpressionNode implements TypeImplementationExpressionNode {

    private final Position position;
    private final TypeNode type;
    private final List<ArrayDimensionNode> dims;

    public QueenTypeImplementationExpressionNode(
        final Position position,
        final TypeNode type,
        final List<ArrayDimensionNode> dims
    ) {
        this.position = position;
        this.type = type != null ? (TypeNode) type.withParent(this) : null;
        this.dims = dims != null ? dims.stream().map(
            d -> (ArrayDimensionNode) d.withParent(this)
        ).collect(Collectors.toList()) : null;
    }

    @Override
    public Expression toJavaExpression() {
        if(this.dims != null && !this.dims.isEmpty()) {
            return new ClassExpr(
                new QueenArrayTypeNode(
                    this.position,
                    this.type,
                    this.dims
                ).toType()
            );
        } else {
            return new ClassExpr(this.type.toType());
        }
    }

    @Override
    public Position position() {
        return this.position;
    }

    @Override
    public List<QueenNode> children() {
        final List<QueenNode> children = new ArrayList<>();
        children.add(this.type);
        if(this.dims != null) {
            children.addAll(this.dims);
        }
        return children;
    }

    @Override
    public TypeNode type() {
        return this.type;
    }

    @Override
    public List<ArrayDimensionNode> dims() {
        return this.dims;
    }
}
