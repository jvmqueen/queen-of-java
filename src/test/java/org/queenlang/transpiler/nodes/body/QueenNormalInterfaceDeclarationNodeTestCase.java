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

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.expressions.AnnotationNode;
import org.queenlang.transpiler.nodes.types.ClassOrInterfaceTypeNode;
import org.queenlang.transpiler.nodes.types.TypeParameterNode;

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
        final Position position = Mockito.mock(Position.class);
        final NormalInterfaceDeclarationNode normalInterfaceDeclarationNode = new QueenNormalInterfaceDeclarationNode(
            position,
            new ArrayList<>(),
            new ArrayList<>(),
            "SomeInterface",
            new ArrayList<>(),
            new ArrayList<>(),
            Mockito.mock(InterfaceBodyNode.class)
        );
        MatcherAssert.assertThat(
            normalInterfaceDeclarationNode.position(),
            Matchers.is(position)
        );
    }

    @Test
    public void returnsName() {
        final NormalInterfaceDeclarationNode normalInterfaceDeclarationNode = new QueenNormalInterfaceDeclarationNode(
            Mockito.mock(Position.class),
            new ArrayList<>(),
            new ArrayList<>(),
            "SomeInterface",
            new ArrayList<>(),
            new ArrayList<>(),
            Mockito.mock(InterfaceBodyNode.class)
        );
        MatcherAssert.assertThat(
            normalInterfaceDeclarationNode.name(),
            Matchers.equalTo("SomeInterface")
        );
    }

    @Test
    public void returnsAnnotations() {
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(Mockito.mock(AnnotationNode.class));
        annotations.add(Mockito.mock(AnnotationNode.class));
        annotations.add(Mockito.mock(AnnotationNode.class));

        final NormalInterfaceDeclarationNode normalInterfaceDeclarationNode = new QueenNormalInterfaceDeclarationNode(
            Mockito.mock(Position.class),
            annotations,
            new ArrayList<>(),
            "SomeInterface",
            new ArrayList<>(),
            new ArrayList<>(),
            Mockito.mock(InterfaceBodyNode.class)
        );
        MatcherAssert.assertThat(
            normalInterfaceDeclarationNode.annotations(),
            Matchers.is(annotations)
        );
    }

    @Test
    public void returnsModifiers() {
        final List<ModifierNode> modifiers = new ArrayList<>();
        modifiers.add(Mockito.mock(ModifierNode.class));
        modifiers.add(Mockito.mock(ModifierNode.class));
        modifiers.add(Mockito.mock(ModifierNode.class));

        final NormalInterfaceDeclarationNode normalInterfaceDeclarationNode = new QueenNormalInterfaceDeclarationNode(
            Mockito.mock(Position.class),
            new ArrayList<>(),
            modifiers,
            "SomeInterface",
            new ArrayList<>(),
            new ArrayList<>(),
            Mockito.mock(InterfaceBodyNode.class)
        );
        MatcherAssert.assertThat(
            normalInterfaceDeclarationNode.modifiers(),
            Matchers.is(modifiers)
        );
    }

    @Test
    public void returnsTypeParams() {
        final List<TypeParameterNode> typeParameterNodes = new ArrayList<>();
        typeParameterNodes.add(Mockito.mock(TypeParameterNode.class));
        typeParameterNodes.add(Mockito.mock(TypeParameterNode.class));
        typeParameterNodes.add(Mockito.mock(TypeParameterNode.class));

        final NormalInterfaceDeclarationNode normalInterfaceDeclarationNode = new QueenNormalInterfaceDeclarationNode(
            Mockito.mock(Position.class),
            new ArrayList<>(),
            new ArrayList<>(),
            "SomeInterface",
            typeParameterNodes,
            new ArrayList<>(),
            Mockito.mock(InterfaceBodyNode.class)
        );
        MatcherAssert.assertThat(
            normalInterfaceDeclarationNode.typeParameters(),
            Matchers.is(typeParameterNodes)
        );
    }

    @Test
    public void returnsExtendsTypes() {
        final List<ClassOrInterfaceTypeNode> classOrInterfaceTypeNodes = new ArrayList<>();
        classOrInterfaceTypeNodes.add(Mockito.mock(ClassOrInterfaceTypeNode.class));
        classOrInterfaceTypeNodes.add(Mockito.mock(ClassOrInterfaceTypeNode.class));
        classOrInterfaceTypeNodes.add(Mockito.mock(ClassOrInterfaceTypeNode.class));

        final NormalInterfaceDeclarationNode normalInterfaceDeclarationNode = new QueenNormalInterfaceDeclarationNode(
            Mockito.mock(Position.class),
            new ArrayList<>(),
            new ArrayList<>(),
            "SomeInterface",
            new ArrayList<>(),
            classOrInterfaceTypeNodes,
            Mockito.mock(InterfaceBodyNode.class)
        );
        MatcherAssert.assertThat(
            normalInterfaceDeclarationNode.extendsTypes(),
            Matchers.is(classOrInterfaceTypeNodes)
        );
    }

    @Test
    public void returnsBody() {
        final InterfaceBodyNode body = Mockito.mock(InterfaceBodyNode.class);
        final NormalInterfaceDeclarationNode normalInterfaceDeclarationNode = new QueenNormalInterfaceDeclarationNode(
            Mockito.mock(Position.class),
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
    public void addsToCompilationUnit() {
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(Mockito.mock(AnnotationNode.class));
        final List<ModifierNode> modifiers = new ArrayList<>();
        modifiers.add(Mockito.mock(ModifierNode.class));
        final List<TypeParameterNode> typeParams = new ArrayList<>();
        typeParams.add(Mockito.mock(TypeParameterNode.class));
        final List<ClassOrInterfaceTypeNode> extendsTypes = new ArrayList<>();
        extendsTypes.add(Mockito.mock(ClassOrInterfaceTypeNode.class));
        final InterfaceBodyNode body = Mockito.mock(InterfaceBodyNode.class);

        final NormalInterfaceDeclarationNode normalInterfaceDeclarationNode = new QueenNormalInterfaceDeclarationNode(
            Mockito.mock(Position.class),
            annotations,
            modifiers,
            "SomeInterface",
            typeParams,
            extendsTypes,
            body
        );

        final CompilationUnit cu = new CompilationUnit();
        normalInterfaceDeclarationNode.addToJavaNode(cu);

        MatcherAssert.assertThat(
            cu.getTypes().get(0).getName().asString(),
            Matchers.equalTo("SomeInterface")
        );
        MatcherAssert.assertThat(
            cu.getTypes().get(0).asClassOrInterfaceDeclaration().isInterface(),
            Matchers.is(true)
        );
        annotations.forEach(
            a -> Mockito.verify(a, Mockito.times(1)).addToJavaNode(Mockito.any())
        );
        modifiers.forEach(
            m -> Mockito.verify(m, Mockito.times(1)).addToJavaNode(Mockito.any())
        );
        typeParams.forEach(
            tp -> Mockito.verify(tp, Mockito.times(1)).addToJavaNode(Mockito.any())
        );
        extendsTypes.forEach(
            et -> Mockito.verify(et, Mockito.times(1)).addToJavaNode(Mockito.any())
        );
        Mockito.verify(body, Mockito.times(1)).addToJavaNode(Mockito.any());
    }

    @Test
    public void addsToClassOrInterfaceDeclaration() {
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(Mockito.mock(AnnotationNode.class));
        final List<ModifierNode> modifiers = new ArrayList<>();
        modifiers.add(Mockito.mock(ModifierNode.class));
        final List<TypeParameterNode> typeParams = new ArrayList<>();
        typeParams.add(Mockito.mock(TypeParameterNode.class));
        final List<ClassOrInterfaceTypeNode> extendsTypes = new ArrayList<>();
        extendsTypes.add(Mockito.mock(ClassOrInterfaceTypeNode.class));
        final InterfaceBodyNode body = Mockito.mock(InterfaceBodyNode.class);

        final NormalInterfaceDeclarationNode normalInterfaceDeclarationNode = new QueenNormalInterfaceDeclarationNode(
            Mockito.mock(Position.class),
            annotations,
            modifiers,
            "SomeInterface",
            typeParams,
            extendsTypes,
            body
        );

        final ClassOrInterfaceDeclaration clazz = new ClassOrInterfaceDeclaration();
        normalInterfaceDeclarationNode.addToJavaNode(clazz);

        MatcherAssert.assertThat(
            clazz.getMember(0).asClassOrInterfaceDeclaration().getName().asString(),
            Matchers.equalTo("SomeInterface")
        );
        MatcherAssert.assertThat(
            clazz.getMember(0).asClassOrInterfaceDeclaration().isInterface(),
            Matchers.is(true)
        );
        annotations.forEach(
            a -> Mockito.verify(a, Mockito.times(1)).addToJavaNode(Mockito.any())
        );
        modifiers.forEach(
            m -> Mockito.verify(m, Mockito.times(1)).addToJavaNode(Mockito.any())
        );
        typeParams.forEach(
            tp -> Mockito.verify(tp, Mockito.times(1)).addToJavaNode(Mockito.any())
        );
        extendsTypes.forEach(
            et -> Mockito.verify(et, Mockito.times(1)).addToJavaNode(Mockito.any())
        );
        Mockito.verify(body, Mockito.times(1)).addToJavaNode(Mockito.any());
    }

    @Test
    public void addsToAnnotationDeclaration() {
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(Mockito.mock(AnnotationNode.class));
        final List<ModifierNode> modifiers = new ArrayList<>();
        modifiers.add(Mockito.mock(ModifierNode.class));
        final List<TypeParameterNode> typeParams = new ArrayList<>();
        typeParams.add(Mockito.mock(TypeParameterNode.class));
        final List<ClassOrInterfaceTypeNode> extendsTypes = new ArrayList<>();
        extendsTypes.add(Mockito.mock(ClassOrInterfaceTypeNode.class));
        final InterfaceBodyNode body = Mockito.mock(InterfaceBodyNode.class);

        final NormalInterfaceDeclarationNode normalInterfaceDeclarationNode = new QueenNormalInterfaceDeclarationNode(
            Mockito.mock(Position.class),
            annotations,
            modifiers,
            "SomeInterface",
            typeParams,
            extendsTypes,
            body
        );

        final AnnotationDeclaration annotationDeclaration = new AnnotationDeclaration();
        normalInterfaceDeclarationNode.addToJavaNode(annotationDeclaration);

        MatcherAssert.assertThat(
            annotationDeclaration.getMember(0).asClassOrInterfaceDeclaration().getName().asString(),
            Matchers.equalTo("SomeInterface")
        );
        MatcherAssert.assertThat(
            annotationDeclaration.getMember(0).asClassOrInterfaceDeclaration().isInterface(),
            Matchers.is(true)
        );
        annotations.forEach(
            a -> Mockito.verify(a, Mockito.times(1)).addToJavaNode(Mockito.any())
        );
        modifiers.forEach(
            m -> Mockito.verify(m, Mockito.times(1)).addToJavaNode(Mockito.any())
        );
        typeParams.forEach(
            tp -> Mockito.verify(tp, Mockito.times(1)).addToJavaNode(Mockito.any())
        );
        extendsTypes.forEach(
            et -> Mockito.verify(et, Mockito.times(1)).addToJavaNode(Mockito.any())
        );
        Mockito.verify(body, Mockito.times(1)).addToJavaNode(Mockito.any());
    }
}
