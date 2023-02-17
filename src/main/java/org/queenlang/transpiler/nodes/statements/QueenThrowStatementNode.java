package org.queenlang.transpiler.nodes.statements;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ThrowStmt;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.expressions.ExpressionNode;

/**
 * Queen Throw AST Node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenThrowStatementNode implements QueenStatementNode {

    private final Position position;

    private final ExpressionNode expression;

    public QueenThrowStatementNode(
        final Position position,
        final ExpressionNode expression
    ) {
        this.position = position;
        this.expression = expression;
    }

    @Override
    public void addToJavaNode(final Node java) {
        ThrowStmt throwStmt = new ThrowStmt();
        this.expression.addToJavaNode(throwStmt);
        ((BlockStmt) java).addStatement(throwStmt);
    }

    @Override
    public Position position() {
        return this.position;
    }
}
