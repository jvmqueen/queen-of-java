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

import org.queenlang.transpiler.nodes.QueenNode;
import org.queenlang.transpiler.nodes.QueenReferenceNode;
import org.queenlang.transpiler.nodes.body.CompilationUnitNode;
import org.queenlang.transpiler.nodes.body.PackageDeclarationNode;

import java.util.Arrays;
import java.util.List;

/**
 * A Queen file, AST Node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenFileNode implements FileNode{

    private final QueenNode parent;
    private final String fileName;
    private final CompilationUnitNode compilationUnit;

    public QueenFileNode(final String fileName, final CompilationUnitNode compilationUnit) {
        this(null, fileName, compilationUnit);
    }

    private QueenFileNode(final QueenNode parent, final String fileName, final CompilationUnitNode compilationUnit) {
        this.parent = parent;
        this.fileName = fileName;
        this.compilationUnit = compilationUnit != null ? (CompilationUnitNode) compilationUnit.withParent(this) : null;
    }

    @Override
    public List<QueenNode> children() {
        return Arrays.asList(this.compilationUnit);
    }

    @Override
    public FileNode withParent(final QueenNode parent) {
        return new QueenFileNode(
            parent,
            this.fileName,
            this.compilationUnit
        );
    }

    @Override
    public QueenNode parent() {
        return this.parent;
    }

    @Override
    public CompilationUnitNode compilationUnit() {
        return this.compilationUnit;
    }

    @Override
    public String fileName() {
        return this.fileName;
    }

    @Override
    public String fullTypeName() {
        String fullTypeName = "";
        final PackageDeclarationNode packageDeclaration = this.compilationUnit.packageDeclaration();
        if(packageDeclaration != null) {
            fullTypeName += packageDeclaration.packageName().name() + ".";
        }
        fullTypeName += this.compilationUnit.typeDeclaration().name();
        return fullTypeName;
    }

    @Override
    public QueenNode resolve(final QueenReferenceNode reference, boolean goUp) {
        if(goUp) {
            return this.parent.resolve(reference, true);
        } else {
            return this.compilationUnit.resolve(reference, false);
        }
    }
}
