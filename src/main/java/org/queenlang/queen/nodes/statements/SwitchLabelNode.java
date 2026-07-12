package org.queenlang.queen.nodes.statements;

import org.queenlang.queen.visitors.QueenASTVisitor;
import org.queenlang.queen.nodes.QueenNode;
import org.queenlang.queen.nodes.expressions.ExpressionNode;

/**
 * A Queen label in a switch case, AST Node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public interface SwitchLabelNode extends QueenNode {

    ExpressionNode expressionNode();
    boolean isDefaultLabel();

    default <T> T accept(QueenASTVisitor<? extends T> visitor) {
        return visitor.visitSwitchLabelNode(this);
    }
}
