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
import org.queenlang.transpiler.nodes.types.TypeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Queen Explicit Constructor Invocation AST Node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenExplicitConstructorInvocationNode implements ExplicitConstructorInvocationNode {

    private final Position position;
    private final QueenNode parent;
    private final boolean isThis;
    private final ExpressionNode scope;
    private final List<TypeNode> typeArguments;
    private final List<ExpressionNode> arguments;

    public QueenExplicitConstructorInvocationNode(
        final Position position,
        final boolean isThis,
        final ExpressionNode scope,
        final List<TypeNode> typeArguments,
        final List<ExpressionNode> arguments
    ) {
        this(
            position,
            null,
            isThis,
            scope,
            typeArguments,
            arguments
        );
    }

    private QueenExplicitConstructorInvocationNode(
        final Position position,
        final QueenNode parent,
        final boolean isThis,
        final ExpressionNode scope,
        final List<TypeNode> typeArguments,
        final List<ExpressionNode> arguments
    ) {
        this.position = position;
        this.parent = parent;
        this.isThis = isThis;
        this.scope = scope != null ? (ExpressionNode) scope.withParent(this) : null;
        this.typeArguments = typeArguments != null ? typeArguments.stream().map(
            ta -> (TypeNode) ta.withParent(this)
        ).collect(Collectors.toList()) : null;
        this.arguments = arguments != null ? arguments.stream().map(
            a -> (ExpressionNode) a.withParent(this)
        ).collect(Collectors.toList()) : null;
    }

    @Override
    public Position position() {
        return this.position;
    }

    @Override
    public List<QueenNode> children() {
        final List<QueenNode> children = new ArrayList<>();
        children.add(this.scope);
        if(this.typeArguments != null) {
            children.addAll(this.typeArguments);
        }
        if(this.arguments != null) {
            children.addAll(this.arguments);
        }
        return children;
    }

    @Override
    public QueenNode withParent(final QueenNode parent) {
        return new QueenExplicitConstructorInvocationNode(
            this.position,
            parent,
            this.isThis,
            this.scope,
            this.typeArguments,
            this.arguments
        );
    }

    @Override
    public QueenNode parent() {
        return this.parent;
    }

    @Override
    public boolean isThis() {
        return this.isThis;
    }

    @Override
    public ExpressionNode scope() {
        return this.scope;
    }

    @Override
    public List<TypeNode> typeArguments() {
        return this.typeArguments;
    }

    @Override
    public List<ExpressionNode> arguments() {
        return this.arguments;
    }
}
