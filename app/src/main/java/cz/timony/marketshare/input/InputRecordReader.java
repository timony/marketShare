package cz.timony.marketshare.input;

import java.io.IOException;
import java.util.List;

/**
 * Defines a contract for reading input records from a data source.
 * The implementation of this interface is responsible for reading the records
 * and returning them as a list of {@link InputRecord} instances.
 * Implementations may represent different types of input, such as CSV files,
 * JSON, or other formats. They are expected to handle the parsing and any
 * necessary transformations required to produce {@link InputRecord} objects.
 * Users of this interface should handle any {@link IOException} related to
 * reading or parsing the data source.
 */
public interface InputRecordReader {

    /**
     * Reads and returns a list of {@link InputRecord} objects from the data source.
     * @return A list of {@link InputRecord} objects.
     * @throws IOException if an I/O error occurs while reading the data source or if there is an issue with parsing the data.
     */
    List<InputRecord> read() throws IOException;
}
