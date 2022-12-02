package org.queenlang.transpiler;

import org.antlr.runtime.tree.ParseTree;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.Trees;
import org.queenlang.generated.antlr4.QueenParser;
import org.queenlang.generated.antlr4.QueenParserBaseVisitor;
import org.queenlang.transpiler.nodes.QueenCompilationUnitNode;
import org.queenlang.transpiler.nodes.QueenImplementationNode;
import org.queenlang.transpiler.nodes.QueenNode;
import org.queenlang.transpiler.nodes.QueenPackageDeclaration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class QueenVisitor extends QueenParserBaseVisitor<QueenNode> {

    public QueenNode visitCompilationUnit(QueenParser.CompilationUnitContext ctx) {
        return new QueenCompilationUnitNode(
            new QueenPackageDeclaration(
                () -> {
                    if(ctx.packageDeclaration() != null) {
                        QueenParser.PackageNameContext packageNameContext = ctx.packageDeclaration().packageName();
                        return packageNameContext.getText();
                    }
                    return null;
                }
            ),
            null,//this.visitImportDeclaration(ctx.importDeclaration(0)),
            null//this.visitTypeDeclaration(ctx.typeDeclaration(0))
        );
    }

    @Override
    public QueenNode visitNormalClassDeclaration(QueenParser.NormalClassDeclarationContext ctx) {
        return new QueenImplementationNode() {
            @Override
            public String name() {
                return ctx.Identifier().getText();
            }

            @Override
            public List<String> of() {
                return ctx.superClassAndOrInterfaces()
                    .superinterfaces()
                    .interfaceTypeList()
                    .interfaceType()
                    .stream()
                    .map(of -> of.classType().Identifier().getText())
                    .collect(Collectors.toList());
            }

            @Override
            public String extendsClass() {
                final QueenParser.SuperclassContext superClass = ctx.superClassAndOrInterfaces().superclass();
                return superClass != null ? superClass.classType().Identifier().getText() : null;
            }
        };
    }
}
