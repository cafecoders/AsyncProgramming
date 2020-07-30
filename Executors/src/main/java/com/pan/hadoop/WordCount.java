package com.pan.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.io.IOException;
import java.util.Arrays;

public class WordCount {
    private static FileSystem fs = null;
    static {
        Configuration conf = new Configuration();
        try {
            conf.set("fs.defaultFS", "hdfs://192.168.0.103:9000");
            FileSystem fs = FileSystem.get(conf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SparkConf sparkConf = new SparkConf();
        sparkConf.setAppName("wordCount");
        JavaSparkContext jsc = new JavaSparkContext(sparkConf);
        jsc.textFile("/input/spark.txt").flatMap(line -> Arrays.asList(line.split(" ")).iterator())
                .mapToPair(word -> new Tuple2<>(word, 1))
                .reduceByKey((a, b) -> a+b).saveAsTextFile("/output/word_count");
    }
}
