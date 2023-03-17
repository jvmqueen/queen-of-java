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
import org.queenlang.transpiler.nodes.expressions.AnnotationNode;
import org.queenlang.transpiler.nodes.expressions.ArrayDimensionNode;
import org.queenlang.transpiler.nodes.statements.BlockStatements;
import org.queenlang.transpiler.nodes.types.ExceptionTypeNode;
import org.queenlang.transpiler.nodes.types.TypeNode;
import org.queenlang.transpiler.nodes.types.TypeParameterNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for {@link QueenInterfaceMethodDeclarationNode}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenInterfaceMethodDeclarationNodeTestCase {

    @Test
    public void returnsPosition() {
        final Position position = Mockito.mock(Position.class);
        final InterfaceMethodDeclarationNode methodDeclaration = new QueenInterfaceMethodDeclarationNode(
            position,
            new ArrayList<>(),
            new ArrayList<>(),
            Mockito.mock(TypeNode.class),
            new ArrayList<>(),
            new ArrayList<>(),
            "myMethod",
            new ArrayList<>(),
            new ArrayList<>(),
            Mockito.mock(BlockStatements.class)
        );
        MatcherAssert.assertThat(
            methodDeclaration.position(),
            Matchers.is(position)
        );
    }

    @Test
    public void returnsAnnotations() {
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(Mockito.mock(AnnotationNode.class));
        final InterfaceMethodDeclarationNode methodDeclaration = new QueenInterfaceMethodDeclarationNode(
            Mockito.mock(Position.class),
            annotations,
            new ArrayList<>(),
            Mockito.mock(TypeNode.class),
            new ArrayList<>(),
            new ArrayList<>(),
            "myMethod",
            new ArrayList<>(),
            new ArrayList<>(),
            Mockito.mock(BlockStatements.class)
        );
        MatcherAssert.assertThat(
            methodDeclaration.annotations(),
            Matchers.is(annotations)
        );
    }

    @Test
    public void returnsModifiers() {
        final List<ModifierNode> modifiers = new ArrayList<>();
        modifiers.add(Mockito.mock(ModifierNode.class));
        final InterfaceMethodDeclarationNode methodDeclaration = new QueenInterfaceMethodDeclarationNode(
            Mockito.mock(Position.class),
            new ArrayList<>(),
            modifiers,
            Mockito.mock(TypeNode.class),
            new ArrayList<>(),
            new ArrayList<>(),
            "myMethod",
            new ArrayList<>(),
            new ArrayList<>(),
            Mockito.mock(BlockStatements.class)
        );
        MatcherAssert.assertThat(
            methodDeclaration.modifiers(),
            Matchers.is(modifiers)
        );
    }

    @Test
    public void returnsReturnType() {
        final TypeNode returnType = Mockito.mock(TypeNode.class);
        final InterfaceMethodDeclarationNode methodDeclaration = new QueenInterfaceMethodDeclarationNode(
            Mockito.mock(Position.class),
            new ArrayList<>(),
            new ArrayList<>(),
            returnType,
            new ArrayList<>(),
            new ArrayList<>(),
            "myMethod",
            new ArrayList<>(),
            new ArrayList<>(),
            Mockito.mock(BlockStatements.class)
        );
        MatcherAssert.assertThat(
            methodDeclaration.returnType(),
            Matchers.is(returnType)
        );
    }

    @Test
    public void returnsDims() {
        final List<ArrayDimensionNode> dims = new ArrayList<>();
        dims.add(Mockito.mock(ArrayDimensionNode.class));
        final InterfaceMethodDeclarationNode methodDeclaration = new QueenInterfaceMethodDeclarationNode(
            Mockito.mock(Position.class),
            new ArrayList<>(),
            new ArrayList<>(),
            Mockito.mock(TypeNode.class),
            dims,
            new ArrayList<>(),
            "myMethod",
            new ArrayList<>(),
            new ArrayList<>(),
            Mockito.mock(BlockStatements.class)
        );
        MatcherAssert.assertThat(
            methodDeclaration.dims(),
            Matchers.is(dims)
        );
    }

    @Test
    public void returnsTypeParameters() {
        final List<TypeParameterNode> typeParameters = new ArrayList<>();
        typeParameters.add(Mockito.mock(TypeParameterNode.class));
        final InterfaceMethodDeclarationNode methodDeclaration = new QueenInterfaceMethodDeclarationNode(
            Mockito.mock(Position.class),
            new ArrayList<>(),
            new ArrayList<>(),
            Mockito.mock(TypeNode.class),
            new ArrayList<>(),
            typeParameters,
            "myMethod",
            new ArrayList<>(),
            new ArrayList<>(),
            Mockito.mock(BlockStatements.class)
        );
        MatcherAssert.assertThat(
            methodDeclaration.typeParameters(),
            Matchers.is(typeParameters)
        );
    }

    @Test
    public void returnsName() {
        final InterfaceMethodDeclarationNode methodDeclaration = new QueenInterfaceMethodDeclarationNode(
            Mockito.mock(Position.class),
            new ArrayList<>(),
            new ArrayList<>(),
            Mockito.mock(TypeNode.class),
            new ArrayList<>(),
            new ArrayList<>(),
            "myMethod",
            new ArrayList<>(),
            new ArrayList<>(),
            Mockito.mock(BlockStatements.class)
        );
        MatcherAssert.assertThat(
            methodDeclaration.name(),
            Matchers.equalTo("myMethod")
        );
    }

    @Test
    public void returnsParameters() {
        final List<ParameterNode> parameters = new ArrayList<>();
        parameters.add(Mockito.mock(ParameterNode.class));
        final InterfaceMethodDeclarationNode methodDeclaration = new QueenInterfaceMethodDeclarationNode(
            Mockito.mock(Position.class),
            new ArrayList<>(),
            new ArrayList<>(),
            Mockito.mock(TypeNode.class),
            new ArrayList<>(),
            new ArrayList<>(),
            "myMethod",
            parameters,
            new ArrayList<>(),
            Mockito.mock(BlockStatements.class)
        );
        MatcherAssert.assertThat(
            methodDeclaration.parameters(),
            Matchers.is(parameters)
        );
    }

    @Test
    public void returnsThrowsList() {
        final List<ExceptionTypeNode> throwsList = new ArrayList<>();
        throwsList.add(Mockito.mock(ExceptionTypeNode.class));
        final InterfaceMethodDeclarationNode methodDeclaration = new QueenInterfaceMethodDeclarationNode(
            Mockito.mock(Position.class),
            new ArrayList<>(),
            new ArrayList<>(),
            Mockito.mock(TypeNode.class),
            new ArrayList<>(),
            new ArrayList<>(),
            "myMethod",
            new ArrayList<>(),
            throwsList,
            Mockito.mock(BlockStatements.class)
        );
        MatcherAssert.assertThat(
            methodDeclaration.throwsList(),
            Matchers.is(throwsList)
        );
    }

    @Test
    public void returnsBlockStatements() {
        final BlockStatements blockStatements = Mockito.mock(BlockStatements.class);
        final InterfaceMethodDeclarationNode methodDeclaration = new QueenInterfaceMethodDeclarationNode(
            Mockito.mock(Position.class),
            new ArrayList<>(),
            new ArrayList<>(),
            Mockito.mock(TypeNode.class),
            new ArrayList<>(),
            new ArrayList<>(),
            "myMethod",
            new ArrayList<>(),
            new ArrayList<>(),
            blockStatements
        );
        MatcherAssert.assertThat(
            methodDeclaration.blockStatements(),
            Matchers.is(blockStatements)
        );
    }

    @Test
    public void addsToClassDeclarationJavaNode() {
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(Mockito.mock(AnnotationNode.class));
        final List<ModifierNode> modifiers = new ArrayList<>();
        modifiers.add(Mockito.mock(ModifierNode.class));
        final List<TypeParameterNode> typeParameters = new ArrayList<>();
        typeParameters.add(Mockito.mock(TypeParameterNode.class));
        final List<ParameterNode> parameters = new ArrayList<>();
        parameters.add(Mockito.mock(ParameterNode.class));
        final List<ExceptionTypeNode> throwsList = new ArrayList<>();
        throwsList.add(Mockito.mock(ExceptionTypeNode.class));
        final List<ArrayDimensionNode> dims = new ArrayList<>();
        dims.add(Mockito.mock(ArrayDimensionNode.class));
        final TypeNode returnType = Mockito.mock(TypeNode.class);
        final BlockStatements blockStatements = Mockito.mock(BlockStatements.class);
        final String name = "myMethod";

        final InterfaceMethodDeclarationNode methodDeclaration = new QueenInterfaceMethodDeclarationNode(
            Mockito.mock(Position.class),
            annotations,
            modifiers,
            returnType,
            dims,
            typeParameters,
            name,
            parameters,
            throwsList,
            blockStatements
        );
        final ClassOrInterfaceDeclaration classOrInterfaceDeclaration = new ClassOrInterfaceDeclaration();
        methodDeclaration.addToJavaNode(classOrInterfaceDeclaration);

        MatcherAssert.assertThat(
            classOrInterfaceDeclaration.getMember(0).asMethodDeclaration().getName().asString(),
            Matchers.equalTo("myMethod")
        );

        annotations.forEach(
            a -> Mockito.verify(a, Mockito.times(1)).addToJavaNode(Mockito.any())
        );
        modifiers.forEach(
            m -> Mockito.verify(m, Mockito.times(1)).addToJavaNode(Mockito.any())
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
        Mockito.verify(returnType, Mockito.times(1)).addToJavaNode(Mockito.any());
        Mockito.verify(blockStatements, Mockito.times(1)).addToJavaNode(Mockito.any());
    }
}