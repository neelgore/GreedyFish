package engines;

import representation.*;
import java.lang.Math;

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

	public String toString() {
		return move.toString();
	}

	public void print() {
		System.out.println();
		System.out.println("Move: " + move.toString());
		System.out.println("Depth reached: " + depth);
		if (eval > 100000) {
			System.out.println("Evaluation: +MATE");
		} else if (eval < -100000) {
			System.out.println("Evaluation: -MATE");
		} else {
			String plus = "";
			if (eval > 0) plus = "+";
			System.out.println("Evaluation: " + plus + Math.round(eval*100.0)/100.0);
		}
		System.out.println("Nodes reached: " + nodeCount);
		System.out.println("Time in seconds: " + time/1000.0);
		System.out.println();
	}

}
