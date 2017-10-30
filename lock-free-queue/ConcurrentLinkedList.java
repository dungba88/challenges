import java.util.concurrent.atomic.AtomicReference;

public class ConcurrentLinkedList {
	
	private AtomicReference<ConcurrentNode> head;

	private AtomicReference<ConcurrentNode> tail;

	public ConcurrentLinkedList() {
		ConcurrentNode startNode = new ConcurrentNode(null, true);
		head = new AtomicReference<>(startNode);
		tail = new AtomicReference<>(startNode);
	}
	
	public void enqueue(Integer data) {
		if (data == null)
			throw new NullPointerException("data cannot be null");
		
		ConcurrentNode node = new ConcurrentNode(data, false);
		ConcurrentNode curTail = tail.getAndSet(node);
		curTail.setNext(node);
	}
	
	public Integer dequeue() {
		ConcurrentNode headNode = null;
		ConcurrentNode nextNode = null;
		
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

class ConcurrentNode {
	
	private Integer data;
	
	public volatile boolean startNode;
	
	private volatile ConcurrentNode next;
	
	public ConcurrentNode(Integer data, boolean startNode) {
		this.data = data;
		this.startNode = startNode;
	}
	
	public boolean isStartNode() {
		return startNode;
	}
	
	public Integer getData() {
		return data;
	}

	public ConcurrentNode getNext() {
		return next;
	}

	public void setNext(ConcurrentNode next) {
		this.next = next;
	}
}
