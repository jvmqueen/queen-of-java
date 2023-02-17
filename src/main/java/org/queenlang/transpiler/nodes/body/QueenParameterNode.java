package org.queenlang.transpiler.nodes.body;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithParameters;
import com.github.javaparser.ast.type.UnknownType;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.QueenNode;
import org.queenlang.transpiler.nodes.expressions.QueenAnnotationNode;
import org.queenlang.transpiler.nodes.types.QueenTypeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Queen parameter AST node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 * @todo #33:60min Write unit tests for QueenParameterNode.
 */
public final class QueenParameterNode implements ParameterNode {
    private final Position position;
    private final List<QueenAnnotationNode> annotations;
    private final List<QueenModifierNode> modifiers;
    private final QueenVariableDeclaratorId name;
    private final QueenTypeNode type;
    private final List<QueenAnnotationNode> varArgsAnnotations;
    private final boolean varArgs;

    public QueenParameterNode(
        final Position position,
        final QueenVariableDeclaratorId name
    ) {
        this(position, new ArrayList<>(), new ArrayList<>(), null, name, false);
    }

    public QueenParameterNode(
        final Position position,
        final List<QueenAnnotationNode> annotations,
        final List<QueenModifierNode> modifiers,
        final QueenTypeNode type,
        final QueenVariableDeclaratorId name
    ) {
        this(position, annotations, modifiers, type, name, false);
    }

    public QueenParameterNode(
        final Position position,
        final List<QueenAnnotationNode> annotations,
        final List<QueenModifierNode> modifiers,
        final QueenTypeNode type,
        final QueenVariableDeclaratorId name,
        final boolean varArgs
    ) {
        this(position, annotations, modifiers, type, name, new ArrayList<>(), varArgs);
    }

    public QueenParameterNode(
        final Position position,
        final List<QueenAnnotationNode> annotations,
        final List<QueenModifierNode> modifiers,
        final QueenTypeNode type,
        final QueenVariableDeclaratorId name,
        final List<QueenAnnotationNode> varArgsAnnotations,
        final boolean varArgs
    ) {
        this.position = position;
        this.annotations = annotations;
        this.modifiers = modifiers;
        this.type = type;
        this.name = name;
        this.varArgsAnnotations = varArgsAnnotations;
        this.varArgs = varArgs;
    }

    @Override
    public void addToJavaNode(final Node java) {
        final Parameter parameter = new Parameter();
        this.annotations.forEach( a -> a.addToJavaNode(parameter));
        this.modifiers.forEach(m -> m.addToJavaNode(parameter));
        if(this.type != null) {
            this.type.addToJavaNode(parameter);
        } else {
            parameter.setType(new UnknownType());
        }
        this.name.addToJavaNode(parameter);
        parameter.setVarArgs(this.varArgs);
        if(this.varArgsAnnotations != null) {
            parameter.setVarArgsAnnotations(
                new NodeList<>(
                    this.varArgsAnnotations.stream().map(
                        varga -> (AnnotationExpr) varga.toJavaExpression()
                    ).collect(Collectors.toList())
                )
            );
        }
        ((NodeWithParameters) java).addParameter(parameter);
    }

    @Override
    public Position position() {
        return this.position;
    }

    @Override
    public List<QueenAnnotationNode> annotations() {
        return this.annotations;
    }

    @Override
    public List<QueenModifierNode> modifiers() {
        return this.modifiers;
    }

    @Override
    public QueenVariableDeclaratorId name() {
        return this.name;
    }

    @Override
    public QueenTypeNode type() {
        return this.type;
    }

    @Override
    public List<QueenAnnotationNode> varArgsAnnotations() {
        return this.varArgsAnnotations;
    }

    @Override
    public boolean varArgs() {
        return this.varArgs;
    }
}
