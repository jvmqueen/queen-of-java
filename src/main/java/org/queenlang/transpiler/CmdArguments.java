package org.queenlang.transpiler;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;

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
    public List<Path> files() {
        final List<Path> files = new ArrayList<>();
        if(this.commandLine.getOptionValues('f') != null) {
            for (final String path : this.commandLine.getOptionValues('f')) {
                files.add(Path.of(path));
            }
        }
        return files;
    }

    @Override
    public Classpath classpath() {
        final List<Path> classpaths = new ArrayList<>();
        if(this.commandLine.getOptionValues("cp") != null) {
            for (final String path : this.commandLine.getOptionValues("cp")) {
                classpaths.add(Path.of(path));
            }
        } else {
            classpaths.add(Path.of("."));
        }
        return new PathCp(classpaths);
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
