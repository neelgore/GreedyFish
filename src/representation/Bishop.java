package representation;

public class Bishop extends Piece{

	public Bishop(boolean w, Square sq) {
		super(w, "Bishop", sq);
	}
	
	public void setMovementDefinition() {
		addShift(1, 1);
		addShift(-1, 1);
		addShift(1, -1);
		addShift(-1, -1);
	}
	
}
