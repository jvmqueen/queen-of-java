package org.queenlang.transpiler.nodes.expressions;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.ArrayInitializerExpr;
import com.github.javaparser.ast.expr.Expression;
import org.queenlang.transpiler.nodes.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Queen array initialized expression, AST Node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenArrayInitializerExpressionNode implements ArrayInitializerExpressionNode {

    private final Position position;
    private final List<ExpressionNode> values;

    public QueenArrayInitializerExpressionNode(
        final Position position,
        final List<ExpressionNode> values
    ) {
        this.position = position;
        this.values = values;
    }

    @Override
    public Expression toJavaExpression() {
        final ArrayInitializerExpr arrayInitializerExpr = new ArrayInitializerExpr();
        if(this.values != null) {
            final List<Expression> javaExp = new ArrayList<>();
            this.values.forEach(
                v -> javaExp.add(v.toJavaExpression())
            );
            arrayInitializerExpr.setValues(new NodeList<>(javaExp));
        }
        return arrayInitializerExpr;
    }

    @Override
    public Position position() {
        return this.position;
    }

    @Override
    public List<ExpressionNode> values() {
        return this.values;
    }
}
