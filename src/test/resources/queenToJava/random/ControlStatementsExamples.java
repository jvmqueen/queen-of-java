package org.queenlang.helloworld;

@ControlStatements("examples")
public final class ControlStatementsExamples implements IControlStatementsExamples {

    @Override
    public final int simpleForLoop() {
        System.out.println("Before the for loop!");
        for (int i = 0; i < 10; i++) {
            System.out.println("Value of i is: " + i);
            System.out.println("Inside for-loop!");
        }
        System.out.println("Got out of the first for loop!");
        for (int a = 3, b = 5; a < 99; a++, b++) {
            hello();
        }
        for (a = 3, b = 5; a < 99; a++) {
            hello();
        }
        for (a(), b(); false; ) {
            hello();
        }
        for (; false; ) {
            somethingElse();
        }
        return 1;
    }

    @Override
    public final int forEachLoop() {
        System.out.println("Before the for loop!");
        for (String s : getListOfStrings()) {
            System.out.println("Enhanced String iteration: " + s);
            System.out.println("Something else");
        }
        for (Student s : new ArrayList<Student>()) {
            System.out.println(s);
        }
        for (Post p : posts) {
            System.out.println("Reading comments of post " + p.name() + "... ");
            for (Comment c : p.comments()) {
                System.out.println("Comment: " + c.toString());
            }
            System.out.println("Finished reading comments of Post " + p.name());
        }
        return 1;
    }
}
