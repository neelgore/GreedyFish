package representation;

public class Square { // represents a square on a chessboard and is guaranteed to be a valid square

	private int file;
	private int rank;
	
	public Square(int a, int b) {
		file = a;
		rank = b;
	}
	
	public Square(String s) {
		file = Character.toLowerCase(s.charAt(0)) - 97;
		rank = s.charAt(1) - 49;
	}
	
	public int getFile() {
		return file;
	}
	
	public int getRank() {
		return rank;
	}
	
	public String toString() {
		return String.valueOf((char) (file + 97)) + (char) (rank + 49);
	}
	
	public static boolean isSquare(int a, int b) {
		return a >= 0 && a <= 7 && b >= 0 && b <= 7;
	}
	
	public boolean equals(Square sq) {
		return (file == sq.file && rank == sq.rank);
	}
	
	public boolean equals(int a, int b) {
		return (file == a && rank == b);
	}
	
	public Square add(Shift sh) {
		return new Square(file + sh.getX(), rank + sh.getY());
	}
	
}
