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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.queenlang.classpath.Classpath;
import org.queenlang.transpiler.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public final class CmdArguments implements Arguments {
    private static final Logger LOG = LoggerFactory.getLogger(CmdArguments.class);
    private static final Config config = new Config();

    private final CommandLine commandLine;

    public CmdArguments(final String[] args) throws ParseException {
        this.commandLine = new DefaultParser().parse(
            this.allPossitbleOptions(), args
        );
    }

    @Override
    public Optional<Functionality> version() {
        if(this.isOptionPresent("v")) {
            return Optional.of(
                () -> LOG.info("queenc version: {}", config.version())
            );
        }
        return Optional.empty();
    }

    @Override
    public Optional<Functionality> help() {
        if(this.isOptionPresent("h")) {
            final Options allOptions = this.allPossitbleOptions();
            return Optional.of(
                () -> {
                    HelpFormatter formatter = new HelpFormatter();
                    formatter.printHelp("queenc", allOptions);
                }
            );
        }
        return Optional.empty();
    }

    @Override
    public Optional<CreateQueenMavenProject> createQueenProject() {
        if (this.commandLine.getOptionValue("cm") != null) {
            final Path parentDir = Path.of(this.commandLine.getOptionValue("cm"));
            return Optional.of(new CreateQueenMavenProject(parentDir));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Functionality> output() {
        if (this.isOptionPresent("o") && !this.isOptionPresent("p")) {
            final String out = this.commandLine.getOptionValue("o");
            return Optional.of(
                () -> LOG.warn("queenc: Output {} specified without a project path (-p). Doing nothing.", out)
            );
        }
        return Optional.empty();
    }

    @Override
    public Optional<TranspileQueenProject> transpileProject() {
        if(this.commandLine.getOptionValues('p') != null) {
            final Path parentDir = Path.of(this.commandLine.getOptionValue('p'));
            final Path output;
            if(commandLine.getOptionValue("o") == null) {
                output = Path.of(parentDir.toString(), "target", "generated-sources", "queen", "java");
            } else {
                output = Path.of(this.commandLine.getOptionValue('o'));
            }
            return Optional.of(new TranspileQueenProject(parentDir, output));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Functionality> classpath() {
        throw new UnsupportedOperationException("Not yet implemented.");

    }

    private boolean isOptionPresent(final String name) {
        final List<Option> options = Arrays.asList(this.commandLine.getOptions());
        if(options.size() == 0) {
            return false;
        }
        for(final Option opt : options) {
            if(opt.getOpt().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
}
