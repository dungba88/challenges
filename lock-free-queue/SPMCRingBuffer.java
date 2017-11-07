import java.util.concurrent.atomic.AtomicBoolean;

public class SPMCRingBuffer<T> implements LockFreeQueue<T> {
	
	private int mask;

	private Object[] data;
	
	private volatile int head;
	
	private volatile int tail;
	
	private AtomicBoolean lock = new AtomicBoolean(false);
	
	private static final long headOffset;

	private static final long tailOffset;

	private static final int dataBaseOffset;

	private static final int indexScale;
	
	static {
		try {
			headOffset = UnsafeUtils.objectFieldOffset(SPMCRingBuffer.class.getDeclaredField("head"));
			tailOffset = UnsafeUtils.objectFieldOffset(SPMCRingBuffer.class.getDeclaredField("tail"));
			dataBaseOffset = UnsafeUtils.arrayBaseOffset(Object[].class);
			indexScale = UnsafeUtils.arrayIndexScale(Object[].class);
		} catch (Exception e) {
			throw new AssertionError(e);
		}
	}
	
	public SPMCRingBuffer(int maximumSize) {
		if (!isPowerOf2(maximumSize)) {
			throw new RuntimeException("Maximum size must be power of 2");
		}
		data = new Object[maximumSize];
		head = tail = 0;
		mask = maximumSize - 1;
	}
	
	private boolean isPowerOf2(int maximumSize) {
		return (maximumSize & (maximumSize - 1)) == 0;
	}

	public boolean add(T number) {
		int nextTail = (tail + indexScale) & mask;
		if (nextTail == head) return false;
		UnsafeUtils.putObject(data, dataBaseOffset + tail, number);
		UnsafeUtils.putOrderedInt(this, tailOffset, nextTail);
		return true;
	}

	public T poll() {
		while(!lock.compareAndSet(false, true)) {}
		try {
			if (isEmpty()) return null;
			T result = (T) UnsafeUtils.getObject(data, dataBaseOffset + head);
			UnsafeUtils.putOrderedInt(this, headOffset, (head + indexScale) & mask);
			return result;
		} finally {
			lock.set(false);
		}
	}
	
	public boolean isEmpty() {
		return head == tail;
	}
}