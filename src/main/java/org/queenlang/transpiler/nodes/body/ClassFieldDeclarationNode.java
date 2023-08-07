package org.queenlang.transpiler.nodes.body;

import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.QueenNode;
import org.queenlang.transpiler.nodes.expressions.AnnotationNode;
import org.queenlang.transpiler.nodes.types.TypeNode;

import java.lang.reflect.Field;
import java.util.List;

/**
 * A field declaration based on a Java Field object.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class ClassFieldDeclarationNode implements FieldDeclarationNode {
    /**
     * Parent.
     */
    private final QueenNode parent;

    /**
     * Java field.
     */
    private final Field field;

    public ClassFieldDeclarationNode(final QueenNode parent, final Field field) {
        this.parent = parent;
        this.field = field;
    }

    @Override
    public String name() {
        return null;
    }

    @Override
    public Position position() {
        return null;
    }

    @Override
    public List<QueenNode> children() {
        return null;
    }

    @Override
    public QueenNode withParent(QueenNode parent) {
        return null;
    }

    @Override
    public QueenNode parent() {
        return null;
    }

    @Override
    public TypeNode type() {
        return null;
    }

    @Override
    public VariableDeclaratorNode variable() {
        return null;
    }

    @Override
    public List<AnnotationNode> annotations() {
        return null;
    }

    @Override
    public List<ModifierNode> modifiers() {
        return null;
    }
}
