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
package org.queenlang.transpiler.nodes.types;

import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.QueenNode;
import org.queenlang.transpiler.nodes.expressions.ArrayDimensionNode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Queen ArrayType AST Node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenArrayTypeNode implements ArrayTypeNode {

    /**
     * Position in the original source code.
     */
    private final Position position;

    /**
     * Parent node.
     */
    private final QueenNode parent;

    /**
     * Type of the array (used for symbol resolution etc).
     */
    private final TypeNode type;

    /**
     * Array type dimensions (pairs of square brackets).
     */
    private final List<ArrayDimensionNode> dims;

    public QueenArrayTypeNode(
        final Position position,
        final TypeNode type,
        final List<ArrayDimensionNode> dims
    ) {
        this(position, null, type, dims);
    }

    private QueenArrayTypeNode(
        final Position position,
        final QueenNode parent,
        final TypeNode type,
        final List<ArrayDimensionNode> dims
    ) {
        this.position = position;
        this.parent = parent;
        this.type = type != null ? (TypeNode) type.withParent(this) : null;
        this.dims = dims != null ? dims.stream().map(
            d -> (ArrayDimensionNode) d.withParent(this)
        ).collect(Collectors.toList()) : null;
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
    public List<ArrayDimensionNode> dims() {
        return this.dims;
    }

    @Override
    public String name() {
        return this.type.name();
    }

    @Override
    public List<QueenNode> children() {
        final List<QueenNode> children = new ArrayList<>();
        children.add(this.type);
        if(this.dims != null) {
            children.addAll(this.dims);
        }
        return children;
    }

    @Override
    public QueenNode withParent(final QueenNode parent) {
        return new QueenArrayTypeNode(
            this.position,
            parent,
            this.type,
            this.dims
        );
    }

    @Override
    public QueenNode parent() {
        return this.parent;
    }
}
