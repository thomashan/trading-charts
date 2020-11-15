package io.github.thomashan.tradingchart.drive

import com.dropbox.core.DbxRequestConfig
import com.dropbox.core.v2.DbxClientV2

import java.util.zip.GZIPInputStream

class DropboxDrive implements ExternalDrive {
    private static final String DROPBOX_ACCESS_TOKEN = "DROPBOX_ACCESS_TOKEN"
    private static final String CLIENT_ID = "tradingChart"
    private final DbxClientV2 client
    private static File testData

    DropboxDrive() {
        String accessToken = System.getenv(DROPBOX_ACCESS_TOKEN)
        if (!accessToken) {
            throw new RuntimeException("please specify $DROPBOX_ACCESS_TOKEN environment variable")
        }

        DbxRequestConfig config = DbxRequestConfig.newBuilder(CLIENT_ID).build()
        this.client = new DbxClientV2(config, accessToken)
    }

    @Override
    InputStream download(String path) {
        return client.files().download(path).inputStream
    }

    @Override
    void download(String path, OutputStream outputStream) {
        client.files().download(path).download(outputStream)
    }

    @Override
    InputStream getTestDataInputStream() {
        if (!testData) {
            this.testData = File.createTempFile("csv_parser_jmh", "")
            FileOutputStream outputStream = new FileOutputStream(testData)
            ExternalDrive.instance.download("/test_data/EURUSD-S5-0.0.1.csv.gz", outputStream)
            testData.deleteOnExit()
        }

        return new GZIPInputStream(new FileInputStream(testData));
    }
}
