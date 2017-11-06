public class SPSCRingBuffer<T> implements LockFreeQueue<T> {

	private int mask;

	private Object[] data;

	private volatile int head;

	private volatile int tail;
    
	public SPSCRingBuffer(int maximumSize) {
		if (!isPowerOf2(maximumSize)) {
			throw new RuntimeException("Maximum size must be power of 2");
		}
		data = new Object[maximumSize];
		mask = maximumSize - 1;
		head = tail = 0;
	}

	private boolean isPowerOf2(int maximumSize) {
		return (maximumSize & (maximumSize - 1)) == 0;
	}

	public boolean add(T number) {
		int nextTail = (tail + 1) & mask;
		if (nextTail == head) return false;
		data[tail] = number;
		tail = nextTail;
		return true;
	}

	public T poll() {
		if (isEmpty()) return null;
		T result = (T)data[head];
		head = (head + 1) & mask;
		return result;
	}

	public boolean isEmpty() {
		return head == tail;
	}
}