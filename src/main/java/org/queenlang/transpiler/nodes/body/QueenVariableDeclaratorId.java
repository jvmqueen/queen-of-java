package org.queenlang.transpiler.nodes.body;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.TypeExpr;
import com.github.javaparser.ast.type.ArrayType;
import com.github.javaparser.ast.type.Type;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.QueenNode;
import org.queenlang.transpiler.nodes.expressions.QueenAnnotationNode;
import org.queenlang.transpiler.nodes.expressions.QueenArrayDimensionNode;
import org.queenlang.transpiler.nodes.types.QueenArrayTypeNode;

import java.util.List;
import java.util.Objects;

public final class QueenVariableDeclaratorId implements QueenNode {

    private final Position position;
    private final String name;
    private final List<QueenArrayDimensionNode> dims;

    public QueenVariableDeclaratorId(
        final Position position,
        final String name,
        final List<QueenArrayDimensionNode> dims
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
            if(this.dims != null && this.dims.size() > 0) {
                Type setType = vd.getType();
                for(int i = this.dims.size() - 1; i>=0; i--) {
                    setType = new ArrayType(
                        setType
                    );
                    for(final QueenAnnotationNode annotation : this.dims.get(i).annotations()) {
                        annotation.addToJavaNode(setType);
                    }
                }
                vd.setType(setType);
            }
        }
    }

    @Override
    public Position position() {
        return this.position;
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
}
