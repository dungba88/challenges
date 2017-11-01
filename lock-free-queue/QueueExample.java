import java.util.ArrayList;
import java.util.List;

public class QueueExample {

	public static void main(String[] args) {
		RingBuffer buffer = new RingBuffer(1024);
		
		int noThreads = 3;
		int noItems = 10000000;
		
		ConsumerThread[] consumers = new ConsumerThread[noThreads];
		for(int i=0; i<consumers.length; i++) {
			consumers[i] = new ConsumerThread(buffer);
			//consumers[i].setPriority(Thread.MAX_PRIORITY);
			consumers[i].start();
		}
		
		long start = System.nanoTime();
		
		for(int i=0; i<noItems; i++) {
			buffer.enqueue(i);
		}
		
		while(!buffer.empty()) {
			// Thread.onSpinWait();
		}
		
		System.out.println("finish all at " + (System.nanoTime() - start)/1000000 + "ms");
		start = System.nanoTime();
		
		for(int i=0; i<consumers.length; i++) {
			consumers[i].cancel();
		}

		for(int i=0; i<consumers.length; i++) {
			try {
				consumers[i].join();
			} catch (InterruptedException e) {
			}
		}
		
		System.out.println("all threads stopped at " + (System.nanoTime() - start)/1000000 + "ms");
	}
}

class ConsumerThread extends Thread {
	
	private RingBuffer buffer;
	
	private List<Integer> list = new ArrayList<>();
	
	private int counter = 0;

	public ConsumerThread(RingBuffer buffer) {
		this.buffer = buffer;
	}
	
	@Override
	public void run() {
		while(!Thread.currentThread().isInterrupted()) {
			while(!Thread.currentThread().isInterrupted() && buffer.empty()) {
				// Thread.onSpinWait();
			}
			Integer item = buffer.dequeue();
			//if (item != null) counter++;
		}
	}
	
	public void cancel() {
		interrupt();
	}
	
	public List<Integer> getList() {
		return list;
	}
	
	public int getCounter() {
		return counter;
	}
}