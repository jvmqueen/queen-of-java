package org.queenlang.transpiler.nodes.body;

import java.util.List;

/**
 * Queen AST Node with parameters (constructor declaration, method declaration etc).
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public interface NodeWithParameters {

    /**
     * Parameters.
     * @return List of parameters.
     */
    List<ParameterNode> parameters();

}
