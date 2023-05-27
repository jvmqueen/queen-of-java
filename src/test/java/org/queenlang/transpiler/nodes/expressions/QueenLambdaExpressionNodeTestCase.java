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

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.QueenNode;
import org.queenlang.transpiler.nodes.body.ParameterNode;
import org.queenlang.transpiler.nodes.statements.BlockStatements;
import org.queenlang.transpiler.util.QueenMockito;

import java.util.ArrayList;
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
        final Position position = QueenMockito.mock(Position.class);
        final LambdaExpressionNode lambda = new QueenLambdaExpressionNode(
            position,
            true,
            new ArrayList<>(),
            QueenMockito.mock(ExpressionNode.class),
            QueenMockito.mock(BlockStatements.class)
        );
        MatcherAssert.assertThat(
            lambda.position(),
            Matchers.is(position)
        );
    }

    @Test
    public void returnsIsEnclosedParameters() {
        final Position position = QueenMockito.mock(Position.class);
        final LambdaExpressionNode lambda = new QueenLambdaExpressionNode(
            position,
            true,
            new ArrayList<>(),
            QueenMockito.mock(ExpressionNode.class),
            QueenMockito.mock(BlockStatements.class)
        );
        MatcherAssert.assertThat(
            lambda.enclosedParameters(),
            Matchers.is(true)
        );
    }

    @Test
    public void returnsParameters() {
        final Position position = QueenMockito.mock(Position.class);
        final List<ParameterNode> parameters = new ArrayList<>();
        parameters.add(QueenMockito.mock(ParameterNode.class));
        final LambdaExpressionNode lambda = new QueenLambdaExpressionNode(
            position,
            true,
            parameters,
            QueenMockito.mock(ExpressionNode.class),
            QueenMockito.mock(BlockStatements.class)
        );
        MatcherAssert.assertThat(
            lambda.parameters(),
            Matchers.is(parameters)
        );
    }

    @Test
    public void returnsExpressionNode() {
        final Position position = QueenMockito.mock(Position.class);
        final List<ParameterNode> parameters = new ArrayList<>();
        parameters.add(QueenMockito.mock(ParameterNode.class));
        final ExpressionNode expressionNode = QueenMockito.mock(ExpressionNode.class);
        final LambdaExpressionNode lambda = new QueenLambdaExpressionNode(
            position,
            true,
            parameters,
            expressionNode,
            QueenMockito.mock(BlockStatements.class)
        );
        MatcherAssert.assertThat(
            lambda.expression(),
            Matchers.is(expressionNode)
        );
    }

    @Test
    public void returnsBlockStatements() {
        final Position position = QueenMockito.mock(Position.class);
        final List<ParameterNode> parameters = new ArrayList<>();
        parameters.add(QueenMockito.mock(ParameterNode.class));
        final ExpressionNode expressionNode = QueenMockito.mock(ExpressionNode.class);
        final BlockStatements blockStatements = QueenMockito.mock(BlockStatements.class);
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
    public void returnsChildren() {
        final Position position = QueenMockito.mock(Position.class);
        final List<ParameterNode> parameters = new ArrayList<>();
        parameters.add(QueenMockito.mock(ParameterNode.class));
        parameters.add(QueenMockito.mock(ParameterNode.class));
        final BlockStatements block = QueenMockito.mock(BlockStatements.class);
        final ExpressionNode expression = QueenMockito.mock(ExpressionNode.class);
        final LambdaExpressionNode lambda = new QueenLambdaExpressionNode(
            position,
            true,
            parameters,
            expression,
            block
        );

        final List<QueenNode> children = lambda.children();
        MatcherAssert.assertThat(
            children.size(),
            Matchers.is(4)
        );
        MatcherAssert.assertThat(
            children.containsAll(
                List.of(
                    parameters.get(0),
                    parameters.get(1),
                    block,
                    expression
                )
            ),
            Matchers.is(true)
        );
    }
}
