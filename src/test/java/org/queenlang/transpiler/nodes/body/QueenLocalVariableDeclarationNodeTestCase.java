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

import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.TryStmt;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.QueenNode;
import org.queenlang.transpiler.nodes.expressions.AnnotationNode;
import org.queenlang.transpiler.nodes.types.TypeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for {@link QueenLocalVariableDeclarationNode}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenLocalVariableDeclarationNodeTestCase {

    @Test
    public void returnsPosition() {
        final Position position = Mockito.mock(Position.class);
        final LocalVariableDeclarationNode localVariable = new QueenLocalVariableDeclarationNode(
            position,
            new ArrayList<>(),
            new ArrayList<>(),
            Mockito.mock(TypeNode.class),
            new ArrayList<>()
        );
        MatcherAssert.assertThat(
            localVariable.position(),
            Matchers.is(position)
        );
    }

    @Test
    public void returnsAnnotations() {
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(Mockito.mock(AnnotationNode.class));
        final LocalVariableDeclarationNode localVariable = new QueenLocalVariableDeclarationNode(
            Mockito.mock(Position.class),
            annotations,
            new ArrayList<>(),
            Mockito.mock(TypeNode.class),
            new ArrayList<>()
        );
        MatcherAssert.assertThat(
            localVariable.annotations(),
            Matchers.is(annotations)
        );
    }

    @Test
    public void returnsType() {
        final TypeNode type = Mockito.mock(TypeNode.class);
        final LocalVariableDeclarationNode localVariable = new QueenLocalVariableDeclarationNode(
            Mockito.mock(Position.class),
            new ArrayList<>(),
            new ArrayList<>(),
            type,
            new ArrayList<>()
        );
        MatcherAssert.assertThat(
            localVariable.type(),
            Matchers.is(type)
        );
    }

    @Test
    public void returnsModifiers() {
        final List<ModifierNode> modifiers = new ArrayList<>();
        modifiers.add(Mockito.mock(ModifierNode.class));
        final LocalVariableDeclarationNode localVariable = new QueenLocalVariableDeclarationNode(
            Mockito.mock(Position.class),
            new ArrayList<>(),
            modifiers,
            Mockito.mock(TypeNode.class),
            new ArrayList<>()
        );
        MatcherAssert.assertThat(
            localVariable.modifiers(),
            Matchers.is(modifiers)
        );
    }

    @Test
    public void returnsVariables() {
        final List<VariableDeclaratorNode> variables = new ArrayList<>();
        variables.add(Mockito.mock(VariableDeclaratorNode.class));
        final LocalVariableDeclarationNode localVariable = new QueenLocalVariableDeclarationNode(
            Mockito.mock(Position.class),
            new ArrayList<>(),
            new ArrayList<>(),
            Mockito.mock(TypeNode.class),
            variables
        );
        MatcherAssert.assertThat(
            localVariable.variables(),
            Matchers.is(variables)
        );
    }

    @Test
    public void returnsJavaExpression() {
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(Mockito.mock(AnnotationNode.class));
        final List<ModifierNode> modifiers = new ArrayList<>();
        modifiers.add(Mockito.mock(ModifierNode.class));
        final TypeNode type = Mockito.mock(TypeNode.class);
        final List<VariableDeclaratorNode> variables = new ArrayList<>();
        variables.add(Mockito.mock(VariableDeclaratorNode.class));
        variables.add(Mockito.mock(VariableDeclaratorNode.class));

        final LocalVariableDeclarationNode localVariable = new QueenLocalVariableDeclarationNode(
            Mockito.mock(Position.class),
            annotations,
            modifiers,
            type,
            variables
        );
        final Expression expression = localVariable.toJavaExpression();
        MatcherAssert.assertThat(
            expression,
            Matchers.notNullValue()
        );
        MatcherAssert.assertThat(
            expression,
            Matchers.instanceOf(VariableDeclarationExpr.class)
        );
        annotations.forEach(
            a -> Mockito.verify(a, Mockito.times(1)).addToJavaNode(
                Mockito.any(VariableDeclarationExpr.class)
            )
        );
        modifiers.forEach(
            m -> Mockito.verify(m, Mockito.times(1)).addToJavaNode(
                Mockito.any(VariableDeclarationExpr.class)
            )
        );
        Mockito.verify(type, Mockito.times(2)).addToJavaNode(
            Mockito.any(VariableDeclarator.class)
        );
        variables.forEach(
            v -> {
                Mockito.verify(v, Mockito.times(1)).addToJavaNode(
                    Mockito.any(VariableDeclarator.class)
                );
            }
        );
    }

    @Test
    public void returnsJavaStatement() {
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(Mockito.mock(AnnotationNode.class));
        final List<ModifierNode> modifiers = new ArrayList<>();
        modifiers.add(Mockito.mock(ModifierNode.class));
        final TypeNode type = Mockito.mock(TypeNode.class);
        final List<VariableDeclaratorNode> variables = new ArrayList<>();
        variables.add(Mockito.mock(VariableDeclaratorNode.class));
        variables.add(Mockito.mock(VariableDeclaratorNode.class));

        final LocalVariableDeclarationNode localVariable = new QueenLocalVariableDeclarationNode(
            Mockito.mock(Position.class),
            annotations,
            modifiers,
            type,
            variables
        );
        final Statement statement = localVariable.toJavaStatement();
        MatcherAssert.assertThat(
            statement,
            Matchers.instanceOf(ExpressionStmt.class)
        );
        MatcherAssert.assertThat(
            statement,
            Matchers.notNullValue()
        );
        annotations.forEach(
            a -> Mockito.verify(a, Mockito.times(1)).addToJavaNode(
                Mockito.any(VariableDeclarationExpr.class)
            )
        );
        modifiers.forEach(
            m -> Mockito.verify(m, Mockito.times(1)).addToJavaNode(
                Mockito.any(VariableDeclarationExpr.class)
            )
        );

        Mockito.verify(type, Mockito.times(2)).addToJavaNode(
            Mockito.any(VariableDeclarator.class)
        );
        variables.forEach(
            v -> {
                Mockito.verify(v, Mockito.times(1)).addToJavaNode(
                    Mockito.any(VariableDeclarator.class)
                );
            }
        );
    }

    @Test
    public void addsToBlockStatementJavaNode() {
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(Mockito.mock(AnnotationNode.class));
        final List<ModifierNode> modifiers = new ArrayList<>();
        modifiers.add(Mockito.mock(ModifierNode.class));
        final TypeNode type = Mockito.mock(TypeNode.class);
        final List<VariableDeclaratorNode> variables = new ArrayList<>();
        variables.add(Mockito.mock(VariableDeclaratorNode.class));
        variables.add(Mockito.mock(VariableDeclaratorNode.class));

        final LocalVariableDeclarationNode localVariable = new QueenLocalVariableDeclarationNode(
            Mockito.mock(Position.class),
            annotations,
            modifiers,
            type,
            variables
        );
        final BlockStmt blockStmt = new BlockStmt();
        localVariable.addToJavaNode(blockStmt);
        final Statement statement = blockStmt.getStatements().get(0);
        MatcherAssert.assertThat(
            statement,
            Matchers.instanceOf(ExpressionStmt.class)
        );
        MatcherAssert.assertThat(
            statement,
            Matchers.notNullValue()
        );
        annotations.forEach(
            a -> Mockito.verify(a, Mockito.times(1)).addToJavaNode(
                Mockito.any(VariableDeclarationExpr.class)
            )
        );
        modifiers.forEach(
            m -> Mockito.verify(m, Mockito.times(1)).addToJavaNode(
                Mockito.any(VariableDeclarationExpr.class)
            )
        );

        Mockito.verify(type, Mockito.times(2)).addToJavaNode(
            Mockito.any(VariableDeclarator.class)
        );
        variables.forEach(
            v -> {
                Mockito.verify(v, Mockito.times(1)).addToJavaNode(
                    Mockito.any(VariableDeclarator.class)
                );
            }
        );
    }

    @Test
    public void addsToTryStatementResourcesJavaNode() {
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(Mockito.mock(AnnotationNode.class));
        final List<ModifierNode> modifiers = new ArrayList<>();
        modifiers.add(Mockito.mock(ModifierNode.class));
        final TypeNode type = Mockito.mock(TypeNode.class);
        final List<VariableDeclaratorNode> variables = new ArrayList<>();
        variables.add(Mockito.mock(VariableDeclaratorNode.class));
        variables.add(Mockito.mock(VariableDeclaratorNode.class));

        final LocalVariableDeclarationNode localVariable = new QueenLocalVariableDeclarationNode(
            Mockito.mock(Position.class),
            annotations,
            modifiers,
            type,
            variables
        );
        final TryStmt tryStmt = new TryStmt();
        localVariable.addToJavaNode(tryStmt);
        final Expression expression = tryStmt.getResources().get(0);
        MatcherAssert.assertThat(
            expression,
            Matchers.instanceOf(VariableDeclarationExpr.class)
        );
        MatcherAssert.assertThat(
            expression,
            Matchers.notNullValue()
        );
        annotations.forEach(
            a -> Mockito.verify(a, Mockito.times(1)).addToJavaNode(
                Mockito.any(VariableDeclarationExpr.class)
            )
        );
        modifiers.forEach(
            m -> Mockito.verify(m, Mockito.times(1)).addToJavaNode(
                Mockito.any(VariableDeclarationExpr.class)
            )
        );

        Mockito.verify(type, Mockito.times(2)).addToJavaNode(
            Mockito.any(VariableDeclarator.class)
        );
        variables.forEach(
            v -> {
                Mockito.verify(v, Mockito.times(1)).addToJavaNode(
                    Mockito.any(VariableDeclarator.class)
                );
            }
        );
    }

    @Test
    public void returnsChildren() {
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(Mockito.mock(AnnotationNode.class));
        final List<ModifierNode> modifiers = new ArrayList<>();
        modifiers.add(Mockito.mock(ModifierNode.class));
        final TypeNode type = Mockito.mock(TypeNode.class);
        final List<VariableDeclaratorNode> variables = new ArrayList<>();
        variables.add(Mockito.mock(VariableDeclaratorNode.class));
        variables.add(Mockito.mock(VariableDeclaratorNode.class));

        final LocalVariableDeclarationNode localVariable = new QueenLocalVariableDeclarationNode(
            Mockito.mock(Position.class),
            annotations,
            modifiers,
            type,
            variables
        );

        final List<QueenNode> children = localVariable.children();
        MatcherAssert.assertThat(
            children.size(),
            Matchers.is(5)
        );
        MatcherAssert.assertThat(
            children.containsAll(
                List.of(
                    annotations.get(0),
                    modifiers.get(0),
                    type,
                    variables.get(0),
                    variables.get(1)
                )
            ),
            Matchers.is(true)
        );
    }
}
