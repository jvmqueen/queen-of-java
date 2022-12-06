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

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;

import java.util.List;

/**
 * Queen FieldDeclaration AST node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 * @todo #29:60min Continue with VariableDeclarationList for fields.
 */
public final class QueenFieldDeclarationNode implements QueenClassMemberDeclarationNode {

    /**
     * Annotations on top of this class.
     */
    private final List<QueenAnnotationNode> annotations;

    /**
     * Access modifiers of this class.
     */
    private final List<QueenFieldModifierNode> modifiers;

    /**
     * Type of the field declaration.
     */
    private final String type;

    public QueenFieldDeclarationNode(
        final List<QueenAnnotationNode> annotations,
        final List<QueenFieldModifierNode> modifiers,
        final String type
    ) {
        this.annotations = annotations;
        this.modifiers = modifiers;
        this.type = type;
    }

    @Override
    public void addToJavaNode(final Node java) {
        ClassOrInterfaceDeclaration clazz = (ClassOrInterfaceDeclaration) java;
        FieldDeclaration field = clazz.addField(this.type, "X");
        this.annotations.forEach(a -> a.addToJavaNode(field));
        this.modifiers.forEach(m -> m.addToJavaNode(field));
    }
}