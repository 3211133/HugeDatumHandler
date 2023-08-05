package hugeDatumHandler;

import java.io.IOException;
import java.io.InputStream;

import converter.IConverter;

public interface IHugeDatumHandler extends AutoCloseable{
	
	/** RAM・ROM振り分けの境界*/
	int RAM_MAX_BYTES = 10;

	/** InputStream からDatumHandlerを生成する
	 *  */
	static IHugeDatumHandler create(InputStream is) throws IOException {
		IHugeDatumHandler ret = null;
		byte[] bytes = is.readNBytes(RAM_MAX_BYTES);
		
		if(is.available()>0){
			ret = RomDatumHandler.create(bytes,is);
		}else{
			ret = RamDatumHandler.create(bytes);
		}
		return ret;
	}
	
	/** 保持しているInputStreamをコピーして取得する
	 * */
	InputStream read() throws IOException;
	
	
	/**引数の変換ルールで内部のInputStreamの値を変換する
	 * */
	void update(IConverter converter) throws IOException;
	
	void delete();
	
	@Override
	default void close() {
		delete();
	}
	
}