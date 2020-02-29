package representation;

public class Rook extends Piece{

	public Rook(boolean w, Square sq) {
		super(w, "Rook", sq);
	}
	
	public void setMovementDefinition() {
		addShift(1, 0);
		addShift(-1, 0);
		addShift(0, 1);
		addShift(0, -1);
	}

	public double materialValue() {
		return 5;
	}

	public double positionalValue() {
		return 1;
	}
	
}
