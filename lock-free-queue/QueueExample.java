public class QueueExample {
	
		public static void main(String[] args) {
			LockFreeQueue<Integer> buffer = new SPSCRingBuffer(1024 * 1024 * 16);
	//		LockFreeQueue<Integer> queue = new ConcurrentLinkedList<>();
	//		LockFreeQueue<Integer> javaQueue = new ConcurrentLinkedQueueWrapper<>();
	
			int noThreads = 1;
			int noItems = 10000000;
			
			for(int i=0; i<10; i++) {
				test(noThreads, noItems, buffer);
			}
	//		test(noThreads, noItems, queue);
	//		test(noThreads, noItems, javaQueue);
		}
	
		private static void test(int noThreads, int noItems, LockFreeQueue<Integer>queue) {
			ConsumerThread[] consumers = new ConsumerThread[noThreads];
			for(int i=0; i<consumers.length; i++) {
				consumers[i] = new ConsumerThread(queue);
	//			consumers[i].setPriority(Thread.MAX_PRIORITY);
				consumers[i].start();
			}
			
			long start = System.currentTimeMillis();
			
			for(int i=0; i<noItems; i++) {
				while (!queue.add(i)) { }
			}
			
			while(!queue.isEmpty()) {
				Thread.onSpinWait();
			}
			
			long elapsed = (System.currentTimeMillis() - start);
			long pace = (long)noItems * 1000 / elapsed;
			
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
				long expected = (long)noItems * (noItems - 1) / 2;
				if (consumers[i].getCounter() != expected) {
					throw new RuntimeException("Consumers dequeue mismatch, expected: " + expected + ", actual: " + consumers[i].getCounter());
				}
			}
		}
	}
	
	class ConsumerThread extends Thread {
		
		private LockFreeQueue<Integer> queue;
		
		private long counter = 0;

		public ConsumerThread(LockFreeQueue<Integer> queue) {
			this.queue = queue;
		}
		
		@Override
		public void run() {
			while(!Thread.currentThread().isInterrupted()) {
				while(!Thread.currentThread().isInterrupted() && queue.isEmpty()) {
					Thread.onSpinWait();
				}
				Integer item = queue.poll();
				if (item != null) counter += item;
			}
		}
		
		public void cancel() {
			interrupt();
		}
		
		public long getCounter() {
			return counter;
		}
	}