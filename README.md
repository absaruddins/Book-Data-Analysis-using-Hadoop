# Book-Data-Analysis-using-Hadoop
# Goodreads Book Dataset Analysis - MapReduce and Pig

University Big Data Analytics project inspired by the provided YouTube Hadoop project structure.

## Technologies
- Apache Hadoop 3.2.4 / HDFS
- Java 8
- Maven
- MapReduce
- Apache Pig

## Dataset
Dataset:https://www.kaggle.com/datasets/arashnic/book-recommendation-dataset
Goodreads Book Recommendation Dataset.
Input files: `Books.csv`, `Ratings.csv`, `Users.csv`.

The project uses `Books.csv` + `Ratings.csv` joined on `ISBN` to create `data/bookDataset.csv` with:
`ISBN,BookTitle,Author,Year,Publisher,UserID,Rating`

## MapReduce programs
1. **Rating_Summarization** - minimum, maximum, total and average rating plus rating count per ISBN.
2. **TopBooks** - top 50 books by number of ratings using two chained MapReduce jobs.
3. **TopBooksByPublisher** - top 25 books by rating count inside each publisher using two chained MapReduce jobs.

## Pig programs
1. `top5Publishers.pig` - top 5 publishers by distinct rated books.
2. `top10Rated.pig` - top 10 highest-rated records.
3. `top10RatedByPublisher.pig` - top 10 rating records per publisher.
4. `top10MostReviewed.pig` - top 10 books by number of ratings.
5. `top10MostReviewedByPublisher.pig` - top 10 most-reviewed books per publisher.

## Project structure
```text
BDLFinalProject
├── rawdata
│   ├── Books.csv
│   ├── Ratings.csv
│   └── Users.csv
├── data
│   └── bookDataset.csv
├── mergedataset
│   ├── src
│   ├── target
│   └── pom.xml
├── Rating_Summarization
│   ├── src
│   ├── target
│   └── pom.xml
├── TopBooks
│   ├── src
│   ├── target
│   └── pom.xml
├── TopBooksByPublisher
│   ├── src
│   ├── target
│   └── pom.xml
├── PigAnalysis
│   └── scripts
│       ├── top5Publishers.pig
│       ├── top10Rated.pig
│       ├── top10RatedByPublisher.pig
│       ├── top10MostReviewed.pig
│       └── top10MostReviewedByPublisher.pig
└── README.md
```

## Build
From each Maven module folder:
```bat
mvn clean package
```
The compiled JAR will appear in that module's `target` folder.

## Merge data
Example:
```bat
cd C:\BDLFinalProject\mergedataset
mvn clean package
java -jar target\mergedataset-0.0.1-SNAPSHOT.jar ..\rawdata\Books.csv ..\rawdata\Ratings.csv ..\data\bookDataset.csv
```

## Upload merged data to HDFS
```bat
hdfs dfs -mkdir -p /BDLFinalProject/data
hdfs dfs -put -f C:\BDLFinalProject\data\bookDataset.csv /BDLFinalProject/data/
```

## Run MapReduce
```bat
hdfs dfs -rm -r -f /BDLFinalProject/output/rating_summary
java -jar Rating_Summarization\target\Rating_Summarization-0.0.1-SNAPSHOT.jar /BDLFinalProject/data/bookDataset.csv /BDLFinalProject/output/rating_summary

hdfs dfs -rm -r -f /BDLFinalProject/output/topbooks_stage1 /BDLFinalProject/output/topbooks
java -jar TopBooks\target\TopBooks-0.0.1-SNAPSHOT.jar /BDLFinalProject/data/bookDataset.csv /BDLFinalProject/output/topbooks_stage1 /BDLFinalProject/output/topbooks

hdfs dfs -rm -r -f /BDLFinalProject/output/topbooks_pub_stage1 /BDLFinalProject/output/topbooks_pub
java -jar TopBooksByPublisher\target\TopBooksByPublisher-0.0.1-SNAPSHOT.jar /BDLFinalProject/data/bookDataset.csv /BDLFinalProject/output/topbooks_pub_stage1 /BDLFinalProject/output/topbooks_pub
```

## Run Pig
Start Pig in MapReduce mode and execute any script from `PigAnalysis/scripts`.

> Note: generated `target/classes` and JAR files are normally build artifacts. They are included in this project skeleton because the reference YouTube project contained `target` directories. Run Maven locally to regenerate them.
