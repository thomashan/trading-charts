package io.github.thomashan.tradingchart

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import com.google.api.services.drive.model.FileList
import io.github.thomashan.tradingchart.domain.ohlc.BidAskOhlc
import io.github.thomashan.tradingchart.input.csv.CsvParser
import io.github.thomashan.tradingchart.input.csv.stream.CsvParserStreamSplitImpl

import java.security.GeneralSecurityException
import java.util.stream.Collectors
import java.util.stream.Stream
import java.util.zip.GZIPInputStream

import static com.google.api.services.drive.DriveScopes.DRIVE_METADATA_READONLY
import static com.google.api.services.drive.DriveScopes.DRIVE_READONLY

class GoogleDrive {
    private static final String APPLICATION_NAME = "Trading chart"
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance()
    private static final String TOKENS_DIRECTORY_PATH = "tokens"
    private static final List<String> SCOPES = Arrays.asList(DRIVE_METADATA_READONLY, DRIVE_READONLY)
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json"


    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        try (InputStream inputStream = GoogleDrive.class.getResourceAsStream(CREDENTIALS_FILE_PATH)) {
            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(inputStream))

            // Build flow and trigger user authorization request.
            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                    HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                    .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                    .setAccessType("offline")
                    .build()
            LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build()
            Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user")
            return credential
        }
    }

    static void main(String... args) throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport()
        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build()

        // Print the names and IDs for up to 10 files.
        FileList result = service.files().list()
                .setPageSize(10)
                .setFields("nextPageToken, files(id, name)")
                .execute()

        println(service.files().get("1AQcNzrVV4dy5RKzUWQ1cIeFUeN9h4b0n")
            .execute()
            .getPermissions())

        try(GZIPInputStream inputStream = service.files().get("1AQcNzrVV4dy5RKzUWQ1cIeFUeN9h4b0n").executeMediaAsInputStream()) {
            CsvParser<BidAskOhlc> csvParser = new CsvParserStreamSplitImpl<>()

            Stream<BidAskOhlc> stream = csvParser.parse(inputStream)
            List<BidAskOhlc> ohlcs = stream.collect(Collectors.toList())

            print(ohlcs)
        }

        List<File> files = result.getFiles()
        if (files == null || files.isEmpty()) {
            System.out.println("No files found.")
        } else {
            System.out.println("Files:")
            for (File file : files) {
                System.out.printf("%s (%s)\n", file.getName(), file.getId())
            }
        }
    }
}
