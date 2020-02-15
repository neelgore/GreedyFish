package engines;

import representation.*;

public class EngineMove {

	private Move move;
	private int depth;
	private double eval;
	private int nodeCount;
	private int time;
	
	public EngineMove(Move m, int d, double e, int n, int t) {
		move = m;
		depth = d;
		eval = e;
		nodeCount = n;
		time = t;
	}
	
	public Move getMove() {
		return move;
	}
	
	public int getDepth() {
		return depth;
	}
	
	public double getEval() {
		return eval;
	}
	
	public int getNodeCount() {
		return nodeCount;
	}
	
	public int getTime() {
		return time;
	}

	public string toString() {
		return move.toString();
	}

	public void print() {
		System.out.println("Move: " + m.toString());
		System.out.println("Depth reached: " + depth);
		System.out.println("Evaluation: " + eval);
		System.out.println("Nodes reached: " + nodeCount);
		System.out.println("Time in seconds: " + time/1000.0);
	}
	
}
