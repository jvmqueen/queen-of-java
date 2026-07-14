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
package org.queenlang.transpiler.cli.functionalities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Create a scaffold Queen project.
 * @author Mihai-Emil Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class CreateQueenMavenProject implements Functionality {
    private final Logger LOG = LoggerFactory.getLogger(CreateQueenMavenProject.class);

    private final Path parentDir;
    private final List<String> projectDirStructure = new ArrayList<>();

    private static final String SRC_MAIN_QUEEN = "src/main/queen";
    private static final String SRC_MAIN_JAVA = "src/main/java";
    private static final String SRC_MAIN_RESOURCES = "src/main/resources";

    private static final String SRC_TEST_QUEEN = "src/test/queen";
    private static final String SRC_TEST_JAVA = "src/test/java";
    private static final String SRC_TEST_RESOURCES = "src/test/resources";

    public CreateQueenMavenProject(Path parentDir) {
        this.parentDir = Path.of(System.getProperty("user.home"), parentDir.toString());
        this.projectDirStructure.add(SRC_MAIN_QUEEN);
        this.projectDirStructure.add(SRC_MAIN_JAVA);
        this.projectDirStructure.add(SRC_MAIN_RESOURCES);
        this.projectDirStructure.add(SRC_TEST_QUEEN);
        this.projectDirStructure.add(SRC_TEST_JAVA);
        this.projectDirStructure.add(SRC_TEST_RESOURCES);

    }

    @Override
    public void execute() {
        LOG.info("Creating a Queen project...");
        this.createDirectoryStructure(this.parentDir);
        for(final String subDir : this.projectDirStructure) {
            this.createDirectoryStructure(Path.of(this.parentDir.toString(), subDir));
        }
        this.copyFile(
            Paths.get("src/main/resources/EntryPoint.queen"),
            Path.of(this.parentDir.toString(), SRC_MAIN_QUEEN, "com/example/EntryPoint.queen")
        );
        this.copyFile(
            Paths.get("src/main/resources/queen-pom.xml"),
            Path.of(this.parentDir.toString(), "pom.xml")
        );
        this.copyFile(
            Paths.get("src/main/resources/queen-readme.md"),
            Path.of(this.parentDir.toString(), "README.md")
        );
        LOG.info("Queen project created successfully under {}", this.parentDir);
    }

    private void createDirectoryStructure(final Path dir) {
        LOG.info("Creating directory: {}", dir);
        if(!Files.exists(dir)) {
            try {
                Files.createDirectories(dir);
            } catch (final IOException ex) {
                LOG.error("IOException while creationg directory {}. Message {}", parentDir, ex.getMessage());
                throw new IllegalStateException("IOException when creating directory.", ex);
            }
        }
    }

    private void copyFile(Path source, Path target) {
        try {
            if(!Files.exists(target)) {
                Files.createDirectories(target);
            }
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            } catch (final IOException ex) {
                LOG.error("IOException while creationg directory {}. Message {}", parentDir, ex.getMessage());
                throw new IllegalStateException("IOException when creating directory.", ex);
            }
    }
}
