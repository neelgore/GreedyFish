package representation;

import java.util.HashSet;

public abstract class Piece {

	private boolean isWhite;
	private String type;
	private Square square;
	private HashSet<Shift> movementDefinition;
	private HashSet<Square> currentSquareSet;
	private String abbreviation; // character used to represent the type (Knight = 'N')
	
	public Piece(boolean w, String t, Square sq) {
		isWhite = w;
		type = t;
		square = sq;
		movementDefinition = new HashSet<Shift>();
		setMovementDefinition();
		currentSquareSet = new HashSet<Square>();
		if (type.equals("Pawn")) {
			abbreviation = "";
		} else if (type.equals("Knight")) {
			abbreviation = "N";
		} else {
			abbreviation = type.substring(0, 1);
		}
	}
	
	public boolean isWhite() {
		return isWhite;
	}
	
	public String getType() {
		return type;
	}
	
	public Square getSquare() {
		return square;
	}
	
	public void setSquare(Square sq) {
		square = sq;
	}
	
	public HashSet<Shift> getMovementDefinition() {
		return movementDefinition;
	}
	
	public abstract void setMovementDefinition();
	
	public boolean sameAs(Piece p) {
		if (p == null) return false;
		return isWhite == p.isWhite;
	}
	
	public boolean enemyOf(Piece p) {
		if (p == null) return false;
		return isWhite != p.isWhite;
	}
	
	public String getAbbreviation() {
		return abbreviation;
	}
	
	public void addShift(int x, int y) { // to be called by setMovementDefinition
		movementDefinition.add(new Shift(x, y));
	}
	
	public HashSet<Square> getCurrentSquareSet() {
		return currentSquareSet;
	}
	
	public Square addSquare(Square sq) {
		currentSquareSet.add(sq);
		return sq;
	}
	
	public Piece copy() {
		if (type.equals("Pawn")) {
			return new Pawn(isWhite, square);
		} else if (type.equals("Knight")) {
			return new Knight(isWhite, square);
		} else if (type.equals("Bishop")) {
			return new Bishop(isWhite, square);
		} else if (type.equals("Rook")) {
			return new Rook(isWhite, square);
		} else if (type.equals("Queen")) {
			return new Queen(isWhite, square);
		} else if (type.equals("King")) {
			return new King(isWhite, square);
		} else { // null
			return null;
		}
	}
	
	public boolean equals(Piece p) {
		return isWhite == p.isWhite && type.equals(p.type) && square.equals(p.square);
	}
	
}
