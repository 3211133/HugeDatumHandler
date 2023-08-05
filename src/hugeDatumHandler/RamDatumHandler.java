package hugeDatumHandler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import converter.IConverter;

public class RamDatumHandler implements IHugeDatumHandler {

	byte[] source;
	
	static IHugeDatumHandler create(byte[] bytes) {
		return new RamDatumHandler(bytes);
	}
	
	private RamDatumHandler(byte[] bytes) {
		this.source = bytes;
	}

	@Override
	public InputStream read() {
		return new ByteArrayInputStream(source);
	}

	@Override
	public void update(IConverter converter) throws IOException {
		InputStream in = read();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		converter.execute(in, out);
		this.source = out.toByteArray();
	}

	@Override
	public void delete() {
		
	}

}
