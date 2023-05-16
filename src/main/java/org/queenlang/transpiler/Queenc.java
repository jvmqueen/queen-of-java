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

        Option classpath = new Option("cp", "classpath", true, "Classpath(s) to look for user-defined classes. Defaults to current dir.");
        classpath.setArgs(Option.UNLIMITED_VALUES);
        options.addOption(classpath);

        Option output = new Option("o", "output", true, "Output path. Defaults to current dir.");
        options.addOption(output);

        Option help = new Option("h", "help", false, "Print help message.");
        options.addOption(help);

        Option version = new Option("v", "version", false, "Print the version of queenc.");
        options.addOption(version);

        Option verbose = new Option("verbose", false, "Print verbose logs.");
        options.addOption(verbose);
    }


    public static void run(final Arguments arguments) throws IOException, QueenTranspilationException {
        if(arguments.verbose()) {
            System.out.println("RUNNING VERBOSE... set logger to DEBUG");
        }
        if(!arguments.files().isEmpty()) {
            final QueenTranspiler transpiler = new QueenToJavaTranspiler(
                new QueenASTParserANTLR(),
                arguments.classpath(),
                new JavaFileOutput(arguments.output())
            );
            transpiler.transpile(arguments.files());
        } else if(!arguments.hasUtilityArgs()) {
            printFilesMissing();
        }

        if(arguments.version()) {
            printVersion();
        }
        if(arguments.help()) {
            printHelp();
        }
    }

    public static void main(String[] args) throws QueenTranspilationException {
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            run(new CmdArguments(cmd));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (QueenTranspilationException ex) {
            System.err.println("Errors in file " + ex.file() + ": ");
            ex.errors().forEach(System.err::println);
        } catch (IllegalStateException ex) {
            if(ex.getCause() instanceof QueenTranspilationException) {
                final QueenTranspilationException queenTranspilationException = (QueenTranspilationException) ex.getCause();
                System.err.println("Errors in file " + queenTranspilationException.file() + ": ");
                queenTranspilationException.errors().forEach(System.err::println);
            } else {
                throw new RuntimeException(ex);
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
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
