package wikipreprocess.pipeline;

public interface Consumer<T> {
	public void consume(T src);
	public void end();
}
