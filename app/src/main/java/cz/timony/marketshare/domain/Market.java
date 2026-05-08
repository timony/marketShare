package cz.timony.marketshare.domain;

import java.util.List;
import java.util.function.Predicate;

public record Market(List<Share> shares) {

    public Market {
        if (shares == null) {
            shares = List.of();
        }

        double total = getTotalUnit();




    }

    public double getTotalUnit() {
        if (shares == null) {
            return 0;
        }
        return shares.stream()
                .mapToDouble(Share::units)
                .sum();
    }

    public Market getSubmarket(Predicate<Share> predicate) {
        List<Share> subShares = shares.stream()
                .filter(predicate)
                .toList();
        return new Market(subShares);
    }
}
