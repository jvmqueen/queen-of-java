package org.queenlang.transpiler.nodes;

import org.queenlang.transpiler.nodes.QueenNode;

public interface ResolutionContext {
    void add(final QueenNode visited);
    boolean alreadyVisited(final QueenNode node);
}
