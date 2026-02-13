package sdm.freedom.agents;

import sdm.freedom.Board;
import sdm.freedom.Move;

import java.util.Random;
import java.util.Scanner;

public class RandomAgent extends AbstractAgent {

    protected RandomAgent() {
        super("Random Player");
    }

    @Override
    public Move selectNextMove(Board b, Move[] successorMoves) {

        return successorMoves[new Random().nextInt(successorMoves.length)];

    }
}
