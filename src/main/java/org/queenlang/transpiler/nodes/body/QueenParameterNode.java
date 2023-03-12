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
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithParameters;
import com.github.javaparser.ast.type.UnknownType;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.expressions.QueenAnnotationNode;
import org.queenlang.transpiler.nodes.types.TypeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Queen parameter AST node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 * @todo #33:60min Write unit tests for QueenParameterNode.
 */
public final class QueenParameterNode implements ParameterNode {
    private final Position position;
    private final List<QueenAnnotationNode> annotations;
    private final List<QueenModifierNode> modifiers;
    private final QueenVariableDeclaratorId variableDeclaratorId;
    private final TypeNode type;
    private final List<QueenAnnotationNode> varArgsAnnotations;
    private final boolean varArgs;

    public QueenParameterNode(
        final Position position,
        final QueenVariableDeclaratorId variableDeclaratorId
    ) {
        this(position, new ArrayList<>(), new ArrayList<>(), null, variableDeclaratorId, false);
    }

    public QueenParameterNode(
        final Position position,
        final List<QueenAnnotationNode> annotations,
        final List<QueenModifierNode> modifiers,
        final TypeNode type,
        final QueenVariableDeclaratorId variableDeclaratorId
    ) {
        this(position, annotations, modifiers, type, variableDeclaratorId, false);
    }

    public QueenParameterNode(
        final Position position,
        final List<QueenAnnotationNode> annotations,
        final List<QueenModifierNode> modifiers,
        final TypeNode type,
        final QueenVariableDeclaratorId variableDeclaratorId,
        final boolean varArgs
    ) {
        this(position, annotations, modifiers, type, variableDeclaratorId, new ArrayList<>(), varArgs);
    }

    public QueenParameterNode(
        final Position position,
        final List<QueenAnnotationNode> annotations,
        final List<QueenModifierNode> modifiers,
        final TypeNode type,
        final QueenVariableDeclaratorId variableDeclaratorId,
        final List<QueenAnnotationNode> varArgsAnnotations,
        final boolean varArgs
    ) {
        this.position = position;
        this.annotations = annotations;
        this.modifiers = modifiers;
        this.type = type;
        this.variableDeclaratorId = variableDeclaratorId;
        this.varArgsAnnotations = varArgsAnnotations;
        this.varArgs = varArgs;
    }

    @Override
    public void addToJavaNode(final Node java) {
        final Parameter parameter = new Parameter();
        this.annotations.forEach( a -> a.addToJavaNode(parameter));
        this.modifiers.forEach(m -> m.addToJavaNode(parameter));
        if(this.type != null) {
            this.type.addToJavaNode(parameter);
        } else {
            parameter.setType(new UnknownType());
        }
        this.variableDeclaratorId.addToJavaNode(parameter);
        parameter.setVarArgs(this.varArgs);
        if(this.varArgsAnnotations != null) {
            parameter.setVarArgsAnnotations(
                new NodeList<>(
                    this.varArgsAnnotations.stream().map(
                        varga -> (AnnotationExpr) varga.toJavaExpression()
                    ).collect(Collectors.toList())
                )
            );
        }
        ((NodeWithParameters) java).addParameter(parameter);
    }

    @Override
    public Position position() {
        return this.position;
    }

    @Override
    public List<QueenAnnotationNode> annotations() {
        return this.annotations;
    }

    @Override
    public List<QueenModifierNode> modifiers() {
        return this.modifiers;
    }

    @Override
    public QueenVariableDeclaratorId variableDeclaratorId() {
        return this.variableDeclaratorId;
    }

    @Override
    public TypeNode type() {
        return this.type;
    }

    @Override
    public List<QueenAnnotationNode> varArgsAnnotations() {
        return this.varArgsAnnotations;
    }

    @Override
    public boolean varArgs() {
        return this.varArgs;
    }
}
