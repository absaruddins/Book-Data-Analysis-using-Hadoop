-- Top 10 highest-rated records for each publisher
books = LOAD 'hdfs:///BDLFinalProject/data/bookDataset.csv' USING PigStorage(',') AS (isbn:chararray, title:chararray, author:chararray, year:int, publisher:chararray, userid:chararray, rating:float);
filtered = FILTER books BY isbn IS NOT NULL AND isbn != 'ISBN' AND publisher IS NOT NULL AND publisher != '' AND rating IS NOT NULL;
grp = GROUP filtered BY publisher;
top_each = FOREACH grp { ordered = ORDER filtered BY rating DESC; limited = LIMIT ordered 10; GENERATE group AS publisher, FLATTEN(limited); };
STORE top_each INTO 'Top10RatedByPublisher.txt' using PigStorage('|');
