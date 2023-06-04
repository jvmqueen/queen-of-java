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

import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.*;
import org.queenlang.transpiler.nodes.NameNode;
import org.queenlang.transpiler.nodes.body.*;
import org.queenlang.transpiler.nodes.expressions.*;
import org.queenlang.transpiler.nodes.statements.*;
import org.queenlang.transpiler.nodes.types.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Turn a Queen AST into Java AST.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenToJavaVisitor implements QueenASTVisitor<Node> {

    @Override
    public CompilationUnit visitCompilationUnit(final CompilationUnitNode node) {
        final CompilationUnit cu = new CompilationUnit();
        if(node.packageDeclaration() != null) {
            cu.setPackageDeclaration(this.visitPackageDeclarationNode(node.packageDeclaration()));
        }
        node.importDeclarations().forEach(
            i -> cu.addImport(this.visitImportDeclarationNode(i))
        );
        cu.addType((TypeDeclaration<?>) this.visitTypeDeclarationNode(node.typeDeclaration()));
        return cu;
    }

    @Override
    public ImportDeclaration visitImportDeclarationNode(final ImportDeclarationNode node) {
        return new ImportDeclaration(node.importDeclarationName().name(), false, node.asteriskImport());
    }

    @Override
    public PackageDeclaration visitPackageDeclarationNode(final PackageDeclarationNode node) {
        return new PackageDeclaration(this.visitNameNode(node.packageName()));
    }

    @Override
    public Node visitTypeDeclarationNode(final TypeDeclarationNode node) {
        if(node instanceof ClassDeclarationNode) {
            return this.visitClassDeclarationNode((ClassDeclarationNode) node);
        } else {
            return this.visitInterfaceDeclarationNode((InterfaceDeclarationNode) node);
        }
    }

    @Override
    public ClassOrInterfaceDeclaration visitClassDeclarationNode(final ClassDeclarationNode node) {
        final ClassOrInterfaceDeclaration clazz = new ClassOrInterfaceDeclaration();
        clazz.setInterface(false);
        clazz.setName(node.name());
        node.annotations().forEach(
            a -> clazz.addAnnotation(this.visitAnnotationNode(a))
        );
        node.modifiers().forEach(
            m -> clazz.addModifier(this.visitModifierNode(m).getKeyword())
        );
        clazz.addModifier(this.visitModifierNode(node.extensionModifier()).getKeyword());
        if(node.extendsType() != null) {
            clazz.addExtendedType(this.visitClassOrInterfaceTypeNode(node.extendsType()));
        }
        if(node.of() != null) {
            node.of().forEach(
                o -> clazz.addImplementedType(this.visitClassOrInterfaceTypeNode(o))
            );
        }
        node.typeParameters().forEach(
            tp -> clazz.addTypeParameter(this.visitTypeParameterNode(tp))
        );
        node.body().classBodyDeclarations().forEach(
            cmd -> clazz.addMember((BodyDeclaration<?>) this.visitClassBodyDeclarationNode(cmd))
        );
        return clazz;
    }

    @Override
    public Node visitInterfaceDeclarationNode(final InterfaceDeclarationNode node) {
        if(node instanceof NormalInterfaceDeclarationNode) {
            return this.visitNormalInterfaceDeclarationNode((NormalInterfaceDeclarationNode) node);
        } else {
            return this.visitAnnotationTypeDeclarationNode((AnnotationTypeDeclarationNode) node);
        }
    }

    @Override
    public ClassOrInterfaceDeclaration visitNormalInterfaceDeclarationNode(final NormalInterfaceDeclarationNode node) {
        final ClassOrInterfaceDeclaration normalInterface = new ClassOrInterfaceDeclaration();
        normalInterface.setInterface(true);
        normalInterface.setName(node.name());
        node.annotations().forEach(
            a -> normalInterface.addAnnotation(this.visitAnnotationNode(a))
        );
        node.modifiers().forEach(
            m -> normalInterface.addModifier(this.visitModifierNode(m).getKeyword())
        );
        if(node.extendsTypes() != null) {
            node.extendsTypes().forEach(
                et -> normalInterface.addExtendedType(this.visitClassOrInterfaceTypeNode(et))
            );
        }
        node.typeParameters().forEach(
            tp -> normalInterface.addTypeParameter(this.visitTypeParameterNode(tp))
        );
        node.body().interfaceMemberDeclarations().forEach(
            imd -> normalInterface.addMember((BodyDeclaration<?>) this.visitInterfaceMemberDeclarationNode(imd))
        );
        return normalInterface;
    }

    @Override
    public Node visitInterfaceMemberDeclarationNode(final InterfaceMemberDeclarationNode node) {
        if(node instanceof TypeDeclarationNode) {
            return this.visitTypeDeclarationNode((TypeDeclarationNode) node);
        } else if(node instanceof ConstantDeclarationNode) {
            return this.visitConstantDeclarationNode((ConstantDeclarationNode) node);
        } else {
            return this.visitInterfaceMethodDeclarationNode((InterfaceMethodDeclarationNode) node);
        }
    }

    @Override
    public Node visitClassBodyDeclarationNode(final ClassBodyDeclarationNode node) {
        if(node instanceof TypeDeclarationNode) {
            return this.visitTypeDeclarationNode((TypeDeclarationNode) node);
        } else if(node instanceof FieldDeclarationNode) {
            return this.visitFieldDeclarationNode((FieldDeclarationNode) node);
        } else if (node instanceof InstanceInitializerNode) {
            return this.visitInstanceInitializerNode((InstanceInitializerNode) node);
        } else if(node instanceof ConstructorDeclarationNode) {
            return this.visitConstructorDeclarationNode((ConstructorDeclarationNode) node);
        } else {
            return this.visitMethodDeclarationNode((MethodDeclarationNode) node);
        }
    }

    @Override
    public InitializerDeclaration visitInstanceInitializerNode(final InstanceInitializerNode node) {
        final InitializerDeclaration instanceInit = new InitializerDeclaration();
        instanceInit.setStatic(node.isStatic());
        instanceInit.setBody(this.visitBlockStatements(node.blockStatements()));
        return instanceInit;
    }

    @Override
    public ConstructorDeclaration visitConstructorDeclarationNode(final ConstructorDeclarationNode node) {
        final ConstructorDeclaration constructorDeclaration = new ConstructorDeclaration();
        node.annotations().forEach(
            a -> constructorDeclaration.addAnnotation(this.visitAnnotationNode(a))
        );
        constructorDeclaration.addModifier(this.visitModifierNode(node.modifier()).getKeyword());
        constructorDeclaration.setName(node.name());
        node.typeParams().forEach(
            tp -> constructorDeclaration.addTypeParameter(this.visitTypeParameterNode(tp))
        );
        node.parameters().forEach(
            p -> constructorDeclaration.addParameter(this.visitParameterNode(p))
        );
        node.throwsList().forEach(
            t -> constructorDeclaration.addThrownException((ReferenceType) this.visitTypeNode(t))
        );
        final BlockStmt blockStmt = new BlockStmt();
        if(node.explicitConstructorInvocationNode() != null) {
            blockStmt.addStatement(this.visitExplicitConstructorInvocationNode(node.explicitConstructorInvocationNode()));
        }
        if(node.blockStatements() != null) {
            final BlockStmt block = this.visitBlockStatements(node.blockStatements());
            block.getStatements().forEach(
                blockStmt::addStatement
            );
        }
        constructorDeclaration.setBody(blockStmt);
        return constructorDeclaration;
    }

    @Override
    public Node visitConstantDeclarationNode(final ConstantDeclarationNode node) {
        final FieldDeclaration fd = new FieldDeclaration();
        node.annotations().forEach(
            a -> fd.addAnnotation(this.visitAnnotationNode(a))
        );
        node.modifiers().forEach(
            m -> fd.addModifier(this.visitModifierNode(m).getKeyword())
        );
        final VariableDeclarator vd = new VariableDeclarator();
        vd.setType(this.visitTypeNode(node.type()));
        vd.setName(node.variable().variableDeclaratorId().name());
        vd.setInitializer(this.visitExpressionNode(node.variable().initializer()));
        fd.addVariable(vd);
        return fd;
    }

    @Override
    public MethodDeclaration visitInterfaceMethodDeclarationNode(final InterfaceMethodDeclarationNode node) {
        final MethodDeclaration md = new MethodDeclaration();
        node.annotations().forEach(
            a -> md.addAnnotation(this.visitAnnotationNode(a))
        );
        node.modifiers().forEach(
            m -> md.addModifier(this.visitModifierNode(m).getKeyword())
        );
        md.setType(this.visitTypeNode(node.returnType()));
        md.setName(node.name());
        node.parameters().forEach(
            p -> md.addParameter(this.visitParameterNode(p))
        );
        node.typeParameters().forEach(
            tp -> md.addTypeParameter(this.visitTypeParameterNode(tp))
        );
        node.throwsList().forEach(
            t -> md.addThrownException(this.visitReferenceTypeNode(t))
        );
        md.setBody(this.visitBlockStatements(node.blockStatements()));
        return md;
    }

    @Override
    public TypeParameter visitTypeParameterNode(final TypeParameterNode node) {
        final TypeParameter tp = new TypeParameter();
        node.annotations().forEach(
            a -> tp.addAnnotation(this.visitAnnotationNode(a))
        );
        tp.setName(node.name());
        tp.setTypeBound(
            new NodeList<>(
                node.typeBound().stream().map(
                    tb -> (ClassOrInterfaceType) this.visitTypeNode(tb)
                ).collect(Collectors.toList())
            )
        );

        return tp;
    }

    @Override
    public MethodDeclaration visitMethodDeclarationNode(final MethodDeclarationNode node) {
        final MethodDeclaration md = new MethodDeclaration();
        node.annotations().forEach(
            a -> md.addAnnotation(this.visitAnnotationNode(a))
        );
        node.modifiers().forEach(
            m -> md.addModifier(this.visitModifierNode(m).getKeyword())
        );
        md.setType(this.visitTypeNode(node.returnType()));
        md.setName(node.name());
        node.parameters().forEach(
            p -> md.addParameter(this.visitParameterNode(p))
        );
        node.typeParameters().forEach(
            tp -> md.addTypeParameter(this.visitTypeParameterNode(tp))
        );
        node.throwsList().forEach(
            t -> md.addThrownException(this.visitReferenceTypeNode(t))
        );
        md.setBody(this.visitBlockStatements(node.blockStatements()));
        return md;
    }

    @Override
    public Parameter visitParameterNode(final ParameterNode node) {
        final Parameter parameter = new Parameter();
        if(node.type() != null) {
            parameter.setType(this.visitTypeNode(node.type()));
        } else {
            parameter.setType(new UnknownType());
        }
        parameter.setName(node.variableDeclaratorId().name());
        node.annotations().forEach(
            a -> parameter.addAnnotation(this.visitAnnotationNode(a))
        );
        node.modifiers().forEach(
            m -> {
                if(!m.modifier().equals("mutable")) {
                    parameter.addModifier(this.visitModifierNode(m).getKeyword());
                }
            }
        );
        parameter.setVarArgs(node.varArgs());
        parameter.setVarArgsAnnotations(new NodeList<>(node.varArgsAnnotations().stream().map(
            this::visitAnnotationNode
        ).collect(Collectors.toList())));
        return parameter;
    }

    @Override
    public Node visitFieldDeclarationNode(final FieldDeclarationNode node) {
        final FieldDeclaration fd = new FieldDeclaration();
        node.annotations().forEach(
            a -> fd.addAnnotation(this.visitAnnotationNode(a))
        );
        node.modifiers().forEach(
            m -> {
                if(!m.modifier().equals("mutable")) {
                    fd.addModifier(this.visitModifierNode(m).getKeyword());
                }
            }
        );
        final VariableDeclarator vd = new VariableDeclarator();
        vd.setType(this.visitTypeNode(node.type()));
        vd.setName(node.variable().variableDeclaratorId().name());
        vd.setInitializer(this.visitExpressionNode(node.variable().initializer()));
        fd.addVariable(vd);
        return fd;
    }

    @Override
    public AnnotationDeclaration visitAnnotationTypeDeclarationNode(final AnnotationTypeDeclarationNode node) {
        final AnnotationDeclaration annotationDeclaration = new AnnotationDeclaration();
        annotationDeclaration.setName(node.name());
        node.annotations().forEach(
            a -> annotationDeclaration.addAnnotation(this.visitAnnotationNode(a))
        );
        node.modifiers().forEach(
            m -> annotationDeclaration.addModifier(this.visitModifierNode(m).getKeyword())
        );
        node.body().annotationMemberDeclarations().forEach(
            amd -> annotationDeclaration.addMember((BodyDeclaration<?>) this.visitAnnotationTypeMemberDeclarationNode(amd))
        );
        return annotationDeclaration;
    }

    @Override
    public Node visitAnnotationTypeMemberDeclarationNode(final AnnotationTypeMemberDeclarationNode node) {
        if(node instanceof TypeDeclarationNode) {
            return this.visitTypeDeclarationNode((TypeDeclarationNode) node);
        } else {
            return this.visitAnnotationElementDeclarationNode((AnnotationElementDeclarationNode) node);
        }
    }

    @Override
    public Node visitAnnotationElementDeclarationNode(final AnnotationElementDeclarationNode node) {
        final AnnotationMemberDeclaration amd = new AnnotationMemberDeclaration();
        amd.setName(node.name());
        node.annotations().forEach(
            a -> amd.addAnnotation(this.visitAnnotationNode(a))
        );
        node.modifiers().forEach(
            m -> amd.addModifier(this.visitModifierNode(m).getKeyword())
        );
        amd.setType(this.visitTypeNode(node.type()));
        amd.setDefaultValue(this.visitExpressionNode(node.defaultValue()));
        return amd;
    }

    @Override
    public AnnotationExpr visitAnnotationNode(final AnnotationNode node) {
        if(node instanceof NormalAnnotationNode) {
            return this.visitNormalAnnotationNode((NormalAnnotationNode) node);
        } else if(node instanceof SingleMemberAnnotationNode) {
            return this.visitSingleMemberAnnotationNode((SingleMemberAnnotationNode) node);
        } else {
            return this.visitMarkerAnnotationNode((MarkerAnnotationNode) node);
        }
    }

    @Override
    public NormalAnnotationExpr visitNormalAnnotationNode(final NormalAnnotationNode node) {
        final NormalAnnotationExpr normalAnnotationExpr = new NormalAnnotationExpr();
        normalAnnotationExpr.setName(this.visitNameNode(node.nameNode()));
        node.elementValuePairs().forEach(
            evp -> normalAnnotationExpr.addPair(evp.identifier(), this.visitExpressionNode(evp.expression()))
        );
        return normalAnnotationExpr;
    }

    @Override
    public SingleMemberAnnotationExpr visitSingleMemberAnnotationNode(final SingleMemberAnnotationNode node) {
        final SingleMemberAnnotationExpr single = new SingleMemberAnnotationExpr();
        single.setName(this.visitNameNode(node.nameNode()));
        single.setMemberValue(this.visitExpressionNode(node.elementValue()));
        return single;
    }

    @Override
    public MarkerAnnotationExpr visitMarkerAnnotationNode(final MarkerAnnotationNode node) {
        return new MarkerAnnotationExpr(this.visitNameNode(node.nameNode()));
    }

    @Override
    public BlockStmt visitBlockStatements(final BlockStatements node) {
        if(node == null) {
            return null;
        }
        final BlockStmt blockStmt = new BlockStmt();
        for(final StatementNode stmt : node) {
            blockStmt.addStatement(this.visitStatementNode(stmt));
        }
        return blockStmt;
    }

    @Override
    public Name visitNameNode(final NameNode node) {
        if(node == null) {
            return null;
        }
        if(node.qualifier() == null) {
            return new Name(node.identifier());
        } else {
            return new Name(
                this.visitNameNode(node.qualifier()),
                node.identifier()
            );
        }
    }

    @Override
    public Type visitTypeNode(final TypeNode node) {
        if(node instanceof PrimitiveTypeNode) {
            return this.visitPrimitiveTypeNode((PrimitiveTypeNode) node);
        } else if(node instanceof ReferenceTypeNode) {
            return this.visitReferenceTypeNode((ReferenceTypeNode) node);
        } else if(node instanceof VoidTypeNode) {
            return this.visitVoidTypeNode((VoidTypeNode) node);
        } else if(node instanceof WildcardTypeNode) {
            return this.visitWildcardTypeNode((WildcardTypeNode) node);
        }
        return null;
    }

    @Override
    public ReferenceType visitReferenceTypeNode(final ReferenceTypeNode node) {
        if(node instanceof ArrayTypeNode) {
            return this.visitArrayTypeNode((ArrayTypeNode) node);
        } else if(node instanceof ClassOrInterfaceTypeNode) {
            return this.visitClassOrInterfaceTypeNode((ClassOrInterfaceTypeNode) node);
        } else if(node instanceof ExceptionTypeNode) {
            return this.visitExceptionTypeNode((ExceptionTypeNode) node);
        }
        return null;
    }

    @Override
    public ClassOrInterfaceType visitClassOrInterfaceTypeNode(final ClassOrInterfaceTypeNode node) {
        final ClassOrInterfaceType classOrInterfaceType = new ClassOrInterfaceType();
        classOrInterfaceType.setName(node.identifier());
        node.annotations().forEach(
            a -> classOrInterfaceType.addAnnotation(this.visitAnnotationNode(a))
        );
        if(node.qualifier() != null) {
            classOrInterfaceType.setScope(this.visitClassOrInterfaceTypeNode(node.qualifier()));
        }
        if(node.hasDiamondOperator()) {
            classOrInterfaceType.setDiamondOperator();
        } else {
            if(node.typeArguments() != null && !node.typeArguments().isEmpty()) {
                classOrInterfaceType.setTypeArguments(
                    new NodeList<>(
                        node.typeArguments().stream().map(
                            this::visitTypeNode
                        ).collect(Collectors.toList())
                    )
                );
            }
        }
        return classOrInterfaceType;
    }

    @Override
    public ArrayType visitArrayTypeNode(final ArrayTypeNode node) {
        ArrayType arrayType = new ArrayType(
            this.visitTypeNode(node.type())
        );
        for(final AnnotationNode annotation : node.dims().get(node.dims().size() - 1).annotations()) {
            arrayType.addAnnotation(this.visitAnnotationNode(annotation));
        }
        for(int i = node.dims().size() - 2; i>=0; i--) {
            arrayType = new ArrayType(
                arrayType
            );
            for(final AnnotationNode annotation : node.dims().get(i).annotations()) {
                arrayType.addAnnotation(this.visitAnnotationNode(annotation));
            }
        }
        return arrayType;
    }

    @Override
    public ClassOrInterfaceType visitExceptionTypeNode(final ExceptionTypeNode node) {
        return this.visitClassOrInterfaceTypeNode(node.exceptionType());
    }

    @Override
    public PrimitiveType visitPrimitiveTypeNode(final PrimitiveTypeNode node) {
        final PrimitiveType primitiveType = new PrimitiveType(
            PrimitiveType.Primitive.valueOf(node.name().toUpperCase())
        );
        node.annotations().forEach(
            a -> primitiveType.addAnnotation(this.visitAnnotationNode(a))
        );
        return primitiveType;
    }

    @Override
    public VoidType visitVoidTypeNode(final VoidTypeNode node) {
        final VoidType vt = new VoidType();
        node.annotations().forEach(
            a -> vt.addAnnotation(this.visitAnnotationNode(a))
        );
        return vt;
    }

    @Override
    public WildcardType visitWildcardTypeNode(final WildcardTypeNode node) {
        final WildcardType wt = new WildcardType();
        node.annotations().forEach(
            a -> wt.addAnnotation(this.visitAnnotationNode(a))
        );
        wt.setExtendedType(this.visitReferenceTypeNode(node.extendedType()));
        wt.setSuperType(this.visitReferenceTypeNode(node.superType()));
        return wt;
    }

    @Override
    public Expression visitExpressionNode(final ExpressionNode node) {
        if(node == null) {
            return null;
        }
        if(node instanceof AnnotationNode) {
            return this.visitAnnotationNode((AnnotationNode) node);
        } else if(node instanceof ArrayAccessExpressionNode) {
            return this.visitArrayAccessExpressionNode((ArrayAccessExpressionNode) node);
        } else if(node instanceof ArrayCreationExpressionNode) {
            return this.visitArrayCreationExpressionNode((ArrayCreationExpressionNode) node);
        } else if(node instanceof ArrayInitializerExpressionNode) {
            return this.visitArrayInitializerExpressionNode((ArrayInitializerExpressionNode) node);
        } else if(node instanceof AssignmentExpressionNode) {
            return this.visitAssignmentExpressionNode((AssignmentExpressionNode) node);
        } else if(node instanceof BinaryExpressionNode) {
            return this.visitBinaryExpressionNode((BinaryExpressionNode) node);
        } else if(node instanceof BooleanLiteralExpressionNode) {
            return this.visitBooleanLiteralExpressionNode((BooleanLiteralExpressionNode) node);
        } else if(node instanceof BracketedExpressionNode) {
            return this.visitBracketedExpressionNode((BracketedExpressionNode) node);
        } else if(node instanceof CastExpressionNode) {
            return this.visitCastExpressionNode((CastExpressionNode) node);
        } else if(node instanceof CharLiteralExpressionNode) {
            return this.visitCharLiteralExpressionNode((CharLiteralExpressionNode) node);
        } else if(node instanceof ConditionalExpressionNode) {
            return this.visitConditionalExpressionNode((ConditionalExpressionNode) node);
        } else if(node instanceof DoubleLiteralExpressionNode) {
            return this.visitDoubleLiteralExpressionNode((DoubleLiteralExpressionNode) node);
        } else if(node instanceof FieldAccessExpressionNode) {
            return this.visitFieldAccessExpressionNode((FieldAccessExpressionNode) node);
        } else if(node instanceof InstanceOfExpressionNode) {
            return this.visitInstanceOfExpressionNode((InstanceOfExpressionNode) node);
        } else if(node instanceof IntegerLiteralExpressionNode) {
            return this.visitIntegerLiteralExpressionNode((IntegerLiteralExpressionNode) node);
        } else if(node instanceof LambdaExpressionNode) {
            return this.visitLambdaExpressionNode((LambdaExpressionNode) node);
        } else if(node instanceof LocalVariableDeclarationNode) {
            return this.visitLocalVariableDeclarationNode((LocalVariableDeclarationNode) node);
        } else if(node instanceof LongLiteralExpressionNode) {
            return this.visitLongLiteralExpressionNode((LongLiteralExpressionNode) node);
        } else if(node instanceof MethodInvocationExpressionNode) {
            return this.visitMethodInvocationExpressionNode((MethodInvocationExpressionNode) node);
        } else if(node instanceof MethodReferenceExpressionNode) {
            return this.visitMethodReferenceExpressionNode((MethodReferenceExpressionNode) node);
        } else if(node instanceof NameNode) {
            final NameNode name = (NameNode) node;
            if(name.qualifier() == null) {
                return new NameExpr(name.identifier());
            } else {
                return new FieldAccessExpr(
                    this.visitExpressionNode(name.qualifier()),
                    name.identifier()
                );
            }
        } else if(node instanceof NullLiteralExpressionNode) {
            return this.visitNullLiteralExpressionNode((NullLiteralExpressionNode) node);
        } else if(node instanceof ObjectCreationExpressionNode) {
            return this.visitObjectCreationExpressionNode((ObjectCreationExpressionNode) node);
        } else if(node instanceof StringLiteralExpressionNode) {
            return this.visitStringLiteralExpressionNode((StringLiteralExpressionNode) node);
        } else if(node instanceof ThisExpressionNode) {
            return this.visitThisExpressionNode((ThisExpressionNode) node);
        } else if(node instanceof SuperExpressionNode) {
            return this.visitSuperExpressionNode((SuperExpressionNode) node);
        } else if(node instanceof TypeImplementationExpressionNode) {
            return this.visitTypeImplementationExpressionNode((TypeImplementationExpressionNode) node);
        } else if(node instanceof UnaryExpressionNode) {
            return this.visitUnaryExpressionNode((UnaryExpressionNode) node);
        }
        return null;
    }

    @Override
    public BinaryExpr visitBinaryExpressionNode(final BinaryExpressionNode node) {
        BinaryExpr.Operator operator = null;
        for(int i=0; i< BinaryExpr.Operator.values().length; i++) {
            if(BinaryExpr.Operator.values()[i].asString().equalsIgnoreCase(node.operator())) {
                operator = BinaryExpr.Operator.values()[i];
                break;
            }
        }
        if(operator == null) {
            throw new IllegalStateException("Unknown operator: " + node.operator());
        }
        return new BinaryExpr(
            this.visitExpressionNode(node.left()),
            this.visitExpressionNode(node.right()),
            operator
        );
    }

    @Override
    public ThisExpr visitThisExpressionNode(final ThisExpressionNode node) {
        return new ThisExpr(
            this.visitNameNode(node.typeName())
        );
    }

    @Override
    public SuperExpr visitSuperExpressionNode(final SuperExpressionNode node) {
        return new SuperExpr(
            this.visitNameNode(node.typeName())
        );
    }

    @Override
    public MethodReferenceExpr visitMethodReferenceExpressionNode(final MethodReferenceExpressionNode node) {
        final MethodReferenceExpr methodReferenceExpr = new MethodReferenceExpr();
        if(node.type() != null) {
            methodReferenceExpr.setScope(
                new TypeExpr(this.visitTypeNode(node.type()))
            );
        } else {
            methodReferenceExpr.setScope(
                this.visitExpressionNode(node.scope())
            );
        }
        if(node.typeArguments() != null && node.typeArguments().size() > 0) {
            methodReferenceExpr.setTypeArguments(
                new NodeList<>(
                    node.typeArguments().stream().map(
                        this::visitTypeNode
                    ).collect(Collectors.toList())
                )
            );
        }
        return methodReferenceExpr;
    }

    @Override
    public ArrayAccessExpr visitArrayAccessExpressionNode(final ArrayAccessExpressionNode node) {
        ArrayAccessExpr arrayAccessExpr = new ArrayAccessExpr();
        arrayAccessExpr.setName(this.visitExpressionNode(node.name()));
        arrayAccessExpr.setIndex(this.visitExpressionNode(node.dims().get(0).expression()));
        for(int i = 1; i<node.dims().size(); i++) {
            arrayAccessExpr = new ArrayAccessExpr(
                arrayAccessExpr,
                this.visitExpressionNode(node.dims().get(i).expression())
            );
        }
        return arrayAccessExpr;
    }

    @Override
    public ArrayInitializerExpr visitArrayInitializerExpressionNode(final ArrayInitializerExpressionNode node) {
        final ArrayInitializerExpr arrayInitializerExpr = new ArrayInitializerExpr();
        if(node.values() != null) {
            arrayInitializerExpr.setValues(new NodeList<>(
                node.values().stream().map(
                    this::visitExpressionNode
                ).collect(Collectors.toList())
            ));
        }
        return arrayInitializerExpr;
    }

    @Override
    public ArrayCreationExpr visitArrayCreationExpressionNode(final ArrayCreationExpressionNode node) {
        final List<ArrayCreationLevel> javaDims = new ArrayList<>();
        node.dims().forEach(
            d -> {
                final ArrayCreationLevel javaDim = new ArrayCreationLevel();
                if(d.expression() != null) {
                    javaDim.setDimension(this.visitExpressionNode(d.expression()));
                }
                d.annotations().forEach(
                    a -> javaDim.addAnnotation(this.visitAnnotationNode(a))
                );
                javaDims.add(javaDim);
            }
        );
        return new ArrayCreationExpr(
            this.visitTypeNode(node.type()),
            new NodeList<>(javaDims),
            node.arrayInitializer() != null
                ? this.visitArrayInitializerExpressionNode((ArrayInitializerExpressionNode) node.arrayInitializer())
                : null

        );
    }

    @Override
    public MethodCallExpr visitMethodInvocationExpressionNode(final MethodInvocationExpressionNode node) {
        final MethodCallExpr methodCallExpr = new MethodCallExpr();
        methodCallExpr.setName(node.name());
        if(node.scope() != null) {
            methodCallExpr.setScope(this.visitExpressionNode(node.scope()));
        }
        if(node.typeArguments() != null && node.typeArguments().size() > 0) {
            methodCallExpr.setTypeArguments(
                new NodeList<>(
                    node.typeArguments().stream().map(
                        this::visitTypeNode
                    ).collect(Collectors.toList())
                )
            );
        }
        if(node.arguments() != null) {
            methodCallExpr.setArguments(
                new NodeList<>(
                    node.arguments().stream().map(
                        this::visitExpressionNode
                    ).collect(Collectors.toList())
                )
            );
        }
        return methodCallExpr;
    }

    @Override
    public LambdaExpr visitLambdaExpressionNode(final LambdaExpressionNode node) {
        final LambdaExpr lambdaExpr = new LambdaExpr();
        lambdaExpr.setEnclosingParameters(node.enclosedParameters());
        if(node.parameters() != null && !node.parameters().isEmpty()) {
            node.parameters().forEach(
                p -> lambdaExpr.addParameter(this.visitParameterNode(p))
            );
            if(node.parameters().size() > 1) {
                lambdaExpr.setEnclosingParameters(true);
            }
        } else {
            lambdaExpr.setEnclosingParameters(true);
        }
        if(node.blockStatements() != null) {
            lambdaExpr.setBody(this.visitBlockStatements(node.blockStatements()));
        } else {
            lambdaExpr.setBody(new ExpressionStmt(this.visitExpressionNode(node.expression())));
        }
        return lambdaExpr;
    }

    @Override
    public InstanceOfExpr visitInstanceOfExpressionNode(final InstanceOfExpressionNode node) {
        final InstanceOfExpr instanceOfExpr = new InstanceOfExpr();
        instanceOfExpr.setExpression(this.visitExpressionNode(node.expression()));
        instanceOfExpr.setType(this.visitReferenceTypeNode(node.referenceType()));
        return instanceOfExpr;
    }

    @Override
    public ConditionalExpr visitConditionalExpressionNode(final ConditionalExpressionNode node) {
        return new ConditionalExpr(
            this.visitExpressionNode(node.condition()),
            this.visitExpressionNode(node.thenExpr()),
            this.visitExpressionNode(node.elseExpr())
        );
    }

    @Override
    public CastExpr visitCastExpressionNode(final CastExpressionNode node) {
        if(node.primitiveType() != null) {
            return new CastExpr(
                this.visitTypeNode(node.primitiveType()),
                this.visitExpressionNode(node.expression())
            );
        } else {
            final List<ReferenceType> javaTypes = new ArrayList<>();
            node.referenceTypes().forEach(
                rt -> javaTypes.add(this.visitReferenceTypeNode(rt))
            );
            final IntersectionType type = new IntersectionType(
                new NodeList<>(javaTypes)
            );
            return new CastExpr(
                type,
                this.visitExpressionNode(node.expression())
            );
        }
    }

    @Override
    public EnclosedExpr visitBracketedExpressionNode(final BracketedExpressionNode node) {
        return new EnclosedExpr(this.visitExpressionNode(node.expression()));
    }

    @Override
    public FieldAccessExpr visitFieldAccessExpressionNode(final FieldAccessExpressionNode node) {
        final FieldAccessExpr fieldAccessExpr = new FieldAccessExpr();
        if(node.scope() != null) {
            fieldAccessExpr.setScope(this.visitExpressionNode(node.scope()));
        }
        fieldAccessExpr.setName(node.name());
        return fieldAccessExpr;
    }

    @Override
    public AssignExpr visitAssignmentExpressionNode(final AssignmentExpressionNode node) {
        AssignExpr.Operator operator = null;
        for(int i=0; i< AssignExpr.Operator.values().length; i++) {
            if(AssignExpr.Operator.values()[i].asString().equalsIgnoreCase(node.operator())) {
                operator = AssignExpr.Operator.values()[i];
                break;
            }
        }
        if(operator == null) {
            throw new IllegalStateException("Unknown assignment operator: " + node.operator());
        }

        return new AssignExpr(
            this.visitExpressionNode(node.target()),
            this.visitExpressionNode(node.value()),
            operator
        );
    }

    @Override
    public ObjectCreationExpr visitObjectCreationExpressionNode(final ObjectCreationExpressionNode node) {
        final ObjectCreationExpr objectCreationExpr = new ObjectCreationExpr();
        if(node.scope() != null) {
            objectCreationExpr.setScope(this.visitExpressionNode(node.scope()));
        }
        objectCreationExpr.setType(this.visitClassOrInterfaceTypeNode(node.type()));
        if(node.typeArguments() != null && node.typeArguments().size() > 0) {
            objectCreationExpr.setTypeArguments(
                new NodeList<>(
                    node.typeArguments().stream().map(
                        this::visitTypeNode
                    ).collect(Collectors.toList())
                )
            );
        }
        if(node.arguments() != null) {
            objectCreationExpr.setArguments(
                new NodeList<>(
                    node.arguments().stream().map(
                        this::visitExpressionNode
                    ).collect(Collectors.toList())
                )
            );
        }
        if(node.anonymousBody() != null) {
            final ClassBodyNode body = node.anonymousBody();
            if(!body.isEmpty()) {
                objectCreationExpr.setAnonymousClassBody(
                    new NodeList<>(
                        body.classBodyDeclarations().stream().map(
                            cbd -> (BodyDeclaration<?>) this.visitClassBodyDeclarationNode(cbd)
                        ).collect(Collectors.toList())
                    )
                );
            } else {
                objectCreationExpr.setAnonymousClassBody(new NodeList<>());
            }
        }
        return objectCreationExpr;
    }

    @Override
    public ClassExpr visitTypeImplementationExpressionNode(final TypeImplementationExpressionNode node) {
        if(node.dims() != null && !node.dims().isEmpty()) {
            return new ClassExpr(
                this.visitTypeNode(
                    new QueenArrayTypeNode(
                        node.position(),
                        node.type(),
                        node.dims()
                    )
                )
            );
        } else {
            return new ClassExpr(this.visitTypeNode(node.type()));
        }
    }

    @Override
    public NullLiteralExpr visitNullLiteralExpressionNode(final NullLiteralExpressionNode node) {
        return new NullLiteralExpr();
    }

    @Override
    public StringLiteralExpr visitStringLiteralExpressionNode(final StringLiteralExpressionNode node) {
        return new StringLiteralExpr(node.value());
    }

    @Override
    public CharLiteralExpr visitCharLiteralExpressionNode(final CharLiteralExpressionNode node) {
        return new CharLiteralExpr(node.value());
    }

    @Override
    public BooleanLiteralExpr visitBooleanLiteralExpressionNode(final BooleanLiteralExpressionNode node) {
        return new BooleanLiteralExpr(node.value());
    }

    @Override
    public IntegerLiteralExpr visitIntegerLiteralExpressionNode(final IntegerLiteralExpressionNode node) {
        return new IntegerLiteralExpr(node.value());
    }

    @Override
    public DoubleLiteralExpr visitDoubleLiteralExpressionNode(final DoubleLiteralExpressionNode node) {
        return new DoubleLiteralExpr(node.value());
    }

    @Override
    public LongLiteralExpr visitLongLiteralExpressionNode(final LongLiteralExpressionNode node) {
        return new LongLiteralExpr(node.value());
    }

    @Override
    public UnaryExpr visitUnaryExpressionNode(final UnaryExpressionNode node) {
        UnaryExpr.Operator operator = null;
        for(int i=0; i< UnaryExpr.Operator.values().length; i++) {
            final UnaryExpr.Operator candidate = UnaryExpr.Operator.values()[i];
            if(candidate.asString().equalsIgnoreCase(node.operator()) && candidate.isPrefix() == node.isPrefix()) {
                operator = UnaryExpr.Operator.values()[i];
                break;
            }
        }
        if(operator == null) {
            throw new IllegalStateException("Unknown unary operator: " + node.operator() + ". (is prefix: " + node.isPrefix() + ").");
        }
        return new UnaryExpr(
            this.visitExpressionNode(node.expression()),
            operator
        );
    }

    @Override
    public Statement visitStatementNode(final StatementNode node) {
        if(node instanceof ClassDeclarationNode) {
            return new LocalClassDeclarationStmt(this.visitClassDeclarationNode((ClassDeclarationNode) node));
        } else if(node instanceof AssertStatementNode) {
            return this.visitAssertStatementNode((AssertStatementNode) node);
        } else if(node instanceof ContinueStatementNode) {
            return this.visitContinueStatementNode((ContinueStatementNode) node);
        } else if(node instanceof ReturnStatementNode) {
            return this.visitReturnStatementNode((ReturnStatementNode) node);
        } else if(node instanceof BreakStatementNode) {
            return this.visitBreakStatementNode((BreakStatementNode) node);
        } else if(node instanceof IfStatementNode) {
            return this.visitIfStatementNode((IfStatementNode) node);
        } else if(node instanceof WhileStatementNode) {
            return this.visitWhileStatementNode((WhileStatementNode) node);
        } else if(node instanceof DoStatementNode) {
            return this.visitDoStatementNode((DoStatementNode) node);
        } else if(node instanceof ForStatementNode) {
            return this.visitForStatementNode((ForStatementNode) node);
        } else if(node instanceof ForEachStatementNode) {
            return this.visitForEachStatementNode((ForEachStatementNode) node);
        } else if (node instanceof BlockStatements) {
            return this.visitBlockStatements((BlockStatements) node);
        } else if (node instanceof SynchronizedStatementNode) {
            return this.visitSynchronizedStatementNode((SynchronizedStatementNode) node);
        } else if (node instanceof ThrowStatementNode) {
            return this.visitThrowStatementNode((ThrowStatementNode) node);
        } else if (node instanceof TryStatementNode) {
            return this.visitTryStatementNode((TryStatementNode) node);
        } else if (node instanceof LocalVariableDeclarationNode) {
            return new ExpressionStmt(this.visitLocalVariableDeclarationNode((LocalVariableDeclarationNode) node));
        } else if (node instanceof LabeledStatementNode) {
            return this.visitLabeledStatementNode((LabeledStatementNode) node);
        } else if (node instanceof EmptyStatementNode) {
            return this.visitEmptyStatementNode((EmptyStatementNode) node);
        } else if (node instanceof ExpressionStatementNode) {
            return this.visitExpressionStatementNode((ExpressionStatementNode) node);
        } else if (node instanceof ExplicitConstructorInvocationNode) {
            return this.visitExplicitConstructorInvocationNode((ExplicitConstructorInvocationNode) node);
        } else if(node instanceof SwitchStatementNode) {
            return this.visitSwitchStatementNode((SwitchStatementNode) node);
        }
        return null;
    }

    @Override
    public ExpressionStmt visitExpressionStatementNode(final ExpressionStatementNode node) {
        return new ExpressionStmt(this.visitExpressionNode(node.expression()));
    }

    @Override
    public EmptyStmt visitEmptyStatementNode(final EmptyStatementNode node) {
        return new EmptyStmt();
    }

    @Override
    public LabeledStmt visitLabeledStatementNode(final LabeledStatementNode node) {
        return new LabeledStmt(node.name(), this.visitStatementNode(node.blockStatements()));
    }

    @Override
    public IfStmt visitIfStatementNode(final IfStatementNode node) {
        final IfStmt ifStmt = new IfStmt();
        ifStmt.setCondition(this.visitExpressionNode(node.condition()));
        ifStmt.setThenStmt(this.visitStatementNode(node.thenBlockStatements()));
        ifStmt.setElseStmt(this.visitStatementNode(node.elseBlockStatements()));
        return ifStmt;
    }

    @Override
    public ForStmt visitForStatementNode(final ForStatementNode node) {
        final ForStmt forStmt = new ForStmt();
        forStmt.setInitialization(
            new NodeList<>(
                node.initialization().stream().map(
                    this::visitExpressionNode
                ).collect(Collectors.toList())
            )
        );
        forStmt.setCompare(this.visitExpressionNode(node.comparison()));
        forStmt.setUpdate(
            new NodeList<>(
                node.update().stream().map(
                    this::visitExpressionNode
                ).collect(Collectors.toList())
            )
        );
        forStmt.setBody(this.visitStatementNode(node.blockStatements()));
        return forStmt;
    }

    @Override
    public ForEachStmt visitForEachStatementNode(final ForEachStatementNode node) {
        final ForEachStmt forEachStmt = new ForEachStmt();
        forEachStmt.setVariable(this.visitLocalVariableDeclarationNode(node.variable()));
        forEachStmt.setIterable(this.visitExpressionNode(node.iterable()));
        forEachStmt.setBody(this.visitStatementNode(node.blockStatements()));
        return forEachStmt;
    }

    @Override
    public WhileStmt visitWhileStatementNode(final WhileStatementNode node) {
        final WhileStmt whileStmt = new WhileStmt();
        whileStmt.setCondition(this.visitExpressionNode(node.expression()));
        whileStmt.setBody(this.visitStatementNode(node.blockStatements()));
        return whileStmt;
    }

    @Override
    public DoStmt visitDoStatementNode(final DoStatementNode node) {
        final DoStmt doStmt = new DoStmt();
        doStmt.setBody(this.visitStatementNode(node.blockStatements()));
        doStmt.setCondition(this.visitExpressionNode(node.expression()));
        return doStmt;
    }

    @Override
    public ThrowStmt visitThrowStatementNode(final ThrowStatementNode node) {
        final ThrowStmt throwStmt = new ThrowStmt();
        throwStmt.setExpression(this.visitExpressionNode(node.expression()));
        return throwStmt;
    }

    @Override
    public TryStmt visitTryStatementNode(final TryStatementNode node) {
        final TryStmt tryStmt = new TryStmt();
        tryStmt.setResources(
            new NodeList<>(
                node.resources().stream().map(
                    this::visitExpressionNode
                ).collect(Collectors.toList())
            )
        );
        tryStmt.setTryBlock(this.visitBlockStatements(node.tryBlockStatements()));
        tryStmt.setCatchClauses(
            new NodeList<>(
                node.catchClauses().stream().map(
                    this::visitCatchClauseNode
                ).collect(Collectors.toList())
            )
        );
        tryStmt.setFinallyBlock(this.visitBlockStatements(node.finallyBlockStatements()));
        return tryStmt;
    }

    @Override
    public CatchClause visitCatchClauseNode(final CatchClauseNode node) {
        final CatchClause catchClause = new CatchClause();
        catchClause.setParameter(this.visitCatchFormalParameterNode(node.parameter()));
        catchClause.setBody(this.visitBlockStatements(node.blockStatements()));
        return catchClause;
    }

    @Override
    public Parameter visitCatchFormalParameterNode(final CatchFormalParameterNode node) {
        final Parameter parameter = new Parameter();
        node.annotations().forEach(
            a -> parameter.addAnnotation(this.visitAnnotationNode(a))
        );
        node.modifiers().forEach(
            m -> parameter.addModifier(this.visitModifierNode(m).getKeyword())
        );
        final UnionType unionType = new UnionType();
        unionType.setElements(
            new NodeList<>(
                node.catchExceptionTypes().stream().map(
                    ct -> (ReferenceType) this.visitTypeNode(ct)
                ).collect(Collectors.toList())
            )
        );
        parameter.setType(unionType);
        parameter.setName(node.exceptionName().name());
        return parameter;
    }

    @Override
    public Modifier visitModifierNode(final ModifierNode node) {
        return new Modifier(Modifier.Keyword.valueOf(node.modifier().toUpperCase()));
    }

    @Override
    public SwitchStmt visitSwitchStatementNode(final SwitchStatementNode node) {
        final SwitchStmt switchStmt = new SwitchStmt();
        switchStmt.setSelector(this.visitExpressionNode(node.expression()));
        switchStmt.setEntries(
            new NodeList<>(
                node.entries().stream().map(
                    this::visitSwitchEntryNode
                ).collect(Collectors.toList())
            )
        );
        return switchStmt;
    }

    @Override
    public SwitchEntry visitSwitchEntryNode(final SwitchEntryNode node) {
        final SwitchEntry switchEntry = new SwitchEntry();
        switchEntry.setLabels(
            new NodeList<>(
                node.labels().stream().filter(l -> !l.isDefaultLabel()).map(
                    this::visitSwitchLabelNode
                ).collect(Collectors.toList())
            )
        );
        switchEntry.setStatements(
            this.visitBlockStatements(node.blockStatements()).getStatements()
        );
        return switchEntry;
    }

    @Override
    public Expression visitSwitchLabelNode(final SwitchLabelNode node) {
        return this.visitExpressionNode(node.expressionNode());
    }

    @Override
    public VariableDeclarationExpr visitLocalVariableDeclarationNode(final LocalVariableDeclarationNode node) {
        VariableDeclarationExpr vde = new VariableDeclarationExpr();
        node.annotations().forEach(
            a -> vde.addAnnotation(this.visitAnnotationNode(a))
        );
        node.modifiers().forEach(
            m -> vde.addModifier(this.visitModifierNode(m).getKeyword())
        );
        final List<VariableDeclarator> variableDeclarators = new ArrayList<>();
        node.variables().forEach(
            v -> {
                final VariableDeclarator vd = new VariableDeclarator();
                vd.setType(this.visitTypeNode(node.type()));
                vd.setName(v.variableDeclaratorId().name());
                vd.setInitializer(this.visitExpressionNode(v.initializer()));
                variableDeclarators.add(vd);
            }
        );
        vde.setVariables(new NodeList<>(variableDeclarators));
        return vde;
    }

    @Override
    public AssertStmt visitAssertStatementNode(final AssertStatementNode node) {
        final AssertStmt assertStmt = new AssertStmt();
        assertStmt.setCheck(this.visitExpressionNode(node.check()));
        assertStmt.setMessage(this.visitExpressionNode(node.message()));
        return assertStmt;
    }

    @Override
    public ContinueStmt visitContinueStatementNode(final ContinueStatementNode node) {
        final ContinueStmt continueStmt = new ContinueStmt();
        continueStmt.setLabel(node.label());
        return continueStmt;
    }

    @Override
    public BreakStmt visitBreakStatementNode(final BreakStatementNode node) {
        final BreakStmt breakStmt = new BreakStmt();
        if(node.label() != null) {
            breakStmt.setLabel(new SimpleName(node.label()));
        }
        return breakStmt;
    }

    @Override
    public ReturnStmt visitReturnStatementNode(final ReturnStatementNode node) {
        final ReturnStmt returnStmt = new ReturnStmt();
        returnStmt.setExpression(this.visitExpressionNode(node.expression()));
        return returnStmt;
    }

    @Override
    public SynchronizedStmt visitSynchronizedStatementNode(final SynchronizedStatementNode node) {
        final SynchronizedStmt synchronizedStmt = new SynchronizedStmt();
        synchronizedStmt.setExpression(this.visitExpressionNode(node.syncExpression()));
        synchronizedStmt.setBody(this.visitBlockStatements(node.blockStatements()));
        return synchronizedStmt;
    }

    @Override
    public ExplicitConstructorInvocationStmt visitExplicitConstructorInvocationNode(final ExplicitConstructorInvocationNode node) {
        final ExplicitConstructorInvocationStmt explicitConstructorInvocationStmt = new ExplicitConstructorInvocationStmt();
        explicitConstructorInvocationStmt.setThis(node.isThis());
        explicitConstructorInvocationStmt.setExpression(this.visitExpressionNode(node.scope()));
        explicitConstructorInvocationStmt.setArguments(
            new NodeList<>(
                node.arguments().stream().map(
                    this::visitExpressionNode
                ).collect(Collectors.toList())
            )
        );
        explicitConstructorInvocationStmt.setTypeArguments(
            new NodeList<>(
                node.typeArguments().stream().map(
                    this::visitTypeNode
                ).collect(Collectors.toList())
            )
        );
        return explicitConstructorInvocationStmt;
    }

    @Override
    public Node visitNodeWithParameters(NodeWithParameters node) {
        return null;
    }

    @Override
    public Node visitNodeWithTypeParameters(NodeWithTypeParameters node) {
        return null;
    }

    @Override
    public Node visitNodeWithTypeArguments(NodeWithTypeArguments node) {
        return null;
    }

    @Override
    public Node visitNodeWithFieldDeclarations(NodeWithFieldDeclarations node) {
        return null;
    }

    @Override
    public Node visitNodeWithConstructors(NodeWithConstructors node) {
        return null;
    }

    @Override
    public Node visitNodeWithThrows(NodeWithThrows node) {
        return null;
    }

    @Override
    public Node visitNodeWithModifiers(NodeWithModifiers node) {
        return null;
    }

    @Override
    public Node visitNodeWithAnnotations(NodeWithAnnotations node) {
        return null;
    }

    @Override
    public Node defaultResult() {
        return null;
    }

    @Override
    public Node aggregateResult(Node aggregate, Node nextResult) {
        return aggregate;
    }
}
