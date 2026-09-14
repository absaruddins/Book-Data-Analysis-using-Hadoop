package com.neu.bigdata;

import java.io.IOException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class MapperClass extends Mapper<Object, Text, Text, RatingSummaryTuple> {
    private final Text isbn = new Text();
    private final RatingSummaryTuple tuple = new RatingSummaryTuple();
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        String line=value.toString();
        if(line.trim().isEmpty()) return;
        String[] f=CsvUtils.parseLine(line);
        if(CsvUtils.isHeader(f) || f.length < 7) return;
        try {
            float rating=Float.parseFloat(f[6].trim());
            if(f[0].trim().isEmpty()) return;
            isbn.set(f[0].trim()); tuple.add(rating); context.write(isbn,tuple);
            // Writable instances are reused by Hadoop, so reset for the next record.
            tuple.setFrom(new RatingSummaryTuple());
        } catch(NumberFormatException ignored) {}
    }
}
