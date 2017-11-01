import java.util.concurrent.atomic.AtomicReference;

public class RingBuffer<T> implements LockFreeQueue<T> {

	private volatile Object[] data;
	
	private volatile int start;
	
	private volatile int length;
	
	private SpinLock lock = new SpinLock();
	
	public RingBuffer(int maximumSize) {
		if (!isPowerOf2(maximumSize)) {
			throw new RuntimeException("Maximum size must be power of 2");
		}
		data = new Object[maximumSize];
		start = length = 0;
	}
	
	private boolean isPowerOf2(int maximumSize) {
		return (maximumSize & (maximumSize - 1)) == 0;
	}

	public void enqueue(T number) {
		lock.lock();
		try {
			if (full()) return;
			int idx = start + length++;
			data[mask(idx)] = number;
		} finally {
			lock.unlock();
		}
	}

	public T dequeue() {
		lock.lock();
		try {
			if (empty()) return null;
			T result = (T)data[start];
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
		lock.set(null);
	}
}