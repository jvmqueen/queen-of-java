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

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import org.queenlang.transpiler.nodes.*;
import org.queenlang.transpiler.nodes.expressions.AnnotationNode;
import org.queenlang.transpiler.nodes.types.ClassOrInterfaceTypeNode;
import org.queenlang.transpiler.nodes.types.TypeParameterNode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Queen NormalInterfaceDeclaration AST node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenNormalInterfaceDeclarationNode implements NormalInterfaceDeclarationNode {

    /**
     * Position in the original source code.
     */
    private final Position position;

    /**
     * Parent node.
     */
    private final QueenNode parent;

    /**
     * Annotations on top of this interface.
     */
    private final List<AnnotationNode> annotations;

    /**
     * Modifiers of this interface.
     */
    private final List<ModifierNode> modifiers;

    /**
     * Name of this type.
     */
    private final String name;

    /**
     * Interface type params.
     */
    private final List<TypeParameterNode> typeParams;

    /**
     * Types which are extended (an interface can extend more interfaces).
     */
    private final List<ClassOrInterfaceTypeNode> extendsTypes;

    /**
     * The body.
     */
    private final InterfaceBodyNode body;

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
        final Position position,
        final List<AnnotationNode> annotations,
        final List<ModifierNode> modifiers,
        final String name,
        final List<TypeParameterNode> typeParams,
        final List<ClassOrInterfaceTypeNode> extendsTypes,
        final InterfaceBodyNode body
    ) {
        this(
            position,
            null,
            annotations,
            modifiers,
            name,
            typeParams,
            extendsTypes,
            body
        );
    }

    private QueenNormalInterfaceDeclarationNode(
        final Position position,
        final QueenNode parent,
        final List<AnnotationNode> annotations,
        final List<ModifierNode> modifiers,
        final String name,
        final List<TypeParameterNode> typeParams,
        final List<ClassOrInterfaceTypeNode> extendsTypes,
        final InterfaceBodyNode body
    ) {
        this.position = position;
        this.parent = parent;
        this.annotations = annotations != null ? annotations.stream().map(
            a -> (AnnotationNode) a.withParent(this)
        ).collect(Collectors.toList()) : null;
        this.modifiers = modifiers != null ? modifiers.stream().map(
            m -> (ModifierNode) m.withParent(this)
        ).collect(Collectors.toList()) : null;
        this.name = name;
        this.typeParams = typeParams != null ? typeParams.stream().map(
            tp -> (TypeParameterNode) tp.withParent(this)
        ).collect(Collectors.toList()) : null;
        this.extendsTypes = extendsTypes != null ? extendsTypes.stream().map(
            et -> (ClassOrInterfaceTypeNode) et.withParent(this)
        ).collect(Collectors.toList()) : null;
        this.body = body != null ? (InterfaceBodyNode) body.withParent(this) : null;
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

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public Position position() {
        return this.position;
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
        this.typeParams.forEach(tp -> tp.addToJavaNode(inter));
        if(this.extendsTypes != null && this.extendsTypes.size() > 0) {
            this.extendsTypes.forEach(et -> et.addToJavaNode(inter));
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

    @Override
    public List<AnnotationNode> annotations() {
        return this.annotations;
    }

    @Override
    public List<ModifierNode> modifiers() {
        return this.modifiers;
    }

    @Override
    public List<ClassOrInterfaceTypeNode> extendsTypes() {
        return this.extendsTypes;
    }

    @Override
    public InterfaceBodyNode body() {
        return this.body;
    }

    @Override
    public List<TypeParameterNode> typeParameters() {
        return this.typeParams;
    }

    @Override
    public List<QueenNode> children() {
        final List<QueenNode> children = new ArrayList<>();
        if(this.annotations != null) {
            children.addAll(this.annotations);
        }
        if(this.modifiers != null) {
            children.addAll(this.modifiers);
        }
        if(this.typeParams != null) {
            children.addAll(this.typeParams);
        }
        if(this.extendsTypes != null) {
            children.addAll(this.extendsTypes);
        }
        children.add(this.body);
        return children;
    }

    @Override
    public QueenNode withParent(final QueenNode parent) {
        return new QueenNormalInterfaceDeclarationNode(
            this.position,
            parent,
            this.annotations,
            this.modifiers,
            this.name,
            this.typeParams,
            this.extendsTypes,
            this.body
        );
    }

    @Override
    public QueenNode parent() {
        return this.parent;
    }
}
