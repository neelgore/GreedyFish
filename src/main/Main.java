package main;
import representation.*;
import engines.*;
import java.util.Scanner;

public class Main {

	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("How many seconds would you like to give the engine per move?");
		playGame(sc.nextInt());
	}
	
	public static void playGame(int timeInSeconds) {
		Board b = new Board();
		Scanner sc = new Scanner(System.in);
		PrunedTreeEngine greedy = new PrunedFish();
		System.out.println("Enter moves using exact algebraic notation. Type \"quit\" to quit.");
		System.out.println("Algebraic notation: https://en.wikipedia.org/wiki/Algebraic_notation_(chess)");
		while (!b.isDone()) {
			System.out.println("Play a move:");
			String userString = sc.next();
			quit(userString);
			Move user = b.parseMove(userString);
			while (user == null) {
				System.out.println("Invalid move. Try again:");
				userString = sc.next();
				quit(userString);
				user = b.parseMove(userString);
			}
			b.move(user);
			printBoard(b);
			if (b.isDone()) finish(b);
			System.out.println("Running . . .");
			EngineMove engine = greedy.bestMove(b, timeInSeconds);
			engine.print();
			b.move(engine.getMove());
			printBoard(b);
			if (b.isDone()) finish(b);
		}
		sc.close();
	}

	public static void finish(Board b) {
		if (b.wim()) {
			System.out.println("Black wins");
		} else if (b.bim()) {
			System.out.println("White wins");
		} else {
			System.out.println("Ended in a draw");
		}
		System.exit(0);
	}

	public static void quit(String s) {
		if (s.equals("quit")) {
			System.out.println("Quitting");
			System.exit(0);
		}
	}

	public static void printBoard(Board b) {
		System.out.println();
		System.out.println(b);
		System.out.println();
	}

	
}
