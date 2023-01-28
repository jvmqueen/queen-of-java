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

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.UnaryExpr;

/**
 * Queen Unary Expression, AST Node.
 * An expression where an operator is applied to a single expression.
 * 11++
 * ++11
 * ~1
 * -333
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenUnaryExpressionNode implements QueenExpressionNode {

    private final Position position;
    private final String operator;
    private final boolean isPrefix;
    private final QueenExpressionNode expression;

    public QueenUnaryExpressionNode(
        final Position position,
        final String operator,
        final boolean isPrefix,
        final QueenExpressionNode expression
    ) {
        this.position = position;
        this.operator = operator;
        this.isPrefix = isPrefix;
        this.expression = expression;
    }

    @Override
    public Expression toJavaExpression() {
        UnaryExpr.Operator operator = null;
        for(int i=0; i< UnaryExpr.Operator.values().length; i++) {
            final UnaryExpr.Operator candidate = UnaryExpr.Operator.values()[i];
            if(candidate.asString().equalsIgnoreCase(this.operator) && candidate.isPrefix() == this.isPrefix) {
                operator = UnaryExpr.Operator.values()[i];
                break;
            }
        }
        if(operator == null) {
            throw new IllegalStateException("Unkown unary operator: " + this.operator + ". (is prefix: " + this.isPrefix + ").");
        }
        return new UnaryExpr(
            this.expression.toJavaExpression(),
            operator
        );
    }

    @Override
    public Position position() {
        return this.position;
    }
}