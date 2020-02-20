package representation;

import java.util.HashSet;

public class Board {

	private Piece[][] board;
	private HashSet<Piece> whitePieces;
	private HashSet<Piece> blackPieces;
	private boolean wtm; // white to move
	public boolean wkc; // white can queenside castle
	public boolean wqc;
	public boolean bkc;
	public boolean bqc;
	private int wep; // white pawn on this file just advanced 2 squares; -1 if none did
	private int bep;
	private boolean isDone;
	public Square whiteKingLocation;
	public Square blackKingLocation;
	private HashSet<Move> moveset;
	private int halfMoveCount;
	private int fullMoveCount;

	public Board() {
		board = new Piece[8][8];
		whitePieces = new HashSet<Piece>();
		blackPieces = new HashSet<Piece>();
		resetAndSetUp();
		wtm = true;
		wkc = true;
		wqc = true;
		bkc = true;
		bqc = true;
		wep = -1;
		bep = -1;
		isDone = false;
		moveset = new HashSet<Move>();
		populateInitialMoveset();
		halfMoveCount = 0;
		fullMoveCount = 0;
	}
	
	public Board(Board b) { // copy of b without updating
		board = new Piece[8][8];
		whitePieces = new HashSet<Piece>();
		blackPieces = new HashSet<Piece>();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (b.board[i][j] != null) {
					board[i][j] = b.board[i][j].copy();
					if (board[i][j].isWhite()) {
						whitePieces.add(board[i][j]);
					} else {
						blackPieces.add(board[i][j]);
					}
				}
			}
		}
		whiteKingLocation = b.whiteKingLocation;
		blackKingLocation = b.blackKingLocation;
		wtm = b.wtm;
		wkc = b.wkc;
		wqc = b.wqc;
		bkc = b.bkc;
		bqc = b.bqc;
		wep = b.wep;
		bep = b.bep;
		isDone = b.isDone;
		moveset = new HashSet<Move>();
		halfMoveCount = b.halfMoveCount;
		fullMoveCount = b.fullMoveCount;
	}
	
	public Board (String s) { // constructs a board from a PGN or FEN
		boolean fen = charsInString(s, '/') == 7 && charsInString(s, ' ') == 5; // yikes can't think of a better way
		if (fen) {
			board = new Piece[8][8];
			whitePieces = new HashSet<Piece>();
			blackPieces = new HashSet<Piece>();
			int firstSpace = s.indexOf(' ');
			String[] ranks = s.substring(0, firstSpace).split("/");
			for (int j = 0; j < 8; j++) {
				int file = 0;
				for (int k = 0; k < ranks[j].length(); k++) {
					if (Character.isDigit(ranks[j].charAt(k))) {
						file += ranks[j].charAt(k) - '0';
					} else {
						board[file][7 - j] = pieceFromChar(ranks[j].charAt(k), file, 7 - j);
						if (board[file][7 - j].isWhite()) {
							whitePieces.add(board[file][7 - j]);
							if (board[file][7 - j].getType().equals("King")) {
								whiteKingLocation = new Square(file, 7 - j);
							}
						} else {
							blackPieces.add(board[file][7 - j]);
							if (board[file][7 - j].getType().equals("King")) {
								blackKingLocation = new Square(file, 7 - j);
							}
						}
						file++;
					}
				}
			}
			wtm = (s.charAt(firstSpace + 1) == 'w');
			int thirdSpace = s.substring(firstSpace + 3).indexOf(' ') + firstSpace + 3;
			wkc = false;
			wqc = false;
			bkc = false;
			bqc = false;
			for (int i = firstSpace + 3; i < thirdSpace; i++) {
				char ch = s.charAt(i);
				if (ch == 'K') {
					wkc = true;
				} else if (ch == 'k') {
					bkc = true;
				} else if (ch == 'Q') {
					wqc = true;
				} else if (ch == 'q') {
					bqc = true;
				}
			}
			int fourthSpace;
			if (s.charAt(thirdSpace + 1) != '-') {
				if (s.charAt(thirdSpace + 2) - 49 == 2) {
					wep = Character.toLowerCase(s.charAt(thirdSpace + 1)) - 97;
					bep = -1;
				} else {
					wep = -1;
					bep = Character.toLowerCase(s.charAt(thirdSpace + 1)) - 97;
				}
				fourthSpace = thirdSpace + 3;
			} else {
				bep = -1;
				wep = -1;
				fourthSpace = thirdSpace + 2;
			}
			int fifthSpace = s.lastIndexOf(" ");
			halfMoveCount = Integer.parseInt(s.substring(fourthSpace + 1, fifthSpace));
			fullMoveCount = Integer.parseInt(s.substring(fifthSpace + 1));
			moveset = new HashSet<Move>();
			update();
		} else { // PGN
			board = new Piece[8][8];
			whitePieces = new HashSet<Piece>();
			blackPieces = new HashSet<Piece>();
			resetAndSetUp();
			wtm = true;
			wkc = true;
			wqc = true;
			bkc = true;
			bqc = true;
			wep = -1;
			bep = -1;
			isDone = false;
			moveset = new HashSet<Move>();
			populateInitialMoveset();
			String[] moves = s.split("[ |\n]");
			for (String move: moves) {
				Move m = parseMove(move);
				if (m != null) move(m);
			}
		}
	}
	
	public void clear() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				board[i][j] = null;
			}
		}
		whitePieces.clear();
		blackPieces.clear();
	}

	public void resetAndSetUp() {
		board[0][0] = new Rook(true, new Square(0, 0));
		board[1][0] = new Knight(true, new Square(1, 0));
		board[2][0] = new Bishop(true, new Square(2, 0));
		board[3][0] = new Queen(true, new Square(3, 0));
		board[4][0] = new King(true, new Square(4, 0));
		board[5][0] = new Bishop(true, new Square(5, 0));
		board[6][0] = new Knight(true, new Square(6, 0));
		board[7][0] = new Rook(true, new Square(7, 0));

		whiteKingLocation = new Square(4, 0);
		blackKingLocation = new Square(4, 7);

		board[0][7] = new Rook(false, new Square(0, 7));
		board[1][7] = new Knight(false, new Square(1, 7));
		board[2][7] = new Bishop(false, new Square(2, 7));
		board[3][7] = new Queen(false, new Square(3, 7));
		board[4][7] = new King(false, new Square(4, 7));
		board[5][7] = new Bishop(false, new Square(5, 7));
		board[6][7] = new Knight(false, new Square(6, 7));
		board[7][7] = new Rook(false, new Square(7, 7));

		for (int i = 0; i < 8; i++) {
			board[i][1] = new Pawn(true, new Square(i, 1));
			board[i][6] = new Pawn(false, new Square(i, 6));
			for (int j = 2; j < 6; j++) {
				board[i][j] = null;
			}
			whitePieces.add(board[i][0]);
			whitePieces.add(board[i][1]);
			blackPieces.add(board[i][6]);
			blackPieces.add(board[i][7]);
		}
	}
	
	public void populateInitialMoveset() {
		moveset.clear();
		moveset.add(new Move(this, board[0][1], new Square(0, 2)));
		moveset.add(new Move(this, board[0][1], new Square(0, 3)));
		moveset.add(new Move(this, board[1][1], new Square(1, 2)));
		moveset.add(new Move(this, board[1][1], new Square(1, 3)));
		moveset.add(new Move(this, board[2][1], new Square(2, 2)));
		moveset.add(new Move(this, board[2][1], new Square(2, 3)));
		moveset.add(new Move(this, board[3][1], new Square(3, 2)));
		moveset.add(new Move(this, board[3][1], new Square(3, 3)));
		moveset.add(new Move(this, board[4][1], new Square(4, 2)));
		moveset.add(new Move(this, board[4][1], new Square(4, 3)));
		moveset.add(new Move(this, board[5][1], new Square(5, 2)));
		moveset.add(new Move(this, board[5][1], new Square(5, 3)));
		moveset.add(new Move(this, board[6][1], new Square(6, 2)));
		moveset.add(new Move(this, board[6][1], new Square(6, 3)));
		moveset.add(new Move(this, board[7][1], new Square(7, 2)));
		moveset.add(new Move(this, board[7][1], new Square(7, 3)));
		moveset.add(new Move(this, board[1][0], new Square(0, 2)));
		moveset.add(new Move(this, board[1][0], new Square(2, 2)));
		moveset.add(new Move(this, board[6][0], new Square(5, 2)));
		moveset.add(new Move(this, board[6][0], new Square(7, 2)));
	}

	public HashSet<Piece> getWhitePieces() {
		return whitePieces;
	}

	public HashSet<Piece> getBlackPieces() {
		return blackPieces;
	}

	public boolean wtm() {
		return wtm;
	}

	public Piece[][] getBoard() {
		return board;
	}

	public Piece pieceAt(Square sq) {
		return board[sq.getFile()][sq.getRank()];
	}

	public Piece pieceAt(int a, int b) {
		return board[a][b];
	}

	public HashSet<Move> getMoveset() {
		return moveset;
	}
	
	public boolean isDone() {
		return isDone;
	}
	
	public int getHalfMoveCount() {
		return halfMoveCount;
	}
	
	public int getFullMoveCount() {
		return fullMoveCount;
	}

	public boolean canGo(Piece p, Square sq) { // checks if a short range piece can go to sq. Doesn't check if move is legal
		if (p.getType().equals("Pawn")) {
			if (Math.abs(sq.getRank() - p.getSquare().getRank()) == 2) { // push twice
				if ((p.isWhite() && p.getSquare().getRank() != 1) || (!p.isWhite() && p.getSquare().getRank() != 6)) return false;
				return (pieceAt(sq) == null) && (pieceAt(sq.getFile(), (sq.getRank() + p.getSquare().getRank())/2) == null);
			} else if (p.getSquare().getFile() == sq.getFile()) { // single push
				return pieceAt(sq) == null;
			} else { // capture
				if ((p.isWhite() && sq.getRank() == 5) || (!p.isWhite() && sq.getRank() == 2)) { // potentially en passant
					if (!p.sameAs(pieceAt(sq)) && pieceAt(sq) != null) { // regular capture
						return true;
					} else { // en passant attempt because square is null
						if (p.isWhite()) {
							return bep == sq.getFile();
						} else {
							return wep == sq.getFile();
						}
					}
				} else { // not en passant; regular capture
					return !p.sameAs(pieceAt(sq)) && pieceAt(sq) != null;
				}
			}
		} else if (p.getType().equals("Knight")) {
			return (!p.sameAs(pieceAt(sq)));
		} else { // King
			if (Math.abs(sq.getFile() - p.getSquare().getFile()) != 2) { // not castle
				return (!p.sameAs(pieceAt(sq)));
			} else { // castle
				return canCastle(p, sq);
			}
		}
	}

	public boolean canCastle(Piece p, Square sq) {
		if ((p.isWhite() && !p.getSquare().equals(4, 0)) || (!p.isWhite() && !p.getSquare().equals(4, 7))) return false;
		if (sq.getFile() == 2) { // queenside
			if (p.isWhite()) {
				if (!wqc) return false;
				if (board[1][0] != null || board[2][0] != null || board[3][0] != null) return false;
			} else {
				if (!bqc) return false;
				if (board[1][7] != null || board[2][7] != null || board[3][7] != null) return false;
			}
			if (attacked(new Square(sq.getFile() - 1, sq.getRank()), !p.isWhite())) return false; // through check
		} else { // kingside
			if (p.isWhite()) {
				if (!wkc) return false;
				if (board[5][0] != null || board[6][0] != null) return false;
			} else {
				if (!bkc) return false;
				if (board[5][7] != null || board[6][7] != null) return false;
			}
			if (attacked(new Square(sq.getFile() + 1, sq.getRank()), !p.isWhite())) return false; // through check
		}
		if (attacked(p.getSquare(), !p.isWhite()) || attacked(sq, !p.isWhite())) return false;
		return true;
	}

	public void addLongRange(Piece p, Shift sh) {
		for (int i = 1; i < 8; i++) {
			if (!Square.isSquare(p.getSquare().getFile() + sh.getX()*i, p.getSquare().getRank() + sh.getY()*i)) break;
			if (pieceAt(p.getSquare().add(sh.times(i))) != null && (pieceAt(p.getSquare().add(sh.times(i))).sameAs(p))) break;
			if (!p.sameAs(pieceAt(p.getSquare().add(sh.times(i))))) {
				if (pieceAt(p.addSquare(p.getSquare().add(sh.times(i)))) != null) break;
			}
		}
	}

	public void updatePieces() {
		for (Piece[] f: board) {
			for (Piece p: f) {
				if (p != null) {
					p.getCurrentSquareSet().clear();
					if (p.getType().equals("Pawn") || p.getType().equals("Knight") || p.getType().equals("King")) {
						for (Shift sh: p.getMovementDefinition()) {
							if (Square.isSquare(p.getSquare().getFile() + sh.getX(), p.getSquare().getRank() + sh.getY())) {
								if (canGo(p, p.getSquare().add(sh))) {
									p.addSquare(p.getSquare().add(sh));
								}
							}
						}
					} else {
						for (Shift sh: p.getMovementDefinition()) {
							addLongRange(p, sh);
						}
					}
				}
			}
		}
	}

	public void updatePieces(boolean w) {
		for (Piece[] f: board) {
			for (Piece p: f) {
				if (p != null && p.isWhite() == w) {
					p.getCurrentSquareSet().clear();
					if (p.getType().equals("Pawn") || p.getType().equals("Knight") || p.getType().equals("King")) {
						for (Shift sh: p.getMovementDefinition()) {
							if (Square.isSquare(p.getSquare().getFile() + sh.getX(), p.getSquare().getRank() + sh.getY())) {
								if (canGo(p, p.getSquare().add(sh))) {
									p.addSquare(p.getSquare().add(sh));
								}
							}
						}
					} else {
						for (Shift sh: p.getMovementDefinition()) {
							addLongRange(p, sh);
						}
					}
				}
			}
		}
	}

	public void update() {
		updatePieces();
		moveset.clear();
		for (Piece[] f: board) {
			for (Piece p: f) {
				if (p != null && p.isWhite() == wtm) {
					for (Square sq: p.getCurrentSquareSet()) {
						if (p.getType().equals("Pawn") && ((sq.getRank() == 7 && p.isWhite()) || (sq.getRank() == 0 && !p.isWhite()))) {
							checkAdd(new Move(this, p, sq, new Queen(p.isWhite(), sq)));
							checkAdd(new Move(this, p, sq, new Rook(p.isWhite(), sq)));
							checkAdd(new Move(this, p, sq, new Bishop(p.isWhite(), sq)));
							checkAdd(new Move(this, p, sq, new Knight(p.isWhite(), sq)));
						} else {
							checkAdd(new Move(this, p, sq));
						}
					}
				}
			}
		}
		if (bim() || wim() || isStalemate() || insufficientMaterial()) isDone = true;
	}

	public void checkAdd(Move m) { // check if move results in being in check. if not, add to movelist
		if (m.isCastle()) { // already checked legality in canCastle()
			moveset.add(m);
			return;
		}
		HashSet<Piece> enemy = whitePieces;
		if (m.getPiece().isWhite()) enemy = blackPieces;
		if (pieceAt(m.getTo()) != null) enemy.remove(pieceAt(m.getTo())); // remove captured piece
		if (m.getPiece().getType().equals("King")) { // update king location
			if (m.getPiece().isWhite()) {
				whiteKingLocation = m.getTo();
			} else {
				blackKingLocation = m.getTo();
			}
		}
		Piece temp = pieceAt(m.getTo());
		board[m.getTo().getFile()][m.getTo().getRank()] = m.getPiece();
		board[m.getFrom().getFile()][m.getFrom().getRank()] = null;
		m.getPiece().setSquare(m.getTo());
		updatePieces(!m.getPiece().isWhite());
		wtm = !wtm;
		// did the move
		if ((m.getPiece().isWhite() && !wic()) || (!m.getPiece().isWhite() && !bic())) {
			moveset.add(m);
		}
		// now undo
		wtm = !wtm;
		m.getPiece().setSquare(m.getFrom());
		board[m.getTo().getFile()][m.getTo().getRank()] = temp;
		board[m.getFrom().getFile()][m.getFrom().getRank()] = m.getPiece();
		if (m.getPiece().getType().equals("King")) { // update king location
			if (m.getPiece().isWhite()) {
				whiteKingLocation = m.getFrom();
			} else {
				blackKingLocation = m.getFrom();
			}
		}
		if (pieceAt(m.getTo()) != null) enemy.add(pieceAt(m.getTo())); // add captured piece
		updatePieces(!m.getPiece().isWhite());
	}

	public boolean attacked(Square sq, boolean w) {
		HashSet<Piece> enemy = blackPieces;
		if (w) enemy = whitePieces;
		for (Piece p: enemy) {
			for (Square target: p.getCurrentSquareSet()) {
				if (target.equals(sq)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean wic() {
		return attacked(whiteKingLocation, false);
	}

	public boolean bic() {
		return attacked(blackKingLocation, true);
	}

	public boolean wim() { // white is mated
		return wic() && moveset.size() == 0;
	}

	public boolean bim() {
		return bic() && moveset.size() == 0;
	}

	public boolean isStalemate() {
		if (wtm) {
			return moveset.isEmpty() && !wic();
		} else {
			return moveset.isEmpty() && !bic();
		}
	}
	
	public void move(Move m) {
		if (m == null) return;
		HashSet<Piece> enemy = whitePieces;
		HashSet<Piece> me = blackPieces;
		if (m.getPiece().isWhite()) {
			enemy = blackPieces;
			me = whitePieces;
		}
		wtm = !wtm;
		if (pieceAt(m.getTo()) != null) enemy.remove(pieceAt(m.getTo())); // remove captured piece
		if (m.getPiece().getType().equals("Pawn") && m.getFrom().getFile() != m.getTo().getFile() && pieceAt(m.getTo()) == null) { // en passant
			Shift sh;
			if (m.getPiece().isWhite()) {
				sh = new Shift(0, -1);
			} else {
				sh = new Shift(0, 1);
			}
			enemy.remove(pieceAt(m.getTo().add(sh)));
			board[m.getTo().getFile()][m.getTo().getRank() + sh.getY()] = null;
		}
		if (m.getPiece().getType().equals("King")) { // update king location
			if (m.getPiece().isWhite()) {
				whiteKingLocation = m.getTo();
			} else {
				blackKingLocation = m.getTo();
			}
		}
		if (m.isCastle()) { // need to move the rook
			if (m.getTo().getFile() == 2) { // queenside
				board[3][m.getTo().getRank()] = board[0][m.getTo().getRank()];
				board[3][m.getTo().getRank()].setSquare(new Square(3, m.getTo().getRank()));
				board[0][m.getTo().getRank()] = null;
			} else { // kingside
				board[5][m.getTo().getRank()] = board[7][m.getTo().getRank()];
				board[5][m.getTo().getRank()].setSquare(new Square(5, m.getTo().getRank()));
				board[7][m.getTo().getRank()] = null;
			}
		}
		if (m.getPromotion() != null) {
			board[m.getTo().getFile()][m.getTo().getRank()] = m.getPromotion();
			me.remove(m.getPiece());
			me.add(m.getPromotion());
		} else {
			board[m.getTo().getFile()][m.getTo().getRank()] = m.getPiece();
		}
		board[m.getFrom().getFile()][m.getFrom().getRank()] = null;
		m.getPiece().setSquare(new Square(m.getTo().getFile(), m.getTo().getRank()));
		wep = -1;
		bep = -1;
		if (m.getPiece().getType().equals("Pawn") && Math.abs(m.getTo().getRank() - m.getFrom().getRank()) == 2) { // double pawn push; update en passant
			if (m.getPiece().isWhite()) {
				wep = m.getFrom().getFile();
			} else {
				bep = m.getFrom().getFile();
			}
		}
		if (m.getPiece().getType().equals("King")) { // update castling rights
			if (m.getPiece().isWhite()) {
				wkc = false;
				wqc = false;
			} else {
				bkc = false;
				bqc = false;
			}
		} else if (m.getPiece().getType().equals("Rook")) {
			if (m.getFrom().getFile() == 0) { // a-file rook
				if (m.getPiece().isWhite()) {
					wqc = false;
				} else {
					bqc = false;
				}
			} else if (m.getFrom().getFile() == 7) { // h-file rook
				if (m.getPiece().isWhite()) {
					wkc = false;
				} else {
					bkc = false;
				}
			}
		}
		update();
		halfMoveCount++;
		if (m.getPiece().getType().equals("Pawn") || pieceAt(m.getTo()) != null) {
			halfMoveCount = 0;
		}
		if (!m.getPiece().isWhite()) {
			fullMoveCount++;
		}
	}
	
	public void move(String f, String t) {
		Square from = new Square(f);
		Square to = new Square(t);
		move(new Move(this, pieceAt(from), to));
	}
	
	public Board copy() {
		Board newBoard = new Board();
		newBoard.whitePieces.clear();
		newBoard.blackPieces.clear();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (this.board[i][j] == null) {
					newBoard.board[i][j] = null;
				} else {
					newBoard.board[i][j] = this.board[i][j].copy();
					if (newBoard.board[i][j].isWhite()) {
						newBoard.whitePieces.add(newBoard.board[i][j]);
					} else {
						newBoard.blackPieces.add(newBoard.board[i][j]);
					}
				}
			}
		}
		newBoard.wtm = wtm;
		newBoard.wqc = wqc;
		newBoard.wkc = wkc;
		newBoard.bqc = bqc;
		newBoard.bkc = bkc;
		newBoard.wep = wep;
		newBoard.bep = bep;
		newBoard.whiteKingLocation = whiteKingLocation;
		newBoard.blackKingLocation = blackKingLocation;
		newBoard.getMoveset().clear();
		for (Move m: getMoveset()) {
			newBoard.getMoveset().add(m.copy(newBoard));
		}
		return newBoard;
	}
	
	public Board ifMove(Move m) {
		Board newBoard = new Board(this);
		newBoard.move(new Move(newBoard, newBoard.pieceAt(m.getFrom()), m.getTo(), m.getPromotion()));
		return newBoard;
	}
	
	public boolean check(Move m) { // check if move is in this board's movelist (is legal)
		for (Move fromList: moveset) {
			if (fromList.getPiece().equals(m.getPiece()) && fromList.getTo().equals(m.getTo())) {
				if (m.getPromotion() == null) {
					if (fromList.getPromotion() == null) return true;
				} else {
					if (fromList.getPromotion().equals(m.getPromotion())) return true;
				}
			}
		}
		return false;
	}
	
	public Move parseMove(String s) {
		for (Move m: moveset) {
			if (m.toString().equals(s)) {
				return m;
			}
		}
		return null;
	}
	
	public boolean equals(Board b) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (board[i][j] == null) {
					if (b.board[i][j] != null) return false;
				} else {
					if (!board[i][j].equals(b.board[i][j])) return false;
				}
			}
		}
		return wtm == b.wtm && wkc == b.wkc && wqc == b.wqc && bkc == b.bkc && bqc == b.bqc && wep == b.wep && bep == b.bep;
	}

	public boolean insufficientMaterial() {
		if (whitePieces.size() + blackPieces.size() == 2) {
			return true;
		} else if (whitePieces.size() + blackPieces.size() == 3) {
			for (Piece p: whitePieces) {
				if (p.getType().equals("Knight") || p.getType().equals("Bishop")) {
					return true;
				}
			}
			for (Piece p: blackPieces) {
				if (p.getType().equals("Knight") || p.getType().equals("Bishop")) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void printMoveset() {
		for (Move m: moveset) {
			System.out.println(m);
		}
	}
	
	public String toString() {
		String s = "";
		for (int j = 7; j >= 0; j--) {
			for (int i = 0; i < 8; i++) {
				if (pieceAt(i, j) != null) {
					if (pieceAt(i, j).isWhite()) {
						if (pieceAt(i, j).getType().equals("Pawn")) {
							s += "P ";
						} else {
							s += pieceAt(i, j).getAbbreviation() + " ";
						}
					} else {
						if (pieceAt(i, j).getType().equals("Pawn")) {
							s += "p ";
						} else {
							s += pieceAt(i, j).getAbbreviation().toLowerCase() + " ";
						}
					}
				} else {
					s += "  ";
				}
			}
			s += "\n";
		}
		return s + "\n";
	}
	
	public static int charsInString(String s, char ch) {
		int count = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == ch) count++;
		}
		return count;
	}
	
	public static Piece pieceFromChar(char ch, int file, int rank) {
		boolean white = Character.isUpperCase(ch);
		ch = Character.toLowerCase(ch);
		if (ch == 'p') {
			return new Pawn(white, new Square(file, rank));
		} else if (ch == 'n') {
			return new Knight(white, new Square(file, rank));
		} else if (ch == 'b') {
			return new Bishop(white, new Square(file, rank));
		} else if (ch == 'r') {
			return new Rook(white, new Square(file, rank));
		} else if (ch == 'q') {
			return new Queen(white, new Square(file, rank));
		} else if (ch == 'k') {
			return new King(white, new Square(file, rank));
		}
		return null;
	}

}
