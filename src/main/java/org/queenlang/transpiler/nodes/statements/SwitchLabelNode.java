package org.queenlang.transpiler.nodes.statements;

import org.queenlang.transpiler.nodes.QueenNode;
import org.queenlang.transpiler.nodes.expressions.ExpressionNode;

/**
 * A Queen label in a switch case, AST Node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public interface SwitchLabelNode extends QueenNode {

    ExpressionNode expressionNode();
    boolean isDefaultLabel();
}
