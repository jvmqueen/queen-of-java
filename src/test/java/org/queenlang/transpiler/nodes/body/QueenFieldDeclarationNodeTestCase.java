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
import org.queenlang.transpiler.nodes.types.TypeNode;
import org.queenlang.transpiler.util.QueenMockito;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for {@link QueenFieldDeclarationNode}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenFieldDeclarationNodeTestCase {
    @Test
    public void returnsPosition() {
        final Position position = QueenMockito.mock(Position.class);
        final FieldDeclarationNode fieldDeclaration = new QueenFieldDeclarationNode(
            position,
            new ArrayList<>(),
            new ArrayList<>(),
            QueenMockito.mock(TypeNode.class),
            QueenMockito.mock(VariableDeclaratorNode.class)
        );
        MatcherAssert.assertThat(
            fieldDeclaration.position(),
            Matchers.is(position)
        );
    }

    @Test
    public void returnsAnnotations() {
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(QueenMockito.mock(AnnotationNode.class));
        final FieldDeclarationNode fieldDeclaration = new QueenFieldDeclarationNode(
            QueenMockito.mock(Position.class),
            annotations,
            new ArrayList<>(),
            QueenMockito.mock(TypeNode.class),
            QueenMockito.mock(VariableDeclaratorNode.class)
        );
        MatcherAssert.assertThat(
            fieldDeclaration.annotations(),
            Matchers.is(annotations)
        );
    }

    @Test
    public void returnsModifiers() {
        final List<ModifierNode> modifiers = new ArrayList<>();
        modifiers.add(QueenMockito.mock(ModifierNode.class));
        final FieldDeclarationNode fieldDeclaration = new QueenFieldDeclarationNode(
            QueenMockito.mock(Position.class),
            new ArrayList<>(),
            modifiers,
            QueenMockito.mock(TypeNode.class),
            QueenMockito.mock(VariableDeclaratorNode.class)
        );
        MatcherAssert.assertThat(
            fieldDeclaration.modifiers(),
            Matchers.is(modifiers)
        );
    }

    @Test
    public void returnTypes() {
        final TypeNode typeNode = QueenMockito.mock(TypeNode.class);
        final FieldDeclarationNode fieldDeclaration = new QueenFieldDeclarationNode(
            QueenMockito.mock(Position.class),
            new ArrayList<>(),
            new ArrayList<>(),
            typeNode,
            QueenMockito.mock(VariableDeclaratorNode.class)
        );
        MatcherAssert.assertThat(
            fieldDeclaration.type(),
            Matchers.is(typeNode)
        );
    }

    @Test
    public void returnsVariables() {
        final VariableDeclaratorNode variable = QueenMockito.mock(VariableDeclaratorNode.class);
        final FieldDeclarationNode fieldDeclaration = new QueenFieldDeclarationNode(
            QueenMockito.mock(Position.class),
            new ArrayList<>(),
            new ArrayList<>(),
            QueenMockito.mock(TypeNode.class),
            variable
        );
        MatcherAssert.assertThat(
            fieldDeclaration.variable(),
            Matchers.is(variable)
        );
    }

    @Test
    public void returnsName() {
        final VariableDeclaratorNode variable = QueenMockito.mock(VariableDeclaratorNode.class);
        final VariableDeclaratorId variableDeclaratorId = QueenMockito.mock(VariableDeclaratorId.class);
        Mockito.when(variableDeclaratorId.name()).thenReturn("v");
        Mockito.when(variable.variableDeclaratorId()).thenReturn(variableDeclaratorId);

        final FieldDeclarationNode fieldDeclaration = new QueenFieldDeclarationNode(
            QueenMockito.mock(Position.class),
            new ArrayList<>(),
            new ArrayList<>(),
            QueenMockito.mock(TypeNode.class),
            variable
        );

        MatcherAssert.assertThat(
            fieldDeclaration.name(),
            Matchers.equalTo("v")
        );
    }

    @Test
    public void returnsChildren() {
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(QueenMockito.mock(AnnotationNode.class));
        final List<ModifierNode> modifiers = new ArrayList<>();
        modifiers.add(QueenMockito.mock(ModifierNode.class));
        final TypeNode type = QueenMockito.mock(TypeNode.class);
        final VariableDeclaratorNode variable = QueenMockito.mock(VariableDeclaratorNode.class);

        final FieldDeclarationNode fieldDeclaration = new QueenFieldDeclarationNode(
            QueenMockito.mock(Position.class),
            annotations,
            modifiers,
            type,
            variable
        );

        final List<QueenNode> children = fieldDeclaration.children();
        MatcherAssert.assertThat(
            children.size(),
            Matchers.is(4)
        );
        MatcherAssert.assertThat(
            children.containsAll(
                List.of(
                    annotations.get(0),
                    modifiers.get(0),
                    type,
                    variable
                )
            ),
            Matchers.is(true)
        );
    }
}
