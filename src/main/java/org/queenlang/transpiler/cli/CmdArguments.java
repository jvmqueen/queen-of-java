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
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.queenlang.classpath.Classpath;
import org.queenlang.classpath.PathsCp;
import org.queenlang.transpiler.Config;
import org.queenlang.transpiler.cli.functionalities.CreateQueenProject;
import org.queenlang.transpiler.cli.functionalities.Functionality;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public final class CmdArguments implements Arguments {
    private static final Logger LOG = LoggerFactory.getLogger(CmdArguments.class);
    private static final Config config = new Config();

    private final CommandLine commandLine;
    private final Options cliOptions;

    public CmdArguments(final CommandLine commandLine, final Options cliOptions) {
        this.commandLine = commandLine;
        this.cliOptions = cliOptions;
    }

    @Override
    public Optional<Functionality> version() {
        if(this.isOptionPresent("v")) {
            return Optional.of(
                () -> LOG.info("Queenc version: {}", config.version())
            );
        }
        return Optional.empty();
    }

    @Override
    public Optional<Functionality> help() {
        if(this.isOptionPresent("h")) {
            return Optional.of(
                () -> {
                    HelpFormatter formatter = new HelpFormatter();
                    formatter.printHelp("queenc", this.cliOptions);
                }
            );
        }
        return Optional.empty();
    }

    @Override
    public Optional<CreateQueenProject> createQueenProject() {
        if (this.commandLine.getOptionValue("ctp") != null) {
            final Path parentDir = Path.of(this.commandLine.getOptionValue("ctp"));
            return Optional.of(new CreateQueenProject(parentDir));
        }
        return Optional.empty();
    }

    @Override
    public Path output() {
        final Path output;
        if (this.commandLine.getOptionValue('o') != null) {
            output = Path.of(this.commandLine.getOptionValue('o'));
        } else {
            output = Path.of(".");
        }
        return output;
    }

    @Override
    public Path project() {
        if(this.commandLine.getOptionValues('p') != null) {
            return Path.of(this.commandLine.getOptionValue('p'));
        }
        return null;
    }

    @Override
    public Classpath classpath() {
        final List<Path> classpaths = new ArrayList<>();
        if(this.project() != null) {
            classpaths.add(this.project());
        }
        if(this.commandLine.getOptionValues("cp") != null) {
            for (final String path : this.commandLine.getOptionValues("cp")) {
                classpaths.add(Path.of(path));
            }
        }
        return new PathsCp(classpaths);
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
