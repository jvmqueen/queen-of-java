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
package org.queenlang.transpiler.nodes.body;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.expressions.AnnotationNode;
import org.queenlang.transpiler.nodes.types.ClassOrInterfaceTypeNode;
import org.queenlang.transpiler.nodes.types.TypeParameterNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for {@link QueenClassDeclarationNode}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenClassDeclarationNodeTestCase {

    @Test
    public void returnsPosition() {
        final Position position = Mockito.mock(Position.class);
        final ClassDeclarationNode classDeclarationNode = new QueenClassDeclarationNode(
            position,
            new ArrayList<>(),
            new ArrayList<>(),
            Mockito.mock(ModifierNode.class),
            "MyClass",
            new ArrayList<>(),
            Mockito.mock(ClassOrInterfaceTypeNode.class),
            new ArrayList<>(),
            Mockito.mock(ClassBodyNode.class)
        );
        MatcherAssert.assertThat(
            classDeclarationNode.position(),
            Matchers.is(position)
        );
    }

    @Test
    public void returnsName() {
        final ClassDeclarationNode classDeclarationNode = new QueenClassDeclarationNode(
            Mockito.mock(Position.class),
            new ArrayList<>(),
            new ArrayList<>(),
            Mockito.mock(ModifierNode.class),
            "MyClass",
            new ArrayList<>(),
            Mockito.mock(ClassOrInterfaceTypeNode.class),
            new ArrayList<>(),
            Mockito.mock(ClassBodyNode.class)
        );
        MatcherAssert.assertThat(
            classDeclarationNode.name(),
            Matchers.equalTo("MyClass")
        );
    }

    @Test
    public void returnsModifiers() {
        final List<ModifierNode> modifiers = new ArrayList<>();
        modifiers.add(Mockito.mock(ModifierNode.class));
        final ClassDeclarationNode classDeclarationNode = new QueenClassDeclarationNode(
            Mockito.mock(Position.class),
            new ArrayList<>(),
            modifiers,
            Mockito.mock(ModifierNode.class),
            "MyClass",
            new ArrayList<>(),
            Mockito.mock(ClassOrInterfaceTypeNode.class),
            new ArrayList<>(),
            Mockito.mock(ClassBodyNode.class)
        );
        MatcherAssert.assertThat(
            classDeclarationNode.modifiers(),
            Matchers.is(modifiers)
        );
    }

    @Test
    public void returnsAnnotations() {
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(Mockito.mock(AnnotationNode.class));
        final ClassDeclarationNode classDeclarationNode = new QueenClassDeclarationNode(
            Mockito.mock(Position.class),
            annotations,
            new ArrayList<>(),
            Mockito.mock(ModifierNode.class),
            "MyClass",
            new ArrayList<>(),
            Mockito.mock(ClassOrInterfaceTypeNode.class),
            new ArrayList<>(),
            Mockito.mock(ClassBodyNode.class)
        );
        MatcherAssert.assertThat(
            classDeclarationNode.annotations(),
            Matchers.is(annotations)
        );
    }

    @Test
    public void returnsExtensionModifier() {
        final ModifierNode extensionModifier = Mockito.mock(ModifierNode.class);
        final ClassDeclarationNode classDeclarationNode = new QueenClassDeclarationNode(
            Mockito.mock(Position.class),
            new ArrayList<>(),
            new ArrayList<>(),
            extensionModifier,
            "MyClass",
            new ArrayList<>(),
            Mockito.mock(ClassOrInterfaceTypeNode.class),
            new ArrayList<>(),
            Mockito.mock(ClassBodyNode.class)
        );
        MatcherAssert.assertThat(
            classDeclarationNode.extensionModifier(),
            Matchers.is(extensionModifier)
        );
    }

    @Test
    public void returnsTypeParameters() {
        final List<TypeParameterNode> typeParameters = new ArrayList<>();
        typeParameters.add(Mockito.mock(TypeParameterNode.class));
        final ClassDeclarationNode classDeclarationNode = new QueenClassDeclarationNode(
            Mockito.mock(Position.class),
            new ArrayList<>(),
            new ArrayList<>(),
            Mockito.mock(ModifierNode.class),
            "MyClass",
            typeParameters,
            Mockito.mock(ClassOrInterfaceTypeNode.class),
            new ArrayList<>(),
            Mockito.mock(ClassBodyNode.class)
        );
        MatcherAssert.assertThat(
            classDeclarationNode.typeParameters(),
            Matchers.is(typeParameters)
        );
    }

    @Test
    public void returnsExtendsType() {
        final ClassOrInterfaceTypeNode extendsType = Mockito.mock(ClassOrInterfaceTypeNode.class);
        final ClassDeclarationNode classDeclarationNode = new QueenClassDeclarationNode(
            Mockito.mock(Position.class),
            new ArrayList<>(),
            new ArrayList<>(),
            Mockito.mock(ModifierNode.class),
            "MyClass",
            new ArrayList<>(),
            extendsType,
            new ArrayList<>(),
            Mockito.mock(ClassBodyNode.class)
        );
        MatcherAssert.assertThat(
            classDeclarationNode.extendsType(),
            Matchers.is(extendsType)
        );
    }

    @Test
    public void returnsOf() {
        final List<ClassOrInterfaceTypeNode> of = new ArrayList<>();
        of.add(Mockito.mock(ClassOrInterfaceTypeNode.class));
        final ClassDeclarationNode classDeclarationNode = new QueenClassDeclarationNode(
            Mockito.mock(Position.class),
            new ArrayList<>(),
            new ArrayList<>(),
            Mockito.mock(ModifierNode.class),
            "MyClass",
            new ArrayList<>(),
            Mockito.mock(ClassOrInterfaceTypeNode.class),
            of,
            Mockito.mock(ClassBodyNode.class)
        );
        MatcherAssert.assertThat(
            classDeclarationNode.of(),
            Matchers.is(of)
        );
    }

    @Test
    public void returnsBody() {
        final ClassBodyNode body = Mockito.mock(ClassBodyNode.class);
        final ClassDeclarationNode classDeclarationNode = new QueenClassDeclarationNode(
            Mockito.mock(Position.class),
            new ArrayList<>(),
            new ArrayList<>(),
            Mockito.mock(ModifierNode.class),
            "MyClass",
            new ArrayList<>(),
            Mockito.mock(ClassOrInterfaceTypeNode.class),
            new ArrayList<>(),
            body
        );
        MatcherAssert.assertThat(
            classDeclarationNode.body(),
            Matchers.is(body)
        );
    }

    @Test
    public void addsToCompilationUnitNode() {
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(Mockito.mock(AnnotationNode.class));
        final List<ModifierNode> modifiers = new ArrayList<>();
        modifiers.add(Mockito.mock(ModifierNode.class));
        final List<TypeParameterNode> typeParameters = new ArrayList<>();
        typeParameters.add(Mockito.mock(TypeParameterNode.class));
        final List<ClassOrInterfaceTypeNode> of = new ArrayList<>();
        of.add(Mockito.mock(ClassOrInterfaceTypeNode.class));
        final ClassOrInterfaceTypeNode extendsType = Mockito.mock(ClassOrInterfaceTypeNode.class);
        final ModifierNode extensionModifier = Mockito.mock(ModifierNode.class);
        final ClassBodyNode body = Mockito.mock(ClassBodyNode.class);

        final ClassDeclarationNode classDeclarationNode = new QueenClassDeclarationNode(
            Mockito.mock(Position.class),
            annotations,
            modifiers,
            extensionModifier,
            "MyClass",
            typeParameters,
            extendsType,
            of,
            body
        );

        final CompilationUnit cu = new CompilationUnit();
        classDeclarationNode.addToJavaNode(cu);

        MatcherAssert.assertThat(
            cu.getType(0).asClassOrInterfaceDeclaration().getName().asString(),
            Matchers.equalTo("MyClass")
        );
        MatcherAssert.assertThat(
            cu.getType(0).asClassOrInterfaceDeclaration().isInterface(),
            Matchers.is(false)
        );
        annotations.forEach(
            a -> Mockito.verify(a, Mockito.times(1)).addToJavaNode(Mockito.any())
        );
        modifiers.forEach(
            m -> Mockito.verify(m, Mockito.times(1)).addToJavaNode(Mockito.any())
        );
        typeParameters.forEach(
            tp -> Mockito.verify(tp, Mockito.times(1)).addToJavaNode(Mockito.any())
        );
        of.forEach(
            o -> Mockito.verify(o, Mockito.times(1)).addToJavaNode(Mockito.any())
        );
        Mockito.verify(extendsType, Mockito.times(1)).addToJavaNode(Mockito.any());
        Mockito.verify(extensionModifier, Mockito.times(1)).addToJavaNode(Mockito.any());
        Mockito.verify(body, Mockito.times(1)).addToJavaNode(Mockito.any());
    }

    @Test
    public void addsToClassOrInterfaceDeclarationNode() {
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(Mockito.mock(AnnotationNode.class));
        final List<ModifierNode> modifiers = new ArrayList<>();
        modifiers.add(Mockito.mock(ModifierNode.class));
        final List<TypeParameterNode> typeParameters = new ArrayList<>();
        typeParameters.add(Mockito.mock(TypeParameterNode.class));
        final List<ClassOrInterfaceTypeNode> of = new ArrayList<>();
        of.add(Mockito.mock(ClassOrInterfaceTypeNode.class));
        final ClassOrInterfaceTypeNode extendsType = Mockito.mock(ClassOrInterfaceTypeNode.class);
        final ModifierNode extensionModifier = Mockito.mock(ModifierNode.class);
        final ClassBodyNode body = Mockito.mock(ClassBodyNode.class);

        final ClassDeclarationNode classDeclarationNode = new QueenClassDeclarationNode(
            Mockito.mock(Position.class),
            annotations,
            modifiers,
            extensionModifier,
            "MyClass",
            typeParameters,
            extendsType,
            of,
            body
        );

        final ClassOrInterfaceDeclaration clazz = new ClassOrInterfaceDeclaration();
        classDeclarationNode.addToJavaNode(clazz);

        MatcherAssert.assertThat(
            clazz.getMember(0).asClassOrInterfaceDeclaration().getName().asString(),
            Matchers.equalTo("MyClass")
        );
        MatcherAssert.assertThat(
            clazz.getMember(0).asClassOrInterfaceDeclaration().isInterface(),
            Matchers.is(false)
        );
        annotations.forEach(
            a -> Mockito.verify(a, Mockito.times(1)).addToJavaNode(Mockito.any())
        );
        modifiers.forEach(
            m -> Mockito.verify(m, Mockito.times(1)).addToJavaNode(Mockito.any())
        );
        typeParameters.forEach(
            tp -> Mockito.verify(tp, Mockito.times(1)).addToJavaNode(Mockito.any())
        );
        of.forEach(
            o -> Mockito.verify(o, Mockito.times(1)).addToJavaNode(Mockito.any())
        );
        Mockito.verify(extendsType, Mockito.times(1)).addToJavaNode(Mockito.any());
        Mockito.verify(extensionModifier, Mockito.times(1)).addToJavaNode(Mockito.any());
        Mockito.verify(body, Mockito.times(1)).addToJavaNode(Mockito.any());
    }

    @Test
    public void addsToAnnotationDeclarationNode() {
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(Mockito.mock(AnnotationNode.class));
        final List<ModifierNode> modifiers = new ArrayList<>();
        modifiers.add(Mockito.mock(ModifierNode.class));
        final List<TypeParameterNode> typeParameters = new ArrayList<>();
        typeParameters.add(Mockito.mock(TypeParameterNode.class));
        final List<ClassOrInterfaceTypeNode> of = new ArrayList<>();
        of.add(Mockito.mock(ClassOrInterfaceTypeNode.class));
        final ClassOrInterfaceTypeNode extendsType = Mockito.mock(ClassOrInterfaceTypeNode.class);
        final ModifierNode extensionModifier = Mockito.mock(ModifierNode.class);
        final ClassBodyNode body = Mockito.mock(ClassBodyNode.class);

        final ClassDeclarationNode classDeclarationNode = new QueenClassDeclarationNode(
            Mockito.mock(Position.class),
            annotations,
            modifiers,
            extensionModifier,
            "MyClass",
            typeParameters,
            extendsType,
            of,
            body
        );

        final AnnotationDeclaration ad = new AnnotationDeclaration();
        classDeclarationNode.addToJavaNode(ad);

        MatcherAssert.assertThat(
            ad.getMember(0).asClassOrInterfaceDeclaration().getName().asString(),
            Matchers.equalTo("MyClass")
        );
        MatcherAssert.assertThat(
            ad.getMember(0).asClassOrInterfaceDeclaration().isInterface(),
            Matchers.is(false)
        );
        annotations.forEach(
            a -> Mockito.verify(a, Mockito.times(1)).addToJavaNode(Mockito.any())
        );
        modifiers.forEach(
            m -> Mockito.verify(m, Mockito.times(1)).addToJavaNode(Mockito.any())
        );
        typeParameters.forEach(
            tp -> Mockito.verify(tp, Mockito.times(1)).addToJavaNode(Mockito.any())
        );
        of.forEach(
            o -> Mockito.verify(o, Mockito.times(1)).addToJavaNode(Mockito.any())
        );
        Mockito.verify(extendsType, Mockito.times(1)).addToJavaNode(Mockito.any());
        Mockito.verify(extensionModifier, Mockito.times(1)).addToJavaNode(Mockito.any());
        Mockito.verify(body, Mockito.times(1)).addToJavaNode(Mockito.any());
    }

    @Test
    public void addsToBlockStatementNode() {
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(Mockito.mock(AnnotationNode.class));
        final List<ModifierNode> modifiers = new ArrayList<>();
        modifiers.add(Mockito.mock(ModifierNode.class));
        final List<TypeParameterNode> typeParameters = new ArrayList<>();
        typeParameters.add(Mockito.mock(TypeParameterNode.class));
        final List<ClassOrInterfaceTypeNode> of = new ArrayList<>();
        of.add(Mockito.mock(ClassOrInterfaceTypeNode.class));
        final ClassOrInterfaceTypeNode extendsType = Mockito.mock(ClassOrInterfaceTypeNode.class);
        final ModifierNode extensionModifier = Mockito.mock(ModifierNode.class);
        final ClassBodyNode body = Mockito.mock(ClassBodyNode.class);

        final ClassDeclarationNode classDeclarationNode = new QueenClassDeclarationNode(
            Mockito.mock(Position.class),
            annotations,
            modifiers,
            extensionModifier,
            "MyClass",
            typeParameters,
            extendsType,
            of,
            body
        );

        final BlockStmt block = new BlockStmt();
        classDeclarationNode.addToJavaNode(block);

        MatcherAssert.assertThat(
            block.getStatement(0).asLocalClassDeclarationStmt().getClassDeclaration().getName().asString(),
            Matchers.equalTo("MyClass")
        );
        MatcherAssert.assertThat(
            block.getStatement(0).asLocalClassDeclarationStmt().getClassDeclaration().isInterface(),
            Matchers.is(false)
        );
        annotations.forEach(
            a -> Mockito.verify(a, Mockito.times(1)).addToJavaNode(Mockito.any())
        );
        modifiers.forEach(
            m -> Mockito.verify(m, Mockito.times(1)).addToJavaNode(Mockito.any())
        );
        typeParameters.forEach(
            tp -> Mockito.verify(tp, Mockito.times(1)).addToJavaNode(Mockito.any())
        );
        of.forEach(
            o -> Mockito.verify(o, Mockito.times(1)).addToJavaNode(Mockito.any())
        );
        Mockito.verify(extendsType, Mockito.times(1)).addToJavaNode(Mockito.any());
        Mockito.verify(extensionModifier, Mockito.times(1)).addToJavaNode(Mockito.any());
        Mockito.verify(body, Mockito.times(1)).addToJavaNode(Mockito.any());
    }

    @Test
    public void addsToObjectCreationExprNode() {
        final List<AnnotationNode> annotations = new ArrayList<>();
        annotations.add(Mockito.mock(AnnotationNode.class));
        final List<ModifierNode> modifiers = new ArrayList<>();
        modifiers.add(Mockito.mock(ModifierNode.class));
        final List<TypeParameterNode> typeParameters = new ArrayList<>();
        typeParameters.add(Mockito.mock(TypeParameterNode.class));
        final List<ClassOrInterfaceTypeNode> of = new ArrayList<>();
        of.add(Mockito.mock(ClassOrInterfaceTypeNode.class));
        final ClassOrInterfaceTypeNode extendsType = Mockito.mock(ClassOrInterfaceTypeNode.class);
        final ModifierNode extensionModifier = Mockito.mock(ModifierNode.class);
        final ClassBodyNode body = Mockito.mock(ClassBodyNode.class);

        final ClassDeclarationNode classDeclarationNode = new QueenClassDeclarationNode(
            Mockito.mock(Position.class),
            annotations,
            modifiers,
            extensionModifier,
            "MyClass",
            typeParameters,
            extendsType,
            of,
            body
        );

        final ObjectCreationExpr objectCreationExpr = new ObjectCreationExpr();
        classDeclarationNode.addToJavaNode(objectCreationExpr);

        MatcherAssert.assertThat(
            objectCreationExpr.getAnonymousClassBody().isPresent(),
            Matchers.is(true)
        );
        annotations.forEach(
            a -> Mockito.verify(a, Mockito.times(1)).addToJavaNode(Mockito.any())
        );
        modifiers.forEach(
            m -> Mockito.verify(m, Mockito.times(1)).addToJavaNode(Mockito.any())
        );
        typeParameters.forEach(
            tp -> Mockito.verify(tp, Mockito.times(1)).addToJavaNode(Mockito.any())
        );
        of.forEach(
            o -> Mockito.verify(o, Mockito.times(1)).addToJavaNode(Mockito.any())
        );
        Mockito.verify(extendsType, Mockito.times(1)).addToJavaNode(Mockito.any());
        Mockito.verify(extensionModifier, Mockito.times(1)).addToJavaNode(Mockito.any());
        Mockito.verify(body, Mockito.times(1)).addToJavaNode(Mockito.any());
    }

}
