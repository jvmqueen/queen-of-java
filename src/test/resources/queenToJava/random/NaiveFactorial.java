package org.queenlang.helloworld;

@BigNumbers
public final class NaiveFactorial implements Factorial {

    private int limit;

    public NaiveFactorial(final int limit) {
        this.limit = limit;
    }

    @Override
    public final int result() {
        int current = limit;
        int result = 1;
        while (current > 0) {
            result = result * current;
            current--;
            if (current % 2 == 0) {
                System.out.println("EVEN NUMBER!");
            } else {
                System.out.println("ODD NUMBER!");
            }
            if (2 + 2 == 4) {
                System.out.println("math");
            }
            while (false) {
                System.out.println("bug maybe");
            }
            while (true) {
                System.out.println("Infinite...");
            }
        }
        return result;
    }
}
