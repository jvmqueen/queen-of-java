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
package org.queenlang.transpiler;

import org.queenlang.transpiler.nodes.QueenNode;
import org.queenlang.transpiler.nodes.body.*;
import org.queenlang.transpiler.nodes.types.*;

/**
 * Queen abstract syntax tree visitor for printing.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class PrintQueenASTVisitor implements QueenASTVisitor<String> {
    public String visitChildren(final QueenNode node) {
        String result = defaultResult();
        result = result + node.getClass().getName() + "\n";
        if(node != null) {
            for (final QueenNode child : node.children()) {
                if(child != null) {
                    String childResult = child.accept(this);
                    result = aggregateResult(result, childResult);
                }
            }
        }
        return result;
    }
    @Override
    public String visitNodeWithParameters(NodeWithParameters node) {
        return "";
    }

    @Override
    public String visitNodeWithTypeParameters(NodeWithTypeParameters node) {
        return "";
    }

    @Override
    public String visitNodeWithTypeArguments(NodeWithTypeArguments node) {
        return "";
    }

    @Override
    public String visitNodeWithFieldDeclarations(NodeWithFieldDeclarations node) {
        return "";
    }

    @Override
    public String visitNodeWithTypeDeclarations(NodeWithTypeDeclarations node) {
        return "";
    }

    @Override
    public String visitNodeWithConstructors(NodeWithConstructors node) {
        return "";
    }

    @Override
    public String visitNodeWithConstantDeclarations(NodeWithConstantDeclarations node) {
        return "";
    }

    @Override
    public String visitNodeWithMethodDeclarations(NodeWithMethodDeclarations node) {
        return "";
    }

    @Override
    public String visitNodeWithInterfaceMethodDeclarations(NodeWithInterfaceMethodDeclarations node) {
        return "";
    }

    @Override
    public String visitNodeWithThrows(NodeWithThrows node) {
        return "";
    }

    @Override
    public String visitNodeWithModifiers(NodeWithModifiers node) {
        return "";
    }

    @Override
    public String visitNodeWithAnnotations(NodeWithAnnotations node) {
        return "";
    }

    @Override
    public String defaultResult() {
        return "";
    }

    @Override
    public String aggregateResult(String aggregate, String nextResult) {
        return aggregate + " --- " + nextResult;
    }
}
