package japa;

import java.util.concurrent.atomic.AtomicInteger;

public class RingBuffer {

	private volatile int[] data;
	
	private AtomicInteger start;
	
	private AtomicInteger end;
	
	public RingBuffer(int maximumSize) {
		if (!isPowerOf2(maximumSize)) {
			throw new RuntimeException("Maximum size must be power of 2");
		}
		data = new int[maximumSize];
		start = new AtomicInteger(0);
		end = new AtomicInteger(0);
	}
	
	private boolean isPowerOf2(int maximumSize) {
		return (maximumSize & (maximumSize - 1)) == 0;
	}

	public void enqueue(int number) {
		int endIdx = -1;
		int startIdx = start.get();
		// increase the endIdx atomically
		do {
			endIdx = end.get();
		} while (endIdx - startIdx < data.length && !end.compareAndSet(endIdx, endIdx + 1));
		// check if queue is full
		if (endIdx - startIdx >= data.length)
			return;
		data[endIdx & (data.length - 1)] = number;
	}
	
	public Integer dequeue() {
		int endIdx = end.get();
		int startIdx = -1;
		// increase the startIdx atomically
		do {
			startIdx = start.get();
		} while(startIdx < endIdx && !start.compareAndSet(startIdx, startIdx + 1));
		// check if queue is empty
		if (startIdx >= endIdx) return null;
		return data[startIdx & (data.length - 1)];
	}
	
	public int size() {
		return end.get() - start.get();
	}
}
