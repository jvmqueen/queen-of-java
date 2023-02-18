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

import com.github.javaparser.ast.ArrayCreationLevel;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.ArrayCreationExpr;
import com.github.javaparser.ast.expr.ArrayInitializerExpr;
import com.github.javaparser.ast.expr.Expression;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.types.TypeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Queen array creation, AST Node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenArrayCreationExpressionNode implements ArrayCreationExpressionNode {

    private final Position position;
    private final TypeNode type;
    private final List<QueenArrayDimensionNode> dims;
    private final ExpressionNode arrayInitializer;

    public QueenArrayCreationExpressionNode(
        final Position position,
        final TypeNode type,
        final List<QueenArrayDimensionNode> dims,
        final ExpressionNode arrayInitializer
    ) {
        this.position = position;
        this.type = type;
        this.dims = dims;
        this.arrayInitializer = arrayInitializer;
    }

    @Override
    public Expression toJavaExpression() {
        final List<ArrayCreationLevel> javaDims = new ArrayList<>();
        this.dims.forEach(
            d -> {
                final ArrayCreationLevel javaDim = new ArrayCreationLevel();
                if(d.expression() != null) {
                    javaDim.setDimension(d.expression().toJavaExpression());
                }
                d.annotations().forEach(
                    a -> a.addToJavaNode(javaDim)
                );
                javaDims.add(javaDim);
            }
        );
        final ArrayCreationExpr arrayCreation = new ArrayCreationExpr(
            this.type.toType(),
            new NodeList<>(javaDims),
            this.arrayInitializer != null
                ? (ArrayInitializerExpr) this.arrayInitializer.toJavaExpression()
                : null

        );
        return arrayCreation;
    }

    @Override
    public Position position() {
        return this.position;
    }

    @Override
    public TypeNode type() {
        return this.type;
    }

    @Override
    public List<QueenArrayDimensionNode> dims() {
        return this.dims;
    }

    @Override
    public ExpressionNode arrayInitializer() {
        return this.arrayInitializer;
    }
}
