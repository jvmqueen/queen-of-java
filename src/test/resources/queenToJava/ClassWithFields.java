package org.queenlang.helloworld;

import com.example.other.Test;
import static java.lang.Math.pow;
import static java.lang.Assert.*;
import com.example.other.Other;
import com.example.other.more.*;

@WithFields
public final class ClassWithFields implements SomeInterface {

    int prot = 0;

    private final int x;

    private final int y;

    private final int z = 3;

    @AnnotatedField
    private final int p = 2;

    @AnnotatedField
    private final int m = 0;

    @AnnotatedField
    private final int n = 1;

    private final Object objs;

    private final List<String> list;

    private final List<String> otherList = new ArrayList<>();

    private final List<String> otherList2 = new ArrayList<String>();

    private final Test test = new AnnonymTest() {
    };

    @AnnotatedField
    private final String[] s = new String[] { "1", "2", "3" };

    public ClassWithFields() {
    }

    public ClassWithFields(final int i, final Student student, @NotNull List<String> stuff, @Array final String... s) {
    }

    public ClassWithFields(@NotEmpty final String... stringArgs) {
    }

    public ClassWithFields(Integer... integers) {
    }

    public ClassWithFields(String... pahts) throws FileNotFound, java.io.IOException {
    }
}
