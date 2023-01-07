package org.queenlang.helloworld;

import com.example.other.Test;
import static java.lang.Math.pow;
import static java.lang.Assert.*;
import com.example.other.Other;
import com.example.other.more.*;

@WithFields
public final class ClassWithFields<T, K extends Other, V extends Stuff, A extends X & Y & Z> implements com.example.SomeInterface, SomeOther {

    int prot = 0;

    private int x;

    private int y;

    private int z = 3;

    @AnnotatedField
    private int p = 2;

    @AnnotatedField
    private int m = 0;

    @AnnotatedField
    private int n = 1;

    private Object objs;

    private List<String> list;

    private List<String> otherList = new ArrayList<>();

    private List<String> otherList2 = new ArrayList<String>();

    private Test test = new AnnonymTest() {
    };

    @AnnotatedField
    private String[] s = new String[] { "1", "2", "3" };

    public ClassWithFields() {
    }

    public ClassWithFields(final int i, final Student student, @NotNull final List<String> stuff, @Array final String... s) {
    }

    public ClassWithFields(@NotEmpty final String... stringArgs) {
    }

    public ClassWithFields(final Integer... integers) {
    }

    public ClassWithFields(final String... pahts) throws FileNotFound, java.io.IOException {
    }

    public String toString() throws Exception, javax.io.SomeIoException {
        return "";
    }
}
