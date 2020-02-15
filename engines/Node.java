package engines;

import java.util.HashSet;
import representation.*;

public class Node {

	private Board board;
	private HashSet<Node> children;
	private int depth;
	private double eval;
	private Move move;

	public Node(Board b, Move m, int n) {
		board = b;
		move = m;
		children = new HashSet<Node>();
		depth = n;
	}

	public Board getBoard() {
		return board;
	}

	public Move getMove() {
		return move;
	}

	public HashSet<Node> getChildren() {
		return children;
	}

	public int getDepth() {
		return depth;
	}

	public double getEval() {
		return eval;
	}

	public void setEval(double d) {
		eval = d;
	}

	public double maxOfChildren() {
		double max = Integer.MIN_VALUE;
		for (Node n: this.children) {
			if (n.eval > max) max = n.eval;
		}
		return max;
	}

	public double minOfChildren() {
		double min = Integer.MAX_VALUE;
		for (Node n: this.children) {
			if (n.eval < min) min = n.eval;
		}
		return min;
	}

	public Node maxNodeOfChildren() {
		double max = Integer.MIN_VALUE;
		Node answer = null;
		for (Node n: this.children) {
			if (n.eval >= max) {
				answer = n;
				max = n.eval;
			}
		}
		return answer;
	}

	public Node minNodeOfChildren() {
		double min = Integer.MAX_VALUE;
		Node answer = null;
		for (Node n: this.children) {
			if (n.eval <= min) {
				answer = n;
				min = n.eval;
			}
		}
		return answer;
	}

}
