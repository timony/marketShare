package cz.timony.marketshare.input;

import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.CSVReaderHeaderAwareBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CsvFileInputRecordReader implements InputRecordReader {

    private final File inputFile;

    public CsvFileInputRecordReader(File inputFile) {
        if (!inputFile.exists()) {
            throw new IllegalArgumentException("CSV File does not exist");
        }
        this.inputFile = inputFile;
    }

    @Override
    public List<InputRecord> read() throws IOException {
        List<InputRecord> shares = new ArrayList<>();
        try (CSVReaderHeaderAware reader = new CSVReaderHeaderAwareBuilder(new FileReader(inputFile))
                .withSkipLines(0)
                .build()) {
            Map<String, String> row;
            while ((row = reader.readMap()) != null) {
                shares.add(new InputRecord(
                                row.get("Country"),
                                row.get("Timescale"),
                                row.get("Vendor"),
                                Long.parseLong(row.get("Units")
                                        .replace(".", ""))
                        )
                );
            }
        } catch (com.opencsv.exceptions.CsvValidationException e) {
            throw new IOException("Failed to parse CSV", e);
        }
        return shares;
    }
}
