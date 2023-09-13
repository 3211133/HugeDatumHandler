package hugeDatumHandler;

import java.io.IOException;
import java.io.InputStream;

import converter.IConverter;

public class AutoSwitchDatumHandler implements IHugeDatumHandler {
	
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
	

	@Override
	public InputStream read() throws IOException {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public void update(IConverter converter) throws IOException {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void delete() {
		// TODO 自動生成されたメソッド・スタブ

	}

}
