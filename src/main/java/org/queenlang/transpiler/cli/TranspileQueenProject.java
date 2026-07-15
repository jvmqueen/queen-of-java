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
package org.queenlang.transpiler.cli;

import org.queenlang.queen.QueenASTParserANTLR;
import org.queenlang.queen.QueenTranspilationException;
import org.queenlang.transpiler.QueenToJavaTranspiler;
import org.queenlang.transpiler.QueenTranspiler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;

/**
 * The actual transpiling functionality of queenc.
 * @author Mihai-Emil Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class TranspileQueenProject implements Functionality {
    private final Logger LOG = LoggerFactory.getLogger(TranspileQueenProject.class);
    private final QueenTranspiler queenTranspiler;

    public TranspileQueenProject(final Path queenProjectDirectory, final Path outputDirectory) {
        this.queenTranspiler = new QueenToJavaTranspiler(
            new QueenASTParserANTLR(),
            queenProjectDirectory,
            outputDirectory
        );
    }

    @Override
    public void execute() {
        final Path sourceDir = queenTranspiler.sourceDirectory();
        final Path targetDir = queenTranspiler.targetDirectory();
        try {
            LOG.debug("Transpiling queen project from [{}] to [{}].", sourceDir, targetDir);
            this.queenTranspiler.transpile();
            LOG.debug("Finished transpiling Queen project from [{}] to [{}].", sourceDir, targetDir);
        } catch (QueenTranspilationException | IOException ex) {
            LOG.error(
                "Caught {} while transpiling Queen project from [{}] to [{}]: {}",
                ex.getClass().getSimpleName(), sourceDir, targetDir, ex.getMessage()
            );
        }
    }
}
