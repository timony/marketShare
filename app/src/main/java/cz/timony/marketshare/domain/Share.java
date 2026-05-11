package cz.timony.marketshare.domain;

import java.math.BigDecimal;

public class Share{
    private final String vendor;
    private final String quoter;
    private final long units;
    private BigDecimal percentage = BigDecimal.ZERO;

    public Share(String vendor, String quoter, long units) {
        this.vendor = vendor;
        this.quoter = quoter;
        this.units = units;
    }

    public String getVendor() {
        return vendor;
    }

    public String getQuoter() {
        return quoter;
    }

    public long getUnits() {
        return units;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }
}
