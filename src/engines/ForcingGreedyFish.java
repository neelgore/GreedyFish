package engines;

import representation.*;

public class ForcingGreedyFish extends ForcingTreeEngine { // looks for mate, if not found looks for material

	public double eval(Board b) {
		if (b.wim()) {
			return Integer.MIN_VALUE;
		} else if (b.bim()) {
			return Integer.MAX_VALUE;
		}
		if (b.insufficientMaterial() || b.isStalemate() || b.getHalfMoveCount() >= 50) {
			return 0;
		}
		int answer = 0;
		for (Piece p: b.getWhitePieces()) {
			if (p.getType().equals("Pawn")) {
				answer++;
			} else if (p.getType().equals("Knight") || p.getType().equals("Bishop")) {
				answer += 3;
			} else if (p.getType().equals("Rook")) {
				answer += 5;
			} else if (p.getType().equals("Queen")) {
				answer += 9;
			}
		}
		for (Piece p: b.getBlackPieces()) {
			if (p.getType().equals("Pawn")) {
				answer--;
			} else if (p.getType().equals("Knight") || p.getType().equals("Bishop")) {
				answer -= 3;
			} else if (p.getType().equals("Rook")) {
				answer -= 5;
			} else if (p.getType().equals("Queen")) {
				answer -= 9;
			}
		}
		return answer;
	}

}
