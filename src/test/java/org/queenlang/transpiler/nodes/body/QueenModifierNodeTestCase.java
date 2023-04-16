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

import com.github.javaparser.ast.body.Parameter;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.util.QueenMockito;

/**
 * Unit tests for {@link QueenModifierNode}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenModifierNodeTestCase {

    @Test
    public void returnsPosition() {
        final Position position = QueenMockito.mock(Position.class);
        final ModifierNode modifierNode = new QueenModifierNode(
            position,
            "public"
        );
        MatcherAssert.assertThat(
            modifierNode.position(),
            Matchers.is(position)
        );
    }

    @Test
    public void returnsModifier() {
        final ModifierNode modifierNode = new QueenModifierNode(
            QueenMockito.mock(Position.class),
            "public"
        );
        MatcherAssert.assertThat(
            modifierNode.modifier(),
            Matchers.equalTo("public")
        );
    }

    @Test
    public void addsToJavaNodeIfNotMutable() {
        final ModifierNode modifierNode = new QueenModifierNode(
            QueenMockito.mock(Position.class),
            "private"
        );
        final Parameter parameter = new Parameter();
        modifierNode.addToJavaNode(parameter);
        MatcherAssert.assertThat(
            parameter.getModifiers().get(0).getKeyword().asString(),
            Matchers.equalTo("private")
        );
    }

    @Test
    public void doesNotAddToJavaNodeIfMutable() {
        final ModifierNode modifierNode = new QueenModifierNode(
            QueenMockito.mock(Position.class),
            "mutable"
        );
        final Parameter parameter = new Parameter();
        modifierNode.addToJavaNode(parameter);
        MatcherAssert.assertThat(
            parameter.getModifiers().size(),
            Matchers.equalTo(0)
        );
    }

    @Test
    public void equalsTest() {
        final ModifierNode modifierNode = new QueenModifierNode(
            QueenMockito.mock(Position.class),
            "mutable"
        );
        MatcherAssert.assertThat(
            modifierNode,
            Matchers.not(
                Matchers.equalTo(
                    new QueenModifierNode(
                        QueenMockito.mock(Position.class),
                        "private"
                    )
                )
            )
        );
        MatcherAssert.assertThat(
            modifierNode,
            Matchers.equalTo(
                new QueenModifierNode(
                    QueenMockito.mock(Position.class),
                    "mutable"
                )
            )
        );
    }

    @Test
    public void returnsChildren() {
        MatcherAssert.assertThat(
            new QueenModifierNode(
                QueenMockito.mock(Position.class),
                "public"
            ).children(),
            Matchers.hasSize(0)
        );
    }

}
