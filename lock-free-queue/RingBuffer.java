import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class RingBuffer {

	private volatile int[] data;
	
	private AtomicReference<RingBufferProp> propRef;
	
	public RingBuffer(int maximumSize) {
		if (!isPowerOf2(maximumSize)) {
			throw new RuntimeException("Maximum size must be power of 2");
		}
		data = new int[maximumSize];
		propRef = new AtomicReference<RingBufferProp>(new RingBufferProp(0, 0, data.length));
	}
	
	private boolean isPowerOf2(int maximumSize) {
		return (maximumSize & (maximumSize - 1)) == 0;
	}

	public void enqueue(int number) {
		RingBufferProp prop = null;
		RingBufferProp nextProp = null;
		// increase the length atomically
		do {
			prop = propRef.get();
			nextProp = prop.nextEnqueue();
		} while(nextProp != null && !propRef.compareAndSet(prop, nextProp));
		// check if queue is full
		if (nextProp == null) return;
		data[(nextProp.getStart() + nextProp.getLength()) & (data.length - 1)] = number;
	}
	
	public Integer dequeue() {
		RingBufferProp prop = null;
		RingBufferProp nextProp = null;
		// increase the startIdx and length atomically
		do {
			prop = propRef.get();
			nextProp = prop.nextDequeue();
		} while(nextProp != null && !propRef.compareAndSet(prop, nextProp));
		// check if queue is empty
		if (nextProp == null) return null;
		return data[nextProp.getStart()];
	}
}

class RingBufferProp {
	
	private int start;
	
	private int length;
	
	private int maxSize;
	
	public RingBufferProp(int start, int length, int maxSize) {
		this.start = start;
		this.length = length;
		this.maxSize = maxSize;
	}
	
	public RingBufferProp nextDequeue() {
		if (length <= 0)
			return null;
		return new RingBufferProp((start + 1) & (maxSize - 1), length - 1, maxSize);
	}

	public RingBufferProp nextEnqueue() {
		if (length >= maxSize)
			return null;
		return new RingBufferProp(start, length + 1, maxSize);
	}

	public int getStart() {
		return start;
	}
	
	public int getLength() {
		return length;
	}
}
