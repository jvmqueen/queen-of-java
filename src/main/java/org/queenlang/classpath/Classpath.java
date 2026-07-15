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
package org.queenlang.classpath;

import org.queenlang.queen.nodes.names.NameNode;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;

/**
 * Classpath used by queenc to search for user-defined classes.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public interface Classpath {

    /**
     * Search and return the full path to a user-defined class.
     * @param clazz Path to class.
     * @return Path or null if the given class path is not found.
     */
    Path find(final Path clazz);

    /**
     * Search and return all the Queen files in this classpath.
     * @return List of Path to each found Queen file.
     */
    List<Path> findAll();

    /**
     * Find the Path to the Queen file represented by the given {@link NameNode}.
     * @param name NameNode to be resolved.
     * @return Path.
     */
    default Path find(final NameNode name) {
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
