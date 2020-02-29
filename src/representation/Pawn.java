package representation;

public class Pawn extends Piece {

	public Pawn(boolean w, Square sq) {
		super(w, "Pawn", sq);
	}
	
	public void setMovementDefinition() {
		int y = -1;
		if (isWhite()) y = 1;
		addShift(-1, y);
		addShift(1, y);
		addShift(0, y);
		addShift(0, 2*y);
	}

	public double materialValue() {
		return 1;
	}

	public double positionalValue() {
		return 1;
	}
	
}
