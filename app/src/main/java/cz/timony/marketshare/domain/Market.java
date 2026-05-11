package cz.timony.marketshare.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Market {

    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);
    private static final int PERCENT_SCALE = 1;

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
        BigDecimal cumulatedTotal = BigDecimal.ZERO;
        BigDecimal totalAsBigDecimal = BigDecimal.valueOf(total);

        for (Share share : shares) {
            BigDecimal roundedPercent = total == 0
                    ? BigDecimal.ZERO.setScale(PERCENT_SCALE, RoundingMode.HALF_UP)
                    : BigDecimal.valueOf(share.getUnits())
                            .multiply(HUNDRED)
                            .divide(totalAsBigDecimal, 10, RoundingMode.HALF_UP)
                            .setScale(PERCENT_SCALE, RoundingMode.HALF_UP);

            cumulatedTotal = cumulatedTotal.add(roundedPercent);
            share.setPercentage(roundedPercent);
        }

        if (cumulatedTotal.compareTo(HUNDRED) != 0) {
            BigDecimal difference = HUNDRED.subtract(cumulatedTotal);
            shares.stream()
                    .max(Comparator.comparingLong(Share::getUnits))
                    .ifPresent(shareToAdjust ->
                        shareToAdjust.setPercentage(
                                shareToAdjust.getPercentage().add(difference).setScale(PERCENT_SCALE, RoundingMode.HALF_UP)
                        )
                    );
        }
    }

}
