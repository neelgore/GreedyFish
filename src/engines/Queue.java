package engines;

import java.util.LinkedList;

public class Queue {

	private LinkedList<Node> list;

	public Queue() {
		list = new LinkedList<Node>();
	}

	public Queue(Node first) {
		list = new LinkedList<Node>();
		list.add(first);
	}

	public Node peek() {
		return list.peek();
	}

	public Node enqueue(Node n) {
		list.add(n);
		return n;
	}

	public Node dequeue() {
		return list.removeFirst();
	}

	public int size() {
		return list.size();
	}

	public boolean isEmpty() {
		return list.size() == 0;
	}

}
