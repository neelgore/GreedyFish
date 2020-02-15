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
	
}
