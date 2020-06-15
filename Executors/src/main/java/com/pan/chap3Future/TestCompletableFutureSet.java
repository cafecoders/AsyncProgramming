package com.pan.chap3Future;

import java.util.concurrent.*;

public class TestCompletableFutureSet {
    private static final int AVALIABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();
    private static final ThreadPoolExecutor POOL_EXECUTOR = new ThreadPoolExecutor(AVALIABLE_PROCESSORS,
                                                            AVALIABLE_PROCESSORS*2, 1, TimeUnit.MINUTES,
                                                            new LinkedBlockingQueue<>(5), new ThreadPoolExecutor.CallerRunsPolicy());

    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<String> future = new CompletableFuture<>();
        POOL_EXECUTOR.execute(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("----------" + Thread.currentThread().getName() + " set future result--------");
            future.complete("completed");
        });

        System.out.println("------------main thread wait future result--------");
        System.out.println(future.get(1000, TimeUnit.SECONDS));
        System.out.println("-------------main thread got future result----------");
    }
}
