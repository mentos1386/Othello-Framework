package main;

import com.eudycontreras.othello.capsules.AgentMove;
import com.eudycontreras.othello.controllers.Agent;
import com.eudycontreras.othello.controllers.AgentController;
import com.eudycontreras.othello.enumerations.PlayerTurn;
import com.eudycontreras.othello.models.GameBoardState;
import com.eudycontreras.othello.threading.ThreadManager;
import com.eudycontreras.othello.threading.TimeSpan;

public class PruningMinimaxAgent extends Agent {
    public PruningMinimaxAgent(PlayerTurn playerTurn) {
        super(playerTurn);
    }

    public PruningMinimaxAgent(String agentName) {
        super(agentName);
    }

    public PruningMinimaxAgent(String agentName, PlayerTurn playerTurn) {
        super(agentName, playerTurn);
    }

    @Override
    public AgentMove getMove(GameBoardState gameState) {
        return getPrunMove(gameState);
    }
    
    

  
    private AgentMove getPrunMove(GameBoardState gameState){
		
		int waitTime = UserSettings.MIN_SEARCH_TIME; // 1.5 seconds
		
		ThreadManager.pause(TimeSpan.millis(waitTime)); // Pauses execution for the wait time to cause delay
		
		//give me all availableMoves - for each move do minmax and find best move
		//take that move (index in the list of available moves) with the best result
		return null;
	}
    
    private Double minimax(GameBoardState gameState, Integer depth, PlayerTurn maximizingPlayer) {
    	
    	if (depth == 0) { // Or final state
    		return AgentController.getGameEvaluation(gameState, maximizingPlayer);
    		
    	}
    			
    	
    	
    	return null;
    			
    }
    
    
}
