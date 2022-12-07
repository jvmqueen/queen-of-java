package org.queenlang.transpiler.nodes;

import com.github.javaparser.ast.expr.Expression;

public interface QueenExpressionNode extends QueenNode {

    Expression asJavaExpression();

}
