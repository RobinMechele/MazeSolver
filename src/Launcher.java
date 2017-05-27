package mazesolver;

import java.io.IOException;

public class Launcher {

	public static void main(String[] args) throws IOException {
		Solver solveMaze = new Solver();
		solveMaze.makeMaze();
		solveMaze.loadMaze();
		solveMaze.printResults();
	}
}
