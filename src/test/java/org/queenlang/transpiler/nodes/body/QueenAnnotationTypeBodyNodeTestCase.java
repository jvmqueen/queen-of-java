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
 * Unit tests for {@link QueenAnnotationTypeBodyNode}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenAnnotationTypeBodyNodeTestCase {

    @Test
    public void returnsPosition() {
        final Position position = Mockito.mock(Position.class);
        final AnnotationTypeBodyNode annotationTypeBodyNode = new QueenAnnotationTypeBodyNode(
            position,
            new ArrayList<>()
        );
        MatcherAssert.assertThat(
            annotationTypeBodyNode.position(),
            Matchers.is(position)
        );
    }

    @Test
    public void returnsAnnotationMemberDeclarations() {
        final List<AnnotationTypeMemberDeclarationNode> annotationMemberDeclarations = new ArrayList<>();
        annotationMemberDeclarations.add(Mockito.mock(AnnotationTypeMemberDeclarationNode.class));
        final AnnotationTypeBodyNode annotationTypeBodyNode = new QueenAnnotationTypeBodyNode(
            Mockito.mock(Position.class),
            annotationMemberDeclarations
        );
        MatcherAssert.assertThat(
            annotationTypeBodyNode.annotationMemberDeclarations(),
            Matchers.is(annotationMemberDeclarations)
        );
    }

    @Test
    public void addsToJavaNode() {
        final List<AnnotationTypeMemberDeclarationNode> annotationMemberDeclarations = new ArrayList<>();
        annotationMemberDeclarations.add(Mockito.mock(AnnotationTypeMemberDeclarationNode.class));
        annotationMemberDeclarations.add(Mockito.mock(AnnotationTypeMemberDeclarationNode.class));
        annotationMemberDeclarations.add(Mockito.mock(AnnotationTypeMemberDeclarationNode.class));

        final AnnotationTypeBodyNode annotationTypeBodyNode = new QueenAnnotationTypeBodyNode(
            Mockito.mock(Position.class),
            annotationMemberDeclarations
        );
        final Node node = Mockito.mock(Node.class);
        annotationTypeBodyNode.addToJavaNode(node);
        annotationMemberDeclarations.forEach(
            amd -> Mockito.verify(amd, Mockito.times(1)).addToJavaNode(node)
        );
    }

}
