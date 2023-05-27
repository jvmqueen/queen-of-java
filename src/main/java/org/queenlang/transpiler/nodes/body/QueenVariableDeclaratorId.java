package org.queenlang.transpiler.nodes.body;

import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.QueenNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class QueenVariableDeclaratorId implements VariableDeclaratorId {

    private final Position position;
    private final QueenNode parent;
    private final String name;

    public QueenVariableDeclaratorId(
        final Position position,
        final String name
    ) {
        this(position, null, name);
    }

    private QueenVariableDeclaratorId(
        final Position position,
        final QueenNode parent,
        final String name
    ) {
        this.position = position;
        this.parent = parent;
        this.name = name;
    }

    @Override
    public Position position() {
        return this.position;
    }

    @Override
    public List<QueenNode> children() {
        return new ArrayList<>();
    }

    @Override
    public QueenNode withParent(final QueenNode parent) {
        return new QueenVariableDeclaratorId(
            this.position,
            parent,
            this.name
        );
    }

    @Override
    public QueenNode parent() {
        return this.parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QueenVariableDeclaratorId that = (QueenVariableDeclaratorId) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String name() {
        return this.name;
    }

}
