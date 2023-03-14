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

import com.github.javaparser.ast.expr.ArrayAccessExpr;
import com.github.javaparser.ast.expr.Expression;
import org.queenlang.transpiler.nodes.Position;

import java.util.List;

/**
 * Queen Array Access Expression, AST Node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenArrayAccessExpressionNode implements ArrayAccessExpressionNode {

    private final Position position;

    private final ExpressionNode name;

    private final List<ArrayDimensionNode> dims;

    public QueenArrayAccessExpressionNode(
        final Position position,
        final ExpressionNode name,
        final List<ArrayDimensionNode> dims
    ) {
        this.position = position;
        this.name = name;
        this.dims = dims;
    }

    @Override
    public Expression toJavaExpression() {
        ArrayAccessExpr arrayAccessExpr = new ArrayAccessExpr();
        arrayAccessExpr.setName(this.name.toJavaExpression());
        arrayAccessExpr.setIndex(this.dims.get(0).expression().toJavaExpression());
        for(int i = 1; i<this.dims.size(); i++) {
            arrayAccessExpr = new ArrayAccessExpr(
                arrayAccessExpr,
                this.dims.get(i).expression().toJavaExpression()
            );
        }
        return arrayAccessExpr;
    }

    @Override
    public Position position() {
        return this.position;
    }

    @Override
    public ExpressionNode name() {
        return this.name;
    }

    @Override
    public List<ArrayDimensionNode> dims() {
        return this.dims;
    }
}
