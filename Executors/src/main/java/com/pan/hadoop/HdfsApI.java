package com.pan.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class HdfsApI {
    public static void main(String[] args) throws IOException {
        String s = "hey man, this is whether at Monday!\n";
        //createFile("/input/someFile", s.getBytes());

        //uploadFile("E://students.json", "/input/");
        readFromFile("/input/user_list.txt");
    }

    public static void createFile(String dst, byte[] contents) throws IOException {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://192.168.0.103:9000");
        FileSystem fs = FileSystem.get(conf);
        Path dstPath = new Path(dst);

        FSDataOutputStream outputStream = fs.create(dstPath);
        outputStream.write(contents);
        outputStream.close();

        fs.close();
        System.out.println("文件创建成功");

    }

    public static void uploadFile(String src, String dst) throws IOException {
        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS", "hdfs://192.168.0.103:9000");

        FileSystem fs = FileSystem.get(configuration);

        Path srcPath = new Path(src);
        Path dstPath = new Path(dst);

        fs.copyFromLocalFile(false, srcPath, dstPath);

        FileStatus[] fileStatus = fs.listStatus(dstPath);

        for(FileStatus file: fileStatus) {
            System.out.println("HDFS中的目标路径为：" + file.getPath());
            System.out.println("upload success");
        }

        fs.close();
    }

    public static void readFromFile(String filePath) throws IOException {
        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS", "hdfs://192.168.0.103:9000");

        FileSystem fs = FileSystem.get(configuration);
        Path srcPath = new Path(filePath);

        InputStream inputStream = null;

        try {
            inputStream = fs.open(srcPath);
            IOUtils.copyBytes(inputStream, System.out, 4096, false);
        } finally {
            IOUtils.closeStream(inputStream);
        }
    }
}
