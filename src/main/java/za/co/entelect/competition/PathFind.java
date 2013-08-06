package za.co.entelect.competition;


import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

public class PathFind {


	/**
	 * Compares an A* search to a BFS. 
	 */
	public void runTest(GameState gameState) {
		int size = 500;
		Random rand = new Random();
		int numTests = 100;

		int NumDiffSolutions = 0;
		int NumDiffPaths = 0;
		int NumEqual = 0;
		int NumImpossible = 0;

		long totalTimeAstar = 0;
		long totalTimeBFS = 0;
		long totalTimeAstarGrid = 0;
		long timer = 0;

		int[] totalNodesVisitedAstar = new int[1];
		int[] totalNodesVisitedBFS = new int[1];
		int[] totalNodesVisitedAstarGrid = new int[1];

		for (int j = 0; j < numTests; j++) {


			boolean[][] grid = new boolean[size][size];

			//randomly fill in squares
			for (int y = 0; y < grid.length; y++) {
				for (int x = 0; x < grid[0].length; x++) {
					if (rand.nextDouble() < 0.40) {
						grid[y][x] = true;
					}
				}
			}

			PointS start = new PointS(rand.nextInt(size), rand.nextInt(size), 0, 'X');
			PointS goal = new PointS(rand.nextInt(size), rand.nextInt(size), 0, 'X');
			while (start.p.x == goal.p.x && start.p.y == goal.p.y) {
				goal = new PointS(rand.nextInt(size), rand.nextInt(size), 0, 'X');
			}
			grid[start.p.y][start.p.x] = false;
			grid[goal.p.y][goal.p.x] = false;

			ArrayList<PointS> tempBfsResult = BFSFinder(start, goal, grid, new int[1], gameState);
			if (tempBfsResult.isEmpty()) {
				j--;
				continue;
			}

			ArrayList<PointS> astarResult = null;
			ArrayList<PointS> bfsResult = null;
			ArrayList<PointS> astarGridResult = null;

			ArrayList<Integer> ordering = new ArrayList<Integer>();
			ordering.add(0);
			ordering.add(1);
			ordering.add(2);
			Collections.shuffle(ordering);

			for (int i = 0; i < ordering.size(); i++) {
				int choice = ordering.get(i);
				if (choice == 0) {
					timer = System.nanoTime();
					//astarResult = AStarFinder(start, goal, grid, totalNodesVisitedAstar, 0, false, false);
					totalTimeAstar += System.nanoTime() - timer;
				} else if (choice == 1) {
					timer = System.nanoTime();
					bfsResult = BFSFinder(start, goal, grid, totalNodesVisitedBFS, gameState);
					totalTimeBFS += System.nanoTime() - timer;
				} else if (choice == 2) {
					timer = System.nanoTime();
					//astarGridResult = AStarFinderGrid(start, goal, grid, totalNodesVisitedAstarGrid, 0, false);
					totalTimeAstarGrid += System.nanoTime() - timer;
				}
			}

			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < astarResult.size(); i++) {
				sb.append(astarResult.get(i).direction);
			}
			String astarStr = sb.toString();

			sb = new StringBuilder();
			for (int i = 0; i < bfsResult.size(); i++) {
				sb.append(bfsResult.get(i).direction);
			}
			String bfsStr = sb.toString();

			sb = new StringBuilder();
			for (int i = 0; i < astarGridResult.size(); i++) {
				sb.append(astarGridResult.get(i).direction);
			}
			String astarGridStr = sb.toString();



			if (astarStr.length() != bfsStr.length() || (astarStr.length() != astarGridStr.length())) {
				//System.out.println("Different solutions: ");
				//System.out.println(astarStr);
				//System.out.println(bfsStr);
				NumDiffSolutions++;
			} else {
				if (!astarStr.equals(bfsStr) || !astarStr.equals(astarGridStr)) {
					//System.out.println("Different paths: ");
					//System.out.println(astarStr);
					//System.out.println(bfsStr);
					NumDiffPaths++;
				} else {
					//System.out.println("Equal paths: ");
					//System.out.println(astarStr);
					NumEqual++;
				}
				if (bfsStr.length() == 0) {
					NumImpossible++;
				}
			}
		}

		if (NumDiffSolutions > 0) {
			System.err.println("NumDiffSolutions > 0");
		}

		System.out.println("Total time used by A*:            " + totalTimeAstar / 1000000 + "ms");
		System.out.println("Total time used by A* grid:       " + totalTimeAstarGrid / 1000000 + "ms");
		System.out.println("Total time used by BFS:           " + totalTimeBFS / 1000000 + "ms");
		System.out.println();

		System.out.println("Average nodes visited by A*:      " + totalNodesVisitedAstar[0] / (double)numTests);
		System.out.println("Average nodes visited by A* grid: " + totalNodesVisitedAstarGrid[0] / (double)numTests);
		System.out.println("Average nodes visited by BFS:     " + totalNodesVisitedBFS[0] / (double)numTests);
		System.out.println();

		//System.out.println("NumDiffSolutions: "+NumDiffSolutions);
		System.out.println("NumDiffPaths: "+NumDiffPaths);
		System.out.println("NumEqual: "+NumEqual);
		System.out.println("NumImpossible: "+NumImpossible);
	}
	
	public static ArrayList<PointS> BFSFinder(PointS start, PointS goal, boolean[][] grid, int[] totalNodesVisited, GameState gameState) {
		HashSet<Point> goalSet = new HashSet<Point>();
		goalSet.add(goal.p);
		return BFSFinder(start, goalSet, goal.p, grid, totalNodesVisited, gameState);
	}

	public static ArrayList<PointS> BFSFinder(PointS start, HashSet<Point> goalArea, Point goalCenter, boolean[][] grid, int[] totalNodesVisited, GameState gameState) {
		Queue<PointS> queue = new LinkedList<PointS>();
		queue.add(start);
		PointS current = null;
		ArrayList<PointS> shortestPath = new ArrayList<PointS>();
		ArrayList<PointS> reachableGoals = new ArrayList<PointS>();
		PointS[][] came_from = new PointS[grid.length][grid[0].length]; 
		boolean[][] visited = new boolean[grid.length][grid[0].length];
		visited[start.p.y][start.p.x] = true;

		while (!queue.isEmpty()) {
			current = queue.poll();

			totalNodesVisited[0]++;
			//printGrid(start, goalArea, grid, visited, current);
			
			if (goalArea.contains(current.p)) {
				goalArea.remove(current.p);
				reachableGoals.add(current);
				continue;
			}

			ArrayList<PointS> neighs = current.getNeighbours(grid);
			for (PointS neigh : neighs) {
				int tentative_g_score = current.g + 1;
				if (!visited[neigh.p.y][neigh.p.x]) {
					neigh.g = tentative_g_score;
					visited[neigh.p.y][neigh.p.x] = true;
					came_from[neigh.p.y][neigh.p.x] = current;
					queue.add(neigh);
				}
			}
		}

		if (reachableGoals.size() != 0) {
			//
			// Get best goal			
			//
			int min = Integer.MAX_VALUE;
			int minIdx = -1;
			for (int i = 0; i < reachableGoals.size(); i++) {
				if (Util.mDist(reachableGoals.get(i).p, goalCenter) < min) {
					min = Util.mDist(reachableGoals.get(i).p, goalCenter);
					minIdx = i;
				}
			}
			PointS bestGoal = reachableGoals.get(minIdx);

			shortestPath.add(bestGoal);
			PointS prev = came_from[bestGoal.p.y][bestGoal.p.x];
			while (!prev.equals(start)) {
				shortestPath.add(prev);
				prev = came_from[prev.p.y][prev.p.x];
			}
			
			
			Collections.reverse(shortestPath);
			return shortestPath;
		} else {
			return new ArrayList<PointS>();
		}

	}


	public static class PointS {
		Point p;
		int g;	// g(x) in A*
		char direction;
		public PointS(int X, int Y, int steps, char direction) {
			super();
			this.p = new Point(X,Y);
			this.g = steps;
			this.direction = direction;
		}
		public Point getP() {
			return p;
		}
		public int getDistance(PointS x) {
			return Math.abs(x.p.x - this.p.x) + Math.abs(x.p.y - this.p.y);
		}
		public int getFScore(PointS x) {
			return this.getDistance(x) + g;
			//return steps;
		}


		@Override
		public int hashCode() {
			int hash = 1;
			hash = hash * 17 + p.x;
			hash = hash * 31 + p.y;
			//hash = hash * 13 + steps;
			return hash;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			PointS other = (PointS) obj;
			if (p == null) {
				if (other.p != null)
					return false;
			} else if (p.x != other.p.x) {
				return false;
			} else if (p.y != other.p.y)
				return false;
			//if (steps != other.steps)
			//	return false;
			return true;
		}

		public String toString() {
			return "("+this.p.x+","+this.p.y+"):"+this.g+","+this.direction;
		}

		public ArrayList<PointS> getNeighbours(boolean[][] grid) {

			//				try {
			//					Thread.sleep(1);
			//				} catch (InterruptedException e) {
			//					e.printStackTrace();
			//				}

//			PointS nextNeigh = new PointS(this.p.x+(1), this.p.y, this.g, 'E');
//			if (nextNeigh.p.x >= 0 && nextNeigh.p.x < grid[0].length && grid[nextNeigh.p.y][nextNeigh.p.x] == false) {
//				neighs.add(nextNeigh);
//			}
//			nextNeigh = new PointS(this.p.x-(1), this.p.y, this.g, 'W');
//			if (nextNeigh.p.x >= 0 && nextNeigh.p.x < grid[0].length && grid[nextNeigh.p.y][nextNeigh.p.x] == false) {
//				neighs.add(nextNeigh);
//			}
//			nextNeigh = new PointS(this.p.x, this.p.y+(1), this.g, 'N');
//			if (nextNeigh.p.y >= 0 && nextNeigh.p.y < grid.length && grid[nextNeigh.p.y][nextNeigh.p.x] == false) {
//				neighs.add(nextNeigh);
//			}
//			nextNeigh = new PointS(this.p.x, this.p.y-(1), this.g, 'S');
//			if (nextNeigh.p.y >= 0 && nextNeigh.p.y < grid.length && grid[nextNeigh.p.y][nextNeigh.p.x] == false) {
//				neighs.add(nextNeigh);
//			}
			ArrayList<PointS> neighs = new  ArrayList<PointS>();
			
			Point p = new Point(this.p.x - 2, this.p.y - 2);
			
			if (p.x < 0 || p.y < 0) {
				return neighs;
			}
			
			ArrayList<Point> points = new ArrayList<Point>();//new gameState.getNaiveTankMovesPoint(tankEdge);
			int tankSize = 5;
		
			ArrayList<Integer> moves = new ArrayList<Integer>();
			Point newP;
			newP = new Point(p.x, p.y + tankSize);
			if (newP.y < grid.length
					&& !grid[newP.y][newP.x]
					&& !grid[newP.y][newP.x+1]
					&& !grid[newP.y][newP.x+2]
					&& !grid[newP.y][newP.x+3]
					&& !grid[newP.y][newP.x+4]) {
				moves.add(0);
			}
			newP = new Point(p.x, p.y-1);
			if (newP.y >= 0
					&& !grid[newP.y][newP.x]
					&& !grid[newP.y][newP.x+1]
					&& !grid[newP.y][newP.x+2]
					&& !grid[newP.y][newP.x+3]
					&& !grid[newP.y][newP.x+4]) {
				moves.add(2);
			}
			newP = new Point(p.x + tankSize, p.y);
			if (newP.x < grid[0].length
					&& !grid[newP.y][newP.x]
					&& !grid[newP.y+1][newP.x]
					&& !grid[newP.y+2][newP.x]
					&& !grid[newP.y+3][newP.x]
					&& !grid[newP.y+4][newP.x]) {
				moves.add(1);
			}
			newP = new Point(p.x-1, p.y);
			if (newP.x >= 0
					&& !grid[newP.y][newP.x]
					&& !grid[newP.y+1][newP.x]
					&& !grid[newP.y+2][newP.x]
					&& !grid[newP.y+3][newP.x]
					&& !grid[newP.y+4][newP.x]) {
				moves.add(3);
			}
			
			for (Integer integer : moves) {
				points.add(Util.movePoint(p, integer));
			}
			
			for (Point point : points) {
				neighs.add(new PointS(point.x + 2, point.y + 2, this.g, 'X'));
			}
			
			return neighs;
		}

//		public ArrayList<PointS> getNeighbours() {	
//			ArrayList<PointS> neighs = new  ArrayList<PointS>();
//
//			PointS nextNeigh = new PointS(this.p.x+(1), this.p.y, this.g, 'E');
//			neighs.add(nextNeigh);
//			nextNeigh = new PointS(this.p.x-(1), this.p.y, this.g, 'W');
//			neighs.add(nextNeigh);
//			nextNeigh = new PointS(this.p.x, this.p.y+(1), this.g, 'N');
//			neighs.add(nextNeigh);
//			nextNeigh = new PointS(this.p.x, this.p.y-(1), this.g, 'S');
//			neighs.add(nextNeigh);
//			return neighs;
//		}
	}

	public static class PointSComparator implements Comparator<PointS>
	{
		public PointS goal;

		public PointSComparator(PointS goal) {
			this.goal = goal;
		}

		@Override
		public int compare(PointS x, PointS y)
		{
			// Assume neither string is null. Real code should
			// probably be more robust
			int xF = x.getFScore(goal);
			int yF = y.getFScore(goal);
			if (xF < yF)
			{
				return -1;
			}
			if (xF > yF)
			{
				return 1;
			}
			return 0;
		}
	}
//
//
//	public static ArrayList<PointS> AStarFinderOld(PointS start, PointS goal, boolean[][] grid, int[] totalNodesVisited) {
//		Comparator<PointS> comparator = new PointSComparator(goal);
//
//		HashSet<PointS> closedset = new HashSet<PointS>();
//		HashSet<PointS> openset = new HashSet<PointS>();
//		HashMap<PointS, PointS> came_from = new HashMap<PointS, PointS>();
//
//
//		PriorityQueue<PointS> pq = new PriorityQueue<PointS>(10, comparator);
//		pq.add(start);
//		openset.add(start);
//		ArrayList<PointS> shortestPath = new ArrayList<PointS>();
//
//		while (!pq.isEmpty()) {
//			PointS current = pq.poll();
//			if (!openset.remove(current)) {
//				System.err.println("!openset.remove(current)");
//			}
//
//			totalNodesVisited[0]++;
//			//printGrid(start, goal, grid, openset, closedset, current);
//
//			if (current.p.equals(goal.p)) {
//				shortestPath.add(current);
//				PointS prev = came_from.get(current);
//				while (!prev.equals(start)) {					
//					shortestPath.add(prev);
//					prev = came_from.get(prev);
//				}
//				break;
//			}
//
//			ArrayList<PointS> neighs = current.getNeighbours(grid);
//			for (PointS neigh : neighs) {
//				neigh.g = current.g + 1;
//
//				boolean stop = false;
//				for (PointS pointS : openset) {
//					if (pointS.p.equals(neigh.p)) {
//						if (comparator.compare(pointS, neigh) <= 0) {
//							stop = true;
//							break;
//						}
//					}
//				}
//				if (stop) {
//					continue;
//				}
//
//				for (PointS pointS : closedset) {
//					if (pointS.p.equals(neigh.p)) {
//						if (comparator.compare(pointS, neigh) <= 0) {
//							stop = true;
//							break;
//						}
//					}
//				}
//				if (stop) {
//					continue;
//				}
//
//				came_from.put(neigh, current);
//				if(!openset.add(neigh)) {
//					System.err.println("!openset.add(neigh)");
//				}
//				pq.add(neigh);
//
//				//					int tentative_g_score = current.steps + 1;
//				//					if (closedset.contains(neigh)) {
//				//						if (current.steps >= neigh.steps) {
//				//						//if (tentative_g_score >= neigh.steps) {
//				//							continue;
//				//						}
//				//					}
//				//					if (!openset.contains(neigh) || tentative_g_score < neigh.steps) {
//				//						came_from.put(neigh, current);
//				//						neigh.steps = tentative_g_score;
//				//						if (!openset.contains(neigh)) {
//				//							openset.add(neigh);
//				//							pq.add(neigh);
//				//						}
//				//					}
//			}
//			closedset.add(current);
//		}
//
//		if (shortestPath.size() != 0) {
//			Collections.reverse(shortestPath);
//			return shortestPath;
//		} else {
//			return new ArrayList<PointS>();
//		}
//
//	}
//
//
//
//	public static ArrayList<PointS> AStarFinder(PointS start, PointS goal, boolean[][] grid, int[] totalNodesVisited, int nodesVisitedBFS, boolean debugMode, boolean checkOpenFirst) {
//		Comparator<PointS> comparator = new PointSComparator(goal);
//
//
//		HashMap<PointS, PointS> closedset = new HashMap<PointS, PointS>();
//		HashMap<PointS, PointS> openset = new HashMap<PointS, PointS>();
//		HashMap<PointS, PointS> came_from = new HashMap<PointS, PointS>();
//
//
//		PriorityQueue<PointS> pq = new PriorityQueue<PointS>(10, comparator);
//		pq.add(start);
//		openset.put(start, start);
//		ArrayList<PointS> shortestPath = new ArrayList<PointS>();
//
//		int nodesVisited = 0;
//
//		while (!pq.isEmpty()) {
//			if (debugMode) {
//				if (pq.size() < openset.size()) {
//					System.err.println("pq.size() < openset.size()");
//				}
//			}
//
//			PointS current = pq.poll();
//			if (debugMode) {
//				@SuppressWarnings("unused")
//				PointS currentFromMap = openset.remove(current);
//			} else {
//				openset.remove(current);
//			}
//
//			totalNodesVisited[0]++;
//			if (debugMode) {
//				nodesVisited++;
//			}
//
//			if (debugMode) {
//				if (nodesVisited > nodesVisitedBFS) {
//					//printGrid(start, goal, grid, openset, closedset, current);
//					System.err.println("nodesVisited > nodesVisitedBFS");
//				}
//			}
//
//			if (current.p.equals(goal.p)) {
//				shortestPath.add(current);
//				PointS prev = came_from.get(current);
//				while (!prev.equals(start)) {					
//					shortestPath.add(prev);
//					prev = came_from.get(prev);
//				}
//				break;
//			}
//
//			ArrayList<PointS> neighs = current.getNeighbours(grid);
//			for (PointS neigh : neighs) {
//				neigh.g = current.g + 1;
//
//				if (checkOpenFirst) {
//					//if (openset.containsKey(neigh) && comparator.compare(openset.get(neigh), neigh) <= 0) {
//					PointS openPoint = openset.get(neigh);
//					if (openPoint != null && openPoint.g <= neigh.g) {
//						continue;
//					}
//
//					//if (closedset.containsKey(neigh) && comparator.compare(closedset.get(neigh), neigh) <= 0) {
//					PointS closedPoint = closedset.get(neigh);
//					if (closedPoint != null && closedPoint.g <= neigh.g) {
//						continue;
//					}
//				} else {
//					//if (closedset.containsKey(neigh) && comparator.compare(closedset.get(neigh), neigh) <= 0) {
//					PointS closedPoint = closedset.get(neigh);
//					if (closedPoint != null && closedPoint.g <= neigh.g) {
//						continue;
//					}
//
//					//if (openset.containsKey(neigh) && comparator.compare(openset.get(neigh), neigh) <= 0) {
//					PointS openPoint = openset.get(neigh);
//					if (openPoint != null && openPoint.g <= neigh.g) {
//						continue;
//					}					
//				}
//
//				came_from.put(neigh, current);
//				openset.put(neigh, neigh);
//				pq.add(neigh);
//			}
//			closedset.put(current, current);
//		}
//
//		if (shortestPath.size() != 0) {
//			Collections.reverse(shortestPath);
//			return shortestPath;
//		} else {
//			return new ArrayList<PointS>();
//		}
//	}	
//
//	public static ArrayList<PointS> AStarFinderGrid(PointS start, PointS goal, boolean[][] grid, int[] totalNodesVisited, int nodesVisitedBFS, boolean debugMode) {
//		Comparator<PointS> comparator = new PointSComparator(goal);
//
//		PointS[][] closedset = new PointS[grid.length][grid[0].length];
//		HashMap<PointS, PointS> openset = new HashMap<PointS, PointS>();
//		PointS[][] came_from = new PointS[grid.length][grid[0].length]; 
//
//
//		PriorityQueue<PointS> pq = new PriorityQueue<PointS>(10, comparator);
//		pq.add(start);
//		openset.put(start, start);
//		ArrayList<PointS> shortestPath = new ArrayList<PointS>();
//
//		int nodesVisited = 0;
//
//		while (!pq.isEmpty()) {
//			if (debugMode) {
//				if (pq.size() < openset.size()) {
//					System.err.println("pq.size() < openset.size()");
//				}
//			}
//
//			PointS current = pq.poll();
//			if (debugMode) {
//				@SuppressWarnings("unused")
//				PointS currentFromMap = openset.remove(current);
//			} else {
//				openset.remove(current);
//			}
//
//			totalNodesVisited[0]++;
//			if (debugMode) {
//				nodesVisited++;
//			}
//
//			if (debugMode) {
//				if (nodesVisited > nodesVisitedBFS) {
//					//printGrid(start, goal, grid, openset, closedset, current);
//					System.err.println("nodesVisited > nodesVisitedBFS");
//				}
//			}
//
//			if (current.p.equals(goal.p)) {
//				shortestPath.add(current);
//				PointS prev = came_from[current.p.y][current.p.x];
//				while (!prev.equals(start)) {					
//					shortestPath.add(prev);
//					prev = came_from[prev.p.y][prev.p.x];
//				}
//				break;
//			}
//
//			ArrayList<PointS> neighs = current.getNeighbours(grid);
//			for (PointS neigh : neighs) {
//				neigh.g = current.g + 1;
//
//
//
//				// Implemented without openset
//				//					if (pq.contains(neigh)) {
//				//						boolean replace = true;
//				//						
//				//						for (PointS pointS : pq) {
//				//							if (pointS.equals(neigh) && pointS.steps <= neigh.steps) {
//				//								replace = false;
//				//							}
//				//						}
//				//						if (!replace) {
//				//							continue;						
//				//						} else {
//				//							pq.remove(neigh);
//				//							pq.add(neigh);
//				//						}
//				//					}
//
//				//if (closedset.containsKey(neigh) && comparator.compare(closedset.get(neigh), neigh) <= 0) {
//				PointS closedPoint = closedset[neigh.p.y][neigh.p.x];
//				if (closedPoint != null && closedPoint.g <= neigh.g) {
//					continue;
//				} else {
//					closedset[neigh.p.y][neigh.p.x] = neigh;
//				}
//
//				//if (openset.containsKey(neigh) && comparator.compare(openset.get(neigh), neigh) <= 0) {
//				PointS openPoint = openset.get(neigh);
//				if (openPoint != null && openPoint.g <= neigh.g) {
//					continue;
//				}
//
//				came_from[neigh.p.y][neigh.p.x] = current;
//				openset.put(neigh, neigh);
//				pq.add(neigh);
//			}
//			closedset[current.p.y][current.p.x] = current;
//		}
//
//		if (shortestPath.size() != 0) {
//			Collections.reverse(shortestPath);
//			return shortestPath;
//		} else {
//			return new ArrayList<PointS>();
//		}
//
//	}

	public static void printGrid(PointS start, PointS goal, boolean[][] grid,
			HashMap<PointS, PointS> openset, HashMap<PointS, PointS> closedset, PointS current) {

		char[][] map = new char[grid.length][grid[0].length];

		for (int y = 0; y < map.length; y++) {
			for (int x = 0; x < map[0].length; x++) {
				if (grid[y][x]) {
					map[y][x] = '#';
				} else {
					map[y][x] = ' ';
				}
			}
		}

		for (PointS p : closedset.keySet()) {
			if (map[p.p.y][p.p.x] == ' ' || map[p.p.y][p.p.x] == '#') {
				map[p.p.y][p.p.x] = '1';
			} else {
				map[p.p.y][p.p.x]++;
			}
		}

		for (PointS p : openset.keySet()) {
			if (map[p.p.y][p.p.x] == ' ' || map[p.p.y][p.p.x] == '#') {
				map[p.p.y][p.p.x] = '1';
			} else {
				map[p.p.y][p.p.x]++;
			}
		}

		map[goal.p.y][goal.p.x] = 'G';
		map[start.p.y][start.p.x] = 'S';
		map[current.p.y][current.p.x] = 'X';

		StringBuilder sb = new StringBuilder();
		for (int y = 0; y < map.length; y++) {
			for (int x = 0; x < map[0].length; x++) {
				sb.append(map[y][x]);
			}
			sb.append('\n');
		}

		System.out.println(sb.toString());
	}

	public static void printGrid(PointS start, HashSet<Point> goal, boolean[][] grid,
			boolean[][] visited, PointS current) {

		char[][] map = new char[grid.length][grid[0].length];

		for (int y = 0; y < map.length; y++) {
			for (int x = 0; x < map[0].length; x++) {
				if (visited[y][x]) {
					map[y][x] = '.';
				} else if (grid[y][x]) {
					map[y][x] = '#';
				} else {
					map[y][x] = ' ';
				}
			}
		}
		
		for (Point p : goal) {
			map[p.y][p.x] = 'G';
		}
		
		map[start.p.y][start.p.x] = 'S';
		map[current.p.y][current.p.x] = 'X';

		StringBuilder sb = new StringBuilder();
		for (int y = 0; y < map.length; y++) {
			for (int x = 0; x < map[0].length; x++) {
				sb.append(map[y][x]);
			}
			sb.append('\n');
		}

		System.out.println(sb.toString());
	}
}
