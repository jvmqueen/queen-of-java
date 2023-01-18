package org.queenlang.transpiler.nodes;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.nodeTypes.NodeWithCondition;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.stmt.SynchronizedStmt;
import com.github.javaparser.ast.stmt.ThrowStmt;

public interface QueenExpressionNode extends QueenNode {

    default void addToJavaNode(final Node java) {
        if(java instanceof VariableDeclarator) {
            final VariableDeclarator variableDeclarator = (VariableDeclarator) java;
            variableDeclarator.setInitializer(this.toJavaExpression());
        } else if (java instanceof NodeWithCondition) {
            ((NodeWithCondition) java).setCondition(this.toJavaExpression());
        } else if(java instanceof ThrowStmt) {
            ((ThrowStmt) java).setExpression(this.toJavaExpression());
        } else if(java instanceof ReturnStmt) {
            ((ReturnStmt) java).setExpression(this.toJavaExpression());
        } else if(java instanceof SynchronizedStmt) {
            ((SynchronizedStmt) java).setExpression(this.toJavaExpression());
        } else if(java instanceof SwitchStmt) {
            ((SwitchStmt) java).setSelector(this.toJavaExpression());
        }
    }

    Expression toJavaExpression();
}
