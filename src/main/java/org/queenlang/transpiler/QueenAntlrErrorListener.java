package org.queenlang.transpiler;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import java.util.ArrayList;
import java.util.List;

public final class QueenAntlrErrorListener extends BaseErrorListener {

    private final List<String> errors;

    public QueenAntlrErrorListener() {
        this.errors = new ArrayList<>();
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer,
                            Object offendingSymbol,
                            int line,
                            int charPositionInLine,
                            String msg,
                            RecognitionException e)
    {
        this.errors.add("line " + line + ":" + charPositionInLine + " " + msg);
    }

    public List<String> errors() {
        return this.errors;
    }
}
