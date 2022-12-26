package org.queenlang.transpiler.nodes;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.nodeTypes.NodeWithModifiers;

/**
 * Queen class extension modifier node (abstract or final).
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenClassExtensionModifierNode extends QueenModifierNode {
    public QueenClassExtensionModifierNode(final Position position, final String modifier) {
        super(position, modifier);
    }

    @Override
    public void addToJavaNode(Node java) {
        ((NodeWithModifiers) java).addModifier(this.modifier());
    }
}
