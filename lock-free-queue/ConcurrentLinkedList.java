import java.util.concurrent.atomic.AtomicReference;

public class ConcurrentLinkedList<T> {
	
	private AtomicReference<ConcurrentNode<T>> head;

	private AtomicReference<ConcurrentNode<T>> tail;

	public ConcurrentLinkedList() {
		ConcurrentNode<T> startNode = new ConcurrentNode<T>(null);
		head = new AtomicReference<>(startNode);
		tail = new AtomicReference<>(startNode);
	}
	
	public void enqueue(T data) {
		ConcurrentNode<T> node = new ConcurrentNode<T>(data);
		ConcurrentNode<T> curTail = tail.getAndSet(node);
		curTail.setNext(node);
	}
	
	public T dequeue() {
		ConcurrentNode<T> headNode = null;
		ConcurrentNode<T> nextNode = null;
		
		while(true) {
			headNode = head.get();
			nextNode = headNode.getNext();
			if (nextNode == null || head.compareAndSet(headNode, nextNode))
				break;
		}
		
		if (nextNode == null) return null;
		return nextNode.getData();
	}
}

class ConcurrentNode<T> {
	
	private T data;
	
	private volatile ConcurrentNode<T> next;
	
	public ConcurrentNode(T data) {
		this.data = data;
	}
	
	public T getData() {
		return data;
	}

	public ConcurrentNode<T> getNext() {
		return next;
	}

	public void setNext(ConcurrentNode<T> next) {
		this.next = next;
	}
}
