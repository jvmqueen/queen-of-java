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
package org.queenlang.transpiler.nodes.types;

import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.QueenNode;
import org.queenlang.transpiler.nodes.expressions.AnnotationNode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Queen class or interface reference type.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenClassOrInterfaceTypeNode implements ClassOrInterfaceTypeNode {

    /**
     * Position in the original source code.
     */
    private final Position position;

    /**
     * Parent node.
     */
    private final QueenNode parent;

    /**
     * Is it an interface type or class type?
     */
    private final boolean interfaceType;

    /**
     * Scope of this reference type (what comes before the dot). E.g.
     * java.util.List (util is the scope of List).
     */
    private final ClassOrInterfaceTypeNode scope;

    /**
     * Annotations on top of this reference type.
     */
    private final List<AnnotationNode> annotations;

    /**
     * Name of this reference type.
     */
    private final String name;

    /**
     * Type arguments of this reference type.
     */
    private final List<TypeNode> typeArguments;

    private final boolean hasDiamondOperator;

    public QueenClassOrInterfaceTypeNode(
        final Position position,
        final String name
    ) {
        this(position, null, name);
    }

    public QueenClassOrInterfaceTypeNode(
        final Position position,
        final ClassOrInterfaceTypeNode scope,
        final String name
    ) {
        this(position, false, scope, new ArrayList<>(), name, new ArrayList<>(), false);
    }

    public QueenClassOrInterfaceTypeNode(
        final Position position,
        final boolean interfaceType,
        final List<AnnotationNode> annotations,
        final String name,
        final List<TypeNode> typeArguments
    ) {
        this(position, interfaceType, null, annotations, name, typeArguments, false);
    }

    public QueenClassOrInterfaceTypeNode(
        final Position position,
        final boolean interfaceType,
        final ClassOrInterfaceTypeNode scope,
        final List<AnnotationNode> annotations,
        final String name,
        final List<TypeNode> typeArguments,
        final boolean hasDiamondOperator
    ) {
        this(position, null, interfaceType, scope, annotations, name, typeArguments, hasDiamondOperator);
    }

    private QueenClassOrInterfaceTypeNode(
        final Position position,
        final QueenNode parent,
        final boolean interfaceType,
        final ClassOrInterfaceTypeNode scope,
        final List<AnnotationNode> annotations,
        final String name,
        final List<TypeNode> typeArguments,
        final boolean hasDiamondOperator
    ) {
        this.position = position;
        this.parent = parent;
        this.interfaceType = interfaceType;
        this.scope = scope != null ? (ClassOrInterfaceTypeNode) scope.withParent(this) : null;
        this.annotations = annotations != null ? annotations.stream().map(
            a -> (AnnotationNode) a.withParent(this)
        ).collect(Collectors.toList()) : null;
        this.name = name;
        this.typeArguments = typeArguments != null ? typeArguments.stream().map(
            ta -> (TypeNode) ta.withParent(this)
        ).collect(Collectors.toList()) : null;
        this.hasDiamondOperator = hasDiamondOperator;
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
    public List<TypeNode> typeArguments() {
        return this.typeArguments;
    }

    @Override
    public boolean hasDiamondOperator() {
        return this.hasDiamondOperator;
    }

    @Override
    public boolean interfaceType() {
        return this.interfaceType;
    }

    @Override
    public ClassOrInterfaceTypeNode scope() {
        return this.scope;
    }

    @Override
    public String simpleName() {
        return this.name;
    }

    @Override
    public List<QueenNode> children() {
        final List<QueenNode> children = new ArrayList<>();
        children.add(this.scope);
        if(this.annotations != null) {
            children.addAll(this.annotations);
        }
        if(this.typeArguments != null) {
            children.addAll(this.typeArguments);
        }
        return children;
    }

    @Override
    public QueenNode withParent(final QueenNode parent) {
        return new QueenClassOrInterfaceTypeNode(
            this.position,
            parent,
            this.interfaceType,
            this.scope,
            this.annotations,
            this.name,
            this.typeArguments,
            this.hasDiamondOperator
        );
    }

    @Override
    public QueenNode parent() {
        return this.parent;
    }
}
