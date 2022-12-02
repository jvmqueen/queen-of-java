package org.queenlang.transpiler.nodes;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import org.queenlang.generated.antlr4.QueenLexer;

import java.util.function.Supplier;

public final class QueenPackageDeclaration implements QueenNode {

    private final String packageName;

    public QueenPackageDeclaration(final Supplier<String> packageName) {
        this.packageName = packageName.get();
    }

    @Override
    public void addToJavaNode(Node java) {
        if(this.packageName != null) {
            ((CompilationUnit) java).setPackageDeclaration(this.packageName);
        }
    }
}
