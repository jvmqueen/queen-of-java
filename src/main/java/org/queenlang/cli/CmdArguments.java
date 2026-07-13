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
package org.queenlang.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.queenlang.classpath.Classpath;
import org.queenlang.classpath.PathsCp;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class CmdArguments implements Arguments {

    private final CommandLine commandLine;

    public CmdArguments(final CommandLine commandLine) {
        this.commandLine = commandLine;
    }

    @Override
    public boolean verbose() {
        return this.isOptionPresent("verbose");
    }

    @Override
    public boolean version() {
        return this.isOptionPresent("v");
    }

    @Override
    public boolean help() {
        return this.isOptionPresent("h");
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
        if(this.commandLine.getOptionValues('f') != null) {
            return Path.of(this.commandLine.getOptionValue('f'));
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
