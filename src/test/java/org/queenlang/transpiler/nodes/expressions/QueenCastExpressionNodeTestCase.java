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

import com.github.javaparser.ast.expr.CastExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.PrimitiveType;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.types.ReferenceTypeNode;
import org.queenlang.transpiler.nodes.types.TypeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for {@link QueenCastExpressionNode}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenCastExpressionNodeTestCase {

    @Test
    public void returnsPosition() {
        final Position position = Mockito.mock(Position.class);
        final CastExpressionNode cast = new QueenCastExpressionNode(
            position,
            Mockito.mock(TypeNode.class),
            new ArrayList<>(),
            Mockito.mock(ExpressionNode.class)
        );
        MatcherAssert.assertThat(
            cast.position(),
            Matchers.is(position)
        );
    }

    @Test
    public void returnsPrimitiveType() {
        final Position position = Mockito.mock(Position.class);
        final TypeNode primitiveType = Mockito.mock(TypeNode.class);
        final CastExpressionNode cast = new QueenCastExpressionNode(
            position,
            primitiveType,
            new ArrayList<>(),
            Mockito.mock(ExpressionNode.class)
        );
        MatcherAssert.assertThat(
            cast.primitiveType(),
            Matchers.is(primitiveType)
        );
    }

    @Test
    public void returnReferenceTypes() {
        final Position position = Mockito.mock(Position.class);
        final TypeNode primitiveType = Mockito.mock(TypeNode.class);
        final List<ReferenceTypeNode> refTypes = new ArrayList<>();
        refTypes.add(Mockito.mock(ReferenceTypeNode.class));
        final CastExpressionNode cast = new QueenCastExpressionNode(
            position,
            primitiveType,
            refTypes,
            Mockito.mock(ExpressionNode.class)
        );
        MatcherAssert.assertThat(
            cast.referenceTypes(),
            Matchers.is(refTypes)
        );
    }

    @Test
    public void returnExpression() {
        final Position position = Mockito.mock(Position.class);
        final TypeNode primitiveType = Mockito.mock(TypeNode.class);
        final List<ReferenceTypeNode> refTypes = new ArrayList<>();
        refTypes.add(Mockito.mock(ReferenceTypeNode.class));
        final ExpressionNode expression = Mockito.mock(ExpressionNode.class);
        final CastExpressionNode cast = new QueenCastExpressionNode(
            position,
            primitiveType,
            refTypes,
            expression
        );
        MatcherAssert.assertThat(
            cast.expression(),
            Matchers.is(expression)
        );
    }

    @Test
    public void returnsJavaExpressionPrimitiveType() {
        final Position position = Mockito.mock(Position.class);
        final TypeNode primitiveType = Mockito.mock(TypeNode.class);
        Mockito.when(primitiveType.toType()).thenReturn(PrimitiveType.intType());
        final ExpressionNode expression = Mockito.mock(ExpressionNode.class);
        Mockito.when(expression.toJavaExpression()).thenReturn(
            new NameExpr("i")
        );
        final CastExpressionNode cast = new QueenCastExpressionNode(
            position,
            primitiveType,
            null,
            expression
        );
        final CastExpr castExpr = (CastExpr) cast.toJavaExpression();
        System.out.println(castExpr);
        MatcherAssert.assertThat(
            castExpr.getType().asPrimitiveType().getType().asString(),
            Matchers.is(PrimitiveType.intType().asString())
        );
        MatcherAssert.assertThat(
            castExpr.getExpression().asNameExpr().getName().asString(),
            Matchers.equalTo("i")
        );
    }

    @Test
    public void returnsJavaExpressionRefTypes() {
        final Position position = Mockito.mock(Position.class);

        final List<ReferenceTypeNode> refTypes = new ArrayList<>();
        final ReferenceTypeNode refType1 = Mockito.mock(ReferenceTypeNode.class);
        Mockito.when(refType1.toType()).thenReturn(new ClassOrInterfaceType("Student"));
        final ReferenceTypeNode refType2 = Mockito.mock(ReferenceTypeNode.class);
        Mockito.when(refType2.toType()).thenReturn(new ClassOrInterfaceType("Pupil"));
        refTypes.add(refType1);
        refTypes.add(refType2);
        final ExpressionNode expression = Mockito.mock(ExpressionNode.class);
        Mockito.when(expression.toJavaExpression()).thenReturn(
            new NameExpr("student")
        );
        final CastExpressionNode cast = new QueenCastExpressionNode(
            position,
            null,
            refTypes,
            expression
        );
        final CastExpr castExpr = (CastExpr) cast.toJavaExpression();
        System.out.println(castExpr);
        MatcherAssert.assertThat(
            castExpr.getType().asIntersectionType().getElements().get(0).asClassOrInterfaceType().getName().asString(),
            Matchers.equalTo("Student")
        );
        MatcherAssert.assertThat(
            castExpr.getType().asIntersectionType().getElements().get(1).asClassOrInterfaceType().getName().asString(),
            Matchers.equalTo("Pupil")
        );
        MatcherAssert.assertThat(
            castExpr.getExpression().asNameExpr().getName().asString(),
            Matchers.equalTo("student")
        );
    }

    @Test
    public void returnsJavaExpressionSingleRefType() {
        final Position position = Mockito.mock(Position.class);

        final List<ReferenceTypeNode> refTypes = new ArrayList<>();
        final ReferenceTypeNode refType1 = Mockito.mock(ReferenceTypeNode.class);
        Mockito.when(refType1.toType()).thenReturn(new ClassOrInterfaceType("Student"));
        refTypes.add(refType1);
        final ExpressionNode expression = Mockito.mock(ExpressionNode.class);
        Mockito.when(expression.toJavaExpression()).thenReturn(
            new NameExpr("student")
        );
        final CastExpressionNode cast = new QueenCastExpressionNode(
            position,
            null,
            refTypes,
            expression
        );
        final CastExpr castExpr = (CastExpr) cast.toJavaExpression();
        System.out.println(castExpr);
        MatcherAssert.assertThat(
            castExpr.getType().asIntersectionType().getElements().get(0).asClassOrInterfaceType().getName().asString(),
            Matchers.equalTo("Student")
        );
        MatcherAssert.assertThat(
            castExpr.getType().asIntersectionType().getElements().size(),
            Matchers.equalTo(1)
        );
        MatcherAssert.assertThat(
            castExpr.getExpression().asNameExpr().getName().asString(),
            Matchers.equalTo("student")
        );
    }
}
