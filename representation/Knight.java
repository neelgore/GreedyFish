package representation;

public class Knight extends Piece{

	public Knight(boolean w, Square sq) {
		super(w, "Knight", sq);
	}
	
	public void setMovementDefinition() {
		addShift(1, 2);
		addShift(-1, 2);
		addShift(1, -2);
		addShift(-1, -2);
		addShift(2, 1);
		addShift(-2, 1);
		addShift(2, -1);
		addShift(-2, -1);
	}
	
}
