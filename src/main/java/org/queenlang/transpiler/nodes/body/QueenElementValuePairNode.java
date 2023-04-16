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
package org.queenlang.transpiler.nodes.body;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.QueenNode;
import org.queenlang.transpiler.nodes.expressions.ExpressionNode;

import java.util.Arrays;
import java.util.List;

/**
 * An element-value paid node consisting of the Identifier and
 * initializing expression, Queen AST Node. User in annotations.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenElementValuePairNode implements ElementValuePairNode {

    private final Position position;
    private final QueenNode parent;
    private final String identifier;
    private final ExpressionNode expression;

    public QueenElementValuePairNode(final Position position, final String identifier, final ExpressionNode expression) {
        this(position, null, identifier, expression);
    }

    private QueenElementValuePairNode(final Position position, final QueenNode parent, final String identifier, final ExpressionNode expression) {
        this.position = position;
        this.parent = parent;
        this.identifier = identifier;
        this.expression = expression != null ? (ExpressionNode) expression.withParent(this) : null;
    }

    @Override
    public void addToJavaNode(final Node java) {
        final NormalAnnotationExpr annotation = (NormalAnnotationExpr) java;
        annotation.addPair(this.identifier, this.expression.toJavaExpression());
    }

    @Override
    public Position position() {
        return this.position;
    }

    @Override
    public List<QueenNode> children() {
        return Arrays.asList(this.expression);
    }

    @Override
    public QueenNode withParent(final QueenNode parent) {
        return new QueenElementValuePairNode(
            this.position,
            parent,
            this.identifier,
            this.expression
        );
    }

    @Override
    public QueenNode parent() {
        return this.parent;
    }

    @Override
    public String identifier() {
        return this.identifier;
    }

    @Override
    public ExpressionNode expression() {
        return this.expression;
    }
}
