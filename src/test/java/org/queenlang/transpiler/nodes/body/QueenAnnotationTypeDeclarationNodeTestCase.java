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
import org.queenlang.transpiler.nodes.QueenNode;
import org.queenlang.transpiler.nodes.expressions.AnnotationNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Unit tests for {@link QueenAnnotationTypeDeclarationNode}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenAnnotationTypeDeclarationNodeTestCase {

    @Test
    public void returnsPosition() {
        final Position position = Mockito.mock(Position.class);
        final AnnotationTypeDeclarationNode annotationTypeDeclarationNode = new QueenAnnotationTypeDeclarationNode(
            position,
            new ArrayList<>(),
            new ArrayList<>(),
            "MyAnnotation",
            Mockito.mock(AnnotationTypeBodyNode.class)
        );
        MatcherAssert.assertThat(
            annotationTypeDeclarationNode.position(),
            Matchers.is(position)
        );
    }

    @Test
    public void returnsAnnotations() {
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(Mockito.mock(AnnotationNode.class));
        final AnnotationTypeDeclarationNode annotationTypeDeclarationNode = new QueenAnnotationTypeDeclarationNode(
            Mockito.mock(Position.class),
            annotations,
            new ArrayList<>(),
            "MyAnnotation",
            Mockito.mock(AnnotationTypeBodyNode.class)
        );
        MatcherAssert.assertThat(
            annotationTypeDeclarationNode.annotations(),
            Matchers.is(annotations)
        );
    }

    @Test
    public void returnsModifiers() {
        final List<ModifierNode> modifiers = new ArrayList<>();
        modifiers.add(Mockito.mock(ModifierNode.class));
        final AnnotationTypeDeclarationNode annotationTypeDeclarationNode = new QueenAnnotationTypeDeclarationNode(
            Mockito.mock(Position.class),
            new ArrayList<>(),
            modifiers,
            "MyAnnotation",
            Mockito.mock(AnnotationTypeBodyNode.class)
        );
        MatcherAssert.assertThat(
            annotationTypeDeclarationNode.modifiers(),
            Matchers.is(modifiers)
        );
    }

    @Test
    public void returnsName() {
        final AnnotationTypeDeclarationNode annotationTypeDeclarationNode = new QueenAnnotationTypeDeclarationNode(
            Mockito.mock(Position.class),
            new ArrayList<>(),
            new ArrayList<>(),
            "MyAnnotation",
            Mockito.mock(AnnotationTypeBodyNode.class)
        );
        MatcherAssert.assertThat(
            annotationTypeDeclarationNode.name(),
            Matchers.is("MyAnnotation")
        );
    }

    @Test
    public void returnsBody() {
        final AnnotationTypeBodyNode body = Mockito.mock(AnnotationTypeBodyNode.class);
        final AnnotationTypeDeclarationNode annotationTypeDeclarationNode = new QueenAnnotationTypeDeclarationNode(
            Mockito.mock(Position.class),
            new ArrayList<>(),
            new ArrayList<>(),
            "MyAnnotation",
            body
        );
        MatcherAssert.assertThat(
            annotationTypeDeclarationNode.body(),
            Matchers.is(body)
        );
    }

    @Test
    public void addsToCompilationUnitJavaNode() {
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(Mockito.mock(AnnotationNode.class));
        final List<ModifierNode> modifiers = new ArrayList<>();
        modifiers.add(Mockito.mock(ModifierNode.class));
        final AnnotationTypeBodyNode body = Mockito.mock(AnnotationTypeBodyNode.class);

        final AnnotationTypeDeclarationNode annotationTypeDeclarationNode = new QueenAnnotationTypeDeclarationNode(
            Mockito.mock(Position.class),
            annotations,
            modifiers,
            "MyAnnotation",
            body
        );

        final CompilationUnit cu = new CompilationUnit();
        annotationTypeDeclarationNode.addToJavaNode(cu);

        MatcherAssert.assertThat(
            cu.getType(0).asAnnotationDeclaration().getName().asString(),
            Matchers.equalTo("MyAnnotation")
        );
        annotations.forEach(
            a -> Mockito.verify(a, Mockito.times(1)).addToJavaNode(Mockito.any())
        );
        modifiers.forEach(
            m -> Mockito.verify(m, Mockito.times(1)).addToJavaNode(Mockito.any())
        );
        Mockito.verify(body, Mockito.times(1)).addToJavaNode(Mockito.any());
    }

    @Test
    public void addsToClassDeclarationJavaNode() {
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(Mockito.mock(AnnotationNode.class));
        final List<ModifierNode> modifiers = new ArrayList<>();
        modifiers.add(Mockito.mock(ModifierNode.class));
        final AnnotationTypeBodyNode body = Mockito.mock(AnnotationTypeBodyNode.class);

        final AnnotationTypeDeclarationNode annotationTypeDeclarationNode = new QueenAnnotationTypeDeclarationNode(
            Mockito.mock(Position.class),
            annotations,
            modifiers,
            "MyAnnotation",
            body
        );

        final ClassOrInterfaceDeclaration clazz = new ClassOrInterfaceDeclaration();
        annotationTypeDeclarationNode.addToJavaNode(clazz);

        MatcherAssert.assertThat(
            clazz.getMember(0).asAnnotationDeclaration().getName().asString(),
            Matchers.equalTo("MyAnnotation")
        );
        annotations.forEach(
            a -> Mockito.verify(a, Mockito.times(1)).addToJavaNode(Mockito.any())
        );
        modifiers.forEach(
            m -> Mockito.verify(m, Mockito.times(1)).addToJavaNode(Mockito.any())
        );
        Mockito.verify(body, Mockito.times(1)).addToJavaNode(Mockito.any());
    }

    @Test
    public void addsToAnnotationDeclarationJavaNode() {
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(Mockito.mock(AnnotationNode.class));
        final List<ModifierNode> modifiers = new ArrayList<>();
        modifiers.add(Mockito.mock(ModifierNode.class));
        final AnnotationTypeBodyNode body = Mockito.mock(AnnotationTypeBodyNode.class);

        final AnnotationTypeDeclarationNode annotationTypeDeclarationNode = new QueenAnnotationTypeDeclarationNode(
            Mockito.mock(Position.class),
            annotations,
            modifiers,
            "MyAnnotation",
            body
        );

        final AnnotationDeclaration ad = new AnnotationDeclaration();
        annotationTypeDeclarationNode.addToJavaNode(ad);

        MatcherAssert.assertThat(
            ad.getMember(0).asAnnotationDeclaration().getName().asString(),
            Matchers.equalTo("MyAnnotation")
        );
        annotations.forEach(
            a -> Mockito.verify(a, Mockito.times(1)).addToJavaNode(Mockito.any())
        );
        modifiers.forEach(
            m -> Mockito.verify(m, Mockito.times(1)).addToJavaNode(Mockito.any())
        );
        Mockito.verify(body, Mockito.times(1)).addToJavaNode(Mockito.any());
    }

    @Test
    public void returnsChildren() {
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(Mockito.mock(AnnotationNode.class));
        final List<ModifierNode> modifiers = new ArrayList<>();
        modifiers.add(Mockito.mock(ModifierNode.class));
        final AnnotationTypeBodyNode body = Mockito.mock(AnnotationTypeBodyNode.class);

        final AnnotationTypeDeclarationNode annotationTypeDeclarationNode = new QueenAnnotationTypeDeclarationNode(
            Mockito.mock(Position.class),
            annotations,
            modifiers,
            "MyAnnotation",
            body
        );

        final List<QueenNode> children = annotationTypeDeclarationNode.children();
        MatcherAssert.assertThat(
            children.size(),
            Matchers.is(3)
        );
        MatcherAssert.assertThat(
            children.containsAll(
                Arrays.asList(annotations.get(0), modifiers.get(0), body)
            ),
            Matchers.is(true)
        );
    }
}
