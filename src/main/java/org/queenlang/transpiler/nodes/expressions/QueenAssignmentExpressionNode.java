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

import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import org.queenlang.transpiler.nodes.Position;

/**
 * Queen Assignment Expression, AST Node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenAssignmentExpressionNode implements QueenExpressionNode {

    private final Position position;
    private final QueenExpressionNode target;
    private final String operator;
    private final QueenExpressionNode value;

    public QueenAssignmentExpressionNode(
        final Position position,
        final QueenExpressionNode target,
        final String operator,
        final QueenExpressionNode value
    ) {
        this.position = position;
        this.target = target;
        this.operator = operator;
        this.value = value;
    }

    @Override
    public Expression toJavaExpression() {
        AssignExpr.Operator operator = null;
        for(int i=0; i< AssignExpr.Operator.values().length; i++) {
            if(AssignExpr.Operator.values()[i].asString().equalsIgnoreCase(this.operator)) {
                operator = AssignExpr.Operator.values()[i];
                break;
            }
        }
        if(operator == null) {
            throw new IllegalStateException("Unknown assignment operator: " + this.operator);
        }


        return new AssignExpr(
            this.target.toJavaExpression(),
            this.value.toJavaExpression(),
            operator
        );
    }

    @Override
    public Position position() {
        return this.position;
    }
}
