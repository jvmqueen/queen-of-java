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

import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.QueenNode;
import org.queenlang.transpiler.nodes.expressions.AnnotationNode;
import org.queenlang.transpiler.nodes.expressions.ExpressionNode;
import org.queenlang.transpiler.nodes.types.TypeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Queen AnnotationElement AST Node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenAnnotationElementDeclarationNode implements AnnotationElementDeclarationNode {

    /**
     * Position in the original source code.
     */
    private final Position position;

    /**
     * Parent of this node.
     */
    private final QueenNode parent;

    /**
     * Annotations on top of this element.
     */
    private final List<AnnotationNode> annotations;

    /**
     * Modifiers of this element.
     */
    private final List<ModifierNode> modifiers;

    /**
     * Type of this annotation element.
     */
    private final TypeNode type;

    /**
     * Name of this annotation element.
     */
    private final String name;

    /**
     * Default value of the element.
     */
    private final ExpressionNode defaultValue;


    public QueenAnnotationElementDeclarationNode(
        final Position position,
        final List<AnnotationNode> annotations,
        final List<ModifierNode> modifiers,
        final TypeNode type,
        final String name,
        final ExpressionNode defaultValue
    ) {
        this(position, null, annotations, modifiers, type, name, defaultValue);
    }

    private QueenAnnotationElementDeclarationNode(
        final Position position,
        final QueenNode parent,
        final List<AnnotationNode> annotations,
        final List<ModifierNode> modifiers,
        final TypeNode type,
        final String name,
        final ExpressionNode defaultValue
    ) {
        this.position = position;
        this.parent = parent;
        this.annotations = annotations != null ? annotations.stream().map(
            a -> (AnnotationNode) a.withParent(this)
        ).collect(Collectors.toList()) : null;
        this.modifiers = modifiers != null ? modifiers.stream().map(
            m -> (ModifierNode) m.withParent(this)
        ).collect(Collectors.toList()) : null;
        this.type = type != null ? (TypeNode) type.withParent(this) : null;
        this.name = name;
        this.defaultValue = defaultValue != null ? (ExpressionNode) defaultValue.withParent(this) : null;
    }
    @Override
    public Position position() {
        return this.position;
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
        children.add(this.defaultValue);
        return children;
    }

    @Override
    public QueenNode withParent(final QueenNode parent) {
        return new QueenAnnotationElementDeclarationNode(
            this.position,
            parent,
            this.annotations,
            this.modifiers,
            this.type,
            this.name,
            this.defaultValue
        );
    }

    @Override
    public QueenNode parent() {
        return this.parent;
    }

    @Override
    public String name() {
        return this.name;
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
    public ExpressionNode defaultValue() {
        return this.defaultValue;
    }
}
