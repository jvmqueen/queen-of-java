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
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithTypeArguments;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * Queen class or interface reference type.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 * @todo #49:60min Implement other types like primitive, array etc.
 */
public final class QueenClassOrInterfaceTypeNode implements QueenReferenceTypeNode {

    /**
     * Position in the original source code.
     */
    private final Position position;

    /**
     * Is it an interface type or class type?
     */
    private final boolean interfaceType;

    /**
     * Scope of this reference type (what comes before the dot). E.g.
     * java.util.List (util is the scope of List).
     */
    private final QueenClassOrInterfaceTypeNode scope;

    /**
     * Annotations on top of this reference type.
     */
    private final List<QueenAnnotationNode> annotations;

    /**
     * Name of this reference type.
     */
    private final String name;

    /**
     * Type arguments of this reference type.
     */
    private final List<QueenTypeNode> typeArguments;

    public QueenClassOrInterfaceTypeNode(
        final Position position,
        final boolean interfaceType,
        final List<QueenAnnotationNode> annotations,
        final String name,
        final List<QueenTypeNode> typeArguments
    ) {
        this(position, interfaceType, null, annotations, name, typeArguments);
    }

    public QueenClassOrInterfaceTypeNode(
        final Position position,
        final boolean interfaceType,
        final QueenClassOrInterfaceTypeNode scope,
        final List<QueenAnnotationNode> annotations,
        final String name,
        final List<QueenTypeNode> typeArguments
    ) {
        this.position = position;
        this.interfaceType = interfaceType;
        this.scope = scope;
        this.annotations = annotations;
        this.name = name;
        this.typeArguments = typeArguments;
    }

    @Override
    public void addToJavaNode(final Node java) {
        if(java instanceof ClassOrInterfaceDeclaration) {
            final ClassOrInterfaceDeclaration clazz = ((ClassOrInterfaceDeclaration) java);
            if(this.interfaceType && !clazz.isInterface()) {
                clazz.addImplementedType(this.toClassOrInterfaceType());
            } else {
                clazz.addExtendedType(this.toClassOrInterfaceType());
            }
        } else if(java instanceof NodeWithTypeArguments) {
            final List<Type> existing = new ArrayList<>();
            ((NodeWithTypeArguments<?>) java)
                .getTypeArguments()
                .ifPresent(
                    tas -> existing.addAll(tas)
                );
            existing.add(this.toClassOrInterfaceType());

            ((NodeWithTypeArguments<?>) java)
                .setTypeArguments(new NodeList<>(existing));
        }
    }

    @Override
    public Position position() {
        return this.position;
    }

    /**
     * Turn it into a JavaParser ClassOrInterfaceType.
     * @return ClassOrInterfaceType.
     */
    private ClassOrInterfaceType toClassOrInterfaceType() {
        final ClassOrInterfaceType classOrInterfaceType = new ClassOrInterfaceType(this.name);
        if(this.scope != null) {
            classOrInterfaceType.setScope(this.scope.toClassOrInterfaceType());
        }
        if(this.annotations != null) {
            this.annotations.forEach(a -> a.addToJavaNode(classOrInterfaceType));
        }
        if(this.typeArguments != null) {
            this.typeArguments.forEach(
                ta -> ta.addToJavaNode(classOrInterfaceType)
            );
        }
        return classOrInterfaceType;
    }
}
