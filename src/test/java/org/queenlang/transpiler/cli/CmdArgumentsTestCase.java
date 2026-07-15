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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.apache.commons.cli.MissingArgumentException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link CmdArguments}.
 * @author Mihai-Emil Andronache
 * @version $Id$
 * @since 0.0.1
 */
final class CmdArgumentsTestCase {

    /**
     * It can return the 'version' Functionality,
     * @throws Exception, if something goes wrong.
     */
    @Test
    void returnsTheVersionFunctionality() throws Exception {
        final Arguments arguments = new CmdArguments(new String[] {"-v"});
        assertThat(
            arguments.version().isPresent(),
            Matchers.is(true)
        );
    }

    /**
     * It can return the 'help' Functionality,
     * @throws Exception, if something goes wrong.
     */
    @Test
    void returnsTheHelpunctionality() throws Exception {
        final Arguments arguments = new CmdArguments(new String[] {"-h"});
        assertThat(
            arguments.help().isPresent(),
            Matchers.is(true)
        );
    }

    /**
     * It can return the 'createMaven' Functionality,
     * @throws Exception, if something goes wrong.
     */
    @Test
    void returnsTheCreateMavenFunctionality() throws Exception {
        final Arguments arguments = new CmdArguments(new String[] {"-createMaven /projects/queen-project"});
        assertThat(
            arguments.createQueenProject().isPresent(),
            Matchers.is(true)
        );
    }

    /**
     * It throws an Exception if 'createMaven' is missing the required argument.,
     */
    @Test
    void throwsExceptionWhenCreateMavenWithoutArgument() {
        assertThrows(
            MissingArgumentException.class,
            () -> new CmdArguments(new String[]{"-createMaven"})
        );
    }

    /**
     * It can return the '-p' Functionality,
     * @throws Exception, if something goes wrong.
     */
    @Test
    void returnsTheTranspileProjectFunctionality() throws Exception {
        final Arguments arguments = new CmdArguments(new String[] {"-p /projects/queen-project"});
        assertThat(
            arguments.transpileProject().isPresent(),
            Matchers.is(true)
        );
    }

    /**
     * It throws an Exception if '-p' is missing the required argument.,
     */
    @Test
    void throwsExceptionWhenTranspileProjectWithoutArgument() {
        assertThrows(
            MissingArgumentException.class,
            () -> new CmdArguments(new String[]{"-p"})
        );
    }

    /**
     * It can return the '-p' Functionality,
     * @throws Exception, if something goes wrong.
     */
    @Test
    void returnsTheOutputFunctionality() throws Exception {
        final Arguments arguments = new CmdArguments(new String[] {"-o /output/queen-transpiled"});
        assertThat(
            arguments.output().isPresent(),
            Matchers.is(true)
        );
    }

    /**
     * It throws an Exception if '-o' is missing the required argument.,
     */
    @Test
    void throwsExceptionWhenOutputWithoutArgument() {
        assertThrows(
            MissingArgumentException.class,
            () -> new CmdArguments(new String[]{"-o"})
        );
    }
}
