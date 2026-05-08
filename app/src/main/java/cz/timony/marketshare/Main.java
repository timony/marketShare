package cz.timony.marketshare;

import cz.timony.marketshare.domain.Market;
import cz.timony.marketshare.input.CSVReader;
import cz.timony.marketshare.input.MarketReader;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class Main {

    private static final Logger log = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws IOException {


        MarketReader reader = new CSVReader(new File(args[0]));
        Market market = reader.read();


        log.info("Total units: " + market.getTotalUnit());


        Market market2010q3 = market.getSubmarket(s -> s.quoter().equals("2010 Q3"));

        log.info("Shares by quoter: {}" + market2010q3);
        log.info("2010 Q3 total units: " + market2010q3.getTotalUnit());

        String a = "adsf";

    }
}
