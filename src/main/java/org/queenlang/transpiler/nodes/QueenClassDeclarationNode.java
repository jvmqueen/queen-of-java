package org.queenlang.transpiler.nodes;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import java.util.List;

/**
 * Queen ClassDeclaration AST node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 * @todo #10:30min QueenClassDeclarationNode needs unit testing.
 */
public final class QueenClassDeclarationNode extends QueenTypeDeclarationNode {
    /**
     * Name of this type.
     */
    private final String name;

    /**
     * Type which is extended.
     */
    private final String extendsType;

    /**
     * Interfaces this type implements.
     */
    private final List<String> of;

    /**
     * Ctor.
     * @param annotations Annotation nodes on top of this type.
     * @param name Name.
     * @param extendsType Type extended.
     * @param of Types implemented.
     */
    public QueenClassDeclarationNode(
        final List<QueenAnnotationNode> annotations,
        final String name,
        final String extendsType,
        final List<String> of
    ) {
        super(annotations);
        this.name = name;
        this.extendsType = extendsType;
        this.of = of;
    }

    @Override
    public void addToJavaNode(final Node java) {
        ClassOrInterfaceDeclaration clazz = ((CompilationUnit) java)
            .addClass(this.name);
        if(this.extendsType != null) {
            clazz.addExtendedType(this.extendsType);
        }
        if(this.of != null && this.of.size() > 0) {
            this.of.forEach(clazz::addImplementedType);
        }
        super.addToJavaNode(clazz);
    }
}
