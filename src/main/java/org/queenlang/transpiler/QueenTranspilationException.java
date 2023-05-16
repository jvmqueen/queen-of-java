package org.queenlang.transpiler;

import java.util.List;
import java.util.stream.Collectors;

public final class QueenTranspilationException extends Exception {

    private final String file;
    private final List<String> errors;

    public QueenTranspilationException(final String file, final List<String> errors) {
        super(errors.stream().collect(Collectors.joining(System.lineSeparator())));
        this.file = file;
        this.errors = errors;
    }

    public List<String> errors() {
        return this.errors;
    }

    public String file() {
        return this.file;
    }
}
