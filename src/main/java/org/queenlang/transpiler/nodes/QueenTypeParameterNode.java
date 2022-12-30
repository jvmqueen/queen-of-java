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
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.nodeTypes.NodeWithTypeParameters;
import com.github.javaparser.ast.type.TypeParameter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Queen TypeParameter AST Node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 * @todo #49:60min Finish type perameters for constructors and methods (Replace
 *  strings with this type).
 */
public final class QueenTypeParameterNode implements Named, QueenNode {
    /**
     * Position in the original source code.
     */
    private final Position position;

    /**
     * Annotations on top of this type parameter.
     */
    private final List<QueenAnnotationNode> annotations;

    /**
     * Name of this type parameter.
     */
    private final String name;

    /**
     * Type bounds.
     */
    private final List<String> typeBound;

    public QueenTypeParameterNode(
        final Position position,
        final List<QueenAnnotationNode> annotations,
        final String name,
        final List<String> typeBound
    ) {
        this.position = position;
        this.annotations = annotations;
        this.name = name;
        this.typeBound = typeBound;
    }

    @Override
    public void addToJavaNode(final Node java) {
        ((NodeWithTypeParameters) java).addTypeParameter(
            this.toTypeParameter()
        );
    }

    @Override
    public Position position() {
        return this.position;
    }

    @Override
    public String name() {
        return this.name;
    }

    /**
     * Turn it into a JavaParser TypeParameter.
     * @return TypeParameter.
     */
    private TypeParameter toTypeParameter() {
        final TypeParameter tp = new TypeParameter(this.name);
        this.annotations.forEach(a -> a.addToJavaNode(tp));
        tp.setTypeBound(
            new NodeList<>(
                this.typeBound.stream().map(
                    StaticJavaParser::parseClassOrInterfaceType
                ).collect(Collectors.toList())
            )
        );
        return tp;
    }
}
