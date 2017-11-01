import java.util.ArrayList;
import java.util.List;

public class QueueExample {

	public static void main(String[] args) {
		RingBuffer buffer = new RingBuffer(1024);
		
		int noThreads = 7;
		int noItems = 1000;
		
		ConsumerThread[] consumers = new ConsumerThread[noThreads];
		for(int i=0; i<consumers.length; i++) {
			consumers[i] = new ConsumerThread(buffer);
			consumers[i].setPriority(Thread.MAX_PRIORITY);
			consumers[i].start();
		}
		
		for(int i=0; i<noItems; i++) {
			buffer.enqueue(i);
		}
		
		while(!buffer.empty()) {
			// Thread.onSpinWait();
		}
		
		System.out.println("finish all");
		
		for(int i=0; i<consumers.length; i++) {
			consumers[i].cancel();
		}

		for(int i=0; i<consumers.length; i++) {
			try {
				consumers[i].join();
			} catch (InterruptedException e) {
			}
		}
		
		List<Integer> counters = new ArrayList<Integer>();
		for(int i=0; i<consumers.length; i++) {
			counters.add(consumers[i].getCounter());
			System.out.println(consumers[i].getCounter());
		}
		assert counters.stream().reduce((a, b)->(a+b)).orElse(0) == noItems;
	}
}

class ConsumerThread extends Thread {
	
	private RingBuffer buffer;
	
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
			if (item != null) counter++;
		}
	}
	
	public void cancel() {
		interrupt();
	}
	
	public int getCounter() {
		return counter;
	}
}