/**
 * Copyright (c) 2022-2032, Extremely Distributed Technologies S.R.L. Romania,
 *                          Silvia Maxima et Co.
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

import org.queenlang.classpath.Classpath;
import org.queenlang.classpath.ProjectClasspath;
import org.queenlang.queen.QueenASTParser;
import org.queenlang.queen.QueenTranspilationException;
import org.queenlang.queen.nodes.project.ProjectNode;
import org.queenlang.queen.nodes.project.QueenProject;
import org.queenlang.transpiler.aspects.WeaveParents;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Queen to Java transpiler.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 * @todo #64:60min Navigate the whole CU trees to find and transpile the imported and
 *  referenced Queen classes from within each CU.
 * @todo #64:60min Implement semantic validation visiting of each CU.g
 */
public final class QueenToJavaTranspiler implements QueenTranspiler {

    private final QueenASTParser parser;
    private final Path parentDirectory;
    private final Path outputDirectory;

    public QueenToJavaTranspiler(
        final QueenASTParser parser,
        final Path parentDirectory,
        final Path outputDirectory
    ) {
        this.parser = parser;
        this.parentDirectory = parentDirectory;
        this.outputDirectory = outputDirectory;
    }

    @Override
    public void transpile() throws QueenTranspilationException, IOException {
        this.project(parentDirectory).transpileTo(this.outputDirectory);
    }

    @WeaveParents
    private ProjectNode project(final Path projectDir) throws QueenTranspilationException, IOException {
        return new QueenProject(
            this.parser,
            new ProjectClasspath(projectDir)
        );
    }
}
