package org.queenlang.transpiler.nodes;

import java.util.List;

/**
 * Queen InterfaceDeclaration AST node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public abstract class QueenInterfaceDeclarationNode extends QueenTypeDeclarationNode {
    public QueenInterfaceDeclarationNode(List<QueenAnnotationNode> annotations) {
        super(annotations);
    }
}
