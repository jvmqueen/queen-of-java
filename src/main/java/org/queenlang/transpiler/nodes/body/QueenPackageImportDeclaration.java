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

import com.github.javaparser.ast.Node;
import org.queenlang.transpiler.QueenResolutionContext;
import org.queenlang.transpiler.nodes.NameNode;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.QueenNameNode;
import org.queenlang.transpiler.nodes.QueenNode;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * An ImportDeclaration based on a PackageDeclaration.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenPackageImportDeclaration implements ImportDeclarationNode {

    private final QueenNode parent;
    private final PackageDeclarationNode packageDeclaration;
    private final String typeName;

    public QueenPackageImportDeclaration(final PackageDeclarationNode packageDeclaration, final String typeName) {
        this(null, packageDeclaration, typeName);
    }

    private QueenPackageImportDeclaration(final QueenNode parent, final PackageDeclarationNode packageDeclaration, final String typeName) {
        this.parent = parent;
        this.packageDeclaration = packageDeclaration;
        this.typeName = typeName;
    }

    @Override
    public void addToJavaNode(final Node java) {}

    @Override
    public Position position() {
        return new Position.Missing();
    }

    @Override
    public List<QueenNode> children() {
        return new ArrayList<>();
    }

    @Override
    public QueenNode withParent(final QueenNode parent) {
        return new QueenPackageImportDeclaration(
            parent,
            this.packageDeclaration,
            this.typeName
        );
    }

    @Override
    public QueenNode parent() {
        return this.parent;
    }

    @Override
    public QueenNode resolve() {
        return this.parent != null ? this.parent.resolve(this, new QueenResolutionContext()) : null;
    }

    @Override
    public boolean asteriskImport() {
        return false;
    }

    @Override
    public NameNode importDeclarationName() {
        if(this.packageDeclaration != null) {
            return new QueenNameNode(new Position.Missing(), this.packageDeclaration.packageName(), this.typeName);
        }
        return new QueenNameNode(new Position.Missing(), this.typeName);
    }

    @Override
    public Path asPath() {
        return Path.of(this.importDeclarationName().name().replaceAll("\\.", FileSystems.getDefault().getSeparator()) + ".queen");
    }

    @Override
    public boolean isContainedBy(final ImportDeclarationNode other) {
        return false;
    }

    @Override
    public ImportDeclarationNode replaceAsteriskWith(final String name) {
        return null;
    }
}
