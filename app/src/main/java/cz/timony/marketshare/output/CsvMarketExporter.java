package cz.timony.marketshare.output;

import cz.timony.marketshare.domain.Market;

public class CsvMarketExporter implements MarkerExporter {

    @Override
    public String export(Market market, String title) {
        throw new UnsupportedOperationException("CSV export is not implemented yet");
    }
}
