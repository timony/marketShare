package cz.timony.marketshare.domain;

import org.jspecify.annotations.NonNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Market {

    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);
    private static final int PERCENT_SCALE = 1;

    private final List<Share> shares;
    private final long total;

    public Market(List<Share> shares) {
        if (shares == null) {
            throw new IllegalArgumentException("Shares list cannot be null");
        }

        this.total = shares.stream()
                .mapToLong(Share::getUnits)
                .sum();

        this.shares = getMergedShares(shares);
        calculatePercentages();
    }

    /**
     * Returns a new Market instance with shares filtered by the given predicate.
     * @param predicate the predicate to filter shares
     * @return a new Market instance with filtered shares
     */
    public Market getSubmarket(Predicate<Share> predicate) {
        return new Market(shares.stream()
                .filter(predicate)
                .toList());
    }

    /**
     * Returns the share with the given vendor and quoter if exists
     * @param vendor the name of the vendor
     * @param quoter the quoter
     * @return Optional of the share
     */
    public Optional<Share> findShare(String vendor, String quoter) {
        return shares.stream()
                .filter(share -> share.getVendor().equals(vendor) && share.getQuoter().equals(quoter))
                .findFirst();
    }

    /**
     * Returns a new Market instance with shares sorted by the given comparator.
     * @param comparator the comparator to sort shares
     * @return a new Market instance with sorted shares
     */
    public Market sorted(Comparator<Share> comparator) {
        shares.sort(comparator);
        return this;
    }

    /**
     * Returns a new Market instance with shares sorted by vendor.
     * @return a new Market instance with sorted shares
     */
    public Market sortedByVendor() {
        return sorted(Comparator.comparing(Share::getVendor));
    }

    /**
     * Returns a new Market instance with shares sorted by units descending.
     * @return a new Market instance with sorted shares
     */
    public Market sortedByUnitsDescending() {
        return sorted(Comparator.comparingLong(Share::getUnits).reversed());
    }

    /**
     * Returns the sum of total units of all shares.
     * @return the total units
     */
    public double getTotal() {
        return total;
    }

    /**
     * Returns the list of shares.
     * @return the list of shares
     */
    public List<Share> getShares() {
        return shares;
    }

    /**
     * Returns row number of the first row of the given vendor in the market.
     * @param vendor the name of the vendor
     * @return row number of the first row of the given vendor in the market
     */
    public OptionalInt getRowNumberOfVendor(String vendor) {
        return shares.stream()
                .filter(share -> share.getVendor().equals(vendor))
                .mapToInt(share -> shares.indexOf(share) + 1)
                .findFirst();
    }

    /**
     * Using Hare-Niemayer algorithm. For the sake of simplicity, only the biggest share is adjusted.
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

    private static @NonNull ArrayList<Share> getMergedShares(List<Share> shares) {
        return shares.stream()
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
    }

}
