package com.pan.chap3Future;

import java.util.concurrent.*;

public class ComFutureRunAsync {
    public static final ThreadPoolExecutor bizPoolExecutor
            = new ThreadPoolExecutor(8, 8, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<>(10));

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        supplyAsync();
    }

    public static void runAsync() throws ExecutionException, InterruptedException {
        CompletableFuture future = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("over");
        });

        System.out.println(future.get());
    }

    public static void runAsyncWithBizExecutor() throws ExecutionException, InterruptedException {
        CompletableFuture future = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("biz over");
        }, bizPoolExecutor);

        System.out.println(future.get());
    }

    /**
     * 带有返回值
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void supplyAsync() throws ExecutionException, InterruptedException {
        CompletableFuture future = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return "hey man, period";
        });

        System.out.println(future.get());
    }
}
