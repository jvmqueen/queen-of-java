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

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.printer.configuration.DefaultPrinterConfiguration;
import org.apache.commons.io.IOUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.queenlang.transpiler.nodes.body.CompilationUnitNode;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

/**
 * QueenASTParser test case.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class QueenASTParserTestCase {

    @ParameterizedTest
    @CsvSource(
        value = {
            "Expressions.queen,Expressions.java",
            "FunnyParameters.queen,FunnyParameters.java",
            "JsonPerson.queen,JsonPerson.java",
            "Preamble.queen,Preamble.java",
            "NaiveFactorial.queen,NaiveFactorial.java",
            "ControlStatementsExamples.queen,ControlStatementsExamples.java",
            "LabeledGraph.queen,LabeledGraph.java",
            "SyncCounter.queen,SyncCounter.java",
            "YamlMapping.queen,YamlMapping.java",
            "ClassWithFields.queen,ClassWithFields.java",
            "Collection.queen,Collection.java",
            "EmptyAnnotatedClass.queen,EmptyAnnotatedClass.java",
            "ExtendedArrayList.queen,ExtendedArrayList.java",
            "EmptyInterface.queen,EmptyInterface.java",
            "GenericConstructor.queen,GenericConstructor.java",
            "EmptyAnnotatedInterface.queen,EmptyAnnotatedInterface.java",
            "EmptyInterfaceWithImports.queen,EmptyInterfaceWithImports.java",
            "EmptyInterfaceWithNoPackage.queen,EmptyInterfaceWithNoPackage.java"
        }
    )
    public void testAstParserWithRandomClasses(final String queenInput, final String javaOutput) throws Exception {
        final String dirPath = "src/test/resources/queenToJava/random/";
        final String expectedJavaClass = this.readTestResource(dirPath, javaOutput);
        final QueenASTParser parser = new QueenASTParserANTLR();

        final CompilationUnitNode compilationUnitNode = parser.parse(Path.of(dirPath, queenInput));

        final CompilationUnit javaCompilationUnit  = new CompilationUnit();
        compilationUnitNode.addToJavaNode(javaCompilationUnit);
        final String javaClass = javaCompilationUnit.toString(new DefaultPrinterConfiguration());

        MatcherAssert.assertThat(
            javaClass,
            Matchers.equalTo(expectedJavaClass)
        );
        StaticJavaParser.parse(javaClass);
    }

    @ParameterizedTest
    @CsvSource(
        value = {
            "ProjectsController.queen,ProjectsController.java",
            "TestEnvFilter.queen,TestEnvFilter.java",
            "JsonContract.queen,JsonContract.java",
        }
    )
    public void testAstParserWithRealClassesSelfWeb(final String queenInput, final String javaOutput) throws Exception {
        final String dirPath = "src/test/resources/queenToJava/real/self-web/";
        final String expectedJavaClass = this.readTestResource(dirPath, javaOutput);
        final QueenASTParser parser = new QueenASTParserANTLR();

        final CompilationUnitNode compilationUnitNode = parser.parse(Path.of(dirPath, queenInput));

        final CompilationUnit javaCompilationUnit  = new CompilationUnit();
        compilationUnitNode.addToJavaNode(javaCompilationUnit);
        final String javaClass = javaCompilationUnit.toString(new DefaultPrinterConfiguration());

        MatcherAssert.assertThat(
            javaClass,
            Matchers.equalTo(expectedJavaClass)
        );
        StaticJavaParser.parse(javaClass);
    }

    /**
     * Read a test resource file's contents.
     * @param dirPath Directory path.
     * @param fileName File to read.
     * @return File's contents as String.
     * @throws FileNotFoundException If something is wrong.
     * @throws IOException If something is wrong.
     */
    private String readTestResource(final String dirPath, final String fileName) throws IOException {
        return new String(
            IOUtils.toByteArray(
                new FileInputStream(
                    dirPath + fileName
                )
            )
        );
    }
}
