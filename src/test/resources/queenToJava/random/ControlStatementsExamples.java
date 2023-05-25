package org.queenlang.helloworld;

@ControlStatements("examples")
public final class ControlStatementsExamples implements IControlStatementsExamples {

    {
    }

    static {
    }

    public ControlStatementsExamples() {
    }

    @Override
    public final int simpleForLoop() {
        System.out.println("Before the for loop!");
        for (int i = 0; i < 10; i++) {
            System.out.println("Value of i is: " + i);
            System.out.println("Inside for-loop!");
        }
        System.out.println("Got out of the first for loop!");
        for (int a = 3, b = 5; a < 99; a++, b++) hello();
        for (a = 3, b = 5; a < 99; a++) {
            hello();
        }
        for (a(), b(); ; ) hello();
        for (; ; ) {
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
        for (Student s : new ArrayList<Student>()) System.out.println(s);
        for (Post p : posts) {
            System.out.println("Reading comments of post " + p.name() + "... ");
            for (Comment c : p.comments()) {
                System.out.println("Comment: " + c.toString());
            }
            System.out.println("Finished reading comments of Post " + p.name());
        }
        return 1;
    }

    @Override
    public String getStringMonth(final int month) {
        String monthString;
        switch(month) {
            case 1:
                monthString = "January";
                break;
            case 2:
                monthString = "February";
                break;
            case 3:
                monthString = "March";
                break;
            case 4:
                monthString = "April";
                break;
            case 5:
                monthString = "May";
                break;
            case 6:
                monthString = "June";
                break;
            case 7:
                monthString = "July";
                break;
            case 8:
                monthString = "August";
                break;
            case 9:
                monthString = "September";
                break;
            case 10:
                monthString = "October";
                break;
            case 11:
                monthString = "November";
                break;
            case 12:
                monthString = "December";
                break;
            default:
                monthString = "Invalid month";
                break;
        }
        System.out.println(monthString);
        return monthString;
        ;
        ;
        ;
    }

    @Override
    public void emptyAll() {
        if (i == 2) {
        }
        while (true) {
        }
        for (; ; ) {
        }
        do {
        } while (true);
        if (i == 3) {
        } else {
        }
        synchronized (this) {
        }
        try {
        } catch (Exception ex) {
        } finally {
        }
    }

    @Override
    public void emptyMethod() {
    }
}
