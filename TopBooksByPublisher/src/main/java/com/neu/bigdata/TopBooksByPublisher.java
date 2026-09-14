package com.neu.bigdata;

import java.io.IOException;
import java.util.HashMap;import java.util.Map;
import org.apache.hadoop.conf.Configuration;import org.apache.hadoop.fs.Path;import org.apache.hadoop.io.IntWritable;import org.apache.hadoop.io.Text;import org.apache.hadoop.mapreduce.Job;import org.apache.hadoop.mapreduce.Mapper;import org.apache.hadoop.mapreduce.Reducer;import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/** Finds the top 25 books by number of ratings inside each publisher. */
public class TopBooksByPublisher {
 public static class Map1 extends Mapper<Object,Text,Text,Text>{
  private final Text publisher=new Text(), book=new Text();
  protected void map(Object k,Text v,Context c)throws IOException,InterruptedException{String[] f=CsvUtils.parseLine(v.toString());if(f.length<7||CsvUtils.isHeader(f))return;String p=f[4].trim(),isbn=f[0].trim();if(!p.isEmpty()&&!isbn.isEmpty()){publisher.set(p);book.set(isbn);c.write(publisher,book);}}
 }
 public static class Reduce1 extends Reducer<Text,Text,Text,Text>{
  protected void reduce(Text p,Iterable<Text> books,Context c)throws IOException,InterruptedException{Map<String,Integer> counts=new HashMap<String,Integer>();for(Text b:books){String s=b.toString();Integer n=counts.get(s);counts.put(s,n==null?1:n+1);}for(Map.Entry<String,Integer> e:counts.entrySet())c.write(new Text(e.getKey()),new Text(p.toString()+"\t"+e.getValue()));}
 }
 public static class Map2 extends Mapper<Object,Text,PublisherCountKey,Text>{
  protected void map(Object k,Text v,Context c)throws IOException,InterruptedException{String[] r=v.toString().split("\\t");if(r.length<3)return;try{c.write(new PublisherCountKey(r[1],Integer.parseInt(r[2])),new Text(r[0]));}catch(Exception ignored){}}
 }
 public static class Reduce2 extends Reducer<PublisherCountKey,Text,Text,IntWritable>{
  private String current="";private int n=0;
  protected void reduce(PublisherCountKey k,Iterable<Text> books,Context c)throws IOException,InterruptedException{if(!k.publisher.equals(current)){current=k.publisher;n=0;}for(Text b:books){if(n>=25)return;c.write(new Text(current+"\t"+b.toString()),new IntWritable(k.count));n++;}}
 }
 public static void main(String[] a)throws Exception{if(a.length!=3){System.err.println("Usage: TopBooksByPublisher <input> <intermediate-output> <final-output>");System.exit(2);}Job j1=Job.getInstance(new Configuration(),"Top Books By Publisher - Count");j1.setJarByClass(TopBooksByPublisher.class);j1.setMapperClass(Map1.class);j1.setReducerClass(Reduce1.class);j1.setMapOutputKeyClass(Text.class);j1.setMapOutputValueClass(Text.class);j1.setOutputKeyClass(Text.class);j1.setOutputValueClass(Text.class);FileInputFormat.addInputPath(j1,new Path(a[0]));FileOutputFormat.setOutputPath(j1,new Path(a[1]));if(!j1.waitForCompletion(true))System.exit(1);Job j2=Job.getInstance(new Configuration(),"Top 25 Books By Publisher");j2.setJarByClass(TopBooksByPublisher.class);j2.setMapperClass(Map2.class);j2.setReducerClass(Reduce2.class);j2.setMapOutputKeyClass(PublisherCountKey.class);j2.setMapOutputValueClass(Text.class);j2.setOutputKeyClass(Text.class);j2.setOutputValueClass(IntWritable.class);j2.setNumReduceTasks(1);FileInputFormat.addInputPath(j2,new Path(a[1]));FileOutputFormat.setOutputPath(j2,new Path(a[2]));System.exit(j2.waitForCompletion(true)?0:1);}
}
