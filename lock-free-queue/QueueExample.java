public class QueueExample {
	
		public static void main(String[] args) {
			LockFreeQueue<Integer> buffer = new RingBuffer(1024 * 1024 * 16);
	//		LockFreeQueue<Integer> queue = new ConcurrentLinkedList<>();
	//		LockFreeQueue<Integer> javaQueue = new ConcurrentLinkedQueueWrapper<>();
	
			int noThreads = 3;
			int noItems = 10000000;
			
			test(noThreads, noItems, buffer);
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
			
			System.out.println("finish all " + noItems + " works at " + elapsed + "ms. Pace: " + pace + " items/s");
			
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
		
		private LockFreeQueue<?> queue;
		
		private int counter = 0;
		
		public ConsumerThread(LockFreeQueue<?> queue) {
			this.queue = queue;
		}
		
		@Override
		public void run() {
			while(!Thread.currentThread().isInterrupted()) {
				while(!Thread.currentThread().isInterrupted() && queue.isEmpty()) {
					Thread.onSpinWait();
				}
				queue.poll();
				//if (item != null) counter++;
			}
		}
		
		public void cancel() {
			interrupt();
		}
		
		public int getCounter() {
			return counter;
		}
	}