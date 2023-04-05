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

import org.queenlang.transpiler.QueenASTVisitor;
import org.queenlang.transpiler.nodes.types.TypeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Queen FieldDeclaration AST node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public interface FieldDeclarationNode extends ClassMemberDeclarationNode, NodeWithModifiers, NodeWithAnnotations {

    /**
     * Type of the field declaration.
     */
    TypeNode type();

    /**
     * Variable names and initializer expressions.
     */
    List<VariableDeclaratorNode> variables();

    default <T> T accept(QueenASTVisitor<? extends T> visitor) {
        return visitor.visitFieldDeclarationNode(this);
    }

    /**
     * Does this field declaration have the specified modifier?
     * @param modifier String modifier.
     * @return ModifierNode if found, null otherwise.
     */
    default ModifierNode modifier(final String modifier) {
        for(final ModifierNode m : this.modifiers()) {
            if(modifier.equals(m.modifier())) {
                return m;
            }
        }
        return null;
    }

    default List<ModifierNode> accessModifiers() {
        final List<ModifierNode> accessModifiers = new ArrayList<>();
        for(final ModifierNode m : this.modifiers()) {
            if("public".equals(m.modifier()) || "private".equals(m.modifier())) {
                accessModifiers.add(m);
            }
        }
        return accessModifiers;
    }

    default boolean isPublic() {
        return this.modifier("public") != null;
    }

    default boolean isStatic() {
        return this.modifier("static") != null;
    }

    default boolean isMutable() {
        return this.modifier("mutable") != null;
    }
}
