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
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.TypeParameter;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.expressions.AnnotationNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for {@link QueenTypeParameterNode}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenTypeParameterNodeTestCase {

    @Test
    public void returnsPosition() {
        final Position position = Mockito.mock(Position.class);
        final TypeParameterNode typeParameterNode = new QueenTypeParameterNode(
            position,
            new ArrayList<>(),
            "T",
            new ArrayList<>()
        );
        MatcherAssert.assertThat(
            typeParameterNode.position(),
            Matchers.is(position)
        );
    }

    @Test
    public void returnsAnnotations() {
        final Position position = Mockito.mock(Position.class);
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(Mockito.mock(AnnotationNode.class));
        final TypeParameterNode typeParameterNode = new QueenTypeParameterNode(
            position,
            annotations,
            "T",
            new ArrayList<>()
        );
        MatcherAssert.assertThat(
            typeParameterNode.annotations(),
            Matchers.is(annotations)
        );
    }

    @Test
    public void returnsName() {
        final Position position = Mockito.mock(Position.class);
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(Mockito.mock(AnnotationNode.class));
        final TypeParameterNode typeParameterNode = new QueenTypeParameterNode(
            position,
            annotations,
            "T",
            new ArrayList<>()
        );
        MatcherAssert.assertThat(
            typeParameterNode.name(),
            Matchers.equalTo("T")
        );
    }

    @Test
    public void returnsTypeBound() {
        final Position position = Mockito.mock(Position.class);
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(Mockito.mock(AnnotationNode.class));
        final List<ClassOrInterfaceTypeNode> typeBound = new ArrayList<>();
        typeBound.add(Mockito.mock(ClassOrInterfaceTypeNode.class));
        final TypeParameterNode typeParameterNode = new QueenTypeParameterNode(
            position,
            annotations,
            "T",
            typeBound
        );
        MatcherAssert.assertThat(
            typeParameterNode.typeBound(),
            Matchers.is(typeBound)
        );
    }

    @Test
    public void addsToJavaNode() {
        final Position position = Mockito.mock(Position.class);
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(Mockito.mock(AnnotationNode.class));
        final List<ClassOrInterfaceTypeNode> typeBound = new ArrayList<>();
        final ClassOrInterfaceTypeNode tb = Mockito.mock(ClassOrInterfaceTypeNode.class);
        Mockito.when(tb.toType()).thenReturn(new ClassOrInterfaceType("Other"));
        typeBound.add(tb);
        final TypeParameterNode typeParameterNode = new QueenTypeParameterNode(
            position,
            annotations,
            "T",
            typeBound
        );

        final ClassOrInterfaceDeclaration clazz = new ClassOrInterfaceDeclaration();
        clazz.setName("MyClass");
        typeParameterNode.addToJavaNode(clazz);

        MatcherAssert.assertThat(
            clazz.toString(),
            Matchers.equalTo("class MyClass<T extends Other> {\n}")
        );
        MatcherAssert.assertThat(
            clazz.getTypeParameter(0).getName().asString(),
            Matchers.equalTo("T")
        );
        MatcherAssert.assertThat(
            clazz.getTypeParameter(0).getTypeBound().get(0).getName().asString(),
            Matchers.equalTo("Other")
        );
        annotations.forEach(
            a -> Mockito.verify(a, Mockito.times(1)).addToJavaNode(
                Mockito.any(TypeParameter.class)
            )
        );
    }
}