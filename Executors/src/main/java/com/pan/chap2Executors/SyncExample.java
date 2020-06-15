package com.pan.chap2Executors;

/**
 * 同步的例子
 */
public class SyncExample {
    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();

        new Thread(() -> {
            try {
                doSomethingA();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        doSomethingB();

        System.out.println(System.currentTimeMillis() - start);
    }

    public static String doSomethingA() throws InterruptedException {
        Thread.sleep(2000);

        System.out.println("do sth A----------------");

        return "A Task Done";
    }

    public static void doSomethingB() throws InterruptedException {
        Thread.sleep(2000);

        System.out.println("do sth B----------------");
    }
}
