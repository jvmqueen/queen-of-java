/**
 * Copyright (c) 2022-2032, Extremely Distributed Technologies S.R.L. Romania,
 *                          Silvia Maxima et Co.
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
package org.queenlang.queen.nodes.body;

import org.queenlang.queen.nodes.*;
import org.queenlang.queen.nodes.expressions.AnnotationNode;
import org.queenlang.queen.nodes.names.NameNode;
import org.queenlang.queen.nodes.statements.BlockStatements;
import org.queenlang.queen.nodes.types.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Queen MethodDeclaration AST Node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenMethodDeclarationNode implements MethodDeclarationNode {

    /**
     * Position in the original source code.
     */
    private final Position position;

    /**
     * Parent node.
     */
    private final QueenNode parent;

    /**
     * Annotations on top of this method.
     */
    private final List<AnnotationNode> annotations;

    /**
     * Access modifiers of this method.
     */
    private final List<ModifierNode> modifiers;

    /**
     * Return type.
     */
    private final TypeNode returnType;

    /**
     * Method type params.
     */
    private final List<TypeParameterNode> typeParams;

    /**
     * Method name.
     */
    private final String name;

    /**
     * Method parameters.
     */
    private final ParameterList parameters;

    /**
     * Thrown exceptions.
     */
    private final List<ExceptionTypeNode> throwsList;

    /**
     * Method body.
     */
    private final BlockStatements blockStatements;

    private final boolean interfaceDeclaration;

    public QueenMethodDeclarationNode(
        final Position position,
        final List<AnnotationNode> annotations,
        final List<ModifierNode> modifiers,
        final TypeNode returnType,
        final List<TypeParameterNode> typeParams,
        final String name,
        final ParameterList parameters,
        final List<ExceptionTypeNode> throwsList,
        final BlockStatements blockStatements,
        final boolean interfaceDeclaration
    ) {
        this(
            position,
            null,
            annotations,
            modifiers,
            returnType,
            typeParams,
            name,
            parameters,
            throwsList,
            blockStatements,
            interfaceDeclaration
        );
    }

    private QueenMethodDeclarationNode(
        final Position position,
        final QueenNode parent,
        final List<AnnotationNode> annotations,
        final List<ModifierNode> modifiers,
        final TypeNode returnType,
        final List<TypeParameterNode> typeParams,
        final String name,
        final ParameterList parameters,
        final List<ExceptionTypeNode> throwsList,
        final BlockStatements blockStatements,
        final boolean interfaceDeclaration
    ) {
        this.position = position;
        this.parent = parent;
        this.annotations = annotations;
        this.modifiers = modifiers;
        this.returnType = returnType;
        this.typeParams = typeParams;
        this.name = name;
        this.parameters = parameters;
        this.throwsList = throwsList;
        this.blockStatements = blockStatements;
        this.interfaceDeclaration = interfaceDeclaration;
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
        if(this.parameters != null) {
            children.add(this.parameters);
        }
        if(this.throwsList != null) {
            children.addAll(this.throwsList);
        }
        children.add(this.returnType);
        children.add(this.blockStatements);
        return children;
    }

    @Override
    public QueenNode parent() {
        return this.parent;
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
        return this.modifiers;
    }

    @Override
    public TypeNode returnType() {
        return this.returnType;
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
    public BlockStatements blockStatements() {
        return this.blockStatements;
    }

    @Override
    public boolean interfaceDeclaration() {
        return this.interfaceDeclaration;
    }

    @Override
    public QueenNode resolve(final QueenReferenceNode reference, boolean goUp) {
        QueenNode resolved = null;
        if(reference instanceof NameNode) {
            for(final ParameterNode param : this.parameters) {
                final String variableName = param.variableDeclaratorId().name();
                if(variableName.equals(((NameNode) reference).name())) {
                    resolved =  param;
                }
                if(resolved != null) {
                    break;
                }
            }
        }
        if(resolved == null && goUp) {
            return this.parent.resolve(reference, goUp);
        }
        return resolved;
    }
}
