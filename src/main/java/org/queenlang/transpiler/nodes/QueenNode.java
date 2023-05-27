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
package org.queenlang.transpiler.nodes;

import org.queenlang.transpiler.QueenASTVisitor;

import java.util.List;

/**
 * A node in Queen's Abstract Syntax Tree.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public interface QueenNode {

    /**
     * Position of this QueenNode in the original source code.
     * @return Position.
     */
    Position position();

    /**
     * Return the children of this node. If the list is empty, it means it's a
     * terminal node.
     * @return List of children, never null.
     */
    List<QueenNode> children();

    /**
     * Accept a QueenASTVisitor.
     * @param visitor Visitor.
     * @return Result of the visit.
     * @param <T> Type of the result.
     */
    <T> T accept(QueenASTVisitor<? extends T> visitor);

    /**
     * An instance of this QueenNode with the parent.
     * @param parent Parent node.
     * @return QueenNode.
     */
    QueenNode withParent(final QueenNode parent);

    /**
     * Return the parent of this AST Node.
     * @return Parent, or null if it's the root of the AST.
     */
    QueenNode parent();

    /**
     * Resolve a reference, return the node to which it refers.
     *
     * @param reference Reference to resolve
     * @param goUp      True if we should look upwards in the AST, false if we should look downwards.
     * @return Node to which the reference refers to or null if none is found.
     */
    default QueenNode resolve(final QueenReferenceNode reference, boolean goUp) {
        final QueenNode parent = this.parent();
        if(parent != null) {
            return parent.resolve(reference, goUp);
        }
        return null;
    }
}
