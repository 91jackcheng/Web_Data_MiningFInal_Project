package wikipreprocess.pipeline;

public interface Processor<T> {
	public T process(T src);
}
