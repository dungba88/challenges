import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicBoolean;

import sun.misc.Unsafe;

public class UnsafeSPSCRingBuffer implements LockFreeQueue<Integer> {

	private static final long headOffset;

	private static final long tailOffset;
	
	private static final Unsafe UNSAFE;

	private int mask;

	private volatile int[] data;

	private volatile int head;

    private volatile int tail;

	static { 
		try { 
			Field field = sun.misc.Unsafe.class.getDeclaredField("theUnsafe"); 
			field.setAccessible(true);
			UNSAFE = (Unsafe) field.get(null); 
			headOffset = UNSAFE.objectFieldOffset(UnsafeSPSCRingBuffer.class.getDeclaredField("head"));
			tailOffset = UNSAFE.objectFieldOffset(UnsafeSPSCRingBuffer.class.getDeclaredField("tail"));
		} catch (Exception e) { 
			throw new AssertionError(e); 
		} 
	} 

	public UnsafeSPSCRingBuffer(int maximumSize) {
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
		UNSAFE.putOrderedInt(this, tailOffset, nextTail);
		return true;
	}

	public Integer poll() {
		if (isEmpty()) return null;
		int result = data[head];
		UNSAFE.putOrderedInt(this, headOffset, (head + 1) & mask);
		return result;
	}

	public boolean isEmpty() {
		return head == tail;
	}
}