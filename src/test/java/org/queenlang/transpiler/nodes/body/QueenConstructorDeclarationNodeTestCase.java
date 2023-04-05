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

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
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
        final Position position = Mockito.mock(Position.class);
        final ConstructorDeclarationNode constructorDeclarationNode = new QueenConstructorDeclarationNode(
            position,
            new ArrayList<>(),
            Mockito.mock(ModifierNode.class),
            new ArrayList<>(),
            "MyClass",
            new ArrayList<>(),
            new ArrayList<>(),
            Mockito.mock(ExplicitConstructorInvocationNode.class),
            Mockito.mock(BlockStatements.class)
        );
        MatcherAssert.assertThat(
            constructorDeclarationNode.position(),
            Matchers.is(position)
        );
    }

    @Test
    public void returnsName() {
        final ConstructorDeclarationNode constructorDeclarationNode = new QueenConstructorDeclarationNode(
            Mockito.mock(Position.class),
            new ArrayList<>(),
            Mockito.mock(ModifierNode.class),
            new ArrayList<>(),
            "MyClass",
            new ArrayList<>(),
            new ArrayList<>(),
            Mockito.mock(ExplicitConstructorInvocationNode.class),
            Mockito.mock(BlockStatements.class)
        );
        MatcherAssert.assertThat(
            constructorDeclarationNode.name(),
            Matchers.equalTo("MyClass")
        );
    }

    @Test
    public void returnsAnnotations() {
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(Mockito.mock(AnnotationNode.class));
        final ConstructorDeclarationNode constructorDeclarationNode = new QueenConstructorDeclarationNode(
            Mockito.mock(Position.class),
            annotations,
            Mockito.mock(ModifierNode.class),
            new ArrayList<>(),
            "MyClass",
            new ArrayList<>(),
            new ArrayList<>(),
            Mockito.mock(ExplicitConstructorInvocationNode.class),
            Mockito.mock(BlockStatements.class)
        );
        MatcherAssert.assertThat(
            constructorDeclarationNode.annotations(),
            Matchers.is(annotations)
        );
    }

    @Test
    public void returnsModifier() {
        final ModifierNode modifier = Mockito.mock(ModifierNode.class);
        final ConstructorDeclarationNode constructorDeclarationNode = new QueenConstructorDeclarationNode(
            Mockito.mock(Position.class),
            new ArrayList<>(),
            modifier,
            new ArrayList<>(),
            "MyClass",
            new ArrayList<>(),
            new ArrayList<>(),
            Mockito.mock(ExplicitConstructorInvocationNode.class),
            Mockito.mock(BlockStatements.class)
        );
        MatcherAssert.assertThat(
            constructorDeclarationNode.modifier(),
            Matchers.is(modifier)
        );
    }

    @Test
    public void returnsTypeParameters() {
        final List<TypeParameterNode> typeParameters = new ArrayList<>();
        typeParameters.add(Mockito.mock(TypeParameterNode.class));
        final ConstructorDeclarationNode constructorDeclarationNode = new QueenConstructorDeclarationNode(
            Mockito.mock(Position.class),
            new ArrayList<>(),
            Mockito.mock(ModifierNode.class),
            typeParameters,
            "MyClass",
            new ArrayList<>(),
            new ArrayList<>(),
            Mockito.mock(ExplicitConstructorInvocationNode.class),
            Mockito.mock(BlockStatements.class)
        );
        MatcherAssert.assertThat(
            constructorDeclarationNode.typeParams(),
            Matchers.is(typeParameters)
        );
    }

    @Test
    public void returnsParameters() {
        final List<ParameterNode> parameters = new ArrayList<>();
        parameters.add(Mockito.mock(ParameterNode.class));
        final ConstructorDeclarationNode constructorDeclarationNode = new QueenConstructorDeclarationNode(
            Mockito.mock(Position.class),
            new ArrayList<>(),
            Mockito.mock(ModifierNode.class),
            new ArrayList<>(),
            "MyClass",
            parameters,
            new ArrayList<>(),
            Mockito.mock(ExplicitConstructorInvocationNode.class),
            Mockito.mock(BlockStatements.class)
        );
        MatcherAssert.assertThat(
            constructorDeclarationNode.parameters(),
            Matchers.is(parameters)
        );
    }

    @Test
    public void returnsThrowsList() {
        final List<ExceptionTypeNode> throwsList = new ArrayList<>();
        throwsList.add(Mockito.mock(ExceptionTypeNode.class));
        final ConstructorDeclarationNode constructorDeclarationNode = new QueenConstructorDeclarationNode(
            Mockito.mock(Position.class),
            new ArrayList<>(),
            Mockito.mock(ModifierNode.class),
            new ArrayList<>(),
            "MyClass",
            new ArrayList<>(),
            throwsList,
            Mockito.mock(ExplicitConstructorInvocationNode.class),
            Mockito.mock(BlockStatements.class)
        );
        MatcherAssert.assertThat(
            constructorDeclarationNode.throwsList(),
            Matchers.is(throwsList)
        );
    }

    @Test
    public void returnsExplicitConstructorInvocationNode() {
        final ExplicitConstructorInvocationNode explicitConstructorInvocationNode = Mockito.mock(ExplicitConstructorInvocationNode.class);
        final ConstructorDeclarationNode constructorDeclarationNode = new QueenConstructorDeclarationNode(
            Mockito.mock(Position.class),
            new ArrayList<>(),
            Mockito.mock(ModifierNode.class),
            new ArrayList<>(),
            "MyClass",
            new ArrayList<>(),
            new ArrayList<>(),
            explicitConstructorInvocationNode,
            Mockito.mock(BlockStatements.class)
        );
        MatcherAssert.assertThat(
            constructorDeclarationNode.explicitConstructorInvocationNode(),
            Matchers.is(explicitConstructorInvocationNode)
        );
    }

    @Test
    public void returnsBlockStatements() {
        final BlockStatements blockStatements = Mockito.mock(BlockStatements.class);
        final ConstructorDeclarationNode constructorDeclarationNode = new QueenConstructorDeclarationNode(
            Mockito.mock(Position.class),
            new ArrayList<>(),
            Mockito.mock(ModifierNode.class),
            new ArrayList<>(),
            "MyClass",
            new ArrayList<>(),
            new ArrayList<>(),
            Mockito.mock(ExplicitConstructorInvocationNode.class),
            blockStatements
        );
        MatcherAssert.assertThat(
            constructorDeclarationNode.blockStatements(),
            Matchers.is(blockStatements)
        );
    }

    @Test
    public void addsToConstructorJavaNode() {
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(Mockito.mock(AnnotationNode.class));
        final ModifierNode modifier = Mockito.mock(ModifierNode.class);
        final List<TypeParameterNode> typeParameters = new ArrayList<>();
        typeParameters.add(Mockito.mock(TypeParameterNode.class));
        final List<ParameterNode> parameters = new ArrayList<>();
        parameters.add(Mockito.mock(ParameterNode.class));
        final List<ExceptionTypeNode> throwsList = new ArrayList<>();
        throwsList.add(Mockito.mock(ExceptionTypeNode.class));
        final ExplicitConstructorInvocationNode explicitConstructorInvocationNode = Mockito.mock(ExplicitConstructorInvocationNode.class);
        final BlockStatements blockStatements = Mockito.mock(BlockStatements.class);
        final String name = "MyClass";

        final ConstructorDeclarationNode constructorDeclarationNode = new QueenConstructorDeclarationNode(
            Mockito.mock(Position.class),
            annotations,
            modifier,
            typeParameters,
            name,
            parameters,
            throwsList,
            explicitConstructorInvocationNode,
            blockStatements
        );
        final ClassOrInterfaceDeclaration clazz = new ClassOrInterfaceDeclaration();
        clazz.setName("MyClass");
        constructorDeclarationNode.addToJavaNode(clazz);

        MatcherAssert.assertThat(
            clazz.getConstructors().get(0).getName().asString(),
            Matchers.equalTo("MyClass")
        );
        Mockito.verify(modifier, Mockito.times(1)).addToJavaNode(Mockito.any());
        annotations.forEach(
            a -> Mockito.verify(a, Mockito.times(1)).addToJavaNode(Mockito.any())
        );
        typeParameters.forEach(
            tp -> Mockito.verify(tp, Mockito.times(1)).addToJavaNode(Mockito.any())
        );
        parameters.forEach(
            p -> Mockito.verify(p, Mockito.times(1)).addToJavaNode(Mockito.any())
        );
        throwsList.forEach(
            t -> Mockito.verify(t, Mockito.times(1)).addToJavaNode(Mockito.any())
        );
        Mockito.verify(explicitConstructorInvocationNode, Mockito.times(1)).addToJavaNode(Mockito.any());
        Mockito.verify(blockStatements, Mockito.times(1)).addToJavaNode(Mockito.any());
    }

    @Test
    public void returnsChildren() {
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(Mockito.mock(AnnotationNode.class));
        final ModifierNode modifier = Mockito.mock(ModifierNode.class);
        final List<TypeParameterNode> typeParameters = new ArrayList<>();
        typeParameters.add(Mockito.mock(TypeParameterNode.class));
        final List<ParameterNode> parameters = new ArrayList<>();
        parameters.add(Mockito.mock(ParameterNode.class));
        final List<ExceptionTypeNode> throwsList = new ArrayList<>();
        throwsList.add(Mockito.mock(ExceptionTypeNode.class));
        final ExplicitConstructorInvocationNode explicitConstructorInvocationNode = Mockito.mock(ExplicitConstructorInvocationNode.class);
        final BlockStatements blockStatements = Mockito.mock(BlockStatements.class);
        final String name = "MyClass";

        final ConstructorDeclarationNode constructorDeclarationNode = new QueenConstructorDeclarationNode(
            Mockito.mock(Position.class),
            annotations,
            modifier,
            typeParameters,
            name,
            parameters,
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
                    parameters.get(0),
                    throwsList.get(0),
                    explicitConstructorInvocationNode,
                    blockStatements
                )
            ),
            Matchers.is(true)
        );
    }
}
