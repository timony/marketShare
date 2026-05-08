package cz.timony.marketshare.domain;

public record Share(
        Vendor vendor,
        String quoter,
        double units
) {
}
