package org.queenlang.transpiler;

import org.queenlang.transpiler.nodes.QueenNode;
import org.queenlang.transpiler.nodes.ResolutionContext;

import java.util.ArrayList;
import java.util.List;

public class QueenResolutionContext implements ResolutionContext {
    private final List<QueenNode> visited = new ArrayList<>();
    @Override
    public void add(final QueenNode visited) {
        this.visited.add(visited);
    }

    @Override
    public boolean alreadyVisited(final QueenNode node) {
        return this.visited.contains(node);
    }
}
