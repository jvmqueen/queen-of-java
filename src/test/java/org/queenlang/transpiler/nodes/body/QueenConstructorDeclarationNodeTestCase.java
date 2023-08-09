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
import org.mockito.Mockito;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.QueenNode;
import org.queenlang.transpiler.nodes.expressions.AnnotationNode;
import org.queenlang.transpiler.nodes.statements.BlockStatements;
import org.queenlang.transpiler.nodes.statements.ExplicitConstructorInvocationNode;
import org.queenlang.transpiler.nodes.types.ExceptionTypeNode;
import org.queenlang.transpiler.nodes.types.TypeParameterNode;
import org.queenlang.transpiler.util.QueenMockito;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for {@link QueenConstructorDeclarationNode}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenConstructorDeclarationNodeTestCase {

    @Test
    public void returnsPosition() {
        final Position position = QueenMockito.mock(Position.class);
        final ConstructorDeclarationNode constructorDeclarationNode = new QueenConstructorDeclarationNode(
            position,
            new ArrayList<>(),
            QueenMockito.mock(ModifierNode.class),
            new ArrayList<>(),
            "MyClass",
            QueenMockito.mock(ParameterList.class),
            new ArrayList<>(),
            QueenMockito.mock(ExplicitConstructorInvocationNode.class),
            QueenMockito.mock(BlockStatements.class)
        );
        MatcherAssert.assertThat(
            constructorDeclarationNode.position(),
            Matchers.is(position)
        );
    }

    @Test
    public void returnsName() {
        final ConstructorDeclarationNode constructorDeclarationNode = new QueenConstructorDeclarationNode(
            QueenMockito.mock(Position.class),
            new ArrayList<>(),
            QueenMockito.mock(ModifierNode.class),
            new ArrayList<>(),
            "MyClass",
            QueenMockito.mock(ParameterList.class),
            new ArrayList<>(),
            QueenMockito.mock(ExplicitConstructorInvocationNode.class),
            QueenMockito.mock(BlockStatements.class)
        );
        MatcherAssert.assertThat(
            constructorDeclarationNode.name(),
            Matchers.equalTo("MyClass")
        );
    }

    @Test
    public void returnsAnnotations() {
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(QueenMockito.mock(AnnotationNode.class));
        final ConstructorDeclarationNode constructorDeclarationNode = new QueenConstructorDeclarationNode(
            QueenMockito.mock(Position.class),
            annotations,
            QueenMockito.mock(ModifierNode.class),
            new ArrayList<>(),
            "MyClass",
            QueenMockito.mock(ParameterList.class),
            new ArrayList<>(),
            QueenMockito.mock(ExplicitConstructorInvocationNode.class),
            QueenMockito.mock(BlockStatements.class)
        );
        MatcherAssert.assertThat(
            constructorDeclarationNode.annotations(),
            Matchers.is(annotations)
        );
    }

    @Test
    public void returnsModifier() {
        final ModifierNode modifier = QueenMockito.mock(ModifierNode.class);
        final ConstructorDeclarationNode constructorDeclarationNode = new QueenConstructorDeclarationNode(
            QueenMockito.mock(Position.class),
            new ArrayList<>(),
            modifier,
            new ArrayList<>(),
            "MyClass",
            QueenMockito.mock(ParameterList.class),
            new ArrayList<>(),
            QueenMockito.mock(ExplicitConstructorInvocationNode.class),
            QueenMockito.mock(BlockStatements.class)
        );
        MatcherAssert.assertThat(
            constructorDeclarationNode.modifier(),
            Matchers.is(modifier)
        );
    }

    @Test
    public void returnsTypeParameters() {
        final List<TypeParameterNode> typeParameters = new ArrayList<>();
        typeParameters.add(QueenMockito.mock(TypeParameterNode.class));
        final ConstructorDeclarationNode constructorDeclarationNode = new QueenConstructorDeclarationNode(
            QueenMockito.mock(Position.class),
            new ArrayList<>(),
            QueenMockito.mock(ModifierNode.class),
            typeParameters,
            "MyClass",
            QueenMockito.mock(ParameterList.class),
            new ArrayList<>(),
            QueenMockito.mock(ExplicitConstructorInvocationNode.class),
            QueenMockito.mock(BlockStatements.class)
        );
        MatcherAssert.assertThat(
            constructorDeclarationNode.typeParams(),
            Matchers.is(typeParameters)
        );
    }

    @Test
    public void returnsParameters() {
        final List<ParameterNode> parameters = new ArrayList<>();
        parameters.add(QueenMockito.mock(ParameterNode.class));
        final ParameterList parameterList = QueenMockito.mock(ParameterList.class);
        Mockito.when(parameterList.parameters()).thenReturn(parameters);
        final ConstructorDeclarationNode constructorDeclarationNode = new QueenConstructorDeclarationNode(
            QueenMockito.mock(Position.class),
            new ArrayList<>(),
            QueenMockito.mock(ModifierNode.class),
            new ArrayList<>(),
            "MyClass",
            parameterList,
            new ArrayList<>(),
            QueenMockito.mock(ExplicitConstructorInvocationNode.class),
            QueenMockito.mock(BlockStatements.class)
        );
        MatcherAssert.assertThat(
            constructorDeclarationNode.parameters(),
            Matchers.is(parameterList)
        );
    }

    @Test
    public void returnsThrowsList() {
        final List<ExceptionTypeNode> throwsList = new ArrayList<>();
        throwsList.add(QueenMockito.mock(ExceptionTypeNode.class));
        final ConstructorDeclarationNode constructorDeclarationNode = new QueenConstructorDeclarationNode(
            QueenMockito.mock(Position.class),
            new ArrayList<>(),
            QueenMockito.mock(ModifierNode.class),
            new ArrayList<>(),
            "MyClass",
            QueenMockito.mock(ParameterList.class),
            throwsList,
            QueenMockito.mock(ExplicitConstructorInvocationNode.class),
            QueenMockito.mock(BlockStatements.class)
        );
        MatcherAssert.assertThat(
            constructorDeclarationNode.throwsList(),
            Matchers.is(throwsList)
        );
    }

    @Test
    public void returnsExplicitConstructorInvocationNode() {
        final ExplicitConstructorInvocationNode explicitConstructorInvocationNode = QueenMockito.mock(ExplicitConstructorInvocationNode.class);
        final ConstructorDeclarationNode constructorDeclarationNode = new QueenConstructorDeclarationNode(
            QueenMockito.mock(Position.class),
            new ArrayList<>(),
            QueenMockito.mock(ModifierNode.class),
            new ArrayList<>(),
            "MyClass",
            QueenMockito.mock(ParameterList.class),
            new ArrayList<>(),
            explicitConstructorInvocationNode,
            QueenMockito.mock(BlockStatements.class)
        );
        MatcherAssert.assertThat(
            constructorDeclarationNode.explicitConstructorInvocationNode(),
            Matchers.is(explicitConstructorInvocationNode)
        );
    }

    @Test
    public void returnsBlockStatements() {
        final BlockStatements blockStatements = QueenMockito.mock(BlockStatements.class);
        final ConstructorDeclarationNode constructorDeclarationNode = new QueenConstructorDeclarationNode(
            QueenMockito.mock(Position.class),
            new ArrayList<>(),
            QueenMockito.mock(ModifierNode.class),
            new ArrayList<>(),
            "MyClass",
            QueenMockito.mock(ParameterList.class),
            new ArrayList<>(),
            QueenMockito.mock(ExplicitConstructorInvocationNode.class),
            blockStatements
        );
        MatcherAssert.assertThat(
            constructorDeclarationNode.blockStatements(),
            Matchers.is(blockStatements)
        );
    }

    @Test
    public void returnsChildren() {
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(QueenMockito.mock(AnnotationNode.class));
        final ModifierNode modifier = QueenMockito.mock(ModifierNode.class);
        final List<TypeParameterNode> typeParameters = new ArrayList<>();
        typeParameters.add(QueenMockito.mock(TypeParameterNode.class));
        final List<ParameterNode> parameters = new ArrayList<>();
        parameters.add(QueenMockito.mock(ParameterNode.class));
        final ParameterList parameterList = QueenMockito.mock(ParameterList.class);
        Mockito.when(parameterList.parameters()).thenReturn(parameters);
        final List<ExceptionTypeNode> throwsList = new ArrayList<>();
        throwsList.add(QueenMockito.mock(ExceptionTypeNode.class));
        final ExplicitConstructorInvocationNode explicitConstructorInvocationNode = QueenMockito.mock(ExplicitConstructorInvocationNode.class);
        final BlockStatements blockStatements = QueenMockito.mock(BlockStatements.class);
        final String name = "MyClass";

        final ConstructorDeclarationNode constructorDeclarationNode = new QueenConstructorDeclarationNode(
            QueenMockito.mock(Position.class),
            annotations,
            modifier,
            typeParameters,
            name,
            parameterList,
            throwsList,
            explicitConstructorInvocationNode,
            blockStatements
        );

        final List<QueenNode> children = constructorDeclarationNode.children();
        MatcherAssert.assertThat(
            children.size(),
            Matchers.is(7)
        );
        MatcherAssert.assertThat(
            children.containsAll(
                List.of(
                    annotations.get(0),
                    modifier,
                    typeParameters.get(0),
                    parameterList,
                    throwsList.get(0),
                    explicitConstructorInvocationNode,
                    blockStatements
                )
            ),
            Matchers.is(true)
        );
    }
}
