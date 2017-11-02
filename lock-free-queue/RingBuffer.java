import java.util.concurrent.atomic.AtomicBoolean;

public class RingBuffer implements LockFreeQueue<Integer> {

	private int mask;

	private int maxSize;

	private volatile int[] data;

	private volatile int start;

	private volatile int length;

//	private SpinLock lock = new SpinLock();
	
	private AtomicBoolean lock = new AtomicBoolean(false);

	public RingBuffer(int maximumSize) {
		if (!isPowerOf2(maximumSize)) {
			throw new RuntimeException("Maximum size must be power of 2");
		}
		data = new int[maximumSize];
		mask = maximumSize - 1;
		maxSize = maximumSize;
		start = length = 0;
	}

	private boolean isPowerOf2(int maximumSize) {
		return (maximumSize & (maximumSize - 1)) == 0;
	}

	public boolean add(Integer number) {
		if (isFull()) return false;
		while(!lock.compareAndSet(false, true)) {}
		try {
			if (isFull()) return false;
			data[(start + length) & mask] = number;
			length++;
		} finally {
			lock.set(false);
		}
		return true;
	}

	public Integer poll() {
		if (isEmpty()) return null;
		while(!lock.compareAndSet(false, true)) {}
		try {
			if (isEmpty()) return null;
			int result = data[start];
			start = (start + 1) & mask;
			length--;
			return result;
		} finally {
			lock.set(false);
		}
	}

	public boolean isFull() {
		return length == maxSize;
	}

	public boolean isEmpty() {
		return length == 0;
	}
}

class SpinLock {

	private AtomicBoolean lock = new AtomicBoolean(false);

	public void lock() {
		while (!lock.compareAndSet(false, true)) {
		}
	}

	public void unlock() {
		lock.set(false);
	}
}