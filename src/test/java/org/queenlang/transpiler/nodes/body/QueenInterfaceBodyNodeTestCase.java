package org.queenlang.transpiler.nodes.body;

import com.github.javaparser.ast.Node;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.queenlang.transpiler.nodes.Position;

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

}
