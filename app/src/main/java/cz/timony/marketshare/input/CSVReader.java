package cz.timony.marketshare.input;

import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.CSVReaderHeaderAwareBuilder;
import cz.timony.marketshare.domain.Market;
import cz.timony.marketshare.domain.Share;
import cz.timony.marketshare.domain.Vendor;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CSVReader implements MarketReader {

    private final File inputFile;

    public CSVReader(File inputFile) {
        if (!inputFile.exists()) {
            throw new IllegalArgumentException("CSV File does not exist");
        }
        this.inputFile = inputFile;
    }

    @Override
    public Market read() throws IOException {
        List<Share> shares = new ArrayList<>();
        try (CSVReaderHeaderAware reader = new CSVReaderHeaderAwareBuilder(new FileReader(inputFile))
                .withSkipLines(0)
                .build()) {
            Map<String, String> row;
            while ((row = reader.readMap()) != null) {
                Vendor vendor = new Vendor(row.get("Vendor"));
                String quoter = row.get("Timescale");
                double units = Double.parseDouble(row.get("Units").trim());
                shares.add(new Share(vendor, quoter, units));
            }
        } catch (com.opencsv.exceptions.CsvValidationException e) {
            throw new IOException("Failed to parse CSV", e);
        }
        return new Market(shares);
    }
}
