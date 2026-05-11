package cz.timony.marketshare.output;

import cz.timony.marketshare.domain.Market;

/**
 * MarkerExporter is an interface for exporting market data into various formats.
 * Implementations of this interface define the logic for exporting the provided market data.
 */
public interface MarkerExporter {

    /**
     * Export market to a string
     * @param market market to export
     * @param title title of the exported file
     * @return string representation of the market
     */
    String export(Market market, String title);
}
