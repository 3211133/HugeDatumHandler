package outputStream;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import filePathHandler.UniqueFileFactory;

public class MemorySafeOutputStream extends OutputStream {

	private int count=0;
	private int maxSize;
	private OutputStream stream;
	private File file;
	
	/**
	 * 
	 * @param maxByteSize
	 * @throws IllegalArgumentException if size is negative.
	 */
	public MemorySafeOutputStream(int maxByteSize) {
		this.maxSize = maxByteSize;
		this.stream = new ByteArrayOutputStream(maxByteSize);
	}
	
	/** write関数 1/3. */
	@Override
	public void write(int b) throws IOException {
		this.switchIfSizeOver(1);
		this.stream.write(b);
	}
	/** write関数 2/3. */
	@Override
	public void write(byte[] b) throws IOException {
		this.switchIfSizeOver(b.length);
		this.stream.write(b);
	}
	/** write関数 3/3. */
	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		this.switchIfSizeOver(len);
		this.stream.write(b, off, len);
	}
	/**Closable#close().*/
	@Override
	public void close() throws IOException {
		this.stream.close();
	}
	/**Flushable#flush().*/
	@Override
	public void flush() throws IOException {
		this.stream.flush();
	}

	public Object getContent() {
		if(this.stream instanceof ByteArrayOutputStream bos) {
			return bos.toByteArray();
		}else{
			return this.file;
		}
	}
	
	private void switchIfSizeOver(int len) throws IOException {
		if(checkOverSize(len) && 
				this.stream instanceof ByteArrayOutputStream bos) {
			this.file = UniqueFileFactory.newFile();
			FileOutputStream fos = new FileOutputStream(this.file);
			fos.write(bos.toByteArray());
			this.stream = fos;
		}
		
	}

	private boolean checkOverSize(int len) {
		return this.count < this.maxSize &&
				this.maxSize < (this.count += len);
	}
	

}
