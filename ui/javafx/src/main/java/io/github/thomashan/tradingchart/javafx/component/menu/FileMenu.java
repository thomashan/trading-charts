package io.github.thomashan.tradingchart.javafx.component.menu;

import io.github.thomashan.tradingchart.domain.ohlc.BidAskOhlc;
import io.github.thomashan.tradingchart.input.csv.CsvParser;
import io.github.thomashan.tradingchart.input.csv.charbuffer.CsvParserCharBuffer;
import io.github.thomashan.tradingchart.persistence.chronicle.BidAskOhlcWriterChronicle;
import io.github.thomashan.tradingchart.persistence.chronicle.BytesOutChronicle;
import io.github.thomashan.tradingchart.persistence.domain.ohlc.BidAskOhlcWriter;
import io.github.thomashan.tradingchart.util.concurrent.NamedThreadFactory;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class FileMenu extends Menu {
    private static final ExecutorService CHRONICLE_WRITER_EXECUTOR = Executors.newSingleThreadExecutor(new NamedThreadFactory("chronicle-writer"));
    private static final String NAME = "File";
    private static final FileChooser OPEN_FILE_CHOOSER = new FileChooser();
    private static final CsvParser<BidAskOhlc> BID_ASK_CSV_PARSER = new CsvParserCharBuffer<>();
    private static final BidAskOhlcWriter<BytesOutChronicle> BID_ASK_OHLC_WRITER = new BidAskOhlcWriterChronicle("/tmp/trading_chart/chronicle/bid_ask_ohlc");
    private static final Consumer<BidAskOhlc> BID_ASK_OHLC_CONSUMER = bidAskOhlc -> {
        System.out.println(bidAskOhlc);
        BID_ASK_OHLC_WRITER.write(bidAskOhlc);
    };

    private static final EventHandler<ActionEvent> OPEN_FILE_EVENT_HANDLER = event -> {
        File fileToLoad = OPEN_FILE_CHOOSER.showOpenDialog(null);
        if (null != fileToLoad) {
            CHRONICLE_WRITER_EXECUTOR.execute(() -> {
                // FIXME: is there a way to reuse the input stream from different files?
                // FIXME: we need to change the csv parser to handle both mid and bidAsk at the same time
                // FIXME: get rid of creating a new runnable every time we parse the CSV
                try (InputStream inputStream = new FileInputStream(fileToLoad)) {
                    BID_ASK_CSV_PARSER.parse(inputStream, BID_ASK_OHLC_CONSUMER);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
        }
    };

    static {
        initialiseOpenFileChooser();
    }

    public FileMenu() {
        super(NAME);
        MenuItem open = new MenuItem("Open...");
        open.setOnAction(OPEN_FILE_EVENT_HANDLER);
        getItems().add(open);
    }

    private static void initialiseOpenFileChooser() {
        OPEN_FILE_CHOOSER.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV", "*.csv"),
                new FileChooser.ExtensionFilter("Zipped CSV", "*.zip")
        );
    }
}
