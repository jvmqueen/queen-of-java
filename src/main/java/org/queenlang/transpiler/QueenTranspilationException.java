package org.queenlang.transpiler;

import java.util.List;
import java.util.stream.Collectors;

public final class QueenTranspilationException extends Exception {

    private final List<String> errors;

    public QueenTranspilationException(final List<String> errors) {
        super(errors.stream().collect(Collectors.joining(System.lineSeparator())));
        this.errors = errors;
    }

    public List<String> errors() {
        return this.errors;
    }
}
