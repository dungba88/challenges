
public interface LockFreeQueue<T> {

	public boolean add(T item);
	
	public T poll();
	
	public boolean isEmpty();
}
