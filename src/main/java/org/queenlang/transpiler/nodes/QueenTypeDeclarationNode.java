package org.queenlang.transpiler.nodes;

import com.github.javaparser.ast.Node;
import java.util.List;

/**
 * Queen TypeDeclaration AST node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 * @todo #10:60min Handle access modifiers for the type declaration.
 * @todo #8:60min Handle TypeDeclaration Parameters (for generic types).
 * @todo #8:60min Handle the TypeBody AST node further.
 */
public abstract class QueenTypeDeclarationNode implements QueenNode {

    /**
     * Annotations on top of this type.
     */
    private final List<QueenAnnotationNode> annotations;

    public QueenTypeDeclarationNode(final List<QueenAnnotationNode> annotations) {
        this.annotations = annotations;
    }

    public void addToJavaNode(final Node java) {
        this.annotations.forEach(
            a -> a.addToJavaNode(java)
        );
    }

}
