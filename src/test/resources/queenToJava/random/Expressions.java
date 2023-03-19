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
        try (Closeable x = 0;
            Closeable y = 0) {
            System.out.println("Close em after try");
        } finally {
        }
        int x = 1, y = 0, z = 2;
        int m, n, p;
        for (int m = 0, v = 0; i < 10; i++) {
            System.out.println("OK");
        }
        return (Student) () -> obj;
    }

    @Override
    public Student returnLambda(final Object obj) {
        return (firstName, lastName) -> new java.util.Student(firstName, lastName);
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

    @Override
    public Student createStudent(final Object obj) {
        new Student(obj);
        new com.test.Student(obj);
        new ArrayList<>();
        new ArrayList<String>();
        new ArrayList<String>(15);
        new <String> ArrayList<String>(15);
        new FunctionalAnonym() {

            private int x;

            private int y = 0;

            @Override
            public void test() {
                return 1;
            }

            @Override
            public void test2() {
                return 2;
            }

            final class Test implements Other {

                private int other = 0;

                @Override
                public void testOther() {
                    System.out.println("ok");
                }
            }
        };
        return new Student(obj);
    }

    @Override
    public boolean conditionalExpr(final boolean test) {
        return test ? true : false;
    }
}
