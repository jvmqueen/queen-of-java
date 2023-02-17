package org.queenlang.transpiler.nodes.body;

import org.queenlang.transpiler.nodes.QueenNode;
import org.queenlang.transpiler.nodes.expressions.QueenAnnotationNode;
import org.queenlang.transpiler.nodes.types.QueenTypeNode;

import java.util.List;

/**
 * Queen parameter AST node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public interface ParameterNode extends QueenNode {
    List<QueenAnnotationNode> annotations();
    List<QueenModifierNode> modifiers();
    QueenVariableDeclaratorId name();
    QueenTypeNode type();
    List<QueenAnnotationNode> varArgsAnnotations();
    boolean varArgs();
}
