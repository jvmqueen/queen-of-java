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

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.UnionType;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.expressions.AnnotationNode;

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
        final Position position = Mockito.mock(Position.class);
        final ClassOrInterfaceTypeNode classOrInterfaceTypeNode = new QueenClassOrInterfaceTypeNode(
            position,
            false,
            Mockito.mock(ClassOrInterfaceTypeNode.class),
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
        final Position position = Mockito.mock(Position.class);
        final ClassOrInterfaceTypeNode classOrInterfaceTypeNode = new QueenClassOrInterfaceTypeNode(
            position,
            true,
            Mockito.mock(ClassOrInterfaceTypeNode.class),
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
    public void returnsScope() {
        final Position position = Mockito.mock(Position.class);
        final ClassOrInterfaceTypeNode scope = Mockito.mock(ClassOrInterfaceTypeNode.class);
        final ClassOrInterfaceTypeNode classOrInterfaceTypeNode = new QueenClassOrInterfaceTypeNode(
            position,
            true,
            scope,
            new ArrayList<>(),
            "MyInterface",
            new ArrayList<>(),
            false
        );
        MatcherAssert.assertThat(
            classOrInterfaceTypeNode.scope(),
            Matchers.is(scope)
        );
    }

    @Test
    public void returnsAnnotations() {
        final Position position = Mockito.mock(Position.class);
        final ClassOrInterfaceTypeNode scope = Mockito.mock(ClassOrInterfaceTypeNode.class);
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(Mockito.mock(AnnotationNode.class));
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
    public void returnsName() {
        final Position position = Mockito.mock(Position.class);
        final ClassOrInterfaceTypeNode scope = Mockito.mock(ClassOrInterfaceTypeNode.class);
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(Mockito.mock(AnnotationNode.class));
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
            classOrInterfaceTypeNode.name(),
            Matchers.equalTo("SomeClass")
        );
    }

    @Test
    public void returnsTypeArguments() {
        final Position position = Mockito.mock(Position.class);
        final ClassOrInterfaceTypeNode scope = Mockito.mock(ClassOrInterfaceTypeNode.class);
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(Mockito.mock(AnnotationNode.class));
        final List<TypeNode> typeArguments = new ArrayList<>();
        typeArguments.add(Mockito.mock(TypeNode.class));
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
        final Position position = Mockito.mock(Position.class);
        final ClassOrInterfaceTypeNode scope = Mockito.mock(ClassOrInterfaceTypeNode.class);
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(Mockito.mock(AnnotationNode.class));
        final List<TypeNode> typeArguments = new ArrayList<>();
        typeArguments.add(Mockito.mock(TypeNode.class));
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
    public void addsToVariableDeclaratorJavaNode() {
        final Position position = Mockito.mock(Position.class);
        final ClassOrInterfaceTypeNode scope = Mockito.mock(ClassOrInterfaceTypeNode.class);
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(Mockito.mock(AnnotationNode.class));
        final List<TypeNode> typeArguments = new ArrayList<>();
        typeArguments.add(Mockito.mock(TypeNode.class));
        final ClassOrInterfaceTypeNode classOrInterfaceTypeNode = new QueenClassOrInterfaceTypeNode(
            position,
            false,
            scope,
            annotations,
            "SomeClass",
            typeArguments,
            false
        );

        final VariableDeclarator javaNode = new VariableDeclarator();
        classOrInterfaceTypeNode.addToJavaNode(javaNode);

        MatcherAssert.assertThat(
            javaNode.getType().asClassOrInterfaceType().getName().asString(),
            Matchers.equalTo("SomeClass")
        );
        Mockito.verify(scope, Mockito.times(1)).toType();
        annotations.forEach(
            a -> Mockito.verify(a, Mockito.times(1)).addToJavaNode(
                Mockito.any(ClassOrInterfaceType.class)
            )
        );
        typeArguments.forEach(
            ta -> Mockito.verify(ta, Mockito.times(1)).addToJavaNode(
                Mockito.any(ClassOrInterfaceType.class)
            )
        );
    }

    @Test
    public void addsToMethodDeclaratorJavaNode() {
        final Position position = Mockito.mock(Position.class);
        final ClassOrInterfaceTypeNode scope = Mockito.mock(ClassOrInterfaceTypeNode.class);
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(Mockito.mock(AnnotationNode.class));
        final List<TypeNode> typeArguments = new ArrayList<>();
        typeArguments.add(Mockito.mock(TypeNode.class));
        final ClassOrInterfaceTypeNode classOrInterfaceTypeNode = new QueenClassOrInterfaceTypeNode(
            position,
            false,
            scope,
            annotations,
            "SomeClass",
            typeArguments,
            false
        );

        final MethodDeclaration javaNode = new MethodDeclaration();
        classOrInterfaceTypeNode.addToJavaNode(javaNode);

        MatcherAssert.assertThat(
            javaNode.getType().asClassOrInterfaceType().getName().asString(),
            Matchers.equalTo("SomeClass")
        );
        Mockito.verify(scope, Mockito.times(1)).toType();
        annotations.forEach(
            a -> Mockito.verify(a, Mockito.times(1)).addToJavaNode(
                Mockito.any(ClassOrInterfaceType.class)
            )
        );
        typeArguments.forEach(
            ta -> Mockito.verify(ta, Mockito.times(1)).addToJavaNode(
                Mockito.any(ClassOrInterfaceType.class)
            )
        );
    }

    @Test
    public void addsToParameterJavaNode() {
        final Position position = Mockito.mock(Position.class);
        final ClassOrInterfaceTypeNode scope = Mockito.mock(ClassOrInterfaceTypeNode.class);
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(Mockito.mock(AnnotationNode.class));
        final List<TypeNode> typeArguments = new ArrayList<>();
        typeArguments.add(Mockito.mock(TypeNode.class));
        final ClassOrInterfaceTypeNode classOrInterfaceTypeNode = new QueenClassOrInterfaceTypeNode(
            position,
            false,
            scope,
            annotations,
            "SomeClass",
            typeArguments,
            false
        );

        final Parameter javaNode = new Parameter();
        classOrInterfaceTypeNode.addToJavaNode(javaNode);

        MatcherAssert.assertThat(
            javaNode.getType().asClassOrInterfaceType().getName().asString(),
            Matchers.equalTo("SomeClass")
        );
        Mockito.verify(scope, Mockito.times(1)).toType();
        annotations.forEach(
            a -> Mockito.verify(a, Mockito.times(1)).addToJavaNode(
                Mockito.any(ClassOrInterfaceType.class)
            )
        );
        typeArguments.forEach(
            ta -> Mockito.verify(ta, Mockito.times(1)).addToJavaNode(
                Mockito.any(ClassOrInterfaceType.class)
            )
        );
    }

    @Test
    public void addsToNodeWithTypeArgumentsJavaNode() {
        final Position position = Mockito.mock(Position.class);
        final ClassOrInterfaceTypeNode scope = Mockito.mock(ClassOrInterfaceTypeNode.class);
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(Mockito.mock(AnnotationNode.class));
        final List<TypeNode> typeArguments = new ArrayList<>();
        typeArguments.add(Mockito.mock(TypeNode.class));
        final ClassOrInterfaceTypeNode classOrInterfaceTypeNode = new QueenClassOrInterfaceTypeNode(
            position,
            false,
            scope,
            annotations,
            "SomeClass",
            typeArguments,
            false
        );

        final ClassOrInterfaceType javaNode = new ClassOrInterfaceType();
        classOrInterfaceTypeNode.addToJavaNode(javaNode);

        MatcherAssert.assertThat(
            javaNode.getTypeArguments().get().get(0).asClassOrInterfaceType().getName().asString(),
            Matchers.equalTo("SomeClass")
        );
        Mockito.verify(scope, Mockito.times(1)).toType();
        annotations.forEach(
            a -> Mockito.verify(a, Mockito.times(1)).addToJavaNode(
                Mockito.any(ClassOrInterfaceType.class)
            )
        );
        typeArguments.forEach(
            ta -> Mockito.verify(ta, Mockito.times(1)).addToJavaNode(
                Mockito.any(ClassOrInterfaceType.class)
            )
        );
    }

    @Test
    public void addsToUnionTypeJavaNode() {
        final Position position = Mockito.mock(Position.class);
        final ClassOrInterfaceTypeNode scope = Mockito.mock(ClassOrInterfaceTypeNode.class);
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(Mockito.mock(AnnotationNode.class));
        final List<TypeNode> typeArguments = new ArrayList<>();
        typeArguments.add(Mockito.mock(TypeNode.class));
        final ClassOrInterfaceTypeNode classOrInterfaceTypeNode = new QueenClassOrInterfaceTypeNode(
            position,
            false,
            scope,
            annotations,
            "SomeClass",
            typeArguments,
            false
        );

        final UnionType javaNode = new UnionType();
        classOrInterfaceTypeNode.addToJavaNode(javaNode);

        MatcherAssert.assertThat(
            javaNode.getElements().get(0).asClassOrInterfaceType().getName().asString(),
            Matchers.equalTo("SomeClass")
        );
        Mockito.verify(scope, Mockito.times(1)).toType();
        annotations.forEach(
            a -> Mockito.verify(a, Mockito.times(1)).addToJavaNode(
                Mockito.any(ClassOrInterfaceType.class)
            )
        );
        typeArguments.forEach(
            ta -> Mockito.verify(ta, Mockito.times(1)).addToJavaNode(
                Mockito.any(ClassOrInterfaceType.class)
            )
        );
    }

    @Test
    public void addsImplementsToClassOrInterfaceDeclarationJavaNode() {
        final Position position = Mockito.mock(Position.class);
        final ClassOrInterfaceTypeNode scope = Mockito.mock(ClassOrInterfaceTypeNode.class);
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(Mockito.mock(AnnotationNode.class));
        final List<TypeNode> typeArguments = new ArrayList<>();
        typeArguments.add(Mockito.mock(TypeNode.class));
        final ClassOrInterfaceTypeNode classOrInterfaceTypeNode = new QueenClassOrInterfaceTypeNode(
            position,
            true,
            scope,
            annotations,
            "SomeInterface",
            typeArguments,
            false
        );

        final ClassOrInterfaceDeclaration javaNode = new ClassOrInterfaceDeclaration();
        javaNode.setInterface(false);
        classOrInterfaceTypeNode.addToJavaNode(javaNode);

        MatcherAssert.assertThat(
            javaNode.getImplementedTypes().get(0).asClassOrInterfaceType().getName().asString(),
            Matchers.equalTo("SomeInterface")
        );
        Mockito.verify(scope, Mockito.times(1)).toType();
        annotations.forEach(
            a -> Mockito.verify(a, Mockito.times(1)).addToJavaNode(
                Mockito.any(ClassOrInterfaceType.class)
            )
        );
        typeArguments.forEach(
            ta -> Mockito.verify(ta, Mockito.times(1)).addToJavaNode(
                Mockito.any(ClassOrInterfaceType.class)
            )
        );
    }

    @Test
    public void addsExtendsToClassOrInterfaceDeclarationJavaNode() {
        final Position position = Mockito.mock(Position.class);
        final ClassOrInterfaceTypeNode scope = Mockito.mock(ClassOrInterfaceTypeNode.class);
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(Mockito.mock(AnnotationNode.class));
        final List<TypeNode> typeArguments = new ArrayList<>();
        typeArguments.add(Mockito.mock(TypeNode.class));
        final ClassOrInterfaceTypeNode classOrInterfaceTypeNode = new QueenClassOrInterfaceTypeNode(
            position,
            false,
            scope,
            annotations,
            "SomeExtendsClass",
            typeArguments,
            false
        );

        final ClassOrInterfaceDeclaration javaNode = new ClassOrInterfaceDeclaration();
        classOrInterfaceTypeNode.addToJavaNode(javaNode);

        MatcherAssert.assertThat(
            javaNode.getExtendedTypes().get(0).asClassOrInterfaceType().getName().asString(),
            Matchers.equalTo("SomeExtendsClass")
        );
        Mockito.verify(scope, Mockito.times(1)).toType();
        annotations.forEach(
            a -> Mockito.verify(a, Mockito.times(1)).addToJavaNode(
                Mockito.any(ClassOrInterfaceType.class)
            )
        );
        typeArguments.forEach(
            ta -> Mockito.verify(ta, Mockito.times(1)).addToJavaNode(
                Mockito.any(ClassOrInterfaceType.class)
            )
        );
    }

    @Test
    public void addsToWildcardSuperTypeJavaNode() {
        final Position position = Mockito.mock(Position.class);
        final ClassOrInterfaceTypeNode scope = Mockito.mock(ClassOrInterfaceTypeNode.class);
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(Mockito.mock(AnnotationNode.class));
        final List<TypeNode> typeArguments = new ArrayList<>();
        typeArguments.add(Mockito.mock(TypeNode.class));
        final ClassOrInterfaceTypeNode classOrInterfaceTypeNode = new QueenClassOrInterfaceTypeNode(
            position,
            false,
            scope,
            annotations,
            "SomeClass",
            typeArguments,
            false
        );

        final QueenWildcardNode.WildcardSuperBound javaNode = new QueenWildcardNode.WildcardSuperBound();
        classOrInterfaceTypeNode.addToJavaNode(javaNode);

        MatcherAssert.assertThat(
            javaNode.getSuperType().get().asClassOrInterfaceType().getName().asString(),
            Matchers.equalTo("SomeClass")
        );
        Mockito.verify(scope, Mockito.times(1)).toType();
        annotations.forEach(
            a -> Mockito.verify(a, Mockito.times(1)).addToJavaNode(
                Mockito.any(ClassOrInterfaceType.class)
            )
        );
        typeArguments.forEach(
            ta -> Mockito.verify(ta, Mockito.times(1)).addToJavaNode(
                Mockito.any(ClassOrInterfaceType.class)
            )
        );
    }

    @Test
    public void addsToWildcardExtendedTypeJavaNode() {
        final Position position = Mockito.mock(Position.class);
        final ClassOrInterfaceTypeNode scope = Mockito.mock(ClassOrInterfaceTypeNode.class);
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(Mockito.mock(AnnotationNode.class));
        final List<TypeNode> typeArguments = new ArrayList<>();
        typeArguments.add(Mockito.mock(TypeNode.class));
        final ClassOrInterfaceTypeNode classOrInterfaceTypeNode = new QueenClassOrInterfaceTypeNode(
            position,
            false,
            scope,
            annotations,
            "SomeClass",
            typeArguments,
            false
        );

        final QueenWildcardNode.WildcardExtendsBound javaNode = new QueenWildcardNode.WildcardExtendsBound();
        classOrInterfaceTypeNode.addToJavaNode(javaNode);

        MatcherAssert.assertThat(
            javaNode.getExtendedType().get().asClassOrInterfaceType().getName().asString(),
            Matchers.equalTo("SomeClass")
        );
        Mockito.verify(scope, Mockito.times(1)).toType();
        annotations.forEach(
            a -> Mockito.verify(a, Mockito.times(1)).addToJavaNode(
                Mockito.any(ClassOrInterfaceType.class)
            )
        );
        typeArguments.forEach(
            ta -> Mockito.verify(ta, Mockito.times(1)).addToJavaNode(
                Mockito.any(ClassOrInterfaceType.class)
            )
        );
    }
}
