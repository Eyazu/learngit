package com.company;


import java.util.concurrent.*;

class H2O {
    /*
    private volatile int H;
    private volatile int O;

     */
    private volatile BlockingQueue<Integer> H2O = new LinkedBlockingQueue<Integer>(2);
    public H2O() {
    }

    public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {

        // releaseHydrogen.run() outputs "H". Do not change or remove this line.
        // System.out.println("Thread : " + Thread.currentThread().getName() + ",count : " + cdlh.getCount());

        H2O.put(0);
        releaseHydrogen.run();
    }

    public void oxygen(Runnable releaseOxygen) throws InterruptedException {
        // releaseOxygen.run() outputs "O". Do not change or remove this line.
        synchronized (this) {
            H2O.take();
            H2O.take();
            releaseOxygen.run();
        }


    }

}

public class H2OTest {
    public static void main(String[] args) {
        H2O h2o = new H2O();
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                try {
                    h2o.oxygen(() -> System.out.print("O"));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    h2o.hydrogen(() -> System.out.print("H"));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }



        try {
            Thread.currentThread().sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



    }
}
