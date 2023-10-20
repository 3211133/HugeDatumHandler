package converter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ChainConverter implements IConverter {

	private final List<IConverter> converters = new ArrayList<IConverter>();

	//纏めて実行したいConverterクラスを一つづ追加する。
	public void add(IConverter converter) {
		Objects.requireNonNull(converter);
		this.converters.add(converter);
	}

	@Override
	public void execute(InputStream in, OutputStream out) throws IOException {
		//登録されているConverterの数だけThreadを用意する。
		ExecutorService executerService = Executors.newFixedThreadPool(converters.size());

		//Tread内例外を潰さないために保持しておく。
		List<Future<?>> futures = new ArrayList<>();
		
		//N+1回目のInputStreamの引数。N回目のOutputStream生成時に都度更新される。
		PipedOutputStream pipedOut = new PipedOutputStream();

		for (int i = 0; i < this.converters.size(); i++) {
			IConverter converter = this.converters.get(i);

			/* PipedInputStreamとPipedOutputStreamは、一組にする必要がある。
			 * PipedInXとPipedOutXで一組にする
			 * 
			 * i 	InputStream	OutputStream
			 * 0 	(引数の)in	PipedOut1
			 * 1 	PipedIn1	PipedOut2
			 * 2 	PipedIn2	PipedOut3
			 * ...
			 * N-1 	PipedInN1	PipedOutN
			 * N 	PipedInN	(引数の)out
			 */
			
			InputStream currentIn = (i == 0) ? in : new PipedInputStream(pipedOut);
			OutputStream currentOut = (i == this.converters.size() - 1) ? out : (pipedOut = new PipedOutputStream());

			//Thread実行。
			Future<?> future = executerService.submit(() -> {
				try (OutputStream currentOut1 = currentOut;
					InputStream currentIn1 = currentIn;){
					converter.execute(currentIn1, currentOut1);
				} catch (IOException e) {
					throw new RuntimeException(e);
				} 
			});
			futures.add(future);
		}

		executerService.shutdown();//全Threadが終了するまで待つ。
		this.throwIfThrowed(futures);//Thread内で例外が投げられていたら投げる。
	}

	private void throwIfThrowed(List<Future<?>> futures) throws IOException {
		for (Future<?> future : futures) {
			try {
				future.get();
			} catch (ExecutionException e) {
				Throwable cause = e.getCause();
				if (cause instanceof IOException ioe) {
					throw ioe;
				} else {//発生しないと思われる。
					throw new RuntimeException(cause);
				}
			} catch (InterruptedException e) {//発生しないと思われる。
				throw new RuntimeException(e);
			}
		}
	}

}
