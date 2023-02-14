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

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import org.queenlang.transpiler.nodes.*;
import org.queenlang.transpiler.nodes.expressions.QueenAnnotationNode;
import org.queenlang.transpiler.nodes.statements.QueenBlockStatements;
import org.queenlang.transpiler.nodes.types.QueenExceptionTypeNode;
import org.queenlang.transpiler.nodes.types.QueenNodeWithTypeParameters;
import org.queenlang.transpiler.nodes.types.QueenTypeNode;
import org.queenlang.transpiler.nodes.types.QueenTypeParameterNode;

import java.util.List;

/**
 * Queen InterfaceMethodDeclaration AST Node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 * @todo #10:30min Unit tests for QueenMethodDeclarationNode are needed.
 */
public final class QueenInterfaceMethodDeclarationNode implements QueenInterfaceMemberDeclarationNode, QueenNodeWithTypeParameters {

    /**
     * Position in the original source code.
     */
    private final Position position;

    /**
     * Annotations on top of this method.
     */
    private final List<QueenAnnotationNode> annotations;

    /**
     * Access modifiers of this method.
     */
    private final List<QueenModifierNode> modifiers;

    /**
     * Return type.
     */
    private final QueenTypeNode returnType;

    /**
     * Method type params.
     */
    private final List<QueenTypeParameterNode> typeParams;

    /**
     * Method name.
     */
    private final String name;

    /**
     * Method parameters.
     */
    private final List<QueenParameterNode> parameters;

    /**
     * Thrown exceptions.
     */
    private final List<QueenExceptionTypeNode> throwsList;

    /**
     * Method body.
     */
    private final QueenBlockStatements blockStatements;

    public QueenInterfaceMethodDeclarationNode(
        final Position position,
        final List<QueenAnnotationNode> annotations,
        final List<QueenModifierNode> modifiers,
        final QueenTypeNode returnType,
        final List<QueenTypeParameterNode> typeParams,
        final String name,
        final List<QueenParameterNode> parameters,
        final List<QueenExceptionTypeNode> throwsList,
        final QueenBlockStatements blockStatements
    ) {
        this.position = position;
        this.annotations = annotations;
        this.modifiers = modifiers;
        this.returnType = returnType;
        this.typeParams = typeParams;
        this.name = name;
        this.parameters = parameters;
        this.throwsList = throwsList;
        this.blockStatements = blockStatements;
    }

    @Override
    public void addToJavaNode(final Node java) {
        final MethodDeclaration method = ((ClassOrInterfaceDeclaration) java).addMethod(this.name);
        method.removeModifier(Modifier.Keyword.PUBLIC);
        this.returnType.addToJavaNode(method);
        this.annotations.forEach(a -> a.addToJavaNode(method));
        this.modifiers.forEach(m -> m.addToJavaNode(method));
        this.typeParams.forEach(tp -> tp.addToJavaNode(method));
        this.parameters.forEach(
            p -> p.addToJavaNode(method)
        );
        this.throwsList.forEach(t->t.addToJavaNode(method));
        if(this.blockStatements != null) {
            final BlockStmt blockStmt = new BlockStmt();
            this.blockStatements.addToJavaNode(blockStmt);
            method.setBody(blockStmt);
        } else {
            method.removeBody();
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

    @Override
    public List<QueenTypeParameterNode> typeParameters() {
        return this.typeParams;
    }
}
