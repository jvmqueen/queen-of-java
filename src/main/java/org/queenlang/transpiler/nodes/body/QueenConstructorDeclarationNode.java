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

import org.queenlang.transpiler.nodes.*;
import org.queenlang.transpiler.nodes.expressions.AnnotationNode;
import org.queenlang.transpiler.nodes.statements.BlockStatements;
import org.queenlang.transpiler.nodes.statements.ExplicitConstructorInvocationNode;
import org.queenlang.transpiler.nodes.types.ExceptionTypeNode;
import org.queenlang.transpiler.nodes.types.TypeParameterNode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Queen ConstructorDeclaration AST Node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenConstructorDeclarationNode implements ConstructorDeclarationNode {

    private final Position position;

    private final QueenNode parent;

    private final List<AnnotationNode> annotations;

    private final ModifierNode modifier;

    private final List<TypeParameterNode> typeParams;

    private final String name;

    private final ParameterList parameters;

    private final List<ExceptionTypeNode> throwsList;

    private final ExplicitConstructorInvocationNode explicitConstructorInvocationNode;

    private final BlockStatements blockStatements;

    public QueenConstructorDeclarationNode(
        final Position position,
        final List<AnnotationNode> annotations,
        final ModifierNode modifier,
        final List<TypeParameterNode> typeParams,
        final String name,
        final ParameterList parameters,
        final List<ExceptionTypeNode> throwsList,
        final ExplicitConstructorInvocationNode explicitConstructorInvocationNode,
        final BlockStatements blockStatements
    ) {
        this(
            position,
            null,
            annotations,
            modifier,
            typeParams,
            name,
            parameters,
            throwsList,
            explicitConstructorInvocationNode,
            blockStatements
        );
    }

    private QueenConstructorDeclarationNode(
        final Position position,
        final QueenNode parent,
        final List<AnnotationNode> annotations,
        final ModifierNode modifier,
        final List<TypeParameterNode> typeParams,
        final String name,
        final ParameterList parameters,
        final List<ExceptionTypeNode> throwsList,
        final ExplicitConstructorInvocationNode explicitConstructorInvocationNode,
        final BlockStatements blockStatements
    ) {
        this.position = position;
        this.parent = parent;
        this.annotations = annotations != null ? annotations.stream().map(
            a -> (AnnotationNode) a.withParent(this)
        ).collect(Collectors.toList()) : null;
        this.modifier = modifier != null ? (ModifierNode) modifier.withParent(this) : null;
        this.typeParams = typeParams != null ? typeParams.stream().map(
            tp -> (TypeParameterNode) tp.withParent(this)
        ).collect(Collectors.toList()) : null;
        this.name = name;
        this.parameters = parameters != null ? (ParameterList) parameters.withParent(this) : null;
        this.throwsList = throwsList != null ? throwsList.stream().map(
            t -> (ExceptionTypeNode) t.withParent(this)
        ).collect(Collectors.toList()) : null;
        this.explicitConstructorInvocationNode = explicitConstructorInvocationNode != null ? (ExplicitConstructorInvocationNode) explicitConstructorInvocationNode.withParent(this) : null;
        this.blockStatements = blockStatements != null ? (BlockStatements) blockStatements.withParent(this) : null;
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
    public ModifierNode modifier() {
        return this.modifier;
    }

    @Override
    public List<TypeParameterNode> typeParams() {
        return this.typeParams;
    }

    @Override
    public ParameterList parameters() {
        return this.parameters;
    }

    @Override
    public List<ExceptionTypeNode> throwsList() {
        return this.throwsList;
    }

    @Override
    public ExplicitConstructorInvocationNode explicitConstructorInvocationNode() {
        return this.explicitConstructorInvocationNode;
    }

    @Override
    public BlockStatements blockStatements() {
        return this.blockStatements;
    }

    @Override
    public List<QueenNode> children() {
        final List<QueenNode> children = new ArrayList<>();
        if(this.annotations != null) {
            children.addAll(this.annotations);
        }
        children.add(this.modifier);
        if(this.typeParams != null) {
            children.addAll(this.typeParams);
        }
        if(this.parameters != null) {
            children.add(this.parameters);
        }
        if(this.throwsList != null) {
            children.addAll(this.throwsList);
        }
        children.add(this.explicitConstructorInvocationNode);
        children.add(this.blockStatements);
        return children;
    }

    @Override
    public QueenNode withParent(final QueenNode parent) {
        return new QueenConstructorDeclarationNode(
            this.position,
            parent,
            this.annotations,
            this.modifier,
            this.typeParams,
            this.name,
            this.parameters,
            this.throwsList,
            this.explicitConstructorInvocationNode,
            this.blockStatements
        );
    }

    @Override
    public QueenNode parent() {
        return this.parent;
    }
}
