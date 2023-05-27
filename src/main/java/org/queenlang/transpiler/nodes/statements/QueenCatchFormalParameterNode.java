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
package org.queenlang.transpiler.nodes.statements;

import org.queenlang.transpiler.nodes.*;
import org.queenlang.transpiler.nodes.body.ModifierNode;
import org.queenlang.transpiler.nodes.body.VariableDeclaratorId;
import org.queenlang.transpiler.nodes.expressions.AnnotationNode;
import org.queenlang.transpiler.nodes.types.TypeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Formal parameter of a Queen CatchClause AST Node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenCatchFormalParameterNode implements CatchFormalParameterNode {

    private final Position position;
    private final QueenNode parent;
    private final List<AnnotationNode> annotations;
    private final List<ModifierNode> modifiers;
    private final List<TypeNode> catchExceptionTypes;
    private final VariableDeclaratorId exceptionName;

    public QueenCatchFormalParameterNode(
        final Position position,
        final List<AnnotationNode> annotations,
        final List<ModifierNode> modifiers,
        final List<TypeNode> catchExceptionTypes,
        final VariableDeclaratorId exceptionName
    ) {
        this(
            position,
            null,
            annotations,
            modifiers,
            catchExceptionTypes,
            exceptionName
        );
    }

    private QueenCatchFormalParameterNode(
        final Position position,
        final QueenNode parent,
        final List<AnnotationNode> annotations,
        final List<ModifierNode> modifiers,
        final List<TypeNode> catchExceptionTypes,
        final VariableDeclaratorId exceptionName
    ) {
        this.position = position;
        this.parent = parent;
        this.annotations = annotations != null ? annotations.stream().map(
            a -> (AnnotationNode) a.withParent(this)
        ).collect(Collectors.toList()) : null;
        this.modifiers = modifiers != null ? modifiers.stream().map(
            m -> (ModifierNode) m.withParent(this)
        ).collect(Collectors.toList()) : null;
        this.catchExceptionTypes = catchExceptionTypes != null ? catchExceptionTypes.stream().map(
            cet -> (TypeNode) cet.withParent(this)
        ).collect(Collectors.toList()) : null;
        this.exceptionName = exceptionName != null ? (VariableDeclaratorId) exceptionName.withParent(this) : null;
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
        if(this.catchExceptionTypes != null) {
            children.addAll(this.catchExceptionTypes);
        }
        children.add(this.exceptionName);
        return children;
    }

    @Override
    public QueenNode withParent(final QueenNode parent) {
        return new QueenCatchFormalParameterNode(
            this.position,
            parent,
            this.annotations,
            this.modifiers,
            this.catchExceptionTypes,
            this.exceptionName
        );
    }

    @Override
    public QueenNode parent() {
        return this.parent;
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
    public List<TypeNode> catchExceptionTypes() {
        return this.catchExceptionTypes;
    }

    @Override
    public VariableDeclaratorId exceptionName() {
        return this.exceptionName;
    }
}
