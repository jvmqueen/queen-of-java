package org.queenlang.transpiler.nodes;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.expr.Expression;

public final class QueenTextExpressionNode implements QueenInitializerExpressionNode {
    final String expression;

    public QueenTextExpressionNode(final String expression) {
        this.expression = expression;
    }

    @Override
    public Expression asJavaExpression() {
        return StaticJavaParser.parseExpression(this.expression);
    }
}
