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

import org.queenlang.transpiler.nodes.NameNode;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Classpath used by queenc to search for user-defined classes.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class PathsCp implements Classpath {

    private final List<Path> classpaths;

    public PathsCp(final List<Path> classpaths) {
        this.classpaths = classpaths;
    }

    @Override
    public Path find(final Path clazz) {
        for(final Path path : this.classpaths) {
            final Path whole = Path.of(path.toString(), clazz.toString());
            if(Files.exists(whole)) {
                return whole;
            }
        }
        return null;
    }

    @Override
    public Path find(final NameNode name) {
        final Path dirPath = Path.of(name.name().replaceAll("\\.", FileSystems.getDefault().getSeparator()));
        if(this.find(dirPath) != null) {
            return dirPath;
        }
        final Path queenPath = Path.of(dirPath + ".queen");
        if(this.find(queenPath) != null) {
            return queenPath;
        }
        final Path javaPath = Path.of(dirPath + ".java");
        if(this.find(javaPath) != null) {
            return javaPath;
        }
        final Path clazzPath = Path.of(dirPath + ".class");
        if(this.find(clazzPath) != null) {
            return clazzPath;
        }
        return null;
    }
}
