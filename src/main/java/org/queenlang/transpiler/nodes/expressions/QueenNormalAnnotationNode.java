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
package org.queenlang.transpiler.nodes.expressions;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations;
import org.queenlang.transpiler.nodes.NameNode;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.QueenNameNode;
import org.queenlang.transpiler.nodes.QueenNode;
import org.queenlang.transpiler.nodes.body.ElementValuePairNode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Queen normal annotation.
 * @author Mihai Emil Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenNormalAnnotationNode extends QueenAnnotationNode implements NormalAnnotationNode {

    /**
     * Key-value pairs within the annotation.
     */
    private final List<ElementValuePairNode> elementValuePairs;

    public QueenNormalAnnotationNode(
        final Position position,
        final NameNode name,
        final List<ElementValuePairNode> elementValuePairs
    ) {
        super(position, name);
        this.elementValuePairs = elementValuePairs != null ? elementValuePairs.stream().map(
            evp -> (ElementValuePairNode) evp.withParent(this)
        ).collect(Collectors.toList()) : null;
    }

    @Override
    public void addToJavaNode(final Node java) {
        ((NodeWithAnnotations) java).addAnnotation(this.toJavaExpression());
    }

    @Override
    public NormalAnnotationExpr toJavaExpression() {
        final NormalAnnotationExpr annotation = new NormalAnnotationExpr();
        annotation.setName(this.name());
        this.elementValuePairs.forEach(
            entry -> entry.addToJavaNode(annotation)
        );
        return annotation;
    }

    @Override
    public List<ElementValuePairNode> elementValuePairs() {
        return this.elementValuePairs;
    }

    @Override
    public List<QueenNode> children() {
        final List<QueenNode> children = new ArrayList<>();
        children.addAll(super.children());
        if(this.elementValuePairs != null) {
            children.addAll(this.elementValuePairs);
        }
        return children;
    }
}
