package com.pan.chap5React;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;

public class AsyncRpcCall4 {
    public static String rpcCall(String ip, String param) {
        System.out.println(Thread.currentThread().getName() + " " + ip + " rpc call: " + param);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return param;
    }

    public static void main(String[] args) {
        List<String> ipList = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            ipList.add("192.168.0." + i);
        }

        long start = System.currentTimeMillis();
        Flowable.fromArray(ipList.toArray(new String[0]))
                .map(v -> rpcCall(v, v))
                .observeOn(Schedulers.io())
                .subscribe(System.out::println);

        System.out.println("cost:" + (System.currentTimeMillis() - start));
    }
}
