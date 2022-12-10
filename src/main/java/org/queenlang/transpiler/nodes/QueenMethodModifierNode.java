package org.queenlang.transpiler.nodes;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.nodeTypes.NodeWithModifiers;

/**
 * Queen method modifier AST node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenMethodModifierNode extends QueenModifierNode {
    public QueenMethodModifierNode(final String modifier) {
        super(modifier);
    }

    @Override
    public void addToJavaNode(final Node java) {
        ((NodeWithModifiers) java).addModifier(this.modifier());
    }
}
