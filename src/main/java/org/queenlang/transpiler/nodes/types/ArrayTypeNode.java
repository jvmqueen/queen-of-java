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

import org.queenlang.transpiler.QueenASTVisitor;
import org.queenlang.transpiler.nodes.NameNode;
import org.queenlang.transpiler.nodes.expressions.ArrayDimensionNode;
import org.queenlang.transpiler.nodes.expressions.QueenArrayDimensionNode;

import java.util.List;

/**
 * Queen ArrayType AST Node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public interface ArrayTypeNode extends ReferenceTypeNode {

    /**
     * Type of the array (used for symbol resolution etc).
     */
    TypeNode type();

    /**
     * Array type dimensions (pairs of square brackets).
     */
    List<ArrayDimensionNode> dims();

    /**
     * Scope of this reference type (what comes before the dot). E.g.
     * java.util.List (util is the scope of List).
     */
    default NameNode qualifier() {
        final TypeNode type = this.type();
        if(type instanceof ReferenceTypeNode) {
            return ((ReferenceTypeNode) type).qualifier();
        }
        return null;
    }

    /**
     * Simple name of this reference type, without scope
     */
    default String identifier() {
        final TypeNode type = this.type();
        if(type instanceof ReferenceTypeNode) {
            return ((ReferenceTypeNode) type).identifier();
        }
        return this.name();
    }

    default <T> T accept(QueenASTVisitor<? extends T> visitor) {
        return visitor.visitArrayTypeNode(this);
    }
}
