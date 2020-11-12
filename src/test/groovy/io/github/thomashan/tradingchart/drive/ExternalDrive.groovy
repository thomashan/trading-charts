package io.github.thomashan.tradingchart.drive

interface ExternalDrive {
    static ExternalDrive instance = new DropboxDrive()

    void download(String path, OutputStream outputStream)

    InputStream download(String path)
}
