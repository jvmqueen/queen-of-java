package org.queenlang.transpiler.nodes.expressions;

import org.queenlang.transpiler.QueenASTVisitor;

import java.util.List;

/**
 * Queen array initialized expression, AST Node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public interface ArrayInitializerExpressionNode extends ExpressionNode {

    List<ExpressionNode> values();

    default <T> T accept(QueenASTVisitor<? extends T> visitor) {
        return visitor.visitArrayInitializerExpressionNode(this);
    }
}
