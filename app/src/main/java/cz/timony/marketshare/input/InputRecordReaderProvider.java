package cz.timony.marketshare.input;

import org.apache.commons.io.FilenameUtils;

import java.io.File;

/**
 * Factory class for creating InputRecordReader instances based on the file extension.
 */
public class InputRecordReaderProvider {

    private InputRecordReaderProvider() {}

    public static InputRecordReader provide(File file) {
        String extension  = FilenameUtils.getExtension(file.getName());
        if (extension.endsWith("csv")) {
            return new CsvFileInputRecordReader(file);
        }
        throw new UnsupportedOperationException(String.format("Unsupported file type: %s", extension));
    }
}
