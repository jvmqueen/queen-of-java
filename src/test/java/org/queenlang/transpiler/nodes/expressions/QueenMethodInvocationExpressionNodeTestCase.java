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
package org.queenlang.transpiler.nodes.expressions;

import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.QueenNode;
import org.queenlang.transpiler.nodes.types.TypeNode;
import org.queenlang.transpiler.util.QueenMockito;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for {@link QueenMethodInvocationExpressionNode}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenMethodInvocationExpressionNodeTestCase {

    @Test
    public void returnsPosition() {
        final Position position = QueenMockito.mock(Position.class);
        final MethodInvocationExpressionNode methodInvocation = new QueenMethodInvocationExpressionNode(
            position,
            QueenMockito.mock(ExpressionNode.class),
            new ArrayList<>(),
            "doSomething",
            new ArrayList<>()
        );
        MatcherAssert.assertThat(
            methodInvocation.position(),
            Matchers.is(position)
        );
    }

    @Test
    public void returnsScope() {
        final Position position = QueenMockito.mock(Position.class);
        final ExpressionNode scope = QueenMockito.mock(ExpressionNode.class);
        final MethodInvocationExpressionNode methodInvocation = new QueenMethodInvocationExpressionNode(
            position,
            scope,
            new ArrayList<>(),
            "doSomething",
            new ArrayList<>()
        );
        MatcherAssert.assertThat(
            methodInvocation.scope(),
            Matchers.is(scope)
        );
    }

    @Test
    public void returnsTypeArguments() {
        final Position position = QueenMockito.mock(Position.class);
        final ExpressionNode scope = QueenMockito.mock(ExpressionNode.class);
        final List<TypeNode> typeArguments = new ArrayList<>();
        typeArguments.add(QueenMockito.mock(TypeNode.class));
        final MethodInvocationExpressionNode methodInvocation = new QueenMethodInvocationExpressionNode(
            position,
            scope,
            typeArguments,
            "doSomething",
            new ArrayList<>()
        );
        MatcherAssert.assertThat(
            methodInvocation.typeArguments(),
            Matchers.is(typeArguments)
        );
    }

    @Test
    public void returnsName() {
        final Position position = QueenMockito.mock(Position.class);
        final ExpressionNode scope = QueenMockito.mock(ExpressionNode.class);
        final List<TypeNode> typeArguments = new ArrayList<>();
        typeArguments.add(QueenMockito.mock(TypeNode.class));
        final MethodInvocationExpressionNode methodInvocation = new QueenMethodInvocationExpressionNode(
            position,
            scope,
            typeArguments,
            "doSomething",
            new ArrayList<>()
        );
        MatcherAssert.assertThat(
            methodInvocation.name(),
            Matchers.equalTo("doSomething")
        );
    }

    @Test
    public void returnsArguments() {
        final Position position = QueenMockito.mock(Position.class);
        final ExpressionNode scope = QueenMockito.mock(ExpressionNode.class);
        final List<TypeNode> typeArguments = new ArrayList<>();
        typeArguments.add(QueenMockito.mock(TypeNode.class));
        final List<ExpressionNode> arguments = new ArrayList<>();
        arguments.add(QueenMockito.mock(ExpressionNode.class));
        final MethodInvocationExpressionNode methodInvocation = new QueenMethodInvocationExpressionNode(
            position,
            scope,
            typeArguments,
            "doSomething",
            arguments
        );
        MatcherAssert.assertThat(
            methodInvocation.arguments(),
            Matchers.is(arguments)
        );
    }

    @Test
    public void returnsJavaExpressionWithArgs() {
        final Position position = QueenMockito.mock(Position.class);
        final ExpressionNode scope = QueenMockito.mock(ExpressionNode.class);
        final List<TypeNode> typeArguments = new ArrayList<>();
        typeArguments.add(QueenMockito.mock(TypeNode.class));
        final List<ExpressionNode> arguments = new ArrayList<>();
        final ExpressionNode arg = QueenMockito.mock(ExpressionNode.class);
        Mockito.when(arg.toJavaExpression()).thenReturn(new NameExpr("a"));
        arguments.add(arg);
        final MethodInvocationExpressionNode methodInvocation = new QueenMethodInvocationExpressionNode(
            position,
            scope,
            typeArguments,
            "doSomething",
            arguments
        );
        final MethodCallExpr methodCall = (MethodCallExpr) methodInvocation.toJavaExpression();
        Mockito.verify(scope, Mockito.times(1)).toJavaExpression();
        typeArguments.forEach(ta -> Mockito.verify(ta, Mockito.times(1)).addToJavaNode(
            Mockito.any(MethodCallExpr.class)
        ));
        MatcherAssert.assertThat(
            methodCall.getName().asString(),
            Matchers.equalTo("doSomething")
        );
        MatcherAssert.assertThat(
            methodCall.getArguments().get(0).asNameExpr().toString(),
            Matchers.equalTo("a")
        );
    }

    @Test
    public void returnsJavaExpressionWithoutArgs() {
        final Position position = QueenMockito.mock(Position.class);
        final ExpressionNode scope = QueenMockito.mock(ExpressionNode.class);
        final List<TypeNode> typeArguments = new ArrayList<>();
        typeArguments.add(QueenMockito.mock(TypeNode.class));
        final List<ExpressionNode> arguments = new ArrayList<>();
        final MethodInvocationExpressionNode methodInvocation = new QueenMethodInvocationExpressionNode(
            position,
            scope,
            typeArguments,
            "doSomething",
            arguments
        );
        final MethodCallExpr methodCall = (MethodCallExpr) methodInvocation.toJavaExpression();
        Mockito.verify(scope, Mockito.times(1)).toJavaExpression();
        typeArguments.forEach(ta -> Mockito.verify(ta, Mockito.times(1)).addToJavaNode(
            Mockito.any(MethodCallExpr.class)
        ));
        MatcherAssert.assertThat(
            methodCall.getName().asString(),
            Matchers.equalTo("doSomething")
        );
        MatcherAssert.assertThat(
            methodCall.getArguments().size(),
            Matchers.equalTo(0)
        );
    }

    @Test
    public void returnsChildren() {
        final Position position = QueenMockito.mock(Position.class);
        final ExpressionNode scope = QueenMockito.mock(ExpressionNode.class);
        final List<TypeNode> typeArguments = new ArrayList<>();
        typeArguments.add(QueenMockito.mock(TypeNode.class));
        final List<ExpressionNode> arguments = new ArrayList<>();
        arguments.add(QueenMockito.mock(ExpressionNode.class));
        final MethodInvocationExpressionNode methodInvocation = new QueenMethodInvocationExpressionNode(
            position,
            scope,
            typeArguments,
            "doSomething",
            arguments
        );

        final List<QueenNode> children = methodInvocation.children();
        MatcherAssert.assertThat(
            children.size(),
            Matchers.is(3)
        );
        MatcherAssert.assertThat(
            children.containsAll(
                List.of(
                    scope,
                    typeArguments.get(0),
                    arguments.get(0)
                )
            ),
            Matchers.is(true)
        );
    }

}
