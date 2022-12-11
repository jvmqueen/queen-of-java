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

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import java.util.List;

/**
 * Queen AnnotationDeclaration AST node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 * @todo #10:30min Don't forget to unit test this class.
 * @todo #10:30min Finish implementing the body here.
 */
public final class QueenAnnotationTypeDeclarationNode implements QueenInterfaceDeclarationNode {
    /**
     * Annotations on top of this annotation declaration.
     */
    private final List<QueenAnnotationNode> annotations;

    /**
     * Modifiers of this annotation.
     */
    private final List<QueenInterfaceModifierNode> modifiers;

    /**
     * Name of this type.
     */
    private final String name;

    /**
     * Ctor.
     * @param annotations Annotation nodes on top of this type.
     * @param modifiers Modifiers of this annotation.
     * @param name Name.
     */
    public QueenAnnotationTypeDeclarationNode(
        final List<QueenAnnotationNode> annotations,
        final List<QueenInterfaceModifierNode> modifiers,
        final String name
    ) {
        this.annotations = annotations;
        this.modifiers = modifiers;
        this.name = name;
    }

    @Override
    public void addToJavaNode(final Node java) {
        if(java instanceof CompilationUnit) {
            ((CompilationUnit) java).addType(this.toJavaAnnotation());
        } else if(java instanceof ClassOrInterfaceDeclaration) {
            ((ClassOrInterfaceDeclaration) java).addMember(this.toJavaAnnotation());
        }
    }

    /**
     * Turn it into a JavaParser annotation declaration.
     * @return AnnotationDeclaration.
     */
    private AnnotationDeclaration toJavaAnnotation() {
        final AnnotationDeclaration annotationDeclaration = new AnnotationDeclaration();
        annotationDeclaration.setName(this.name);
        this.annotations.forEach(a -> a.addToJavaNode(annotationDeclaration));
        this.modifiers.forEach(m -> m.addToJavaNode(annotationDeclaration));
        return annotationDeclaration;
    }
}
