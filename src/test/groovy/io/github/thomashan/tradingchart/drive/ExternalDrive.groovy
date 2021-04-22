package io.github.thomashan.tradingchart.drive

interface ExternalDrive {
    static ExternalDrive instance = new DropboxDrive()

    InputStream getTestDataInputStream()
}
