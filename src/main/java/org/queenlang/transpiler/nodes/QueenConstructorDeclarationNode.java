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
package org.queenlang.transpiler.nodes;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.stmt.BlockStmt;

import java.util.ArrayList;
import java.util.List;

/**
 * Queen ConstructorDeclaration AST Node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 * @todo #10:30min QueenConstructorDeclaration needs unit tests.
 * @todo #10:60min Handle constructor type parameters.
 */
public final class QueenConstructorDeclarationNode implements QueenClassBodyDeclarationNode {

    private final List<QueenNode> annotations;

    private final QueenConstructorModifierNode modifier;

    private final List<QueenParameterNode> parameters;

    private final QueenExplicitConstructorInvocationNode explicitConstructorInvocationNode;

    private final QueenBlockStatements blockStatements;

    private final List<String> throwsList;

    public QueenConstructorDeclarationNode(
        final List<QueenNode> annotations,
        final QueenConstructorModifierNode modifier,
        final List<QueenParameterNode> parameters,
        final List<String> throwsList,
        final QueenExplicitConstructorInvocationNode explicitConstructorInvocationNode,
        final QueenBlockStatements blockStatements
    ) {
        this.annotations = annotations;
        this.modifier = modifier;
        this.parameters = parameters;
        this.throwsList = throwsList;
        this.explicitConstructorInvocationNode = explicitConstructorInvocationNode;
        this.blockStatements = blockStatements;
    }

    public void addToJavaNode(final Node java) {
        final ConstructorDeclaration constructor = ((ClassOrInterfaceDeclaration) java)
            .addConstructor();
        this.annotations.forEach(a -> a.addToJavaNode(constructor));
        if(this.modifier != null) {
            this.modifier.addToJavaNode(constructor);
        }
        this.parameters.forEach(
            p -> p.addToJavaNode(constructor)
        );
        this.throwsList.forEach(
            t -> constructor.addThrownException(
                StaticJavaParser.parseClassOrInterfaceType(t)
            )
        );
        final BlockStmt blockStmt = new BlockStmt();
        if(this.explicitConstructorInvocationNode != null) {
            this.explicitConstructorInvocationNode.addToJavaNode(blockStmt);
        }
        if(this.blockStatements != null) {
            this.blockStatements.addToJavaNode(blockStmt);
        }
        constructor.setBody(blockStmt);
    }

}
