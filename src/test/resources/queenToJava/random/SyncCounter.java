package org.queen.random.tests;

public final class SyncCounter implements Counter {

    private int counter = 0;

    public int getCounter() {
        return this.counter;
    }

    public int increment() {
        synchronized (this) {
            System.out.println("Entering synchronized block!");
            this.setCounter(this.getCounter()++);
            System.out.println("Exiting synchronized block!");
        }
        return this.counter;
    }

    private void setCounter(final int newValue) {
        this.counter = newValue;
    }
}
