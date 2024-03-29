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
import org.queenlang.transpiler.nodes.QueenNode;
import org.queenlang.transpiler.nodes.expressions.AnnotationNode;
import org.queenlang.transpiler.nodes.expressions.QueenAnnotationNode;
import org.queenlang.transpiler.nodes.statements.StatementNode;
import org.queenlang.transpiler.nodes.types.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Queen ClassDeclaration AST node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public interface ClassDeclarationNode extends TypeDeclarationNode, StatementNode, ClassMemberDeclarationNode, InterfaceMemberDeclarationNode, AnnotationTypeMemberDeclarationNode, NodeWithTypeParameters {

    /**
     * Extension modifier (abstract or final).
     */
    ModifierNode extensionModifier();

    /**
     * Type which is extended.
     */
    ClassOrInterfaceTypeNode extendsType();

    /**
     * Interfaces this type implements.
     */
    InterfaceTypeList of();

    /**
     * The body.
     */
    ClassBodyNode body();

    default List<MethodDeclarationNode> inheritedMethods() {
        final List<MethodDeclarationNode> inherited = new ArrayList<>();
        if(this.extendsType() != null) {
            final QueenNode resolvedExtendsType = this.extendsType().resolve();
            if(resolvedExtendsType != null && resolvedExtendsType.asClassDeclarationNode() != null) {
                final ClassDeclarationNode extendsClass = resolvedExtendsType.asClassDeclarationNode();
                inherited.addAll(extendsClass.body().methods().stream().filter(
                    m -> !m.isStatic() && !m.isPrivate()
                ).collect(Collectors.toList()));
                inherited.addAll(extendsClass.inheritedMethods());
            }
        }
        if(this.of() != null) {
            for(final ClassOrInterfaceTypeNode ofType : this.of()) {
                final QueenNode resolvedOfType = ofType.resolve();
                if (resolvedOfType != null && resolvedOfType.asNormalInterfaceDeclaration() != null) {
                    final NormalInterfaceDeclarationNode ofInterface = resolvedOfType.asNormalInterfaceDeclaration();
                    inherited.addAll(ofInterface.body().methods());
                    inherited.addAll(ofInterface.inheritedMethods());
                }
            }
        }
        return inherited;
    }

    /**
     * Is this class declaration abstract or not?
     * @return True if abstract, false otherwise.
     */
    default boolean isAbstract() {
        return "abstract".equals(this.extensionModifier().modifier());
    }

    default <T> T accept(QueenASTVisitor<? extends T> visitor) {
        return visitor.visitClassDeclarationNode(this);
    }
}
