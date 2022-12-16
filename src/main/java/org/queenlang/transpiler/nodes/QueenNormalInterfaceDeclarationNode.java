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
import com.github.javaparser.ast.stmt.BlockStmt;

import java.util.List;

/**
 * Queen NormalInterfaceDeclaration AST node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 * @todo #10:60min Don't forget to write some unit tests here.
 */
public final class QueenNormalInterfaceDeclarationNode implements QueenInterfaceDeclarationNode {
    /**
     * Annotations on top of this interface.
     */
    private final List<QueenAnnotationNode> annotations;

    /**
     * Modifiers of this interface.
     */
    private final List<QueenInterfaceModifierNode> modifiers;

    /**
     * Name of this type.
     */
    private final String name;

    /**
     * Interface type params.
     */
    private final List<String> typeParams;

    /**
     * Types which are extended (an interface can extend more interfaces).
     */
    private final List<String> extendsTypes;

    /**
     * The body.
     */
    private final QueenInterfaceBodyNode body;

    /**
     * Ctor.
     * @param annotations Annotation nodes on top of this type.
     * @param modifiers Modifiers on this interface.
     * @param name Name.
     * @param typeParams Type params.
     * @param extendsTypes Types extended.
     * @param body The body.
     */
    public QueenNormalInterfaceDeclarationNode(
        final List<QueenAnnotationNode> annotations,
        final List<QueenInterfaceModifierNode> modifiers,
        final String name,
        final List<String> typeParams,
        final List<String> extendsTypes,
        final QueenInterfaceBodyNode body
    ) {
        this.annotations = annotations;
        this.modifiers = modifiers;
        this.name = name;
        this.typeParams = typeParams;
        this.extendsTypes = extendsTypes;
        this.body = body;
    }

    @Override
    public void addToJavaNode(final Node java) {
        if(java instanceof CompilationUnit) {
            ((CompilationUnit) java).addType(this.toJavaInterface());
        } else if(java instanceof ClassOrInterfaceDeclaration) {
            ((ClassOrInterfaceDeclaration) java).addMember(this.toJavaInterface());
        } else if(java instanceof AnnotationDeclaration) {
            ((AnnotationDeclaration) java).addMember(this.toJavaInterface());
        }
    }

    /**
     * Turn it into a JavaParser interface declaration.
     * @return ClassOrInterfaceDeclaration.
     */
    private ClassOrInterfaceDeclaration toJavaInterface() {
        ClassOrInterfaceDeclaration inter = new ClassOrInterfaceDeclaration();
        inter.setInterface(true);
        inter.setName(this.name);
        inter.removeModifier(Modifier.Keyword.PUBLIC);
        this.typeParams.forEach(tp -> inter.addTypeParameter(tp));
        if(this.extendsTypes != null && this.extendsTypes.size() > 0) {
            this.extendsTypes.forEach(inter::addExtendedType);
        }
        this.annotations.forEach(
            a -> a.addToJavaNode(inter)
        );
        this.modifiers.forEach(
            m -> m.addToJavaNode(inter)
        );
        this.body.addToJavaNode(inter);

        return inter;
    }
}
