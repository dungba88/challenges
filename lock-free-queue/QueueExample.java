public class QueueExample {

	private final static int BATCH_SIZE = 1024;
	
	public static void main(String[] args) {
		// LockFreeQueue<Integer> buffer = new SPSCRingBuffer(1024 * 1024 * 16);
		// LockFreeQueue<Integer> unsafeBuffer = new UnsafeSPSCRingBuffer(1024 * 1024 * 16);
		LockFreeQueue<Integer[]> batchUnsafeBuffer = new BatchUnsafeSPSCRingBuffer(1024);
//		LockFreeQueue<Integer> queue = new ConcurrentLinkedList<>();
//		LockFreeQueue<Integer> javaQueue = new ConcurrentLinkedQueueWrapper<>();

		int noThreads = 1;
		int noItems = 1000000;

		long iterations = 10;
//		long totalSafe = 0;
//		long maxSafe = -1;
//		long minSafe = Long.MAX_VALUE;
		long totalUnsafe = 0;
		long maxUnsafe = -1;
		long minUnsafe = Long.MAX_VALUE;
		
		for(int i=0; i<iterations; i++) {
			// System.out.println("Testing Safe");
			// long safe = test(noThreads, noItems, buffer);
			// totalSafe += safe;
			// if (maxSafe < safe) maxSafe = safe;
			// if (minSafe > safe) minSafe = safe;
			// System.out.println("Testing Unsafe");
			long unsafe = test(noThreads, noItems, batchUnsafeBuffer);
			totalUnsafe += unsafe;
			if (maxUnsafe < unsafe) maxUnsafe = unsafe;
			if (minUnsafe > unsafe) minUnsafe = unsafe;
		}

//		System.out.println("Safe average pace: " + totalSafe / iterations + ". Max: " + maxSafe + ". Min: " + minSafe);
		System.out.println("Unsafe average pace: " + totalUnsafe / iterations + ". Max: " + maxUnsafe + ". Min: " + minUnsafe);
//		test(noThreads, noItems, queue);
//		test(noThreads, noItems, javaQueue);
	}

	private static long test(int noThreads, int noItems, LockFreeQueue<Integer[]>queue) {
		ConsumerThread[] consumers = new ConsumerThread[noThreads];
		for(int i=0; i<consumers.length; i++) {
			consumers[i] = new ConsumerThread(queue);
//			consumers[i].setPriority(Thread.MAX_PRIORITY);
			consumers[i].start();
		}
		
		long start = System.currentTimeMillis();
		
		for(int i=0; i<noItems; i++) {
			Integer[] batch = new Integer[BATCH_SIZE];
			for(int j=0; j<batch.length; j++) {
				batch[j] = i;
			}
			while (!queue.add(batch)) { }
		}
		
		while(!queue.isEmpty()) {
			Thread.onSpinWait();
		}
		
		long elapsed = (System.currentTimeMillis() - start);
		long pace = (long)noItems * 1000 * BATCH_SIZE / elapsed;
		
		System.out.println(noItems + " works @ " + elapsed + "ms. Pace: " + pace + " items/s");
		
		start = System.nanoTime();
		
		for(int i=0; i<consumers.length; i++) {
			consumers[i].cancel();
		}

		for(int i=0; i<consumers.length; i++) {
			try {
				consumers[i].join();
			} catch (InterruptedException e) {
			}
			long expected = (long)noItems * (noItems - 1) * BATCH_SIZE / 2;
			if (consumers[i].getCounter() != expected) {
				throw new RuntimeException("Consumers dequeue mismatch, expected: " + expected + ", actual: " + consumers[i].getCounter());
			}
		}
		return pace;
	}
}
	
class ConsumerThread extends Thread {
	
	private LockFreeQueue<Integer[]> queue;
	
	private long counter = 0;

	public ConsumerThread(LockFreeQueue<Integer[]> queue) {
		this.queue = queue;
	}
	
	@Override
	public void run() {
		while(!Thread.currentThread().isInterrupted()) {
			while(!Thread.currentThread().isInterrupted() && queue.isEmpty()) {
				Thread.onSpinWait();
			}
			Integer[] item = queue.poll();
			if (item != null) {
				for(int i=0; i<item.length; i++) {
					counter += item[i];
				}
			}
		}
	}
	
	public void cancel() {
		interrupt();
	}
	
	public long getCounter() {
		return counter;
	}
}