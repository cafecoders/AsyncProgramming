package com.pan.chap3Future;

import java.util.ArrayList;
import java.util.List;

public class StreamTestFuture {
    public static String rpcCall(String ip, String param) {
        System.out.println(ip + " rpcCall: " + param);
        try {
            Thread.sleep(1000);
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
        List<String> result = new ArrayList<>();
        for(String ip : ipList) {
            result.add(rpcCall(ip, ip));
        }

        result.stream().forEach(System.out::println);
        System.out.println("cost:" + (System.currentTimeMillis() - start));
    }
}
