package org.queenlang.transpiler;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.printer.configuration.PrettyPrinterConfiguration;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.queenlang.generated.antlr4.QueenLexer;
import org.queenlang.generated.antlr4.QueenParser;
import org.queenlang.generated.antlr4.QueenParserBaseListener;
import org.queenlang.transpiler.nodes.QueenNode;

import java.io.*;

/**
 * The transpiler's entrypoint.
 */
public final class EntryPoint {
    public static void run(String path) throws IOException {
        final StringBuilder builder = new StringBuilder();
        try (
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                    EntryPoint.class.getClassLoader()
                        .getResourceAsStream(path)
                )
            )
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append('\n');
            }
        }
        System.out.println("[DEBUG] Queen file contents: ");
        System.out.println("------------------------------");
        System.out.println(builder);
        System.out.println("------------------------------");
        System.out.println("[DEBUG] End of Queen file contents.");

        QueenLexer lexer = new QueenLexer(
            CharStreams.fromString(builder.toString())
        );
        QueenParser parser = new QueenParser(
            new CommonTokenStream(lexer)
        );
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(
            new QueenParserBaseListener() {
                int i = 1;
                public void enterPackageName(QueenParser.PackageNameContext ctx) {
                    System.out.println("ENTER PACKAGE NAME: " + i + " " + ctx.getText());
                    i++;
                }
            },
            parser.compilationUnit()
        );

        System.out.println("ERRORS: " + parser.getNumberOfSyntaxErrors());

        QueenParser parser2 = new QueenParser(
            new CommonTokenStream(
                new QueenLexer(CharStreams.fromString(builder.toString()))
            )
        );
        QueenVisitor visitor = new QueenVisitor();
        QueenNode queenCompilationUnitNode = visitor.visitCompilationUnit(parser2.compilationUnit());
        CompilationUnit javaCompilationUnit  = new CompilationUnit();
        queenCompilationUnitNode.addToJavaNode(javaCompilationUnit);

        System.out.println("[DEBUG] Transpiled Java Class:");
        System.out.println("------------------------------");

        System.out.println(
            javaCompilationUnit.toString(new PrettyPrinterConfiguration())
        );

        System.out.println("------------------------------");
        System.out.println("[DEBUG] End of transpiled java class.");
        
    }

    public static void main(String[] args) {
//        if(args == null || args.length !=1) {
//            System.out.println("[ERROR] Expecting exactly 1 argument.");
//        } else {
//            System.out.println("[DEBUG] Received arg: " + args[0]);
            try {
                //run(args[0]);
                run("HelloWorld.queen");
            } catch (IOException e) {
                e.printStackTrace();
            }
//        }
    }
}
