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
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.expressions.AnnotationNode;
import org.queenlang.transpiler.nodes.expressions.ExpressionNode;
import org.queenlang.transpiler.nodes.types.TypeNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Unit tests for {@link QueenConstantDeclarationNode}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenConstantDeclarationNodeTestCase {
    @Test
    public void returnsPosition() {
        final Position position = Mockito.mock(Position.class);
        final ConstantDeclarationNode constantDeclarationNode = new QueenConstantDeclarationNode(
            position,
            new ArrayList<>(),
            new ArrayList<>(),
            Mockito.mock(TypeNode.class),
            new ArrayList<>()
        );
        MatcherAssert.assertThat(
            constantDeclarationNode.position(),
            Matchers.is(position)
        );
    }

    @Test
    public void returnsAnnotations() {
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(Mockito.mock(AnnotationNode.class));
        final ConstantDeclarationNode constantDeclarationNode = new QueenConstantDeclarationNode(
            Mockito.mock(Position.class),
            annotations,
            new ArrayList<>(),
            Mockito.mock(TypeNode.class),
            new ArrayList<>()
        );
        MatcherAssert.assertThat(
            constantDeclarationNode.annotations(),
            Matchers.is(annotations)
        );
    }

    @Test
    public void returnsModifiers() {
        final List<ModifierNode> modifiers = new ArrayList<>();
        modifiers.add(Mockito.mock(ModifierNode.class));
        final ConstantDeclarationNode constantDeclarationNode = new QueenConstantDeclarationNode(
            Mockito.mock(Position.class),
            new ArrayList<>(),
            modifiers,
            Mockito.mock(TypeNode.class),
            new ArrayList<>()
        );
        MatcherAssert.assertThat(
            constantDeclarationNode.modifiers(),
            Matchers.is(modifiers)
        );
    }

    @Test
    public void returnTypes() {
        final TypeNode typeNode = Mockito.mock(TypeNode.class);
        final ConstantDeclarationNode constantDeclarationNode = new QueenConstantDeclarationNode(
            Mockito.mock(Position.class),
            new ArrayList<>(),
            new ArrayList<>(),
            typeNode,
            new ArrayList<>()
        );
        MatcherAssert.assertThat(
            constantDeclarationNode.type(),
            Matchers.is(typeNode)
        );
    }

    @Test
    public void returnsVariables() {
        final List<VariableDeclaratorNode> variables = new ArrayList<>();
        variables.add(Mockito.mock(VariableDeclaratorNode.class));
        final ConstantDeclarationNode constantDeclarationNode = new QueenConstantDeclarationNode(
            Mockito.mock(Position.class),
            new ArrayList<>(),
            new ArrayList<>(),
            Mockito.mock(TypeNode.class),
            variables
        );
        MatcherAssert.assertThat(
            constantDeclarationNode.variables(),
            Matchers.is(variables)
        );
    }

    @Test
    public void addsToClassDeclarationJavaNode() {
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(Mockito.mock(AnnotationNode.class));
        final List<ModifierNode> modifiers = new ArrayList<>();
        modifiers.add(Mockito.mock(ModifierNode.class));
        final TypeNode type = Mockito.mock(TypeNode.class);
        final List<VariableDeclaratorNode> variables = new ArrayList<>();
        variables.add(Mockito.mock(VariableDeclaratorNode.class));
        variables.add(Mockito.mock(VariableDeclaratorNode.class));

        final ConstantDeclarationNode constantDeclarationNode = new QueenConstantDeclarationNode(
            Mockito.mock(Position.class),
            annotations,
            modifiers,
            type,
            variables
        );

        final ClassOrInterfaceDeclaration clazz = new ClassOrInterfaceDeclaration();
        constantDeclarationNode.addToJavaNode(clazz);
        MatcherAssert.assertThat(
            clazz.getMembers().size(),
            Matchers.equalTo(2)
        );
        MatcherAssert.assertThat(
            clazz.getMember(0).asFieldDeclaration().getVariable(0).getInitializer(),
            Matchers.notNullValue()
        );
        MatcherAssert.assertThat(
            clazz.getMember(1).asFieldDeclaration().getVariable(0).getInitializer(),
            Matchers.notNullValue()
        );
        Mockito.verify(type, Mockito.times(2)).addToJavaNode(
            Mockito.any(VariableDeclarator.class)
        );
        annotations.forEach(
            a -> Mockito.verify(a, Mockito.times(2)).addToJavaNode(
                Mockito.any(FieldDeclaration.class)
            )
        );
        modifiers.forEach(
            m -> Mockito.verify(m, Mockito.times(2)).addToJavaNode(
                Mockito.any(FieldDeclaration.class)
            )
        );
        variables.forEach(
            variable -> {
                Mockito.verify(variable, Mockito.times(1)).addToJavaNode(
                    Mockito.any(VariableDeclarator.class)
                );
            }
        );
    }
}
