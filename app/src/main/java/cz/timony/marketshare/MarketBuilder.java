package cz.timony.marketshare;

import cz.timony.marketshare.domain.Market;
import cz.timony.marketshare.domain.Share;
import cz.timony.marketshare.input.InputRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MarketBuilder {

    public Market build(List<InputRecord> inputRecords) {
        List<Share> shares = new ArrayList<>();
        if (inputRecords != null) {
            shares = inputRecords.stream()
                    .map(ir -> new Share(
                            ir.vendor(),
                            ir.timescale(),
                            ir.units()
                    ))
                    .collect(Collectors.toCollection(ArrayList::new));
        }
        return new Market(shares);
    }
}
