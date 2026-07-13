package org.queenlang.transpiler;
import org.apache.commons.cli.*;

public final class QueenCliOptions {

    public static Options cliOptions() {
        final Options options = new Options();

        Option projectPath = new Option("p", "project", true, "Path to the Queen project directory to compile.");
        options.addOption(projectPath);

        Option createTemplateProject = new Option("ctp", "createTemplateProject", true, "Create a template Queen project. Path to the parent dir.");
        options.addOption(createTemplateProject);

        Option classpath = new Option("cp", "classpath", true, "Classpath(s) to look for user-defined classes. Project dir is always included.");
        classpath.setArgs(Option.UNLIMITED_VALUES);
        options.addOption(classpath);

        Option output = new Option("o", "output", true, "Output path. Defaults to current dir.");
        options.addOption(output);

        Option help = new Option("h", "help", false, "Print help message.");
        options.addOption(help);

        Option version = new Option("v", "version", false, "Print the version of queenc.");
        options.addOption(version);

        return options;
    }
}
