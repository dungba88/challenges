import java.util.concurrent.ConcurrentLinkedQueue;

public class ConcurrentLinkedQueueWrapper<T> implements LockFreeQueue<T> {

	private ConcurrentLinkedQueue<T> queue = new ConcurrentLinkedQueue<>();

	public ConcurrentLinkedQueueWrapper() {
		
	}
	
	public boolean add(T data) {
		queue.add(data);
		return true;
	}
	
	public T poll() {
		return queue.poll();
	}
	
	public boolean isEmpty() {
		return queue.isEmpty();
	}
}