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

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.QueenNode;
import org.queenlang.transpiler.nodes.expressions.AnnotationNode;
import org.queenlang.transpiler.util.QueenMockito;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for {@link QueenClassOrInterfaceTypeNode}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenClassOrInterfaceTypeNodeTestCase {

    @Test
    public void returnsPosition() {
        final Position position = QueenMockito.mock(Position.class);
        final ClassOrInterfaceTypeNode classOrInterfaceTypeNode = new QueenClassOrInterfaceTypeNode(
            position,
            false,
            QueenMockito.mock(ClassOrInterfaceTypeNode.class),
            new ArrayList<>(),
            "MyClass",
            new ArrayList<>(),
            false
        );
        MatcherAssert.assertThat(
            classOrInterfaceTypeNode.position(),
            Matchers.is(position)
        );
    }

    @Test
    public void returnsIsInterface() {
        final Position position = QueenMockito.mock(Position.class);
        final ClassOrInterfaceTypeNode classOrInterfaceTypeNode = new QueenClassOrInterfaceTypeNode(
            position,
            true,
            QueenMockito.mock(ClassOrInterfaceTypeNode.class),
            new ArrayList<>(),
            "MyInterface",
            new ArrayList<>(),
            false
        );
        MatcherAssert.assertThat(
            classOrInterfaceTypeNode.interfaceType(),
            Matchers.is(true)
        );
    }

    @Test
    public void returnsQualifier() {
        final Position position = QueenMockito.mock(Position.class);
        final ClassOrInterfaceTypeNode qualifier = QueenMockito.mock(ClassOrInterfaceTypeNode.class);
        final ClassOrInterfaceTypeNode classOrInterfaceTypeNode = new QueenClassOrInterfaceTypeNode(
            position,
            true,
            qualifier,
            new ArrayList<>(),
            "MyInterface",
            new ArrayList<>(),
            false
        );
        MatcherAssert.assertThat(
            classOrInterfaceTypeNode.qualifier(),
            Matchers.is(qualifier)
        );
    }

    @Test
    public void returnsAnnotations() {
        final Position position = QueenMockito.mock(Position.class);
        final ClassOrInterfaceTypeNode scope = QueenMockito.mock(ClassOrInterfaceTypeNode.class);
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(QueenMockito.mock(AnnotationNode.class));
        final ClassOrInterfaceTypeNode classOrInterfaceTypeNode = new QueenClassOrInterfaceTypeNode(
            position,
            true,
            scope,
            annotations,
            "MyInterface",
            new ArrayList<>(),
            false
        );
        MatcherAssert.assertThat(
            classOrInterfaceTypeNode.annotations(),
            Matchers.is(annotations)
        );
    }

    @Test
    public void returnsIdentifier() {
        final Position position = QueenMockito.mock(Position.class);
        final ClassOrInterfaceTypeNode scope = QueenMockito.mock(ClassOrInterfaceTypeNode.class);
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(QueenMockito.mock(AnnotationNode.class));
        final ClassOrInterfaceTypeNode classOrInterfaceTypeNode = new QueenClassOrInterfaceTypeNode(
            position,
            false,
            scope,
            annotations,
            "SomeClass",
            new ArrayList<>(),
            false
        );
        MatcherAssert.assertThat(
            classOrInterfaceTypeNode.identifier(),
            Matchers.equalTo("SomeClass")
        );
    }

    @Test
    public void returnsTypeArguments() {
        final Position position = QueenMockito.mock(Position.class);
        final ClassOrInterfaceTypeNode scope = QueenMockito.mock(ClassOrInterfaceTypeNode.class);
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(QueenMockito.mock(AnnotationNode.class));
        final List<TypeNode> typeArguments = new ArrayList<>();
        typeArguments.add(QueenMockito.mock(TypeNode.class));
        final ClassOrInterfaceTypeNode classOrInterfaceTypeNode = new QueenClassOrInterfaceTypeNode(
            position,
            false,
            scope,
            annotations,
            "SomeClass",
            typeArguments,
            false
        );
        MatcherAssert.assertThat(
            classOrInterfaceTypeNode.typeArguments(),
            Matchers.is(typeArguments)
        );
    }

    @Test
    public void returnsHasDiamondOperator() {
        final Position position = QueenMockito.mock(Position.class);
        final ClassOrInterfaceTypeNode scope = QueenMockito.mock(ClassOrInterfaceTypeNode.class);
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(QueenMockito.mock(AnnotationNode.class));
        final List<TypeNode> typeArguments = new ArrayList<>();
        typeArguments.add(QueenMockito.mock(TypeNode.class));
        final ClassOrInterfaceTypeNode classOrInterfaceTypeNode = new QueenClassOrInterfaceTypeNode(
            position,
            false,
            scope,
            annotations,
            "SomeClass",
            typeArguments,
            true
        );
        MatcherAssert.assertThat(
            classOrInterfaceTypeNode.hasDiamondOperator(),
            Matchers.is(true)
        );
    }

    @Test
    public void returnsChildren() {
        final Position position = QueenMockito.mock(Position.class);
        final ClassOrInterfaceTypeNode scope = QueenMockito.mock(ClassOrInterfaceTypeNode.class);
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(QueenMockito.mock(AnnotationNode.class));
        final List<TypeNode> typeArguments = new ArrayList<>();
        typeArguments.add(QueenMockito.mock(TypeNode.class));
        final ClassOrInterfaceTypeNode classOrInterfaceTypeNode = new QueenClassOrInterfaceTypeNode(
            position,
            false,
            scope,
            annotations,
            "SomeClass",
            typeArguments,
            false
        );

        final List<QueenNode> children = classOrInterfaceTypeNode.children();
        MatcherAssert.assertThat(
            children.size(),
            Matchers.is(3)
        );
        MatcherAssert.assertThat(
            children.containsAll(
                List.of(
                    scope,
                    annotations.get(0),
                    typeArguments.get(0)
                )
            ),
            Matchers.is(true)
        );
    }
}
