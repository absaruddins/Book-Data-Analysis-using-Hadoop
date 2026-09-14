package mergedataset;

import java.io.*;
import java.util.*;

/** Merges Goodreads Books.csv with Ratings.csv using ISBN and writes the classroom-friendly bookDataset.csv. */
public class MergeCSV {
    static String[] parse(String line){List<String> f=new ArrayList<String>();StringBuilder s=new StringBuilder();boolean q=false;for(int i=0;i<line.length();i++){char c=line.charAt(i);if(c=='"'){if(q&&i+1<line.length()&&line.charAt(i+1)=='"'){s.append('"');i++;}else q=!q;}else if(c==','&&!q){f.add(s.toString());s.setLength(0);}else s.append(c);}f.add(s.toString());return f.toArray(new String[f.size()]);}
    static String clean(String s){return s.replace('\t',' ').replace('\r',' ').replace('\n',' ').replace(',',' ').trim();}
    public static void main(String[] args)throws Exception{
        if(args.length!=3){System.err.println("Usage: MergeCSV <Books.csv> <Ratings.csv> <bookDataset.csv>");System.exit(2);}
        Map<String,String[]> books=new HashMap<String,String[]>();
        BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(args[0]),"UTF-8"));
        String line; boolean first=true; while((line=br.readLine())!=null){if(first){first=false;continue;}String[] f=parse(line);if(f.length>=8){String isbn=f[0].trim();if(!isbn.isEmpty())books.put(isbn,f);}} br.close();
        BufferedReader rr=new BufferedReader(new InputStreamReader(new FileInputStream(args[1]),"UTF-8"));
        BufferedWriter out=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[2]),"UTF-8"));
        out.write("ISBN,BookTitle,Author,Year,Publisher,UserID,Rating\n"); first=true; long rows=0;
        while((line=rr.readLine())!=null){if(first){first=false;continue;}String[] r=parse(line);if(r.length<3)continue;String[] b=books.get(r[1].trim());if(b==null)continue;
            try{Float.parseFloat(r[2].trim());}catch(Exception e){continue;}
            out.write(clean(b[0])+","+clean(b[1])+","+clean(b[2])+","+clean(b[3])+","+clean(b[4])+","+clean(r[0])+","+clean(r[2])+"\n");rows++;}
        rr.close();out.close();System.out.println("Merged rows: "+rows);
    }
}
