package com.neu.bigdata;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/** Two-stage MapReduce: summarize ratings per ISBN, then globally rank by rating count. */
public class TopBooks {
    public static class Map1 extends Mapper<Object,Text,Text,IntWritable>{
        private final Text isbn=new Text(); private final IntWritable one=new IntWritable(1);
        protected void map(Object key,Text value,Context c)throws IOException,InterruptedException{
            String[] f=CsvUtils.parseLine(value.toString()); if(f.length<7||CsvUtils.isHeader(f))return;
            if(!f[0].trim().isEmpty()){isbn.set(f[0].trim()); c.write(isbn,one);}
        }
    }
    public static class Reduce1 extends Reducer<Text,IntWritable,Text,IntWritable>{
        private final IntWritable out=new IntWritable();
        protected void reduce(Text k,Iterable<IntWritable> vals,Context c)throws IOException,InterruptedException{int n=0;for(IntWritable v:vals)n+=v.get();out.set(n);c.write(k,out);}
    }
    public static class Map2 extends Mapper<Object,Text,IntWritable,Text>{
        protected void map(Object key,Text value,Context c)throws IOException,InterruptedException{
            String[] r=value.toString().split("\\t"); if(r.length<2)return;
            try{c.write(new IntWritable(Integer.parseInt(r[1].trim())),new Text(r[0].trim()));}catch(Exception ignored){}
        }
    }
    public static class Reduce2 extends Reducer<IntWritable,Text,Text,IntWritable>{
        private int remaining=50;
        protected void reduce(IntWritable count,Iterable<Text> books,Context c)throws IOException,InterruptedException{
            for(Text book:books){if(remaining<=0)return;c.write(book,count);remaining--;}
        }
    }
    public static void main(String[] args)throws Exception{
        if(args.length!=3){System.err.println("Usage: TopBooks <input> <intermediate-output> <final-output>");System.exit(2);}
        Job j1=Job.getInstance(new Configuration(),"Top Books - Count Ratings"); j1.setJarByClass(TopBooks.class);
        j1.setMapperClass(Map1.class);j1.setReducerClass(Reduce1.class);j1.setMapOutputKeyClass(Text.class);j1.setMapOutputValueClass(IntWritable.class);j1.setOutputKeyClass(Text.class);j1.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(j1,new Path(args[0]));FileOutputFormat.setOutputPath(j1,new Path(args[1]));
        if(!j1.waitForCompletion(true))System.exit(1);
        Job j2=Job.getInstance(new Configuration(),"Top 50 Most Rated Books"); j2.setJarByClass(TopBooks.class);
        j2.setMapperClass(Map2.class);j2.setReducerClass(Reduce2.class);j2.setMapOutputKeyClass(IntWritable.class);j2.setMapOutputValueClass(Text.class);j2.setOutputKeyClass(Text.class);j2.setOutputValueClass(IntWritable.class);j2.setSortComparatorClass(SortKeyComparator.class);j2.setNumReduceTasks(1);
        FileInputFormat.addInputPath(j2,new Path(args[1]));FileOutputFormat.setOutputPath(j2,new Path(args[2]));
        System.exit(j2.waitForCompletion(true)?0:1);
    }
}
