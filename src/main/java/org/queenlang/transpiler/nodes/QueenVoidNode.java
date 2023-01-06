package org.queenlang.transpiler.nodes;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;

/**
 * Queen Void AST node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenVoidNode implements QueenTypeNode {
    private final Position position;

    public QueenVoidNode(final Position position) {
        this.position = position;
    }

    @Override
    public void addToJavaNode(final Node java) {
        if(java instanceof MethodDeclaration) {
            ((MethodDeclaration) java).setType("void");
        }
    }

    @Override
    public Position position() {
        return this.position;
    }
}
