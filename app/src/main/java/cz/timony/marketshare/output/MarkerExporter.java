package cz.timony.marketshare.output;

import cz.timony.marketshare.domain.Market;

public interface MarkerExporter {

    String export(Market marker, String title);
}
