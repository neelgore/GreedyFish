package engines;

import representation.*;

public class PrunedFish extends PrunedTreeEngine { // looks for mate, if not found looks for material

	public double eval(Board b) {
		if (b.wim()) {
			return Integer.MIN_VALUE;
		} else if (b.bim()) {
			return Integer.MAX_VALUE;
		}
		if (b.insufficientMaterial() || b.isStalemate() || b.getHalfMoveCount() >= 50) {
			return 0;
		}
		double answer = 0;
		for (Piece p: b.getWhitePieces()) {
			answer += p.materialValue()*p.positionalValue();
		}
		for (Piece p: b.getBlackPieces()) {
			answer -= p.materialValue()*p.positionalValue();
		}
		return answer;
	}

}
