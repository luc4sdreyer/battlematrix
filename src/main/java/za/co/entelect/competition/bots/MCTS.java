package za.co.entelect.competition.bots;

import java.util.Arrays;
import java.util.HashMap;

import za.co.entelect.competition.GameState;
import za.co.entelect.competition.Util;

public class MCTS extends Bot {

	protected boolean ignoreTimeLimit;
	
	public long numGames = 0;
    public long numTicks = 0;
    public long numNodes = 0;
    public long newTranspositionTableHits = 0;
    public GameState origin;
    public HashMap<Long, Long> transpositionTable;
    
    // Timing
    public long timeTotalCloning = 0;
    public long timeTotalSim = 0;
	
	public MCTS(int playerIndex) {
		super(playerIndex);
		super.setDebugLevel(1);
		ignoreTimeLimit = true;
	}

	@Override
	public int[] getActions(GameState gameState, int timeLimitMS) {
		int[] gameActions = new int[2];
		Arrays.fill(gameActions, -1);
		
		if (gameState.isActive()) {
			this.numGames = 0;
			this.numTicks = 0;
			this.numNodes = 0;
			this.newTranspositionTableHits = 0;
			this.timeTotalCloning = 0;
			this.timeTotalSim = 0;
			this.origin = gameState;
			this.transpositionTable = new HashMap<Long, Long>();
			TreeNode root = new TreeNode(null, 1, this);
			
			long t1 = System.nanoTime();
			if (ignoreTimeLimit) {
				for (int i = 0; i < 10000; i++) {
					TreeNode.selectAction(root, this);
				}
				t1 = System.nanoTime() - t1;
			} else {
				long timeLimitNS = timeLimitMS * 1000000;
				while (System.nanoTime() - t1 < timeLimitNS) {
					TreeNode.selectAction(root, this);
				}
			}
			
			TreeNode child = root.selectFinal();
			TreeNode grandChild = null;
			TreeNode chosenNode = null;
			String childResults = new String();
			if (child.children != null) {
				grandChild = child.selectFinal();
			}
			
			if (getPlayerIndex() == 0 && child.actions != null) {
				chosenNode = child;
				childResults += '[';
				for (int i = 0; i < root.children.length; i++) {
					if (i != 0) {
						childResults += ", ";
					}
					childResults += String.format("%.2f", root.children[i].totValue/root.children[i].nVisits*100);
				}
				childResults += ']';
			} else if (getPlayerIndex() == 1 && grandChild.actions != null) {
				chosenNode = grandChild;
				childResults += '[';
				for (int i = 0; i < child.children.length; i++) {
					if (i != 0) {
						childResults += ", ";
					}
					childResults += String.format("%.2f", child.children[i].totValue/child.children[i].nVisits*100);
				}
				childResults += ']';		
			}
			
			gameActions = chosenNode.actions;
			
			if (this.getDebugLevel() > 0) {
				System.out.println("Time: " 	+ Util.padRight(t1/1000000 + "", 8)
						+ "Clone/Sim ratio: "	+ Util.padRight((int)(this.timeTotalCloning*100/t1) + "%/" + (int)(this.timeTotalSim*100/t1) + "%", 8)
						+ "Success rate: " 		+ Util.padRight(String.format("%.2f", chosenNode.getSuccessRate()*100) + "%", 8) + " " + childResults						
						//+ "Num TreeNodes: "		+ Util.padRight((int)(TreeNode.numNodes) + "", 8)
						//+ "Num TPT hits: "		+ Util.padRight(TreeNode.newTranspositionTableHits + "", 6)
						);
			}
			
			if (gameActions == null) {
				gameActions = Random.getActionsStatic();
				System.out.println("\tMCTS using RANDOM move instead.");
			}
		} else {
			gameActions = Random.getActionsStatic();
			System.out.println("\tMCTS using RANDOM move instead.");
		}
		return gameActions;
	}
}