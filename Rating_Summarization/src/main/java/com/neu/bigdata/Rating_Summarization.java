package com.neu.bigdata;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Rating_Summarization {
    public static void main(String[] args) throws Exception {
        if(args.length!=2){System.err.println("Usage: Rating_Summarization <input> <output>");System.exit(2);}
        Job job=Job.getInstance(new Configuration(),"Book Rating Summarization");
        job.setJarByClass(Rating_Summarization.class);
        job.setMapperClass(MapperClass.class); job.setCombinerClass(ReducerClass.class); job.setReducerClass(ReducerClass.class);
        job.setOutputKeyClass(Text.class); job.setOutputValueClass(RatingSummaryTuple.class);
        FileInputFormat.addInputPath(job,new Path(args[0])); FileOutputFormat.setOutputPath(job,new Path(args[1]));
        System.exit(job.waitForCompletion(true)?0:1);
    }
}
