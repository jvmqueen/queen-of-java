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
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.PrimitiveType;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.expressions.QueenAnnotationNode;

import java.util.List;

/**
 * Queen PrimitiveType AST Node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenPrimitiveTypeNode implements QueenTypeNode {
    /**
     * Position in the original source code.
     */
    private final Position position;

    /**
     * Annotations on top of this primitive type.
     */
    private final List<QueenAnnotationNode> annotations;

    /**
     * Name of this primitive type.
     */
    private final String name;

    public QueenPrimitiveTypeNode(
        final Position position,
        final List<QueenAnnotationNode> annotations,
        final String name
    ) {
        this.position = position;
        this.annotations = annotations;
        this.name = name;
    }

    @Override
    public void addToJavaNode(final Node java) {
        if(java instanceof VariableDeclarator) {
            ((VariableDeclarator) java).setType(this.toType());
        } else if(java instanceof MethodDeclaration) {
            ((MethodDeclaration) java).setType(this.toType());
        } else if(java instanceof Parameter) {
            ((Parameter) java).setType(this.toType());
        }
    }

    @Override
    public Position position() {
        return this.position;
    }

    @Override
    public PrimitiveType toType() {
        final PrimitiveType primitiveType = new PrimitiveType(
            PrimitiveType.Primitive.valueOf(this.name.toUpperCase())
        );
        if(this.annotations != null) {
            this.annotations.forEach(a -> a.addToJavaNode(primitiveType));
        }
        return primitiveType;
    }
}