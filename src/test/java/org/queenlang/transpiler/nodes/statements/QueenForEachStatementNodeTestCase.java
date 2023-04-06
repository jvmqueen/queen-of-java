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
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.LabeledStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.QueenNode;
import org.queenlang.transpiler.nodes.body.LocalVariableDeclarationNode;
import org.queenlang.transpiler.nodes.expressions.ExpressionNode;

import java.util.List;

/**
 * Unit tests for {@link QueenForEachStatementNode}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenForEachStatementNodeTestCase {

    @Test
    public void returnsPosition() {
        final Position position = Mockito.mock(Position.class);
        final ForEachStatementNode forEach = new QueenForEachStatementNode(
            position,
            Mockito.mock(LocalVariableDeclarationNode.class),
            Mockito.mock(ExpressionNode.class),
            Mockito.mock(BlockStatements.class)
        );
        MatcherAssert.assertThat(
            forEach.position(),
            Matchers.is(position)
        );
    }

    @Test
    public void returnsVariable() {
        final Position position = Mockito.mock(Position.class);
        final LocalVariableDeclarationNode variable = Mockito.mock(LocalVariableDeclarationNode.class);
        final ForEachStatementNode forEach = new QueenForEachStatementNode(
            position,
            variable,
            Mockito.mock(ExpressionNode.class),
            Mockito.mock(BlockStatements.class)
        );
        MatcherAssert.assertThat(
            forEach.variable(),
            Matchers.is(variable)
        );
    }

    @Test
    public void returnsIterable() {
        final Position position = Mockito.mock(Position.class);
        final LocalVariableDeclarationNode variable = Mockito.mock(LocalVariableDeclarationNode.class);
        final ExpressionNode iterable = Mockito.mock(ExpressionNode.class);
        final ForEachStatementNode forEach = new QueenForEachStatementNode(
            position,
            variable,
            iterable,
            Mockito.mock(BlockStatements.class)
        );
        MatcherAssert.assertThat(
            forEach.iterable(),
            Matchers.is(iterable)
        );
    }

    @Test
    public void returnsBlock() {
        final Position position = Mockito.mock(Position.class);
        final LocalVariableDeclarationNode variable = Mockito.mock(LocalVariableDeclarationNode.class);
        final ExpressionNode iterable = Mockito.mock(ExpressionNode.class);
        final BlockStatements block = Mockito.mock(BlockStatements.class);
        final ForEachStatementNode forEach = new QueenForEachStatementNode(
            position,
            variable,
            iterable,
            block
        );
        MatcherAssert.assertThat(
            forEach.blockStatements(),
            Matchers.is(block)
        );
    }

    @Test
    public void addsToBlockJavaNode() {
        final Position position = Mockito.mock(Position.class);
        final LocalVariableDeclarationNode variable = Mockito.mock(LocalVariableDeclarationNode.class);
        Mockito.when(variable.toJavaExpression()).thenReturn(
            new VariableDeclarationExpr(new ClassOrInterfaceType("String"), "s")
        );
        final ExpressionNode iterable = Mockito.mock(ExpressionNode.class);
        Mockito.when(iterable.toJavaExpression()).thenReturn(new NameExpr("strings"));
        final BlockStatements block = Mockito.mock(BlockStatements.class);
        final ForEachStatementNode forEach = new QueenForEachStatementNode(
            position,
            variable,
            iterable,
            block
        );

        final BlockStmt javaBlock = new BlockStmt();
        forEach.addToJavaNode(javaBlock);

        MatcherAssert.assertThat(
            javaBlock.getStatement(0).asForEachStmt(),
            Matchers.notNullValue()
        );
        Mockito.verify(variable, Mockito.times(1)).toJavaExpression();
        Mockito.verify(iterable, Mockito.times(1)).toJavaExpression();
        Mockito.verify(block, Mockito.times(1)).addToJavaNode(Mockito.any(BlockStmt.class));
    }

    @Test
    public void addsToLabeledJavaNode() {
        final Position position = Mockito.mock(Position.class);
        final LocalVariableDeclarationNode variable = Mockito.mock(LocalVariableDeclarationNode.class);
        Mockito.when(variable.toJavaExpression()).thenReturn(
            new VariableDeclarationExpr(new ClassOrInterfaceType("String"), "s")
        );
        final ExpressionNode iterable = Mockito.mock(ExpressionNode.class);
        Mockito.when(iterable.toJavaExpression()).thenReturn(new NameExpr("strings"));
        final BlockStatements block = Mockito.mock(BlockStatements.class);
        final ForEachStatementNode forEach = new QueenForEachStatementNode(
            position,
            variable,
            iterable,
            block
        );

        final LabeledStmt javaLabeledStatement = new LabeledStmt();
        forEach.addToJavaNode(javaLabeledStatement);

        MatcherAssert.assertThat(
            javaLabeledStatement.getStatement().asForEachStmt(),
            Matchers.notNullValue()
        );
        Mockito.verify(variable, Mockito.times(1)).toJavaExpression();
        Mockito.verify(iterable, Mockito.times(1)).toJavaExpression();
        Mockito.verify(block, Mockito.times(1)).addToJavaNode(Mockito.any(BlockStmt.class));
    }

    @Test
    public void returnsChildren() {
        final Position position = Mockito.mock(Position.class);
        final LocalVariableDeclarationNode variable = Mockito.mock(LocalVariableDeclarationNode.class);
        final ExpressionNode iterable = Mockito.mock(ExpressionNode.class);
        final BlockStatements block = Mockito.mock(BlockStatements.class);
        final ForEachStatementNode forEach = new QueenForEachStatementNode(
            position,
            variable,
            iterable,
            block
        );

        final List<QueenNode> children = forEach.children();
        MatcherAssert.assertThat(
            children.size(),
            Matchers.is(3)
        );
        MatcherAssert.assertThat(
            children.containsAll(
                List.of(
                    variable,
                    iterable,
                    block
                )
            ),
            Matchers.is(true)
        );

    }
}
