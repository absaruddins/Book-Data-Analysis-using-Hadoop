package com.neu.bigdata;
import java.io.DataInput;import java.io.DataOutput;import java.io.IOException;
import org.apache.hadoop.io.WritableComparable;
public class PublisherCountKey implements WritableComparable<PublisherCountKey>{
 public String publisher=""; public int count; public PublisherCountKey(){} public PublisherCountKey(String p,int c){publisher=p;count=c;}
 public void write(DataOutput o)throws IOException{o.writeUTF(publisher);o.writeInt(count);} public void readFields(DataInput i)throws IOException{publisher=i.readUTF();count=i.readInt();}
 public int compareTo(PublisherCountKey x){int c=publisher.compareTo(x.publisher);return c!=0?c:-Integer.compare(count,x.count);}
 public int hashCode(){return publisher.hashCode();} public boolean equals(Object o){return o instanceof PublisherCountKey&&compareTo((PublisherCountKey)o)==0;}
 public String toString(){return publisher+"\t"+count;}
}
