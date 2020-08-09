package com.pan.hadoop;

import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

public class SocketToPrint {
    public static void main(String[] args) throws InterruptedException {
        SparkConf sparkConf = new SparkConf();
        sparkConf.setMaster("local[2]");
        sparkConf.setAppName("WordCountOnline");

        JavaStreamingContext jsc = new JavaStreamingContext(sparkConf, Durations.seconds(5));
        JavaReceiverInputDStream<String> jobLines = jsc.socketTextStream("192.168.0.103", 9999);

        jobLines.foreachRDD(rdd -> {
            long size = rdd.count();
            System.out.println("----foreach-call-collection-size-" + size + "---");
            rdd.foreach(line -> System.out.println(line));
        });

        jsc.start();
        System.out.println("--------------already start------------");
        jsc.awaitTermination();
        System.out.println("-------------already await-------------");
        jsc.close();
        System.out.println("-------------already close-------------");
    }
}
