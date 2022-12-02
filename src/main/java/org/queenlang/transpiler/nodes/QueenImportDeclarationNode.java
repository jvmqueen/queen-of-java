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
import com.github.javaparser.ast.Node;

/**
 * Queen ImportDeclaration AST node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 * @todo #2:30min We need to write some unit tests for this class.
 */
public final class QueenImportDeclarationNode implements QueenNode {

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
     * @param importDeclaration Type name.
     * @param staticImport Is it a static import or not?
     * @param asteriskImport Is it an asterysk import or not?
     */
    public QueenImportDeclarationNode(
        final String importDeclaration,
        final boolean staticImport,
        final boolean asteriskImport
    ) {
        this.importDeclaration = importDeclaration;
        this.staticImport = staticImport;
        this.asteriskImport = asteriskImport;
    }


    @Override
    public void addToJavaNode(final Node java) {
        System.out.println("ADDING IMPORT: " + this.importDeclaration);
        ((CompilationUnit) java).addImport(this.importDeclaration, this.staticImport, this.asteriskImport);
    }
}
