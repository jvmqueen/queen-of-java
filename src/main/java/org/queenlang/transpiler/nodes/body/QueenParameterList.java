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

import org.queenlang.transpiler.nodes.Position;
import org.queenlang.transpiler.nodes.QueenNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A list of parameters (in constructors, methods and lambdas).
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 * @todo #100:60min Implement equals and hashcode for all TypeNodes.
 * @todo #100:60min After all TypeNodes have equals and hashcode, use this class for ConstructorDeclaration and MethodDeclaration.
 */
public final class QueenParameterList implements ParameterList {

    private final Position position;
    private final QueenNode parent;
    private final List<ParameterNode> parameters;

    public QueenParameterList(
        final Position position
    ) {
        this(position, new ArrayList<>());
    }

    public QueenParameterList(
        final Position position,
        final List<ParameterNode> parameters
    ) {
        this(position, null, parameters);
    }

    private QueenParameterList(
        final Position position,
        final QueenNode parent,
        final List<ParameterNode> parameters
    ) {
        this.position = position;
        this.parent = parent;
        this.parameters = parameters.stream().map(
            p -> (ParameterNode) p.withParent(this)
        ).collect(Collectors.toList());
    }

    @Override
    public Position position() {
        return this.position;
    }

    @Override
    public QueenNode withParent(final QueenNode parent) {
        return new QueenParameterList(
            this.position,
            parent,
            this.parameters
        );
    }

    @Override
    public QueenNode parent() {
        return this.parent;
    }

    @Override
    public List<ParameterNode> parameters() {
        return this.parameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ParameterList)) {
            return false;
        }
        final ParameterList that = (ParameterList) o;
        final List<ParameterNode> otherParams = that.parameters();
        if(this.parameters.size() != otherParams.size()) {
            return false;
        }
        boolean isEqual = true;
        for(int i = 0; i < this.parameters.size(); i++) {
            final ParameterNode thisParam = this.parameters.get(i);
            final ParameterNode otherParam = otherParams.get(i);
            isEqual = isEqual && thisParam.type().equals(otherParam.type()) && thisParam.varArgs() == otherParam.varArgs();
        }
        return isEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            this.parameters.stream().map(
                ParameterNode::type
            ).toArray()
        );
    }
}
