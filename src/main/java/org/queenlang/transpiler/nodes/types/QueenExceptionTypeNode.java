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

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.nodeTypes.NodeWithThrownExceptions;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import org.queenlang.transpiler.nodes.Position;

/**
 * Queen ExceptionType AST Node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenExceptionTypeNode implements ExceptionTypeNode {

    private final ClassOrInterfaceTypeNode exceptionType;

    public QueenExceptionTypeNode(
        final ClassOrInterfaceTypeNode exceptionType
    ) {
        this.exceptionType = exceptionType;
    }

    @Override
    public void addToJavaNode(final Node java) {
        if(java instanceof NodeWithThrownExceptions) {
            ((NodeWithThrownExceptions) java).addThrownException(this.toType());
        }
    }

    @Override
    public Position position() {
        return this.exceptionType.position();
    }

    /**
     * Turn it into a JavaParser ClassOrInterfaceType.
     * @return ClassOrInterfaceType.
     */
    @Override
    public ClassOrInterfaceType toType() {
        final ClassOrInterfaceType classOrInterfaceType = new ClassOrInterfaceType(this.exceptionType.name());
        if(this.exceptionType.scope() != null) {
            classOrInterfaceType.setScope(
                new QueenExceptionTypeNode(this.exceptionType.scope()).toType()
            );
        }
        if(this.exceptionType.annotations() != null) {
            this.exceptionType.annotations().forEach(a -> a.addToJavaNode(classOrInterfaceType));
        }
        if(this.exceptionType.typeArguments() != null) {
            this.exceptionType.typeArguments().forEach(
                ta -> ta.addToJavaNode(classOrInterfaceType)
            );
        }
        return classOrInterfaceType;
    }

    @Override
    public ClassOrInterfaceTypeNode exceptionType() {
        return this.exceptionType;
    }
}
