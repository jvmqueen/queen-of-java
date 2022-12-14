package org.queenlang.transpiler.nodes;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.nodeTypes.NodeWithCondition;

public final class QueenTextExpressionNode implements QueenExpressionNode {

    private final Position position;
    private final String expression;

    public QueenTextExpressionNode(final Position position, final String expression) {
        this.position = position;
        this.expression = expression;
    }

    @Override
    public void addToJavaNode(final Node java) {
        if(java instanceof VariableDeclarator) {
            final VariableDeclarator variableDeclarator = (VariableDeclarator) java;
            variableDeclarator.setInitializer(this.toJavaExpression());
        } else if (java instanceof NodeWithCondition) {
            ((NodeWithCondition) java).setCondition(this.toJavaExpression());
        }
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
