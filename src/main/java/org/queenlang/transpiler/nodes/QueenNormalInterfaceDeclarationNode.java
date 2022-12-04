package org.queenlang.transpiler.nodes;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import java.util.List;

/**
 * Queen NormalInterfaceDeclaration AST node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 * @todo #10:60min Don't forget to write some unit tests here.
 */
public final class QueenNormalInterfaceDeclarationNode extends QueenInterfaceDeclarationNode {
    /**
     * Name of this type.
     */
    private final String name;

    /**
     * Types which are extended (an interface can extend more interfaces).
     */
    private final List<String> extendsTypes;

    /**
     * Ctor.
     * @param annotations Annotation nodes on top of this type.
     * @param name Name.
     * @param extendsTypes Types extended.
     */
    public QueenNormalInterfaceDeclarationNode(
        final List<QueenAnnotationNode> annotations,
        final String name,
        final List<String> extendsTypes
    ) {
        super(annotations);
        this.name = name;
        this.extendsTypes = extendsTypes;
    }

    @Override
    public void addToJavaNode(final Node java) {
        ClassOrInterfaceDeclaration clazz = ((CompilationUnit) java)
            .addInterface(this.name);
        if(this.extendsTypes != null && this.extendsTypes.size() > 0) {
            this.extendsTypes.forEach(clazz::addExtendedType);
        }
        super.addToJavaNode(clazz);
    }
}
