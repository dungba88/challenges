
public interface LockFreeQueue<T> {

	public void enqueue(T item);
	
	public T dequeue();
	
	public boolean empty();
}
