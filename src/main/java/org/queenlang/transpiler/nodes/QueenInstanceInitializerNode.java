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

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

/**
 * Queen Instance Initializer AST Node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 * @todo #33:30min Unit tests for class QueenInstanceInitializerNode are needed.
 */
public final class QueenInstanceInitializerNode implements QueenClassBodyDeclarationNode {

    /**
     * Position in the original source code.
     */
    private final Position position;

    /**
     * Statements in this initializer.
     */
    private final QueenBlockStatements blockStatements;

    /**
     * Is it static or not?
     */
    private final boolean isStatic;

    public QueenInstanceInitializerNode(
        final Position position,
        final QueenBlockStatements blockStatements
    ) {
        this(position, blockStatements, false);
    }

    public QueenInstanceInitializerNode(
        final Position position,
        final QueenBlockStatements blockStatements,
        final boolean isStatic
    ) {
        this.position = position;
        this.blockStatements = blockStatements;
        this.isStatic = isStatic;
    }

    @Override
    public void addToJavaNode(final Node java) {
        final ClassOrInterfaceDeclaration clazz = (ClassOrInterfaceDeclaration) java;
        if(this.isStatic) {
            this.blockStatements.addToJavaNode(clazz.addStaticInitializer());
        } else {
            this.blockStatements.addToJavaNode(clazz.addInitializer());
        }
    }

    @Override
    public Position position() {
        return this.position;
    }
}
