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
package org.queenlang.transpiler.nodes.java;

import org.queenlang.transpiler.nodes.NameNode;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.QueenNode;
import org.queenlang.transpiler.nodes.QueenReferenceNode;
import org.queenlang.transpiler.nodes.body.*;
import org.queenlang.transpiler.nodes.project.ProjectNode;

import java.lang.reflect.Field;
import java.util.List;

/**
 * A compilation unit based on a Java Class object.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class ClassCompilationUnitNode implements CompilationUnitNode {
    /**
     * Parent project.
     */
    private final ProjectNode parent;

    /**
     * Java class.
     */
    private final Class clazz;


    public ClassCompilationUnitNode(final ProjectNode parent, final Class clazz) {
        this.parent = parent;
        this.clazz = clazz;
    }

    @Override
    public Position position() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public List<QueenNode> children() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public QueenNode withParent(QueenNode parent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public QueenNode parent() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public PackageDeclarationNode packageDeclaration() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public List<ImportDeclarationNode> importDeclarations() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public TypeDeclarationNode typeDeclaration() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public QueenNode resolve(final QueenReferenceNode reference, boolean goUp) {
        if(reference instanceof NameNode) {
            for(final Field field : this.clazz.getFields()) {
                if(field.getName().equals(((NameNode) reference).identifier())) {
                    return new ClassFieldDeclarationNode(this, field);
                }
            }
        }
        return null;
    }
}
