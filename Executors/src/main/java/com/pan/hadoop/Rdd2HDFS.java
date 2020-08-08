package com.pan.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SparkSession;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Rdd2HDFS {
    private static FileSystem fs = null;
    static Configuration conf = null;
    static {
        conf = new Configuration();
        try {
            conf.set("fs.defaultFS", "hdfs://192.168.0.103:9000");
            FileSystem fs = FileSystem.get(conf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class User {
        Long userId;
        String userName;
        Integer weight;

        public User(Long userId, String userName, Integer weight) {
            this.userId = userId;
            this.userName = userName;
            this.weight = weight;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public Integer getWeight() {
            return weight;
        }

        public void setWeight(Integer weight) {
            this.weight = weight;
        }
    }

    public static void main(String[] args) throws IOException {
        SparkSession spark = SparkSession.builder()
                                    .appName("Rdd2Hdfs")
                                    .master("local")
                                    .getOrCreate();

        JavaRDD<String> lineRdd = spark.read().textFile("hdfs://192.168.0.103:9000/input/user_list.txt").toJavaRDD();
        JavaRDD<User> userRdd = lineRdd.map(line -> {
            String[] fields = line.split(" ");
            Long userId = Long.parseLong(fields[0]);
            String userName = fields[1];
            Integer weight = Integer.parseInt(fields[2]);
            return new User(userId, userName, weight);
        });

        Dataset<Row> userDataSet = spark.createDataFrame(userRdd, User.class);
        userDataSet.createOrReplaceTempView("t_user");
        Dataset<Row> queryDataset = spark.sql("select * from t_user where weight > 70");

        JavaRDD<Row> rowRdd = queryDataset.toJavaRDD();
        JavaRDD<User> userRddResult = rowRdd.map(row -> {
            Long userId = row.getAs("userId");
            String userName = row.getAs("userName");
            Integer weight = row.getAs("weight");

            return new User(userId, userName, weight);
        });

        userRddResult.saveAsTextFile("hdfs://192.168.0.103:9000/output/hql_res");
    }
}
