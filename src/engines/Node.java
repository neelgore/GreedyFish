package engines;

import java.util.HashSet;
import representation.*;
import java.util.Random;
import java.lang.Math;

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
		HashSet<Node> contenders = new HashSet<Node>();
		for (Node n: this.children) {
			if (n.eval > max) {
				contenders.clear();
				contenders.add(n);
				max = n.eval;
			} else if (Math.abs(n.eval - max) < 0.2) {
				contenders.add(n);
			}
		}
		return randomNode(contenders);
	}

	public Node minNodeOfChildren() {
		double min = Integer.MAX_VALUE;
		HashSet<Node> contenders = new HashSet<Node>();
		for (Node n: this.children) {
			if (n.eval < min) {
				contenders.clear();
				contenders.add(n);
				min = n.eval;
			} else if (Math.abs(n.eval - min) < 0.2) {
				contenders.add(n);
			}
		}
		return randomNode(contenders);
	}

	public static Node randomNode(HashSet<Node> contenders) {
		Random r = new Random();
		int rand = r.nextInt(contenders.size());
		int i = 0;
		for (Node n: contenders) {
			if (i == rand) {
				return n;
			}
			i++;
		}
		return null;
	}
}
