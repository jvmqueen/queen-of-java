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

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithType;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.type.ArrayType;
import com.github.javaparser.ast.type.Type;
import org.queenlang.transpiler.nodes.*;
import org.queenlang.transpiler.nodes.expressions.AnnotationNode;
import org.queenlang.transpiler.nodes.expressions.ArrayDimensionNode;
import org.queenlang.transpiler.nodes.statements.BlockStatements;
import org.queenlang.transpiler.nodes.types.*;

import java.util.List;

/**
 * Queen InterfaceMethodDeclaration AST Node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenInterfaceMethodDeclarationNode implements InterfaceMethodDeclarationNode {

    /**
     * Position in the original source code.
     */
    private final Position position;

    /**
     * Annotations on top of this method.
     */
    private final List<AnnotationNode> annotations;

    /**
     * Access modifiers of this method.
     */
    private final List<ModifierNode> modifiers;

    /**
     * Return type.
     */
    private final TypeNode returnType;

    /**
     * Dims on the method declaration. They can be found at the end of the method header:
     * <pre>public int example()[]; </pre>
     */
    private final List<ArrayDimensionNode> dims;

    /**
     * Method type params.
     */
    private final List<TypeParameterNode> typeParams;

    /**
     * Method name.
     */
    private final String name;

    /**
     * Method parameters.
     */
    private final List<ParameterNode> parameters;

    /**
     * Thrown exceptions.
     */
    private final List<ExceptionTypeNode> throwsList;

    /**
     * Method body.
     */
    private final BlockStatements blockStatements;

    public QueenInterfaceMethodDeclarationNode(
        final Position position,
        final List<AnnotationNode> annotations,
        final List<ModifierNode> modifiers,
        final TypeNode returnType,
        final List<ArrayDimensionNode> dims,
        final List<TypeParameterNode> typeParams,
        final String name,
        final List<ParameterNode> parameters,
        final List<ExceptionTypeNode> throwsList,
        final BlockStatements blockStatements
    ) {
        this.position = position;
        this.annotations = annotations;
        this.modifiers = modifiers;
        this.returnType = returnType;
        this.dims = dims;
        this.typeParams = typeParams;
        this.name = name;
        this.parameters = parameters;
        this.throwsList = throwsList;
        this.blockStatements = blockStatements;
    }

    @Override
    public void addToJavaNode(final Node java) {
        final MethodDeclaration method = ((ClassOrInterfaceDeclaration) java).addMethod(this.name);
        method.removeModifier(Modifier.Keyword.PUBLIC);
        this.returnType.addToJavaNode(method);
        if(this.dims != null && this.dims.size() > 0) {
            this.setDims(method);
        }
        this.annotations.forEach(a -> a.addToJavaNode(method));
        this.modifiers.forEach(m -> m.addToJavaNode(method));
        this.typeParams.forEach(tp -> tp.addToJavaNode(method));
        this.parameters.forEach(
            p -> p.addToJavaNode(method)
        );
        this.throwsList.forEach(t->t.addToJavaNode(method));
        if(this.blockStatements != null) {
            final BlockStmt blockStmt = new BlockStmt();
            this.blockStatements.addToJavaNode(blockStmt);
            method.setBody(blockStmt);
        } else {
            method.removeBody();
        }
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public Position position() {
        return this.position;
    }

    @Override
    public List<TypeParameterNode> typeParameters() {
        return this.typeParams;
    }

    /**
     * Set the dims from the name to the type.
     * @param withType Node with type.
     */
    private void setDims(final NodeWithType withType) {
        if(this.dims != null && this.dims.size() > 0) {
            Type setType = withType.getType();
            for(int i = this.dims.size() - 1; i>=0; i--) {
                setType = new ArrayType(
                    setType
                );
                for(final AnnotationNode annotation : this.dims.get(i).annotations()) {
                    annotation.addToJavaNode(setType);
                }
            }
            withType.setType(setType);
        }
    }

    @Override
    public List<AnnotationNode> annotations() {
        return this.annotations;
    }

    @Override
    public List<ModifierNode> modifiers() {
        return this.modifiers;
    }

    @Override
    public TypeNode returnType() {
        return this.returnType;
    }

    @Override
    public List<ArrayDimensionNode> dims() {
        return this.dims;
    }

    @Override
    public List<ParameterNode> parameters() {
        return this.parameters;
    }

    @Override
    public List<ExceptionTypeNode> throwsList() {
        return this.throwsList;
    }

    @Override
    public BlockStatements blockStatements() {
        return this.blockStatements;
    }
}
