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
import com.github.javaparser.ast.Node;
import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.QueenNode;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Queen ImportDeclaration AST node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenImportDeclarationNode implements QueenNode {

    /**
     * Position in the original source code.
     */
    private final Position position;

    /**
     * Is it a static import or not?
     */
    private final boolean staticImport;

    /**
     * Is it an asterysk import or not?
     */
    private final boolean asteriskImport;

    /**
     * Import's type name.
     */
    private final String importDeclaration;

    /**
     * Ctor.
     * @param position Position in the original source code.
     * @param importDeclaration Type name.
     * @param staticImport Is it a static import or not?
     * @param asteriskImport Is it an asterysk import or not?
     */
    public QueenImportDeclarationNode(
        final Position position,
        final String importDeclaration,
        final boolean staticImport,
        final boolean asteriskImport
    ) {
        this.position = position;
        this.importDeclaration = importDeclaration;
        this.staticImport = staticImport;
        this.asteriskImport = asteriskImport;
    }

    @Override
    public void addToJavaNode(final Node java) {
        ((CompilationUnit) java).addImport(this.importDeclaration, this.staticImport, this.asteriskImport);
    }

    @Override
    public Position position() {
        return this.position;
    }

    public boolean isStaticImport() {
        return this.staticImport;
    }

    public boolean isAsteriskImport() {
        return this.asteriskImport;
    }

    public String importDeclaration() {
        return this.importDeclaration;
    }

    /**
     * Returns true if this import declaration is already contained by the given
     * import declaration. Example: this is java.util.List and the given import is
     * java.util.*: this method will return true.
     * @param importDeclarationNode Givem import declaration.
     * @return True if this import declaration is contained by the other import declaration.
     */
    public boolean isContainedBy(final QueenImportDeclarationNode importDeclarationNode) {
        if(this.equals(importDeclarationNode)) {
            return true;
        } else {
            if(importDeclarationNode.isAsteriskImport()) {
                final Pattern other = Pattern.compile("^" + importDeclarationNode.importDeclaration() + ".*$", Pattern.CASE_INSENSITIVE);
                final Matcher matcher = other.matcher(this.importDeclaration);
                if(matcher.find()) {
                    return this.importDeclaration.split("\\.").length == importDeclarationNode.importDeclaration().split("\\.").length + 1;
                }
            }
            return false;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        QueenImportDeclarationNode that = (QueenImportDeclarationNode) o;
        return staticImport == that.staticImport && asteriskImport == that.asteriskImport && importDeclaration.equals(that.importDeclaration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(staticImport, asteriskImport, importDeclaration);
    }
}
