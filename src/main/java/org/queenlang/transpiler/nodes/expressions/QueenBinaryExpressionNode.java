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

import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import org.queenlang.transpiler.nodes.Position;

/**
 * Queen Binary Expression, AST Node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.01
 */
public final class QueenBinaryExpressionNode implements ExpressionNode {
    private final Position position;
    private final ExpressionNode left;
    private final String operator;
    private final ExpressionNode right;

    public QueenBinaryExpressionNode(
        final Position position,
        final ExpressionNode left,
        final String operator,
        final ExpressionNode right
    ) {
        this.position = position;
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public Expression toJavaExpression() {
        BinaryExpr.Operator operator = null;
        for(int i=0; i< BinaryExpr.Operator.values().length; i++) {
            if(BinaryExpr.Operator.values()[i].asString().equalsIgnoreCase(this.operator)) {
                operator = BinaryExpr.Operator.values()[i];
                break;
            }
        }
        if(operator == null) {
            throw new IllegalStateException("Unknown operator: " + this.operator);
        }
        return new BinaryExpr(
            this.left.toJavaExpression(),
            this.right.toJavaExpression(),
            operator
        );
    }

    @Override
    public Position position() {
        return this.position;
    }
}
