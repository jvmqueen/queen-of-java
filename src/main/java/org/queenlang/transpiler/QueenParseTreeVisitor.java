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
import org.queenlang.generated.antlr4.QueenParser;
import org.queenlang.generated.antlr4.QueenParserBaseVisitor;
import org.queenlang.transpiler.nodes.*;
import org.queenlang.transpiler.nodes.body.*;
import org.queenlang.transpiler.nodes.expressions.*;
import org.queenlang.transpiler.nodes.statements.*;
import org.queenlang.transpiler.nodes.types.*;

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
    public CompilationUnitNode visitCompilationUnit(QueenParser.CompilationUnitContext ctx) {
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
            this.visitTypeDeclaration(ctx.typeDeclaration())
        );
    }

    @Override
    public ImportDeclarationNode visitImportDeclaration(QueenParser.ImportDeclarationContext ctx) {
        if(ctx.singleTypeImportDeclaration() != null) {
            return this.visitSingleTypeImportDeclaration(ctx.singleTypeImportDeclaration());
        } else if(ctx.typeImportOnDemandDeclaration() != null) {
            return this.visitTypeImportOnDemandDeclaration(ctx.typeImportOnDemandDeclaration());
        } else if(ctx.singleStaticImportDeclaration() != null) {
            return this.visitSingleStaticImportDeclaration(ctx.singleStaticImportDeclaration());
        } else {
            return this.visitStaticImportOnDemandDeclaration(ctx.staticImportOnDemandDeclaration());
        }
    }

    @Override
    public ImportDeclarationNode visitSingleTypeImportDeclaration(QueenParser.SingleTypeImportDeclarationContext ctx) {
        return new QueenImportDeclarationNode(
            getPosition(ctx),
            this.visitTypeName(ctx.typeName()),
            false,
            false
        );
    }

    @Override
    public ImportDeclarationNode visitTypeImportOnDemandDeclaration(QueenParser.TypeImportOnDemandDeclarationContext ctx) {
        return new QueenImportDeclarationNode(
            getPosition(ctx),
            this.visitPackageOrTypeName(ctx.packageOrTypeName()),
            false,
            true
        );
    }

    @Override
    public ImportDeclarationNode visitSingleStaticImportDeclaration(QueenParser.SingleStaticImportDeclarationContext ctx) {
        return new QueenImportDeclarationNode(
            getPosition(ctx),
            new QueenNameNode(
                getPosition(ctx.typeName()),
                this.visitTypeName(ctx.typeName()),
                ctx.Identifier().getText()
            ),
            true,
            false
        );
    }

    @Override
    public ImportDeclarationNode visitStaticImportOnDemandDeclaration(QueenParser.StaticImportOnDemandDeclarationContext ctx) {
        return new QueenImportDeclarationNode(
            getPosition(ctx),
            this.visitTypeName(ctx.typeName()),
            true,
            true
        );
    }

    @Override
    public TypeDeclarationNode visitTypeDeclaration(QueenParser.TypeDeclarationContext ctx) {
        if(ctx.classDeclaration() != null) {
            return this.visitClassDeclaration(ctx.classDeclaration());
        } else if(ctx.interfaceDeclaration() != null) {
            return this.visitInterfaceDeclaration(ctx.interfaceDeclaration());
        }
        return null;
    }

    @Override
    public ClassDeclarationNode visitClassDeclaration(QueenParser.ClassDeclarationContext ctx) {
        final List<AnnotationNode> annotations = new ArrayList<>();
        final List<ModifierNode> accessModifiers = new ArrayList<>();
        final List<TypeParameterNode> typeParameters = new ArrayList<>();

        final String name = ctx.Identifier().getText();
        if(ctx.typeParameters() != null && ctx.typeParameters().typeParameterList() != null) {
            ctx.typeParameters().typeParameterList().typeParameter().forEach(
                tp -> typeParameters.add(this.visitTypeParameter(tp))
            );
        }
        final List<ClassOrInterfaceTypeNode> ofTypes = new ArrayList<>();
        if(ctx.superinterfaces() != null && ctx.superinterfaces().interfaceTypeList() != null) {
            ctx.superinterfaces().interfaceTypeList().interfaceType().forEach(
                inter -> ofTypes.add(this.visitInterfaceType(inter))
            );
        }
        ClassOrInterfaceTypeNode extendsType = null;
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
    public InterfaceDeclarationNode visitInterfaceDeclaration(QueenParser.InterfaceDeclarationContext ctx) {
        if(ctx.normalInterfaceDeclaration() != null) {
            return this.visitNormalInterfaceDeclaration(ctx.normalInterfaceDeclaration());
        } else {
            return this.visitAnnotationTypeDeclaration(ctx.annotationTypeDeclaration());
        }
    }

    @Override
    public NormalInterfaceDeclarationNode visitNormalInterfaceDeclaration(QueenParser.NormalInterfaceDeclarationContext ctx) {
        final List<AnnotationNode> annotations = new ArrayList<>();
        final List<ModifierNode> modifiers = new ArrayList<>();
        final List<TypeParameterNode> typeParams = new ArrayList<>();

        if(ctx.typeParameters() != null && ctx.typeParameters().typeParameterList() != null) {
            ctx.typeParameters().typeParameterList().typeParameter().forEach(
                tp -> typeParams.add(this.visitTypeParameter(tp))
            );
        }

        final List<ClassOrInterfaceTypeNode> extendsTypes = new ArrayList<>();
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
    public AnnotationTypeDeclarationNode visitAnnotationTypeDeclaration(QueenParser.AnnotationTypeDeclarationContext ctx) {
        final List<AnnotationNode> annotations = new ArrayList<>();
        final List<ModifierNode> modifiers = new ArrayList<>();

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
    public ModifierNode visitClassModifier(QueenParser.ClassModifierContext ctx) {
        return new QueenModifierNode(getPosition(ctx), asString(ctx));
    }

    @Override
    public ModifierNode visitMethodModifier(QueenParser.MethodModifierContext ctx) {
        return new QueenModifierNode(getPosition(ctx), asString(ctx));
    }

    @Override
    public ModifierNode visitInterfaceMethodModifier(QueenParser.InterfaceMethodModifierContext ctx) {
        return new QueenModifierNode(getPosition(ctx), asString(ctx));
    }

    @Override
    public ModifierNode visitAnnotationTypeElementModifier(QueenParser.AnnotationTypeElementModifierContext ctx) {
        return new QueenModifierNode(getPosition(ctx), asString(ctx));
    }

    @Override
    public ModifierNode visitClassAbstractOrFinal(QueenParser.ClassAbstractOrFinalContext ctx) {
        return new QueenModifierNode(getPosition(ctx), asString(ctx));
    }

    @Override
    public ModifierNode visitInterfaceModifier(QueenParser.InterfaceModifierContext ctx) {
        return new QueenModifierNode(getPosition(ctx), asString(ctx));
    }

    @Override
    public ModifierNode visitFieldModifier(QueenParser.FieldModifierContext ctx) {
        return new QueenModifierNode(getPosition(ctx), asString(ctx));
    }

    @Override
    public ModifierNode visitConstantModifier(QueenParser.ConstantModifierContext ctx) {
        return new QueenModifierNode(getPosition(ctx), asString(ctx));
    }

    @Override
    public ModifierNode visitConstructorModifier(QueenParser.ConstructorModifierContext ctx) {
        if(ctx != null) {
            return new QueenModifierNode(getPosition(ctx), asString(ctx));
        }
        return null;
    }

    @Override
    public AnnotationNode visitAnnotation(QueenParser.AnnotationContext ctx) {
        if(ctx.markerAnnotation() != null) {
            return new QueenMarkerAnnotationNode(getPosition(ctx), this.visitTypeName(ctx.markerAnnotation().typeName()));
        } else if(ctx.normalAnnotation() != null) {
            final List<ElementValuePairNode> pairs = new LinkedList<>();
            ctx.normalAnnotation()
                .elementValuePairList()
                .elementValuePair()
                .forEach(
                    pair -> pairs.add(
                        this.visitElementValuePair(pair)
                    )
                );
            return new QueenNormalAnnotationNode(
                getPosition(ctx),
                this.visitTypeName(ctx.normalAnnotation().typeName()),
                pairs
            );
        } else if(ctx.singleElementAnnotation() != null) {
            return new QueenSingleMemberAnnotationNode(
                getPosition(ctx),
                this.visitTypeName(ctx.singleElementAnnotation().typeName()),
                this.visitElementValue(ctx.singleElementAnnotation().elementValue())
            );
        }
        return null;
    }

    @Override
    public ExpressionNode visitElementValue(QueenParser.ElementValueContext ctx) {
        if(ctx.conditionalExpression() != null) {
            return this.visitConditionalExpression(ctx.conditionalExpression());
        } else if(ctx.annotation() != null) {
            return this.visitAnnotation(ctx.annotation());
        } else if(ctx.elementValueArrayInitializer() != null) {
            return this.visitElementValueArrayInitializer(ctx.elementValueArrayInitializer());
        }
        return null;
    }

    @Override
    public ExpressionNode visitElementValueArrayInitializer(QueenParser.ElementValueArrayInitializerContext ctx) {
        final List<ExpressionNode> values;
        if(ctx.elementValueList() != null) {
            values = new ArrayList<>();
            ctx.elementValueList().elementValue().forEach(
                ev -> values.add(this.visitElementValue(ev))
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
    public ClassBodyDeclarationNode visitClassBodyDeclaration(QueenParser.ClassBodyDeclarationContext ctx) {
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
    public LocalVariableDeclarationNode visitLocalVariableDeclaration(QueenParser.LocalVariableDeclarationContext ctx) {
        final List<AnnotationNode> annotations = new ArrayList<>();
        final List<ModifierNode> modifiers = new ArrayList<>();

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
            getPosition(ctx),
            annotations,
            modifiers,
            this.visitUnannType(ctx.unannType()),
            Arrays.asList(this.visitVariableDeclarator(ctx.variableDeclarator()))
        );
    }

    @Override
    public LocalVariableDeclarationNode visitForInitlocalVariableDeclaration(QueenParser.ForInitlocalVariableDeclarationContext ctx) {
        final List<AnnotationNode> annotations = new ArrayList<>();
        final List<ModifierNode> modifiers = new ArrayList<>();

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

        final List<VariableDeclaratorNode> variables = new LinkedList<>();
        ctx.variableDeclaratorList().variableDeclarator().forEach(
            vd -> variables.add(this.visitVariableDeclarator(vd))
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
    public FieldDeclarationNode visitFieldDeclaration(QueenParser.FieldDeclarationContext ctx) {
        final List<AnnotationNode> annotations = new ArrayList<>();
        ctx.annotation().forEach(
            a -> annotations.add(this.visitAnnotation(a))
        );

        final List<ModifierNode> modifiers = new ArrayList<>();
        ctx.fieldModifier().forEach(
            m -> modifiers.add(this.visitFieldModifier(m))
        );

        return new QueenFieldDeclarationNode(
            getPosition(ctx),
            annotations,
            modifiers,
            this.visitUnannType(ctx.unannType()),
            this.visitVariableDeclarator(ctx.variableDeclarator())
        );
    }

    @Override
    public ConstantDeclarationNode visitConstantDeclaration(QueenParser.ConstantDeclarationContext ctx) {
        final List<AnnotationNode> annotations = new ArrayList<>();
        ctx.annotation().forEach(
            a -> annotations.add(this.visitAnnotation(a))
        );

        final List<ModifierNode> modifiers = new ArrayList<>();
        ctx.constantModifier().forEach(
            m -> modifiers.add(this.visitConstantModifier(m))
        );

        return new QueenConstantDeclarationNode(
            getPosition(ctx),
            annotations,
            modifiers,
            this.visitUnannType(ctx.unannType()),
            this.visitVariableDeclarator(ctx.variableDeclarator())
        );
    }

    @Override
    public VariableDeclaratorNode visitVariableDeclarator(QueenParser.VariableDeclaratorContext ctx) {
        final ExpressionNode initializer;
        if(ctx.variableInitializer() != null) {
            initializer = this.visitVariableInitializer(ctx.variableInitializer());
        } else {
            initializer = null;
        }
        return new QueenVariableDeclaratorNode(
            getPosition(ctx),
            this.visitVariableDeclaratorId(
                ctx.variableDeclaratorId()
            ),
            initializer
        );
    }

    @Override
    public ElementValuePairNode visitElementValuePair(QueenParser.ElementValuePairContext ctx) {
        return new QueenElementValuePairNode(
            getPosition(ctx),
            ctx.Identifier().getText(),
            this.visitElementValue(ctx.elementValue())
        );
    }

    @Override
    public ConstructorDeclarationNode visitConstructorDeclaration(QueenParser.ConstructorDeclarationContext ctx) {
        final List<AnnotationNode> annotations = new ArrayList<>();
        ctx.annotation().forEach(
            a -> annotations.add(this.visitAnnotation(a))
        );
        final List<TypeParameterNode> typeParams = new ArrayList<>();
        final QueenParser.TypeParametersContext typeParameters = ctx.constructorDeclarator().typeParameters();
        if(typeParameters != null && typeParameters.typeParameterList() != null) {
            typeParameters.typeParameterList().typeParameter().forEach(
                tp -> typeParams.add(this.visitTypeParameter(tp))
            );
        }
        final List<ParameterNode> parameters = new ArrayList<>();
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
        final List<ExceptionTypeNode> throwsList = new ArrayList<>();
        if(ctx.throws_() != null && ctx.throws_().exceptionTypeList() != null) {
            ctx.throws_().exceptionTypeList().exceptionType().forEach(
                et -> throwsList.add(this.visitExceptionType(et))
            );
        }
        final QueenParser.ConstructorBodyContext constructorBodyContext = ctx.constructorBody();
        final ExplicitConstructorInvocationNode explicitConstructorInvocationNode;
        if(constructorBodyContext.explicitConstructorInvocation() != null) {
            explicitConstructorInvocationNode = this.visitExplicitConstructorInvocation(constructorBodyContext.explicitConstructorInvocation());
        } else {
            explicitConstructorInvocationNode = null;
        }
        final BlockStatements queenBlockStatements;
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
            ctx.constructorDeclarator().simpleTypeName().Identifier().getText(),
            parameters,
            throwsList,
            explicitConstructorInvocationNode,
            queenBlockStatements
        );
    }

    @Override
    public MethodDeclarationNode visitMethodDeclaration(QueenParser.MethodDeclarationContext ctx) {
        final List<AnnotationNode> annotations = new ArrayList<>();
        ctx.annotation().forEach(
            a -> annotations.add(this.visitAnnotation(a))
        );
        final List<ModifierNode> modifiers = new ArrayList<>();
        ctx.methodModifier().forEach(
            m -> modifiers.add(this.visitMethodModifier(m))
        );
        final List<ParameterNode> parameters = new ArrayList<>();
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
        final List<ExceptionTypeNode> throwsList = new ArrayList<>();
        if(ctx.methodHeader().throws_() != null && ctx.methodHeader().throws_().exceptionTypeList() != null) {
            ctx.methodHeader().throws_().exceptionTypeList().exceptionType().forEach(
                et -> throwsList.add(this.visitExceptionType(et))
            );
        }
        final QueenParser.MethodBodyContext methodBodyContext = ctx.methodBody();
        final BlockStatements queenBlockStatements;
        if(methodBodyContext.block() != null) {
            queenBlockStatements = this.visitBlock(methodBodyContext.block());
        } else {
            queenBlockStatements = null;
        }
        final List<TypeParameterNode> typeParams = new ArrayList<>();
        final List<AnnotationNode> annotationsOnResult = new ArrayList<>();
        if(ctx.methodHeader().typeParameters() != null && ctx.methodHeader().typeParameters().typeParameterList() != null) {
            ctx.methodHeader().typeParameters().typeParameterList().typeParameter().forEach(
                tp -> typeParams.add(this.visitTypeParameter(tp))
            );
            ctx.methodHeader().annotation().forEach(
                a -> annotationsOnResult.add(this.visitAnnotation(a))
            );
        }
        return new QueenMethodDeclarationNode(
            getPosition(ctx),
            annotations,
            modifiers,
            this.visitResult(annotationsOnResult, ctx.methodHeader().result()),
            typeParams,
            methodDeclarator.Identifier().getText(),
            parameters,
            throwsList,
            queenBlockStatements
        );
    }

    public TypeNode visitResult(List<AnnotationNode> annotations, QueenParser.ResultContext ctx) {
        if(ctx.unannType() != null) {
            return this.visitUnannType(annotations, ctx.unannType());
        } else {
            return new QueenVoidNode(this.getPosition(ctx), annotations);
        }
    }

    @Override
    public InterfaceMethodDeclarationNode visitInterfaceMethodDeclaration(QueenParser.InterfaceMethodDeclarationContext ctx) {
        final List<AnnotationNode> annotations = new ArrayList<>();
        ctx.annotation().forEach(
            a -> annotations.add(this.visitAnnotation(a))
        );
        final List<ModifierNode> modifiers = new ArrayList<>();
        ctx.interfaceMethodModifier().forEach(
            m -> modifiers.add(this.visitInterfaceMethodModifier(m))
        );
        final List<ParameterNode> parameters = new ArrayList<>();
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
        final List<ExceptionTypeNode> throwsList = new ArrayList<>();
        if(ctx.methodHeader().throws_() != null && ctx.methodHeader().throws_().exceptionTypeList() != null) {
            ctx.methodHeader().throws_().exceptionTypeList().exceptionType().forEach(
                et -> throwsList.add(this.visitExceptionType(et))
            );
        }
        final QueenParser.MethodBodyContext methodBodyContext = ctx.methodBody();
        final BlockStatements queenBlockStatements;
        if(methodBodyContext.block() != null) {
            queenBlockStatements = this.visitBlock(methodBodyContext.block());
        } else {
            queenBlockStatements = null;
        }
        final List<TypeParameterNode> typeParams = new ArrayList<>();
        final List<AnnotationNode> annotationsOnResult = new ArrayList<>();

        if(ctx.methodHeader().typeParameters() != null && ctx.methodHeader().typeParameters().typeParameterList() != null) {
            ctx.methodHeader().typeParameters().typeParameterList().typeParameter().forEach(
                tp -> typeParams.add(this.visitTypeParameter(tp))
            );
            ctx.methodHeader().annotation().forEach(
                a -> annotationsOnResult.add(this.visitAnnotation(a))
            );
        }

        return new QueenInterfaceMethodDeclarationNode(
            getPosition(ctx),
            annotations,
            modifiers,
            this.visitResult(annotationsOnResult, ctx.methodHeader().result()),
            typeParams,
            methodDeclarator.Identifier().getText(),
            parameters,
            throwsList,
            queenBlockStatements
        );
    }

    @Override
    public ExpressionNode visitConstantExpression(QueenParser.ConstantExpressionContext ctx) {
        return this.visitExpression(ctx.expression());
    }

    @Override
    public ExpressionNode visitExpression(QueenParser.ExpressionContext ctx) {
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
    public ExpressionNode visitArrayInitializer(QueenParser.ArrayInitializerContext ctx) {
        final List<ExpressionNode> values;
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
    public ExpressionNode visitAssignmentExpression(QueenParser.AssignmentExpressionContext ctx) {
        if(ctx.assignment() != null) {
            return this.visitAssignment(ctx.assignment());
        } else {
            return this.visitConditionalExpression(ctx.conditionalExpression());
        }
    }

    @Override
    public ExpressionNode visitPrimary(QueenParser.PrimaryContext ctx) {
        ExpressionNode primary;
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
    public ExpressionNode visitPrimaryNoNewArray_lfno_arrayAccess(QueenParser.PrimaryNoNewArray_lfno_arrayAccessContext ctx) {
        if(ctx.literal() != null) {
            return this.visitLiteral(ctx.literal());
        } else if(ctx.typeName() != null && ctx.IMPLEMENTATION() != null) {
            final QueenNameNode typeName = this.visitTypeName(ctx.typeName());
            final List<ArrayDimensionNode> dims = new ArrayList<>();
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

    public ExpressionNode visitPrimaryNoNewArray_lf_primary(ExpressionNode scope, QueenParser.PrimaryNoNewArray_lf_primaryContext ctx) {
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
    public ExpressionNode visitPrimaryNoNewArray_lfno_primary(QueenParser.PrimaryNoNewArray_lfno_primaryContext ctx) {
        if(ctx.literal() != null) {
            return this.visitLiteral(ctx.literal());
        } else if(ctx.typeName() != null && ctx.IMPLEMENTATION() != null) {
            final QueenNameNode typeName = this.visitTypeName(ctx.typeName());
            final List<ArrayDimensionNode> dims = new ArrayList<>();
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
            final PrimitiveTypeNode primitiveType = this.visitUnannPrimitiveType(ctx.unannPrimitiveType());
            final List<ArrayDimensionNode> dims = new ArrayList<>();
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
    public ExpressionNode visitArrayCreationExpression(QueenParser.ArrayCreationExpressionContext ctx) {
        final Position position = getPosition(ctx);
        final TypeNode type;
        if(ctx.primitiveType() != null) {
            type = this.visitPrimitiveType(ctx.primitiveType());
        } else {
            type = this.visitClassOrInterfaceType(ctx.classOrInterfaceType());
        }
        final List<ArrayDimensionNode> dims = new ArrayList<>();
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
        final ExpressionNode arrayInitExpr;
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
    public ArrayDimensionNode visitDimExpr(QueenParser.DimExprContext ctx) {
        final Position position = getPosition(ctx);
        final List<AnnotationNode> annotations = new ArrayList<>();
        if(ctx.annotation() != null) {
            ctx.annotation().forEach(
                a -> annotations.add(this.visitAnnotation(a))
            );
        }
        final ExpressionNode expression = this.visitExpression(ctx.expression());
        return new QueenArrayDimensionNode(
            position,
            annotations,
            expression
        );
    }

    @Override
    public ArrayDimensionNode visitDim(QueenParser.DimContext ctx) {
        final Position position = getPosition(ctx);
        final List<AnnotationNode> annotations = new ArrayList<>();
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
    public ExpressionNode visitLiteral(QueenParser.LiteralContext ctx) {
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
    public ExpressionNode visitVariableInitializer(QueenParser.VariableInitializerContext ctx) {
        if(ctx.expression() != null) {
            return this.visitExpression(ctx.expression());
        } else {
            return this.visitArrayInitializer(ctx.arrayInitializer());
        }
    }

    @Override
    public ParameterNode visitFormalParameter(QueenParser.FormalParameterContext ctx) {
        final List<AnnotationNode> annotations = new ArrayList<>();
        final List<ModifierNode> modifiers = new ArrayList<>();

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
        return new QueenParameterNode(
            getPosition(ctx),
            annotations,
            modifiers,
            this.visitUnannType(ctx.unannType()),
            this.visitVariableDeclaratorId(ctx.variableDeclaratorId())
        );
    }

    @Override
    public ParameterNode visitLastFormalParameter(QueenParser.LastFormalParameterContext ctx) {
        if(ctx.formalParameter() != null) {
            return this.visitFormalParameter(ctx.formalParameter());
        }
        final List<AnnotationNode> varArgAnnotations = new ArrayList<>();
        final List<AnnotationNode> annotations = new ArrayList<>();
        final List<ModifierNode> modifiers = new ArrayList<>();

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

        if(ctx.annotation() != null) {
            ctx.annotation().forEach(
                va -> varArgAnnotations.add(this.visitAnnotation(va))
            );
        }
        return new QueenParameterNode(
            getPosition(ctx),
            annotations,
            modifiers,
            this.visitUnannType(ctx.unannType()),
            this.visitVariableDeclaratorId(ctx.variableDeclaratorId()),
            varArgAnnotations,
            true
        );
    }

    @Override
    public ExplicitConstructorInvocationNode visitExplicitConstructorInvocation(QueenParser.ExplicitConstructorInvocationContext ctx) {
        final Position position = getPosition(ctx);
        final List<TypeNode> typeArguments = new ArrayList<>();
        if (ctx.typeArguments() != null) {
            ctx.typeArguments().typeArgumentList().typeArgument()
                .forEach(ta -> typeArguments.add(this.visitTypeArgument(ta)));
        }
        ExpressionNode scope = null;
        if (ctx.expressionName() != null) {
            scope = this.visitExpressionName(ctx.expressionName());
        } else if(ctx.primary() != null){
            scope = this.visitPrimary(ctx.primary());
        }
        final boolean isThis = ctx.THIS() != null;
        final List<ExpressionNode> arguments = new ArrayList<>();
        if(ctx.argumentList() != null) {
            ctx.argumentList().expression().forEach(
                e -> arguments.add(this.visitExpression(e))
            );
        }
        return new QueenExplicitConstructorInvocationNode(
            position,
            isThis,
            scope,
            typeArguments,
            arguments
        );
    }

    @Override
    public BlockStatements visitBlock(QueenParser.BlockContext ctx) {
        if(ctx.blockStatements() != null) {
            return this.visitBlockStatements(ctx.blockStatements());
        }
        return new QueenBlockStatements(getPosition(ctx));
    }

    @Override
    public BlockStatements visitBlockStatements(QueenParser.BlockStatementsContext ctx) {
        final List<StatementNode> blockStatements = new ArrayList<>();
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
    public InstanceInitializerNode visitInstanceInitializer(QueenParser.InstanceInitializerContext ctx) {
        return new QueenInstanceInitializerNode(
            getPosition(ctx),
            this.visitBlock(ctx.block())
        );
    }

    @Override
    public InstanceInitializerNode visitStaticInitializer(QueenParser.StaticInitializerContext ctx) {
        return new QueenInstanceInitializerNode(
            getPosition(ctx),
            this.visitBlock(ctx.block()),
            true
        );
    }

    @Override
    public StatementNode visitStatement(QueenParser.StatementContext ctx) {
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
    public StatementNode visitStatementNoShortIf(QueenParser.StatementNoShortIfContext ctx) {
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
    public IfStatementNode visitIfThenStatement(QueenParser.IfThenStatementContext ctx) {
        final Position position = this.getPosition(ctx);
        final ExpressionNode condition = this.visitExpression(ctx.expression());
        final BlockStatements thenBlockStatements = new QueenBlockStatements(
            getPosition(ctx.statement()),
            Arrays.asList(this.visitStatement(ctx.statement()))
        );
        return new QueenIfStatementNode(
            position,
            condition,
            thenBlockStatements
        );
    }

    @Override
    public IfStatementNode visitIfThenElseStatement(QueenParser.IfThenElseStatementContext ctx) {
        final Position position = this.getPosition(ctx);
        final ExpressionNode condition = this.visitExpression(ctx.expression());

        final BlockStatements thenBlockStatements = new QueenBlockStatements(
            getPosition(ctx.statementNoShortIf()),
            Arrays.asList(this.visitStatementNoShortIf(ctx.statementNoShortIf()))
        );

        final BlockStatements elseBlockStatements = new QueenBlockStatements(
            getPosition(ctx.statement()),
            Arrays.asList(this.visitStatement(ctx.statement()))
        );
        return new QueenIfStatementNode(
            position,
            condition,
            thenBlockStatements,
            elseBlockStatements
        );
    }

    @Override
    public IfStatementNode visitIfThenElseStatementNoShortIf(QueenParser.IfThenElseStatementNoShortIfContext ctx) {
        final Position position = this.getPosition(ctx);
        final ExpressionNode condition = this.visitExpression(ctx.expression());

        final BlockStatements thenBlockStatements = new QueenBlockStatements(
            getPosition(ctx.statementNoShortIf().get(0)),
            Arrays.asList(this.visitStatementNoShortIf(ctx.statementNoShortIf().get(0)))
        );
        final BlockStatements elseBlockStatements = new QueenBlockStatements(
            getPosition(ctx.statementNoShortIf().get(1)),
            Arrays.asList(this.visitStatementNoShortIf(ctx.statementNoShortIf().get(1)))
        );

        return new QueenIfStatementNode(
            position,
            condition,
            thenBlockStatements,
            elseBlockStatements
        );
    }


    @Override
    public ForStatementNode visitBasicForStatement(QueenParser.BasicForStatementContext ctx) {
        final Position position = this.getPosition(ctx);
        final List<ExpressionNode> init = new ArrayList<>();
        if(ctx.forInit() != null) {
            if(ctx.forInit().statementExpressionList() != null) {
                ctx.forInit().statementExpressionList().statementExpression().forEach(
                    stmtExpression -> init.add(
                        this.visitStatementExpression(stmtExpression)
                    )
                );
            } else if(ctx.forInit().forInitlocalVariableDeclaration() != null) {
                init.add(
                    this.visitForInitlocalVariableDeclaration(
                        ctx.forInit().forInitlocalVariableDeclaration()
                    )
                );
            }
        }

        final ExpressionNode condition = this.visitExpression(ctx.expression());
        final List<ExpressionNode> update = new ArrayList<>();
        if(ctx.forUpdate() != null) {
            if(ctx.forUpdate().statementExpressionList() != null) {
                ctx.forUpdate().statementExpressionList().statementExpression().forEach(
                    stmtExpression -> update.add(
                        this.visitStatementExpression(stmtExpression)
                    )
                );
            }
        }

        final BlockStatements blockStatements= new QueenBlockStatements(
            getPosition(ctx.statement()),
            Arrays.asList(this.visitStatement(ctx.statement()))
        );

        return new QueenForStatementNode(
            position,
            init,
            condition,
            update,
            blockStatements
        );
    }

    @Override
    public ForStatementNode visitBasicForStatementNoShortIf(QueenParser.BasicForStatementNoShortIfContext ctx) {
        final Position position = this.getPosition(ctx);
        final List<ExpressionNode> init = new ArrayList<>();
        if(ctx.forInit() != null) {
            if(ctx.forInit().statementExpressionList() != null) {
                ctx.forInit().statementExpressionList().statementExpression().forEach(
                    stmtExpression -> init.add(
                        this.visitStatementExpression(stmtExpression)
                    )
                );
            } else if(ctx.forInit().forInitlocalVariableDeclaration() != null) {
                init.add(
                    this.visitForInitlocalVariableDeclaration(
                        ctx.forInit().forInitlocalVariableDeclaration()
                    )
                );
            }
        }

        final ExpressionNode condition = this.visitExpression(ctx.expression());
        final List<ExpressionNode> update = new ArrayList<>();
        if(ctx.forUpdate() != null) {
            if(ctx.forUpdate().statementExpressionList() != null) {
                ctx.forUpdate().statementExpressionList().statementExpression().forEach(
                    stmtExpression -> update.add(
                        this.visitStatementExpression(stmtExpression)
                    )
                );
            }
        }

        final BlockStatements blockStatements = new QueenBlockStatements(
            getPosition(ctx.statementNoShortIf()),
            Arrays.asList(this.visitStatementNoShortIf(ctx.statementNoShortIf()))
        );

        return new QueenForStatementNode(
            position,
            init,
            condition,
            update,
            blockStatements
        );
    }

    @Override
    public ForEachStatementNode visitEnhancedForStatement(QueenParser.EnhancedForStatementContext ctx) {
        final Position position = this.getPosition(ctx);

        final LocalVariableDeclarationNode variable;
        final List<AnnotationNode> annotations = new ArrayList<>();
        final List<ModifierNode> modifiers = new ArrayList<>();
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

        variable = new QueenLocalVariableDeclarationNode(
            getPosition(ctx),
            annotations,
            modifiers,
            this.visitUnannType(ctx.unannType()),
            Arrays.asList(
                new QueenVariableDeclaratorNode(
                    getPosition(ctx.variableDeclaratorId()),
                    this.visitVariableDeclaratorId(ctx.variableDeclaratorId()),
                    null
                )
            )
        );


        final ExpressionNode iterable = this.visitExpression(ctx.expression());
        final BlockStatements blockStatements = new QueenBlockStatements(
            getPosition(ctx.statement()),
            Arrays.asList(this.visitStatement(ctx.statement()))
        );
        return new QueenForEachStatementNode(
            position,
            variable,
            iterable,
            blockStatements
        );
    }

    @Override
    public VariableDeclaratorId visitVariableDeclaratorId(QueenParser.VariableDeclaratorIdContext ctx) {
        return new QueenVariableDeclaratorId(
            getPosition(ctx),
            ctx.Identifier().getText()
        );
    }

    @Override
    public ForEachStatementNode visitEnhancedForStatementNoShortIf(QueenParser.EnhancedForStatementNoShortIfContext ctx) {
        final Position position = this.getPosition(ctx);

        final QueenLocalVariableDeclarationNode variable;
        final List<AnnotationNode> annotations = new ArrayList<>();
        final List<ModifierNode> modifiers = new ArrayList<>();
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
        variable = new QueenLocalVariableDeclarationNode(
            getPosition(ctx),
            annotations,
            modifiers,
            this.visitUnannType(ctx.unannType()),
            Arrays.asList(
                new QueenVariableDeclaratorNode(
                    getPosition(ctx.variableDeclaratorId()),
                    this.visitVariableDeclaratorId(ctx.variableDeclaratorId()),
                    null
                )
            )
        );

        final ExpressionNode iterable = this.visitExpression(ctx.expression());
        final BlockStatements blockStatements = new QueenBlockStatements(
            getPosition(ctx.statementNoShortIf()),
            Arrays.asList(this.visitStatementNoShortIf(ctx.statementNoShortIf()))
        );

        return new QueenForEachStatementNode(
            position,
            variable,
            iterable,
            blockStatements
        );
    }

    @Override
    public WhileStatementNode visitWhileStatement(QueenParser.WhileStatementContext ctx) {
        final Position position = this.getPosition(ctx);
        final ExpressionNode expression = this.visitExpression(ctx.expression());
        final BlockStatements blockStatements= new QueenBlockStatements(
            getPosition(ctx.statement()),
            Arrays.asList(this.visitStatement(ctx.statement()))
        );
        return new QueenWhileStatementNode(
            position,
            expression,
            blockStatements
        );
    }

    @Override
    public WhileStatementNode visitWhileStatementNoShortIf(QueenParser.WhileStatementNoShortIfContext ctx) {
        final Position position = this.getPosition(ctx);
        final ExpressionNode expression = this.visitExpression(ctx.expression());
        final BlockStatements blockStatements = new QueenBlockStatements(
            getPosition(ctx.statementNoShortIf()),
            Arrays.asList(this.visitStatementNoShortIf(ctx.statementNoShortIf()))
        );
        return new QueenWhileStatementNode(
            position,
            expression,
            blockStatements
        );
    }

    @Override
    public DoStatementNode visitDoStatement(QueenParser.DoStatementContext ctx) {
        final Position position = this.getPosition(ctx);
        final ExpressionNode expression = this.visitExpression(ctx.expression());
        final BlockStatements blockStatements = new QueenBlockStatements(
            getPosition(ctx.statement()),
            Arrays.asList(this.visitStatement(ctx.statement()))
        );
        return new QueenDoStatementNode(
            position,
            blockStatements,
            expression
        );
    }

    @Override
    public LabeledStatementNode visitLabeledStatement(QueenParser.LabeledStatementContext ctx) {
        final BlockStatements blockStatements= new QueenBlockStatements(
            getPosition(ctx.statement()),
            Arrays.asList(this.visitStatement(ctx.statement()))
        );
        return new QueenLabeledStatementNode(
            getPosition(ctx),
            ctx.Identifier().getText(),
            blockStatements
        );
    }

    @Override
    public LabeledStatementNode visitLabeledStatementNoShortIf(QueenParser.LabeledStatementNoShortIfContext ctx) {
        final BlockStatements blockStatements = new QueenBlockStatements(
            getPosition(ctx.statementNoShortIf()),
            Arrays.asList(this.visitStatementNoShortIf(ctx.statementNoShortIf()))
        );
        return new QueenLabeledStatementNode(
            getPosition(ctx),
            ctx.Identifier().getText(),
            blockStatements
        );
    }

    @Override
    public StatementNode visitStatementWithoutTrailingSubstatement(
        QueenParser.StatementWithoutTrailingSubstatementContext ctx
    ) {
        final StatementNode statementWithoutTrailingSubstatement;
        if (ctx.block() != null) {
            statementWithoutTrailingSubstatement =  this.visitBlock(ctx.block());
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
        return statementWithoutTrailingSubstatement;
    }

    @Override
    public ThrowStatementNode visitThrowStatement(QueenParser.ThrowStatementContext ctx) {
        return new QueenThrowStatementNode(
            getPosition(ctx),
            this.visitExpression(ctx.expression())
        );
    }

    @Override
    public ReturnStatementNode visitReturnStatement(QueenParser.ReturnStatementContext ctx) {
        return new QueenReturnStatementNode(
            getPosition(ctx),
            this.visitExpression(ctx.expression())
        );
    }

    @Override
    public ContinueStatementNode visitContinueStatement(QueenParser.ContinueStatementContext ctx) {
        return new QueenContinueStatementNode(
            getPosition(ctx),
            ctx.Identifier() != null ? ctx.Identifier().getText() : null
        );
    }

    @Override
    public BreakStatementNode visitBreakStatement(QueenParser.BreakStatementContext ctx) {
        return new QueenBreakStatementNode(
            getPosition(ctx),
            ctx.Identifier() != null ? ctx.Identifier().getText() : null
        );
    }

    @Override
    public EmptyStatementNode visitEmptyStatement(QueenParser.EmptyStatementContext ctx) {
        return new QueenEmptyStatementNode(
            getPosition(ctx)
        );
    }

    @Override
    public AssertStatementNode visitAssertStatement(QueenParser.AssertStatementContext ctx) {
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
    public SynchronizedStatementNode visitSynchronizedStatement(QueenParser.SynchronizedStatementContext ctx) {
        final Position position = getPosition(ctx);
        final ExpressionNode expression = this.visitExpression(ctx.expression());
        return new QueenSynchronizedStatementNode(
            position,
            expression,
            this.visitBlock(ctx.block())
        );
    }

    @Override
    public SwitchStatementNode visitSwitchStatement(QueenParser.SwitchStatementContext ctx) {
        final Position position = getPosition(ctx);
        final ExpressionNode expression = this.visitExpression(ctx.expression());
        final List<SwitchEntryNode> entries = new ArrayList<>();

        if(ctx.switchBlock() != null) {
            if(ctx.switchBlock().switchBlockStatementGroup() != null) {
                ctx.switchBlock().switchBlockStatementGroup().forEach(
                    sbsg -> {
                        final Position p = getPosition(sbsg);
                        final List<SwitchLabelNode> labels = new ArrayList<>();
                        final BlockStatements blockStatements;
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
                final List<SwitchLabelNode> labels = new ArrayList<>();
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
    public TryStatementNode visitTryStatement(QueenParser.TryStatementContext ctx) {
        if(ctx.tryWithResourcesStatement() != null) {
            return this.visitTryWithResourcesStatement(ctx.tryWithResourcesStatement());
        } else {
            final Position position = getPosition(ctx);
            final List<CatchClauseNode> catchClauses = new ArrayList<>();
            if(ctx.catches() != null) {
                ctx.catches().catchClause().forEach(
                    cc -> catchClauses.add(this.visitCatchClause(cc))
                );
            }
            return new QueenTryStatementNode(
                position,
                new ArrayList<>(),
                this.visitBlock(ctx.block()),
                catchClauses,
                ctx.finally_() != null ? this.visitFinally_(ctx.finally_()) : null
            );
        }
    }

    @Override
    public TryStatementNode visitTryWithResourcesStatement(QueenParser.TryWithResourcesStatementContext ctx) {
        final Position position = getPosition(ctx);
        final List<ExpressionNode> resources = new ArrayList<>();
        ctx.resourceSpecification().resourceList().resource().forEach(
            r -> resources.add(this.visitResource(r))
        );
        final List<CatchClauseNode> catchClauses = new ArrayList<>();
        if(ctx.catches() != null) {
            ctx.catches().catchClause().forEach(
                cc -> catchClauses.add(this.visitCatchClause(cc))
            );
        }
        return new QueenTryStatementNode(
            position,
            resources,
            this.visitBlock(ctx.block()),
            catchClauses,
            ctx.finally_() != null ? this.visitFinally_(ctx.finally_()) : null
        );
    }

    @Override
    public BlockStatements visitFinally_(QueenParser.Finally_Context ctx) {
        return this.visitBlock(ctx.block());
    }

    @Override
    public ExpressionNode visitResource(QueenParser.ResourceContext ctx) {
        final Position position = getPosition(ctx);
        final List<AnnotationNode> annotations = new ArrayList<>();
        final List<ModifierNode> modifiers = new ArrayList<>();
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
            Arrays.asList(
                new QueenVariableDeclaratorNode(
                    getPosition(ctx),
                    this.visitVariableDeclaratorId(ctx.variableDeclaratorId()),
                    this.visitExpression(ctx.expression())
                )
            )
        );
    }

    @Override
    public CatchClauseNode visitCatchClause(QueenParser.CatchClauseContext ctx) {
        final Position position = getPosition(ctx);
        final CatchFormalParameterNode parameter = this.visitCatchFormalParameter(ctx.catchFormalParameter());
        return new QueenCatchClauseNode(
            position,
            parameter,
            this.visitBlock(ctx.block())
        );
    }

    @Override
    public CatchFormalParameterNode visitCatchFormalParameter(QueenParser.CatchFormalParameterContext ctx) {
        final Position position = getPosition(ctx);
        final List<AnnotationNode> annotations = new ArrayList<>();
        final List<ModifierNode> modifiers = new ArrayList<>();
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
        final List<TypeNode> catchTypes = new ArrayList<>();
        ctx.catchType().unannClassType().forEach(
            ct -> catchTypes.add(this.visitUnannClassType(ct))
        );
        return new QueenCatchFormalParameterNode(
            position,
            annotations,
            modifiers,
            catchTypes,
            this.visitVariableDeclaratorId(ctx.variableDeclaratorId())
        );
    }

    @Override
    public StatementNode visitExpressionStatement(QueenParser.ExpressionStatementContext ctx) {
        return new QueenExpressionStatementNode(
            getPosition(ctx),
            this.visitStatementExpression(ctx.statementExpression())
        );
    }

    @Override
    public ExpressionNode visitStatementExpression(QueenParser.StatementExpressionContext ctx) {
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
    public ExpressionNode visitAssignment(QueenParser.AssignmentContext ctx) {
        final Position position = getPosition(ctx);
        final ExpressionNode target = this.visitLeftHandSide(ctx.leftHandSide());
        final String operator = asString(ctx.assignmentOperator()).trim().toUpperCase();
        final ExpressionNode value = this.visitExpression(ctx.expression());
        return new QueenAssignmentExpressionNode(
            position,
            target,
            operator,
            value
        );
    }

    @Override
    public ExpressionNode visitLeftHandSide(QueenParser.LeftHandSideContext ctx) {
        if(ctx.expressionName() != null) {
            return this.visitExpressionName(ctx.expressionName());
        } else if(ctx.arrayAccess() != null) {
            return this.visitArrayAccess(ctx.arrayAccess());
        } else {
            return this.visitFieldAccess(ctx.fieldAccess());
        }
    }

    @Override
    public ExpressionNode visitMethodInvocation(final QueenParser.MethodInvocationContext ctx) {
        final Position position = getPosition(ctx);
        final String name;
        if(ctx.methodName() != null) {
            name = ctx.methodName().Identifier().getText();
        } else {
            name = ctx.Identifier().getText();
        }
        final List<TypeNode> typeArguments = new ArrayList<>();
        if(ctx.typeArguments() != null) {
            ctx.typeArguments().typeArgumentList().typeArgument()
                .forEach(ta -> typeArguments.add(this.visitTypeArgument(ta)));
        }
        final List<ExpressionNode> arguments = new ArrayList<>();
        if(ctx.argumentList() != null) {
            ctx.argumentList().expression().forEach(
                e -> arguments.add(this.visitExpression(e))
            );
        }
        final ExpressionNode scope;
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

    public ExpressionNode visitMethodInvocation_lf_primary(ExpressionNode scope, QueenParser.MethodInvocation_lf_primaryContext ctx) {
        final Position position = getPosition(ctx);
        final String name = ctx.Identifier().getText();
        final List<TypeNode> typeArguments = new ArrayList<>();
        if(ctx.typeArguments() != null) {
            ctx.typeArguments().typeArgumentList().typeArgument()
                .forEach(ta -> typeArguments.add(this.visitTypeArgument(ta)));
        }
        final List<ExpressionNode> arguments = new ArrayList<>();
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
    public ExpressionNode visitMethodInvocation_lfno_primary(final QueenParser.MethodInvocation_lfno_primaryContext ctx) {
        final Position position = getPosition(ctx);
        final String name;
        if(ctx.methodName() != null) {
            name = ctx.methodName().Identifier().getText();
        } else {
            name = ctx.Identifier().getText();
        }
        final List<TypeNode> typeArguments = new ArrayList<>();
        if(ctx.typeArguments() != null) {
            ctx.typeArguments().typeArgumentList().typeArgument()
                .forEach(ta -> typeArguments.add(this.visitTypeArgument(ta)));
        }
        final List<ExpressionNode> arguments = new ArrayList<>();
        if(ctx.argumentList() != null) {
            ctx.argumentList().expression().forEach(
                e -> arguments.add(this.visitExpression(e))
            );
        }
        final ExpressionNode scope;
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
    public ExpressionNode visitMethodReference(QueenParser.MethodReferenceContext ctx) {
        final Position position = getPosition(ctx);
        final List<TypeNode> typeArguments = new ArrayList<>();
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
        final ExpressionNode scope;
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
        final TypeNode type;
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

    public ExpressionNode visitMethodReference_lf_primary(ExpressionNode scope, QueenParser.MethodReference_lf_primaryContext ctx) {
        final Position position = getPosition(ctx);
        final List<TypeNode> typeArguments = new ArrayList<>();
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
    public ExpressionNode visitMethodReference_lfno_primary(QueenParser.MethodReference_lfno_primaryContext ctx) {
        final Position position = getPosition(ctx);
        final List<TypeNode> typeArguments = new ArrayList<>();
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
        final ExpressionNode scope;
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
        final TypeNode type;
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
    public ExpressionNode visitClassInstanceCreationExpression(QueenParser.ClassInstanceCreationExpressionContext ctx) {
        final Position position = getPosition(ctx);
        final List<TypeNode> typeArguments = new ArrayList<>();
        if (ctx.typeArguments() != null) {
            ctx.typeArguments().typeArgumentList().typeArgument()
                .forEach(ta -> typeArguments.add(this.visitTypeArgument(ta)));
        }
        ExpressionNode scope = null;
        if (ctx.primary() != null) {
            scope = this.visitPrimary(ctx.primary());
        } else if (ctx.expressionName() != null) {
            scope = this.visitExpressionName(ctx.expressionName());
        }

        ClassOrInterfaceTypeNode type = null;
        for (int i=0; i < ctx.constructorIdentifier().size(); i++) {
            type = this.visitConstructorIdentifier(
                ctx.constructorIdentifier(i),
                i == ctx.constructorIdentifier().size() - 1 ? ctx.typeArgumentsOrDiamond() : null,
                type
            );
        }
        final List<ExpressionNode> arguments = new ArrayList<>();
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

    public ExpressionNode visitClassInstanceCreationExpression_lf_primary(ExpressionNode scope, QueenParser.ClassInstanceCreationExpression_lf_primaryContext ctx) {
        final Position position = getPosition(ctx);
        final List<TypeNode> typeArguments = new ArrayList<>();
        if (ctx.typeArguments() != null) {
            ctx.typeArguments().typeArgumentList().typeArgument()
                .forEach(ta -> typeArguments.add(this.visitTypeArgument(ta)));
        }

        ClassOrInterfaceTypeNode type = this.visitConstructorIdentifier(
            ctx.constructorIdentifier(),
            ctx.typeArgumentsOrDiamond(),
            null
        );
        final List<ExpressionNode> arguments = new ArrayList<>();
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
    public ExpressionNode visitClassInstanceCreationExpression_lfno_primary(QueenParser.ClassInstanceCreationExpression_lfno_primaryContext ctx) {
        final Position position = getPosition(ctx);
        final List<TypeNode> typeArguments = new ArrayList<>();
        if (ctx.typeArguments() != null) {
            ctx.typeArguments().typeArgumentList().typeArgument()
                .forEach(ta -> typeArguments.add(this.visitTypeArgument(ta)));
        }
        ExpressionNode scope = null;
        if (ctx.expressionName() != null) {
            scope = this.visitExpressionName(ctx.expressionName());
        }

        ClassOrInterfaceTypeNode type = null;
        for (int i=0; i < ctx.constructorIdentifier().size(); i++) {
            type = this.visitConstructorIdentifier(
                ctx.constructorIdentifier(i),
                i == ctx.constructorIdentifier().size() - 1 ? ctx.typeArgumentsOrDiamond() : null,
                type
            );
        }
        final List<ExpressionNode> arguments = new ArrayList<>();
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

    public ClassOrInterfaceTypeNode visitConstructorIdentifier(
        QueenParser.ConstructorIdentifierContext ctx,
        QueenParser.TypeArgumentsOrDiamondContext typeArgsOrDiamondCtx,
        ClassOrInterfaceTypeNode scope
    ) {
        final Position position = this.getPosition(ctx);
        final List<AnnotationNode> annotations = new ArrayList<>();
        ctx.annotation().forEach(
            a -> annotations.add(this.visitAnnotation(a))
        );
        final String name = ctx.Identifier().getText();
        final List<TypeNode> typeArguments = new ArrayList<>();
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
    public ExpressionNode visitArrayAccess(QueenParser.ArrayAccessContext ctx) {
        final ExpressionNode name;
        if(ctx.expressionName() != null) {
            name = this.visitExpressionName(ctx.expressionName());
        } else {
            name = this.visitPrimaryNoNewArray_lfno_arrayAccess(ctx.primaryNoNewArray_lfno_arrayAccess());
        }
        final List<ArrayDimensionNode> dims = new ArrayList<>();
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

    public ExpressionNode visitArrayAccess_lf_primary(ExpressionNode scope, QueenParser.ArrayAccess_lf_primaryContext ctx) {
        final ExpressionNode name = this.visitPrimaryNoNewArray_lf_primary_lfno_arrayAccess_lf_primary(
            scope, ctx.primaryNoNewArray_lf_primary_lfno_arrayAccess_lf_primary()
        );
        final List<ArrayDimensionNode> dims = new ArrayList<>();
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
    public ExpressionNode visitArrayAccess_lfno_primary(QueenParser.ArrayAccess_lfno_primaryContext ctx) {
        final ExpressionNode name;
        if(ctx.expressionName() != null) {
            name = this.visitExpressionName(ctx.expressionName());
        } else {
            name = this.visitPrimaryNoNewArray_lfno_primary_lfno_arrayAccess_lfno_primary(
                ctx.primaryNoNewArray_lfno_primary_lfno_arrayAccess_lfno_primary()
            );
        }
        final List<ArrayDimensionNode> dims = new ArrayList<>();
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

    public ExpressionNode visitPrimaryNoNewArray_lf_primary_lfno_arrayAccess_lf_primary(ExpressionNode scope, QueenParser.PrimaryNoNewArray_lf_primary_lfno_arrayAccess_lf_primaryContext ctx) {
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
    public ExpressionNode visitPrimaryNoNewArray_lfno_primary_lfno_arrayAccess_lfno_primary(QueenParser.PrimaryNoNewArray_lfno_primary_lfno_arrayAccess_lfno_primaryContext ctx) {
        if(ctx.literal() != null) {
            return this.visitLiteral(ctx.literal());
        } else if(ctx.typeName() != null && ctx.IMPLEMENTATION() != null) {
            final QueenNameNode typeName = this.visitTypeName(ctx.typeName());
            final List<ArrayDimensionNode> dims = new ArrayList<>();
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
            final PrimitiveTypeNode primitiveType = this.visitUnannPrimitiveType(ctx.unannPrimitiveType());
            final List<ArrayDimensionNode> dims = new ArrayList<>();
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
    public ExpressionNode visitFieldAccess(QueenParser.FieldAccessContext ctx) {
        final Position position = getPosition(ctx);
        final ExpressionNode scope;
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

    public ExpressionNode visitFieldAccess_lf_primary(ExpressionNode scope, QueenParser.FieldAccess_lf_primaryContext ctx) {
        return new QueenFieldAccessExpressionNode(
            getPosition(ctx),
            scope,
            ctx.Identifier().getText()
        );
    }

    @Override
    public ExpressionNode visitFieldAccess_lfno_primary(QueenParser.FieldAccess_lfno_primaryContext ctx) {
        final Position position = getPosition(ctx);
        final ExpressionNode scope;
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
    public ExpressionNode visitUnaryExpression(QueenParser.UnaryExpressionContext ctx) {
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
    public ExpressionNode visitMultiplicativeExpression(QueenParser.MultiplicativeExpressionContext ctx) {
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
    public ExpressionNode visitAdditiveExpression(QueenParser.AdditiveExpressionContext ctx) {
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
    public ExpressionNode visitShiftExpression(QueenParser.ShiftExpressionContext ctx) {
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
    public ExpressionNode visitRelationalExpression(QueenParser.RelationalExpressionContext ctx) {
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
    public ExpressionNode visitEqualityExpression(QueenParser.EqualityExpressionContext ctx) {
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
    public ExpressionNode visitAndExpression(QueenParser.AndExpressionContext ctx) {
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
    public ExpressionNode visitExclusiveOrExpression(final QueenParser.ExclusiveOrExpressionContext ctx) {
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
    public ExpressionNode visitInclusiveOrExpression(final QueenParser.InclusiveOrExpressionContext ctx) {
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
    public ExpressionNode visitConditionalAndExpression(final QueenParser.ConditionalAndExpressionContext ctx) {
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
    public ExpressionNode visitConditionalOrExpression(final QueenParser.ConditionalOrExpressionContext ctx) {
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
    public ExpressionNode visitConditionalExpression(final QueenParser.ConditionalExpressionContext ctx) {
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
    public ExpressionNode visitPreIncrementExpression(QueenParser.PreIncrementExpressionContext ctx) {
        return new QueenUnaryExpressionNode(
            getPosition(ctx),
            "++",
            true,
            this.visitUnaryExpression(ctx.unaryExpression())
        );
    }

    @Override
    public ExpressionNode visitPreDecrementExpression(QueenParser.PreDecrementExpressionContext ctx) {
        return new QueenUnaryExpressionNode(
            getPosition(ctx),
            "--",
            true,
            this.visitUnaryExpression(ctx.unaryExpression())
        );
    }

    @Override
    public ExpressionNode visitPostIncrementExpression(QueenParser.PostIncrementExpressionContext ctx) {
        return new QueenUnaryExpressionNode(
            getPosition(ctx),
            "++",
            false,
            this.visitPostfixExpression(ctx.postfixExpression())
        );
    }

    @Override
    public ExpressionNode visitPostDecrementExpression(QueenParser.PostDecrementExpressionContext ctx) {
        return new QueenUnaryExpressionNode(
            getPosition(ctx),
            "--",
            false,
            this.visitPostfixExpression(ctx.postfixExpression())
        );
    }

    @Override
    public ExpressionNode visitUnaryExpressionNotPlusMinus(QueenParser.UnaryExpressionNotPlusMinusContext ctx) {
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
    public ExpressionNode visitCastExpression(QueenParser.CastExpressionContext ctx) {
        if(ctx.primitiveType() != null) {
            return new QueenCastExpressionNode(
                getPosition(ctx),
                this.visitPrimitiveType(ctx.primitiveType()),
                Arrays.asList(),
                this.visitUnaryExpression(ctx.unaryExpression())
            );
        } else {
            final List<ReferenceTypeNode> referenceTypes = new ArrayList<>();
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
    public ExpressionNode visitLambdaExpression(QueenParser.LambdaExpressionContext ctx) {
        final Position position = getPosition(ctx);
        final List<ParameterNode> parameters = new ArrayList<>();
        boolean enclosedParameters = false;
        if(ctx.lambdaParameters() != null) {
            if(ctx.lambdaParameters().LPAREN() != null) {
                enclosedParameters = true;
            }
            if(ctx.lambdaParameters().Identifier() != null) {
                parameters.add(
                    new QueenParameterNode(
                        getPosition(ctx.lambdaParameters()),
                        new QueenVariableDeclaratorId(
                            getPosition(ctx.lambdaParameters()),
                            ctx.lambdaParameters().Identifier().getText()
                        )
                    )
                );
            } else if(ctx.lambdaParameters().inferredFormalParameterList() != null) {
                ctx.lambdaParameters().inferredFormalParameterList().Identifier().forEach(
                    inferred -> parameters.add(
                        new QueenParameterNode(
                            getPosition(ctx.lambdaParameters().inferredFormalParameterList()),
                            new QueenVariableDeclaratorId(
                                getPosition(ctx.lambdaParameters().inferredFormalParameterList()),
                                inferred.getText()
                            )
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
        final ExpressionNode expression;
        final BlockStatements queenBlockStatements;
        if (ctx.lambdaBody().block() != null) {
            queenBlockStatements = this.visitBlock(ctx.lambdaBody().block());
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
    public ExpressionNode visitPostfixExpression(QueenParser.PostfixExpressionContext ctx) {
        ExpressionNode postfixExpression;
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
    public InterfaceBodyNode visitInterfaceBody(QueenParser.InterfaceBodyContext ctx) {
        final List<InterfaceMemberDeclarationNode> members = new ArrayList<>();
        if(ctx.interfaceMemberDeclaration() != null) {
            ctx.interfaceMemberDeclaration().forEach(
                imd -> members.add(this.visitInterfaceMemberDeclaration(imd))
            );
        }
        return new QueenInterfaceBodyNode(getPosition(ctx), members);
    }

    @Override
    public AnnotationTypeBodyNode visitAnnotationTypeBody(QueenParser.AnnotationTypeBodyContext ctx) {
        final List<AnnotationTypeMemberDeclarationNode> members = new ArrayList<>();
        if(ctx.annotationTypeMemberDeclaration() != null) {
            ctx.annotationTypeMemberDeclaration().forEach(
                amd -> members.add(this.visitAnnotationTypeMemberDeclaration(amd))
            );
        }
        return new QueenAnnotationTypeBodyNode(getPosition(ctx), members);
    }

    @Override
    public InterfaceMemberDeclarationNode visitInterfaceMemberDeclaration(QueenParser.InterfaceMemberDeclarationContext ctx) {
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
    public AnnotationTypeMemberDeclarationNode visitAnnotationTypeMemberDeclaration(QueenParser.AnnotationTypeMemberDeclarationContext ctx) {
        if(ctx.classDeclaration() != null) {
            return this.visitClassDeclaration(ctx.classDeclaration());
        } else if(ctx.interfaceDeclaration() != null) {
            return this.visitInterfaceDeclaration(ctx.interfaceDeclaration());
        }
        return this.visitAnnotationTypeElementDeclaration(ctx.annotationTypeElementDeclaration());
    }

    @Override
    public AnnotationElementDeclarationNode visitAnnotationTypeElementDeclaration(QueenParser.AnnotationTypeElementDeclarationContext ctx) {
        final List<AnnotationNode> annotations = new ArrayList<>();
        final List<ModifierNode> modifiers = new ArrayList<>();
        ctx.annotation().forEach(
            a -> annotations.add(this.visitAnnotation(a))
        );
        ctx.annotationTypeElementModifier().forEach(
            m -> modifiers.add(this.visitAnnotationTypeElementModifier(m))
        );
        return new QueenAnnotationElementDeclarationNode(
            this.getPosition(ctx),
            annotations,
            modifiers,
            this.visitUnannType(ctx.unannType()),
            ctx.Identifier().getText(),
            ctx.defaultValue() != null ? this.visitElementValue(ctx.defaultValue().elementValue()) : null
        );
    }

    @Override
    public TypeParameterNode visitTypeParameter(QueenParser.TypeParameterContext ctx) {
        final Position position = this.getPosition(ctx);
        final List<AnnotationNode> annotations = new ArrayList<>();
        ctx.annotation().forEach(
            a -> annotations.add(this.visitAnnotation(a))
        );
        final String name = ctx.Identifier().getText();
        final List<ClassOrInterfaceTypeNode> typeBound = new ArrayList<>();
        if(ctx.typeBound() != null) {
            final QueenParser.TypeBoundContext typeBoundContext = ctx.typeBound();
            if(typeBoundContext.typeVariable() != null) {
                typeBound.add(
                    this.visitTypeVariable(typeBoundContext.typeVariable())
                );
            }
            if(typeBoundContext.classOrInterfaceType() != null) {
                typeBound.add(
                    this.visitClassOrInterfaceType(typeBoundContext.classOrInterfaceType())
                );
            }
            if(typeBoundContext.additionalBound() != null) {
                typeBoundContext.additionalBound().forEach(
                    ab -> typeBound.add(
                        this.visitInterfaceType(ab.interfaceType())
                    )
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
    public ClassOrInterfaceTypeNode visitSuperclass(QueenParser.SuperclassContext ctx) {
        return this.visitClassType(ctx.classType());
    }

    @Override
    public ClassOrInterfaceTypeNode visitClassType(QueenParser.ClassTypeContext ctx) {
        final Position position = this.getPosition(ctx);
        final List<AnnotationNode> annotations = new ArrayList<>();
        ctx.annotation().forEach(
            a -> annotations.add(this.visitAnnotation(a))
        );
        final String name = ctx.Identifier().getText();
        final List<TypeNode> typeArguments = new ArrayList<>();
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
    public ClassOrInterfaceTypeNode visitUnannClassType(QueenParser.UnannClassTypeContext ctx) {
        final Position position = this.getPosition(ctx);
        final List<AnnotationNode> annotations = new ArrayList<>();
        ctx.annotation().forEach(
            a -> annotations.add(this.visitAnnotation(a))
        );
        final String name = ctx.Identifier().getText();
        final List<TypeNode> typeArguments = new ArrayList<>();
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
    public ClassOrInterfaceTypeNode visitInterfaceType(QueenParser.InterfaceTypeContext ctx) {
        final Position position = this.getPosition(ctx);
        final List<AnnotationNode> annotations = new ArrayList<>();
        ctx.classType().annotation().forEach(
            a -> annotations.add(this.visitAnnotation(a))
        );
        final String name = ctx.classType().Identifier().getText();
        final List<TypeNode> typeArguments = new ArrayList<>();
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
    public TypeNode visitTypeArgument(QueenParser.TypeArgumentContext ctx) {
        if(ctx.referenceType() != null) {
            return this.visitReferenceType(ctx.referenceType());
        } else {
            return this.visitWildcard(ctx.wildcard());
        }
    }

    @Override
    public ReferenceTypeNode visitReferenceType(QueenParser.ReferenceTypeContext ctx) {
        if(ctx.classOrInterfaceType() != null) {
            return this.visitClassOrInterfaceType(ctx.classOrInterfaceType());
        } else if(ctx.typeVariable() != null) {
            return this.visitTypeVariable(ctx.typeVariable());
        } else {
            return this.visitArrayType(ctx.arrayType());
        }
    }

    @Override
    public ClassOrInterfaceTypeNode visitTypeVariable(QueenParser.TypeVariableContext ctx) {
        final Position position = this.getPosition(ctx);
        final List<AnnotationNode> annotations = new ArrayList<>();
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
    public ClassOrInterfaceTypeNode visitUnannTypeVariable(QueenParser.UnannTypeVariableContext ctx) {
        return this.visitUnannTypeVariable(new ArrayList<>(), ctx);
    }

    public ClassOrInterfaceTypeNode visitUnannTypeVariable(List<AnnotationNode> annotations, QueenParser.UnannTypeVariableContext ctx) {
        final Position position = this.getPosition(ctx);
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
    public ExceptionTypeNode visitExceptionType(QueenParser.ExceptionTypeContext ctx) {
        if(ctx.classType() != null) {
            return new QueenExceptionTypeNode(this.visitClassType(ctx.classType()));
        } else {
            return new QueenExceptionTypeNode(this.visitTypeVariable(ctx.typeVariable()));
        }
    }

    @Override
    public PrimitiveTypeNode visitPrimitiveType(QueenParser.PrimitiveTypeContext ctx) {
        final Position position = this.getPosition(ctx);
        final List<AnnotationNode> annotations = new ArrayList<>();
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
    public ClassOrInterfaceTypeNode visitClassOrInterfaceType(QueenParser.ClassOrInterfaceTypeContext ctx) {
        ClassOrInterfaceTypeNode classOrInterfaceTypeNode = null;
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
            final List<AnnotationNode> annotations = new ArrayList<>();
            part.annotation().forEach(
                a -> annotations.add(this.visitAnnotation(a))
            );
            final String name = part.Identifier().getText();
            final List<TypeNode> typeArguments = new ArrayList<>();
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
    public ClassOrInterfaceTypeNode visitClassType_lfno_classOrInterfaceType(QueenParser.ClassType_lfno_classOrInterfaceTypeContext ctx) {
        final Position position = this.getPosition(ctx);
        final List<AnnotationNode> annotations = new ArrayList<>();
        ctx.annotation().forEach(
            a -> annotations.add(this.visitAnnotation(a))
        );
        final String name = ctx.Identifier().getText();
        final List<TypeNode> typeArguments = new ArrayList<>();
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
    public ArrayTypeNode visitArrayType(QueenParser.ArrayTypeContext ctx) {
        final Position position = this.getPosition(ctx);
        final TypeNode type;
        if(ctx.primitiveType() != null) {
            type = this.visitPrimitiveType(ctx.primitiveType());
        } else if(ctx.classOrInterfaceType() != null) {
            type = this.visitClassOrInterfaceType(ctx.classOrInterfaceType());
        } else {
            type = this.visitTypeVariable(ctx.typeVariable());
        }
        final List<ArrayDimensionNode> dims = new ArrayList<>();
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
    public ArrayTypeNode visitUnannArrayType(QueenParser.UnannArrayTypeContext ctx) {
        return this.visitUnannArrayType(new ArrayList<>(), ctx);
    }

    public ArrayTypeNode visitUnannArrayType(List<AnnotationNode> annotations, QueenParser.UnannArrayTypeContext ctx) {
        final Position position = this.getPosition(ctx);
        final TypeNode type;
        if(ctx.unannPrimitiveType() != null) {
            type = this.visitUnannPrimitiveType(annotations, ctx.unannPrimitiveType());
        } else if(ctx.unannClassOrInterfaceType() != null) {
            type = this.visitUnannClassOrInterfaceType(annotations, ctx.unannClassOrInterfaceType());
        } else {
            type = this.visitUnannTypeVariable(annotations, ctx.unannTypeVariable());
        }
        final List<ArrayDimensionNode> dims = new ArrayList<>();
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
    public WildcardTypeNode visitWildcard(QueenParser.WildcardContext ctx) {
        final Position position = this.getPosition(ctx);
        final List<AnnotationNode> annotations = new ArrayList<>();
        ctx.annotation().forEach(
            a -> annotations.add(this.visitAnnotation(a))
        );
        ReferenceTypeNode superType = null;
        ReferenceTypeNode extendedType = null;
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
    public TypeNode visitUnannType(QueenParser.UnannTypeContext ctx) {
        return this.visitUnannType(new ArrayList<>(), ctx);
    }

    /**
     * There can still be annotations on an unannType, in the case of annotated result/return type
     * for a method which also has type parameters (see methodHeader definition),
     */
    public TypeNode visitUnannType(List<AnnotationNode> annotations, QueenParser.UnannTypeContext ctx) {
        if(ctx.unannPrimitiveType() != null) {
            return this.visitUnannPrimitiveType(annotations, ctx.unannPrimitiveType());
        } else {
            return this.visitUnannReferenceType(annotations, ctx.unannReferenceType());
        }
    }

    @Override
    public PrimitiveTypeNode visitUnannPrimitiveType(QueenParser.UnannPrimitiveTypeContext ctx) {
        return this.visitUnannPrimitiveType(new ArrayList<>(), ctx);
    }

    public PrimitiveTypeNode visitUnannPrimitiveType(List<AnnotationNode> annotations, QueenParser.UnannPrimitiveTypeContext ctx) {
        final Position position = this.getPosition(ctx);
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
    public ReferenceTypeNode visitUnannReferenceType(QueenParser.UnannReferenceTypeContext ctx) {
        return this.visitUnannReferenceType(new ArrayList<>(), ctx);
    }

    public ReferenceTypeNode visitUnannReferenceType(List<AnnotationNode> annotations, QueenParser.UnannReferenceTypeContext ctx) {
        if(ctx.unannClassOrInterfaceType() != null) {
            return this.visitUnannClassOrInterfaceType(annotations, ctx.unannClassOrInterfaceType());
        } else if(ctx.unannTypeVariable() != null) {
            return this.visitUnannTypeVariable(annotations, ctx.unannTypeVariable());
        } else {
            return this.visitUnannArrayType(annotations, ctx.unannArrayType());
        }
    }

    @Override
    public ClassOrInterfaceTypeNode visitUnannClassOrInterfaceType(QueenParser.UnannClassOrInterfaceTypeContext ctx) {
        return this.visitUnannClassOrInterfaceType(new ArrayList<>(), ctx);
    }

    public ClassOrInterfaceTypeNode visitUnannClassOrInterfaceType(List<AnnotationNode> annotations, QueenParser.UnannClassOrInterfaceTypeContext ctx) {
        ClassOrInterfaceTypeNode unannClassOrInterfaceTypeNode = null;
        if(ctx.unannClassType_lfno_unannClassOrInterfaceType() != null) {
            unannClassOrInterfaceTypeNode = this.visitUnannClassType_lfno_unannClassOrInterfaceType(annotations, ctx.unannClassType_lfno_unannClassOrInterfaceType());
        } else if(ctx.unannInterfaceType_lfno_unannClassOrInterfaceType() != null) {
            unannClassOrInterfaceTypeNode = this.visitUnannClassType_lfno_unannClassOrInterfaceType(
                annotations,
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
            final List<AnnotationNode> lfAnnotations = new ArrayList<>();
            part.annotation().forEach(
                a -> lfAnnotations.add(this.visitAnnotation(a))
            );
            final String name = part.Identifier().getText();
            final List<TypeNode> typeArguments = new ArrayList<>();
            if(part.typeArguments() != null) {
                part.typeArguments().typeArgumentList().typeArgument()
                    .forEach(ta -> typeArguments.add(this.visitTypeArgument(ta)));
            }
            unannClassOrInterfaceTypeNode = new QueenClassOrInterfaceTypeNode(
                position,
                false,
                unannClassOrInterfaceTypeNode,
                lfAnnotations,
                name,
                typeArguments,
                false
            );
        }
        return unannClassOrInterfaceTypeNode;
    }

    public ClassOrInterfaceTypeNode visitUnannClassType_lfno_unannClassOrInterfaceType(
        List<AnnotationNode> annotations,
        QueenParser.UnannClassType_lfno_unannClassOrInterfaceTypeContext ctx
    ) {
        final Position position = this.getPosition(ctx);
        final String name = ctx.Identifier().getText();
        final List<TypeNode> typeArguments = new ArrayList<>();
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


    /**
     * Return context as String. getText is not enough for non-terminal nodes,
     * because it does not preseve the original spacing/indentation.
     *
     * On terminal nodes such as Identifier() and FINAL(), getText is fine to use.
     *
     * <b>ATTENTION!</b><br><br>
     * This method is to be used only on nodes such as access modifiers, operators, primitive type names
     * or for debugging/quick PoC purposes. All complex nodes should be modeled as an AST Node
     * and not read as String.
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
