package org.queenlang.transpiler.nodes;

import com.github.javaparser.ast.Node;

public final class QueenCompilationUnitNode implements QueenNode {
    private final QueenNode packageDeclaration;
    private final QueenNode importDeclaration;
    private final QueenNode typeDeclaration;

    public QueenCompilationUnitNode(
        final QueenNode packageDeclaration,
        final QueenNode importDeclaration,
        final QueenNode typeDeclaration
    ) {
        this.packageDeclaration = packageDeclaration;
        this.importDeclaration = importDeclaration;
        this.typeDeclaration = typeDeclaration;
    }

    @Override
    public void addToJavaNode(Node java) {
        this.packageDeclaration.addToJavaNode(java);
//        this.importDeclaration.addToJavaNode(java);
//        this.typeDeclaration.addToJavaNode(java);
    }
}
