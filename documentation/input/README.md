# Input
The input was designed with the following principals in mind
* fast!
* wide range of formats
* minimise redundancy

CSV and JSON was chosen as the first formats to implement as these are widely used.


## Minimum fields
There are two modes. Candlesticks with only mid-prices or bid-ask prices.
The plot for the two differ.

The dateTime format should be in rfc3339 format with times in UTC (e.g. 2020-09-25T20:59:50Z)  
https://en.wikipedia.org/wiki/ISO_8601.
Extra information such as provider (source), instrument and time-frame has to be chosen beforehand.
Once the provider (source), instrument, and the time-frame is chosen it is assumed it will be applied to the whole dataset.

### Prices with mid-points
The formats will contain the following fields
1. dateTime (string)
2. open (double)
3. high (double)
4. low (double)
5. close (double)
6. volume (double)

### Prices with bid/ask
The formats will contain the following fields
1. dateTime (string)
2. openAsk (double)
3. openBid (double)
4. highAsk (double)
5. highBid (double)
6. lowAsk (double)
7. lowBid (double)
8. closeAsk (double)
9. closeBid (double)
10. volume (double)
