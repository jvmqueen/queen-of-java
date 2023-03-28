package org.queenlang.transpiler.nodes.statements;

import org.queenlang.transpiler.QueenASTVisitor;
import org.queenlang.transpiler.nodes.expressions.ExpressionNode;

/**
 * Queen Throw AST Node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public interface ThrowStatementNode extends StatementNode {

    ExpressionNode expression();

    default <T> T accept(QueenASTVisitor<? extends T> visitor) {
        return visitor.visitThrowStatementNode(this);
    }
}
