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

import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.QueenNameNode;
import org.queenlang.transpiler.nodes.body.ParameterNode;
import org.queenlang.transpiler.nodes.body.QueenParameterNode;
import org.queenlang.transpiler.nodes.body.QueenVariableDeclaratorId;
import org.queenlang.transpiler.nodes.statements.BlockStatements;
import org.queenlang.transpiler.nodes.statements.QueenBlockStatements;
import org.queenlang.transpiler.nodes.statements.QueenReturnStatementNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Unit tests for {@link QueenLambdaExpressionNode}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenLambdaExpressionNodeTestCase {

    @Test
    public void returnsPosition() {
        final Position position = Mockito.mock(Position.class);
        final LambdaExpressionNode lambda = new QueenLambdaExpressionNode(
            position,
            true,
            new ArrayList<>(),
            Mockito.mock(ExpressionNode.class),
            Mockito.mock(BlockStatements.class)
        );
        MatcherAssert.assertThat(
            lambda.position(),
            Matchers.is(position)
        );
    }

    @Test
    public void returnsIsEnclosedParameters() {
        final Position position = Mockito.mock(Position.class);
        final LambdaExpressionNode lambda = new QueenLambdaExpressionNode(
            position,
            true,
            new ArrayList<>(),
            Mockito.mock(ExpressionNode.class),
            Mockito.mock(BlockStatements.class)
        );
        MatcherAssert.assertThat(
            lambda.enclosedParameters(),
            Matchers.is(true)
        );
    }

    @Test
    public void returnsParameters() {
        final Position position = Mockito.mock(Position.class);
        final List<ParameterNode> parameters = new ArrayList<>();
        parameters.add(Mockito.mock(ParameterNode.class));
        final LambdaExpressionNode lambda = new QueenLambdaExpressionNode(
            position,
            true,
            parameters,
            Mockito.mock(ExpressionNode.class),
            Mockito.mock(BlockStatements.class)
        );
        MatcherAssert.assertThat(
            lambda.parameters(),
            Matchers.is(parameters)
        );
    }

    @Test
    public void returnsExpressionNode() {
        final Position position = Mockito.mock(Position.class);
        final List<ParameterNode> parameters = new ArrayList<>();
        parameters.add(Mockito.mock(ParameterNode.class));
        final ExpressionNode expressionNode = Mockito.mock(ExpressionNode.class);
        final LambdaExpressionNode lambda = new QueenLambdaExpressionNode(
            position,
            true,
            parameters,
            expressionNode,
            Mockito.mock(BlockStatements.class)
        );
        MatcherAssert.assertThat(
            lambda.expression(),
            Matchers.is(expressionNode)
        );
    }

    @Test
    public void returnsBlockStatements() {
        final Position position = Mockito.mock(Position.class);
        final List<ParameterNode> parameters = new ArrayList<>();
        parameters.add(Mockito.mock(ParameterNode.class));
        final ExpressionNode expressionNode = Mockito.mock(ExpressionNode.class);
        final BlockStatements blockStatements = Mockito.mock(BlockStatements.class);
        final LambdaExpressionNode lambda = new QueenLambdaExpressionNode(
            position,
            true,
            parameters,
            expressionNode,
            blockStatements
        );
        MatcherAssert.assertThat(
            lambda.blockStatements(),
            Matchers.is(blockStatements)
        );
    }

    @Test
    public void returnsSimpleJavaLambdaNoParams() {
        final Position position = Mockito.mock(Position.class);
        final List<ParameterNode> parameters = new ArrayList<>();
        final ExpressionNode expressionNode = Mockito.mock(ExpressionNode.class);
        Mockito.when(expressionNode.toJavaExpression()).thenReturn(new StringLiteralExpr("test"));
        final LambdaExpressionNode lambda = new QueenLambdaExpressionNode(
            position,
            false,
            parameters,
            expressionNode,
            null
        );
        final LambdaExpr lambdaExpr = (LambdaExpr) lambda.toJavaExpression();
        MatcherAssert.assertThat(
            lambdaExpr.toString(),
            Matchers.equalTo("() -> \"test\"")
        );
    }

    @Test
    public void returnsSimpleJavaLambdaOneParamEnclosed() {
        final Position position = Mockito.mock(Position.class);
        final List<ParameterNode> parameters = new ArrayList<>();
        final ParameterNode param = new QueenParameterNode(
            Mockito.mock(Position.class),
            new QueenVariableDeclaratorId(Mockito.mock(Position.class), "a")
        );
        parameters.add(param);
        final ExpressionNode expressionNode = Mockito.mock(ExpressionNode.class);
        Mockito.when(expressionNode.toJavaExpression()).thenReturn(new StringLiteralExpr("test"));
        final LambdaExpressionNode lambda = new QueenLambdaExpressionNode(
            position,
            true,
            parameters,
            expressionNode,
            null
        );
        final LambdaExpr lambdaExpr = (LambdaExpr) lambda.toJavaExpression();
        MatcherAssert.assertThat(
            lambdaExpr.toString(),
            Matchers.equalTo("(a) -> \"test\"")
        );
    }

    @Test
    public void returnsSimpleJavaLambdaOneParamNotEnclosed() {
        final Position position = Mockito.mock(Position.class);
        final List<ParameterNode> parameters = new ArrayList<>();
        final ParameterNode param = new QueenParameterNode(
            Mockito.mock(Position.class),
            new QueenVariableDeclaratorId(Mockito.mock(Position.class), "a")
        );
        parameters.add(param);
        final ExpressionNode expressionNode = Mockito.mock(ExpressionNode.class);
        Mockito.when(expressionNode.toJavaExpression()).thenReturn(new StringLiteralExpr("test"));
        final LambdaExpressionNode lambda = new QueenLambdaExpressionNode(
            position,
            false,
            parameters,
            expressionNode,
            null
        );
        final LambdaExpr lambdaExpr = (LambdaExpr) lambda.toJavaExpression();
        MatcherAssert.assertThat(
            lambdaExpr.toString(),
            Matchers.equalTo("a -> \"test\"")
        );
    }

    @Test
    public void returnsSimpleJavaLambdaMoreParamsEnclosed() {
        final Position position = Mockito.mock(Position.class);
        final List<ParameterNode> parameters = new ArrayList<>();
        final ParameterNode param1 = new QueenParameterNode(
            Mockito.mock(Position.class),
            new QueenVariableDeclaratorId(Mockito.mock(Position.class), "a")
        );
        final ParameterNode param2 = new QueenParameterNode(
            Mockito.mock(Position.class),
            new QueenVariableDeclaratorId(Mockito.mock(Position.class), "b")
        );
        parameters.add(param1);
        parameters.add(param2);
        final ExpressionNode expressionNode = Mockito.mock(ExpressionNode.class);
        Mockito.when(expressionNode.toJavaExpression()).thenReturn(new StringLiteralExpr("test"));
        final LambdaExpressionNode lambda = new QueenLambdaExpressionNode(
            position,
            true,
            parameters,
            expressionNode,
            null
        );
        final LambdaExpr lambdaExpr = (LambdaExpr) lambda.toJavaExpression();
        MatcherAssert.assertThat(
            lambdaExpr.toString(),
            Matchers.equalTo("(a, b) -> \"test\"")
        );
    }

    @Test
    public void returnsBlockJavaLambdaOneParamEnclosed() {
        final Position position = Mockito.mock(Position.class);
        final List<ParameterNode> parameters = new ArrayList<>();
        final ParameterNode param = new QueenParameterNode(
            Mockito.mock(Position.class),
            new QueenVariableDeclaratorId(Mockito.mock(Position.class), "a")
        );
        parameters.add(param);
        final BlockStatements block = new QueenBlockStatements(
            Mockito.mock(Position.class),
            Arrays.asList(
                new QueenReturnStatementNode(
                    Mockito.mock(Position.class),
                    new QueenNameNode(Mockito.mock(Position.class), "test")
                )
            )
        );
        final LambdaExpressionNode lambda = new QueenLambdaExpressionNode(
            position,
            true,
            parameters,
            null,
            block
        );
        final LambdaExpr lambdaExpr = (LambdaExpr) lambda.toJavaExpression();
        MatcherAssert.assertThat(
            lambdaExpr.toString(),
            Matchers.equalTo("(a) -> {\n    return test;\n}")
        );
    }

    @Test
    public void returnsBlockJavaLambdaOneParamNotEnclosed() {
        final Position position = Mockito.mock(Position.class);
        final List<ParameterNode> parameters = new ArrayList<>();
        final ParameterNode param = new QueenParameterNode(
            Mockito.mock(Position.class),
            new QueenVariableDeclaratorId(Mockito.mock(Position.class), "a")
        );
        parameters.add(param);
        final BlockStatements block = new QueenBlockStatements(
            Mockito.mock(Position.class),
            Arrays.asList(
                new QueenReturnStatementNode(
                    Mockito.mock(Position.class),
                    new QueenNameNode(Mockito.mock(Position.class), "test")
                )
            )
        );
        final LambdaExpressionNode lambda = new QueenLambdaExpressionNode(
            position,
            false,
            parameters,
            null,
            block
        );
        final LambdaExpr lambdaExpr = (LambdaExpr) lambda.toJavaExpression();
        MatcherAssert.assertThat(
            lambdaExpr.toString(),
            Matchers.equalTo("a -> {\n    return test;\n}")
        );
    }

    @Test
    public void returnsBlockJavaLambdaMoreParamsEnclosed() {
        final Position position = Mockito.mock(Position.class);
        final List<ParameterNode> parameters = new ArrayList<>();
        final ParameterNode param1 = new QueenParameterNode(
            Mockito.mock(Position.class),
            new QueenVariableDeclaratorId(Mockito.mock(Position.class), "a")
        );
        final ParameterNode param2 = new QueenParameterNode(
            Mockito.mock(Position.class),
            new QueenVariableDeclaratorId(Mockito.mock(Position.class), "b")
        );
        parameters.add(param1);
        parameters.add(param2);
        final BlockStatements block = new QueenBlockStatements(
            Mockito.mock(Position.class),
            Arrays.asList(
                new QueenReturnStatementNode(
                    Mockito.mock(Position.class),
                    new QueenNameNode(Mockito.mock(Position.class), "test")
                )
            )
        );
        final LambdaExpressionNode lambda = new QueenLambdaExpressionNode(
            position,
            true,
            parameters,
            null,
            block
        );
        final LambdaExpr lambdaExpr = (LambdaExpr) lambda.toJavaExpression();
        MatcherAssert.assertThat(
            lambdaExpr.toString(),
            Matchers.equalTo("(a, b) -> {\n    return test;\n}")
        );
    }
}
