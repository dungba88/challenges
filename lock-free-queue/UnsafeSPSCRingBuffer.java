import java.lang.reflect.Field;
import sun.misc.Unsafe;

public class UnsafeSPSCRingBuffer<T> implements LockFreeQueue<T> {

	private static final long headOffset;

	private static final long tailOffset;
	
	private static final Unsafe UNSAFE;
	
	private static final int dataBaseOffset;
	
	private static final int indexScale;

	private int mask;

	private Object[] data;

	private volatile int head;

	private volatile int tail;

	static {
		try {
			Field field = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
			field.setAccessible(true);
			UNSAFE = (Unsafe) field.get(null);
			headOffset = UNSAFE.objectFieldOffset(UnsafeSPSCRingBuffer.class.getDeclaredField("head"));
			tailOffset = UNSAFE.objectFieldOffset(UnsafeSPSCRingBuffer.class.getDeclaredField("tail"));
			dataBaseOffset = UNSAFE.arrayBaseOffset(Object[].class);
			indexScale = UNSAFE.arrayIndexScale(Object[].class);
		} catch (Exception e) {
			throw new AssertionError(e);
		}
	}

	public UnsafeSPSCRingBuffer(int maximumSize) {
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

	public boolean add(Object number) {
		int nextTail = (tail + indexScale) & mask;
		if (nextTail == head) return false;
		UNSAFE.putObject(data, dataBaseOffset + tail, number);
		UNSAFE.putOrderedInt(this, tailOffset, nextTail);
		return true;
	}

	public T poll() {
		if (isEmpty()) return null;
		T result = (T)UNSAFE.getObject(data, dataBaseOffset + head);
		UNSAFE.putOrderedInt(this, headOffset, (head + indexScale) & mask);
		return result;
	}

	public boolean isEmpty() {
		return head == tail;
	}
}