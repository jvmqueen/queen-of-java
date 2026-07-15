package org.queenlang.transpiler.cli;

import com.github.javaparser.utils.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public final class TranspileQueenProject implements Functionality {
    private final Logger LOG = LoggerFactory.getLogger(TranspileQueenProject.class);
    private final Path queenProjectDirectory;
    private final Path outputDirectory;

    public TranspileQueenProject(final Path queenProjectDirectory, final Path outputDirectory) {
        this.queenProjectDirectory = queenProjectDirectory;
        this.outputDirectory = outputDirectory;
    }

    @Override
    public void execute() {
        LOG.debug("Transpiling queen project from [{}] to [{}].", queenProjectDirectory, outputDirectory);
        LOG.debug("Finished transpiling Queen project.");
    }
}
