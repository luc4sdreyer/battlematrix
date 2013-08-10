package za.co.entelect.competition.bots;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
//import java.util.Random;

import za.co.entelect.competition.GameState;
import za.co.entelect.competition.GameAction;
import za.co.entelect.competition.Unit;

public class TreeNode {
    static final double epsilon = 1e-6;
    static final cern.jet.random.engine.MersenneTwister64 random = new cern.jet.random.engine.MersenneTwister64(new java.util.Date());
    static final double root2 = Math.sqrt(2);
    
    // Selection functions:
    static final int SF_UCT = 0;
    static final int SF_UCB1_TUNED = 1;    
    
    static long numGames = 0;
    static long numTicks = 0;
    static GameState origin;
    
    // Timing
    static long timeTotalCloning = 0;
    static long timeTotalSim = 0;
    
    final int selectionFunction = SF_UCT;    
    int[] actions;
    int playerIdx;			// This node's playerIdx refers to the player that has moved to get to this "state"
    
    TreeNode[] children;
    double nVisits;
    double totValue;
    
    public TreeNode(int[] actions, int playerIdx) {
    	this.actions = actions;
    	this.playerIdx = playerIdx;
    }

    //
    // This function may ONLY be called on the root!
    //    
    public static void selectAction(TreeNode root) {
        List<TreeNode> visited = new LinkedList<TreeNode>();
        TreeNode leaf = root;
        //visited.add(this);
        while (!leaf.isLeaf()) {
            leaf = leaf.select();
            visited.add(leaf);
        }
        int[] result = new int[1];
        int[] visitedCursor = new int[1];
        long t = System.nanoTime();
    	GameState simGame = origin.clone();
    	timeTotalCloning += System.nanoTime() - t;
    	
    	t = System.nanoTime();
        preSimulate(visited, simGame, result, visitedCursor);
        timeTotalSim += System.nanoTime() - t;
        
        if (simGame.isActive()) {            
            leaf.expand(simGame);
            TreeNode newNode = leaf.select();
            visited.add(newNode);
            
            t = System.nanoTime();
            simulate(visited, simGame, result, visitedCursor);
            timeTotalSim += System.nanoTime() - t;
        }
        
        int pValue = 0;
        root.nVisits++;
        for (TreeNode node : visited) {
        	pValue = 0;
        	if (node.playerIdx == 0 && result[0] == 1) {
        		pValue = 1;
        	} else if (node.playerIdx == 1 && result[0] == -1) {
        		pValue = 1;
        	}
        	node.updateStats(pValue);
        }
    }

	public void expand(GameState simGame) {
		int unitCode = 0;
		if (this.playerIdx == 0) {
			unitCode = Unit.TANK2A;
		} else {
			unitCode = Unit.TANK1A;
		}
    	ArrayList<Integer> t1Moves = simGame.getTankActions(unitCode);
    	ArrayList<Integer> t2Moves = simGame.getTankActions(unitCode + 1);
		
        children = new TreeNode[t1Moves.size() * t2Moves.size()];
        for (int i = 0; i < t1Moves.size(); i++) {
			for (int j = 0; j < t2Moves.size(); j++) {
				int[] actions = new int[2];
				actions[0] = t1Moves.get(i);
				actions[1] = t2Moves.get(j);
				children[i * t2Moves.size() + j] = new TreeNode(actions, (this.playerIdx+1) % 2);
			}
		}
    }

    private TreeNode select() {
        TreeNode selected = null;
        double bestValue = Double.NEGATIVE_INFINITY;
        double selectionValue = 0;
        
        for (TreeNode c : children) {
        	if (c.nVisits > 0) {
	        	if (this.selectionFunction == TreeNode.SF_UCT) {
	                // small random number to break ties randomly in unexpanded nodes
		        	selectionValue = c.totValue / c.nVisits +
		        			Math.sqrt(Math.log(nVisits+1) / c.nVisits);
	        	} else {
	        		double r = c.totValue / c.nVisits;
	        		double PV = this.nVisits;
	        		double p = c.nVisits;
	        		selectionValue = r + Math.sqrt((Math.log(PV) / p) * Math.min(0.25, (r - Math.pow(r, 2) + Math.sqrt(2 * Math.log(PV) / p))));
	        		
	        		double a = (Math.log(PV) / p);
	        		double eq = r + Math.sqrt(a * Math.min(0.25, (r - Math.pow(r, 2) + root2 * Math.sqrt(a))));
	        		//System.out.println(selectionValue);
	        		if (Math.abs(eq / selectionValue - 1) > 0.00000001) {
	        			System.out.println("Math.abs(eq / selectionValue - 1) > 0.00000001");
	        		}
	        	}
        	} else {
        		selectionValue = random.nextDouble();
        	}

            //ri + sqrt(((ln P) / pi) * min(0.25, (ri - ri2 + sqrt(2 * (ln P) / pi))))
            if (selectionValue > bestValue) {
                selected = c;
                bestValue = selectionValue;
            }
        }
        return selected;
    }

	public TreeNode selectFinal() {
        TreeNode selected = null;
        double bestValue = Double.NEGATIVE_INFINITY ;

        for (TreeNode c : children) {
        	double selectionValue = c.totValue;
            if (selectionValue > bestValue) {
                selected = c;
                bestValue = selectionValue;
            }
        }
        return selected;
	}

    public boolean isLeaf() {
        return children == null;
    }

    private static GameState preSimulate(List<TreeNode> visited, GameState simGame, int[] result, int[] visitedCursor) {
    	
    	boolean isActive = true;
    	
    	while (isActive && (visitedCursor[0] + 1 < visited.size())) {
    		int[] p1Moves = null;
			int[] p2Moves = null;
		
			p1Moves = visited.get(visitedCursor[0]++).actions;
			p2Moves = visited.get(visitedCursor[0]++).actions;
			
			int[] actions = new int[4];
			
			actions[0] = p1Moves[0];
			actions[1] = p1Moves[1];
			actions[2] = p2Moves[0];
			actions[3] = p2Moves[1];
			
			for (int i = 0; i < 4; i++) {
				simGame.getTanks()[i].setNextAction(actions[i]);
			}
			
			simGame.nextTick();
			numTicks++;
			
			isActive = simGame.isActive();
    	}
    	
    	if (!isActive && (visitedCursor[0] + 1 < visited.size())) {
    		System.err.println("FATAL ERROR: !isActive && (visitedCursor[0] + 1 < visited.size())");
    	}
    	
    	if (!isActive) {        	
    		switch (simGame.getStatus()) {
    			case GameState.STATUS_PLAYER1_WINS:		result[0] = 1;		break;
    			case GameState.STATUS_PLAYER2_WINS:		result[0] = -1;		break;
    			case GameState.STATUS_DRAW:				result[0] = 0;		break;
    		}
    	
    		numGames++;
    	}
    	
    	return simGame;		
	}

    public static void simulate(List<TreeNode> visited, GameState simGame, int[] result, int[] visitedCursor) {
        // ultimately a roll out will end in some value
        // assume for now that it ends in a win or a loss
        // and just return this at random   	
    	
    	boolean isActive = true;
    	
    	while (isActive) {
    		int[] p1Moves = null;
			int[] p2Moves = null;
			
			if (visitedCursor[0] < visited.size()) {
				p1Moves = visited.get(visitedCursor[0]++).actions;
			} else {
				p1Moves = RandomLegal.getActionsStatic(simGame, 0);
			}
			
			if (visitedCursor[0] < visited.size()) {
				p2Moves = visited.get(visitedCursor[0]++).actions;
			} else {
				p2Moves = RandomLegal.getActionsStatic(simGame, 1);
			}
			
			int[] actions = new int[4];
			
			actions[0] = p1Moves[0];
			actions[1] = p1Moves[1];
			actions[2] = p2Moves[0];
			actions[3] = p2Moves[1];
			
			for (int i = 0; i < 4; i++) {
				simGame.getTanks()[i].setNextAction(actions[i]);
			}
			
			simGame.nextTick();
			numTicks++;
			
			isActive = simGame.isActive();		
    	}
    	
		switch (simGame.getStatus()) {
			case GameState.STATUS_PLAYER1_WINS:		result[0] = 1;		break;
			case GameState.STATUS_PLAYER2_WINS:		result[0] = -1;		break;
			case GameState.STATUS_DRAW:				result[0] = 0;		break;
		}
		
		numGames++;
    }

    public void updateStats(int value) {
        nVisits++;
        totValue += value;
    }

    public int arity() {
        return children == null ? 0 : children.length;
    }
}