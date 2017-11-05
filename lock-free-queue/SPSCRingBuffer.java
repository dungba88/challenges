import java.util.concurrent.atomic.AtomicBoolean;

public class SPSCRingBuffer implements LockFreeQueue<Integer> {

	private int mask;

	private volatile int[] data;

	private volatile int head;

	private volatile int tail;
    
	public SPSCRingBuffer(int maximumSize) {
		if (!isPowerOf2(maximumSize)) {
			throw new RuntimeException("Maximum size must be power of 2");
		}
		data = new int[maximumSize];
		mask = maximumSize - 1;
		head = tail = 0;
	}

	private boolean isPowerOf2(int maximumSize) {
		return (maximumSize & (maximumSize - 1)) == 0;
	}

	public boolean add(Integer number) {
		int nextTail = (tail + 1) & mask;
		if (nextTail == head) return false;
		data[tail] = number;
		tail = nextTail;
		return true;
	}

	public Integer poll() {
		if (isEmpty()) return null;
		int result = data[head];
		head = (head + 1) & mask;
		return result;
	}

	public boolean isEmpty() {
		return head == tail;
	}
}