package org.queenlang.transpiler.nodes;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import java.util.List;

public interface QueenImplementationNode extends QueenNode {

    /**
     * Name of the Implementation.
     * @return String, never null.
     */
    String name();

    /**
     * Names of the interfaces which this implementation respects.
     * @return List of String, minimum 1 element.
     */
    List<String> of();

    /**
     * Other implementation extended by this implementation.
     * @return String or null.
     */
    String extendsClass();

    @Override
    default void addToJavaNode(final Node java) {
        PackageDeclaration pg;
//        ((CompilationUnit) java).setPackageDeclaration()
        ClassOrInterfaceDeclaration clazz = ((CompilationUnit) java).addClass(this.name());
        clazz.addMethod("ASADSASD");
        if(this.extendsClass() != null) {
            clazz.addExtendedType(this.extendsClass());
        }
        for(final String implOf : this.of()) {
            clazz.addImplementedType(implOf);
        }
    }
}
