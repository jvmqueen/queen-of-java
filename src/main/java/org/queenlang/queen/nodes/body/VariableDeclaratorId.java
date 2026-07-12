package org.queenlang.queen.nodes.body;

import org.queenlang.queen.visitors.QueenASTVisitor;
import org.queenlang.queen.nodes.QueenNode;

public interface VariableDeclaratorId extends QueenNode {

    String name();

    default <T> T accept(QueenASTVisitor<? extends T> visitor) {
        return visitor.visitVariableDeclaratorId(this);
    }
}
