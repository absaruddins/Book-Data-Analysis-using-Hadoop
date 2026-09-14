package com.neu.bigdata;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.hadoop.io.Writable;

public class RatingSummaryTuple implements Writable {
    private float minRating = Float.MAX_VALUE;
    private float maxRating = Float.MIN_VALUE;
    private float totalRating = 0f;
    private long ratingCount = 0L;

    public RatingSummaryTuple() {}
    public RatingSummaryTuple(float rating) { add(rating); }
    public void add(float rating) {
        if (rating < minRating) minRating = rating;
        if (rating > maxRating) maxRating = rating;
        totalRating += rating;
        ratingCount++;
    }
    public void merge(RatingSummaryTuple other) {
        if (other.ratingCount == 0) return;
        if (ratingCount == 0 || other.minRating < minRating) minRating = other.minRating;
        if (ratingCount == 0 || other.maxRating > maxRating) maxRating = other.maxRating;
        totalRating += other.totalRating;
        ratingCount += other.ratingCount;
    }
    public void setFrom(RatingSummaryTuple other){ minRating=other.minRating; maxRating=other.maxRating; totalRating=other.totalRating; ratingCount=other.ratingCount; }
    public float getMinRating(){return minRating;}
    public float getMaxRating(){return maxRating;}
    public float getTotalRating(){return totalRating;}
    public long getRatingCount(){return ratingCount;}
    public void write(DataOutput out) throws IOException {
        out.writeFloat(minRating); out.writeFloat(maxRating); out.writeFloat(totalRating); out.writeLong(ratingCount);
    }
    public void readFields(DataInput in) throws IOException {
        minRating=in.readFloat(); maxRating=in.readFloat(); totalRating=in.readFloat(); ratingCount=in.readLong();
    }
    public String toString(){
        float avg = ratingCount == 0 ? 0f : totalRating / ratingCount;
        return minRating + "\t" + maxRating + "\t" + totalRating + "\t" + ratingCount + "\t" + avg;
    }
}
