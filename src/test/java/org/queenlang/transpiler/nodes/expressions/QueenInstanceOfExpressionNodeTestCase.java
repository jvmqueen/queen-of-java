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

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.types.ReferenceTypeNode;

/**
 * Unit tests for {@link QueenInstanceOfExpressionNode}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenInstanceOfExpressionNodeTestCase {

    @Test
    public void returnsPosition() {
        final Position position = Mockito.mock(Position.class);
        final InstanceOfExpressionNode instanceOfExpression = new QueenInstanceOfExpressionNode(
            position,
            Mockito.mock(ExpressionNode.class),
            Mockito.mock(ReferenceTypeNode.class)
        );
        MatcherAssert.assertThat(
            instanceOfExpression.position(),
            Matchers.is(position)
        );
    }

    @Test
    public void returnsExpression() {
        final Position position = Mockito.mock(Position.class);
        final ExpressionNode expressionNode = Mockito.mock(ExpressionNode.class);
        final InstanceOfExpressionNode instanceOfExpression = new QueenInstanceOfExpressionNode(
            position,
            expressionNode,
            Mockito.mock(ReferenceTypeNode.class)
        );
        MatcherAssert.assertThat(
            instanceOfExpression.expression(),
            Matchers.is(expressionNode)
        );
    }

    @Test
    public void returnsReferenceType() {
        final Position position = Mockito.mock(Position.class);
        final ExpressionNode expressionNode = Mockito.mock(ExpressionNode.class);
        final ReferenceTypeNode refType = Mockito.mock(ReferenceTypeNode.class);
        final InstanceOfExpressionNode instanceOfExpression = new QueenInstanceOfExpressionNode(
            position,
            expressionNode,
            refType
        );
        MatcherAssert.assertThat(
            instanceOfExpression.referenceType(),
            Matchers.is(refType)
        );
    }

    @Test
    public void returnsJavaNode() {
        final Position position = Mockito.mock(Position.class);
        final ExpressionNode expressionNode = Mockito.mock(ExpressionNode.class);
        Mockito.when(expressionNode.toJavaExpression()).thenReturn(new NameExpr("s"));
        final ReferenceTypeNode refType = Mockito.mock(ReferenceTypeNode.class);
        Mockito.when(refType.toType()).thenReturn(new ClassOrInterfaceType("Student"));
        final InstanceOfExpressionNode instanceOfExpression = new QueenInstanceOfExpressionNode(
            position,
            expressionNode,
            refType
        );

        final Expression javaExpr = instanceOfExpression.toJavaExpression();
        MatcherAssert.assertThat(
            javaExpr.toString(),
            Matchers.equalTo("s instanceof Student")
        );
        MatcherAssert.assertThat(
            javaExpr.asInstanceOfExpr().getExpression().asNameExpr().getName().asString(),
            Matchers.equalTo("s")
        );
        MatcherAssert.assertThat(
            javaExpr.asInstanceOfExpr().getTypeAsString(),
            Matchers.equalTo("Student")
        );
    }
}
