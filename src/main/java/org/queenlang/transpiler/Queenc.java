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

import org.apache.commons.cli.*;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The transpiler's entry point.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class Queenc {
    private static final String NAME_AND_VERSION = "Queenc version: %s";

    private static final Config config = new Config();
    private static final Options options = new Options();

    static {
        Option files = new Option("f", "files", true, "Queen files to transpile.");
        files.setArgs(Option.UNLIMITED_VALUES);
        options.addOption(files);

        Option output = new Option("o", "output", true, "Output path. Defaults to current dir.");
        options.addOption(output);

        Option help = new Option("h", "help", false, "Print help message.");
        options.addOption(help);

        Option version = new Option("v", "version", false, "Print the version of queenc.");
        options.addOption(version);

        Option verbose = new Option("verbose", false, "Print verbose logs.");
        options.addOption(verbose);
    }


    public static void run(final CommandLine cmd) throws IOException, QueenTranspilationException {
        final List<Option> options = Arrays.asList(cmd.getOptions());
        if(options.size() == 0) {
            printFilesMissing();
            return;
        }
        boolean calledWithUtilityParam = false;
        boolean verbose = false;
        for(final Option opt : options) {
            if(opt.getOpt().equalsIgnoreCase("v")) {
                calledWithUtilityParam = true;
                printVersion();
            }
            if(opt.getOpt().equalsIgnoreCase("h")) {
                calledWithUtilityParam = true;
                printHelp();
            }
            if(opt.getOpt().equalsIgnoreCase("verbose")) {
                verbose = true;
            }
        }
        if(cmd.getOptionValues('f') != null) {
            final List<Path> files = new ArrayList<>();
            for (final String path : cmd.getOptionValues('f')) {
                files.add(Path.of(path));
            }
            final Path output;
            if (cmd.getOptionValue('o') != null) {
                output = Path.of(cmd.getOptionValue('o'));
            } else {
                output = Path.of(".");
            }
            final QueenTranspiler transpiler = new JavaQueenTranspiler();

            transpiler.transpile(
                files,
                output,
                verbose
            );
        } else if(!calledWithUtilityParam) {
            printFilesMissing();
        }

//        final String javaClass = transpiler.transpile(
//            EntryPoint.class.getClassLoader()
//                .getResourceAsStream("/path"),
//            "fileName"
//        );
//
//        System.out.println("[DEBUG] Transpiled Java Class:");
//        System.out.println("------------------------------");
//
//        System.out.println(javaClass);
//
//        System.out.println("------------------------------");
//        System.out.println("[DEBUG] End of transpiled java class.");
    }

    public static void main(String[] args) throws QueenTranspilationException {
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            run(cmd);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
//        if(args == null || args.length !=1) {
//            System.out.println("[ERROR] Expecting exactly 1 argument.");
//        } else {
//            System.out.println("[DEBUG] Received arg: " + args[0]);
//            try {
//                //run(args[0]);
//                run("HelloWorldWrong.queen", "HelloWorldWrong.queen");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }

    /**
     * Print quennc's version.
     */
    private static void printVersion() {
        System.out.println(String.format(NAME_AND_VERSION, config.version()));
    }

    /**
     * Print help message, including possible command line args/options.
     */
    private static void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("queenc", options);
    }

    private static void printFilesMissing() {
        System.out.println("Missing files argument (-f of -files).");
        printHelp();
    }
}
