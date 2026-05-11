package cz.timony.marketshare.domain;

public class Share{
    private final String vendor;
    private final String quoter;
    private final long units;
    private double percentage;

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

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public double getPercentage() {
        return percentage;
    }
}
