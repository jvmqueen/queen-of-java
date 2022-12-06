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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public QueenCompilationUnitNode visitCompilationUnit(QueenParser.CompilationUnitContext ctx) {
        return new QueenCompilationUnitNode(
            new QueenPackageDeclarationNode(
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
    public QueenImportDeclarationNode visitImportDeclaration(QueenParser.ImportDeclarationContext ctx) {
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
    public QueenTypeDeclarationNode visitTypeDeclaration(QueenParser.TypeDeclarationContext ctx) {
        if(ctx.classDeclaration() != null) {
            return this.visitClassDeclaration(ctx.classDeclaration());
        } else if(ctx.interfaceDeclaration().normalInterfaceDeclaration() != null) {
            return this.visitNormalInterfaceDeclaration(ctx.interfaceDeclaration().normalInterfaceDeclaration());
        } else if(ctx.interfaceDeclaration().annotationTypeDeclaration() != null) {
            return this.visitAnnotationTypeDeclaration(ctx.interfaceDeclaration().annotationTypeDeclaration());
        }
        return null;
    }

    @Override
    public QueenClassDeclarationNode visitClassDeclaration(QueenParser.ClassDeclarationContext ctx) {
        final List<QueenAnnotationNode> annotations = new ArrayList<>();
        final List<QueenClassAccessModifierNode> accessModifiers = new ArrayList<>();

        final String name = ctx.Identifier().getText();
        final List<String> ofTypes = ctx
            .superClassAndOrInterfaces()
            .superinterfaces()
            .interfaceTypeList()
            .interfaceType()
            .stream()
            .map(of -> of.classType().Identifier().getText())
            .collect(Collectors.toList());
        String extendsType = null;
        final QueenParser.SuperclassContext superClass = ctx.superClassAndOrInterfaces().superclass();
        if(superClass != null) {
            extendsType = superClass.classType().Identifier().getText();
        }
        ctx.annotation().forEach(
            a -> annotations.add(this.visitAnnotation(a))
        );
        ctx.classModifier().forEach(
            m -> accessModifiers.add(this.visitClassModifier(m))
        );
        return new QueenClassDeclarationNode(
            annotations,
            accessModifiers,
            this.visitClassAbstractOrFinal(ctx.classAbstractOrFinal()),
            name,
            extendsType,
            ofTypes,
            new QueenClassBodyNode(
                ctx.classBody().classBodyDeclaration().stream().map(
                    this::visitClassBodyDeclaration
                ).collect(Collectors.toList())
            )
        );
    }

    public QueenNormalInterfaceDeclarationNode visitNormalInterfaceDeclaration(QueenParser.NormalInterfaceDeclarationContext ctx) {
        final List<QueenAnnotationNode> annotations = new ArrayList<>();
        final List<QueenInterfaceModifierNode> modifiers = new ArrayList<>();

        final List<String> extendsTypes = new ArrayList<>();
        final String name = ctx.Identifier().getText();
        final QueenParser.ExtendsInterfacesContext extendsInterfacesContext = ctx
            .extendsInterfaces();
        if(extendsInterfacesContext != null) {
            extendsTypes.addAll(
                extendsInterfacesContext
                    .interfaceTypeList()
                    .interfaceType()
                    .stream()
                    .map(of -> of.classType().Identifier().getText())
                    .collect(Collectors.toList())
            );
        }
        ctx.annotation().forEach(
            a -> annotations.add(this.visitAnnotation(a))
        );
        ctx.interfaceModifier().forEach(
            m -> modifiers.add(this.visitInterfaceModifier(m))
        );
        return new QueenNormalInterfaceDeclarationNode(
            annotations, modifiers, name, extendsTypes
        );
    }

    public QueenAnnotationTypeDeclarationNode visitAnnotationTypeDeclaration(QueenParser.AnnotationTypeDeclarationContext ctx) {
        final List<QueenAnnotationNode> annotations = new ArrayList<>();
        final List<QueenInterfaceModifierNode> modifiers = new ArrayList<>();

        final String name = ctx.Identifier().getText();
        ctx.annotation().forEach(
            a -> annotations.add(this.visitAnnotation(a))
        );
        ctx.interfaceModifier().forEach(
            m -> modifiers.add(this.visitInterfaceModifier(m))
        );
        return new QueenAnnotationTypeDeclarationNode(annotations, modifiers, name);
    }

    @Override
    public QueenClassAccessModifierNode visitClassModifier(QueenParser.ClassModifierContext ctx) {
        return new QueenClassAccessModifierNode(ctx.getText());
    }

    @Override
    public QueenClassExtensionModifierNode visitClassAbstractOrFinal(QueenParser.ClassAbstractOrFinalContext ctx) {
        return new QueenClassExtensionModifierNode(ctx.getText());
    }

    @Override
    public QueenInterfaceModifierNode visitInterfaceModifier(QueenParser.InterfaceModifierContext ctx) {
        return new QueenInterfaceModifierNode(ctx.getText());
    }

    @Override
    public QueenFieldModifierNode visitFieldModifier(QueenParser.FieldModifierContext ctx) {
        return new QueenFieldModifierNode(ctx.getText());
    }

    @Override
    public QueenAnnotationNode visitAnnotation(QueenParser.AnnotationContext ctx) {
        if(ctx.markerAnnotation() != null) {
            return new QueenMarkerAnnotationNode(ctx.markerAnnotation().typeName().getText());
        } else if(ctx.normalAnnotation() != null) {
            final Map<String, String> pairs = new HashMap<>();
            ctx.normalAnnotation()
                .elementValuePairList()
                .elementValuePair()
                .forEach(
                    pair -> pairs.put(
                        pair.Identifier().getText(),
                        pair.elementValue().getText()
                    )
                );
            return new QueenNormalAnnotationNode(
                ctx.normalAnnotation().typeName().getText(),
                pairs
            );
        } else if(ctx.singleElementAnnotation() != null) {
            return new QueenSingleMemberAnnotationNode(
                ctx.singleElementAnnotation().typeName().getText(),
                ctx.singleElementAnnotation().elementValue().getText()
            );
        }
        return null;
    }

    @Override
    public QueenClassBodyDeclarationNode visitClassBodyDeclaration(QueenParser.ClassBodyDeclarationContext ctx) {
        if(ctx.classMemberDeclaration() != null) {
            if(ctx.classMemberDeclaration().fieldDeclaration() != null) {
                return this.visitFieldDeclaration(ctx.classMemberDeclaration().fieldDeclaration());
            }
        } else if(ctx.constructorDeclaration() != null) {
            final List<QueenNode> annotations = new ArrayList<>();
            ctx.constructorDeclaration().annotation().forEach(
                a -> annotations.add(this.visitAnnotation(a))
            );
            return new QueenConstructorDeclarationNode(annotations);
        }
        return null;
    }

    @Override
    public QueenFieldDeclarationNode visitFieldDeclaration(QueenParser.FieldDeclarationContext ctx) {
        final List<QueenAnnotationNode> annotations = new ArrayList<>();
        ctx.annotation().forEach(
            a -> annotations.add(this.visitAnnotation(a))
        );

        final List<QueenFieldModifierNode> modifiers = new ArrayList<>();
        ctx.fieldModifier().forEach(
            m -> modifiers.add(this.visitFieldModifier(m))
        );

        return new QueenFieldDeclarationNode(
            annotations,
            modifiers,
            ctx.unannType().getText()
        );
    }
}
