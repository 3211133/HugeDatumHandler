package hugeDatumHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import converter.IConverter;
import filePathHandler.UniqueFileFactory;

public class RomDatumHandler implements IHugeDatumHandler {

	File source;
	
	private RomDatumHandler(File source) {
		this.source = source;
	}
	
	public static IHugeDatumHandler create(byte[] bytes, InputStream is) throws IOException {
		File source = UniqueFileFactory.newFile();
		FileOutputStream out = new FileOutputStream(source);
		out.write(bytes);
		is.transferTo(out);
		return new RomDatumHandler(source);
	}

	@Override
	public InputStream read() throws FileNotFoundException {
		return new FileInputStream(source);
	}

	@Override
	public void update(IConverter converter) throws IOException {
		//InputStream, OutputStream を生成し、Converterに渡す
		InputStream in = read();
		
		File temp = UniqueFileFactory.createTempFile(source);
		OutputStream out = new FileOutputStream(temp);
		
		converter.execute(in, out);
		
		//元のファイルを削除し一時ファイルに置き換える
		this.replace(temp);

	}


	@Override
	public void delete() {
		source.delete();
	}

	private void replace(File temp) {
		this.delete();
		temp.renameTo(this.source);
		this.source = temp;
	}

}
