package wikipreprocess.pipeline;

import java.util.concurrent.ArrayBlockingQueue;

public class PipelineWorker<T> extends Thread{
	private Processor<T>[] processors;
	private ArrayBlockingQueue<T> comsumerQueue;
	private Producer<T> producer;
	
	public boolean isWaiting;
	public boolean isOver;
	
	public PipelineWorker (Producer<T> pd , Processor<T>[] pcs , ArrayBlockingQueue<T> csq) {
		producer = pd;
		processors = pcs;
		comsumerQueue = csq;
		isWaiting = false;
		isOver = false;
	}
	@Override
	public void run() {
		while(!isInterrupted()){
			T src = null;
			if(isOver)break;
			isWaiting = true;
			try{
				src = producer.getProduction();
				
			}catch(InterruptedException e){
				break;
			}finally{
				isWaiting = false;
			}

			for(Processor<T> p : processors){
				src = p.process(src);
				if(src == null) break;
			}
			if(src != null){
				try {
					comsumerQueue.put(src);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("Worker done!");
	}
}
