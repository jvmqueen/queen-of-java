package org.queenlang.transpiler.nodes.body;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.nodeTypes.NodeWithType;
import com.github.javaparser.ast.type.ArrayType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.UnknownType;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.QueenNode;
import org.queenlang.transpiler.nodes.expressions.AnnotationNode;
import org.queenlang.transpiler.nodes.expressions.ArrayDimensionNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class QueenVariableDeclaratorId implements VariableDeclaratorId {

    private final Position position;
    private final String name;
    private final List<ArrayDimensionNode> dims;

    public QueenVariableDeclaratorId(
        final Position position,
        final String name
    ) {
        this(position, name, null);
    }

    public QueenVariableDeclaratorId(
        final Position position,
        final String name,
        final List<ArrayDimensionNode> dims
    ) {
        this.position = position;
        this.name = name;
        this.dims = dims;
    }

    @Override
    public void addToJavaNode(final Node java) {
        if(java instanceof VariableDeclarator) {
            final VariableDeclarator vd = (VariableDeclarator) java;
            vd.setName(this.name);
            this.setDims(vd);
        } else if(java instanceof Parameter) {
            final Parameter parameter = (Parameter) java;
            parameter.setName(this.name);
            if(parameter.getType() != null && !(parameter.getType() instanceof UnknownType)) {
                this.setDims(parameter);
            }
        }
    }

    @Override
    public Position position() {
        return this.position;
    }

    @Override
    public List<QueenNode> children() {
        final List<QueenNode> children = new ArrayList<>();
        if(this.dims != null) {
            children.addAll(this.dims);
        }
        return children;
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

    /**
     * Set the dims from the name to the type.
     * @param withType Node with type.
     */
    private void setDims(final NodeWithType withType) {
        if(this.dims != null && this.dims.size() > 0) {
            Type setType = withType.getType();
            for(int i = this.dims.size() - 1; i>=0; i--) {
                setType = new ArrayType(
                    setType
                );
                for(final AnnotationNode annotation : this.dims.get(i).annotations()) {
                    annotation.addToJavaNode(setType);
                }
            }
            withType.setType(setType);
        }
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public List<ArrayDimensionNode> dims() {
        return this.dims;
    }
}
