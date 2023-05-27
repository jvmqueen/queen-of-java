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

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.QueenNode;
import org.queenlang.transpiler.nodes.expressions.AnnotationNode;
import org.queenlang.transpiler.nodes.types.ClassOrInterfaceTypeNode;
import org.queenlang.transpiler.nodes.types.TypeParameterNode;
import org.queenlang.transpiler.util.QueenMockito;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for {@link QueenNormalInterfaceDeclarationNode}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenNormalInterfaceDeclarationNodeTestCase {

    @Test
    public void returnsPosition() {
        final Position position = QueenMockito.mock(Position.class);
        final NormalInterfaceDeclarationNode normalInterfaceDeclarationNode = new QueenNormalInterfaceDeclarationNode(
            position,
            new ArrayList<>(),
            new ArrayList<>(),
            "SomeInterface",
            new ArrayList<>(),
            new ArrayList<>(),
            QueenMockito.mock(InterfaceBodyNode.class)
        );
        MatcherAssert.assertThat(
            normalInterfaceDeclarationNode.position(),
            Matchers.is(position)
        );
    }

    @Test
    public void returnsName() {
        final NormalInterfaceDeclarationNode normalInterfaceDeclarationNode = new QueenNormalInterfaceDeclarationNode(
            QueenMockito.mock(Position.class),
            new ArrayList<>(),
            new ArrayList<>(),
            "SomeInterface",
            new ArrayList<>(),
            new ArrayList<>(),
            QueenMockito.mock(InterfaceBodyNode.class)
        );
        MatcherAssert.assertThat(
            normalInterfaceDeclarationNode.name(),
            Matchers.equalTo("SomeInterface")
        );
    }

    @Test
    public void returnsAnnotations() {
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(QueenMockito.mock(AnnotationNode.class));
        annotations.add(QueenMockito.mock(AnnotationNode.class));
        annotations.add(QueenMockito.mock(AnnotationNode.class));

        final NormalInterfaceDeclarationNode normalInterfaceDeclarationNode = new QueenNormalInterfaceDeclarationNode(
            QueenMockito.mock(Position.class),
            annotations,
            new ArrayList<>(),
            "SomeInterface",
            new ArrayList<>(),
            new ArrayList<>(),
            QueenMockito.mock(InterfaceBodyNode.class)
        );
        MatcherAssert.assertThat(
            normalInterfaceDeclarationNode.annotations(),
            Matchers.is(annotations)
        );
    }

    @Test
    public void returnsModifiers() {
        final List<ModifierNode> modifiers = new ArrayList<>();
        modifiers.add(QueenMockito.mock(ModifierNode.class));
        modifiers.add(QueenMockito.mock(ModifierNode.class));
        modifiers.add(QueenMockito.mock(ModifierNode.class));

        final NormalInterfaceDeclarationNode normalInterfaceDeclarationNode = new QueenNormalInterfaceDeclarationNode(
            QueenMockito.mock(Position.class),
            new ArrayList<>(),
            modifiers,
            "SomeInterface",
            new ArrayList<>(),
            new ArrayList<>(),
            QueenMockito.mock(InterfaceBodyNode.class)
        );
        MatcherAssert.assertThat(
            normalInterfaceDeclarationNode.modifiers(),
            Matchers.is(modifiers)
        );
    }

    @Test
    public void returnsTypeParams() {
        final List<TypeParameterNode> typeParameterNodes = new ArrayList<>();
        typeParameterNodes.add(QueenMockito.mock(TypeParameterNode.class));
        typeParameterNodes.add(QueenMockito.mock(TypeParameterNode.class));
        typeParameterNodes.add(QueenMockito.mock(TypeParameterNode.class));

        final NormalInterfaceDeclarationNode normalInterfaceDeclarationNode = new QueenNormalInterfaceDeclarationNode(
            QueenMockito.mock(Position.class),
            new ArrayList<>(),
            new ArrayList<>(),
            "SomeInterface",
            typeParameterNodes,
            new ArrayList<>(),
            QueenMockito.mock(InterfaceBodyNode.class)
        );
        MatcherAssert.assertThat(
            normalInterfaceDeclarationNode.typeParameters(),
            Matchers.is(typeParameterNodes)
        );
    }

    @Test
    public void returnsExtendsTypes() {
        final List<ClassOrInterfaceTypeNode> classOrInterfaceTypeNodes = new ArrayList<>();
        classOrInterfaceTypeNodes.add(QueenMockito.mock(ClassOrInterfaceTypeNode.class));
        classOrInterfaceTypeNodes.add(QueenMockito.mock(ClassOrInterfaceTypeNode.class));
        classOrInterfaceTypeNodes.add(QueenMockito.mock(ClassOrInterfaceTypeNode.class));

        final NormalInterfaceDeclarationNode normalInterfaceDeclarationNode = new QueenNormalInterfaceDeclarationNode(
            QueenMockito.mock(Position.class),
            new ArrayList<>(),
            new ArrayList<>(),
            "SomeInterface",
            new ArrayList<>(),
            classOrInterfaceTypeNodes,
            QueenMockito.mock(InterfaceBodyNode.class)
        );
        MatcherAssert.assertThat(
            normalInterfaceDeclarationNode.extendsTypes(),
            Matchers.is(classOrInterfaceTypeNodes)
        );
    }

    @Test
    public void returnsBody() {
        final InterfaceBodyNode body = QueenMockito.mock(InterfaceBodyNode.class);
        final NormalInterfaceDeclarationNode normalInterfaceDeclarationNode = new QueenNormalInterfaceDeclarationNode(
            QueenMockito.mock(Position.class),
            new ArrayList<>(),
            new ArrayList<>(),
            "SomeInterface",
            new ArrayList<>(),
            new ArrayList<>(),
            body
        );
        MatcherAssert.assertThat(
            normalInterfaceDeclarationNode.body(),
            Matchers.is(body)
        );
    }

    @Test
    public void returnsChildren() {
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(QueenMockito.mock(AnnotationNode.class));
        final List<ModifierNode> modifiers = new ArrayList<>();
        modifiers.add(QueenMockito.mock(ModifierNode.class));
        final List<TypeParameterNode> typeParams = new ArrayList<>();
        typeParams.add(QueenMockito.mock(TypeParameterNode.class));
        final List<ClassOrInterfaceTypeNode> extendsTypes = new ArrayList<>();
        extendsTypes.add(QueenMockito.mock(ClassOrInterfaceTypeNode.class));
        final InterfaceBodyNode body = QueenMockito.mock(InterfaceBodyNode.class);

        final NormalInterfaceDeclarationNode normalInterfaceDeclarationNode = new QueenNormalInterfaceDeclarationNode(
            QueenMockito.mock(Position.class),
            annotations,
            modifiers,
            "SomeInterface",
            typeParams,
            extendsTypes,
            body
        );

        final List<QueenNode> children = normalInterfaceDeclarationNode.children();
        MatcherAssert.assertThat(
            children.size(),
            Matchers.is(5)
        );
        MatcherAssert.assertThat(
            children.containsAll(
                List.of(
                    annotations.get(0),
                    modifiers.get(0),
                    typeParams.get(0),
                    extendsTypes.get(0),
                    body
                )
            ),
            Matchers.is(true)
        );
    }

}
