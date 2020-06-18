package com.pan.chap5React;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

public class AsyncCall {
    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();
        Flowable.fromCallable(() -> {
            Thread.sleep(1000);
            return "Done";
        }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single())
                .subscribe(System.out::println, Throwable::printStackTrace);

        System.out.println("cost: " + (System.currentTimeMillis() - start));

        Thread.sleep(2000);
    }
}
