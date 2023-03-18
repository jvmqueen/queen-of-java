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

import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.AnnotationMemberDeclaration;
import com.github.javaparser.ast.type.PrimitiveType;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.expressions.AnnotationNode;
import org.queenlang.transpiler.nodes.expressions.ExpressionNode;
import org.queenlang.transpiler.nodes.types.TypeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for {@link QueenAnnotationElementDeclarationNode}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenAnnotationElementDeclarationNodeTestCase {

    @Test
    public void returnsPosition() {
        final Position position = Mockito.mock(Position.class);

        final AnnotationElementDeclarationNode annotationElement = new QueenAnnotationElementDeclarationNode(
            position,
            new ArrayList<>(),
            new ArrayList<>(),
            Mockito.mock(TypeNode.class),
            "grades",
            Mockito.mock(ExpressionNode.class)
        );

        MatcherAssert.assertThat(
            annotationElement.position(),
            Matchers.is(position)
        );
    }

    @Test
    public void returnsAnnotations() {
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(Mockito.mock(AnnotationNode.class));
        final AnnotationElementDeclarationNode annotationElement = new QueenAnnotationElementDeclarationNode(
            Mockito.mock(Position.class),
            annotations,
            new ArrayList<>(),
            Mockito.mock(TypeNode.class),
            "grades",
            Mockito.mock(ExpressionNode.class)
        );

        MatcherAssert.assertThat(
            annotationElement.annotations(),
            Matchers.is(annotations)
        );
    }

    @Test
    public void returnsModifiers() {
        final List<ModifierNode> modifiers = new ArrayList<>();
        modifiers.add(Mockito.mock(ModifierNode.class));
        final AnnotationElementDeclarationNode annotationElement = new QueenAnnotationElementDeclarationNode(
            Mockito.mock(Position.class),
            new ArrayList<>(),
            modifiers,
            Mockito.mock(TypeNode.class),
            "grades",
            Mockito.mock(ExpressionNode.class)
        );

        MatcherAssert.assertThat(
            annotationElement.modifiers(),
            Matchers.is(modifiers)
        );
    }

    @Test
    public void returnsType() {
        final TypeNode type = Mockito.mock(TypeNode.class);
        final AnnotationElementDeclarationNode annotationElement = new QueenAnnotationElementDeclarationNode(
            Mockito.mock(Position.class),
            new ArrayList<>(),
            new ArrayList<>(),
            type,
            "grades",
            Mockito.mock(ExpressionNode.class)
        );

        MatcherAssert.assertThat(
            annotationElement.type(),
            Matchers.is(type)
        );
    }

    @Test
    public void returnsName() {
        final AnnotationElementDeclarationNode annotationElement = new QueenAnnotationElementDeclarationNode(
            Mockito.mock(Position.class),
            new ArrayList<>(),
            new ArrayList<>(),
            Mockito.mock(TypeNode.class),
            "someElement",
            Mockito.mock(ExpressionNode.class)
        );

        MatcherAssert.assertThat(
            annotationElement.name(),
            Matchers.equalTo("someElement")
        );
    }

    @Test
    public void returnsDefaultValue() {
        final ExpressionNode expressionNode = Mockito.mock(ExpressionNode.class);
        final AnnotationElementDeclarationNode annotationElement = new QueenAnnotationElementDeclarationNode(
            Mockito.mock(Position.class),
            new ArrayList<>(),
            new ArrayList<>(),
            Mockito.mock(TypeNode.class),
            "someElement",
            expressionNode
        );

        MatcherAssert.assertThat(
            annotationElement.defaultValue(),
            Matchers.is(expressionNode)
        );
    }

    @Test
    public void addsToAnnotationDeclarationJavaNode() {
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(Mockito.mock(AnnotationNode.class));
        final List<ModifierNode> modifiers = new ArrayList<>();
        modifiers.add(Mockito.mock(ModifierNode.class));
        final TypeNode type = Mockito.mock(TypeNode.class);
        Mockito.when(type.toType()).thenReturn(PrimitiveType.intType());

        final ExpressionNode defaultValue = Mockito.mock(ExpressionNode.class);

        final AnnotationElementDeclarationNode annotationElementDeclarationNode = new QueenAnnotationElementDeclarationNode(
            Mockito.mock(Position.class),
            annotations,
            modifiers,
            type,
            "someName",
            defaultValue
        );

        final AnnotationDeclaration ad = new AnnotationDeclaration();
        annotationElementDeclarationNode.addToJavaNode(ad);

        annotations.forEach(
            a -> Mockito.verify(a, Mockito.times(1)).addToJavaNode(
                Mockito.any(AnnotationMemberDeclaration.class)
            )
        );
        modifiers.forEach(
            m -> Mockito.verify(m, Mockito.times(1)).addToJavaNode(
                Mockito.any(AnnotationMemberDeclaration.class)
            )
        );
        Mockito.verify(type, Mockito.times(1)).toType();
        Mockito.verify(defaultValue, Mockito.times(1)).toJavaExpression();
        MatcherAssert.assertThat(
            ad.getMember(0).asAnnotationMemberDeclaration().getName().asString(),
            Matchers.equalTo("someName")
        );
    }


}
