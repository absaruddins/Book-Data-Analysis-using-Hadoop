-- Top 10 books with the highest number of ratings/reviews
books = LOAD 'hdfs:///BDLFinalProject/data/bookDataset.csv' USING PigStorage(',') AS (isbn:chararray, title:chararray, author:chararray, year:int, publisher:chararray, userid:chararray, rating:float);
filtered = FILTER books BY isbn IS NOT NULL AND isbn != 'ISBN';
grp = GROUP filtered BY isbn;
summary = FOREACH grp {
    one = LIMIT filtered 1;
    GENERATE group AS isbn, FLATTEN(one.title) AS title, COUNT(filtered) AS review_count;
};
sorted = ORDER summary BY review_count DESC;
top10 = LIMIT sorted 10;
STORE top10 INTO 'Top10MostReviewed.txt' using PigStorage('|');
