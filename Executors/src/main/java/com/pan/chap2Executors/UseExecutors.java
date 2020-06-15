package com.pan.chap2Executors;

import java.util.concurrent.*;

public class UseExecutors {
    public static final int AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();
    public static final ThreadPoolExecutor POOL_EXECUTOR = new ThreadPoolExecutor(AVAILABLE_PROCESSORS,
                                                            AVAILABLE_PROCESSORS*2, 1, TimeUnit.MINUTES,
                                                            new LinkedBlockingQueue<>(5),
                                                            new ThreadPoolExecutor.CallerRunsPolicy());

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        long start = System.currentTimeMillis();

        Future<?> submit = POOL_EXECUTOR.submit(() -> {
            try {
                SyncExample.doSomethingA();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        SyncExample.doSomethingB();

        System.out.println(submit.get());

        System.out.println(System.currentTimeMillis() - start);

        //Thread.currentThread().join();
    }

}
