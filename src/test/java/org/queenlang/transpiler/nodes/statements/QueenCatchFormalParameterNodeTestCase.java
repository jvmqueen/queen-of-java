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

import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.type.UnionType;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.QueenNode;
import org.queenlang.transpiler.nodes.body.ModifierNode;
import org.queenlang.transpiler.nodes.body.VariableDeclaratorId;
import org.queenlang.transpiler.nodes.expressions.AnnotationNode;
import org.queenlang.transpiler.nodes.types.TypeNode;
import org.queenlang.transpiler.util.QueenMockito;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for {@link QueenCatchFormalParameterNode}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenCatchFormalParameterNodeTestCase {

    @Test
    public void returnsPosition() {
        final Position position = QueenMockito.mock(Position.class);
        final CatchFormalParameterNode catchFormalParameterNode = new QueenCatchFormalParameterNode(
            position,
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>(),
            QueenMockito.mock(VariableDeclaratorId.class)
        );
        MatcherAssert.assertThat(
            catchFormalParameterNode.position(),
            Matchers.is(position)
        );
    }

    @Test
    public void returnsAnnotations() {
        final Position position = QueenMockito.mock(Position.class);
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(QueenMockito.mock(AnnotationNode.class));
        final CatchFormalParameterNode catchFormalParameterNode = new QueenCatchFormalParameterNode(
            position,
            annotations,
            new ArrayList<>(),
            new ArrayList<>(),
            QueenMockito.mock(VariableDeclaratorId.class)
        );
        MatcherAssert.assertThat(
            catchFormalParameterNode.annotations(),
            Matchers.is(annotations)
        );
    }

    @Test
    public void returnsModifiers() {
        final Position position = QueenMockito.mock(Position.class);
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(QueenMockito.mock(AnnotationNode.class));
        final List<ModifierNode> modifiers = new ArrayList<>();
        modifiers.add(QueenMockito.mock(ModifierNode.class));
        final CatchFormalParameterNode catchFormalParameterNode = new QueenCatchFormalParameterNode(
            position,
            annotations,
            modifiers,
            new ArrayList<>(),
            QueenMockito.mock(VariableDeclaratorId.class)
        );
        MatcherAssert.assertThat(
            catchFormalParameterNode.modifiers(),
            Matchers.is(modifiers)
        );
    }

    @Test
    public void returnsCatchExceptionTypes() {
        final Position position = QueenMockito.mock(Position.class);
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(QueenMockito.mock(AnnotationNode.class));
        final List<ModifierNode> modifiers = new ArrayList<>();
        modifiers.add(QueenMockito.mock(ModifierNode.class));
        final List<TypeNode> caught = new ArrayList<>();
        caught.add(QueenMockito.mock(TypeNode.class));
        final CatchFormalParameterNode catchFormalParameterNode = new QueenCatchFormalParameterNode(
            position,
            annotations,
            modifiers,
            caught,
            QueenMockito.mock(VariableDeclaratorId.class)
        );
        MatcherAssert.assertThat(
            catchFormalParameterNode.catchExceptionTypes(),
            Matchers.is(caught)
        );
    }

    @Test
    public void returnsExceptionName() {
        final Position position = QueenMockito.mock(Position.class);
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(QueenMockito.mock(AnnotationNode.class));
        final List<ModifierNode> modifiers = new ArrayList<>();
        modifiers.add(QueenMockito.mock(ModifierNode.class));
        final List<TypeNode> caught = new ArrayList<>();
        caught.add(QueenMockito.mock(TypeNode.class));
        final VariableDeclaratorId variableDeclaratorId = QueenMockito.mock(VariableDeclaratorId.class);
        final CatchFormalParameterNode catchFormalParameterNode = new QueenCatchFormalParameterNode(
            position,
            annotations,
            modifiers,
            caught,
            variableDeclaratorId
        );
        MatcherAssert.assertThat(
            catchFormalParameterNode.exceptionName(),
            Matchers.is(variableDeclaratorId)
        );
    }

    @Test
    public void addsToJavaNode() {
        final Position position = QueenMockito.mock(Position.class);
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(QueenMockito.mock(AnnotationNode.class));
        final List<ModifierNode> modifiers = new ArrayList<>();
        modifiers.add(QueenMockito.mock(ModifierNode.class));
        final List<TypeNode> caught = new ArrayList<>();
        caught.add(QueenMockito.mock(TypeNode.class));
        final VariableDeclaratorId variableDeclaratorId = QueenMockito.mock(VariableDeclaratorId.class);
        final CatchFormalParameterNode catchFormalParameterNode = new QueenCatchFormalParameterNode(
            position,
            annotations,
            modifiers,
            caught,
            variableDeclaratorId
        );

        final CatchClause javaCatchClause = new CatchClause();
        catchFormalParameterNode.addToJavaNode(javaCatchClause);

        annotations.forEach(
            a -> Mockito.verify(a, Mockito.times(1)).addToJavaNode(
                Mockito.any(Parameter.class)
            )
        );
        modifiers.forEach(
            m -> Mockito.verify(m, Mockito.times(1)).addToJavaNode(
                Mockito.any(Parameter.class)
            )
        );
        Mockito.verify(variableDeclaratorId, Mockito.times(1)).addToJavaNode(
            Mockito.any(Parameter.class)
        );
        caught.forEach(
            c -> Mockito.verify(c, Mockito.times(1)).addToJavaNode(
                Mockito.any(UnionType.class)
            )
        );
        MatcherAssert.assertThat(
            javaCatchClause.getParameter(),
            Matchers.notNullValue()
        );
    }

    @Test
    public void returnsChildren() {
        final Position position = QueenMockito.mock(Position.class);
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(QueenMockito.mock(AnnotationNode.class));
        final List<ModifierNode> modifiers = new ArrayList<>();
        modifiers.add(QueenMockito.mock(ModifierNode.class));
        final List<TypeNode> caught = new ArrayList<>();
        caught.add(QueenMockito.mock(TypeNode.class));
        final VariableDeclaratorId variableDeclaratorId = QueenMockito.mock(VariableDeclaratorId.class);
        final CatchFormalParameterNode catchFormalParameterNode = new QueenCatchFormalParameterNode(
            position,
            annotations,
            modifiers,
            caught,
            variableDeclaratorId
        );

        final List<QueenNode> children = catchFormalParameterNode.children();
        MatcherAssert.assertThat(
            children.size(),
            Matchers.is(4)
        );
        MatcherAssert.assertThat(
            children.containsAll(
                List.of(
                    annotations.get(0),
                    modifiers.get(0),
                    caught.get(0),
                    variableDeclaratorId
                )
            ),
            Matchers.is(true)
        );
    }
}
