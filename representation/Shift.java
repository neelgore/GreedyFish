package representation;

public class Shift { // represents an (x, y) shift; this class is basically just a tuple

	private int x;
	private int y;
	
	public Shift(int a, int b) {
		x = a;
		y = b;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public Shift times(int n) {
		return new Shift(n*x, n*y);
	}
	
}
