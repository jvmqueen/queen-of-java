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

import org.queenlang.transpiler.nodes.NameNode;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.QueenNameNode;
import org.queenlang.transpiler.nodes.QueenNode;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Queen ImportDeclaration AST node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenImportDeclarationNode implements ImportDeclarationNode {

    /**
     * Position in the original source code.
     */
    private final Position position;

    /**
     * Parent node.
     */
    private final QueenNode parent;

    /**
     * Is it an asterysk import or not?
     */
    private final boolean asteriskImport;

    /**
     * Import's type name.
     */
    private final NameNode importDeclarationName;
    /**
     * Ctor.
     * @param position Position in the original source code.
     * @param asteriskImport Is it an asterysk import or not?
     */
    public QueenImportDeclarationNode(
        final Position position,
        final NameNode importDeclarationName,
        final boolean asteriskImport
    ) {
        this(position, null, importDeclarationName, asteriskImport);
    }

    private QueenImportDeclarationNode(
        final Position position,
        final QueenNode parent,
        final NameNode importDeclarationName,
        final boolean asteriskImport
    ) {
        this.position = position;
        this.parent = parent;
        this.asteriskImport = asteriskImport;
        this.importDeclarationName = importDeclarationName != null ? (NameNode) importDeclarationName.withParent(this) : null;
    }

    @Override
    public Position position() {
        return this.position;
    }

    @Override
    public List<QueenNode> children() {
        return Arrays.asList(this.importDeclarationName);
    }

    @Override
    public QueenNode withParent(final QueenNode parent) {
        return new QueenImportDeclarationNode(
            this.position,
            parent,
            this.importDeclarationName,
            this.asteriskImport
        );
    }

    @Override
    public QueenNode parent() {
        return this.parent;
    }

    @Override
    public boolean asteriskImport() {
        return this.asteriskImport;
    }

    @Override
    public NameNode importDeclarationName() {
        return this.importDeclarationName;
    }

    @Override
    public Path asPath() {
        return Path.of(this.importDeclarationName.name().replaceAll("\\.", FileSystems.getDefault().getSeparator()) + ".queen");
    }

    @Override
    public boolean isContainedBy(final ImportDeclarationNode other) {
        if(other.asteriskImport()) {
            final String otherFullName = other.importDeclarationName().name();
            final String thisFullName = this.importDeclarationName.name();
            final Pattern otherp = Pattern.compile("^" + otherFullName + ".*$", Pattern.CASE_INSENSITIVE);
            final Matcher matcher = otherp.matcher(thisFullName);
            if(matcher.find()) {
                return thisFullName.split("\\.").length == otherFullName.split("\\.").length + 1;
            }
        } else {
            if(!this.asteriskImport && other.importDeclarationName().identifier().equalsIgnoreCase(this.importDeclarationName.identifier())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ImportDeclarationNode replaceAsteriskWith(final String name) {
        return new QueenImportDeclarationNode(
            this.position,
            this.parent,
            new QueenNameNode(this.importDeclarationName.position(), this.importDeclarationName, name),
            false
        );
    }
}
