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
package org.queenlang.transpiler.nodes.statements;

import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.QueenNode;
import org.queenlang.transpiler.nodes.expressions.ExpressionNode;

import java.util.Arrays;
import java.util.List;

/**
 * A Queen label in a switch case, AST Node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenSwitchLabelNode implements SwitchLabelNode {

    private final Position position;
    private final QueenNode parent;
    private final ExpressionNode expressionNode;
    private final boolean isDefaultLabel;

    public QueenSwitchLabelNode(final Position position, final ExpressionNode expressionNode, final boolean isDefaultLabel) {
        this(position, null, expressionNode, isDefaultLabel);
    }

    private QueenSwitchLabelNode(final Position position, final QueenNode parent, final ExpressionNode expressionNode, final boolean isDefaultLabel) {
        this.position = position;
        this.parent = parent;
        this.expressionNode = expressionNode != null ? (ExpressionNode) expressionNode.withParent(this) : null;
        this.isDefaultLabel = isDefaultLabel;
    }

    @Override
    public Position position() {
        return this.position;
    }

    @Override
    public List<QueenNode> children() {
        return Arrays.asList(this.expressionNode);
    }

    @Override
    public QueenNode withParent(final QueenNode parent) {
        return new QueenSwitchLabelNode(
            this.position,
            parent,
            this.expressionNode,
            this.isDefaultLabel
        );
    }

    @Override
    public QueenNode parent() {
        return this.parent;
    }

    @Override
    public ExpressionNode expressionNode() {
        return this.expressionNode;
    }

    @Override
    public boolean isDefaultLabel() {
        return this.isDefaultLabel;
    }
}
