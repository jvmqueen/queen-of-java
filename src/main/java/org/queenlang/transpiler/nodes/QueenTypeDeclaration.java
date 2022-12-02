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
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import java.util.List;

/**
 * Queen TypeDeclaration AST node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 * @todo #10:60min Handle access modifiers for the type declaration.
 * @todo #8:60min Handle TypeDeclaration Parameters (for generic types).
 * @todo #8:60min Handle the TypeBody AST node further.
 * @todo #8:60min Write unit tests for this class.
 */
public final class QueenTypeDeclaration implements QueenNode {

    /**
     * Annotations on top of this type.
     */
    private List<QueenNode> annotations;

    /**
     * Type: implementation, interface or @interface (annotation).
     */
    private final String type;

    /**
     * Name of this type.
     */
    private final String name;

    /**
     * Types which are extended (an interface can extend more interfaces).
     */
    private final List<String> extendsTypes;

    /**
     * Interfaces this type implements.
     */
    private final List<String> of;

    /**
     * Ctor.
     * @param annotations Annotation nodes on top of this type.
     * @param type Type (implementation, interface, @interface).
     * @param name Name.
     * @param extendsTypes Types extended.
     * @param of Types implemented.
     */
    public QueenTypeDeclaration(
        final List<QueenNode> annotations,
        final String type,
        final String name,
        final List<String> extendsTypes,
        final List<String> of
    ) {
        this.annotations = annotations;
        this.type = type;
        this.name = name;
        this.extendsTypes = extendsTypes;
        this.of = of;
    }

    @Override
    public void addToJavaNode(final Node java) {
        if("implementation".equalsIgnoreCase(this.type)) {
            ClassOrInterfaceDeclaration clazz = ((CompilationUnit) java)
                .addClass(this.name);
            if(this.extendsTypes != null && this.extendsTypes.size() > 0) {
                clazz.addExtendedType(this.extendsTypes.get(0));
            }
            if(this.of != null && this.of.size() > 0) {
                this.of.forEach(clazz::addImplementedType);
            }
            this.annotations.forEach(
                a -> a.addToJavaNode(clazz)
            );
        } else if("interface".equalsIgnoreCase(this.type)) {
            ClassOrInterfaceDeclaration clazz = ((CompilationUnit) java)
                .addInterface(this.name);
            if(this.extendsTypes != null && this.extendsTypes.size() > 0) {
                this.extendsTypes.forEach(clazz::addExtendedType);
            }
            this.annotations.forEach(
                a -> a.addToJavaNode(clazz)
            );
        } else if("@interface".equalsIgnoreCase(this.type)) {
            final AnnotationDeclaration annotationDeclaration = ((CompilationUnit) java).addAnnotationDeclaration(this.name);
            this.annotations.forEach(
                a -> a.addToJavaNode((annotationDeclaration))
            );
        }
    }
}
