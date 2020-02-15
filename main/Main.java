package main;
import representation.*;
import engines.*;
import java.util.Scanner;

public class Main {

	
	public static void main(String[] args) {
		playGame();
	}
	
	public static void playGame() {
		Board b = new Board();
		Scanner sc = new Scanner(System.in);
		TreeEngine greedy = new GreedyFish();
		while (!b.bim() && !b.wim()) {
			Move user = b.parseMove(sc.next());
			while (user == null) {
				System.out.println("invalid move");
				user = b.parseMove(sc.next().strip());
			}
			b.move(user);
			if (b.isDone()) break;
			System.out.println("Running . . .");
			EngineMove engine = greedy.bestMove(3, b);
			System.out.println(engine.getMove());
			System.out.println("Depth reached: " + engine.getDepth());
			System.out.println("Evaluation: " + engine.getEval());
			System.out.println("Nodes reached: " + engine.getNodeCount());
			System.out.println("Time in seconds: " + engine.getTime()/1000.0);
			b.move(engine.getMove());
		}
		sc.close();
	}

	
}
