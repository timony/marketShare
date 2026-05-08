package cz.timony.marketshare.input;

import cz.timony.marketshare.domain.Market;

import java.io.IOException;

public interface MarketReader {

    Market read() throws IOException;
}
