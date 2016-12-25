package wikipreprocess.pipeline;

public interface Producer<T> {
	public T getProduction() throws InterruptedException;
	public boolean isStop();
	public void start();
}
