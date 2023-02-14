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

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.AnnotationMemberDeclaration;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.QueenAnnotationNode;

import java.util.List;

/**
 * Queen AnnotationElement AST Node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenAnnotationElementDeclarationNode implements QueenAnnotationTypeMemberDeclarationNode {

    /**
     * Position in the original source code.
     */
    private final Position position;

    /**
     * Annotations on top of this element.
     */
    private final List<QueenAnnotationNode> annotations;

    /**
     * Modifiers of this element.
     */
    private final List<QueenModifierNode> modifiers;

    /**
     * Type of this annotation element.
     */
    private final String type;

    /**
     * Name of this annotation element.
     */
    private final String name;

    /**
     * Default value of the element.
     */
    private final String defaultValue;


    public QueenAnnotationElementDeclarationNode(
        final Position position,
        final List<QueenAnnotationNode> annotations,
        final List<QueenModifierNode> modifiers,
        final String type,
        final String name,
        final String defaultValue
    ) {
        this.position = position;
        this.annotations = annotations;
        this.modifiers = modifiers;
        this.type = type;
        this.name = name;
        this.defaultValue = defaultValue;
    }

    @Override
    public void addToJavaNode(final Node java) {
        final AnnotationMemberDeclaration annotationMemberDeclaration = new AnnotationMemberDeclaration();
        this.annotations.forEach(
            a -> a.addToJavaNode(annotationMemberDeclaration)
        );
        this.modifiers.forEach(
            m -> m.addToJavaNode(annotationMemberDeclaration)
        );
        annotationMemberDeclaration.setType(this.type);
        annotationMemberDeclaration.setName(this.name);
        if(this.defaultValue != null) {
            annotationMemberDeclaration.setDefaultValue(
                StaticJavaParser.parseExpression(this.defaultValue)
            );
        }
        ((AnnotationDeclaration) java).addMember(annotationMemberDeclaration);
    }

    @Override
    public Position position() {
        return this.position;
    }

    @Override
    public String name() {
        return this.name;
    }
}
