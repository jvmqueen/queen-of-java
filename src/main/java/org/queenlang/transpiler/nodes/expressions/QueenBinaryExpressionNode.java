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

import java.util.Arrays;
import java.util.List;

/**
 * Queen Binary Expression, AST Node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.01
 */
public final class QueenBinaryExpressionNode implements BinaryExpressionNode {
    private final Position position;
    private final QueenNode parent;
    private final ExpressionNode left;
    private final String operator;
    private final ExpressionNode right;

    public QueenBinaryExpressionNode(
        final Position position,
        final ExpressionNode left,
        final String operator,
        final ExpressionNode right
    ) {
        this(position, null, left, operator, right);
    }

    private QueenBinaryExpressionNode(
        final Position position,
        final QueenNode parent,
        final ExpressionNode left,
        final String operator,
        final ExpressionNode right
    ) {
        this.position = position;
        this.parent = parent;
        this.left = left != null ? (ExpressionNode) left.withParent(this) : null;
        this.operator = operator;
        this.right = right != null ? (ExpressionNode) right.withParent(this) : null;
    }

    @Override
    public Position position() {
        return this.position;
    }

    @Override
    public List<QueenNode> children() {
        return Arrays.asList(this.left, this.right);
    }

    @Override
    public QueenNode withParent(final QueenNode parent) {
        return new QueenBinaryExpressionNode(
            this.position,
            parent,
            this.left,
            this.operator,
            this.right
        );
    }

    @Override
    public QueenNode parent() {
        return this.parent;
    }

    @Override
    public ExpressionNode left() {
        return this.left;
    }

    @Override
    public String operator() {
        return this.operator;
    }

    @Override
    public ExpressionNode right() {
        return this.right;
    }
}
