package converter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface IConverter {
	
	void execute(InputStream in, OutputStream out) throws IOException;
	
}
