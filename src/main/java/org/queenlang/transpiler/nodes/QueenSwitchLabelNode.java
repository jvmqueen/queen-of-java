package org.queenlang.transpiler.nodes;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.SwitchEntry;

/**
 * A Queen label in a switch case, AST Node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenSwitchLabelNode implements QueenNode {

    private final Position position;
    private final QueenExpressionNode expressionNode;
    private final boolean isDefaultLabel;

    public QueenSwitchLabelNode(final Position position, final QueenExpressionNode expressionNode, final boolean isDefaultLabel) {
        this.position = position;
        this.expressionNode = expressionNode;
        this.isDefaultLabel = isDefaultLabel;
    }

    @Override
    public void addToJavaNode(final Node java) {
        if(java instanceof SwitchEntry) {
            final SwitchEntry entry = (SwitchEntry) java;
            if(!this.isDefaultLabel && this.expressionNode != null) {
                entry.getLabels().add(
                    this.expressionNode.toJavaExpression()
                );
            }
        }
    }

    @Override
    public Position position() {
        return this.position;
    }
}
