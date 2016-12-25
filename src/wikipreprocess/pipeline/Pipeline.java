package wikipreprocess.pipeline;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class Pipeline<T> {
	private Processor<T>[] processors;
	private ArrayBlockingQueue<T> consumerQueue;
	private Producer<T> producer;
	private Consumer<T> consumer;
	private Thread consumerThread;
	private boolean consumerWaiting;
	private PipelineWorker<T>[] workers;
	private int processorsCount;
	
	public Pipeline(int numProcessors , int numWorkers){
		workers = new PipelineWorker[numWorkers];
		consumerQueue = new ArrayBlockingQueue<T>(500);
		processors = new Processor[numProcessors];
		producer = null;
		consumer = null;
		consumerThread = null;
		processorsCount = 0;
		consumerWaiting = false;
	}
	
	public void registerProcessor(Processor<T> processor){
		if(processorsCount >= processors.length) return;
		processors[processorsCount++] = processor;
	}
	
	public void registerProducer(Producer<T> producer){
		this.producer = producer;
	}
	
	public void registerConsumer(Consumer<T> consumer){
		this.consumer = consumer;
	}
	
	public boolean start(){
		if(processorsCount < processors.length) return false;
		if(consumer != null){
			consumerThread = new Thread(()->{
				while(!consumerThread.isInterrupted()){
					T src = null;
					consumerWaiting = true;
					try{
						src = consumerQueue.take();
					}catch(InterruptedException e){
						break;
					}
					consumerWaiting = false;
					consumer.consume(src);
				}
			});
			consumerThread.start();
		}
		for(int i=0;i<workers.length;++i){
			workers[i] = new PipelineWorker<T>(producer, processors, consumerQueue);
			workers[i].start();
		}
		producer.start();
		return true;
	}
	
	public void waitStop(){
		while(!producer.isStop()){
			try{Thread.sleep(5);}catch(InterruptedException e){}
		}
//		try{Thread.sleep(500);}catch(InterruptedException e){}
		for(PipelineWorker<T> p : workers){
			p.isOver = true;
			while(p.isWaiting){
				p.interrupt();
			}		
			try {p.join();} catch (InterruptedException e) {}
		}
		while(!consumerQueue.isEmpty() || !consumerWaiting){
			try{Thread.sleep(5);}catch(InterruptedException e){}
		}
		consumerThread.interrupt();
		try {consumerThread.join();} catch (InterruptedException e) {}
		consumer.end();
	}
}
