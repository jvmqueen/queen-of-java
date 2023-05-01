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
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.LocalClassDeclarationStmt;
import org.queenlang.transpiler.nodes.*;
import org.queenlang.transpiler.nodes.expressions.AnnotationNode;
import org.queenlang.transpiler.nodes.statements.StatementNode;
import org.queenlang.transpiler.nodes.types.ClassOrInterfaceTypeNode;
import org.queenlang.transpiler.nodes.types.TypeParameterNode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Queen ClassDeclaration AST node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenClassDeclarationNode implements ClassDeclarationNode {

    /**
     * Position in the original source code.
     */
    private final Position position;

    /**
     * Parent of this node.
     */
    private final QueenNode parent;

    /**
     * Annotations on top of this class.
     */
    private final List<AnnotationNode> annotations;

    /**
     * Access modifiers of this class.
     */
    private final List<ModifierNode> accessModifiers;

    /**
     * Extension modifier (abstract or final).
     */
    private final ModifierNode extensionModifier;

    /**
     * Name of this type.
     */
    private final String name;

    /**
     * Class type params.
     */
    private final List<TypeParameterNode> typeParams;

    /**
     * Type which is extended.
     */
    private final ClassOrInterfaceTypeNode extendsType;

    /**
     * Interfaces this type implements.
     */
    private final List<ClassOrInterfaceTypeNode> of;

    /**
     * The body.
     */
    private final ClassBodyNode body;

    /**
     * Ctor.
     * @param position Position in the orifinal source code.
     * @param annotations Annotation nodes on top of this type.
     * @param accessModifiers Access modifiers of this class.
     * @param extensionModifier Extension modifier (abstract or final).
     * @param name Name.
     * @param extendsType Type extended.
     * @param typeParams Class type params.
     * @param of Types implemented.
     * @param body The body.
     */
    public QueenClassDeclarationNode(
        final Position position,
        final List<AnnotationNode> annotations,
        final List<ModifierNode> accessModifiers,
        final ModifierNode extensionModifier,
        final String name,
        final List<TypeParameterNode> typeParams,
        final ClassOrInterfaceTypeNode extendsType,
        final List<ClassOrInterfaceTypeNode> of,
        final ClassBodyNode body
    ) {
        this(position, null, annotations, accessModifiers, extensionModifier, name, typeParams, extendsType, of, body);
    }

    /**
     * Ctor.
     * @param position Position in the orifinal source code.
     * @param parent Parent of this class declaration node.
     * @param annotations Annotation nodes on top of this type.
     * @param accessModifiers Access modifiers of this class.
     * @param extensionModifier Extension modifier (abstract or final).
     * @param name Name.
     * @param extendsType Type extended.
     * @param typeParams Class type params.
     * @param of Types implemented.
     * @param body The body.
     */
    private QueenClassDeclarationNode(
        final Position position,
        final QueenNode parent,
        final List<AnnotationNode> annotations,
        final List<ModifierNode> accessModifiers,
        final ModifierNode extensionModifier,
        final String name,
        final List<TypeParameterNode> typeParams,
        final ClassOrInterfaceTypeNode extendsType,
        final List<ClassOrInterfaceTypeNode> of,
        final ClassBodyNode body
    ) {
        this.position = position;
        this.parent = parent;
        this.annotations = annotations != null ? annotations.stream().map(
            a -> (AnnotationNode) a.withParent(this)
        ).collect(Collectors.toList()) : null;
        this.accessModifiers = accessModifiers != null ? accessModifiers.stream().map(
            am -> (ModifierNode) am.withParent(this)
        ).collect(Collectors.toList()) : null;
        this.extensionModifier = extensionModifier != null ? (ModifierNode) extensionModifier.withParent(this) : null;
        this.name = name;
        this.typeParams = typeParams != null ? typeParams.stream().map(
            tp -> (TypeParameterNode) tp.withParent(this)
        ).collect(Collectors.toList()) : null;
        this.extendsType = extendsType != null ? (ClassOrInterfaceTypeNode) extendsType.withParent(this) : null;
        this.of = of != null ? of.stream().map(
            o -> (ClassOrInterfaceTypeNode) o.withParent(this)
        ).collect(Collectors.toList()) : null;
        this.body = body != null ? (ClassBodyNode) body.withParent(this) : null;
    }

    @Override
    public void addToJavaNode(final Node java) {
        if(java instanceof CompilationUnit) {
            ((CompilationUnit) java).addType(this.toJavaClass());
        } else if(java instanceof ClassOrInterfaceDeclaration) {
            ((ClassOrInterfaceDeclaration) java).addMember(this.toJavaClass());
        } else if(java instanceof AnnotationDeclaration) {
            ((AnnotationDeclaration) java).addMember(this.toJavaClass());
        } else if(java instanceof BlockStmt) {
            ((BlockStmt) java).addStatement(
                new LocalClassDeclarationStmt(this.toJavaClass())
            );
        } else if(java instanceof ObjectCreationExpr) {
            ((ObjectCreationExpr) java).addAnonymousClassBody(this.toJavaClass());
        }
    }

    /**
     * Turn it into a JavaParser class declaration.
     * @return ClassOrInterfaceDeclaration.
     */
    private ClassOrInterfaceDeclaration toJavaClass() {
        final ClassOrInterfaceDeclaration clazz = new ClassOrInterfaceDeclaration();
        clazz.setName(this.name);
        clazz.removeModifier(Modifier.Keyword.PUBLIC);
        this.typeParams.forEach(tp -> tp.addToJavaNode(clazz));
        if(this.extendsType != null) {
            this.extendsType.addToJavaNode(clazz);
        }
        if(this.of != null && this.of.size() > 0) {
            this.of.forEach(o -> o.addToJavaNode(clazz));
        }
        this.annotations.forEach(a -> a.addToJavaNode(clazz));
        this.accessModifiers.forEach(am -> am.addToJavaNode(clazz));
        this.extensionModifier.addToJavaNode(clazz);
        this.body.addToJavaNode(clazz);
        return clazz;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public Position position() {
        return this.position;
    }

    @Override
    public List<TypeParameterNode> typeParameters() {
        return this.typeParams;
    }

    @Override
    public List<AnnotationNode> annotations() {
        return this.annotations;
    }

    @Override
    public List<ModifierNode> modifiers() {
        return this.accessModifiers;
    }

    @Override
    public ModifierNode extensionModifier() {
        return this.extensionModifier;
    }

    @Override
    public ClassOrInterfaceTypeNode extendsType() {
        return this.extendsType;
    }

    @Override
    public List<ClassOrInterfaceTypeNode> of() {
        return this.of;
    }

    @Override
    public ClassBodyNode body() {
        return this.body;
    }

    @Override
    public List<QueenNode> children() {
        final List<QueenNode> children = new ArrayList<>();
        if(this.annotations != null) {
            children.addAll(this.annotations);
        }
        if(this.accessModifiers != null) {
            children.addAll(this.accessModifiers);
        }
        children.add(this.extensionModifier);
        if(this.typeParams != null) {
            children.addAll(this.typeParams);
        }
        children.add(this.extendsType);
        if(this.of != null) {
            children.addAll(this.of);
        }
        children.add(this.body);
        return children;
    }

    @Override
    public QueenNode withParent(final QueenNode parent) {
        return new QueenClassDeclarationNode(
            this.position,
            parent,
            this.annotations,
            this.accessModifiers,
            this.extensionModifier,
            this.name,
            this.typeParams,
            this.extendsType,
            this.of,
            this.body
        );
    }

    @Override
    public QueenNode parent() {
        return this.parent;
    }

    @Override
    public QueenNode resolve(final QueenReferenceNode reference, final ResolutionContext resolutionContext) {
        if (resolutionContext.alreadyVisited(this)) {
            return null;
        }
        resolutionContext.add(this);
        QueenNode resolved = null;
        if(reference instanceof NameNode) {
            resolved = this.body.resolve(reference, resolutionContext);
        }
        if(resolved == null && this.parent != null && this.parent instanceof CompilationUnitNode) {
            return this.parent.resolve(reference, resolutionContext);
        }
        return resolved;
    }
}
