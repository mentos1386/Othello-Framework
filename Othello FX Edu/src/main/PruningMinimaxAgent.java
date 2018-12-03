package main;

import com.eudycontreras.othello.capsules.AgentMove;
import com.eudycontreras.othello.capsules.MoveWrapper;
import com.eudycontreras.othello.capsules.ObjectiveWrapper;
import com.eudycontreras.othello.controllers.Agent;
import com.eudycontreras.othello.controllers.AgentController;
import com.eudycontreras.othello.enumerations.PlayerTurn;
import com.eudycontreras.othello.models.GameBoardState;

import java.util.LinkedList;
import java.util.List;

public class PruningMinimaxAgent extends Agent {

    private Integer maxDepth = 2;

    public PruningMinimaxAgent(String name, PlayerTurn playerTurn) {
        super(name, playerTurn);
    }

    @Override
    public AgentMove getMove(GameBoardState gameState) {
        gameState = this.createGameBoardStateTree(gameState);


        List<ObjectiveWrapper> test = AgentController.getAvailableMoves(gameState, getPlayerTurn());
        return new MoveWrapper(test.get(0));
    }

    private GameBoardState createGameBoardStateTree(GameBoardState gameState) {
        return this.createGameBoardStateTree(gameState, 0, this.getPlayerTurn());
    }

    private GameBoardState createGameBoardStateTree(GameBoardState gameState, Integer depth, PlayerTurn player) {
        System.out.println("bananana" + depth);
        if (depth > this.maxDepth) return gameState;

        for (ObjectiveWrapper move: AgentController.getAvailableMoves(gameState, player)) {
            GameBoardState moveState = AgentController.getNewState(gameState, move);
            gameState.addChildState(this.createGameBoardStateTree(moveState, ++depth, this.getOppositePLayer(player)));
        }

        return gameState;
    }

    private PlayerTurn getOppositePLayer(PlayerTurn player) {
        switch (player) {
            case PLAYER_ONE:
                return PlayerTurn.PLAYER_TWO;
            case PLAYER_TWO:
                return PlayerTurn.PLAYER_ONE;
        }
        return null;
    }
}

//class MinimaxTreeNode {
//    public MinimaxTreeNode parentNode;
//    public List<ObjectiveWrapper> validMoves;
//    public PlayerTurn player;
//    public GameBoardState state;
//
//    public MinimaxTreeNode(GameBoardState state, PlayerTurn player) {
//        this.parentNode = null;
//        this.state = state;
//        this.player = player;
//        this.validMoves =
//    }
//
//
//    public MinimaxTreeNode(MinimaxTreeNode parentNode, GameBoardState state, PlayerTurn player) {
//        this.parentNode = parentNode;
//        this.state = state;
//        this.player = player;
//        this.validMoves = AgentController.getAvailableMoves(state, this.getOppositePLayer(this.player));
//    }
//
//
//}