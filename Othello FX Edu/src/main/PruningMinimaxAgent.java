package main;

import com.eudycontreras.othello.capsules.AgentMove;
import com.eudycontreras.othello.capsules.MoveWrapper;
import com.eudycontreras.othello.controllers.Agent;
import com.eudycontreras.othello.controllers.AgentController;
import com.eudycontreras.othello.enumerations.PlayerTurn;
import com.eudycontreras.othello.models.GameBoardState;
import com.eudycontreras.othello.utilities.GameTreeUtility;


public class PruningMinimaxAgent extends Agent {

    private Integer maxDepth = 3;
    private Integer maxTime = 5000; // In ms?

    public PruningMinimaxAgent(String name, PlayerTurn playerTurn) {
        super(name, playerTurn);
    }

    @Override
    public AgentMove getMove(GameBoardState gameState) {

        // Create the tree to maxDepth or maxTime, whichever is first
        gameState = GameTreeUtility.buildDecissionTree(gameState, this.maxDepth, this.maxTime);

        // Get best alphaBeta node
        AlphaBetaValue alphaBetaValue = this.alphaBeta(gameState, this.maxDepth, Double.MIN_VALUE, Double.MAX_VALUE, true);

        // We go back throu the tree (from leaf to root) to find which is the move that we need to do.
        GameBoardState backtrackState = alphaBetaValue.state;
        while(true) {
            // When we found that currenct backtrackState's parent is our current gameState
            // we get the move that took us there and return it
            if (backtrackState.getParentState().equals(gameState)) {
                return new MoveWrapper(backtrackState.getLeadingMove());
            }
            backtrackState = backtrackState.getParentState();
        }
    }


    /**
     * AlphaBeta recursive algorithm
     * @param node node that we are searching
     * @param depth current depth
     * @param alpha alpha value
     * @param beta beta value
     * @param maximizePLayer if TRUE we are doing MAX step else we are doing MIN step
     * @return
     */
    private AlphaBetaValue alphaBeta(GameBoardState node, Integer depth, Double alpha, Double beta, boolean maximizePLayer) {
        this.setNodesExamined(this.getNodesExamined() + 1);

        // Check if we reached the end of the tree (leaf node) or if we reached max depth that we are searching
        if (depth == 0 || node.getChildStates().isEmpty()) {
            if (this.maxDepth - depth > this.getSearchDepth()) {
                this.setSearchDepth(this.maxDepth - depth);
            }
            this.setReachedLeafNodes(this.getReachedLeafNodes() + 1);


            // return AlphaBetaValue object, which has node and value of the node inside.
            // value of the node is a score of how good is the node. whiteCount - blackCount could be a score.
//            return new AlphaBetaValue(node.getWhiteCount() - node.getBlackCount(), node);
            return new AlphaBetaValue(AgentController.getGameEvaluation(node, this.getPlayerTurn()), node);
        }

        // Do the MAX step
        if (maximizePLayer) {
            AlphaBetaValue bestCandidate = new AlphaBetaValue(Double.MIN_VALUE, null);

            // Go through all the children
            for (int i = 0; i < node.getChildStates().size(); i++) {
                GameBoardState child =  node.getChildStates().get(i);

                // calculate alphaBeta for each one of them (recursion)
                AlphaBetaValue childValue = this.alphaBeta(child, depth - 1, alpha, beta, false);

                // set ALPHA value and bestCandidate (one with biggest value)
                if (childValue.value > bestCandidate.value) bestCandidate = childValue;
                if (bestCandidate.value > alpha) alpha = bestCandidate.value;

                // if alpha is => beta, then we remove all other children from the tree, and we skip them
                if (alpha >= beta) {
                    // break (* β cut-off *)
                    node.removeChildState(child);
                    this.setPrunedCounter(this.getPrunedCounter() + 1);
                    i--;
                }
            }
            return bestCandidate;
        // Do the MIN step
        } else {
            AlphaBetaValue bestCandidate = new AlphaBetaValue(Double.MAX_VALUE, null);

            // Go through all the children
            for (int i = 0; i < node.getChildStates().size(); i++) {
                GameBoardState child =  node.getChildStates().get(i);

                // calculate alphaBeta for each one of them (recursion)
                AlphaBetaValue childValue = this.alphaBeta(child, depth - 1, alpha, beta, true);

                // set BETA value and bestCandidate (one with biggest value)
                if (childValue.value < bestCandidate.value) bestCandidate = childValue;
                if (bestCandidate.value < beta) beta = bestCandidate.value;

                // if alpha is => beta, then we remove all other children from the tree, and we skip them
                if (alpha >= beta) {
                    // break (* α cut-off *)
                    node.removeChildState(child);
                    this.setPrunedCounter(this.getPrunedCounter() + 1);
                    i--;
                }
            }
            return bestCandidate;
        }
    }


//    private GameBoardState createGameBoardStateTree(GameBoardState gameState) {
//        return this.createGameBoardStateTree(gameState, 0, this.getPlayerTurn());
//    }
//
//    private GameBoardState createGameBoardStateTree(GameBoardState gameState, Integer depth, PlayerTurn player) {
//        System.out.println("bananana" + depth);
//        if (depth > this.maxDepth) return gameState;
//
//        for (ObjectiveWrapper move: AgentController.getAvailableMoves(gameState, player)) {
//            GameBoardState moveState = AgentController.getNewState(gameState, move);
//            gameState.addChildState(this.createGameBoardStateTree(moveState, ++depth, GameTreeUtility.getCounterPlayer(player)));
//        }
//
//        return gameState;
//    }
}


/**
 * Class that just holds value(score) and state(node).
 */
class AlphaBetaValue {
    public double value;
    public GameBoardState state;

    public AlphaBetaValue(double value, GameBoardState state) {
        this.value = value;
        this.state = state;
    }
}
