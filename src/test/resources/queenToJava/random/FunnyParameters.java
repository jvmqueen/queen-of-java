package org.test.queen;

import org.test.Student;
import com.example.other.Test;
import com.other.*;
import com.example.other.something.elseo.Other;
import com.example.other.more.*;

@org.some.JustTestingFunnyParams({ "random_funny_params" })
public final class FunnyParameters implements FunnyParametersTest {

    @Override
    public Object arrayParam(final Object[][] obj, final Object[][][] three, final Object[] one, final Object nonArray) {
        return null;
    }

    @Override
    public Object annotatedVarArg(final Object[][] obj, final String@AnnonVarArg @Second("Bla") @Third({ 1 }) ... annotatedVarArg) {
        return null;
    }

    @Override
    public Array[][][][] returnsSimpleArrayOrDoesIt(final Some[] stuff) {
        return null;
    }

    @Override
    public <Simple> @AnnotationForResult Simple[][][] returnsSimpleOrDoesIt(final Some[] stuff) {
        return null;
    }

    @Override
    public <Simple> @AnnotationForResult Simple returnsAnnotatedSimple(final Some[] stuff) {
        return null;
    }

    @Override
    public <Simple>  @AnnotationForResult int returnsAnnotatedInt(final Some[] stuff) {
        return null;
    }
}
