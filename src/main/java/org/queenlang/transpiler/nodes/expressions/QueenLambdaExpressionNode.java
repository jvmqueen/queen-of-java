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

import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.QueenNode;
import org.queenlang.transpiler.nodes.body.ParameterList;
import org.queenlang.transpiler.nodes.body.ParameterNode;
import org.queenlang.transpiler.nodes.statements.BlockStatements;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Queen Lambda Expression, AST Node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenLambdaExpressionNode implements LambdaExpressionNode {

    private final Position position;
    private final QueenNode parent;
    private final boolean enclosedParameters;
    private final ParameterList parameters;
    private final ExpressionNode expression;
    private final BlockStatements blockStatements;

    public QueenLambdaExpressionNode(
        final Position position,
        final boolean enclosedParameters,
        final ParameterList parameters,
        final ExpressionNode expression,
        final BlockStatements blockStatements
    ) {
        this(
            position,
            null,
            enclosedParameters,
            parameters,
            expression,
            blockStatements
        );
    }

    private QueenLambdaExpressionNode(
        final Position position,
        final QueenNode parent,
        final boolean enclosedParameters,
        final ParameterList parameters,
        final ExpressionNode expression,
        final BlockStatements blockStatements
    ) {
        this.position = position;
        this.parent = parent;
        this.enclosedParameters = enclosedParameters;
        this.parameters = parameters != null ? (ParameterList) parameters.withParent(this) : null;
        this.expression = expression != null ? (ExpressionNode) expression.withParent(this) : null;
        this.blockStatements = blockStatements != null ? (BlockStatements) blockStatements.withParent(this) : null;
    }

    @Override
    public Position position() {
        return this.position;
    }

    @Override
    public List<QueenNode> children() {
        final List<QueenNode> children = new ArrayList<>();
        if(this.parameters != null) {
            children.add(this.parameters);
        }
        children.add(this.expression);
        children.add(this.blockStatements);
        return children;
    }

    @Override
    public QueenNode withParent(final QueenNode parent) {
        return new QueenLambdaExpressionNode(
            this.position,
            parent,
            this.enclosedParameters,
            this.parameters,
            this.expression,
            this.blockStatements
        );
    }

    @Override
    public QueenNode parent() {
        return this.parent;
    }

    @Override
    public boolean enclosedParameters() {
        return this.enclosedParameters;
    }

    @Override
    public ParameterList parameters() {
        return this.parameters;
    }

    @Override
    public ExpressionNode expression() {
        return this.expression;
    }

    @Override
    public BlockStatements blockStatements() {
        return this.blockStatements;
    }
}
