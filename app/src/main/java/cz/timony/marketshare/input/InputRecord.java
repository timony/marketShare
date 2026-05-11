package cz.timony.marketshare.input;

public record InputRecord(
        String country,
        String timescale,
        String vendor,
        long units
) {
}