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

    @Override
    public Student returnLambda(final Object obj) {
        return (firstName, lastName) -> new Student(firstName, lastName);
    }

    @Override
    public Student returnLambdaEmptyStudent(final Object obj) {
        Supplier<Student> supplier = firstName -> new Student(firstName);
        Supplier<Student> another = (firstName, lastName, grades) -> {
            final Student s = new Student();
            s.setFirstName(firstName);
            s.setLastName(lastName);
            s.setGrades(grades);
            return s;
        };
        return () -> new Student(firstName, lastName);
    }

    @Override
    public boolean isInstanceOfStudent(final Object obj) {
        return obj instanceof Student;
    }
}
