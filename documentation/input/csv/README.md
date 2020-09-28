## Schema
The headers can have optional header containing the following fields
`dateTime,openAsk,openBid,highAsk,highBid,lowAsk,lowBid,closeAsk,closeBid,volume`.

If the header is missing it will assume the order of the fields to be
`dateTime,openAsk,openBid,highAsk,highBid,lowAsk,lowBid,closeAsk,closeBid,volume`.

By having the dateTime as the first field it should be quite easy to sort it in chronological order with
```
sort <file>
```

Test csv has been uploaded to https://drive.google.com/file/d/1AQcNzrVV4dy5RKzUWQ1cIeFUeN9h4b0n/view?usp=sharing
