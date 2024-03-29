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
package org.queenlang.transpiler.nodes.project;

import org.queenlang.transpiler.QueenASTVisitor;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.QueenNode;
import org.queenlang.transpiler.nodes.body.AnnotationTypeDeclarationNode;
import org.queenlang.transpiler.nodes.body.ClassDeclarationNode;
import org.queenlang.transpiler.nodes.body.CompilationUnitNode;
import org.queenlang.transpiler.nodes.body.NormalInterfaceDeclarationNode;

/**
 * A Queen file, AST Node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public interface FileNode extends QueenNode {

    /**
     * Compilation unit within this file.
     * @return CompilationUnit.
     */
    CompilationUnitNode compilationUnit();

    /**
     * Return the file's name.
     * @return File name.
     */
    String fileName();

    /**
     * Return the full name of the type declared in this file.
     * @return String, never null.
     */
    String fullTypeName();

    @Override
    default Position position() {
        return new Position.Missing();
    }

    default <T> T accept(QueenASTVisitor<? extends T> visitor) {
        return visitor.visitFile(this);
    }

    /**
     * Turn this FileNode (containing CompilationUnit) into a NormalInterfaceDeclarationNode.
     * @return NormalInterfaceDeclarationNode or null if this node is not a normal interface declaration.
     */
    default NormalInterfaceDeclarationNode asNormalInterfaceDeclaration() {
        return this.compilationUnit().asNormalInterfaceDeclaration();
    }

    /**
     * Turn this FileNode (containing CompilationUnit) into a ClassDeclarationNode.
     * @return ClassDeclarationNode or null if this node is not a class declaration.
     */
    default ClassDeclarationNode asClassDeclarationNode() {
        return this.compilationUnit().asClassDeclarationNode();
    }

    /**
     * Turn this FileNode (containing CompilationUnit) into an AnnotationTypeDeclarationNode.
     * @return ClassDeclarationNode or null if this node is not an annotation declaration.
     */
    default AnnotationTypeDeclarationNode asAnnotationTypeDeclarationNode() {
        return this.compilationUnit().asAnnotationTypeDeclarationNode();
    }
}
