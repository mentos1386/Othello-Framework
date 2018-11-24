package main;

import com.eudycontreras.othello.capsules.AgentMove;
import com.eudycontreras.othello.controllers.Agent;
import com.eudycontreras.othello.enumerations.PlayerTurn;
import com.eudycontreras.othello.models.GameBoardState;

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
        return null;
    }
}
