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
import org.queenlang.transpiler.nodes.statements.BlockStatements;
import org.queenlang.transpiler.nodes.types.ArrayTypeNode;
import org.queenlang.transpiler.nodes.types.NodeWithTypeParameters;
import org.queenlang.transpiler.nodes.types.TypeNode;

import java.util.List;

/**
 * Queen MethodDeclaration AST Node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public interface MethodDeclarationNode extends ClassMemberDeclarationNode, InterfaceMemberDeclarationNode, NodeWithModifiers, NodeWithAnnotations, NodeWithParameters, NodeWithTypeParameters, NodeWithThrows {

    /**
     * Return type.
     */
    TypeNode returnType();

    /**
     * Method body.
     */
    BlockStatements blockStatements();

    /**
     * It this method declared in an interface or a class?
     * @return True if interface, false otherwise.
     */
    boolean interfaceDeclaration();

    /**
     * It this method public?
     * @return True or false.
     */
    default boolean isPublic() {
        for(final ModifierNode modifier : this.modifiers()) {
            if("public".equals(modifier.modifier())) {
                return true;
            }
        }
        return false;
    }

    /**
     * It this method static?
     * @return True or false.
     */
    default boolean isStatic() {
        for(final ModifierNode modifier : this.modifiers()) {
            if("static".equals(modifier.modifier())) {
                return true;
            }
        }
        return false;
    }

    /**
     * It this method abstract?
     * @return True or false.
     */
    default boolean isAbstract() {
        for(final ModifierNode modifier : this.modifiers()) {
            if("abstract".equals(modifier.modifier())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Does this method declaration have the specified modifier?
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

    /**
     * Is this the main method?
     * @return True or false.
     */
    default boolean isMainMethod() {
        final boolean isPublic = this.isPublic();
        final boolean isStatic = this.isStatic();
        final boolean isVoid = this.returnType().isVoid();
        final boolean hasSingleStringArrayParam;
        final List<ParameterNode> params = this.parameters();
        if(params != null && params.size() == 1) {
            final ParameterNode param = params.get(0);
            final boolean isArray = param.type() instanceof ArrayTypeNode;
            final String fullName = param.type().name();
            if(isArray && ("String".equals(fullName) || "java.lang.String".equals(fullName))) {
                hasSingleStringArrayParam = true;
            } else {
                hasSingleStringArrayParam = false;
            }
        } else {
            hasSingleStringArrayParam = false;
        }
        return isPublic && isStatic && isVoid && "main".equals(this.name()) && hasSingleStringArrayParam;
    }

    default <T> T accept(QueenASTVisitor<? extends T> visitor) {
        return visitor.visitMethodDeclarationNode(this);
    }
}
