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

import org.queenlang.transpiler.nodes.types.TypeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Queen parameter AST node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenParameterNode implements ParameterNode {
    private final Position position;
    private final QueenNode parent;
    private final List<AnnotationNode> annotations;
    private final List<ModifierNode> modifiers;
    private final VariableDeclaratorId variableDeclaratorId;
    private final TypeNode type;
    private final List<AnnotationNode> varArgsAnnotations;
    private final boolean varArgs;

    public QueenParameterNode(
        final Position position,
        final VariableDeclaratorId variableDeclaratorId
    ) {
        this(position, new ArrayList<>(), new ArrayList<>(), null, variableDeclaratorId, false);
    }

    public QueenParameterNode(
        final Position position,
        final List<AnnotationNode> annotations,
        final List<ModifierNode> modifiers,
        final TypeNode type,
        final VariableDeclaratorId variableDeclaratorId
    ) {
        this(position, annotations, modifiers, type, variableDeclaratorId, false);
    }

    public QueenParameterNode(
        final Position position,
        final List<AnnotationNode> annotations,
        final List<ModifierNode> modifiers,
        final TypeNode type,
        final VariableDeclaratorId variableDeclaratorId,
        final boolean varArgs
    ) {
        this(position, annotations, modifiers, type, variableDeclaratorId, new ArrayList<>(), varArgs);
    }

    public QueenParameterNode(
        final Position position,
        final List<AnnotationNode> annotations,
        final List<ModifierNode> modifiers,
        final TypeNode type,
        final VariableDeclaratorId variableDeclaratorId,
        final List<AnnotationNode> varArgsAnnotations,
        final boolean varArgs
    ) {
        this(
            position,
            null,
            annotations,
            modifiers,
            type,
            variableDeclaratorId,
            varArgsAnnotations,
            varArgs
        );
    }

    private QueenParameterNode(
        final Position position,
        final QueenNode parent,
        final List<AnnotationNode> annotations,
        final List<ModifierNode> modifiers,
        final TypeNode type,
        final VariableDeclaratorId variableDeclaratorId,
        final List<AnnotationNode> varArgsAnnotations,
        final boolean varArgs
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
        this.variableDeclaratorId = variableDeclaratorId != null ? (VariableDeclaratorId) variableDeclaratorId.withParent(this) : null;
        this.varArgsAnnotations = varArgsAnnotations != null ? varArgsAnnotations.stream().map(
            varA -> (AnnotationNode) varA.withParent(this)
        ).collect(Collectors.toList()) : null;
        this.varArgs = varArgs;
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
    public VariableDeclaratorId variableDeclaratorId() {
        return this.variableDeclaratorId;
    }

    @Override
    public TypeNode type() {
        return this.type;
    }

    @Override
    public List<AnnotationNode> varArgsAnnotations() {
        return this.varArgsAnnotations;
    }

    @Override
    public boolean varArgs() {
        return this.varArgs;
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
        children.add(this.variableDeclaratorId);
        children.add(this.type);
        if(this.varArgsAnnotations != null) {
            children.addAll(this.varArgsAnnotations);
        }
        return children;
    }

    @Override
    public QueenNode withParent(final QueenNode parent) {
        return new QueenParameterNode(
            this.position,
            parent,
            this.annotations,
            this.modifiers,
            this.type,
            this.variableDeclaratorId,
            this.varArgsAnnotations,
            this.varArgs
        );
    }

    @Override
    public QueenNode parent() {
        return this.parent;
    }
}
