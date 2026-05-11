package cz.timony.marketshare.input;

import java.io.IOException;
import java.util.List;

public interface InputRecordReader {

    List<InputRecord> read() throws IOException;
}
