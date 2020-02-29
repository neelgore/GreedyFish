package representation;

public class King extends Piece {

	public King(boolean w, Square sq) {
		super(w, "King", sq);
	}
	
	public void setMovementDefinition() {
		addShift(1, 1);
		addShift(1, 0);
		addShift(1, -1);
		addShift(0, 1);
		addShift(0, -1);
		addShift(-1, 1);
		addShift(-1, 0);
		addShift(-1, -1);
		addShift(2, 0);
		addShift(-2, 0);
	}

	public double materialValue() {
		return 3;
	}

	public double positionalValue() {
		if (getSquare().getRank() > 2 && getSquare().getRank() < 5 && getSquare().getFile() > 2 && getSquare().getFile() < 5) {
			return 1;
		} else if (getSquare().getRank() > 1 && getSquare().getRank() < 6 && getSquare().getFile() > 1 && getSquare().getFile() < 6) {
			return 0.9;
		} else {
			return 0.8;
		}
	}
	
}
