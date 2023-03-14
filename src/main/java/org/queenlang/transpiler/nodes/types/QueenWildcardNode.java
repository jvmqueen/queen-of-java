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
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.nodeTypes.NodeWithTypeArguments;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.WildcardType;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.expressions.AnnotationNode;
import org.queenlang.transpiler.nodes.expressions.QueenAnnotationNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Queen Wildcard Type node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenWildcardNode implements WildcardTypeNode {
    /**
     * Position in the original source code.
     */
    private final Position position;

    /**
     * Annotations on top of this wildcard.
     */
    private final List<AnnotationNode> annotations;

    /**
     * Extended type bound.
     */
    private final ReferenceTypeNode extendedType;

    /**
     * Super type bound.
     */
    private final ReferenceTypeNode superType;

    public QueenWildcardNode(
        final Position position,
        final List<AnnotationNode> annotations,
        final ReferenceTypeNode extendedType,
        final ReferenceTypeNode superType
    ) {
        this.position = position;
        this.annotations = annotations;
        this.extendedType = extendedType;
        this.superType = superType;
    }

    @Override
    public void addToJavaNode(final Node java) {
        if(java instanceof NodeWithTypeArguments) {
            final List<Type> existing = new ArrayList<>();
            ((NodeWithTypeArguments<?>) java)
                .getTypeArguments()
                .ifPresent(
                    tas -> existing.addAll(tas)
                );
            existing.add(this.toWildcardType());

            ((NodeWithTypeArguments<?>) java)
                .setTypeArguments(new NodeList<>(existing));
        }
    }

    @Override
    public Position position() {
        return this.position;
    }

    /**
     * Turn it into a JavaParser WildcardType.
     * @return WildcardType.
     */
    private WildcardType toWildcardType() {
        final WildcardType wildcardType = (WildcardType) this.toType();
        if(this.annotations != null) {
            this.annotations.forEach(a -> a.addToJavaNode(wildcardType));
        }
        if(this.extendedType != null) {
            this.extendedType.addToJavaNode(wildcardType);
        } else if(this.superType != null) {
            this.superType.addToJavaNode(wildcardType);
        }
        return wildcardType;
    }

    @Override
    public Type toType() {
        final WildcardType wildcardType;
        if(this.extendedType != null) {
            wildcardType = new WildcardExtendsBound();
        } else if (this.superType != null) {
            wildcardType = new WildcardSuperBound();
        } else {
            wildcardType = new WildcardType();
        }
        return wildcardType;
    }

    @Override
    public List<AnnotationNode> annotations() {
        return this.annotations;
    }

    @Override
    public ReferenceTypeNode extendedType() {
        return this.extendedType;
    }

    @Override
    public ReferenceTypeNode superType() {
        return this.superType;
    }

    static class WildcardSuperBound extends WildcardType {

    }

    static class WildcardExtendsBound extends WildcardType {

    }
}
