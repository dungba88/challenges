import java.util.concurrent.atomic.AtomicReference;

public class ConcurrentLinkedList {
	
	private AtomicReference<ConcurrentNode> head;

	private AtomicReference<ConcurrentNode> tail;

	public ConcurrentLinkedList() {
		ConcurrentNode startNode = new ConcurrentNode(null);
		head = new AtomicReference<>(startNode);
		tail = new AtomicReference<>(startNode);
	}
	
	public void enqueue(Integer data) {
		ConcurrentNode node = new ConcurrentNode(data);
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
	
	private volatile ConcurrentNode next;
	
	public ConcurrentNode(Integer data) {
		this.data = data;
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
