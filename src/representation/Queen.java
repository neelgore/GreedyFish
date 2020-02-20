package representation;

public class Queen extends Piece {
	
	public Queen(boolean w, Square sq) {
		super(w, "Queen", sq);
	}
	
	public void setMovementDefinition() {
		addShift(1, 0);
		addShift(-1, 0);
		addShift(0, 1);
		addShift(0, -1);
		addShift(1, 1);
		addShift(-1, 1);
		addShift(1, -1);
		addShift(-1, -1);
	}
	
}
