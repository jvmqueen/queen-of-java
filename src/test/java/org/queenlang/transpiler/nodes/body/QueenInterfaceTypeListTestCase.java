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

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.QueenNode;
import org.queenlang.transpiler.nodes.types.ClassOrInterfaceTypeNode;
import org.queenlang.transpiler.util.QueenMockito;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for {@link QueenInterfaceTypeList}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenInterfaceTypeListTestCase {

    @Test
    public void returnsPosition() {
        final Position position = QueenMockito.mock(Position.class);
        final InterfaceTypeList interfaceTypeList = new QueenInterfaceTypeList(position);
        MatcherAssert.assertThat(
            interfaceTypeList.position(),
            Matchers.is(position)
        );
    }

    @Test
    public void returnsInterfaces() {
        final Position position = QueenMockito.mock(Position.class);
        final List<ClassOrInterfaceTypeNode> interfaces = new ArrayList<>();
        interfaces.add(QueenMockito.mock(ClassOrInterfaceTypeNode.class));
        interfaces.add(QueenMockito.mock(ClassOrInterfaceTypeNode.class));
        final InterfaceTypeList interfaceTypeList = new QueenInterfaceTypeList(position, interfaces);
        MatcherAssert.assertThat(
            interfaceTypeList.interfaceTypes(),
            Matchers.is(interfaces)
        );
    }

    @Test
    public void canBeIterated() {
        final Position position = QueenMockito.mock(Position.class);
        final List<ClassOrInterfaceTypeNode> interfaces = new ArrayList<>();
        interfaces.add(QueenMockito.mock(ClassOrInterfaceTypeNode.class));
        interfaces.add(QueenMockito.mock(ClassOrInterfaceTypeNode.class));
        final InterfaceTypeList interfaceTypeList = new QueenInterfaceTypeList(position, interfaces);
        MatcherAssert.assertThat(
            interfaceTypeList,
            Matchers.iterableWithSize(2)
        );
    }

    @Test
    public void returnsChildren() {
        final Position position = QueenMockito.mock(Position.class);
        final List<ClassOrInterfaceTypeNode> interfaces = new ArrayList<>();
        interfaces.add(QueenMockito.mock(ClassOrInterfaceTypeNode.class));
        interfaces.add(QueenMockito.mock(ClassOrInterfaceTypeNode.class));
        final InterfaceTypeList interfaceTypeList = new QueenInterfaceTypeList(position, interfaces);

        final List<QueenNode> children = interfaceTypeList.children();
        MatcherAssert.assertThat(
            children.size(),
            Matchers.is(2)
        );
        MatcherAssert.assertThat(
            children.containsAll(
                List.of(interfaces.get(0), interfaces.get(1))
            ),
            Matchers.is(true)
        );
    }

}
