package org.queenlang.transpiler.nodes;

import com.github.javaparser.ast.Node;

public interface QueenNode {
    void addToJavaNode(final Node java);
}
