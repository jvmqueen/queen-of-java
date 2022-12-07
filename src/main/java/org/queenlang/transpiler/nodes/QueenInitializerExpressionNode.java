package org.queenlang.transpiler.nodes;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.VariableDeclarator;

public interface QueenInitializerExpressionNode extends QueenExpressionNode {

    default void addToJavaNode(final Node java) {
        final VariableDeclarator variableDeclarator = (VariableDeclarator) java;
        variableDeclarator.setInitializer(this.asJavaExpression());
    }
}
