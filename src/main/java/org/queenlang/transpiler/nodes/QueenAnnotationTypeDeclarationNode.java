package org.queenlang.transpiler.nodes;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.AnnotationDeclaration;

import java.util.List;

/**
 * Queen AnnotationDeclaration AST node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 * @todo #10:30min Don't forget to unit test this class.
 */
public final class QueenAnnotationTypeDeclarationNode extends QueenInterfaceDeclarationNode {
    /**
     * Name of this type.
     */
    private final String name;

    /**
     * Ctor.
     * @param annotations Annotation nodes on top of this type.
     * @param name Name.
     */
    public QueenAnnotationTypeDeclarationNode(
        final List<QueenAnnotationNode> annotations,
        final String name
    ) {
        super(annotations);
        this.name = name;
    }

    @Override
    public void addToJavaNode(final Node java) {
        final AnnotationDeclaration annotationDeclaration = ((CompilationUnit) java).addAnnotationDeclaration(this.name);
        super.addToJavaNode(annotationDeclaration);
    }
}
