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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Queen ExceptionType AST Node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenExceptionTypeNode implements ExceptionTypeNode {

    private final ClassOrInterfaceTypeNode exceptionType;

    /**
     * Parent node.
     */
    private final QueenNode parent;

    public QueenExceptionTypeNode(final ClassOrInterfaceTypeNode exceptionType) {
        this(null, exceptionType);
    }

    private QueenExceptionTypeNode(
        final QueenNode parent,
        final ClassOrInterfaceTypeNode exceptionType
    ) {
        this.parent = parent;
        this.exceptionType = exceptionType != null ? (ClassOrInterfaceTypeNode) exceptionType.withParent(this) : null;
    }

    @Override
    public Position position() {
        return this.exceptionType.position();
    }

    @Override
    public ClassOrInterfaceTypeNode exceptionType() {
        return this.exceptionType;
    }

    @Override
    public String name() {
        return this.exceptionType.name();
    }

    @Override
    public List<QueenNode> children() {
        final List<QueenNode> children = new ArrayList<>();
        children.add(this.exceptionType);
        return children;
    }

    @Override
    public QueenNode withParent(final QueenNode parent) {
        return new QueenExceptionTypeNode(
            parent,
            this.exceptionType
        );
    }

    @Override
    public QueenNode parent() {
        return this.parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final QueenExceptionTypeNode that = (QueenExceptionTypeNode) o;
        return this.exceptionType.equals(that.exceptionType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.exceptionType);
    }
}
