package cz.timony.marketshare.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Market {

    private final List<Share> shares;
    private final double total;

    public Market(List<Share> shares) {
        if (shares == null) {
            throw new IllegalArgumentException("Shares list cannot be null");
        }

        this.shares = shares.stream()
                .collect(Collectors.groupingBy(Share::getVendor,
                        Collectors.groupingBy(Share::getQuoter, Collectors.summingLong(Share::getUnits))
                ))
                .entrySet().stream()
                .flatMap(vendorEntry -> vendorEntry.getValue()
                        .entrySet().stream()
                        .map(quoterEntry -> new Share(
                                vendorEntry.getKey(),
                                quoterEntry.getKey(),
                                quoterEntry.getValue()
                        )))
                .collect(Collectors.toCollection(ArrayList::new));

        this.total = shares.stream()
                .mapToDouble(Share::getUnits)
                .sum();

        calculatePercentages();
    }

    public Market getSubmarket(Predicate<Share> predicate) {
        return new Market(shares.stream()
                .filter(predicate)
                .toList());
    }

    public Market sorted(Comparator<Share> comparator) {
        shares.sort(comparator);
        return this;
    }

    public Market sortedByVendor() {
        return sorted(Comparator.comparing(Share::getVendor));
    }

    public Market sortedByUnitsDescending() {
        return sorted(Comparator.comparingLong(Share::getUnits).reversed());
    }

    public double getTotal() {
        return total;
    }

    public List<Share> getShares() {
        return shares;
    }

    /**
     * Using Hare-Niemayer algorithm. For the sake of simplicity not fully implemented.
     */
    private void calculatePercentages() {
        double cumulatedTotal = 0.0;

        for (Share share : shares) {
            double sharePercent = total == 0 ? 0.0 : (share.getUnits() * 100.0) / total;
            double roundedPercent = Math.round(sharePercent * 10.0) / 10.0;
            cumulatedTotal += roundedPercent;
            share.setPercentage(roundedPercent);
        }

        if (cumulatedTotal != 100.0) {
            double difference = 100.0 - cumulatedTotal;
            shares.stream()
                    .max(Comparator.comparingDouble(Share::getUnits))
                    .ifPresent(shareToAdjust ->
                        shareToAdjust.setPercentage(shareToAdjust.getPercentage() + difference)
                    );
        }
    }

}
