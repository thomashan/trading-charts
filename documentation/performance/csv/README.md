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

```
./gradlew -Dorg.gradle.parallel=false clean jmh
```

Using disk
```
Benchmark                                                                           Mode  Cnt     Score     Error  Units
i.g.t.t.i.c.stream.CsvParserStreamIndexOfImplJmhTest.testParse_InputStream            ss    5   423.706 ±  27.600  ms/op
i.g.t.t.i.c.stream.CsvParserStreamSplitImplJmhTest.testParse_InputStream              ss    5   422.415 ±  34.528  ms/op
i.g.t.t.i.c.stream.CsvParserStreamStringTokeniserImplJmhTest.testParse_InputStream    ss    5   422.590 ±  31.372  ms/op
i.g.t.t.i.c.univocity.CsvParserUnivocityJmhTest.testParse_InputStream                 ss    5  1629.348 ± 123.398  ms/op
```

Using tmpfs backed by memory
```
Benchmark                                                                           Mode  Cnt     Score    Error  Units
i.g.t.t.i.c.stream.CsvParserStreamIndexOfImplJmhTest.testParse_InputStream            ss    5    53.208 ±  9.521  ms/op
i.g.t.t.i.c.stream.CsvParserStreamSplitImplJmhTest.testParse_InputStream              ss    5    51.275 ±  8.600  ms/op
i.g.t.t.i.c.stream.CsvParserStreamStringTokeniserImplJmhTest.testParse_InputStream    ss    5    53.281 ± 16.278  ms/op
i.g.t.t.i.c.univocity.CsvParserUnivocityJmhTest.testParse_InputStream                 ss    5  1348.883 ± 99.840  ms/op
```
