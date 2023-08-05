package filePathHandler;

import java.io.File;
import java.io.IOException;

public class UniqueFileFactory {

	
	
	private static final String PREFIX = "TEMP";

	public static File createTempFile(File source) throws IOException {
		
		return File.createTempFile(PREFIX, null);
	}

	public static File newFile() {
		
		String filePath = String.valueOf(Thread.currentThread().getId())
				+"_"+
				String.valueOf(System.currentTimeMillis());
		return new File(filePath);
	}
	
	

}
