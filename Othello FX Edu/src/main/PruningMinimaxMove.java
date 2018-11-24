package main;

import com.eudycontreras.othello.capsules.AgentMove;

public class PruningMinimaxMove extends AgentMove {
    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public int compareTo(AgentMove o) {
        return 0;
    }
}
