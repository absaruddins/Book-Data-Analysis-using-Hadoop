-- Top 10 individual ratings in the merged dataset
books = LOAD 'hdfs:///BDLFinalProject/data/bookDataset.csv' USING PigStorage(',') AS (isbn:chararray, title:chararray, author:chararray, year:int, publisher:chararray, userid:chararray, rating:float);
filtered = FILTER books BY isbn IS NOT NULL AND isbn != 'ISBN' AND rating IS NOT NULL;
sorted = ORDER filtered BY rating DESC;
top10 = LIMIT sorted 10;
STORE top10 INTO 'Top10Rated.txt' using PigStorage('|');
