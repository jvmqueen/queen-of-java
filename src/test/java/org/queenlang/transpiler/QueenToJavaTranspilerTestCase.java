package org.queenlang.transpiler;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.queenlang.classpath.PathsCp;
import org.queenlang.queen.QueenASTParserANTLR;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

final class QueenToJavaTranspilerTestCase{

    @Test
    void testProjectTranspilation() throws Exception{
        QueenTranspiler transpiler = new QueenToJavaTranspiler(
            new QueenASTParserANTLR(),
            new PathsCp(List.of(Path.of("."))),
            new JavaFileOutput(Path.of("."))
        );
        final Path helloWorld = Paths.get("src/test/resources/queenToJava/random/HelloWorld.queen");
        transpiler.transpile(List.of(helloWorld));
    }
}
