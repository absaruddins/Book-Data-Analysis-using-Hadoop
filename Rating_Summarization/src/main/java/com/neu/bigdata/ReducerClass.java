package com.neu.bigdata;

import java.io.IOException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class ReducerClass extends Reducer<Text, RatingSummaryTuple, Text, RatingSummaryTuple> {
    protected void reduce(Text key, Iterable<RatingSummaryTuple> values, Context context) throws IOException, InterruptedException {
        RatingSummaryTuple result=new RatingSummaryTuple();
        for(RatingSummaryTuple v:values) result.merge(v);
        context.write(key,result);
    }
}
