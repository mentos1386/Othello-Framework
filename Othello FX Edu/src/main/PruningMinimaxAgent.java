package main;

import com.eudycontreras.othello.capsules.AgentMove;
import com.eudycontreras.othello.capsules.MoveWrapper;
import com.eudycontreras.othello.capsules.ObjectiveWrapper;
import com.eudycontreras.othello.controllers.Agent;
import com.eudycontreras.othello.controllers.AgentController;
import com.eudycontreras.othello.controllers.GameController;
import com.eudycontreras.othello.enumerations.PlayerTurn;
import com.eudycontreras.othello.models.GameBoardState;
import com.eudycontreras.othello.utilities.GameTreeUtility;

import java.util.LinkedList;
import java.util.List;

public class PruningMinimaxAgent extends Agent {

    private Integer maxDepth = 5;
    private Integer maxTime = 3000; // In ms?

    public PruningMinimaxAgent(String name, PlayerTurn playerTurn) {
        super(name, playerTurn);
    }

    @Override
    public AgentMove getMove(GameBoardState gameState) {
        gameState = GameTreeUtility.buildDecissionTree(gameState, this.maxDepth, this.maxTime);

        AlphaBetaValue alphaBetaValue = this.alphaBeta(gameState, this.maxDepth, Integer.MIN_VALUE, Integer.MAX_VALUE, true);

        GameBoardState backtrackState = alphaBetaValue.state;
        while(true) {
            if (backtrackState.getParentState().equals(gameState)) {
                return new MoveWrapper(backtrackState.getLeadingMove());
            }
            backtrackState = backtrackState.getParentState();
        }
    }



    private AlphaBetaValue alphaBeta(GameBoardState node, Integer depth, Integer alpha, Integer beta, boolean maximizePLayer) {
        this.setNodesExamined(this.getNodesExamined() + 1);

        if (depth == 0 || node.getChildStates().isEmpty()) {
            if (this.maxDepth - depth > this.getSearchDepth()) {
                this.setSearchDepth(this.maxDepth - depth);
            }

            this.setReachedLeafNodes(this.getReachedLeafNodes() + 1);
            return new AlphaBetaValue(node.getWhiteCount() - node.getBlackCount(), node);
//            return node.getParentState().getTotalReward(new MoveWrapper(node.getLeadingMove()));
        }
        if (maximizePLayer) {
            AlphaBetaValue bestCandidat = new AlphaBetaValue(Integer.MIN_VALUE, null);

            for (int i = 0; i < node.getChildStates().size(); i++) {
                GameBoardState child =  node.getChildStates().get(i);

                AlphaBetaValue childValue = this.alphaBeta(child, depth - 1, alpha, beta, false);

                if (childValue.value > bestCandidat.value) bestCandidat = childValue;
                if (bestCandidat.value > alpha) alpha = bestCandidat.value;

                if (alpha >= beta) {
                    // break (* β cut-off *)
                    node.removeChildState(child);
                    this.setPrunedCounter(this.getPrunedCounter() + 1);
                    i--;
                }
            }
            return bestCandidat;
        } else {
            AlphaBetaValue bestCandidat = new AlphaBetaValue(Integer.MAX_VALUE, null);

            for (int i = 0; i < node.getChildStates().size(); i++) {
                GameBoardState child =  node.getChildStates().get(i);

                AlphaBetaValue childValue = this.alphaBeta(child, depth - 1, alpha, beta, true);

                if (childValue.value < bestCandidat.value) bestCandidat = childValue;
                if (bestCandidat.value < beta) beta = bestCandidat.value;

                if (alpha >= beta) {
                    // break (* α cut-off *)
                    node.removeChildState(child);
                    this.setPrunedCounter(this.getPrunedCounter() + 1);
                    i--;
                }
            }
            return bestCandidat;
        }
    }
}

class AlphaBetaValue {
    public int value;
    public GameBoardState state;

    public AlphaBetaValue(int value, GameBoardState state) {
        this.value = value;
        this.state = state;
    }
}
