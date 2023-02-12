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
import org.queenlang.transpiler.nodes.expressions.*;
import org.queenlang.transpiler.nodes.statements.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Queen visitor which traverses the ParseTree and generates the
 * Abstract Syntax Tree.
 *
 * In order to understand the APIs and logic used in this visitor, you
 * need to study the QueenParser.g4 ANTLR file, as this visitor is based on
 * the ParseTree genertated by ANTLR.
 *
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
                        return this.visitPackageName(ctx.packageDeclaration().packageName());
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

        final List<QueenClassOrInterfaceTypeNode> extendsTypes = new ArrayList<>();
        final String name = ctx.Identifier().getText();
        final QueenParser.ExtendsInterfacesContext extendsInterfacesContext = ctx
            .extendsInterfaces();
        if(extendsInterfacesContext != null) {
            extendsInterfacesContext.interfaceTypeList().interfaceType().forEach(
                interfaceType -> extendsTypes.add(
                    this.visitInterfaceType(interfaceType)
                )
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
            } else {
                return this.visitInterfaceDeclaration(ctx.classMemberDeclaration().interfaceDeclaration());
            }
        } else if(ctx.instanceInitializer() != null) {
            return this.visitInstanceInitializer(ctx.instanceInitializer());
        } else if(ctx.staticInitializer() != null) {
            return this.visitStaticInitializer(ctx.staticInitializer());
        } else {
            return this.visitConstructorDeclaration(ctx.constructorDeclaration());
        }
    }

    @Override
    public QueenLocalVariableDeclarationNode visitLocalVariableDeclaration(QueenParser.LocalVariableDeclarationContext ctx) {
        final List<QueenAnnotationNode> annotations = new ArrayList<>();
        final List<QueenModifierNode> modifiers = new ArrayList<>();

        if(ctx.variableModifier() != null) {
            ctx.variableModifier().forEach(
                vm -> {
                    if(vm.annotation() != null) {
                        annotations.add(this.visitAnnotation(vm.annotation()));
                    }
                    if(vm.FINAL() != null) {
                        modifiers.add(
                            new QueenModifierNode(
                                this.getPosition(vm),
                                vm.FINAL().getText()
                            )
                        );
                    }
                }
            );
        }

        final Map<String, QueenExpressionNode> variables = new LinkedHashMap<>();
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

        return new QueenLocalVariableDeclarationNode(
            getPosition(ctx),
            annotations,
            modifiers,
            this.visitUnannType(ctx.unannType()),
            variables
        );
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

        final Map<String, QueenExpressionNode> variables = new HashMap<>();
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
            this.visitUnannType(ctx.unannType()),
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

        final Map<String, QueenExpressionNode> variables = new HashMap<>();
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
            this.visitUnannType(ctx.unannType()),
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
        final List<QueenExceptionTypeNode> throwsList = new ArrayList<>();
        if(ctx.throws_() != null && ctx.throws_().exceptionTypeList() != null) {
            ctx.throws_().exceptionTypeList().exceptionType().forEach(
                et -> throwsList.add(this.visitExceptionType(et))
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
        final List<QueenExceptionTypeNode> throwsList = new ArrayList<>();
        if(ctx.methodHeader().throws_() != null && ctx.methodHeader().throws_().exceptionTypeList() != null) {
            ctx.methodHeader().throws_().exceptionTypeList().exceptionType().forEach(
                et -> throwsList.add(this.visitExceptionType(et))
            );
        }
        final QueenParser.MethodBodyContext methodBodyContext = ctx.methodBody();
        final QueenBlockStatements queenBlockStatements;
        if(methodBodyContext.block() != null && methodBodyContext.block().blockStatements() != null) {
            queenBlockStatements = this.visitBlockStatements(methodBodyContext.block().blockStatements());
        } else {
            queenBlockStatements = new QueenBlockStatements(getPosition(methodBodyContext));
        }
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
            this.visitResult(ctx.methodHeader().result()),
            typeParams,
            methodDeclarator.Identifier().getText(),
            parameters,
            throwsList,
            queenBlockStatements
        );
    }

    @Override
    public QueenTypeNode visitResult(QueenParser.ResultContext ctx) {
        if(ctx.unannType() != null) {
            return this.visitUnannType(ctx.unannType());
        } else {
            return new QueenVoidNode(this.getPosition(ctx));
        }
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
        final List<QueenExceptionTypeNode> throwsList = new ArrayList<>();
        if(ctx.methodHeader().throws_() != null && ctx.methodHeader().throws_().exceptionTypeList() != null) {
            ctx.methodHeader().throws_().exceptionTypeList().exceptionType().forEach(
                et -> throwsList.add(this.visitExceptionType(et))
            );
        }
        final QueenParser.MethodBodyContext methodBodyContext = ctx.methodBody();
        final QueenBlockStatements queenBlockStatements;
        if(methodBodyContext.block() != null && methodBodyContext.block().blockStatements() != null) {
            queenBlockStatements = this.visitBlockStatements(methodBodyContext.block().blockStatements());
        } else {
            queenBlockStatements = null;
        }
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
            this.visitResult(ctx.methodHeader().result()),
            typeParams,
            methodDeclarator.Identifier().getText(),
            parameters,
            throwsList,
            queenBlockStatements
        );
    }

    @Override
    public QueenExpressionNode visitConstantExpression(QueenParser.ConstantExpressionContext ctx) {
        return this.visitExpression(ctx.expression());
    }

    @Override
    public QueenExpressionNode visitExpression(QueenParser.ExpressionContext ctx) {
        if(ctx == null) {
            return null;
        }
        if(ctx.lambdaExpression() != null) {
            return this.visitLambdaExpression(ctx.lambdaExpression());
        } else {
            return this.visitAssignmentExpression((ctx.assignmentExpression()));
        }
    }

    @Override
    public QueenExpressionNode visitArrayInitializer(QueenParser.ArrayInitializerContext ctx) {
        final List<QueenExpressionNode> values;
        if(ctx.variableInitializerList() != null) {
            values = new ArrayList<>();
            ctx.variableInitializerList().variableInitializer().forEach(
                vi -> values.add(this.visitVariableInitializer(vi))
            );
        } else {
            values = null;
        }
        return new QueenArrayInitializerExpressionNode(
            getPosition(ctx),
            values
        );
    }

    @Override
    public QueenExpressionNode visitAssignmentExpression(QueenParser.AssignmentExpressionContext ctx) {
        if(ctx.assignment() != null) {
            return this.visitAssignment(ctx.assignment());
        } else {
            return this.visitConditionalExpression(ctx.conditionalExpression());
//            return new QueenTextExpressionNode(getPosition(ctx), asString(ctx));
        }
    }

    @Override
    public QueenExpressionNode visitPrimary(QueenParser.PrimaryContext ctx) {
        QueenExpressionNode primary;
        if(ctx.primaryNoNewArray_lfno_primary() != null) {
            primary = this.visitPrimaryNoNewArray_lfno_primary(
                ctx.primaryNoNewArray_lfno_primary()
            );
        } else {
            primary = this.visitArrayCreationExpression(ctx.arrayCreationExpression());
        }
        if(ctx.primaryNoNewArray_lf_primary() != null) {
            for(final QueenParser.PrimaryNoNewArray_lf_primaryContext ctxp : ctx.primaryNoNewArray_lf_primary()) {
                primary = this.visitPrimaryNoNewArray_lf_primary(primary, ctxp);
            }
        }
        return primary;
    }

    @Override
    public QueenExpressionNode visitPrimaryNoNewArray_lfno_arrayAccess(QueenParser.PrimaryNoNewArray_lfno_arrayAccessContext ctx) {
        if(ctx.literal() != null) {
            return this.visitLiteral(ctx.literal());
        } else if(ctx.typeName() != null && ctx.IMPLEMENTATION() != null) {
            final QueenNameNode typeName = this.visitTypeName(ctx.typeName());
            final List<QueenArrayDimensionNode> dims = new ArrayList<>();
            if(ctx.unannDim() != null) {
                ctx.unannDim().forEach(
                    dim -> dims.add(
                        new QueenArrayDimensionNode(getPosition(dim))
                    )
                );
            }
            return new QueenTypeImplementationExpressionNode(
                getPosition(ctx.typeName()),
                typeName,
                dims
            );
        } else if(ctx.VOID() != null && ctx.IMPLEMENTATION() != null) {
            return new QueenTypeImplementationExpressionNode(
                getPosition(ctx),
                new QueenVoidNode(getPosition(ctx)),
                new ArrayList<>()
            );
        } else if(ctx.THIS() != null) {
            if(ctx.typeName() != null) {
                return new QueenThisExpressionNode(
                    getPosition(ctx),
                    this.visitTypeName(ctx.typeName())
                );
            } else {
                return new QueenThisExpressionNode(
                    getPosition(ctx)
                );
            }
        } else if(ctx.LPAREN() != null && ctx.expression() != null && ctx.RPAREN() != null) {
            return new QueenBracketedExpressionNode(
                getPosition(ctx.expression()),
                this.visitExpression(ctx.expression())
            );
        } else if(ctx.classInstanceCreationExpression() != null) {
            return this.visitClassInstanceCreationExpression(ctx.classInstanceCreationExpression());
        } else if(ctx.fieldAccess() != null) {
            return this.visitFieldAccess(ctx.fieldAccess());
        } else if(ctx.methodInvocation() != null) {
            return this.visitMethodInvocation(ctx.methodInvocation());
        } else {
            return this.visitMethodReference(ctx.methodReference());
        }
    }

    public QueenExpressionNode visitPrimaryNoNewArray_lf_primary(QueenExpressionNode scope, QueenParser.PrimaryNoNewArray_lf_primaryContext ctx) {
        if(ctx.classInstanceCreationExpression_lf_primary() != null) {
            return this.visitClassInstanceCreationExpression_lf_primary(
                scope,
                ctx.classInstanceCreationExpression_lf_primary()
            );
        } else if(ctx.fieldAccess_lf_primary() != null) {
            return this.visitFieldAccess_lf_primary(
                scope,
                ctx.fieldAccess_lf_primary()
            );
        } else if(ctx.arrayAccess_lf_primary() != null) {
            return this.visitArrayAccess_lf_primary(
                scope,
                ctx.arrayAccess_lf_primary()
            );
        } else if(ctx.methodInvocation_lf_primary() != null) {
            return this.visitMethodInvocation_lf_primary(
                scope,
                ctx.methodInvocation_lf_primary()
            );
        } else {
            return this.visitMethodReference_lf_primary(
                scope,
                ctx.methodReference_lf_primary()
            );
        }
    }

    @Override
    public QueenExpressionNode visitPrimaryNoNewArray_lfno_primary(QueenParser.PrimaryNoNewArray_lfno_primaryContext ctx) {
        if(ctx.literal() != null) {
            return this.visitLiteral(ctx.literal());
        } else if(ctx.typeName() != null && ctx.IMPLEMENTATION() != null) {
            final QueenNameNode typeName = this.visitTypeName(ctx.typeName());
            final List<QueenArrayDimensionNode> dims = new ArrayList<>();
            if(ctx.unannDim() != null) {
                ctx.unannDim().forEach(
                    dim -> dims.add(
                        new QueenArrayDimensionNode(getPosition(dim))
                    )
                );
            }
            return new QueenTypeImplementationExpressionNode(
                getPosition(ctx.typeName()),
                typeName,
                dims
            );
        } else if(ctx.unannPrimitiveType() != null && ctx.IMPLEMENTATION() != null) {
            final QueenPrimitiveTypeNode primitiveType = this.visitUnannPrimitiveType(ctx.unannPrimitiveType());
            final List<QueenArrayDimensionNode> dims = new ArrayList<>();
            if(ctx.unannDim() != null) {
                ctx.unannDim().forEach(
                    dim -> dims.add(
                        new QueenArrayDimensionNode(getPosition(dim))
                    )
                );
            }
            return new QueenTypeImplementationExpressionNode(
                getPosition(ctx.unannPrimitiveType()),
                primitiveType,
                dims
            );
        } else if(ctx.VOID() != null && ctx.IMPLEMENTATION() != null) {
            return new QueenTypeImplementationExpressionNode(
                getPosition(ctx),
                new QueenVoidNode(getPosition(ctx)),
                new ArrayList<>()
            );
        } else if(ctx.THIS() != null) {
            if(ctx.typeName() != null) {
                return new QueenThisExpressionNode(
                    getPosition(ctx),
                    this.visitTypeName(ctx.typeName())
                );
            } else {
                return new QueenThisExpressionNode(
                    getPosition(ctx)
                );
            }
        } else if(ctx.LPAREN() != null && ctx.expression() != null && ctx.RPAREN() != null) {
            return new QueenBracketedExpressionNode(
                getPosition(ctx.expression()),
                this.visitExpression(ctx.expression())
            );
        } else if(ctx.classInstanceCreationExpression_lfno_primary() != null) {
            return this.visitClassInstanceCreationExpression_lfno_primary(ctx.classInstanceCreationExpression_lfno_primary());
        } else if(ctx.fieldAccess_lfno_primary() != null) {
            return this.visitFieldAccess_lfno_primary(ctx.fieldAccess_lfno_primary());
        } else if(ctx.arrayAccess_lfno_primary() != null) {
            return this.visitArrayAccess_lfno_primary(ctx.arrayAccess_lfno_primary());
        } else if(ctx.methodInvocation_lfno_primary() != null) {
            return this.visitMethodInvocation_lfno_primary(ctx.methodInvocation_lfno_primary());
        } else {
            return this.visitMethodReference_lfno_primary(ctx.methodReference_lfno_primary());
        }
    }

    @Override
    public QueenNameNode visitTypeName(QueenParser.TypeNameContext ctx) {
        if(ctx.packageOrTypeName() == null) {
            return new QueenNameNode(
                getPosition(ctx),
                ctx.Identifier().getText()
            );
        } else {
            final QueenNameNode packageOrTypeName = this.visitPackageOrTypeName(ctx.packageOrTypeName());
            return new QueenNameNode(
                getPosition(ctx),
                packageOrTypeName,
                ctx.Identifier().getText()
            );
        }
    }

    @Override
    public QueenNameNode visitPackageOrTypeName(QueenParser.PackageOrTypeNameContext ctx) {
        if(ctx.packageOrTypeName() == null) {
            return new QueenNameNode(
                getPosition(ctx),
                ctx.Identifier().getText()
            );
        } else {
            final QueenNameNode packageOrTypeName = this.visitPackageOrTypeName(ctx.packageOrTypeName());
            return new QueenNameNode(
                getPosition(ctx),
                packageOrTypeName,
                ctx.Identifier().getText()
            );
        }
    }

    @Override
    public QueenNameNode visitPackageName(QueenParser.PackageNameContext ctx) {
        if(ctx.packageName() == null) {
            return new QueenNameNode(
                getPosition(ctx),
                ctx.Identifier().getText()
            );
        } else {
            final QueenNameNode packageName = this.visitPackageName(ctx.packageName());
            return new QueenNameNode(
                getPosition(ctx),
                packageName,
                ctx.Identifier().getText()
            );
        }
    }

    @Override
    public QueenNameNode visitExpressionName(QueenParser.ExpressionNameContext ctx) {
        if(ctx.ambiguousName() == null) {
            return new QueenNameNode(
                getPosition(ctx),
                ctx.Identifier().getText()
            );
        } else {
            final QueenNameNode ambiguousName = this.visitAmbiguousName(ctx.ambiguousName());
            return new QueenNameNode(
                getPosition(ctx),
                ambiguousName,
                ctx.Identifier().getText()
            );
        }
    }

    @Override
    public QueenNameNode visitMethodName(QueenParser.MethodNameContext ctx) {
        return new QueenNameNode(
            getPosition(ctx),
            ctx.Identifier().getText()
        );
    }

    @Override
    public QueenNameNode visitAmbiguousName(QueenParser.AmbiguousNameContext ctx) {
        if(ctx.ambiguousName() == null) {
            return new QueenNameNode(
                getPosition(ctx),
                ctx.Identifier().getText()
            );
        } else {
            final QueenNameNode ambiguousName = this.visitAmbiguousName(ctx.ambiguousName());
            return new QueenNameNode(
                getPosition(ctx),
                ambiguousName,
                ctx.Identifier().getText()
            );
        }
    }

    @Override
    public QueenExpressionNode visitArrayCreationExpression(QueenParser.ArrayCreationExpressionContext ctx) {
        final Position position = getPosition(ctx);
        final QueenTypeNode type;
        if(ctx.primitiveType() != null) {
            type = this.visitPrimitiveType(ctx.primitiveType());
        } else {
            type = this.visitClassOrInterfaceType(ctx.classOrInterfaceType());
        }
        final List<QueenArrayDimensionNode> dims = new ArrayList<>();
        if(ctx.dimExprs() != null) {
            ctx.dimExprs().dimExpr().forEach(
                de -> dims.add(this.visitDimExpr(de))
            );
        }
        if(ctx.dims() != null) {
            ctx.dims().dim().forEach(
                d -> dims.add(this.visitDim(d))
            );
        }
        final QueenExpressionNode arrayInitExpr;
        if(ctx.arrayInitializer() != null) {
            arrayInitExpr = this.visitArrayInitializer(ctx.arrayInitializer());
        } else {
            arrayInitExpr = null;
        }
        return new QueenArrayCreationExpressionNode(
            position,
            type,
            dims,
            arrayInitExpr
        );
    }

    @Override
    public QueenArrayDimensionNode visitDimExpr(QueenParser.DimExprContext ctx) {
        final Position position = getPosition(ctx);
        final List<QueenAnnotationNode> annotations = new ArrayList<>();
        if(ctx.annotation() != null) {
            ctx.annotation().forEach(
                a -> annotations.add(this.visitAnnotation(a))
            );
        }
        final QueenExpressionNode expression = this.visitExpression(ctx.expression());
        return new QueenArrayDimensionNode(
            position,
            annotations,
            expression
        );
    }

    @Override
    public QueenArrayDimensionNode visitDim(QueenParser.DimContext ctx) {
        final Position position = getPosition(ctx);
        final List<QueenAnnotationNode> annotations = new ArrayList<>();
        if(ctx.annotation() != null) {
            ctx.annotation().forEach(
                a -> annotations.add(this.visitAnnotation(a))
            );
        }
        return new QueenArrayDimensionNode(
            position,
            annotations
        );
    }

    @Override
    public QueenExpressionNode visitLiteral(QueenParser.LiteralContext ctx) {
        if(ctx.BooleanLiteral() != null) {
            return new QueenBooleanLiteralExpressionNode(
                getPosition(ctx),
                Boolean.parseBoolean(ctx.BooleanLiteral().getText())
            );
        } else if(ctx.CharacterLiteral() != null) {
            return new QueenCharLiteralExpressionNode(
                getPosition(ctx),
                ctx.CharacterLiteral().getText()
            );
        } else if(ctx.StringLiteral() != null) {
            final String withoutQuotes = ctx.StringLiteral().getText()
                .substring(1,  ctx.StringLiteral().getText().length() - 1);
            return new QueenStringLiteralExpressionNode(
                getPosition(ctx),
                withoutQuotes
            );
        } else if(ctx.FloatingPointLiteral() != null) {
            return new QueenDoubleLiteralExpressionNode(
                getPosition(ctx),
                ctx.FloatingPointLiteral().getText()
            );
        } else if(ctx.IntegerLiteral() != null) {
            final String literal = ctx.IntegerLiteral().getText();
            if(literal.endsWith("l") || literal.endsWith("L")) {
                return new QueenLongLiteralExpressionNode(
                    getPosition(ctx),
                    literal
                );
            } else {
                return new QueenIntegerLiteralExpressionNode(
                    getPosition(ctx),
                    literal
                );
            }
        } else {
            return new QueenNullLiteralExpressionNode(getPosition(ctx));
        }
    }

    @Override
    public QueenExpressionNode visitVariableInitializer(QueenParser.VariableInitializerContext ctx) {
        if(ctx.expression() != null) {
            return this.visitExpression(ctx.expression());
        } else {
            return this.visitArrayInitializer(ctx.arrayInitializer());
        }
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
        final String name = asString(ctx.variableDeclaratorId());
        return new QueenParameterNode(
            getPosition(ctx),
            annotations,
            modifiers,
            this.visitUnannType(ctx.unannType()),
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
            this.visitUnannType(ctx.unannType()),
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
        final List<QueenStatementNode> blockStatements = new ArrayList<>();
        ctx.blockStatement().forEach(
            bs -> {
                if(bs.classDeclaration() != null) {
                    blockStatements.add(this.visitClassDeclaration(bs.classDeclaration()));
                }
                if(bs.localVariableDeclarationStatement() != null) {
                    blockStatements.add(
                        this.visitLocalVariableDeclaration(
                            bs.localVariableDeclarationStatement().localVariableDeclaration()
                        )
                    );
                }
                if(bs.statement() != null) {
                    blockStatements.add(
                        this.visitStatement(bs.statement())
                    );
                }
            }
        );
        return new QueenBlockStatements(getPosition(ctx), blockStatements);
    }

    @Override
    public QueenInstanceInitializerNode visitInstanceInitializer(QueenParser.InstanceInitializerContext ctx) {
        final QueenBlockStatements queenBlockStatements;
        if(ctx.block() != null && ctx.block().blockStatements() != null) {
            queenBlockStatements = this.visitBlockStatements(ctx.block().blockStatements());
        } else {
            queenBlockStatements = new QueenBlockStatements(getPosition(ctx));
        }
        return new QueenInstanceInitializerNode(
            getPosition(ctx), queenBlockStatements
        );
    }

    @Override
    public QueenInstanceInitializerNode visitStaticInitializer(QueenParser.StaticInitializerContext ctx) {
        final QueenBlockStatements queenBlockStatements;
        if(ctx.block() != null && ctx.block().blockStatements() != null) {
            queenBlockStatements = this.visitBlockStatements(ctx.block().blockStatements());
        } else {
            queenBlockStatements = new QueenBlockStatements(getPosition(ctx));
        }
        return new QueenInstanceInitializerNode(
            getPosition(ctx),
            queenBlockStatements,
            true
        );
    }

    @Override
    public QueenStatementNode visitStatement(QueenParser.StatementContext ctx) {
        if(ctx.ifThenStatement() != null) {
            return this.visitIfThenStatement(ctx.ifThenStatement());
        } else if(ctx.ifThenElseStatement() != null) {
            return this.visitIfThenElseStatement(ctx.ifThenElseStatement());
        } else if(ctx.whileStatement() != null) {
            return this.visitWhileStatement(ctx.whileStatement());
        } else if(ctx.forStatement() != null && ctx.forStatement().basicForStatement() != null) {
            return this.visitBasicForStatement(ctx.forStatement().basicForStatement());
        } else if(ctx.forStatement() != null && ctx.forStatement().enhancedForStatement() != null) {
            return this.visitEnhancedForStatement(ctx.forStatement().enhancedForStatement());
        } else if(ctx.labeledStatement() != null) {
            return this.visitLabeledStatement(ctx.labeledStatement());
        } else {
            return this.visitStatementWithoutTrailingSubstatement(ctx.statementWithoutTrailingSubstatement());
        }
    }

    @Override
    public QueenStatementNode visitStatementNoShortIf(QueenParser.StatementNoShortIfContext ctx) {
        if(ctx.ifThenElseStatementNoShortIf() != null) {
            return this.visitIfThenElseStatementNoShortIf(ctx.ifThenElseStatementNoShortIf());
        } else if(ctx.whileStatementNoShortIf() != null) {
            return this.visitWhileStatementNoShortIf(ctx.whileStatementNoShortIf());
        } else if(ctx.forStatementNoShortIf() != null && ctx.forStatementNoShortIf().basicForStatementNoShortIf() != null) {
            return this.visitBasicForStatementNoShortIf(ctx.forStatementNoShortIf().basicForStatementNoShortIf());
        } else if(ctx.forStatementNoShortIf() != null && ctx.forStatementNoShortIf().enhancedForStatementNoShortIf() != null) {
            return this.visitEnhancedForStatementNoShortIf(ctx.forStatementNoShortIf().enhancedForStatementNoShortIf());
        } else if(ctx.labeledStatementNoShortIf() != null) {
            return this.visitLabeledStatementNoShortIf(ctx.labeledStatementNoShortIf());
        } else {
            return this.visitStatementWithoutTrailingSubstatement(ctx.statementWithoutTrailingSubstatement());
        }
    }

    @Override
    public QueenIfStatementNode visitIfThenStatement(QueenParser.IfThenStatementContext ctx) {
        final Position position = this.getPosition(ctx);
        final QueenExpressionNode condition = this.visitExpression(ctx.expression());
        final QueenBlockStatements thenBlockStatements;
        if(ctx.statement().statementWithoutTrailingSubstatement() != null) {
            thenBlockStatements = this.visitStatementWithoutTrailingSubstatement(
                ctx.statement().statementWithoutTrailingSubstatement()
            );
        } else {
            thenBlockStatements = new QueenBlockStatements(
                getPosition(ctx.statement()),
                List.of(this.visitStatement(ctx.statement()))
            );
        }
        return new QueenIfStatementNode(
            position,
            condition,
            thenBlockStatements
        );
    }

    @Override
    public QueenIfStatementNode visitIfThenElseStatement(QueenParser.IfThenElseStatementContext ctx) {
        final Position position = this.getPosition(ctx);
        final QueenExpressionNode condition = this.visitExpression(ctx.expression());

        final QueenBlockStatements thenBlockStatements;
        if(ctx.statementNoShortIf().statementWithoutTrailingSubstatement() != null) {
            thenBlockStatements = this.visitStatementWithoutTrailingSubstatement(
                ctx.statementNoShortIf().statementWithoutTrailingSubstatement()
            );
        } else {
            thenBlockStatements = new QueenBlockStatements(
                getPosition(ctx.statementNoShortIf()),
                List.of(this.visitStatementNoShortIf(ctx.statementNoShortIf()))
            );
        }

        final QueenBlockStatements elseBlockStatements;
        if(ctx.statement().statementWithoutTrailingSubstatement() != null) {
            elseBlockStatements = this.visitStatementWithoutTrailingSubstatement(
                ctx.statement().statementWithoutTrailingSubstatement()
            );
        } else {
            elseBlockStatements = new QueenBlockStatements(
                getPosition(ctx.statement()),
                List.of(this.visitStatement(ctx.statement()))
            );
        }
        return new QueenIfStatementNode(
            position,
            condition,
            thenBlockStatements,
            elseBlockStatements
        );
    }

    @Override
    public QueenIfStatementNode visitIfThenElseStatementNoShortIf(QueenParser.IfThenElseStatementNoShortIfContext ctx) {
        final Position position = this.getPosition(ctx);
        final QueenExpressionNode condition = this.visitExpression(ctx.expression());

        QueenBlockStatements thenBlockStatements = null;
        QueenBlockStatements elseBlockStatements = null;
        if(ctx.statementNoShortIf() != null) {
            if(ctx.statementNoShortIf().size() == 1) {
                final QueenParser.StatementNoShortIfContext thenStatementNoShortIfContext = ctx.statementNoShortIf().get(0);
                if(thenStatementNoShortIfContext.statementWithoutTrailingSubstatement() != null) {
                    thenBlockStatements = this.visitStatementWithoutTrailingSubstatement(
                        thenStatementNoShortIfContext.statementWithoutTrailingSubstatement()
                    );
                } else {
                    thenBlockStatements = new QueenBlockStatements(
                        getPosition(thenStatementNoShortIfContext),
                        List.of(this.visitStatementNoShortIf(thenStatementNoShortIfContext))
                    );
                }
            } else if(ctx.statementNoShortIf().size() == 2) {
                final QueenParser.StatementNoShortIfContext thenStatementNoShortIfContext = ctx.statementNoShortIf().get(0);
                if(thenStatementNoShortIfContext.statementWithoutTrailingSubstatement() != null) {
                    thenBlockStatements = this.visitStatementWithoutTrailingSubstatement(
                        thenStatementNoShortIfContext.statementWithoutTrailingSubstatement()
                    );
                } else {
                    thenBlockStatements = new QueenBlockStatements(
                        getPosition(thenStatementNoShortIfContext),
                        List.of(this.visitStatementNoShortIf(thenStatementNoShortIfContext))
                    );
                }
                final QueenParser.StatementNoShortIfContext elseStatementNoShortIfContext = ctx.statementNoShortIf().get(1);
                if(elseStatementNoShortIfContext.statementWithoutTrailingSubstatement() != null) {
                    elseBlockStatements = this.visitStatementWithoutTrailingSubstatement(
                        elseStatementNoShortIfContext.statementWithoutTrailingSubstatement()
                    );
                } else {
                    elseBlockStatements = new QueenBlockStatements(
                        getPosition(elseStatementNoShortIfContext),
                        List.of(this.visitStatementNoShortIf(elseStatementNoShortIfContext))
                    );
                }
            }
        }

        return new QueenIfStatementNode(
            position,
            condition,
            thenBlockStatements,
            elseBlockStatements
        );
    }


    @Override
    public QueenForStatementNode visitBasicForStatement(QueenParser.BasicForStatementContext ctx) {
        final Position position = this.getPosition(ctx);
        final List<QueenExpressionNode> init = new ArrayList<>();
        if(ctx.forInit() != null) {
            if(ctx.forInit().statementExpressionList() != null) {
                ctx.forInit().statementExpressionList().statementExpression().forEach(
                    stmtExpression -> init.add(
                        this.visitStatementExpression(stmtExpression)
                    )
                );
            } else if(ctx.forInit().localVariableDeclaration() != null) {
                init.add(
                    this.visitLocalVariableDeclaration(
                        ctx.forInit().localVariableDeclaration()
                    )
                );
            }
        }

        final QueenExpressionNode condition = this.visitExpression(ctx.expression());
        final List<QueenExpressionNode> update = new ArrayList<>();
        if(ctx.forUpdate() != null) {
            if(ctx.forUpdate().statementExpressionList() != null) {
                ctx.forUpdate().statementExpressionList().statementExpression().forEach(
                    stmtExpression -> update.add(
                        this.visitStatementExpression(stmtExpression)
                    )
                );
            }
        }

        final QueenBlockStatements blockStatements;
        if(ctx.statement().statementWithoutTrailingSubstatement() != null) {
            blockStatements = this.visitStatementWithoutTrailingSubstatement(
                ctx.statement().statementWithoutTrailingSubstatement()
            );
        } else {
            blockStatements = new QueenBlockStatements(
                getPosition(ctx.statement()),
                List.of(this.visitStatement(ctx.statement()))
            );
        }
        return new QueenForStatementNode(
            position,
            init,
            condition,
            update,
            blockStatements
        );
    }

    @Override
    public QueenForStatementNode visitBasicForStatementNoShortIf(QueenParser.BasicForStatementNoShortIfContext ctx) {
        final Position position = this.getPosition(ctx);
        final List<QueenExpressionNode> init = new ArrayList<>();
        if(ctx.forInit() != null) {
            if(ctx.forInit().statementExpressionList() != null) {
                ctx.forInit().statementExpressionList().statementExpression().forEach(
                    stmtExpression -> init.add(
                        this.visitStatementExpression(stmtExpression)
                    )
                );
            } else if(ctx.forInit().localVariableDeclaration() != null) {
                init.add(
                    this.visitLocalVariableDeclaration(
                        ctx.forInit().localVariableDeclaration()
                    )
                );
            }
        }

        final QueenExpressionNode condition = this.visitExpression(ctx.expression());
        final List<QueenExpressionNode> update = new ArrayList<>();
        if(ctx.forUpdate() != null) {
            if(ctx.forUpdate().statementExpressionList() != null) {
                ctx.forUpdate().statementExpressionList().statementExpression().forEach(
                    stmtExpression -> update.add(
                        this.visitStatementExpression(stmtExpression)
                    )
                );
            }
        }

        final QueenBlockStatements blockStatements;
        if(ctx.statementNoShortIf().statementWithoutTrailingSubstatement() != null) {
            blockStatements = this.visitStatementWithoutTrailingSubstatement(
                ctx.statementNoShortIf().statementWithoutTrailingSubstatement()
            );
        } else {
            blockStatements = new QueenBlockStatements(
                getPosition(ctx.statementNoShortIf()),
                List.of(this.visitStatementNoShortIf(ctx.statementNoShortIf()))
            );
        }
        return new QueenForStatementNode(
            position,
            init,
            condition,
            update,
            blockStatements
        );
    }

    @Override
    public QueenForEachStatementNode visitEnhancedForStatement(QueenParser.EnhancedForStatementContext ctx) {
        final Position position = this.getPosition(ctx);

        final QueenLocalVariableDeclarationNode variable;
        final List<QueenAnnotationNode> annotations = new ArrayList<>();
        final List<QueenModifierNode> modifiers = new ArrayList<>();
        if(ctx.variableModifier() != null) {
            ctx.variableModifier().forEach(
                vm -> {
                    if(vm.annotation() != null) {
                        annotations.add(this.visitAnnotation(vm.annotation()));
                    }
                    if(vm.FINAL() != null) {
                        modifiers.add(
                            new QueenModifierNode(
                                this.getPosition(vm),
                                vm.FINAL().getText()
                            )
                        );
                    }
                }
            );
        }
        final Map<String, QueenExpressionNode> variables = new LinkedHashMap<>();
        variables.put(asString(ctx.variableDeclaratorId()), null);
        variable = new QueenLocalVariableDeclarationNode(
            getPosition(ctx),
            annotations,
            modifiers,
            this.visitUnannType(ctx.unannType()),
            variables
        );


        final QueenExpressionNode iterable = this.visitExpression(ctx.expression());
        final QueenBlockStatements blockStatements;
        if(ctx.statement().statementWithoutTrailingSubstatement() != null) {
            blockStatements = this.visitStatementWithoutTrailingSubstatement(
                ctx.statement().statementWithoutTrailingSubstatement()
            );
        } else {
            blockStatements = new QueenBlockStatements(
                getPosition(ctx.statement()),
                List.of(this.visitStatement(ctx.statement()))
            );
        }
        return new QueenForEachStatementNode(
            position,
            variable,
            iterable,
            blockStatements
        );
    }

    @Override
    public QueenForEachStatementNode visitEnhancedForStatementNoShortIf(QueenParser.EnhancedForStatementNoShortIfContext ctx) {
        final Position position = this.getPosition(ctx);

        final QueenLocalVariableDeclarationNode variable;
        final List<QueenAnnotationNode> annotations = new ArrayList<>();
        final List<QueenModifierNode> modifiers = new ArrayList<>();
        if(ctx.variableModifier() != null) {
            ctx.variableModifier().forEach(
                vm -> {
                    if(vm.annotation() != null) {
                        annotations.add(this.visitAnnotation(vm.annotation()));
                    }
                    if(vm.FINAL() != null) {
                        modifiers.add(
                            new QueenModifierNode(
                                this.getPosition(vm),
                                vm.FINAL().getText()
                            )
                        );
                    }
                }
            );
        }
        final Map<String, QueenExpressionNode> variables = new LinkedHashMap<>();
        variables.put(asString(ctx.variableDeclaratorId()), null);
        variable = new QueenLocalVariableDeclarationNode(
            getPosition(ctx),
            annotations,
            modifiers,
            this.visitUnannType(ctx.unannType()),
            variables
        );


        final QueenExpressionNode iterable = this.visitExpression(ctx.expression());
        final QueenBlockStatements blockStatements;
        if(ctx.statementNoShortIf().statementWithoutTrailingSubstatement() != null) {
            blockStatements = this.visitStatementWithoutTrailingSubstatement(
                ctx.statementNoShortIf().statementWithoutTrailingSubstatement()
            );
        } else {
            blockStatements = new QueenBlockStatements(
                getPosition(ctx.statementNoShortIf()),
                List.of(this.visitStatementNoShortIf(ctx.statementNoShortIf()))
            );
        }
        return new QueenForEachStatementNode(
            position,
            variable,
            iterable,
            blockStatements
        );
    }

    @Override
    public QueenWhileStatementNode visitWhileStatement(QueenParser.WhileStatementContext ctx) {
        final Position position = this.getPosition(ctx);
        final QueenExpressionNode expression = this.visitExpression(ctx.expression());
        final QueenBlockStatements blockStatements;
        if(ctx.statement().statementWithoutTrailingSubstatement() != null) {
            blockStatements = this.visitStatementWithoutTrailingSubstatement(
                ctx.statement().statementWithoutTrailingSubstatement()
            );
        } else {
            blockStatements = new QueenBlockStatements(
                getPosition(ctx.statement()),
                List.of(this.visitStatement(ctx.statement()))
            );
        }
        return new QueenWhileStatementNode(
            position,
            expression,
            blockStatements
        );
    }

    @Override
    public QueenWhileStatementNode visitWhileStatementNoShortIf(QueenParser.WhileStatementNoShortIfContext ctx) {
        final Position position = this.getPosition(ctx);
        final QueenExpressionNode expression = this.visitExpression(ctx.expression());
        final QueenBlockStatements blockStatements;
        if(ctx.statementNoShortIf().statementWithoutTrailingSubstatement() != null) {
            blockStatements = this.visitStatementWithoutTrailingSubstatement(
                ctx.statementNoShortIf().statementWithoutTrailingSubstatement()
            );
        } else {
            blockStatements = new QueenBlockStatements(
                getPosition(ctx.statementNoShortIf()),
                List.of(this.visitStatementNoShortIf(ctx.statementNoShortIf()))
            );
        }
        return new QueenWhileStatementNode(
            position,
            expression,
            blockStatements
        );
    }

    @Override
    public QueenDoStatementNode visitDoStatement(QueenParser.DoStatementContext ctx) {
        final Position position = this.getPosition(ctx);
        final QueenExpressionNode expression = this.visitExpression(ctx.expression());
        final QueenBlockStatements blockStatements;
        if(ctx.statement().statementWithoutTrailingSubstatement() != null) {
            blockStatements = this.visitStatementWithoutTrailingSubstatement(
                ctx.statement().statementWithoutTrailingSubstatement()
            );
        } else {
            blockStatements = new QueenBlockStatements(
                getPosition(ctx.statement()),
                List.of(this.visitStatement(ctx.statement()))
            );
        }
        return new QueenDoStatementNode(
            position,
            blockStatements,
            expression
        );
    }

    @Override
    public QueenLabeledStatementNode visitLabeledStatement(QueenParser.LabeledStatementContext ctx) {
        final QueenBlockStatements blockStatements;
        if(ctx.statement().statementWithoutTrailingSubstatement() != null) {
            blockStatements = this.visitStatementWithoutTrailingSubstatement(
                ctx.statement().statementWithoutTrailingSubstatement()
            );
        } else {
            blockStatements = new QueenBlockStatements(
                getPosition(ctx.statement()),
                List.of(this.visitStatement(ctx.statement()))
            );
        }
        return new QueenLabeledStatementNode(
            getPosition(ctx),
            ctx.Identifier().getText(),
            blockStatements
        );
    }

    @Override
    public QueenLabeledStatementNode visitLabeledStatementNoShortIf(QueenParser.LabeledStatementNoShortIfContext ctx) {
        final QueenBlockStatements blockStatements;
        if(ctx.statementNoShortIf().statementWithoutTrailingSubstatement() != null) {
            blockStatements = this.visitStatementWithoutTrailingSubstatement(
                ctx.statementNoShortIf().statementWithoutTrailingSubstatement()
            );
        } else {
            blockStatements = new QueenBlockStatements(
                getPosition(ctx.statementNoShortIf()),
                List.of(this.visitStatementNoShortIf(ctx.statementNoShortIf()))
            );
        }
        return new QueenLabeledStatementNode(
            getPosition(ctx),
            ctx.Identifier().getText(),
            blockStatements
        );
    }

    @Override
    public QueenBlockStatements visitStatementWithoutTrailingSubstatement(
        QueenParser.StatementWithoutTrailingSubstatementContext ctx
    ) {
        final QueenStatementNode statementWithoutTrailingSubstatement;
        if (ctx.block() != null && ctx.block().blockStatements() != null) {
            return visitBlockStatements(
                ctx.block().blockStatements()
            );
        } else if(ctx.throwStatement() != null) {
            statementWithoutTrailingSubstatement = this.visitThrowStatement(ctx.throwStatement());
        } else if(ctx.returnStatement() != null) {
            statementWithoutTrailingSubstatement = this.visitReturnStatement(ctx.returnStatement());
        } else if(ctx.continueStatement() != null) {
            statementWithoutTrailingSubstatement = this.visitContinueStatement(ctx.continueStatement());
        } else if(ctx.breakStatement() != null) {
            statementWithoutTrailingSubstatement = this.visitBreakStatement(ctx.breakStatement());
        } else if(ctx.emptyStatement() != null) {
            statementWithoutTrailingSubstatement = this.visitEmptyStatement(ctx.emptyStatement());
        } else if(ctx.assertStatement() != null) {
            statementWithoutTrailingSubstatement = this.visitAssertStatement(ctx.assertStatement());
        } else if(ctx.expressionStatement() != null) {
            statementWithoutTrailingSubstatement = this.visitExpressionStatement(ctx.expressionStatement());
        } else if(ctx.doStatement() != null) {
            statementWithoutTrailingSubstatement = this.visitDoStatement(ctx.doStatement());
        } else if(ctx.tryStatement() != null) {
            statementWithoutTrailingSubstatement = this.visitTryStatement(ctx.tryStatement());
        } else if(ctx.synchronizedStatement() != null) {
            statementWithoutTrailingSubstatement = this.visitSynchronizedStatement(ctx.synchronizedStatement());
        } else if(ctx.switchStatement() != null) {
            statementWithoutTrailingSubstatement = this.visitSwitchStatement(ctx.switchStatement());
        } else {
            statementWithoutTrailingSubstatement = null;
        }
        return new QueenBlockStatements(
            getPosition(ctx),
            statementWithoutTrailingSubstatement != null
                ? List.of(statementWithoutTrailingSubstatement)
                : new ArrayList<>()
        );
    }

    @Override
    public QueenThrowStatementNode visitThrowStatement(QueenParser.ThrowStatementContext ctx) {
        return new QueenThrowStatementNode(
            getPosition(ctx),
            this.visitExpression(ctx.expression())
        );
    }

    @Override
    public QueenReturnStatementNode visitReturnStatement(QueenParser.ReturnStatementContext ctx) {
        return new QueenReturnStatementNode(
            getPosition(ctx),
            this.visitExpression(ctx.expression())
        );
    }

    @Override
    public QueenContinueStatementNode visitContinueStatement(QueenParser.ContinueStatementContext ctx) {
        return new QueenContinueStatementNode(
            getPosition(ctx),
            ctx.Identifier() != null ? ctx.Identifier().getText() : null
        );
    }

    @Override
    public QueenBreakStatementNode visitBreakStatement(QueenParser.BreakStatementContext ctx) {
        return new QueenBreakStatementNode(
            getPosition(ctx),
            ctx.Identifier() != null ? ctx.Identifier().getText() : null
        );
    }

    @Override
    public QueenEmptyStatementNode visitEmptyStatement(QueenParser.EmptyStatementContext ctx) {
        return new QueenEmptyStatementNode(
            getPosition(ctx)
        );
    }

    @Override
    public QueenAssertStatementNode visitAssertStatement(QueenParser.AssertStatementContext ctx) {
        if(ctx.expression().size() < 2) {
            return new QueenAssertStatementNode(
                getPosition(ctx),
                this.visitExpression(ctx.expression(0))
            );
        } else {
            return new QueenAssertStatementNode(
                getPosition(ctx),
                this.visitExpression(ctx.expression(0)),
                this.visitExpression(ctx.expression(1))
            );
        }
    }

    @Override
    public QueenSynchronizedStatementNode visitSynchronizedStatement(QueenParser.SynchronizedStatementContext ctx) {
        final Position position = getPosition(ctx);
        final QueenExpressionNode expression = this.visitExpression(ctx.expression());
        final QueenBlockStatements blockStatements;
        if(ctx.block() != null && ctx.block().blockStatements() != null) {
            blockStatements = this.visitBlockStatements(ctx.block().blockStatements());
        } else {
            blockStatements = new QueenBlockStatements(getPosition(ctx));
        }
        return new QueenSynchronizedStatementNode(
            position,
            expression,
            blockStatements
        );
    }

    @Override
    public QueenSwitchStatementNode visitSwitchStatement(QueenParser.SwitchStatementContext ctx) {
        final Position position = getPosition(ctx);
        final QueenExpressionNode expression = this.visitExpression(ctx.expression());
        final List<QueenSwitchEntryNode> entries = new ArrayList<>();

        if(ctx.switchBlock() != null) {
            if(ctx.switchBlock().switchBlockStatementGroup() != null) {
                ctx.switchBlock().switchBlockStatementGroup().forEach(
                    sbsg -> {
                        final Position p = getPosition(sbsg);
                        final List<QueenSwitchLabelNode> labels = new ArrayList<>();
                        final QueenBlockStatements blockStatements;
                        if(sbsg.blockStatements() != null) {
                            blockStatements = this.visitBlockStatements(sbsg.blockStatements());
                        } else {
                            blockStatements = null;
                        }
                        sbsg.switchLabels().switchLabel().forEach(
                            (sl) -> labels.add(
                                new QueenSwitchLabelNode(
                                    getPosition(sl),
                                    sl.DEFAULT() != null ? null :
                                        this.visitConstantExpression(sl.constantExpression()),
                                    sl.DEFAULT() != null
                                )
                            )
                        );
                        entries.add(
                            new QueenSwitchEntryNode(
                                p,
                                labels,
                                blockStatements
                            )
                        );
                    }
                );
            } else if(ctx.switchBlock().switchLabel() != null) {
                final List<QueenSwitchLabelNode> labels = new ArrayList<>();
                ctx.switchBlock().switchLabel().forEach(
                    (sl) -> labels.add(
                        new QueenSwitchLabelNode(
                            getPosition(sl),
                            sl.DEFAULT() != null ? null :
                                this.visitConstantExpression(sl.constantExpression()),
                            sl.DEFAULT() != null
                        )
                    )
                );
                entries.add(
                    new QueenSwitchEntryNode(
                        getPosition(ctx.switchBlock()),
                        labels,
                        null
                    )
                );
            }
        }

        return new QueenSwitchStatementNode(
            position,
            expression,
            entries
        );
    }

    @Override
    public QueenTryStatementNode visitTryStatement(QueenParser.TryStatementContext ctx) {
        if(ctx.tryWithResourcesStatement() != null) {
            return this.visitTryWithResourcesStatement(ctx.tryWithResourcesStatement());
        } else {
            final Position position = getPosition(ctx);
            final QueenBlockStatements tryBlockStatements;
            if(ctx.block() != null && ctx.block().blockStatements() != null) {
                tryBlockStatements = this.visitBlockStatements(ctx.block().blockStatements());
            } else {
                tryBlockStatements = new QueenBlockStatements(getPosition(ctx));
            }
            final QueenBlockStatements finallyBlockStatements;
            if(ctx.finally_() != null) {
                finallyBlockStatements = this.visitFinally_(ctx.finally_());
            } else {
                finallyBlockStatements = null;
            }
            final List<QueenCatchClauseNode> catchClauses = new ArrayList<>();
            if(ctx.catches() != null) {
                ctx.catches().catchClause().forEach(
                    cc -> catchClauses.add(this.visitCatchClause(cc))
                );
            }
            return new QueenTryStatementNode(
                position,
                new ArrayList<>(),
                tryBlockStatements,
                catchClauses,
                finallyBlockStatements
            );
        }
    }

    @Override
    public QueenTryStatementNode visitTryWithResourcesStatement(QueenParser.TryWithResourcesStatementContext ctx) {
        final Position position = getPosition(ctx);
        final List<QueenExpressionNode> resources = new ArrayList<>();
        ctx.resourceSpecification().resourceList().resource().forEach(
            r -> resources.add(this.visitResource(r))
        );
        final QueenBlockStatements tryBlockStatements;
        if(ctx.block() != null && ctx.block().blockStatements() != null) {
            tryBlockStatements = this.visitBlockStatements(ctx.block().blockStatements());
        } else {
            tryBlockStatements = new QueenBlockStatements(getPosition(ctx));
        }
        final QueenBlockStatements finallyBlockStatements = this.visitFinally_(ctx.finally_());
        final List<QueenCatchClauseNode> catchClauses = new ArrayList<>();
        if(ctx.catches() != null) {
            ctx.catches().catchClause().forEach(
                cc -> catchClauses.add(this.visitCatchClause(cc))
            );
        }
        return new QueenTryStatementNode(
            position,
            resources,
            tryBlockStatements,
            catchClauses,
            finallyBlockStatements
        );
    }

    @Override
    public QueenBlockStatements visitFinally_(QueenParser.Finally_Context ctx) {
        final QueenBlockStatements finallyBlockStatements;
        if(ctx != null && ctx.block() != null && ctx.block().blockStatements() != null) {
            finallyBlockStatements = this.visitBlockStatements(ctx.block().blockStatements());
        } else {
            finallyBlockStatements = new QueenBlockStatements(getPosition(ctx));
        }
        return finallyBlockStatements;
    }

    @Override
    public QueenExpressionNode visitResource(QueenParser.ResourceContext ctx) {
        final Position position = getPosition(ctx);
        final List<QueenAnnotationNode> annotations = new ArrayList<>();
        final List<QueenModifierNode> modifiers = new ArrayList<>();
        if(ctx.variableModifier() != null) {
            ctx.variableModifier().forEach(
                vm -> {
                    if(vm.annotation() != null) {
                        annotations.add(this.visitAnnotation(vm.annotation()));
                    }
                    if(vm.FINAL() != null) {
                        modifiers.add(
                            new QueenModifierNode(
                                this.getPosition(vm),
                                vm.FINAL().getText()
                            )
                        );
                    }
                }
            );
        }

        return new QueenLocalVariableDeclarationNode(
            position,
            annotations,
            modifiers,
            this.visitUnannType(ctx.unannType()),
            Map.of(
                asString(ctx.variableDeclaratorId()),
                this.visitExpression(ctx.expression())
            )
        );
    }

    @Override
    public QueenCatchClauseNode visitCatchClause(QueenParser.CatchClauseContext ctx) {
        final Position position = getPosition(ctx);
        final QueenCatchFormalParameterNode parameter = this.visitCatchFormalParameter(ctx.catchFormalParameter());
        final QueenBlockStatements catchBlockStatements;
        if(ctx.block() != null && ctx.block().blockStatements() != null) {
            catchBlockStatements = this.visitBlockStatements(ctx.block().blockStatements());
        } else {
            catchBlockStatements = new QueenBlockStatements(getPosition(ctx));
        }
        return new QueenCatchClauseNode(
            position,
            parameter,
            catchBlockStatements
        );
    }

    @Override
    public QueenCatchFormalParameterNode visitCatchFormalParameter(QueenParser.CatchFormalParameterContext ctx) {
        final Position position = getPosition(ctx);
        final List<QueenAnnotationNode> annotations = new ArrayList<>();
        final List<QueenModifierNode> modifiers = new ArrayList<>();
        if(ctx.variableModifier() != null) {
           ctx.variableModifier().forEach(
               vm -> {
                   if(vm.annotation() != null) {
                       annotations.add(this.visitAnnotation(vm.annotation()));
                   }
                   if (vm.FINAL() != null) {
                       modifiers.add(
                           new QueenModifierNode(getPosition(vm), vm.FINAL().getText())
                       );
                   }
               }
           );
        }
        final List<QueenTypeNode> catchTypes = new ArrayList<>();
        ctx.catchType().unannClassType().forEach(
            ct -> catchTypes.add(this.visitUnannClassType(ct))
        );
        return new QueenCatchFormalParameterNode(
            position,
            annotations,
            modifiers,
            catchTypes,
            asString(ctx.variableDeclaratorId())
        );
    }

    @Override
    public QueenStatementNode visitExpressionStatement(QueenParser.ExpressionStatementContext ctx) {
        return new QueenExpressionStatementNode(
            getPosition(ctx),
            this.visitStatementExpression(ctx.statementExpression())
        );
    }

    @Override
    public QueenExpressionNode visitStatementExpression(QueenParser.StatementExpressionContext ctx) {
        if(ctx.assignment() != null) {
            return this.visitAssignment(ctx.assignment());
        } else if(ctx.preIncrementExpression() != null) {
            return this.visitPreIncrementExpression(ctx.preIncrementExpression());
        } else if(ctx.preDecrementExpression() != null) {
            return this.visitPreDecrementExpression(ctx.preDecrementExpression());
        } else if(ctx.postIncrementExpression() != null) {
            return this.visitPostIncrementExpression(ctx.postIncrementExpression());
        } else if(ctx.postDecrementExpression() != null) {
            return this.visitPostDecrementExpression(ctx.postDecrementExpression());
        } else if(ctx.methodInvocation() != null) {
            return this.visitMethodInvocation(ctx.methodInvocation());
        } else {
            return this.visitClassInstanceCreationExpression(ctx.classInstanceCreationExpression());
        }
    }

    @Override
    public QueenExpressionNode visitAssignment(QueenParser.AssignmentContext ctx) {
        final Position position = getPosition(ctx);
        final QueenExpressionNode target = this.visitLeftHandSide(ctx.leftHandSide());
        final String operator = asString(ctx.assignmentOperator()).trim().toUpperCase();
        final QueenExpressionNode value = this.visitExpression(ctx.expression());
        return new QueenAssignmentExpressionNode(
            position,
            target,
            operator,
            value
        );
    }

    @Override
    public QueenExpressionNode visitLeftHandSide(QueenParser.LeftHandSideContext ctx) {
        if(ctx.expressionName() != null) {
            return this.visitExpressionName(ctx.expressionName());
        } else if(ctx.arrayAccess() != null) {
            return this.visitArrayAccess(ctx.arrayAccess());
        } else {
            return this.visitFieldAccess(ctx.fieldAccess());
        }
    }

    @Override
    public QueenExpressionNode visitMethodInvocation(final QueenParser.MethodInvocationContext ctx) {
        final Position position = getPosition(ctx);
        final String name;
        if(ctx.methodName() != null) {
            name = ctx.methodName().Identifier().getText();
        } else {
            name = ctx.Identifier().getText();
        }
        final List<QueenTypeNode> typeArguments = new ArrayList<>();
        if(ctx.typeArguments() != null) {
            ctx.typeArguments().typeArgumentList().typeArgument()
                .forEach(ta -> typeArguments.add(this.visitTypeArgument(ta)));
        }
        final List<QueenExpressionNode> arguments = new ArrayList<>();
        if(ctx.argumentList() != null) {
            ctx.argumentList().expression().forEach(
                e -> arguments.add(this.visitExpression(e))
            );
        }
        final QueenExpressionNode scope;
        if(ctx.SUPER() != null && ctx.typeName() != null) {
            scope = new QueenSuperExpressionNode(
                getPosition(ctx.typeName()),
                this.visitTypeName(ctx.typeName())
            );
        } else if(ctx.SUPER() != null) {
            scope = new QueenSuperExpressionNode(
                getPosition(ctx)
            );
        } else if(ctx.primary() != null) {
            scope = this.visitPrimary(ctx.primary());
        } else if(ctx.expressionName() != null) {
            scope = this.visitExpressionName(ctx.expressionName());
        } else if(ctx.typeName() != null) {
            scope = this.visitTypeName(ctx.typeName());
        } else {
            scope = null;
        }
        return new QueenMethodInvocationExpressionNode(
            position,
            scope,
            typeArguments,
            name,
            arguments
        );
    }

    public QueenExpressionNode visitMethodInvocation_lf_primary(QueenExpressionNode scope, QueenParser.MethodInvocation_lf_primaryContext ctx) {
        final Position position = getPosition(ctx);
        final String name = ctx.Identifier().getText();
        final List<QueenTypeNode> typeArguments = new ArrayList<>();
        if(ctx.typeArguments() != null) {
            ctx.typeArguments().typeArgumentList().typeArgument()
                .forEach(ta -> typeArguments.add(this.visitTypeArgument(ta)));
        }
        final List<QueenExpressionNode> arguments = new ArrayList<>();
        if(ctx.argumentList() != null) {
            ctx.argumentList().expression().forEach(
                e -> arguments.add(this.visitExpression(e))
            );
        }
        return new QueenMethodInvocationExpressionNode(
            position,
            scope,
            typeArguments,
            name,
            arguments
        );
    }

    @Override
    public QueenExpressionNode visitMethodInvocation_lfno_primary(final QueenParser.MethodInvocation_lfno_primaryContext ctx) {
        final Position position = getPosition(ctx);
        final String name;
        if(ctx.methodName() != null) {
            name = ctx.methodName().Identifier().getText();
        } else {
            name = ctx.Identifier().getText();
        }
        final List<QueenTypeNode> typeArguments = new ArrayList<>();
        if(ctx.typeArguments() != null) {
            ctx.typeArguments().typeArgumentList().typeArgument()
                .forEach(ta -> typeArguments.add(this.visitTypeArgument(ta)));
        }
        final List<QueenExpressionNode> arguments = new ArrayList<>();
        if(ctx.argumentList() != null) {
            ctx.argumentList().expression().forEach(
                e -> arguments.add(this.visitExpression(e))
            );
        }
        final QueenExpressionNode scope;
        if(ctx.SUPER() != null && ctx.typeName() != null) {
            scope = new QueenSuperExpressionNode(
                getPosition(ctx.typeName()),
                this.visitTypeName(ctx.typeName())
            );
        } else if(ctx.SUPER() != null) {
            scope = new QueenSuperExpressionNode(
                getPosition(ctx)
            );
        } else if(ctx.expressionName() != null) {
            scope = this.visitExpressionName(ctx.expressionName());
        } else if(ctx.typeName() != null) {
            scope = this.visitTypeName(ctx.typeName());
        } else {
            scope = null;
        }
        return new QueenMethodInvocationExpressionNode(
            position,
            scope,
            typeArguments,
            name,
            arguments
        );
    }

    @Override
    public QueenExpressionNode visitMethodReference(QueenParser.MethodReferenceContext ctx) {
        final Position position = getPosition(ctx);
        final List<QueenTypeNode> typeArguments = new ArrayList<>();
        if (ctx.typeArguments() != null) {
            ctx.typeArguments().typeArgumentList().typeArgument()
                .forEach(ta -> typeArguments.add(this.visitTypeArgument(ta)));
        }
        final String identifier;
        if(ctx.Identifier() != null) {
            identifier = ctx.Identifier().getText();
        } else {
            identifier = ctx.NEW().getText();
        }
        final QueenExpressionNode scope;
        if(ctx.expressionName() != null) {
            scope = this.visitExpressionName(ctx.expressionName());
        } else if(ctx.primary() != null) {
            scope = this.visitPrimary(ctx.primary());
        } else if(ctx.typeName() != null && ctx.SUPER() != null) {
            scope = new QueenSuperExpressionNode(
                getPosition(ctx.typeName()),
                this.visitTypeName(ctx.typeName())
            );
        } else if(ctx.SUPER() != null) {
            scope = new QueenSuperExpressionNode(
                getPosition(ctx)
            );
        } else {
            scope = null;
        }
        final QueenTypeNode type;
        if(ctx.referenceType() != null) {
            type = this.visitReferenceType(ctx.referenceType());
        } else if(ctx.classType() != null) {
            type = this.visitClassType(ctx.classType());
        } else if(ctx.arrayType() != null) {
            type = this.visitArrayType(ctx.arrayType());
        } else {
            type = null;
        }
        return new QueenMethodReferenceExpressionNode(
            position,
            type,
            scope,
            typeArguments,
            identifier
        );
    }

    public QueenExpressionNode visitMethodReference_lf_primary(QueenExpressionNode scope, QueenParser.MethodReference_lf_primaryContext ctx) {
        final Position position = getPosition(ctx);
        final List<QueenTypeNode> typeArguments = new ArrayList<>();
        if (ctx.typeArguments() != null) {
            ctx.typeArguments().typeArgumentList().typeArgument()
                .forEach(ta -> typeArguments.add(this.visitTypeArgument(ta)));
        }
        final String identifier = ctx.Identifier().getText();
        return new QueenMethodReferenceExpressionNode(
            position,
            null,
            scope,
            typeArguments,
            identifier
        );
    }

    @Override
    public QueenExpressionNode visitMethodReference_lfno_primary(QueenParser.MethodReference_lfno_primaryContext ctx) {
        final Position position = getPosition(ctx);
        final List<QueenTypeNode> typeArguments = new ArrayList<>();
        if (ctx.typeArguments() != null) {
            ctx.typeArguments().typeArgumentList().typeArgument()
                .forEach(ta -> typeArguments.add(this.visitTypeArgument(ta)));
        }
        final String identifier;
        if(ctx.Identifier() != null) {
            identifier = ctx.Identifier().getText();
        } else {
            identifier = ctx.NEW().getText();
        }
        final QueenExpressionNode scope;
        if(ctx.expressionName() != null) {
            scope = this.visitExpressionName(ctx.expressionName());
        } else if(ctx.typeName() != null && ctx.SUPER() != null) {
            scope = new QueenSuperExpressionNode(
                getPosition(ctx.typeName()),
                this.visitTypeName(ctx.typeName())
            );
        } else if(ctx.SUPER() != null) {
            scope = new QueenSuperExpressionNode(
                getPosition(ctx)
            );
        } else {
            scope = null;
        }
        final QueenTypeNode type;
        if(ctx.referenceType() != null) {
            type = this.visitReferenceType(ctx.referenceType());
        } else if(ctx.classType() != null) {
            type = this.visitClassType(ctx.classType());
        } else if(ctx.arrayType() != null) {
            type = this.visitArrayType(ctx.arrayType());
        } else {
            type = null;
        }
        return new QueenMethodReferenceExpressionNode(
            position,
            type,
            scope,
            typeArguments,
            identifier
        );
    }

    @Override
    public QueenExpressionNode visitClassInstanceCreationExpression(QueenParser.ClassInstanceCreationExpressionContext ctx) {
        final Position position = getPosition(ctx);
        final List<QueenTypeNode> typeArguments = new ArrayList<>();
        if (ctx.typeArguments() != null) {
            ctx.typeArguments().typeArgumentList().typeArgument()
                .forEach(ta -> typeArguments.add(this.visitTypeArgument(ta)));
        }
        QueenExpressionNode scope = null;
        if (ctx.primary() != null) {
            scope = this.visitPrimary(ctx.primary());
        } else if (ctx.expressionName() != null) {
            scope = this.visitExpressionName(ctx.expressionName());
        }

        QueenClassOrInterfaceTypeNode type = null;
        for (int i=0; i < ctx.constructorIdentifier().size(); i++) {
            type = this.visitConstructorIdentifier(
                ctx.constructorIdentifier(i),
                i == ctx.constructorIdentifier().size() - 1 ? ctx.typeArgumentsOrDiamond() : null,
                type
            );
        }
        final List<QueenExpressionNode> arguments = new ArrayList<>();
        if(ctx.argumentList() != null) {
            ctx.argumentList().expression().forEach(
                e -> arguments.add(this.visitExpression(e))
            );
        }
        final QueenClassBodyNode classBody;
        if(ctx.classBody() != null) {
            classBody = new QueenClassBodyNode(
                getPosition(ctx.classBody()),
                ctx.classBody().classBodyDeclaration().stream().map(
                    this::visitClassBodyDeclaration
                ).collect(Collectors.toList())
            );
        } else {
            classBody = null;
        }

        return new QueenObjectCreationExpressionNode(
            position,
            scope,
            type,
            typeArguments,
            arguments,
            classBody
        );
    }

    public QueenExpressionNode visitClassInstanceCreationExpression_lf_primary(QueenExpressionNode scope, QueenParser.ClassInstanceCreationExpression_lf_primaryContext ctx) {
        final Position position = getPosition(ctx);
        final List<QueenTypeNode> typeArguments = new ArrayList<>();
        if (ctx.typeArguments() != null) {
            ctx.typeArguments().typeArgumentList().typeArgument()
                .forEach(ta -> typeArguments.add(this.visitTypeArgument(ta)));
        }

        QueenClassOrInterfaceTypeNode type = this.visitConstructorIdentifier(
            ctx.constructorIdentifier(),
            ctx.typeArgumentsOrDiamond(),
            null
        );
        final List<QueenExpressionNode> arguments = new ArrayList<>();
        if(ctx.argumentList() != null) {
            ctx.argumentList().expression().forEach(
                e -> arguments.add(this.visitExpression(e))
            );
        }
        final QueenClassBodyNode classBody;
        if(ctx.classBody() != null) {
            classBody = new QueenClassBodyNode(
                getPosition(ctx.classBody()),
                ctx.classBody().classBodyDeclaration().stream().map(
                    this::visitClassBodyDeclaration
                ).collect(Collectors.toList())
            );
        } else {
            classBody = null;
        }

        return new QueenObjectCreationExpressionNode(
            position,
            scope,
            type,
            typeArguments,
            arguments,
            classBody
        );
    }

    @Override
    public QueenExpressionNode visitClassInstanceCreationExpression_lfno_primary(QueenParser.ClassInstanceCreationExpression_lfno_primaryContext ctx) {
        final Position position = getPosition(ctx);
        final List<QueenTypeNode> typeArguments = new ArrayList<>();
        if (ctx.typeArguments() != null) {
            ctx.typeArguments().typeArgumentList().typeArgument()
                .forEach(ta -> typeArguments.add(this.visitTypeArgument(ta)));
        }
        QueenExpressionNode scope = null;
        if (ctx.expressionName() != null) {
            scope = this.visitExpressionName(ctx.expressionName());
        }

        QueenClassOrInterfaceTypeNode type = null;
        for (int i=0; i < ctx.constructorIdentifier().size(); i++) {
            type = this.visitConstructorIdentifier(
                ctx.constructorIdentifier(i),
                i == ctx.constructorIdentifier().size() - 1 ? ctx.typeArgumentsOrDiamond() : null,
                type
            );
        }
        final List<QueenExpressionNode> arguments = new ArrayList<>();
        if(ctx.argumentList() != null) {
            ctx.argumentList().expression().forEach(
                e -> arguments.add(this.visitExpression(e))
            );
        }
        final QueenClassBodyNode classBody;
        if(ctx.classBody() != null) {
            classBody = new QueenClassBodyNode(
                getPosition(ctx.classBody()),
                ctx.classBody().classBodyDeclaration().stream().map(
                    this::visitClassBodyDeclaration
                ).collect(Collectors.toList())
            );
        } else {
            classBody = null;
        }

        return new QueenObjectCreationExpressionNode(
            position,
            scope,
            type,
            typeArguments,
            arguments,
            classBody
        );
    }

    public QueenClassOrInterfaceTypeNode visitConstructorIdentifier(
        QueenParser.ConstructorIdentifierContext ctx,
        QueenParser.TypeArgumentsOrDiamondContext typeArgsOrDiamondCtx,
        QueenClassOrInterfaceTypeNode scope
    ) {
        final Position position = this.getPosition(ctx);
        final List<QueenAnnotationNode> annotations = new ArrayList<>();
        ctx.annotation().forEach(
            a -> annotations.add(this.visitAnnotation(a))
        );
        final String name = ctx.Identifier().getText();
        final List<QueenTypeNode> typeArguments = new ArrayList<>();
        final boolean isDiamondOperator;
        if(typeArgsOrDiamondCtx != null) {
            if(typeArgsOrDiamondCtx.typeArguments() != null && typeArgsOrDiamondCtx.typeArguments().typeArgumentList() != null) {
                isDiamondOperator = false;
                typeArgsOrDiamondCtx.typeArguments().typeArgumentList().typeArgument().forEach(
                    ta -> typeArguments.add(this.visitTypeArgument(ta))
                );
            } else {
                isDiamondOperator = true;
            }
        } else {
            isDiamondOperator = false;
        }
        return new QueenClassOrInterfaceTypeNode(
            position,
            false,
            scope,
            annotations,
            name,
            typeArguments,
            isDiamondOperator
        );
    }

    @Override
    public QueenExpressionNode visitArrayAccess(QueenParser.ArrayAccessContext ctx) {
        final QueenExpressionNode name;
        if(ctx.expressionName() != null) {
            name = this.visitExpressionName(ctx.expressionName());
        } else {
            name = this.visitPrimaryNoNewArray_lfno_arrayAccess(ctx.primaryNoNewArray_lfno_arrayAccess());
        }
        final List<QueenArrayDimensionNode> dims = new ArrayList<>();
        ctx.expression().forEach(
            e -> dims.add(
                new QueenArrayDimensionNode(
                    getPosition(e),
                    this.visitExpression(e)
                )
            )
        );
        return new QueenArrayAccessExpressionNode(
            getPosition(ctx),
            name,
            dims
        );
    }

    public QueenExpressionNode visitArrayAccess_lf_primary(QueenExpressionNode scope, QueenParser.ArrayAccess_lf_primaryContext ctx) {
        final QueenExpressionNode name = this.visitPrimaryNoNewArray_lf_primary_lfno_arrayAccess_lf_primary(
            scope, ctx.primaryNoNewArray_lf_primary_lfno_arrayAccess_lf_primary()
        );
        final List<QueenArrayDimensionNode> dims = new ArrayList<>();
        ctx.expression().forEach(
            e -> dims.add(
                new QueenArrayDimensionNode(
                    getPosition(e),
                    this.visitExpression(e)
                )
            )
        );
        return new QueenArrayAccessExpressionNode(
            getPosition(ctx),
            name,
            dims
        );
    }

    @Override
    public QueenExpressionNode visitArrayAccess_lfno_primary(QueenParser.ArrayAccess_lfno_primaryContext ctx) {
        final QueenExpressionNode name;
        if(ctx.expressionName() != null) {
            name = this.visitExpressionName(ctx.expressionName());
        } else {
            name = this.visitPrimaryNoNewArray_lfno_primary_lfno_arrayAccess_lfno_primary(
                ctx.primaryNoNewArray_lfno_primary_lfno_arrayAccess_lfno_primary()
            );
        }
        final List<QueenArrayDimensionNode> dims = new ArrayList<>();
        ctx.expression().forEach(
            e -> dims.add(
                new QueenArrayDimensionNode(
                    getPosition(e),
                    this.visitExpression(e)
                )
            )
        );
        return new QueenArrayAccessExpressionNode(
            getPosition(ctx),
            name,
            dims
        );
    }

    public QueenExpressionNode visitPrimaryNoNewArray_lf_primary_lfno_arrayAccess_lf_primary(QueenExpressionNode scope, QueenParser.PrimaryNoNewArray_lf_primary_lfno_arrayAccess_lf_primaryContext ctx) {
        if(ctx.classInstanceCreationExpression_lf_primary() != null) {
            return this.visitClassInstanceCreationExpression_lf_primary(
                scope,
                ctx.classInstanceCreationExpression_lf_primary()
            );
        } else if(ctx.fieldAccess_lf_primary() != null) {
            return this.visitFieldAccess_lf_primary(
                scope,
                ctx.fieldAccess_lf_primary()
            );
        } else if(ctx.methodInvocation_lf_primary() != null) {
            return this.visitMethodInvocation_lf_primary(
                scope,
                ctx.methodInvocation_lf_primary()
            );
        } else {
            return this.visitMethodReference_lf_primary(
                scope,
                ctx.methodReference_lf_primary()
            );
        }
    }

    @Override
    public QueenExpressionNode visitPrimaryNoNewArray_lfno_primary_lfno_arrayAccess_lfno_primary(QueenParser.PrimaryNoNewArray_lfno_primary_lfno_arrayAccess_lfno_primaryContext ctx) {
        if(ctx.literal() != null) {
            return this.visitLiteral(ctx.literal());
        } else if(ctx.typeName() != null && ctx.IMPLEMENTATION() != null) {
            final QueenNameNode typeName = this.visitTypeName(ctx.typeName());
            final List<QueenArrayDimensionNode> dims = new ArrayList<>();
            if(ctx.unannDim() != null) {
                ctx.unannDim().forEach(
                    dim -> dims.add(
                        new QueenArrayDimensionNode(getPosition(dim))
                    )
                );
            }
            return new QueenTypeImplementationExpressionNode(
                getPosition(ctx.typeName()),
                typeName,
                dims
            );
        } else if(ctx.unannPrimitiveType() != null && ctx.IMPLEMENTATION() != null) {
            final QueenPrimitiveTypeNode primitiveType = this.visitUnannPrimitiveType(ctx.unannPrimitiveType());
            final List<QueenArrayDimensionNode> dims = new ArrayList<>();
            if(ctx.unannDim() != null) {
                ctx.unannDim().forEach(
                    dim -> dims.add(
                        new QueenArrayDimensionNode(getPosition(dim))
                    )
                );
            }
            return new QueenTypeImplementationExpressionNode(
                getPosition(ctx.unannPrimitiveType()),
                primitiveType,
                dims
            );
        } else if(ctx.VOID() != null && ctx.IMPLEMENTATION() != null) {
            return new QueenTypeImplementationExpressionNode(
                getPosition(ctx),
                new QueenVoidNode(getPosition(ctx)),
                new ArrayList<>()
            );
        } else if(ctx.THIS() != null) {
            if(ctx.typeName() != null) {
                return new QueenThisExpressionNode(
                    getPosition(ctx),
                    this.visitTypeName(ctx.typeName())
                );
            } else {
                return new QueenThisExpressionNode(
                    getPosition(ctx)
                );
            }
        } else if(ctx.LPAREN() != null && ctx.expression() != null && ctx.RPAREN() != null) {
            return new QueenBracketedExpressionNode(
                getPosition(ctx.expression()),
                this.visitExpression(ctx.expression())
            );
        } else if(ctx.classInstanceCreationExpression_lfno_primary() != null) {
            return this.visitClassInstanceCreationExpression_lfno_primary(ctx.classInstanceCreationExpression_lfno_primary());
        } else if(ctx.fieldAccess_lfno_primary() != null) {
            return this.visitFieldAccess_lfno_primary(ctx.fieldAccess_lfno_primary());
        } else if(ctx.methodInvocation_lfno_primary() != null) {
            return this.visitMethodInvocation_lfno_primary(ctx.methodInvocation_lfno_primary());
        } else {
            return this.visitMethodReference_lfno_primary(ctx.methodReference_lfno_primary());
        }
    }

    @Override
    public QueenExpressionNode visitFieldAccess(QueenParser.FieldAccessContext ctx) {
        final Position position = getPosition(ctx);
        final QueenExpressionNode scope;
        if(ctx.primary() != null) {
            scope = this.visitPrimary(ctx.primary());
        } else if(ctx.typeName() != null) {
            scope = new QueenSuperExpressionNode(
                getPosition(ctx.typeName()),
                this.visitTypeName(ctx.typeName())
            );
        } else {
            scope = new QueenSuperExpressionNode(
                getPosition(ctx)
            );
        }
        return new QueenFieldAccessExpressionNode(
            position,
            scope,
            ctx.Identifier().getText()
        );
    }

    public QueenExpressionNode visitFieldAccess_lf_primary(QueenExpressionNode scope, QueenParser.FieldAccess_lf_primaryContext ctx) {
        return new QueenFieldAccessExpressionNode(
            getPosition(ctx),
            scope,
            ctx.Identifier().getText()
        );
    }

    @Override
    public QueenExpressionNode visitFieldAccess_lfno_primary(QueenParser.FieldAccess_lfno_primaryContext ctx) {
        final Position position = getPosition(ctx);
        final QueenExpressionNode scope;
        if(ctx.typeName() != null) {
            scope = new QueenSuperExpressionNode(
                getPosition(ctx.typeName()),
                this.visitTypeName(ctx.typeName())
            );
        } else {
            scope = new QueenSuperExpressionNode(
                getPosition(ctx)
            );
        }
        return new QueenFieldAccessExpressionNode(
            position,
            scope,
            ctx.Identifier().getText()
        );
    }

    @Override
    public QueenExpressionNode visitUnaryExpression(QueenParser.UnaryExpressionContext ctx) {
        if(ctx.preIncrementExpression() != null) {
            return this.visitPreIncrementExpression(ctx.preIncrementExpression());
        } else if (ctx.preDecrementExpression() != null) {
            return this.visitPreDecrementExpression(ctx.preDecrementExpression());
        } else if(ctx.ADD() != null){
            return new QueenUnaryExpressionNode(
                getPosition(ctx),
                ctx.ADD().getText(),
                true,
                this.visitUnaryExpression(ctx.unaryExpression())
            );
        } else if(ctx.SUB() != null){
            return new QueenUnaryExpressionNode(
                getPosition(ctx),
                ctx.SUB().getText(),
                true,
                this.visitUnaryExpression(ctx.unaryExpression())
            );
        } else {
            return this.visitUnaryExpressionNotPlusMinus(ctx.unaryExpressionNotPlusMinus());
        }
    }

    @Override
    public QueenExpressionNode visitMultiplicativeExpression(QueenParser.MultiplicativeExpressionContext ctx) {
        if(ctx.MUL() != null) {
            return new QueenBinaryExpressionNode(
                getPosition(ctx),
                this.visitMultiplicativeExpression(ctx.multiplicativeExpression()),
                ctx.MUL().getText(),
                this.visitUnaryExpression(ctx.unaryExpression())
            );
        } else if(ctx.DIV() != null) {
            return new QueenBinaryExpressionNode(
                getPosition(ctx),
                this.visitMultiplicativeExpression(ctx.multiplicativeExpression()),
                ctx.DIV().getText(),
                this.visitUnaryExpression(ctx.unaryExpression())
            );
        } else if(ctx.MOD() != null) {
            return new QueenBinaryExpressionNode(
                getPosition(ctx),
                this.visitMultiplicativeExpression(ctx.multiplicativeExpression()),
                ctx.MOD().getText(),
                this.visitUnaryExpression(ctx.unaryExpression())
            );
        } else {
            return this.visitUnaryExpression(ctx.unaryExpression());
        }
    }

    @Override
    public QueenExpressionNode visitAdditiveExpression(QueenParser.AdditiveExpressionContext ctx) {
        if(ctx.ADD() != null) {
            return new QueenBinaryExpressionNode(
                getPosition(ctx),
                this.visitAdditiveExpression(ctx.additiveExpression()),
                ctx.ADD().getText(),
                this.visitMultiplicativeExpression(ctx.multiplicativeExpression())
            );
        } else if(ctx.SUB() != null) {
            return new QueenBinaryExpressionNode(
                getPosition(ctx),
                this.visitAdditiveExpression(ctx.additiveExpression()),
                ctx.SUB().getText(),
                this.visitMultiplicativeExpression(ctx.multiplicativeExpression())
            );
        } else {
            return this.visitMultiplicativeExpression(ctx.multiplicativeExpression());
        }
    }

    @Override
    public QueenExpressionNode visitShiftExpression(QueenParser.ShiftExpressionContext ctx) {
        if(ctx.GT() != null && ctx.GT().size() == 3) {
            return new QueenBinaryExpressionNode(
                getPosition(ctx),
                this.visitShiftExpression(ctx.shiftExpression()),
                ">>>",
                this.visitAdditiveExpression(ctx.additiveExpression())
            );
        } else if(ctx.GT() != null && ctx.GT().size() == 2) {
            return new QueenBinaryExpressionNode(
                getPosition(ctx),
                this.visitShiftExpression(ctx.shiftExpression()),
                ">>",
                this.visitAdditiveExpression(ctx.additiveExpression())
            );
        } else if(ctx.LT() != null && ctx.LT().size() == 2) {
            return new QueenBinaryExpressionNode(
                getPosition(ctx),
                this.visitShiftExpression(ctx.shiftExpression()),
                "<<",
                this.visitAdditiveExpression(ctx.additiveExpression())
            );
        } else {
            return this.visitAdditiveExpression(ctx.additiveExpression());
        }
    }

    @Override
    public QueenExpressionNode visitRelationalExpression(QueenParser.RelationalExpressionContext ctx) {
        if(ctx.LT() != null) {
            return new QueenBinaryExpressionNode(
                getPosition(ctx),
                this.visitRelationalExpression(ctx.relationalExpression()),
                ctx.LT().getText(),
                this.visitShiftExpression(ctx.shiftExpression())
            );
        } else if(ctx.GT() != null) {
            return new QueenBinaryExpressionNode(
                getPosition(ctx),
                this.visitRelationalExpression(ctx.relationalExpression()),
                ctx.GT().getText(),
                this.visitShiftExpression(ctx.shiftExpression())
            );
        } else if(ctx.LE() != null) {
            return new QueenBinaryExpressionNode(
                getPosition(ctx),
                this.visitRelationalExpression(ctx.relationalExpression()),
                ctx.LE().getText(),
                this.visitShiftExpression(ctx.shiftExpression())
            );
        } else if(ctx.GE() != null) {
            return new QueenBinaryExpressionNode(
                getPosition(ctx),
                this.visitRelationalExpression(ctx.relationalExpression()),
                ctx.GE().getText(),
                this.visitShiftExpression(ctx.shiftExpression())
            );
        } else if(ctx.INSTANCEOF() != null) {
            return new QueenInstanceOfExpressionNode(
                getPosition(ctx),
                this.visitRelationalExpression(ctx.relationalExpression()),
                this.visitReferenceType(ctx.referenceType())
            );
        } else {
            return this.visitShiftExpression(ctx.shiftExpression());
        }
    }

    @Override
    public QueenExpressionNode visitEqualityExpression(QueenParser.EqualityExpressionContext ctx) {
        if(ctx.EQUAL() != null) {
            return new QueenBinaryExpressionNode(
                getPosition(ctx),
                this.visitEqualityExpression(ctx.equalityExpression()),
                ctx.EQUAL().getText(),
                this.visitRelationalExpression(ctx.relationalExpression())
            );
        } else if(ctx.NOTEQUAL() != null) {
            return new QueenBinaryExpressionNode(
                getPosition(ctx),
                this.visitEqualityExpression(ctx.equalityExpression()),
                ctx.NOTEQUAL().getText(),
                this.visitRelationalExpression(ctx.relationalExpression())
            );
        } else {
            return this.visitRelationalExpression(ctx.relationalExpression());
        }
    }

    @Override
    public QueenExpressionNode visitAndExpression(QueenParser.AndExpressionContext ctx) {
        if(ctx.BITAND() != null) {
            return new QueenBinaryExpressionNode(
                getPosition(ctx),
                this.visitAndExpression(ctx.andExpression()),
                ctx.BITAND().getText(),
                this.visitEqualityExpression(ctx.equalityExpression())
            );
        } else {
            return this.visitEqualityExpression(ctx.equalityExpression());
        }
    }

    @Override
    public QueenExpressionNode visitExclusiveOrExpression(final QueenParser.ExclusiveOrExpressionContext ctx) {
        if(ctx.CARET() != null) {
            return new QueenBinaryExpressionNode(
                getPosition(ctx),
                this.visitExclusiveOrExpression(ctx.exclusiveOrExpression()),
                ctx.CARET().getText(),
                this.visitAndExpression(ctx.andExpression())
            );
        } else {
            return this.visitAndExpression(ctx.andExpression());
        }
    }

    @Override
    public QueenExpressionNode visitInclusiveOrExpression(final QueenParser.InclusiveOrExpressionContext ctx) {
        if(ctx.BITOR() != null) {
            return new QueenBinaryExpressionNode(
                getPosition(ctx),
                this.visitInclusiveOrExpression(ctx.inclusiveOrExpression()),
                ctx.BITOR().getText(),
                this.visitExclusiveOrExpression(ctx.exclusiveOrExpression())
            );
        } else {
            return this.visitExclusiveOrExpression(ctx.exclusiveOrExpression());
        }
    }

    @Override
    public QueenExpressionNode visitConditionalAndExpression(final QueenParser.ConditionalAndExpressionContext ctx) {
        if(ctx.AND() != null) {
            return new QueenBinaryExpressionNode(
                getPosition(ctx),
                this.visitConditionalAndExpression(ctx.conditionalAndExpression()),
                ctx.AND().getText(),
                this.visitInclusiveOrExpression(ctx.inclusiveOrExpression())
            );
        } else {
            return this.visitInclusiveOrExpression(ctx.inclusiveOrExpression());
        }
    }

    @Override
    public QueenExpressionNode visitConditionalOrExpression(final QueenParser.ConditionalOrExpressionContext ctx) {
        if(ctx.OR() != null) {
            return new QueenBinaryExpressionNode(
                getPosition(ctx),
                this.visitConditionalOrExpression(ctx.conditionalOrExpression()),
                ctx.OR().getText(),
                this.visitConditionalAndExpression(ctx.conditionalAndExpression())
            );
        } else {
            return this.visitConditionalAndExpression(ctx.conditionalAndExpression());
        }
    }

    @Override
    public QueenExpressionNode visitConditionalExpression(final QueenParser.ConditionalExpressionContext ctx) {
        if(ctx.QUESTION() != null) {
            return new QueenConditionalExpressionNode(
                getPosition(ctx),
                this.visitConditionalOrExpression(ctx.conditionalOrExpression()),
                this.visitExpression(ctx.expression()),
                this.visitConditionalExpression(ctx.conditionalExpression())
            );
        } else {
            return this.visitConditionalOrExpression(ctx.conditionalOrExpression());
        }
    }

    @Override
    public QueenExpressionNode visitPreIncrementExpression(QueenParser.PreIncrementExpressionContext ctx) {
        return new QueenUnaryExpressionNode(
            getPosition(ctx),
            "++",
            true,
            this.visitUnaryExpression(ctx.unaryExpression())
        );
    }

    @Override
    public QueenExpressionNode visitPreDecrementExpression(QueenParser.PreDecrementExpressionContext ctx) {
        return new QueenUnaryExpressionNode(
            getPosition(ctx),
            "--",
            true,
            this.visitUnaryExpression(ctx.unaryExpression())
        );
    }

    @Override
    public QueenExpressionNode visitPostIncrementExpression(QueenParser.PostIncrementExpressionContext ctx) {
        return new QueenUnaryExpressionNode(
            getPosition(ctx),
            "++",
            false,
            this.visitPostfixExpression(ctx.postfixExpression())
        );
    }

    @Override
    public QueenExpressionNode visitPostDecrementExpression(QueenParser.PostDecrementExpressionContext ctx) {
        return new QueenUnaryExpressionNode(
            getPosition(ctx),
            "--",
            false,
            this.visitPostfixExpression(ctx.postfixExpression())
        );
    }

    @Override
    public QueenExpressionNode visitUnaryExpressionNotPlusMinus(QueenParser.UnaryExpressionNotPlusMinusContext ctx) {
        if(ctx.postfixExpression() != null) {
            return this.visitPostfixExpression(ctx.postfixExpression());
        } else if(ctx.TILDE() != null) {
            return new QueenUnaryExpressionNode(
                getPosition(ctx),
                ctx.TILDE().getText(),
                true,
                this.visitUnaryExpression(ctx.unaryExpression())
            );
        } else if(ctx.BANG() != null) {
            return new QueenUnaryExpressionNode(
                getPosition(ctx),
                ctx.BANG().getText(),
                true,
                this.visitUnaryExpression(ctx.unaryExpression())
            );
        } else {
            return this.visitCastExpression(ctx.castExpression());
        }
    }

    @Override
    public QueenExpressionNode visitCastExpression(QueenParser.CastExpressionContext ctx) {
        if(ctx.primitiveType() != null) {
            return new QueenCastExpressionNode(
                getPosition(ctx),
                this.visitPrimitiveType(ctx.primitiveType()),
                List.of(),
                this.visitUnaryExpression(ctx.unaryExpression())
            );
        } else {
            final List<QueenReferenceTypeNode> referenceTypes = new ArrayList<>();
            referenceTypes.add(this.visitReferenceType(ctx.referenceType()));
            if(ctx.additionalBound() != null) {
                ctx.additionalBound().forEach(
                    ab -> referenceTypes.add(
                        this.visitInterfaceType(ab.interfaceType())
                    )
                );
            }
            if(ctx.unaryExpressionNotPlusMinus() != null) {
                return new QueenCastExpressionNode(
                    getPosition(ctx),
                    referenceTypes,
                    this.visitUnaryExpressionNotPlusMinus(ctx.unaryExpressionNotPlusMinus())
                );
            } else {
                return new QueenCastExpressionNode(
                    getPosition(ctx),
                    referenceTypes,
                    this.visitLambdaExpression(ctx.lambdaExpression())
                );
            }
        }
    }

    @Override
    public QueenExpressionNode visitLambdaExpression(QueenParser.LambdaExpressionContext ctx) {
        final Position position = getPosition(ctx);
        final List<QueenParameterNode> parameters = new ArrayList<>();
        boolean enclosedParameters = false;
        if(ctx.lambdaParameters() != null) {
            if(ctx.lambdaParameters().LPAREN() != null) {
                enclosedParameters = true;
            }
            if(ctx.lambdaParameters().Identifier() != null) {
                parameters.add(
                    new QueenParameterNode(
                        getPosition(ctx.lambdaParameters()),
                        ctx.lambdaParameters().Identifier().getText()
                    )
                );
            } else if(ctx.lambdaParameters().inferredFormalParameterList() != null) {
                ctx.lambdaParameters().inferredFormalParameterList().Identifier().forEach(
                    inferred -> parameters.add(
                        new QueenParameterNode(
                            getPosition(ctx.lambdaParameters().inferredFormalParameterList()),
                            inferred.getText()
                        )
                    )
                );
            } else {
                final QueenParser.FormalParameterListContext formalParameterList = ctx.lambdaParameters().formalParameterList();
                if(formalParameterList != null && formalParameterList.formalParameters() != null) {
                    formalParameterList.formalParameters().formalParameter()
                        .forEach(fp -> parameters.add(this.visitFormalParameter(fp)));
                }
                if(formalParameterList != null && formalParameterList.lastFormalParameter() != null) {
                    parameters.add(this.visitLastFormalParameter(formalParameterList.lastFormalParameter()));
                }
            }
        }
        final QueenExpressionNode expression;
        final QueenBlockStatements queenBlockStatements;
        if (ctx.lambdaBody().block() != null && ctx.lambdaBody().block().blockStatements() != null) {
            queenBlockStatements = this.visitBlockStatements(ctx.lambdaBody().block().blockStatements());
            expression = null;
        } else {
            queenBlockStatements = null;
            expression = this.visitExpression(ctx.lambdaBody().expression());
        }
        return new QueenLambdaExpressionNode(
            position,
            enclosedParameters,
            parameters,
            expression,
            queenBlockStatements
        );
    }

    @Override
    public QueenExpressionNode visitPostfixExpression(QueenParser.PostfixExpressionContext ctx) {
        QueenExpressionNode postfixExpression;
        if(ctx.primary() != null) {
            postfixExpression = this.visitPrimary(ctx.primary());
        } else {
            postfixExpression=  this.visitExpressionName(ctx.expressionName());
        }
        for(int i=0; i<ctx.postIncrementExpression_lf_postfixExpression().size(); i++) {
            postfixExpression = new QueenUnaryExpressionNode(
                getPosition(ctx),
                "++",
                false,
                postfixExpression
            );
        }
        for(int i=0; i<ctx.postDecrementExpression_lf_postfixExpression().size(); i++) {
            postfixExpression = new QueenUnaryExpressionNode(
                getPosition(ctx),
                "--",
                false,
                postfixExpression
            );
        }
        return postfixExpression;
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
        return this.visitClassType(ctx.classType());
    }

    @Override
    public QueenClassOrInterfaceTypeNode visitClassType(QueenParser.ClassTypeContext ctx) {
        final Position position = this.getPosition(ctx);
        final List<QueenAnnotationNode> annotations = new ArrayList<>();
        ctx.annotation().forEach(
            a -> annotations.add(this.visitAnnotation(a))
        );
        final String name = ctx.Identifier().getText();
        final List<QueenTypeNode> typeArguments = new ArrayList<>();
        if(ctx.typeArguments() != null) {
            ctx.typeArguments().typeArgumentList().typeArgument()
                .forEach(ta -> typeArguments.add(this.visitTypeArgument(ta)));
        }
        return new QueenClassOrInterfaceTypeNode(
            position,
            false,
            ctx.classOrInterfaceType() != null ?
                this.visitClassOrInterfaceType(ctx.classOrInterfaceType())
                :
                null,
            annotations,
            name,
            typeArguments,
            false
        );
    }

    @Override
    public QueenClassOrInterfaceTypeNode visitUnannClassType(QueenParser.UnannClassTypeContext ctx) {
        final Position position = this.getPosition(ctx);
        final List<QueenAnnotationNode> annotations = new ArrayList<>();
        ctx.annotation().forEach(
            a -> annotations.add(this.visitAnnotation(a))
        );
        final String name = ctx.Identifier().getText();
        final List<QueenTypeNode> typeArguments = new ArrayList<>();
        if(ctx.typeArguments() != null) {
            ctx.typeArguments().typeArgumentList().typeArgument()
                .forEach(ta -> typeArguments.add(this.visitTypeArgument(ta)));
        }
        return new QueenClassOrInterfaceTypeNode(
            position,
            false,
            ctx.unannClassOrInterfaceType() != null ?
                this.visitUnannClassOrInterfaceType(ctx.unannClassOrInterfaceType())
                :
                null,
            annotations,
            name,
            typeArguments,
            false
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
        final List<QueenTypeNode> typeArguments = new ArrayList<>();
        if(ctx.classType().typeArguments() != null) {
            ctx.classType().typeArguments().typeArgumentList().typeArgument()
                .forEach(ta -> typeArguments.add(this.visitTypeArgument(ta)));
        }
        return new QueenClassOrInterfaceTypeNode(
            position,
            true,
            ctx.classType().classOrInterfaceType() != null ?
                this.visitClassOrInterfaceType(ctx.classType().classOrInterfaceType())
                :
                null,
            annotations,
            name,
            typeArguments,
            false
        );
    }

    @Override
    public QueenTypeNode visitTypeArgument(QueenParser.TypeArgumentContext ctx) {
        if(ctx.referenceType() != null) {
            return this.visitReferenceType(ctx.referenceType());
        } else {
            return this.visitWildcard(ctx.wildcard());
        }
    }

    @Override
    public QueenReferenceTypeNode visitReferenceType(QueenParser.ReferenceTypeContext ctx) {
        if(ctx.classOrInterfaceType() != null) {
            return this.visitClassOrInterfaceType(ctx.classOrInterfaceType());
        } else if(ctx.typeVariable() != null) {
            return this.visitTypeVariable(ctx.typeVariable());
        } else {
            return this.visitArrayType(ctx.arrayType());
        }
    }

    @Override
    public QueenClassOrInterfaceTypeNode visitTypeVariable(QueenParser.TypeVariableContext ctx) {
        final Position position = this.getPosition(ctx);
        final List<QueenAnnotationNode> annotations = new ArrayList<>();
        ctx.annotation().forEach(
            a -> annotations.add(this.visitAnnotation(a))
        );
        final String name = ctx.Identifier().getText();
        return new QueenClassOrInterfaceTypeNode(
            position,
            false,
            annotations,
            name,
            new ArrayList<>()
        );
    }

    @Override
    public QueenClassOrInterfaceTypeNode visitUnannTypeVariable(QueenParser.UnannTypeVariableContext ctx) {
        final Position position = this.getPosition(ctx);
        final String name = ctx.Identifier().getText();
        return new QueenClassOrInterfaceTypeNode(
            position,
            false,
            new ArrayList<>(),
            name,
            new ArrayList<>()
        );
    }

    @Override
    public QueenExceptionTypeNode visitExceptionType(QueenParser.ExceptionTypeContext ctx) {
        if(ctx.classType() != null) {
            return new QueenExceptionTypeNode(this.visitClassType(ctx.classType()));
        } else {
            return new QueenExceptionTypeNode(this.visitTypeVariable(ctx.typeVariable()));
        }
    }

    @Override
    public QueenPrimitiveTypeNode visitPrimitiveType(QueenParser.PrimitiveTypeContext ctx) {
        final Position position = this.getPosition(ctx);
        final List<QueenAnnotationNode> annotations = new ArrayList<>();
        ctx.annotation().forEach(
            a -> annotations.add(this.visitAnnotation(a))
        );
        final String name;
        if(ctx.BOOLEAN() != null) {
            name = ctx.BOOLEAN().getText();
        } else {
            name = asString(ctx.numericType());
        }
        return new QueenPrimitiveTypeNode(
            position,
            annotations,
            name
        );
    }

    @Override
    public QueenClassOrInterfaceTypeNode visitClassOrInterfaceType(QueenParser.ClassOrInterfaceTypeContext ctx) {
        QueenClassOrInterfaceTypeNode classOrInterfaceTypeNode = null;
        if(ctx.classType_lfno_classOrInterfaceType() != null) {
            classOrInterfaceTypeNode = this.visitClassType_lfno_classOrInterfaceType(ctx.classType_lfno_classOrInterfaceType());
        } else if(ctx.interfaceType_lfno_classOrInterfaceType() != null) {
            classOrInterfaceTypeNode = this.visitClassType_lfno_classOrInterfaceType(
                ctx.interfaceType_lfno_classOrInterfaceType().classType_lfno_classOrInterfaceType()
            );
        }
        final List<QueenParser.ClassType_lf_classOrInterfaceTypeContext> moreParts = new ArrayList<>();
        if(ctx.classType_lf_classOrInterfaceType() != null) {
            moreParts.addAll(ctx.classType_lf_classOrInterfaceType());
        } else if(ctx.interfaceType_lf_classOrInterfaceType() != null) {
            moreParts.addAll(
                ctx.interfaceType_lf_classOrInterfaceType()
                    .stream().map(QueenParser.InterfaceType_lf_classOrInterfaceTypeContext::classType_lf_classOrInterfaceType)
                    .collect(Collectors.toList())
            );
        }
        for(QueenParser.ClassType_lf_classOrInterfaceTypeContext part : moreParts) {
            final Position position = this.getPosition(ctx);
            final List<QueenAnnotationNode> annotations = new ArrayList<>();
            part.annotation().forEach(
                a -> annotations.add(this.visitAnnotation(a))
            );
            final String name = part.Identifier().getText();
            final List<QueenTypeNode> typeArguments = new ArrayList<>();
            if(part.typeArguments() != null) {
                part.typeArguments().typeArgumentList().typeArgument()
                    .forEach(ta -> typeArguments.add(this.visitTypeArgument(ta)));
            }
            classOrInterfaceTypeNode = new QueenClassOrInterfaceTypeNode(
                position,
                false,
                classOrInterfaceTypeNode,
                annotations,
                name,
                typeArguments,
                false
            );
        }
        return classOrInterfaceTypeNode;
    }

    @Override
    public QueenClassOrInterfaceTypeNode visitClassType_lfno_classOrInterfaceType(QueenParser.ClassType_lfno_classOrInterfaceTypeContext ctx) {
        final Position position = this.getPosition(ctx);
        final List<QueenAnnotationNode> annotations = new ArrayList<>();
        ctx.annotation().forEach(
            a -> annotations.add(this.visitAnnotation(a))
        );
        final String name = ctx.Identifier().getText();
        final List<QueenTypeNode> typeArguments = new ArrayList<>();
        if(ctx.typeArguments() != null) {
            ctx.typeArguments().typeArgumentList().typeArgument()
                .forEach(ta -> typeArguments.add(this.visitTypeArgument(ta)));
        }
        return new QueenClassOrInterfaceTypeNode(
            position,
            false,
            annotations,
            name,
            typeArguments
        );
    }

    @Override
    public QueenArrayTypeNode visitArrayType(QueenParser.ArrayTypeContext ctx) {
        final Position position = this.getPosition(ctx);
        final QueenTypeNode type;
        if(ctx.primitiveType() != null) {
            type = this.visitPrimitiveType(ctx.primitiveType());
        } else if(ctx.classOrInterfaceType() != null) {
            type = this.visitClassOrInterfaceType(ctx.classOrInterfaceType());
        } else {
            type = this.visitTypeVariable(ctx.typeVariable());
        }
        final List<QueenArrayDimensionNode> dims = new ArrayList<>();
        ctx.dims().dim().forEach(
            d -> dims.add(this.visitDim(d))
        );
        return new QueenArrayTypeNode(
            position,
            type,
            dims
        );
    }

    @Override
    public QueenArrayTypeNode visitUnannArrayType(QueenParser.UnannArrayTypeContext ctx) {
        final Position position = this.getPosition(ctx);
        final QueenTypeNode type;
        if(ctx.unannPrimitiveType() != null) {
            type = this.visitUnannPrimitiveType(ctx.unannPrimitiveType());
        } else if(ctx.unannClassOrInterfaceType() != null) {
            type = this.visitUnannClassOrInterfaceType(ctx.unannClassOrInterfaceType());
        } else {
            type = this.visitUnannTypeVariable(ctx.unannTypeVariable());
        }
        final List<QueenArrayDimensionNode> dims = new ArrayList<>();
        ctx.dims().dim().forEach(
            d -> dims.add(this.visitDim(d))
        );
        return new QueenArrayTypeNode(
            position,
            type,
            dims
        );
    }

    @Override
    public QueenWildcardNode visitWildcard(QueenParser.WildcardContext ctx) {
        final Position position = this.getPosition(ctx);
        final List<QueenAnnotationNode> annotations = new ArrayList<>();
        ctx.annotation().forEach(
            a -> annotations.add(this.visitAnnotation(a))
        );
        QueenReferenceTypeNode superType = null;
        QueenReferenceTypeNode extendedType = null;
        if(ctx.wildcardBounds() != null) {
            if(ctx.wildcardBounds().SUPER() != null) {
                superType = this.visitReferenceType(ctx.wildcardBounds().referenceType());
            } else if(ctx.wildcardBounds().EXTENDS() != null) {
                extendedType = this.visitReferenceType(ctx.wildcardBounds().referenceType());
            }
        }
        return new QueenWildcardNode(
            position,
            annotations,
            extendedType,
            superType
        );
    }

    @Override
    public QueenTypeNode visitUnannType(QueenParser.UnannTypeContext ctx) {
        if(ctx.unannPrimitiveType() != null) {
            return this.visitUnannPrimitiveType(ctx.unannPrimitiveType());
        } else {
            return this.visitUnannReferenceType(ctx.unannReferenceType());
        }
    }

    @Override
    public QueenPrimitiveTypeNode visitUnannPrimitiveType(QueenParser.UnannPrimitiveTypeContext ctx) {
        final Position position = this.getPosition(ctx);
        final String name;
        if(ctx.BOOLEAN() != null) {
            name = ctx.BOOLEAN().getText();
        } else {
            name = asString(ctx.numericType());
        }
        return new QueenPrimitiveTypeNode(
            position,
            new ArrayList<>(),
            name
        );
    }

    @Override
    public QueenReferenceTypeNode visitUnannReferenceType(QueenParser.UnannReferenceTypeContext ctx) {
        if(ctx.unannClassOrInterfaceType() != null) {
            return this.visitUnannClassOrInterfaceType(ctx.unannClassOrInterfaceType());
        } else if(ctx.unannTypeVariable() != null) {
            return this.visitUnannTypeVariable(ctx.unannTypeVariable());
        } else {
            return this.visitUnannArrayType(ctx.unannArrayType());
        }
    }

    @Override
    public QueenClassOrInterfaceTypeNode visitUnannClassOrInterfaceType(QueenParser.UnannClassOrInterfaceTypeContext ctx) {
        QueenClassOrInterfaceTypeNode unannClassOrInterfaceTypeNode = null;
        if(ctx.unannClassType_lfno_unannClassOrInterfaceType() != null) {
            unannClassOrInterfaceTypeNode = this.visitUnannClassType_lfno_unannClassOrInterfaceType(ctx.unannClassType_lfno_unannClassOrInterfaceType());
        } else if(ctx.unannInterfaceType_lfno_unannClassOrInterfaceType() != null) {
            unannClassOrInterfaceTypeNode = this.visitUnannClassType_lfno_unannClassOrInterfaceType(
                ctx.unannInterfaceType_lfno_unannClassOrInterfaceType().unannClassType_lfno_unannClassOrInterfaceType()
            );
        }
        final List<QueenParser.UnannClassType_lf_unannClassOrInterfaceTypeContext> moreParts = new ArrayList<>();
        if(ctx.unannClassType_lf_unannClassOrInterfaceType() != null) {
            moreParts.addAll(ctx.unannClassType_lf_unannClassOrInterfaceType());
        } else if(ctx.unannInterfaceType_lf_unannClassOrInterfaceType() != null) {
            moreParts.addAll(
                ctx.unannInterfaceType_lf_unannClassOrInterfaceType()
                    .stream().map(QueenParser.UnannInterfaceType_lf_unannClassOrInterfaceTypeContext::unannClassType_lf_unannClassOrInterfaceType)
                    .collect(Collectors.toList())
            );
        }
        for(QueenParser.UnannClassType_lf_unannClassOrInterfaceTypeContext part : moreParts) {
            final Position position = this.getPosition(ctx);
            final List<QueenAnnotationNode> annotations = new ArrayList<>();
            part.annotation().forEach(
                a -> annotations.add(this.visitAnnotation(a))
            );
            final String name = part.Identifier().getText();
            final List<QueenTypeNode> typeArguments = new ArrayList<>();
            if(part.typeArguments() != null) {
                part.typeArguments().typeArgumentList().typeArgument()
                    .forEach(ta -> typeArguments.add(this.visitTypeArgument(ta)));
            }
            unannClassOrInterfaceTypeNode = new QueenClassOrInterfaceTypeNode(
                position,
                false,
                unannClassOrInterfaceTypeNode,
                annotations,
                name,
                typeArguments,
                false
            );
        }
        return unannClassOrInterfaceTypeNode;
    }

    @Override
    public QueenClassOrInterfaceTypeNode visitUnannClassType_lfno_unannClassOrInterfaceType(QueenParser.UnannClassType_lfno_unannClassOrInterfaceTypeContext ctx) {
        final Position position = this.getPosition(ctx);
        final String name = ctx.Identifier().getText();
        final List<QueenTypeNode> typeArguments = new ArrayList<>();
        if(ctx.typeArguments() != null) {
            ctx.typeArguments().typeArgumentList().typeArgument()
                .forEach(ta -> typeArguments.add(this.visitTypeArgument(ta)));
        }
        return new QueenClassOrInterfaceTypeNode(
            position,
            false,
            new ArrayList<>(),
            name,
            typeArguments
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
