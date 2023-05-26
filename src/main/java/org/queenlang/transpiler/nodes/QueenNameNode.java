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

import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import org.queenlang.transpiler.nodes.expressions.AnnotationNode;
import org.queenlang.transpiler.nodes.types.ClassOrInterfaceTypeNode;
import org.queenlang.transpiler.nodes.types.TypeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * A name of something. Could be a package declaration, a type name, a method name etc.
 * In some cases, it can have a context/qualifier prefix like java.util.List.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenNameNode implements NameNode {

    private final Position position;
    private final QueenNode parent;
    private final NameNode qualifier;
    private final String identifier;

    public QueenNameNode(final Position position, final String identifier) {
        this(position, null, identifier);
    }

    public QueenNameNode(final Position position, final NameNode qualifier, final String identifier) {
        this(position, null, qualifier, identifier);
    }

    private QueenNameNode(final Position position, final QueenNode parent, final NameNode qualifier, final String identifier) {
        this.parent = parent;
        this.position = position;
        this.qualifier = qualifier != null ? (NameNode) qualifier.withParent(this.parent()) : null;
        this.identifier = identifier;
    }

    @Override
    public Position position() {
        return this.position;
    }

    @Override
    public List<QueenNode> children() {
        return new ArrayList<>();
    }

    @Override
    public ClassOrInterfaceType toType() {
        final ClassOrInterfaceType classOrInterfaceType = new ClassOrInterfaceType(this.identifier);
        if(this.qualifier != null) {
            classOrInterfaceType.setScope((ClassOrInterfaceType) this.qualifier.toType());
        }
        return classOrInterfaceType;
    }

    @Override
    public Expression toJavaExpression() {
        if(this.qualifier == null) {
            return new NameExpr(this.identifier);
        } else {
            return new FieldAccessExpr(
                this.qualifier.toJavaExpression(),
                this.identifier
            );
        }
    }

    public Name toName() {
        final Name name = new Name(this.identifier);
        if(this.qualifier != null) {
            name.setQualifier(this.qualifier.toName());
        }
        return name;
    }

    @Override
    public NameNode qualifier() {
        return this.qualifier;
    }

    @Override
    public String identifier() {
        return this.identifier;
    }

    @Override
    public boolean interfaceType() {
        return false;
    }

    @Override
    public ClassOrInterfaceTypeNode scope() {
        return this.qualifier;
    }

    @Override
    public String simpleName() {
        return this.identifier;
    }

    @Override
    public List<TypeNode> typeArguments() {
        return new ArrayList<>();
    }

    @Override
    public boolean hasDiamondOperator() {
        return false;
    }

    @Override
    public QueenNode resolve() {
        final QueenNode definition;
        if(this.qualifier == null) {
            if(this.parent != null) {
                definition = this.parent.resolve(this, true);
            } else {
                definition = null;
            }
        } else {
            final QueenNode qualifierDefinition = this.qualifier.resolve();
            if(qualifierDefinition != null) {
                return qualifierDefinition.resolve(this, false);
            } else {
                return null;
            }
        }
        return definition;
    }

    @Override
    public QueenNameNode withParent(final QueenNode parent) {
        return new QueenNameNode(
            this.position,
            parent,
            this.qualifier,
            this.identifier
        );
    }

    @Override
    public QueenNode parent() {
        return this.parent;
    }

    @Override
    public List<AnnotationNode> annotations() {
        return new ArrayList<>();
    }
}
