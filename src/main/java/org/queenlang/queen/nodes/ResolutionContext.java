package org.queenlang.queen.nodes;

public interface ResolutionContext {
    void add(final QueenNode visited);
    boolean alreadyVisited(final QueenNode node);
}
