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
package org.queenlang.transpiler.nodes.statements;

import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExplicitConstructorInvocationStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.expressions.ExpressionNode;
import org.queenlang.transpiler.nodes.types.TypeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for {@link QueenExplicitConstructorInvocationNode}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenExplicitConstructorInvocationNodeTestCase {

    @Test
    public void returnsPosition() {
        final Position position = Mockito.mock(Position.class);
        final ExplicitConstructorInvocationNode explicit = new QueenExplicitConstructorInvocationNode(
            position,
            true,
            Mockito.mock(ExpressionNode.class),
            new ArrayList<>(),
            new ArrayList<>()
        );
        MatcherAssert.assertThat(
            explicit.position(),
            Matchers.is(position)
        );
    }

    @Test
    public void returnsIsThis() {
        final Position position = Mockito.mock(Position.class);
        final ExplicitConstructorInvocationNode explicit = new QueenExplicitConstructorInvocationNode(
            position,
            true,
            Mockito.mock(ExpressionNode.class),
            new ArrayList<>(),
            new ArrayList<>()
        );
        MatcherAssert.assertThat(
            explicit.isThis(),
            Matchers.is(true)
        );
    }

    @Test
    public void returnsScope() {
        final Position position = Mockito.mock(Position.class);
        final ExpressionNode scope = Mockito.mock(ExpressionNode.class);
        final ExplicitConstructorInvocationNode explicit = new QueenExplicitConstructorInvocationNode(
            position,
            true,
            scope,
            new ArrayList<>(),
            new ArrayList<>()
        );
        MatcherAssert.assertThat(
            explicit.scope(),
            Matchers.is(scope)
        );
    }

    @Test
    public void returnsTypeArguments() {
        final Position position = Mockito.mock(Position.class);
        final ExpressionNode scope = Mockito.mock(ExpressionNode.class);
        final List<TypeNode> typeArgs = new ArrayList<>();
        typeArgs.add(Mockito.mock(TypeNode.class));
        final ExplicitConstructorInvocationNode explicit = new QueenExplicitConstructorInvocationNode(
            position,
            true,
            scope,
            typeArgs,
            new ArrayList<>()
        );
        MatcherAssert.assertThat(
            explicit.typeArguments(),
            Matchers.is(typeArgs)
        );
    }

    @Test
    public void returnsArguments() {
        final Position position = Mockito.mock(Position.class);
        final ExpressionNode scope = Mockito.mock(ExpressionNode.class);
        final List<TypeNode> typeArgs = new ArrayList<>();
        typeArgs.add(Mockito.mock(TypeNode.class));
        final List<ExpressionNode> args = new ArrayList<>();
        args.add(Mockito.mock(ExpressionNode.class));

        final ExplicitConstructorInvocationNode explicit = new QueenExplicitConstructorInvocationNode(
            position,
            true,
            scope,
            typeArgs,
            args
        );
        MatcherAssert.assertThat(
            explicit.arguments(),
            Matchers.is(args)
        );
    }

    @Test
    public void addsToJavaNode() {
        final Position position = Mockito.mock(Position.class);
        final ExpressionNode scope = Mockito.mock(ExpressionNode.class);
        final List<TypeNode> typeArgs = new ArrayList<>();
        final TypeNode typeArg = Mockito.mock(TypeNode.class);
        Mockito.when(typeArg.toType()).thenReturn(new ClassOrInterfaceType("String"));
        typeArgs.add(typeArg);
        final List<ExpressionNode> args = new ArrayList<>();
        final ExpressionNode arg = Mockito.mock(ExpressionNode.class);
        Mockito.when(arg.toJavaExpression()).thenReturn(new NameExpr("a"));
        args.add(arg);

        final ExplicitConstructorInvocationNode explicit = new QueenExplicitConstructorInvocationNode(
            position,
            true,
            scope,
            typeArgs,
            args
        );
        final BlockStmt blockStmt = new BlockStmt();
        explicit.addToJavaNode(blockStmt);

        MatcherAssert.assertThat(
            blockStmt.getStatement(0).asExplicitConstructorInvocationStmt(),
            Matchers.notNullValue()
        );
        MatcherAssert.assertThat(
            blockStmt.getStatement(0).asExplicitConstructorInvocationStmt().isThis(),
            Matchers.is(true)
        );
        Mockito.verify(typeArg, Mockito.times(1)).addToJavaNode(
            Mockito.any(ExplicitConstructorInvocationStmt.class)
        );
        Mockito.verify(arg, Mockito.times(1)).toJavaExpression();
        Mockito.verify(scope, Mockito.times(1)).toJavaExpression();
    }
}
