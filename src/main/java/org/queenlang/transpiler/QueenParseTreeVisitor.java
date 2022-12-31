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

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.ParseTree;
import org.queenlang.generated.antlr4.QueenParser;
import org.queenlang.generated.antlr4.QueenParserBaseVisitor;
import org.queenlang.transpiler.nodes.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Queen visitor which traverses the ParseTree and generates the
 * Abstract Syntax Tree.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenParseTreeVisitor extends QueenParserBaseVisitor<QueenNode> {

    @Override
    public QueenCompilationUnitNode visitCompilationUnit(QueenParser.CompilationUnitContext ctx) {
        return new QueenCompilationUnitNode(
            getPosition(ctx),
            new QueenPackageDeclarationNode(
                getPosition(ctx.packageDeclaration()),
                () -> {
                    if(ctx.packageDeclaration() != null) {
                        QueenParser.PackageNameContext packageNameContext = ctx.packageDeclaration().packageName();
                        return asString(packageNameContext);
                    }
                    return null;
                }
            ),
            ctx.importDeclaration().stream().map(
                this::visitImportDeclaration
            ).collect(Collectors.toList()),
            List.of(this.visitTypeDeclaration(ctx.typeDeclaration()))
        );
    }

    @Override
    public QueenImportDeclarationNode visitImportDeclaration(QueenParser.ImportDeclarationContext ctx) {
        String importedType = "";
        boolean isStaticImport = false;
        boolean isAsteriskImport = false;

        final ParseTree importDeclaration = ctx.getChild(0);
        if(importDeclaration instanceof QueenParser.SingleTypeImportDeclarationContext) {
            importedType = asString(ctx.singleTypeImportDeclaration().typeName());
        } else if(importDeclaration instanceof QueenParser.StaticImportOnDemandDeclarationContext) {
            importedType = asString(ctx.staticImportOnDemandDeclaration().typeName());
            isAsteriskImport = true;
            isStaticImport = true;
        } else if(importDeclaration instanceof QueenParser.SingleStaticImportDeclarationContext) {
            final QueenParser.SingleStaticImportDeclarationContext staticImport = ctx.singleStaticImportDeclaration();
            importedType = asString(staticImport.typeName()) + "." + staticImport.Identifier().getText();
            isStaticImport = true;
        } else if(importDeclaration instanceof QueenParser.TypeImportOnDemandDeclarationContext) {
            importedType = asString(ctx.typeImportOnDemandDeclaration().packageOrTypeName());
            isAsteriskImport = true;
        }
        return new QueenImportDeclarationNode(
            getPosition(ctx), importedType, isStaticImport, isAsteriskImport
        );
    }

    @Override
    public QueenTypeDeclarationNode visitTypeDeclaration(QueenParser.TypeDeclarationContext ctx) {
        if(ctx.classDeclaration() != null) {
            return this.visitClassDeclaration(ctx.classDeclaration());
        } else if(ctx.interfaceDeclaration() != null) {
            return this.visitInterfaceDeclaration(ctx.interfaceDeclaration());
        }
        return null;
    }

    @Override
    public QueenClassDeclarationNode visitClassDeclaration(QueenParser.ClassDeclarationContext ctx) {
        final List<QueenAnnotationNode> annotations = new ArrayList<>();
        final List<QueenModifierNode> accessModifiers = new ArrayList<>();
        final List<QueenTypeParameterNode> typeParameters = new ArrayList<>();

        final String name = ctx.Identifier().getText();
        if(ctx.typeParameters() != null && ctx.typeParameters().typeParameterList() != null) {
            ctx.typeParameters().typeParameterList().typeParameter().forEach(
                tp -> typeParameters.add(this.visitTypeParameter(tp))
            );
        }
        final List<QueenClassOrInterfaceTypeNode> ofTypes = new ArrayList<>();
        if(ctx.superinterfaces() != null && ctx.superinterfaces().interfaceTypeList() != null) {
            ctx.superinterfaces().interfaceTypeList().interfaceType().forEach(
                inter -> ofTypes.add(this.visitInterfaceType(inter))
            );
        }
        QueenClassOrInterfaceTypeNode extendsType = null;
        if(ctx.superclass() != null) {
            extendsType = this.visitSuperclass(ctx.superclass());
        }
        ctx.annotation().forEach(
            a -> annotations.add(this.visitAnnotation(a))
        );
        ctx.classModifier().forEach(
            m -> accessModifiers.add(this.visitClassModifier(m))
        );
        return new QueenClassDeclarationNode(
            this.getPosition(ctx),
            annotations,
            accessModifiers,
            this.visitClassAbstractOrFinal(ctx.classAbstractOrFinal()),
            name,
            typeParameters,
            extendsType,
            ofTypes,
            new QueenClassBodyNode(
                getPosition(ctx),
                ctx.classBody().classBodyDeclaration().stream().map(
                    this::visitClassBodyDeclaration
                ).collect(Collectors.toList())
            )
        );
    }

    @Override
    public QueenInterfaceDeclarationNode visitInterfaceDeclaration(QueenParser.InterfaceDeclarationContext ctx) {
        if(ctx.normalInterfaceDeclaration() != null) {
            return this.visitNormalInterfaceDeclaration(ctx.normalInterfaceDeclaration());
        } else {
            return this.visitAnnotationTypeDeclaration(ctx.annotationTypeDeclaration());
        }
    }

    @Override
    public QueenNormalInterfaceDeclarationNode visitNormalInterfaceDeclaration(QueenParser.NormalInterfaceDeclarationContext ctx) {
        final List<QueenAnnotationNode> annotations = new ArrayList<>();
        final List<QueenModifierNode> modifiers = new ArrayList<>();
        final List<QueenTypeParameterNode> typeParams = new ArrayList<>();

        if(ctx.typeParameters() != null && ctx.typeParameters().typeParameterList() != null) {
            ctx.typeParameters().typeParameterList().typeParameter().forEach(
                tp -> typeParams.add(this.visitTypeParameter(tp))
            );
        }

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
            getPosition(ctx),
            annotations,
            modifiers,
            name,
            typeParams,
            extendsTypes,
            this.visitInterfaceBody(ctx.interfaceBody())
        );
    }

    @Override
    public QueenAnnotationTypeDeclarationNode visitAnnotationTypeDeclaration(QueenParser.AnnotationTypeDeclarationContext ctx) {
        final List<QueenAnnotationNode> annotations = new ArrayList<>();
        final List<QueenModifierNode> modifiers = new ArrayList<>();

        final String name = ctx.Identifier().getText();
        ctx.annotation().forEach(
            a -> annotations.add(this.visitAnnotation(a))
        );
        ctx.interfaceModifier().forEach(
            m -> modifiers.add(this.visitInterfaceModifier(m))
        );
        return new QueenAnnotationTypeDeclarationNode(
            getPosition(ctx), annotations, modifiers, name, this.visitAnnotationTypeBody(ctx.annotationTypeBody())
        );
    }

    @Override
    public QueenModifierNode visitClassModifier(QueenParser.ClassModifierContext ctx) {
        return new QueenModifierNode(getPosition(ctx), asString(ctx));
    }

    @Override
    public QueenModifierNode visitMethodModifier(QueenParser.MethodModifierContext ctx) {
        return new QueenModifierNode(getPosition(ctx), asString(ctx));
    }

    @Override
    public QueenModifierNode visitInterfaceMethodModifier(QueenParser.InterfaceMethodModifierContext ctx) {
        return new QueenModifierNode(getPosition(ctx), asString(ctx));
    }

    @Override
    public QueenModifierNode visitAnnotationTypeElementModifier(QueenParser.AnnotationTypeElementModifierContext ctx) {
        return new QueenModifierNode(getPosition(ctx), asString(ctx));
    }

    @Override
    public QueenModifierNode visitClassAbstractOrFinal(QueenParser.ClassAbstractOrFinalContext ctx) {
        return new QueenModifierNode(getPosition(ctx), asString(ctx));
    }

    @Override
    public QueenModifierNode visitInterfaceModifier(QueenParser.InterfaceModifierContext ctx) {
        return new QueenModifierNode(getPosition(ctx), asString(ctx));
    }

    @Override
    public QueenModifierNode visitFieldModifier(QueenParser.FieldModifierContext ctx) {
        return new QueenModifierNode(getPosition(ctx), asString(ctx));
    }

    @Override
    public QueenModifierNode visitConstantModifier(QueenParser.ConstantModifierContext ctx) {
        return new QueenModifierNode(getPosition(ctx), asString(ctx));
    }

    @Override
    public QueenModifierNode visitConstructorModifier(QueenParser.ConstructorModifierContext ctx) {
        if(ctx != null) {
            return new QueenModifierNode(getPosition(ctx), asString(ctx));
        }
        return null;
    }

    @Override
    public QueenAnnotationNode visitAnnotation(QueenParser.AnnotationContext ctx) {
        if(ctx.markerAnnotation() != null) {
            return new QueenMarkerAnnotationNode(getPosition(ctx), asString(ctx.markerAnnotation().typeName()));
        } else if(ctx.normalAnnotation() != null) {
            final Map<String, String> pairs = new HashMap<>();
            ctx.normalAnnotation()
                .elementValuePairList()
                .elementValuePair()
                .forEach(
                    pair -> pairs.put(
                        pair.Identifier().getText(),
                        asString(pair.elementValue())
                    )
                );
            return new QueenNormalAnnotationNode(
                getPosition(ctx),
                asString(ctx.normalAnnotation().typeName()),
                pairs
            );
        } else if(ctx.singleElementAnnotation() != null) {
            return new QueenSingleMemberAnnotationNode(
                getPosition(ctx),
                asString(ctx.singleElementAnnotation().typeName()),
                asString(ctx.singleElementAnnotation().elementValue())
            );
        }
        return null;
    }

    @Override
    public QueenClassBodyDeclarationNode visitClassBodyDeclaration(QueenParser.ClassBodyDeclarationContext ctx) {
        if(ctx.classMemberDeclaration() != null) {
            if(ctx.classMemberDeclaration().fieldDeclaration() != null) {
                return this.visitFieldDeclaration(ctx.classMemberDeclaration().fieldDeclaration());
            } else if(ctx.classMemberDeclaration().methodDeclaration() != null) {
                return this.visitMethodDeclaration(ctx.classMemberDeclaration().methodDeclaration());
            } else if(ctx.classMemberDeclaration().classDeclaration() != null) {
                return this.visitClassDeclaration(ctx.classMemberDeclaration().classDeclaration());
            } else if(ctx.classMemberDeclaration().interfaceDeclaration() != null) {
                return this.visitInterfaceDeclaration(ctx.classMemberDeclaration().interfaceDeclaration());
            }
        } else if(ctx.instanceInitializer() != null) {
            return this.visitInstanceInitializer(ctx.instanceInitializer());
        } else if(ctx.staticInitializer() != null) {
            return this.visitStaticInitializer(ctx.staticInitializer());
        } else if(ctx.constructorDeclaration() != null) {
            return this.visitConstructorDeclaration(ctx.constructorDeclaration());
        }
        return null;
    }

    @Override
    public QueenFieldDeclarationNode visitFieldDeclaration(QueenParser.FieldDeclarationContext ctx) {
        final List<QueenAnnotationNode> annotations = new ArrayList<>();
        ctx.annotation().forEach(
            a -> annotations.add(this.visitAnnotation(a))
        );

        final List<QueenModifierNode> modifiers = new ArrayList<>();
        ctx.fieldModifier().forEach(
            m -> modifiers.add(this.visitFieldModifier(m))
        );

        final Map<String, QueenInitializerExpressionNode> variables = new HashMap<>();
        ctx.variableDeclaratorList().variableDeclarator().forEach(
            vd -> {
                variables.put(asString(vd.variableDeclaratorId()), null);
                if(vd.variableInitializer() != null) {
                    variables.put(
                        asString(vd.variableDeclaratorId()),
                        this.visitVariableInitializer(vd.variableInitializer())
                    );
                }
            }
        );

        return new QueenFieldDeclarationNode(
            getPosition(ctx),
            annotations,
            modifiers,
            asString(ctx.unannType()),
            variables
        );
    }

    @Override
    public QueenConstantDeclarationNode visitConstantDeclaration(QueenParser.ConstantDeclarationContext ctx) {
        final List<QueenAnnotationNode> annotations = new ArrayList<>();
        ctx.annotation().forEach(
            a -> annotations.add(this.visitAnnotation(a))
        );

        final List<QueenModifierNode> modifiers = new ArrayList<>();
        ctx.constantModifier().forEach(
            m -> modifiers.add(this.visitConstantModifier(m))
        );

        final Map<String, QueenInitializerExpressionNode> variables = new HashMap<>();
        ctx.variableDeclaratorList().variableDeclarator().forEach(
            vd -> {
                variables.put(asString(vd.variableDeclaratorId()), null);
                if(vd.variableInitializer() != null) {
                    variables.put(
                        asString(vd.variableDeclaratorId()),
                        this.visitVariableInitializer(vd.variableInitializer())
                    );
                }
            }
        );

        return new QueenConstantDeclarationNode(
            getPosition(ctx),
            annotations,
            modifiers,
            asString(ctx.unannType()),
            variables
        );
    }

    @Override
    public QueenConstructorDeclarationNode visitConstructorDeclaration(QueenParser.ConstructorDeclarationContext ctx) {
        final List<QueenNode> annotations = new ArrayList<>();
        ctx.annotation().forEach(
            a -> annotations.add(this.visitAnnotation(a))
        );
        final List<QueenTypeParameterNode> typeParams = new ArrayList<>();
        final QueenParser.TypeParametersContext typeParameters = ctx.constructorDeclarator().typeParameters();
        if(typeParameters != null && typeParameters.typeParameterList() != null) {
            typeParameters.typeParameterList().typeParameter().forEach(
                tp -> typeParams.add(this.visitTypeParameter(tp))
            );
        }
        final List<QueenParameterNode> parameters = new ArrayList<>();
        if(ctx.constructorDeclarator().formalParameterList() != null) {
            final QueenParser.FormalParameterListContext formalParameterList = ctx.constructorDeclarator().formalParameterList();
            if(formalParameterList.formalParameters() != null) {
                formalParameterList.formalParameters().formalParameter()
                    .forEach(fp -> parameters.add(this.visitFormalParameter(fp)));
            }
            if(formalParameterList.lastFormalParameter() != null) {
                parameters.add(this.visitLastFormalParameter(formalParameterList.lastFormalParameter()));
            }
        }
        final List<String> throwsList = new ArrayList<>();
        if(ctx.throws_() != null && ctx.throws_().exceptionTypeList() != null) {
            ctx.throws_().exceptionTypeList().exceptionType().forEach(
                et -> throwsList.add(asString(et))
            );
        }
        final QueenParser.ConstructorBodyContext constructorBodyContext = ctx.constructorBody();
        final QueenExplicitConstructorInvocationNode explicitConstructorInvocationNode;
        if(constructorBodyContext.explicitConstructorInvocation() != null) {
            explicitConstructorInvocationNode = this.visitExplicitConstructorInvocation(constructorBodyContext.explicitConstructorInvocation());
        } else {
            explicitConstructorInvocationNode = null;
        }
        final QueenBlockStatements queenBlockStatements;
        if(constructorBodyContext.blockStatements() != null) {
            queenBlockStatements = this.visitBlockStatements(constructorBodyContext.blockStatements());
        } else {
            queenBlockStatements = null;
        }
        return new QueenConstructorDeclarationNode(
            getPosition(ctx),
            annotations,
            this.visitConstructorModifier(ctx.constructorModifier()),
            typeParams,
            asString(ctx.constructorDeclarator().simpleTypeName()),
            parameters,
            throwsList,
            explicitConstructorInvocationNode,
            queenBlockStatements
        );
    }

    @Override
    public QueenMethodDeclarationNode visitMethodDeclaration(QueenParser.MethodDeclarationContext ctx) {
        final List<QueenAnnotationNode> annotations = new ArrayList<>();
        ctx.annotation().forEach(
            a -> annotations.add(this.visitAnnotation(a))
        );
        final List<QueenModifierNode> modifiers = new ArrayList<>();
        ctx.methodModifier().forEach(
            m -> modifiers.add(this.visitMethodModifier(m))
        );
        final List<QueenParameterNode> parameters = new ArrayList<>();
        final QueenParser.MethodDeclaratorContext methodDeclarator = ctx.methodHeader().methodDeclarator();
        if(methodDeclarator.formalParameterList() != null) {
            final QueenParser.FormalParameterListContext formalParameterList = methodDeclarator.formalParameterList();
            if(formalParameterList.formalParameters() != null) {
                formalParameterList.formalParameters().formalParameter()
                    .forEach(fp -> parameters.add(this.visitFormalParameter(fp)));
            }
            if(formalParameterList.lastFormalParameter() != null) {
                parameters.add(this.visitLastFormalParameter(formalParameterList.lastFormalParameter()));
            }
        }
        final List<String> throwsList = new ArrayList<>();
        if(ctx.methodHeader().throws_() != null && ctx.methodHeader().throws_().exceptionTypeList() != null) {
            ctx.methodHeader().throws_().exceptionTypeList().exceptionType().forEach(
                et -> throwsList.add(asString(et))
            );
        }
        final QueenParser.MethodBodyContext methodBodyContext = ctx.methodBody();
        final QueenBlockStatements queenBlockStatements;
        if(methodBodyContext.block() != null && methodBodyContext.block().blockStatements() != null) {
            queenBlockStatements = this.visitBlockStatements(methodBodyContext.block().blockStatements());
        } else {
            queenBlockStatements = null;
        }
        final String returnType = asString(ctx.methodHeader().result());
        final List<QueenTypeParameterNode> typeParams = new ArrayList<>();
        if(ctx.methodHeader().typeParameters() != null && ctx.methodHeader().typeParameters().typeParameterList() != null) {
            ctx.methodHeader().typeParameters().typeParameterList().typeParameter().forEach(
                tp -> typeParams.add(this.visitTypeParameter(tp))
            );
        }
        return new QueenMethodDeclarationNode(
            getPosition(ctx),
            annotations,
            modifiers,
            returnType,
            typeParams,
            methodDeclarator.Identifier().getText(),
            parameters,
            throwsList,
            queenBlockStatements
        );
    }

    @Override
    public QueenInterfaceMethodDeclarationNode visitInterfaceMethodDeclaration(QueenParser.InterfaceMethodDeclarationContext ctx) {
        final List<QueenAnnotationNode> annotations = new ArrayList<>();
        ctx.annotation().forEach(
            a -> annotations.add(this.visitAnnotation(a))
        );
        final List<QueenModifierNode> modifiers = new ArrayList<>();
        ctx.interfaceMethodModifier().forEach(
            m -> modifiers.add(this.visitInterfaceMethodModifier(m))
        );
        final List<QueenParameterNode> parameters = new ArrayList<>();
        final QueenParser.MethodDeclaratorContext methodDeclarator = ctx.methodHeader().methodDeclarator();
        if(methodDeclarator.formalParameterList() != null) {
            final QueenParser.FormalParameterListContext formalParameterList = methodDeclarator.formalParameterList();
            if(formalParameterList.formalParameters() != null) {
                formalParameterList.formalParameters().formalParameter()
                    .forEach(fp -> parameters.add(this.visitFormalParameter(fp)));
            }
            if(formalParameterList.lastFormalParameter() != null) {
                parameters.add(this.visitLastFormalParameter(formalParameterList.lastFormalParameter()));
            }
        }
        final List<String> throwsList = new ArrayList<>();
        if(ctx.methodHeader().throws_() != null && ctx.methodHeader().throws_().exceptionTypeList() != null) {
            ctx.methodHeader().throws_().exceptionTypeList().exceptionType().forEach(
                et -> throwsList.add(asString(et))
            );
        }
        final QueenParser.MethodBodyContext methodBodyContext = ctx.methodBody();
        final QueenBlockStatements queenBlockStatements;
        if(methodBodyContext.block() != null && methodBodyContext.block().blockStatements() != null) {
            queenBlockStatements = this.visitBlockStatements(methodBodyContext.block().blockStatements());
        } else {
            queenBlockStatements = null;
        }
        final String returnType = asString(ctx.methodHeader().result());
        final List<QueenTypeParameterNode> typeParams = new ArrayList<>();
        if(ctx.methodHeader().typeParameters() != null && ctx.methodHeader().typeParameters().typeParameterList() != null) {
            ctx.methodHeader().typeParameters().typeParameterList().typeParameter().forEach(
                tp -> typeParams.add(this.visitTypeParameter(tp))
            );
        }
        return new QueenInterfaceMethodDeclarationNode(
            getPosition(ctx),
            annotations,
            modifiers,
            returnType,
            typeParams,
            methodDeclarator.Identifier().getText(),
            parameters,
            throwsList,
            queenBlockStatements
        );
    }

    @Override
    public QueenInitializerExpressionNode visitVariableInitializer(QueenParser.VariableInitializerContext ctx) {
        return new QueenTextExpressionNode(getPosition(ctx), asString(ctx));
    }

    @Override
    public QueenParameterNode visitFormalParameter(QueenParser.FormalParameterContext ctx) {
        final List<QueenAnnotationNode> annotations = new ArrayList<>();
        final List<QueenModifierNode> modifiers = new ArrayList<>();

        if(ctx.formalParameterModifier() != null) {
            ctx.formalParameterModifier().forEach(
                v -> {
                    if(v.annotation() != null) {
                        annotations.add(this.visitAnnotation(v.annotation()));
                    }
                    if(v.MUTABLE() != null) {
                        modifiers.add(new QueenModifierNode(getPosition(v), v.MUTABLE().getText()));
                    }
                }
            );
        }
        if(modifiers.isEmpty()) {
            modifiers.add(new QueenModifierNode(getPosition(ctx),"final"));
        }
        final String type = asString(ctx.unannType());
        final String name = asString(ctx.variableDeclaratorId());
        return new QueenParameterNode(
            getPosition(ctx),
            annotations,
            modifiers,
            type,
            name
        );
    }

    @Override
    public QueenParameterNode visitLastFormalParameter(QueenParser.LastFormalParameterContext ctx) {
        if(ctx.formalParameter() != null) {
            return this.visitFormalParameter(ctx.formalParameter());
        }
        final List<String> varArgAnnotations = new ArrayList<>();
        final List<QueenAnnotationNode> annotations = new ArrayList<>();
        final List<QueenModifierNode> modifiers = new ArrayList<>();

        if(ctx.formalParameterModifier() != null) {
            ctx.formalParameterModifier().forEach(
                v -> {
                    if(v.annotation() != null) {
                        annotations.add(this.visitAnnotation(v.annotation()));
                    }
                    if(v.MUTABLE() != null) {
                        modifiers.add(new QueenModifierNode(getPosition(v), v.MUTABLE().getText()));
                    }
                }
            );
        }
        if(modifiers.isEmpty()) {
            modifiers.add(new QueenModifierNode(getPosition(ctx),"final"));
        }

        final String type = asString(ctx.unannType());
        final String name = asString(ctx.variableDeclaratorId());
        if(ctx.annotation() != null) {
            ctx.annotation().forEach(
                va -> varArgAnnotations.add(asString(va))
            );
        }
        return new QueenParameterNode(
            getPosition(ctx),
            annotations,
            modifiers,
            type,
            name,
            varArgAnnotations,
            true
        );
    }

    @Override
    public QueenExplicitConstructorInvocationNode visitExplicitConstructorInvocation(QueenParser.ExplicitConstructorInvocationContext ctx) {
        return new QueenExplicitConstructorInvocationNode(getPosition(ctx), asString(ctx));
    }

    @Override
    public QueenBlockStatements visitBlockStatements(QueenParser.BlockStatementsContext ctx) {
        final List<QueenBlockStatementNode> blockStatements = new ArrayList<>();
        ctx.blockStatement().forEach(
            bs -> {
                if(bs.classDeclaration() != null) {
                    blockStatements.add(this.visitClassDeclaration(bs.classDeclaration()));
                }
                if(bs.localVariableDeclarationStatement() != null) {
                    blockStatements.add(
                        new QueenTextStatementNode(
                            getPosition(bs.localVariableDeclarationStatement()),
                            asString(bs.localVariableDeclarationStatement())
                        )
                    );
                }
                if(bs.statement() != null) {
                    blockStatements.add(
                        new QueenTextStatementNode(
                            getPosition(bs.statement()),
                            asString(bs.statement())
                        )
                    );
                }
            }
        );
        return new QueenBlockStatements(getPosition(ctx), blockStatements);
    }

    @Override
    public QueenInstanceInitializerNode visitInstanceInitializer(QueenParser.InstanceInitializerContext ctx) {
        return new QueenInstanceInitializerNode(
            getPosition(ctx), this.visitBlockStatements(ctx.block().blockStatements())
        );
    }

    @Override
    public QueenInstanceInitializerNode visitStaticInitializer(QueenParser.StaticInitializerContext ctx) {
        return new QueenInstanceInitializerNode(
            getPosition(ctx),
            this.visitBlockStatements(ctx.block().blockStatements()),
            true
        );
    }

    @Override
    public QueenInterfaceBodyNode visitInterfaceBody(QueenParser.InterfaceBodyContext ctx) {
        final List<QueenInterfaceMemberDeclarationNode> members = new ArrayList<>();
        if(ctx.interfaceMemberDeclaration() != null) {
            ctx.interfaceMemberDeclaration().forEach(
                imd -> members.add(this.visitInterfaceMemberDeclaration(imd))
            );
        }
        return new QueenInterfaceBodyNode(getPosition(ctx), members);
    }

    @Override
    public QueenAnnotationTypeBodyNode visitAnnotationTypeBody(QueenParser.AnnotationTypeBodyContext ctx) {
        final List<QueenAnnotationTypeMemberDeclarationNode> members = new ArrayList<>();
        if(ctx.annotationTypeMemberDeclaration() != null) {
            ctx.annotationTypeMemberDeclaration().forEach(
                amd -> members.add(this.visitAnnotationTypeMemberDeclaration(amd))
            );
        }
        return new QueenAnnotationTypeBodyNode(getPosition(ctx), members);
    }

    @Override
    public QueenInterfaceMemberDeclarationNode visitInterfaceMemberDeclaration(QueenParser.InterfaceMemberDeclarationContext ctx) {
        if(ctx.classDeclaration() != null) {
            return this.visitClassDeclaration(ctx.classDeclaration());
        } else if(ctx.interfaceDeclaration() != null) {
            return this.visitInterfaceDeclaration(ctx.interfaceDeclaration());
        } else if(ctx.constantDeclaration() != null) {
            return this.visitConstantDeclaration(ctx.constantDeclaration());
        } else if(ctx.interfaceMethodDeclaration() != null) {
            return this.visitInterfaceMethodDeclaration(ctx.interfaceMethodDeclaration());
        }
        return null;
    }

    @Override
    public QueenAnnotationTypeMemberDeclarationNode visitAnnotationTypeMemberDeclaration(QueenParser.AnnotationTypeMemberDeclarationContext ctx) {
        if(ctx.classDeclaration() != null) {
            return this.visitClassDeclaration(ctx.classDeclaration());
        } else if(ctx.interfaceDeclaration() != null) {
            return this.visitInterfaceDeclaration(ctx.interfaceDeclaration());
        }
        return this.visitAnnotationTypeElementDeclaration(ctx.annotationTypeElementDeclaration());
    }

    @Override
    public QueenAnnotationElementDeclarationNode visitAnnotationTypeElementDeclaration(QueenParser.AnnotationTypeElementDeclarationContext ctx) {
        final List<QueenAnnotationNode> annotations = new ArrayList<>();
        final List<QueenModifierNode> modifiers = new ArrayList<>();
        ctx.annotation().forEach(
            a -> annotations.add(this.visitAnnotation(a))
        );
        ctx.annotationTypeElementModifier().forEach(
            m -> modifiers.add(this.visitAnnotationTypeElementModifier(m))
        );
        final String type = asString(ctx.unannType());
        final String name = ctx.Identifier().getText();
        return new QueenAnnotationElementDeclarationNode(
            this.getPosition(ctx),
            annotations,
            modifiers,
            type,
            name,
            ctx.defaultValue() != null ? asString(ctx.defaultValue().elementValue()) : null
        );
    }

    public QueenTypeParameterNode visitTypeParameter(QueenParser.TypeParameterContext ctx) {
        final Position position = this.getPosition(ctx);
        final List<QueenAnnotationNode> annotations = new ArrayList<>();
        ctx.annotation().forEach(
            a -> annotations.add(this.visitAnnotation(a))
        );
        final String name = ctx.Identifier().getText();
        final List<String> typeBound = new ArrayList<>();
        if(ctx.typeBound() != null) {
            final QueenParser.TypeBoundContext typeBoundContext = ctx.typeBound();
            if(typeBoundContext.typeVariable() != null) {
                typeBound.add(asString(typeBoundContext.typeVariable()));
            }
            if(typeBoundContext.classOrInterfaceType() != null) {
                typeBound.add(asString(typeBoundContext.classOrInterfaceType()));
            }
            if(typeBoundContext.additionalBound() != null) {
                typeBoundContext.additionalBound().forEach(
                    ab -> typeBound.add(asString(ab.interfaceType()))
                );
            }
        }
        return new QueenTypeParameterNode(
            position,
            annotations,
            name,
            typeBound
        );
    }

    @Override
    public QueenClassOrInterfaceTypeNode visitSuperclass(QueenParser.SuperclassContext ctx) {
        final Position position = this.getPosition(ctx);
        final List<QueenAnnotationNode> annotations = new ArrayList<>();
        ctx.classType().annotation().forEach(
            a -> annotations.add(this.visitAnnotation(a))
        );
        final String name = ctx.classType().Identifier().getText();
        return new QueenClassOrInterfaceTypeNode(
            position,
            false,
            annotations,
            name
        );
    }

    @Override
    public QueenClassOrInterfaceTypeNode visitInterfaceType(QueenParser.InterfaceTypeContext ctx) {
        final Position position = this.getPosition(ctx);
        final List<QueenAnnotationNode> annotations = new ArrayList<>();
        ctx.classType().annotation().forEach(
            a -> annotations.add(this.visitAnnotation(a))
        );
        final String name = ctx.classType().Identifier().getText();
        return new QueenClassOrInterfaceTypeNode(
            position,
            true,
            annotations,
            name
        );
    }

    /**
     * Return context as String. getText is not enough for non-terminal nodes,
     * because it does not preseve the original spacing/indentation.
     *
     * On terminal nodes such as Identifier() and FINAL(), getText is fine to use.
     *
     * @param ctx ParserRuleContext.
     * @return String.
     */
    private String asString(final ParserRuleContext ctx) {
        return ctx.start.getInputStream()
            .getText(
                new Interval(
                    ctx.start.getStartIndex(),
                    ctx.stop.getStopIndex()
                )
            );
    }

    /**
     * Get the position of a ParseTree node.
     * @param ctx ParseTree node from ANTLR.
     * @return Position, never null.
     */
    private Position getPosition(final ParserRuleContext ctx) {
        final int line;
        final int column;
        if(ctx == null) {
            line = -1;
            column = -1;
        } else {
            line = ctx.getStart().getLine();
            column = ctx.getStart().getCharPositionInLine();
        }
        return new Position() {
            @Override
            public int line() {
                return line;
            }

            @Override
            public int column() {
                return column;
            }

            @Override
            public String toString() {
                return this.line() + ":" + this.column();
            }
        };
    }
}
