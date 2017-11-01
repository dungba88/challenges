import java.util.concurrent.atomic.AtomicReference;

public class RingBuffer {

	private volatile int[] data;
	
	private volatile int start;
	
	private volatile int length;
	
	private SpinLock lock = new SpinLock();
	
	public RingBuffer(int maximumSize) {
		if (!isPowerOf2(maximumSize)) {
			throw new RuntimeException("Maximum size must be power of 2");
		}
		data = new int[maximumSize];
		start = length = 0;
	}
	
	private boolean isPowerOf2(int maximumSize) {
		return mask(maximumSize) == 0;
	}

	public void enqueue(int number) {
		lock.lock();
		try {
			if (full()) return;
			data[mask(start + length++)] = number;
		} finally {
			lock.unlock();
		}
	}

	public Integer dequeue() {
		lock.lock();
		try {
			if (empty()) return null;
			int result = data[start];
			start = mask(start + 1);
			length--;
			return result;
		} finally {
			lock.unlock();
		}
	}
	
	public boolean full() {
		return length == data.length;
	}

	public boolean empty() {
		return length == 0;
	}
	
	private int mask(int i) {
		return i & (data.length - 1);
	}
}

class SpinLock {

	private AtomicReference<Thread> lock = new AtomicReference<Thread>(null);
	
	public void lock() {
		Thread callingThread = Thread.currentThread();
		while(!lock.compareAndSet(null, callingThread)) {}
	}

	public void unlock() {
		Thread callingThread = Thread.currentThread();
		lock.compareAndSet(callingThread, null);
	}
}