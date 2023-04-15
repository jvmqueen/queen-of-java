package org.queenlang.transpiler.nodes.expressions;

import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.QueenNode;
import org.queenlang.transpiler.nodes.body.ClassBodyNode;
import org.queenlang.transpiler.nodes.types.ClassOrInterfaceTypeNode;
import org.queenlang.transpiler.nodes.types.TypeNode;
import org.queenlang.transpiler.util.QueenMockito;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for {@link QueenObjectCreationExpressionNode}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenObjectCreationExpressionNodeTestCase {

    @Test
    public void returnsPosition() {
        final Position position = QueenMockito.mock(Position.class);
        final ObjectCreationExpressionNode objectCreationExpr = new QueenObjectCreationExpressionNode(
            position,
            QueenMockito.mock(ExpressionNode.class),
            QueenMockito.mock(ClassOrInterfaceTypeNode.class),
            new ArrayList<>(),
            new ArrayList<>(),
            QueenMockito.mock(ClassBodyNode.class)
        );
        MatcherAssert.assertThat(
            objectCreationExpr.position(),
            Matchers.is(position)
        );
    }

    @Test
    public void returnsScope() {
        final Position position = QueenMockito.mock(Position.class);
        final ExpressionNode scope = QueenMockito.mock(ExpressionNode.class);
        final ObjectCreationExpressionNode objectCreationExpr = new QueenObjectCreationExpressionNode(
            position,
            scope,
            QueenMockito.mock(ClassOrInterfaceTypeNode.class),
            new ArrayList<>(),
            new ArrayList<>(),
            QueenMockito.mock(ClassBodyNode.class)
        );
        MatcherAssert.assertThat(
            objectCreationExpr.scope(),
            Matchers.is(scope)
        );
    }

    @Test
    public void returnsType() {
        final Position position = QueenMockito.mock(Position.class);
        final ExpressionNode scope = QueenMockito.mock(ExpressionNode.class);
        final ClassOrInterfaceTypeNode type = QueenMockito.mock(ClassOrInterfaceTypeNode.class);
        final ObjectCreationExpressionNode objectCreationExpr = new QueenObjectCreationExpressionNode(
            position,
            scope,
            type,
            new ArrayList<>(),
            new ArrayList<>(),
            QueenMockito.mock(ClassBodyNode.class)
        );
        MatcherAssert.assertThat(
            objectCreationExpr.type(),
            Matchers.is(type)
        );
    }

    @Test
    public void returnsTypeArguments() {
        final Position position = QueenMockito.mock(Position.class);
        final ExpressionNode scope = QueenMockito.mock(ExpressionNode.class);
        final ClassOrInterfaceTypeNode type = QueenMockito.mock(ClassOrInterfaceTypeNode.class);
        final List<TypeNode> typeArgs = new ArrayList<>();
        typeArgs.add(QueenMockito.mock(TypeNode.class));
        final ObjectCreationExpressionNode objectCreationExpr = new QueenObjectCreationExpressionNode(
            position,
            scope,
            type,
            typeArgs,
            new ArrayList<>(),
            QueenMockito.mock(ClassBodyNode.class)
        );
        MatcherAssert.assertThat(
            objectCreationExpr.typeArguments(),
            Matchers.is(typeArgs)
        );
    }

    @Test
    public void returnsArguments() {
        final Position position = QueenMockito.mock(Position.class);
        final ExpressionNode scope = QueenMockito.mock(ExpressionNode.class);
        final ClassOrInterfaceTypeNode type = QueenMockito.mock(ClassOrInterfaceTypeNode.class);
        final List<TypeNode> typeArgs = new ArrayList<>();
        typeArgs.add(QueenMockito.mock(TypeNode.class));
        final List<ExpressionNode> arguments = new ArrayList<>();
        arguments.add(QueenMockito.mock(ExpressionNode.class));
        final ObjectCreationExpressionNode objectCreationExpr = new QueenObjectCreationExpressionNode(
            position,
            scope,
            type,
            typeArgs,
            arguments,
            QueenMockito.mock(ClassBodyNode.class)
        );
        MatcherAssert.assertThat(
            objectCreationExpr.arguments(),
            Matchers.is(arguments)
        );
    }

    @Test
    public void returnsBody() {
        final Position position = QueenMockito.mock(Position.class);
        final ExpressionNode scope = QueenMockito.mock(ExpressionNode.class);
        final ClassOrInterfaceTypeNode type = QueenMockito.mock(ClassOrInterfaceTypeNode.class);
        final List<TypeNode> typeArgs = new ArrayList<>();
        typeArgs.add(QueenMockito.mock(TypeNode.class));
        final List<ExpressionNode> arguments = new ArrayList<>();
        arguments.add(QueenMockito.mock(ExpressionNode.class));
        final ClassBodyNode body = QueenMockito.mock(ClassBodyNode.class);
        final ObjectCreationExpressionNode objectCreationExpr = new QueenObjectCreationExpressionNode(
            position,
            scope,
            type,
            typeArgs,
            arguments,
            body
        );
        MatcherAssert.assertThat(
            objectCreationExpr.anonymousBody(),
            Matchers.is(body)
        );
    }

    @Test
    public void returnsJavaNodeFull() {
        final Position position = QueenMockito.mock(Position.class);
        final ExpressionNode scope = QueenMockito.mock(ExpressionNode.class);
        Mockito.when(scope.toJavaExpression()).thenReturn(new NameExpr("java.util"));
        final ClassOrInterfaceTypeNode type = QueenMockito.mock(ClassOrInterfaceTypeNode.class);
        Mockito.when(type.toType()).thenReturn(new ClassOrInterfaceType("Student"));
        final List<TypeNode> typeArgs = new ArrayList<>();
        final TypeNode typeArg = QueenMockito.mock(TypeNode.class);
        Mockito.when(typeArg.toType()).thenReturn(new ClassOrInterfaceType("String"));
        typeArgs.add(typeArg);
        final List<ExpressionNode> arguments = new ArrayList<>();
        final ExpressionNode arg = QueenMockito.mock(ExpressionNode.class);
        Mockito.when(arg.toJavaExpression()).thenReturn(new StringLiteralExpr("Mihai"));
        arguments.add(arg);
        final ClassBodyNode body = QueenMockito.mock(ClassBodyNode.class);
        Mockito.when(body.isEmpty()).thenReturn(false);
        final ObjectCreationExpressionNode objectCreationExpr = new QueenObjectCreationExpressionNode(
            position,
            scope,
            type,
            typeArgs,
            arguments,
            body
        );
        final ObjectCreationExpr javaObjectCreation = (ObjectCreationExpr) objectCreationExpr.toJavaExpression();
        MatcherAssert.assertThat(
            javaObjectCreation,
            Matchers.notNullValue()
        );
        typeArgs.forEach(
            ta -> Mockito.verify(ta, Mockito.times(1)).addToJavaNode(
                Mockito.any(ObjectCreationExpr.class)
            )
        );
        arguments.forEach(
            a -> Mockito.verify(a, Mockito.times(1)).toJavaExpression()
        );
        Mockito.verify(body, Mockito.times(1)).addToJavaNode(
            Mockito.any(ObjectCreationExpr.class)
        );
        Mockito.verify(scope, Mockito.times(1)).toJavaExpression();
        Mockito.verify(type, Mockito.times(1)).toType();

    }

    @Test
    public void returnsChildren() {
        final Position position = QueenMockito.mock(Position.class);
        final ExpressionNode scope = QueenMockito.mock(ExpressionNode.class);
        final ClassOrInterfaceTypeNode type = QueenMockito.mock(ClassOrInterfaceTypeNode.class);
        final List<TypeNode> typeArgs = new ArrayList<>();
        typeArgs.add(QueenMockito.mock(TypeNode.class));
        final List<ExpressionNode> arguments = new ArrayList<>();
        arguments.add(QueenMockito.mock(ExpressionNode.class));
        final ClassBodyNode body = QueenMockito.mock(ClassBodyNode.class);
        final ObjectCreationExpressionNode objectCreationExpr = new QueenObjectCreationExpressionNode(
            position,
            scope,
            type,
            typeArgs,
            arguments,
            body
        );

        final List<QueenNode> children = objectCreationExpr.children();
        MatcherAssert.assertThat(
            children.size(),
            Matchers.is(5)
        );
        MatcherAssert.assertThat(
            children.containsAll(
                List.of(
                    scope,
                    type,
                    typeArgs.get(0),
                    arguments.get(0),
                    body
                )
            ),
            Matchers.is(true)
        );
    }
}
