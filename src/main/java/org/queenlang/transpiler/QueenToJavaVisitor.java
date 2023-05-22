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
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.TypeParameter;
import org.checkerframework.checker.units.qual.A;
import org.queenlang.transpiler.nodes.NameNode;
import org.queenlang.transpiler.nodes.body.*;
import org.queenlang.transpiler.nodes.expressions.*;
import org.queenlang.transpiler.nodes.statements.*;
import org.queenlang.transpiler.nodes.types.NodeWithTypeParameters;
import org.queenlang.transpiler.nodes.types.TypeNode;

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
        return new PackageDeclaration(node.packageName().toName());
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
            a -> clazz.addAnnotation((AnnotationExpr) this.visitAnnotationNode(a))
        );
        node.modifiers().forEach(
            m -> clazz.addModifier(Modifier.Keyword.valueOf(m.modifier().toUpperCase()))
        );
        if(node.extendsType() != null) {
            clazz.addExtendedType((ClassOrInterfaceType) this.visitClassOrInterfaceTypeNode(node.extendsType()));
        }
        node.of().forEach(
            o -> clazz.addImplementedType((ClassOrInterfaceType) this.visitClassOrInterfaceTypeNode(o))
        );
        node.typeParameters().forEach(
            tp -> clazz.addTypeParameter((TypeParameter) this.visitTypeParameterNode(tp))
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
            a -> normalInterface.addAnnotation((AnnotationExpr) this.visitAnnotationNode(a))
        );
        node.modifiers().forEach(
            m -> normalInterface.addModifier(Modifier.Keyword.valueOf(m.modifier().toUpperCase()))
        );
        node.extendsTypes().forEach(
            et -> normalInterface.addExtendedType((ClassOrInterfaceType) this.visitClassOrInterfaceTypeNode(et))
        );
        node.typeParameters().forEach(
            tp -> normalInterface.addTypeParameter((TypeParameter) this.visitTypeParameterNode(tp))
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
    public Node visitConstantDeclarationNode(final ConstantDeclarationNode node) {
        final FieldDeclaration fd = new FieldDeclaration();
        node.annotations().forEach(
            a -> fd.addAnnotation((AnnotationExpr) this.visitAnnotationNode(a))
        );
        node.modifiers().forEach(
            m -> fd.addModifier(Modifier.Keyword.valueOf(m.modifier().toUpperCase()))
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
            a -> md.addAnnotation((AnnotationExpr) this.visitAnnotationNode(a))
        );
        node.modifiers().forEach(
            m -> md.addModifier(Modifier.Keyword.valueOf(m.modifier().toUpperCase()))
        );
        md.setType(this.visitTypeNode(node.returnType()));
        md.setName(node.name());
        node.parameters().forEach(
            p -> md.addParameter((Parameter) this.visitParameterNode(p))
        );
        node.typeParameters().forEach(
            tp -> md.addTypeParameter((TypeParameter) this.visitTypeParameterNode(tp))
        );
        node.throwsList().forEach(
            t -> md.addThrownException((ReferenceType) this.visitReferenceTypeNode(t))
        );
        md.setBody(this.visitBlockStatements(node.blockStatements()));
        return md;
    }

    @Override
    public MethodDeclaration visitMethodDeclarationNode(final MethodDeclarationNode node) {
        final MethodDeclaration md = new MethodDeclaration();
        node.annotations().forEach(
            a -> md.addAnnotation((AnnotationExpr) this.visitAnnotationNode(a))
        );
        node.modifiers().forEach(
            m -> md.addModifier(Modifier.Keyword.valueOf(m.modifier().toUpperCase()))
        );
        md.setType((Type) this.visitTypeNode(node.returnType()));
        md.setName(node.name());
        node.parameters().forEach(
            p -> md.addParameter((Parameter) this.visitParameterNode(p))
        );
        node.typeParameters().forEach(
            tp -> md.addTypeParameter((TypeParameter) this.visitTypeParameterNode(tp))
        );
        node.throwsList().forEach(
            t -> md.addThrownException((ReferenceType) this.visitReferenceTypeNode(t))
        );
        md.setBody(this.visitBlockStatements(node.blockStatements()));
        return md;
    }

    @Override
    public Parameter visitParameterNode(final ParameterNode node) {
        final Parameter parameter = new Parameter();
        parameter.setType(this.visitTypeNode(node.type()));
        parameter.setName(node.variableDeclaratorId().name());
        node.annotations().forEach(
            a -> parameter.addAnnotation((AnnotationExpr) this.visitAnnotationNode(a))
        );
        node.modifiers().forEach(
            m -> parameter.addModifier(Modifier.Keyword.valueOf(m.modifier().toUpperCase()))
        );
        parameter.setVarArgs(node.varArgs());
        parameter.setVarArgsAnnotations(new NodeList<>(node.varArgsAnnotations().stream().map(
            vaa -> (AnnotationExpr) this.visitAnnotationNode(vaa)
        ).collect(Collectors.toList())));
        return parameter;
    }

    @Override
    public Node visitFieldDeclarationNode(final FieldDeclarationNode node) {
        final FieldDeclaration fd = new FieldDeclaration();
        node.annotations().forEach(
            a -> fd.addAnnotation((AnnotationExpr) this.visitAnnotationNode(a))
        );
        node.modifiers().forEach(
            m -> fd.addModifier(Modifier.Keyword.valueOf(m.modifier().toUpperCase()))
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
            a -> annotationDeclaration.addAnnotation((AnnotationExpr) this.visitAnnotationNode(a))
        );
        node.modifiers().forEach(
            m -> annotationDeclaration.addModifier(Modifier.Keyword.valueOf(m.modifier().toUpperCase()))
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
            a -> amd.addAnnotation((AnnotationExpr) this.visitAnnotationNode(a))
        );
        node.modifiers().forEach(
            m -> amd.addModifier(Modifier.Keyword.valueOf(m.modifier().toUpperCase()))
        );
        amd.setType(this.visitTypeNode(node.type()));
        amd.setDefaultValue(this.visitExpressionNode(node.defaultValue()));
        return amd;
    }

    @Override
    public Node visitAnnotationNode(final AnnotationNode node) {
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
        normalAnnotationExpr.setName((Name) this.visitNameNode(node.nameNode()));
        node.elementValuePairs().forEach(
            evp -> normalAnnotationExpr.addPair(evp.identifier(), this.visitExpressionNode(evp.expression()))
        );
        return normalAnnotationExpr;
    }

    @Override
    public SingleMemberAnnotationExpr visitSingleMemberAnnotationNode(final SingleMemberAnnotationNode node) {
        final SingleMemberAnnotationExpr single = new SingleMemberAnnotationExpr();
        single.setName((Name) this.visitNameNode(node.nameNode()));
        single.setMemberValue(this.visitExpressionNode(node.elementValue()));
        return single;
    }

    @Override
    public MarkerAnnotationExpr visitMarkerAnnotationNode(final MarkerAnnotationNode node) {
        return new MarkerAnnotationExpr((Name) this.visitNameNode(node.nameNode()));
    }

    @Override
    public BlockStmt visitBlockStatements(final BlockStatements node) {
        final BlockStmt blockStmt = new BlockStmt();
        for(final StatementNode stmt : node) {
            blockStmt.addStatement((Statement) this.visitStatementNode(stmt));
        }
        return blockStmt;
    }

    public Name visitNameNode(final NameNode node) {
        return node.toName();
    }

    @Override
    public Type visitTypeNode(final TypeNode node) {
        return node.toType();
    }

    @Override
    public Expression visitExpressionNode(final ExpressionNode node) {
        if(node == null) {
            return null;
        }
        return node.toJavaExpression();
    }

    @Override
    public Node visitStatementNode(final StatementNode node) {
        if(node instanceof ClassDeclarationNode) {
            return this.visitClassDeclarationNode((ClassDeclarationNode) node);
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
            return this.visitLocalVariableDeclarationNode((LocalVariableDeclarationNode) node);
        } else if (node instanceof LabeledStatementNode) {
            return this.visitLabeledStatementNode((LabeledStatementNode) node);
        } else if (node instanceof EmptyStatementNode) {
            return this.visitEmptyStatementNode((EmptyStatementNode) node);
        } else if (node instanceof ExpressionStatementNode) {
            return this.visitExpressionStatementNode((ExpressionStatementNode) node);
        } else if (node instanceof ExplicitConstructorInvocationNode) {
            return this.visitExplicitConstructorInvocationNode((ExplicitConstructorInvocationNode) node);
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
        return new LabeledStmt(node.name(), this.visitBlockStatements(node.blockStatements()));
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
        breakStmt.setLabel(new SimpleName(node.label()));
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
        //...set args and type args + implement remaining statement visitors
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
