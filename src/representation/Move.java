package representation;

import java.util.HashSet;

public class Move {

	private boolean isCastle;
	private Board board;
	private Piece piece;
	private Square from;
	private Square to;
	private Piece promotion;
	public boolean isCheck;
	public boolean isCapture;
	
	public Move(Board b, Piece p, Square sq, Piece prom) {
		isCastle = (p.getType().equals("King") && Math.abs(sq.getFile() - p.getSquare().getFile()) == 2);
		board = b;
		piece = p;
		from = piece.getSquare();
		to = sq;
		promotion = prom;
		isCheck = false;
		isCapture = false;
	}
	
	public Move(Board b, Piece p, Square sq) {
		isCastle = (p.getType().equals("King") && Math.abs(sq.getFile() - p.getSquare().getFile()) == 2);
		board = b;
		piece = p;
		from = piece.getSquare();
		to = sq;
		promotion = null;
		isCheck = false;
		isCapture = false;
	}
	
	public Move(Board b, Square f, Square t) {
		piece = b.pieceAt(f);
		isCastle = (piece.getType().equals("King") && Math.abs(t.getFile() - piece.getSquare().getFile()) == 2);
		board = b;
		from = f;
		to = t;
		promotion = null;
		isCheck = false;
		isCapture = false;
	}
	
	public boolean isCastle() {
		return isCastle;
	}
	
	public Board getBoard() {
		return board;
	}
	
	public Piece getPiece() {
		return piece;
	}
	
	public Square getFrom() {
		return from;
	}
	
	public Square getTo() {
		return to;
	}
	
	public Piece getPromotion() {
		return promotion;
	}
	
	public Move copy(Board b) {
		if (promotion == null) {
			return new Move(b, b.pieceAt(from), to);
		} else {
			return new Move(b, b.pieceAt(from), to, promotion.copy());
		}
	}
	
	public String toString() {
		if (isCastle) {
			if (to.getFile() == 2) return "O-O-O";
			return "O-O";
		}
		
		String capture = "";
		if (board.pieceAt(to) != null) capture = "x";
		if (piece.getType().equals("Pawn") && from.getFile() != to.getFile()) capture = "x";
		
		HashSet<Piece> myPieces = board.getBlackPieces();
		if (piece.isWhite()) myPieces = board.getWhitePieces();
		// myPieces = set of all the pieces of the player making this move
		String identity = "";
		HashSet<Piece> possiblePieces = new HashSet<Piece>();
		// otherPieces will contain all myPieces of the same type as piece that can go to the to square
		for (Piece p: myPieces) {
			if (p.getType().equals(piece.getType())) {
				for (Square sq: p.getCurrentSquareSet()) {
					if (sq.equals(to) && board.check(new Move(board, p, sq))) possiblePieces.add(p);
				}
			}
		}
		if (possiblePieces.size() > 1) {
			if (numOfFiles(possiblePieces) == possiblePieces.size()) {
				identity = from.toString().substring(0, 1);
			} else if (numOfRanks(possiblePieces) == possiblePieces.size()) {
				identity = from.toString().substring(1, 2);
			} else {
				identity = from.toString();
			}
		}
		if (capture.equals("x") && piece.getType().equals("Pawn")) {
			identity = piece.getSquare().toString().substring(0, 1);
		} 
		
		String prom = "";
		if (promotion != null) {
			prom = "=" + promotion.getAbbreviation();
		}
		
		String checkOrMate = "";
		Board newBoard = board.ifMove(this);
		if ((piece.isWhite() && newBoard.bim()) || (!piece.isWhite() && newBoard.wim())) {
			checkOrMate = "#";
		} else if ((piece.isWhite() && newBoard.bic()) || (!piece.isWhite() && newBoard.wic())) {
			checkOrMate = "+";
		}
		return piece.getAbbreviation() + identity + capture + to.toString() + prom + checkOrMate;
	}
	
	public int numOfFiles(HashSet<Piece> set) {
		HashSet<Integer> ints = new HashSet<Integer>();
		for (Piece p: set) {
			ints.add(p.getSquare().getFile());
		}
		return ints.size();
	}
	
	public int numOfRanks(HashSet<Piece> set) {
		HashSet<Integer> ints = new HashSet<Integer>();
		for (Piece p: set) {
			ints.add(p.getSquare().getRank());
		}
		return ints.size();
	}
	
}
