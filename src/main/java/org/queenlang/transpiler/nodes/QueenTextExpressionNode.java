package org.queenlang.transpiler.nodes;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.expr.Expression;

public final class QueenTextExpressionNode implements QueenExpressionNode {

    private final Position position;
    private final String expression;

    public QueenTextExpressionNode(final Position position, final String expression) {
        this.position = position;
        this.expression = expression;
    }

    /**
     * Turn it into a JavaParser Expression.
     * @return Expression, never null.
     */
    public Expression toJavaExpression() {
        return StaticJavaParser.parseExpression(this.expression);
    }

    @Override
    public Position position() {
        return this.position;
    }
}
