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
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import java.util.List;

/**
 * Queen ClassDeclaration AST node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 * @todo #10:30min QueenClassDeclarationNode needs unit testing.
 */
public final class QueenClassDeclarationNode extends QueenTypeDeclarationNode {
    /**
     * Name of this type.
     */
    private final String name;

    /**
     * Type which is extended.
     */
    private final String extendsType;

    /**
     * Interfaces this type implements.
     */
    private final List<String> of;

    /**
     * Ctor.
     * @param annotations Annotation nodes on top of this type.
     * @param name Name.
     * @param extendsType Type extended.
     * @param of Types implemented.
     */
    public QueenClassDeclarationNode(
        final List<QueenAnnotationNode> annotations,
        final String name,
        final String extendsType,
        final List<String> of
    ) {
        super(annotations);
        this.name = name;
        this.extendsType = extendsType;
        this.of = of;
    }

    @Override
    public void addToJavaNode(final Node java) {
        ClassOrInterfaceDeclaration clazz = ((CompilationUnit) java)
            .addClass(this.name);
        if(this.extendsType != null) {
            clazz.addExtendedType(this.extendsType);
        }
        if(this.of != null && this.of.size() > 0) {
            this.of.forEach(clazz::addImplementedType);
        }
        super.addToJavaNode(clazz);
    }
}
