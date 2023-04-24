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

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.QueenNode;
import org.queenlang.transpiler.nodes.expressions.AnnotationNode;
import org.queenlang.transpiler.nodes.types.TypeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Queen FieldDeclaration AST node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenFieldDeclarationNode implements FieldDeclarationNode {

    /**
     * Position in the original source code.
     */
    private final Position position;

    /**
     * Parent node.
     */
    private final QueenNode parent;

    /**
     * Annotations on top of this field.
     */
    private final List<AnnotationNode> annotations;

    /**
     * Access modifiers of this field.
     */
    private final List<ModifierNode> modifiers;

    /**
     * Type of the field declaration.
     */
    private final TypeNode type;

    /**
     * Variable name and initializer expression.
     */
    private final VariableDeclaratorNode variable;

    public QueenFieldDeclarationNode(
        final Position position,
        final List<AnnotationNode> annotations,
        final List<ModifierNode> modifiers,
        final TypeNode type,
        final VariableDeclaratorNode variable
    ) {
        this(position, null, annotations, modifiers, type, variable);
    }

    private QueenFieldDeclarationNode(
        final Position position,
        final QueenNode parent,
        final List<AnnotationNode> annotations,
        final List<ModifierNode> modifiers,
        final TypeNode type,
        final VariableDeclaratorNode variable
    ) {
        this.position = position;
        this.parent = parent;
        this.annotations = annotations != null ? annotations.stream().map(
            a -> (AnnotationNode) a.withParent(this)
        ).collect(Collectors.toList()) : null;
        this.modifiers = modifiers != null ? modifiers.stream().map(
            m -> (ModifierNode) m.withParent(this)
        ).collect(Collectors.toList()) : null;;
        this.type = type != null ? (TypeNode) type.withParent(this) : null;
        this.variable = variable != null ? (VariableDeclaratorNode) variable.withParent(this) : null;
    }

    @Override
    public void addToJavaNode(final Node java) {
        final VariableDeclarator vd = new VariableDeclarator();
        this.type.addToJavaNode(vd);
        this.variable.addToJavaNode(vd);

        final FieldDeclaration field = new FieldDeclaration();
        field.addVariable(vd);
        if(java instanceof ClassOrInterfaceDeclaration) {
            ((ClassOrInterfaceDeclaration) java).addMember(field);
        } else {
            ((ObjectCreationExpr) java).addAnonymousClassBody(field);
        }

        this.annotations.forEach(a -> a.addToJavaNode(field));
        this.modifiers.forEach(m -> m.addToJavaNode(field));
    }

    @Override
    public String name() {
        return "todo...";
    }

    @Override
    public Position position() {
        return this.position;
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
    public TypeNode type() {
        return this.type;
    }

    @Override
    public VariableDeclaratorNode variable() {
        return this.variable;
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
        children.add(this.type);
        children.add(this.variable);
        return children;
    }

    @Override
    public QueenNode withParent(final QueenNode parent) {
        return new QueenFieldDeclarationNode(
            this.position,
            parent,
            this.annotations,
            this.modifiers,
            this.type,
            this.variable
        );
    }

    @Override
    public QueenNode parent() {
        return this.parent;
    }
}
