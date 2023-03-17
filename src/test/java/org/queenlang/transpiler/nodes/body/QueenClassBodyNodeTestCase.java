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
 * Unit tests for {@link QueenClassBodyNode}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenClassBodyNodeTestCase {

    @Test
    public void returnsPosition() {
        final Position position = Mockito.mock(Position.class);
        final ClassBodyNode classBody = new QueenClassBodyNode(
            position,
            new ArrayList<>()
        );
        MatcherAssert.assertThat(
            classBody.position(),
            Matchers.is(position)
        );
    }

    @Test
    public void returnsClassBodyDeclarations() {
        final List<ClassBodyDeclarationNode> classBodyDeclarations = new ArrayList<>();
        classBodyDeclarations.add(Mockito.mock(ClassBodyDeclarationNode.class));
        final ClassBodyNode classBody = new QueenClassBodyNode(
            Mockito.mock(Position.class),
            classBodyDeclarations
        );
        MatcherAssert.assertThat(
            classBody.classBodyDeclarations(),
            Matchers.is(classBodyDeclarations)
        );
    }

    @Test
    public void addsToJavaNode() {
        final List<ClassBodyDeclarationNode> classBodyDeclarations = new ArrayList<>();
        classBodyDeclarations.add(Mockito.mock(ClassBodyDeclarationNode.class));
        classBodyDeclarations.add(Mockito.mock(ClassBodyDeclarationNode.class));
        classBodyDeclarations.add(Mockito.mock(ClassBodyDeclarationNode.class));
        final ClassBodyNode classBody = new QueenClassBodyNode(
            Mockito.mock(Position.class),
            classBodyDeclarations
        );
        final Node node = Mockito.mock(Node.class);
        classBody.addToJavaNode(node);
        classBodyDeclarations.forEach(
            cbd -> Mockito.verify(cbd, Mockito.times(1)).addToJavaNode(node)
        );
    }

}
