import java.util.concurrent.CountDownLatch;

public class QueueExample {

	public static void main(String[] args) {
		RingBuffer buffer = new RingBuffer(1024);
		
		int noThreads = 8;
		CountDownLatch startSignal = new CountDownLatch(1);
		ConsumerThread[] consumers = new ConsumerThread[noThreads];
		for(int i=0; i<consumers.length; i++) {
			consumers[i] = new ConsumerThread(startSignal, buffer);
		}
		
		for(int i=0; i<consumers.length; i++) {
			consumers[i].start();
		}
		
		startSignal.countDown();
		
		for(int i=0; i<1000; i++) {
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
		
		for(int i=0; i<consumers.length; i++) {
			System.out.println(consumers[i].getCounter());
		}
	}
}

class ConsumerThread extends Thread {
	
	private RingBuffer buffer;
	
	private CountDownLatch startSignal;
	
	private int counter = 0;

	public ConsumerThread(CountDownLatch startSignal, RingBuffer buffer) {
		this.startSignal = startSignal;
		this.buffer = buffer;
	}
	
	@Override
	public void run() {
		try {
			startSignal.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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