package converter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class NoConverter implements IConverter {

	@Override
	public void execute(InputStream in, OutputStream out) throws IOException {
		in.transferTo(out);
	}

}
