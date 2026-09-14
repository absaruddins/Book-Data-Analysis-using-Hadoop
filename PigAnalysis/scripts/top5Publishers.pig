-- Top 5 publishers by number of distinct rated books
books = LOAD 'hdfs:///BDLFinalProject/data/bookDataset.csv' USING PigStorage(',') AS (isbn:chararray, title:chararray, author:chararray, year:int, publisher:chararray, userid:chararray, rating:float);
filtered = FILTER books BY isbn IS NOT NULL AND isbn != 'ISBN' AND publisher IS NOT NULL AND publisher != '';
grp = GROUP filtered BY publisher;
counts = FOREACH grp { distinct_books = DISTINCT filtered.isbn; GENERATE group AS publisher, COUNT(distinct_books) AS book_count; };
sorted = ORDER counts BY book_count DESC;
top5 = LIMIT sorted 5;
STORE top5 INTO 'Top5Publishers.txt' using PigStorage('|');
