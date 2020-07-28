package com.pan.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.StringTokenizer;

public class MapReduceAPI {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://192.168.0.103:9000");
        Job job = Job.getInstance(conf, "word_count");
        job.setJarByClass(MapReduceAPI.class);
        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);
        job.setNumReduceTasks(1);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        String in = "/input/stu.json";
        String out = "/input/word_count";
        Path input = new Path(in);
        Path output = new Path(out);

        FileInputFormat.addInputPath(job, input);
        FileOutputFormat.setOutputPath(job, output);

        System.out.println(job.waitForCompletion(true) ? 0 : 1);
    }

    static class WordCountMapper extends Mapper<Object, Text, Text, Text> {
        @Override
        protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String s = value.toString();
            StringTokenizer st = new StringTokenizer(s);
            while(st.hasMoreElements()) {
                String wd = st.nextToken();
                String word = wd.replace(",", "")
                        .replace(".", "")
                        .replace(":", "")
                        .replace("“", "")
                        .replace("”", "")
                        .replace(";", "")
                        .replace("?", "")
                        .replace("(", "")
                        .replace(")", "");
                System.out.println("==================word: " + word);
                context.write(new Text(word), new Text("1"));
            }
        }
    }

    static class WordCountReducer extends Reducer<Text, Text, Text, Text> {
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            for(Text val : values) {
                String[] q = val.toString().split("\t");
                int num = Integer.parseInt(q[0]);
                sum += num;
            }

            context.write(key, new Text("出现：" + sum + "次"));

        }
    }
}
