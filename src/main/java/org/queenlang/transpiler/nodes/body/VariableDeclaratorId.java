package org.queenlang.transpiler.nodes.body;

import org.queenlang.transpiler.nodes.QueenNode;
import org.queenlang.transpiler.nodes.expressions.ArrayDimensionNode;
import org.queenlang.transpiler.nodes.expressions.QueenArrayDimensionNode;

import java.util.List;

public interface VariableDeclaratorId extends QueenNode {

    String name();
    List<ArrayDimensionNode> dims();
}
