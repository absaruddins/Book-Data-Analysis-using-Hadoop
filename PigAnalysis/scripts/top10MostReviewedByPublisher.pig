-- Top 10 most-reviewed books inside each publisher
books = LOAD 'hdfs:///BDLFinalProject/data/bookDataset.csv' USING PigStorage(',') AS (isbn:chararray, title:chararray, author:chararray, year:int, publisher:chararray, userid:chararray, rating:float);
filtered = FILTER books BY isbn IS NOT NULL AND isbn != 'ISBN' AND publisher IS NOT NULL AND publisher != '';
bookgrp = GROUP filtered BY (publisher,isbn);
booksummary = FOREACH bookgrp {
    one = LIMIT filtered 1;
    GENERATE group.publisher AS publisher, group.isbn AS isbn, FLATTEN(one.title) AS title, COUNT(filtered) AS review_count;
};
pubgrp = GROUP booksummary BY publisher;
top_each = FOREACH pubgrp {
    ordered = ORDER booksummary BY review_count DESC;
    limited = LIMIT ordered 10;
    GENERATE group AS publisher, FLATTEN(limited);
};
STORE top_each INTO 'Top10MostReviewedByPublisher.txt' using PigStorage('|');
