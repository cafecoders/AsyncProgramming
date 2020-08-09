package com.pan.hadoop;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import scala.Tuple2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

public class WordCountWithMySQL {
    private static Connection conn;
    private static PreparedStatement pstat;

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://192.168.0.103:3306/test?characterEncoding=utf8", "root", "");
            pstat = conn.prepareStatement("insert into wordcount(word, count) values (?, ?)");
        } catch (Exception e){
            e.printStackTrace();

        }
    }

    public static int save(Tuple2<String, Integer> line) throws SQLException {
        pstat.setString(1, line._1);
        pstat.setInt(2, line._2);

        return pstat.executeUpdate();
    }

    public static void saveToDB(JavaPairRDD<String, Integer> pairRDD) {
        pairRDD.foreach(line -> save(line));
    }

    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder().appName("Local Java Spark RDD")
                            .master("local").getOrCreate();
        JavaRDD<String> lineRdd = spark.read().textFile("hdfs://192.168.0.103:9000/input/spark.txt").toJavaRDD();
//        JavaRDD<String> lineRdd = spark.read().textFile("/root/pws/wordcount/").toJavaRDD();
        JavaRDD<String> wordRdd = lineRdd.flatMap(line -> Arrays.asList(line.split(" ")).iterator());
        JavaPairRDD<String, Integer> wordTupleList = wordRdd.mapToPair(word -> new Tuple2<String, Integer>(word, 1));
        JavaPairRDD<String, Integer> wordGroupList = wordTupleList.reduceByKey((a, b) -> a + b);
        saveToDB(wordGroupList);
    }
}
