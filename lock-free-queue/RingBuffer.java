import java.util.concurrent.atomic.AtomicReference;

public class RingBuffer implements LockFreeQueue<Integer> {

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
		return (maximumSize & (maximumSize - 1)) == 0;
	}

	public boolean add(Integer number) {
		if (isFull()) return false;
		lock.lock();
		try {
			if (isFull()) return false;
			data[mask(start + length++)] = number;
		} finally {
			lock.unlock();
		}
		return true;
	}

	public Integer poll() {
		if (isEmpty()) return null;
		lock.lock();
		try {
			if (isEmpty()) return null;
			int result = data[start];
			start = mask(start + 1);
			length--;
			return result;
		} finally {
			lock.unlock();
		}
	}
	
	public boolean isFull() {
		return length == data.length;
	}

	public boolean isEmpty() {
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