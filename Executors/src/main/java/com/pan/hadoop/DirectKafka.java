package com.pan.hadoop;
import kafka.serializer.StringDecoder;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import scala.Tuple2;

import java.util.*;

public class DirectKafka {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("direct kafka").setMaster("local[2]");
        JavaStreamingContext jsc = new JavaStreamingContext(conf, Durations.seconds(5));

        Map<String, String> kafkaParam = new HashMap<>();
        kafkaParam.put("metadata.broker.list", "spark1:9092,spark2:9092,spark3:9092");
        Set<String> topics = new HashSet<>();
        topics.add("DirectKafkaSparkStreaming");

        JavaPairInputDStream<String, String> lineList = KafkaUtils.createDirectStream(jsc, String.class, String.class, StringDecoder.class, StringDecoder.class, kafkaParam, topics);

        JavaDStream<String> wordDStream = lineList.flatMap(tuple -> Arrays.asList(tuple._2.split(" ")).iterator());

        wordDStream.foreachRDD(word -> System.out.println(word));
    }
}
