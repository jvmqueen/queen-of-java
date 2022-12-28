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
package org.queenlang.transpiler.nodes;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.LocalClassDeclarationStmt;
import com.github.javaparser.ast.stmt.Statement;

import java.util.List;

/**
 * Queen ClassDeclaration AST node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 * @todo #10:30min QueenClassDeclarationNode needs unit testing.
 */
public final class QueenClassDeclarationNode implements QueenTypeDeclarationNode, QueenBlockStatementNode, QueenClassMemberDeclarationNode, QueenInterfaceMemberDeclarationNode, QueenAnnotationTypeMemberDeclarationNode{

    /**
     * Position in the original source code.
     */
    private final Position position;

    /**
     * Annotations on top of this class.
     */
    private final List<QueenAnnotationNode> annotations;

    /**
     * Access modifiers of this class.
     */
    private final List<QueenClassAccessModifierNode> accessModifiers;

    /**
     * Extension modifier (abstract or final).
     */
    private final QueenClassExtensionModifierNode extensionModifier;

    /**
     * Name of this type.
     */
    private final String name;

    /**
     * Class type params.
     */
    private final List<String> typeParams;

    /**
     * Type which is extended.
     */
    private final String extendsType;

    /**
     * Interfaces this type implements.
     */
    private final List<String> of;

    /**
     * The body.
     */
    private final QueenClassBodyNode body;

    /**
     * Ctor.
     * @param position Position in the orifinal source code.
     * @param annotations Annotation nodes on top of this type.
     * @param accessModifiers Access modifiers of this class.
     * @param extensionModifier Extension modifier (abstract or final).
     * @param name Name.
     * @param extendsType Type extended.
     * @param typeParams Class type params.
     * @param of Types implemented.
     * @param body The body.
     */
    public QueenClassDeclarationNode(
        final Position position,
        final List<QueenAnnotationNode> annotations,
        final List<QueenClassAccessModifierNode> accessModifiers,
        final QueenClassExtensionModifierNode extensionModifier,
        final String name,
        final List<String> typeParams,
        final String extendsType,
        final List<String> of,
        final QueenClassBodyNode body
    ) {
        this.position = position;
        this.annotations = annotations;
        this.accessModifiers = accessModifiers;
        this.extensionModifier = extensionModifier;
        this.name = name;
        this.typeParams = typeParams;
        this.extendsType = extendsType;
        this.of = of;
        this.body = body;
    }

    @Override
    public void addToJavaNode(final Node java) {
        if(java instanceof CompilationUnit) {
            ((CompilationUnit) java).addType(this.toJavaClass());
        } else if(java instanceof ClassOrInterfaceDeclaration) {
            ((ClassOrInterfaceDeclaration) java).addMember(this.toJavaClass());
        } else if(java instanceof AnnotationDeclaration) {
            ((AnnotationDeclaration) java).addMember(this.toJavaClass());
        } else if(java instanceof BlockStmt) {
            ((BlockStmt) java).addStatement(this.asJavaStatement());
        }
    }

    @Override
    public Statement asJavaStatement() {
        return new LocalClassDeclarationStmt(this.toJavaClass());
    }

    /**
     * Turn it into a JavaParser class declaration.
     * @return ClassOrInterfaceDeclaration.
     */
    private ClassOrInterfaceDeclaration toJavaClass() {
        final ClassOrInterfaceDeclaration clazz = new ClassOrInterfaceDeclaration();
        clazz.setName(this.name);
        clazz.removeModifier(Modifier.Keyword.PUBLIC);
        this.typeParams.forEach(tp -> clazz.addTypeParameter(tp));
        if(this.extendsType != null) {
            clazz.addExtendedType(this.extendsType);
        }
        if(this.of != null && this.of.size() > 0) {
            this.of.forEach(clazz::addImplementedType);
        }
        this.annotations.forEach(a -> a.addToJavaNode(clazz));
        this.accessModifiers.forEach(am -> am.addToJavaNode(clazz));
        this.extensionModifier.addToJavaNode(clazz);
        this.body.addToJavaNode(clazz);
        return clazz;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public Position position() {
        return this.position;
    }
}
