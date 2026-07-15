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

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.TERMINATE;

/**
 * A queen's project classpath. The place where all the user-defined Queen files
 * are expected to be found.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class ProjectClasspath implements Classpath {

    private final Path projectDir;

    public ProjectClasspath(final Path projectDir) {
        this.projectDir = projectDir;
    }

    @Override
    public Path find(final Path queenFile) {
        final List<Path> found = new ArrayList<>();
        try {
            Files.walkFileTree(
                this.projectDir,
                new SimpleFileVisitor<>() {
                    private Path root;

                    @Override
                    public FileVisitResult preVisitDirectory(
                        final Path dir, final BasicFileAttributes attrs
                    ) throws IOException {
                        if (this.root == null) {
                            this.root = dir;
                        }
                        return super.preVisitDirectory(dir, attrs);
                    }

                    @Override
                    public FileVisitResult visitFile(
                        final Path path,
                        final BasicFileAttributes attrs
                    ) {
                        if (path.equals(queenFile)) {
                            found.add(path);
                        }
                        return TERMINATE;
                    }
                }
            );
        } catch (final IOException ex) {
            throw new RuntimeException("IOException while searching for Queen file: " + queenFile, ex);
        }

        return found.size() == 1 ? found.get(0) : null;
    }

    @Override
    public List<Path> findAll() {
        final List<Path> queenFiles = new ArrayList<>();
        try {
            Files.walkFileTree(
                this.projectDir,
                new SimpleFileVisitor<>() {
                    private Path root;

                    @Override
                    public FileVisitResult preVisitDirectory(
                        final Path dir, final BasicFileAttributes attrs
                    ) throws IOException {
                        if (this.root == null) {
                            this.root = dir;
                        }
                        return super.preVisitDirectory(dir, attrs);
                    }

                    @Override
                    public FileVisitResult visitFile(
                        final Path path,
                        final BasicFileAttributes attrs
                    ) {
                        if (path.getFileName().endsWith(".queen")) {
                            queenFiles.add(path);
                        }
                        return CONTINUE;
                    }
                }
            );
        } catch (final IOException ex) {
            throw new RuntimeException("IOException while traversing directory: " + this.projectDir, ex);
        }
        return queenFiles;
    }

}
