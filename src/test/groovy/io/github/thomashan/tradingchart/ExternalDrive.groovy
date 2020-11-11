package io.github.thomashan.tradingchart

import com.dropbox.core.DbxDownloader
import com.dropbox.core.DbxException
import com.dropbox.core.DbxRequestConfig
import com.dropbox.core.v2.DbxClientV2
import com.dropbox.core.v2.files.FileMetadata
import com.dropbox.core.v2.files.ListFolderResult
import com.dropbox.core.v2.files.Metadata
import io.github.thomashan.tradingchart.domain.ohlc.BidAskOhlc
import io.github.thomashan.tradingchart.input.csv.CsvParser
import io.github.thomashan.tradingchart.input.csv.stream.CsvParserStreamSplitImpl

import java.util.stream.Collectors
import java.util.stream.Stream
import java.util.zip.GZIPInputStream

class ExternalDrive {
    private static final String ACCESS_TOKEN = "eDxNr1rlUesAAAAAAAAAAZbIvjhvkS-D7v1CZuyYJBVN_Z3sIS-0IxdewBXfMgz8"

    static void main(String[] args) throws DbxException {
        // Create Dropbox client
        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build()
        DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN)

        // Get files and folder metadata from Dropbox root directory
        ListFolderResult result = client.files().listFolder("/test_data")

//        DbxDownloader<FileMetadata> file = client.files().download("/test_data/EURUSD-S5-0.0.1.csv.gz")
        DbxDownloader<FileMetadata> file = client.files().download("/test_data/EURUSD-S5.csv.gz")
        try (InputStream inputStream = file.inputStream) {
            GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream)

            CsvParser<BidAskOhlc> csvParser = new CsvParserStreamSplitImpl<>()

            Stream<BidAskOhlc> stream = csvParser.parse(gzipInputStream)
            List<BidAskOhlc> ohlcs = stream.collect(Collectors.toList())

            println(ohlcs[0])
        }

        while (true) {
            for (Metadata metadata : result.getEntries()) {
                System.out.println(metadata.getPathLower())
            }

            if (!result.getHasMore()) {
                break
            }

            result = client.files().listFolderContinue(result.getCursor())
        }
    }
}
