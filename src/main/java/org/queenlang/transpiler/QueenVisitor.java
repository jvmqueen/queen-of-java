/**
 * Copyright (c) 2022-2023, Extremely Distributed Technologies S.R.L. Romania
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 * Neither the name of the copyright holder nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */
package org.queenlang.transpiler;

import org.antlr.v4.runtime.tree.ParseTree;
import org.queenlang.generated.antlr4.QueenParser;
import org.queenlang.generated.antlr4.QueenParserBaseVisitor;
import org.queenlang.transpiler.nodes.*;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Queen visitor which traverses the ParseTree and generates the
 * Abstract Syntax Tree.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenVisitor extends QueenParserBaseVisitor<QueenNode> {

    @Override
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
            ctx.importDeclaration().stream().map(
                this::visitImportDeclaration
            ).collect(Collectors.toList()),
            ctx.typeDeclaration().stream().map(
                this::visitTypeDeclaration
            ).collect(Collectors.toList())
        );
    }

    @Override
    public QueenNode visitImportDeclaration(QueenParser.ImportDeclarationContext ctx) {
        String importedType = "";
        boolean isStaticImport = false;
        boolean isAsteriskImport = false;

        final ParseTree importDeclaration = ctx.getChild(0);
        if(importDeclaration instanceof QueenParser.SingleTypeImportDeclarationContext) {
            importedType = ctx.singleTypeImportDeclaration().typeName().getText();
        } else if(importDeclaration instanceof QueenParser.StaticImportOnDemandDeclarationContext) {
            importedType = ctx.staticImportOnDemandDeclaration().typeName().getText();
            isAsteriskImport = true;
            isStaticImport = true;
        } else if(importDeclaration instanceof QueenParser.SingleStaticImportDeclarationContext) {
            final QueenParser.SingleStaticImportDeclarationContext staticImport = ctx.singleStaticImportDeclaration();
            importedType = staticImport.typeName().getText() + "." + staticImport.Identifier().getText();
            isStaticImport = true;
        } else if(importDeclaration instanceof QueenParser.TypeImportOnDemandDeclarationContext) {
            importedType = ctx.typeImportOnDemandDeclaration().packageOrTypeName().getText();
            isAsteriskImport = true;
        }
        return new QueenImportDeclarationNode(importedType, isStaticImport, isAsteriskImport);
    }

    @Override
    public QueenNode visitTypeDeclaration(QueenParser.TypeDeclarationContext ctx) {
        String type = "";
        String name = "";
        List<String> extendsTypes = new ArrayList<>();
        List<String> ofTypes = new ArrayList<>();
        if(ctx.classDeclaration() != null) {
            type = ctx.classDeclaration().IMPLEMENTATION().getText();
            name = ctx.classDeclaration().Identifier().getText();
            ofTypes = ctx.classDeclaration()
                .superClassAndOrInterfaces()
                .superinterfaces()
                .interfaceTypeList()
                .interfaceType()
                .stream()
                .map(of -> of.classType().Identifier().getText())
                .collect(Collectors.toList());
            final QueenParser.SuperclassContext superClass = ctx
                .classDeclaration().superClassAndOrInterfaces().superclass();
            if(superClass != null) {
                extendsTypes.add(superClass.classType().Identifier().getText());
            }
        } else if(ctx.interfaceDeclaration().normalInterfaceDeclaration() != null) {
            type = ctx.interfaceDeclaration().normalInterfaceDeclaration().INTERFACE().getText();            name = ctx.interfaceDeclaration().normalInterfaceDeclaration().Identifier().getText();
            final QueenParser.ExtendsInterfacesContext extendsInterfacesContext = ctx
                .interfaceDeclaration()
                .normalInterfaceDeclaration()
                .extendsInterfaces();
            if(extendsInterfacesContext != null) {
                extendsTypes = extendsInterfacesContext
                    .interfaceTypeList()
                    .interfaceType()
                    .stream()
                    .map(of -> of.classType().Identifier().getText())
                    .collect(Collectors.toList());
            }
        } else if(ctx.interfaceDeclaration().annotationTypeDeclaration() != null) {
            type = "@" + ctx.interfaceDeclaration().annotationTypeDeclaration().INTERFACE().getText();
            name = ctx.interfaceDeclaration().annotationTypeDeclaration().Identifier().getText();
        }

        return new QueenTypeDeclaration(
            type,
            name,
            extendsTypes,
            ofTypes
        );
    }

//    @Override
//    public QueenNode visitNormalClassDeclaration(QueenParser.NormalClassDeclarationContext ctx) {
//        return new QueenImplementationNode() {
//            @Override
//            public String name() {
//                return ctx.Identifier().getText();
//            }
//
//            @Override
//            public List<String> of() {
//                return ctx.superClassAndOrInterfaces()
//                    .superinterfaces()
//                    .interfaceTypeList()
//                    .interfaceType()
//                    .stream()
//                    .map(of -> of.classType().Identifier().getText())
//                    .collect(Collectors.toList());
//            }
//
//            @Override
//            public String extendsClass() {
//                final QueenParser.SuperclassContext superClass = ctx.superClassAndOrInterfaces().superclass();
//                return superClass != null ? superClass.classType().Identifier().getText() : null;
//            }
//        };
//    }
}
