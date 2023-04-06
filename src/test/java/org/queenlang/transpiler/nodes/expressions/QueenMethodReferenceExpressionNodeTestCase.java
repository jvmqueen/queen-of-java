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

import com.github.javaparser.ast.expr.MethodReferenceExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.QueenNode;
import org.queenlang.transpiler.nodes.types.TypeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for {@link QueenMethodReferenceExpressionNode}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenMethodReferenceExpressionNodeTestCase {

    @Test
    public void returnsPosition() {
        final Position position = Mockito.mock(Position.class);
        final MethodReferenceExpressionNode methodRef = new QueenMethodReferenceExpressionNode(
            position,
            Mockito.mock(TypeNode.class),
            Mockito.mock(ExpressionNode.class),
            new ArrayList<>(),
            "println"
        );
        MatcherAssert.assertThat(
            methodRef.position(),
            Matchers.is(position)
        );
    }

    @Test
    public void returnsType() {
        final Position position = Mockito.mock(Position.class);
        final TypeNode type = Mockito.mock(TypeNode.class);
        final MethodReferenceExpressionNode methodRef = new QueenMethodReferenceExpressionNode(
            position,
            type,
            Mockito.mock(ExpressionNode.class),
            new ArrayList<>(),
            "println"
        );
        MatcherAssert.assertThat(
            methodRef.type(),
            Matchers.is(type)
        );
    }

    @Test
    public void returnsScope() {
        final Position position = Mockito.mock(Position.class);
        final TypeNode type = Mockito.mock(TypeNode.class);
        final ExpressionNode scope = Mockito.mock(ExpressionNode.class);
        final MethodReferenceExpressionNode methodRef = new QueenMethodReferenceExpressionNode(
            position,
            type,
            scope,
            new ArrayList<>(),
            "println"
        );
        MatcherAssert.assertThat(
            methodRef.scope(),
            Matchers.is(scope)
        );
    }

    @Test
    public void returnsTypeArgs() {
        final Position position = Mockito.mock(Position.class);
        final TypeNode type = Mockito.mock(TypeNode.class);
        final ExpressionNode scope = Mockito.mock(ExpressionNode.class);
        final List<TypeNode> typeArguments = new ArrayList<>();
        typeArguments.add(Mockito.mock(TypeNode.class));
        final MethodReferenceExpressionNode methodRef = new QueenMethodReferenceExpressionNode(
            position,
            type,
            scope,
            typeArguments,
            "println"
        );
        MatcherAssert.assertThat(
            methodRef.typeArguments(),
            Matchers.is(typeArguments)
        );
    }

    @Test
    public void returnsIdentifier() {
        final Position position = Mockito.mock(Position.class);
        final TypeNode type = Mockito.mock(TypeNode.class);
        final ExpressionNode scope = Mockito.mock(ExpressionNode.class);
        final List<TypeNode> typeArguments = new ArrayList<>();
        typeArguments.add(Mockito.mock(TypeNode.class));
        final MethodReferenceExpressionNode methodRef = new QueenMethodReferenceExpressionNode(
            position,
            type,
            scope,
            typeArguments,
            "println"
        );
        MatcherAssert.assertThat(
            methodRef.identifier(),
            Matchers.equalTo("println")
        );
    }

    @Test
    public void returnsJavaTypeExpression() {
        final Position position = Mockito.mock(Position.class);
        final TypeNode type = Mockito.mock(TypeNode.class);
        Mockito.when(type.toType()).thenReturn(new ClassOrInterfaceType("System"));
        final List<TypeNode> typeArguments = new ArrayList<>();
        typeArguments.add(Mockito.mock(TypeNode.class));
        final MethodReferenceExpressionNode methodRef = new QueenMethodReferenceExpressionNode(
            position,
            type,
            null,
            typeArguments,
            "println"
        );
        final MethodReferenceExpr javaMethodRef = (MethodReferenceExpr) methodRef.toJavaExpression();
        Mockito.verify(type, Mockito.times(1)).toType();
        typeArguments.forEach(ta -> Mockito.verify(ta, Mockito.times(1)).addToJavaNode(
            Mockito.any(MethodReferenceExpr.class)
        ));
        MatcherAssert.assertThat(
            javaMethodRef.getIdentifier(),
            Matchers.equalTo("println")
        );
    }

    @Test
    public void returnsJavaScopeExpression() {
        final Position position = Mockito.mock(Position.class);
        final ExpressionNode scope = Mockito.mock(ExpressionNode.class);
        Mockito.when(scope.toJavaExpression()).thenReturn(new NameExpr("Scope"));
        final List<TypeNode> typeArguments = new ArrayList<>();
        typeArguments.add(Mockito.mock(TypeNode.class));
        final MethodReferenceExpressionNode methodRef = new QueenMethodReferenceExpressionNode(
            position,
            null,
            scope,
            typeArguments,
            "println"
        );
        final MethodReferenceExpr javaMethodRef = (MethodReferenceExpr) methodRef.toJavaExpression();
        Mockito.verify(scope, Mockito.times(1)).toJavaExpression();
        typeArguments.forEach(ta -> Mockito.verify(ta, Mockito.times(1)).addToJavaNode(
            Mockito.any(MethodReferenceExpr.class)
        ));
        MatcherAssert.assertThat(
            javaMethodRef.getIdentifier(),
            Matchers.equalTo("println")
        );
    }

    @Test
    public void returnsChildren() {
        final Position position = Mockito.mock(Position.class);
        final ExpressionNode scope = Mockito.mock(ExpressionNode.class);
        final TypeNode type = Mockito.mock(TypeNode.class);
        final List<TypeNode> typeArguments = new ArrayList<>();
        typeArguments.add(Mockito.mock(TypeNode.class));
        final MethodReferenceExpressionNode methodRef = new QueenMethodReferenceExpressionNode(
            position,
            type,
            scope,
            typeArguments,
            "println"
        );

        final List<QueenNode> children = methodRef.children();
        MatcherAssert.assertThat(
            children.size(),
            Matchers.is(3)
        );
        MatcherAssert.assertThat(
            children.containsAll(
                List.of(
                    scope,
                    type,
                    typeArguments.get(0)
                )
            ),
            Matchers.is(true)
        );
    }
}
