# CSV
We check the performance to read 3,000,000 csv rows (excluding headers).

For the test data with 5S time-frame that equates to around 9 months of worth of data.
```
number of seconds = 3,000,000 * 5 = 15,000,000
number of minutes = 15,000,000 / 60 = 250,000
number of hours = 250,000 / 60 = 4,166.66
number of days = 4,166.66 / 24 = 173.61
number of months = 173.61 / 20 = 8.6805
```

## Performance results

Benchmark                                                              Mode  Cnt     Score     Error  Units
i.g.t.t.i.c.stream.CsvParserStreamImplJmhTest.testParse_InputStream      ss    5   431.807 ±  57.530  ms/op
i.g.t.t.i.c.univocity.CsvParserUnivocityJmhTest.testParse_InputStream    ss    5  1745.991 ± 180.569  ms/op
