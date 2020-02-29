package engines;

import representation.*;

public abstract class ForcingTreeEngine {

	public abstract double eval(Board b);

	public EngineMove bestMove(Board b, int nodeCap) {
		long before = System.currentTimeMillis();
		Node head = new Node(b, null, 0);
		Tuple data = expandTree(head, nodeCap);
		setEvals(head);
		long after = System.currentTimeMillis();
		if (b.wtm()) {
			return new EngineMove(head.maxNodeOfChildren().getMove(), data.getDepth(), head.maxNodeOfChildren().getEval(), data.getNodeCount(), (int) (after - before));
		} else {
			return new EngineMove(head.minNodeOfChildren().getMove(), data.getDepth(), head.minNodeOfChildren().getEval(), data.getNodeCount(), (int) (after - before));
		}
	}
	
	public EngineMove bestMove(int depth, Board b) {
		long before = System.currentTimeMillis();
		Node head = new Node(b, null, 0);
		Tuple data = expandTree(depth, head);
		setEvals(head);
		long after = System.currentTimeMillis();
		if (b.wtm()) {
			return new EngineMove(head.maxNodeOfChildren().getMove(), data.getDepth(), head.maxNodeOfChildren().getEval(), data.getNodeCount(), (int) (after - before));
		} else {
			return new EngineMove(head.minNodeOfChildren().getMove(), data.getDepth(), head.minNodeOfChildren().getEval(), data.getNodeCount(), (int) (after - before));
		}
	}

	public void setEvals(Node head) { // sets evals for all nodes in a tree recursively given the head
		if (head.getChildren().isEmpty()) { // base of tree
			head.setEval(eval(head.getBoard()));
		} else {
			for (Node child: head.getChildren()) {
				setEvals(child);
			}
			if (head.getBoard().wtm()) {
				head.setEval(head.maxOfChildren());
			} else {
				head.setEval(head.minOfChildren());
			}
		}
	}

	public Tuple expandTree(Node n, int nodeCap) { // returns depth
		int nodeCount = 1;
		Queue queue = new Queue(n);
		int currentDepth = 0;
		while (nodeCount < nodeCap) {
			while (queue.peek().getDepth() == currentDepth) { // for each depth
				Node current = queue.peek();
				if (!current.getBoard().isDone()) {
					for (Move m: current.getBoard().getMoveset()) {
						Node nextInQueue = new Node(current.getBoard().ifMove(m), m, currentDepth + 1);
						current.getChildren().add(nextInQueue);
						queue.enqueue(nextInQueue);
						nodeCount++;
					}
				}
				queue.dequeue();
			}
			currentDepth++;
		}
		return new Tuple(currentDepth, nodeCount);
	}
	
	public Tuple expandTree(int depth, Node n) {
		int nodeCount = 1;
		Queue queue = new Queue(n);
		int currentDepth = 0;
		while (currentDepth < depth) {
			while (queue.peek().getDepth() == currentDepth) { // for each depth
				Node current = queue.peek();
				if (!current.getBoard().isDone()) {
					for (Move m: current.getBoard().getMoveset()) {
						Node nextInQueue = new Node(current.getBoard().ifMove(m), m, currentDepth + 1);
						current.getChildren().add(nextInQueue);
						queue.enqueue(nextInQueue);
						nodeCount++;
					}
				}
				queue.dequeue();
			}
			currentDepth++;
		}
		while (currentDepth < 3*depth) {
			while (queue.peek().getDepth() == currentDepth) {
				if (queue.peek().getMove().isCheck || queue.peek().getMove().isCapture) {
					Node current = queue.peek();
					if (!current.getBoard().isDone()) {
						for (Move m: current.getBoard().getMoveset()) {
							Board board = current.getBoard().ifMove(m);
							if (m.isCheck || m.isCapture) {
								Node nextInQueue = new Node(board, m, currentDepth + 1);
								current.getChildren().add(nextInQueue);
								queue.enqueue(nextInQueue);
								nodeCount++;
							}
						}
					}
				}
				queue.dequeue();
				if (queue.isEmpty()) return new Tuple(depth, nodeCount);
			}
			currentDepth++;
		}
		return new Tuple(depth, nodeCount);
	}

}
