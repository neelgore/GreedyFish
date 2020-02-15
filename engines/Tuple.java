package engines;

public class Tuple {

	private int depth;
	private int nodeCount;
	
	public Tuple(int a, int b) {
		depth = a;
		nodeCount = b;
	}
	
	public int getDepth() {
		return depth;
	}
	
	public int getNodeCount() {
		return nodeCount;
	}
	
}
