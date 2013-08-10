package za.co.entelect.competition.bots;

import java.util.Arrays;

import za.co.entelect.competition.GameAction;
import za.co.entelect.competition.GameState;

public class MCTS extends Bot {

	public MCTS(int playerIndex) {
		super(playerIndex);
		super.setDebugLevel(1);
	}

	@Override
	public int[] getActions(GameState gameState, int timeLimitMS) {
		int[] gameActions = new int[2];
		Arrays.fill(gameActions, -1);
		
		if (gameState.isActive()) {
			TreeNode.origin = gameState;
			TreeNode root = new TreeNode(null, 1);
			
			long t1 = System.nanoTime();			
			for (int i = 0; i < 20000; i++) {
				TreeNode.selectAction(root);
			}
			if (this.getDebugLevel() > 0) {
				t1 = System.nanoTime() - t1;
				System.out.println("Total time: " + t1/1000000 + "\t Clone time: " + (int)(TreeNode.timeTotalCloning*100/t1) + "%\t Sim time: " + (int)(TreeNode.timeTotalSim*100/t1) + "%");
				TreeNode.timeTotalCloning = 0;
				TreeNode.timeTotalSim = 0;
			}
			
			
			
			TreeNode child = root.selectFinal();
			TreeNode grandChild = null;
			if (child.children != null) {
				grandChild = child.selectFinal();
			}
			
			if (getPlayerIndex() == 0 && child.actions != null) {
				gameActions = child.actions;
			} else if (getPlayerIndex() == 1 && grandChild.actions != null) {
				gameActions = grandChild.actions;			
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