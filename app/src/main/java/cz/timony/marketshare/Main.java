package cz.timony.marketshare;

import cz.timony.marketshare.domain.Market;
import cz.timony.marketshare.domain.Share;
import cz.timony.marketshare.input.InputRecord;
import cz.timony.marketshare.input.InputRecordReader;
import cz.timony.marketshare.input.InputRecordReaderProvider;
import cz.timony.marketshare.output.HtmlMarketExporter;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final Logger log = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws IOException {
        File inputFile = new File(args[0]);
        if (!inputFile.exists()) {
            throw new IllegalArgumentException("Input file does not exist");
        }
        InputRecordReader inputRecordReader = InputRecordReaderProvider.provide(inputFile);
        List<InputRecord> inputRecords = inputRecordReader.read();

        Market market = new MarketBuilder().build(inputRecords);

        HtmlMarketExporter exporter = new HtmlMarketExporter();

        //ALL
        String marketShare = exporter.export(market, "Market Share");
        log.log(Level.INFO, marketShare);

        //All sorted by vendor
        String marketShareSortedByVendor = exporter.export(market.sortedByVendor(), "Market Share sorted by vendor");
        log.log(Level.INFO, marketShareSortedByVendor);

        //All sorted by units descending
        String marketShareSortedByUnits = exporter.export(market.sortedByUnitsDescending(), "Market Share sorted by units descending");
        log.log(Level.INFO, marketShareSortedByUnits);

        //2010 Q3
        Market subMarket2010Q3 = market.getSubmarket(share -> share.getQuoter().equals("2010 Q3"));
        String marketShare2010Q3 = exporter.export(subMarket2010Q3, "Market Share 2010 Q3");
        log.log(Level.INFO, marketShare2010Q3);
        log.log(Level.INFO, "Row number of Lenovo: {0}", subMarket2010Q3.getRowNumberOfVendor("Lenovo").orElse(-1));
        log.log(Level.INFO, "Row number of Acer: {0}", subMarket2010Q3.getRowNumberOfVendor("Acer").orElse(-1));

        //2010 Q4 sorted by units asc
        Market subMarket2010Q4 = market.getSubmarket(share -> share.getQuoter().equals("2010 Q4"))
                .sorted(Comparator.comparingLong(Share::getUnits));
        String marketShare2010Q4 = exporter.export(subMarket2010Q4, "Market Share 2010 Q4 sorted by units ASC");
        log.log(Level.INFO, marketShare2010Q4);
    }
}
