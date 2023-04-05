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

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

/**
 * Unit tests for {@link PathsCp}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class PathsCpTestCase {

    /**
     * An existing file is found in the classpath.
     */
    @Test
    public void findsQueenFileInClassPath() {
        final Classpath cp = new PathsCp(
            Arrays.asList(
                Path.of("."),
                Path.of("src/test/resources"),
                Path.of("src/test/resources/test_classpath")
            )
        );
        final Path found = cp.find(Path.of("com/example/Greeting.queen"));
        MatcherAssert.assertThat(
            found,
            Matchers.equalTo(Path.of("src/test/resources/test_classpath/com/example/Greeting.queen"))
        );
    }

    /**
     * Returns null if the queen file is not found in the existing classpath.
     */
    @Test
    public void returnsNullIfQueenFileNotFoundInClassPath() {
        final Classpath cp = new PathsCp(
            Arrays.asList(
                Path.of("."),
                Path.of("src/test/resources"),
                Path.of("src/test/resources/test_classpath")
            )
        );
        final Path found = cp.find(Path.of("missing/example/Greeting.queen"));
        MatcherAssert.assertThat(
            found,
            Matchers.nullValue()
        );
    }

}
