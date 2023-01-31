package org.test.queen;

import org.test.Student;

@JustTestingExpressions(value = "random_expressions")
public final class Expressions implements ExpressionsTest {

    @Override
    public Student casting(final Object obj) {
        return (Student) obj;
    }

    @Override
    public Student castingLambda(final Object obj) {
        return (Student) () -> obj;
    }
}
