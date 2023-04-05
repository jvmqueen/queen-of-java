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

import com.github.javaparser.ast.Node;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.QueenNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for {@link QueenInterfaceBodyNode}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenInterfaceBodyNodeTestCase {

    @Test
    public void returnsPosition() {
        final Position position = Mockito.mock(Position.class);
        final InterfaceBodyNode interfaceBody = new QueenInterfaceBodyNode(
            position,
            new ArrayList<>()
        );
        MatcherAssert.assertThat(
            interfaceBody.position(),
            Matchers.is(position)
        );
    }

    @Test
    public void returnsInterfaceMemberDeclarations() {
        final List<InterfaceMemberDeclarationNode> interfaceMemberDeclarations = new ArrayList<>();
        interfaceMemberDeclarations.add(Mockito.mock(InterfaceMemberDeclarationNode.class));
        final InterfaceBodyNode interfaceBody = new QueenInterfaceBodyNode(
            Mockito.mock(Position.class),
            interfaceMemberDeclarations
        );
        MatcherAssert.assertThat(
            interfaceBody.interfaceMemberDeclarations(),
            Matchers.is(interfaceMemberDeclarations)
        );
    }

    @Test
    public void addsToJavaNode() {
        final List<InterfaceMemberDeclarationNode> interfaceMemberDeclarations = new ArrayList<>();
        interfaceMemberDeclarations.add(Mockito.mock(InterfaceMemberDeclarationNode.class));
        interfaceMemberDeclarations.add(Mockito.mock(InterfaceMemberDeclarationNode.class));
        interfaceMemberDeclarations.add(Mockito.mock(InterfaceMemberDeclarationNode.class));

        final InterfaceBodyNode interfaceBody = new QueenInterfaceBodyNode(
            Mockito.mock(Position.class),
            interfaceMemberDeclarations
        );
        final Node node = Mockito.mock(Node.class);
        interfaceBody.addToJavaNode(node);
        interfaceMemberDeclarations.forEach(
            imd -> Mockito.verify(imd, Mockito.times(1)).addToJavaNode(node)
        );
    }

    @Test
    public void returnsChildren() {
        final List<InterfaceMemberDeclarationNode> interfaceMemberDeclarations = new ArrayList<>();
        interfaceMemberDeclarations.add(Mockito.mock(InterfaceMemberDeclarationNode.class));
        interfaceMemberDeclarations.add(Mockito.mock(InterfaceMemberDeclarationNode.class));
        interfaceMemberDeclarations.add(Mockito.mock(InterfaceMemberDeclarationNode.class));

        final InterfaceBodyNode interfaceBody = new QueenInterfaceBodyNode(
            Mockito.mock(Position.class),
            interfaceMemberDeclarations
        );

        final List<QueenNode> children = interfaceBody.children();
        MatcherAssert.assertThat(
            children.size(),
            Matchers.is(3)
        );
        MatcherAssert.assertThat(
            children.containsAll(interfaceMemberDeclarations),
            Matchers.is(true)
        );
    }
}
