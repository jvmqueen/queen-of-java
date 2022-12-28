package org.queenlang.transpiler.nodes;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.expr.Expression;

public final class QueenTextExpressionNode implements QueenInitializerExpressionNode {

    private final Position position;
    private final String expression;

    public QueenTextExpressionNode(final Position position, final String expression) {
        this.position = position;
        this.expression = expression;
    }

    @Override
    public Expression asJavaExpression() {
        return StaticJavaParser.parseExpression(this.expression);
    }

    @Override
    public Position position() {
        return this.position;
    }
}
