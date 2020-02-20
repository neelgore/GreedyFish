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
		while (!b.isDone()) {
			System.out.println("Play a move:");
			String userString = sc.next();
			if (userString.equals("quit")) {
				System.out.println("Quitting");
				System.exit(0);
			}
			Move user = b.parseMove(userString);
			while (user == null) {
				System.out.println("Invalid move. Try again:");
				user = b.parseMove(sc.next().strip());
			}
			b.move(user);
			if (b.isDone()) break;
			System.out.println("Running . . .");
			EngineMove engine = greedy.bestMove(3, b);
			engine.print();
			b.move(engine.getMove());
		}
		sc.close();
	}

	
}
