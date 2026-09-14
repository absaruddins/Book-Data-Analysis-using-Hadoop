package com.neu.bigdata;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;
public class SortKeyComparator extends WritableComparator {
    protected SortKeyComparator(){super(IntWritable.class,true);}
    public int compare(WritableComparable a, WritableComparable b){return -Integer.compare(((IntWritable)a).get(),((IntWritable)b).get());}
}
