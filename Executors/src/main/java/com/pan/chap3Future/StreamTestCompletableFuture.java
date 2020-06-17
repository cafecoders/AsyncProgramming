package com.pan.chap3Future;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.pan.chap3Future.StreamTestFuture.rpcCall;

/**
 * runAsync:无返回值
 * supplyAsync:有返回值
 * thenRun:接收异步任务
 * thenApply:接收函数
 * thenAccept:接收consumer
 */
public class StreamTestCompletableFuture {
    public static void main(String[] args) {
        List<String> ipList = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            ipList.add("192.168.0." + i);
        }

        long start = System.currentTimeMillis();
        List<CompletableFuture<String>> futureList = ipList.stream()
                .map(ip -> CompletableFuture.supplyAsync(() -> rpcCall(ip, ip)))
                .collect(Collectors.toList());//同步转异步

        List<String> resultList = futureList.stream()
                .map(future -> future.join())
                .collect(Collectors.toList());//同步等待结果

        resultList.stream().forEach(r -> System.out.println(r));

        System.out.println("cost:" + (System.currentTimeMillis()-start));
    }
}
