package cz.timony.marketshare.input;

import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.CSVReaderHeaderAwareBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * An implementation of {@link InputRecordReader} that reads input records from a CSV file.
 * This class is responsible for parsing a CSV file where the first row contains
 * column headers. It maps the data in each row to an instance of {@link InputRecord}.
 * Provides mechanisms for parsing the following columns in the CSV:
 * - Country
 * - Timescale
 * - Vendor
 * - Units
 * The parsed data is then transformed into a list of {@link InputRecord} objects
 * for further processing.
 * Validation:
 * - The constructor validates the existence of the specified file. If the file does not exist, an
 *   {@link IllegalArgumentException} is thrown.
 * Error handling:
 * - Handles {@link IOException} for general file reading issues.
 * - Handles {@link com.opencsv.exceptions.CsvValidationException} for CSV parsing issues
 *   and rethrows it as an {@link IOException}.
 */
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
