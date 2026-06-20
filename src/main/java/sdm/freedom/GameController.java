package sdm.freedom;

import java.util.InputMismatchException;
import java.util.concurrent.CompletableFuture;

import javax.swing.SwingUtilities;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import sdm.freedom.agents.AbstractAgent;
import sdm.freedom.agents.AgentFactory;
import sdm.freedom.agents.InputListenerAgent;

public class GameController implements MoveInputListener {

    private static GameController instance;

    private Match match;
    private UIController uiController;
    private AbstractAgent[] agents;

    private GameController() {
    }

    public static GameController getInstance() {
        if (instance == null) {
            instance = new GameController();
        }
        return instance;
    }

    public void initialize(int boardSize, UIController uiController, AbstractAgent[] agents) {
        this.uiController = uiController;
        this.agents = agents;
        this.match = new Match(boardSize);

        uiController.setMoveInputListener(this);

        refreshUI();
        CompletableFuture.runAsync(this::startTurn);
    }

    public void reset() {
        match = null;
        uiController = null;
        agents = null;
        instance = null;
    }

    public Move[] getLegalMoves() {
        return match.getCurrentState().getLegalSuccessors();
    }

    public Move getLastMove() {
        return match.getCurrentState().getLastMove();
    }

    private void startTurn() {
        AbstractAgent agent = agents[match.getCurrentPlayerIdx()];

        CompletableFuture<Move> future
                = agent.selectNextMove(match.getCurrentState());

        future.thenAccept(this::applyMove);
    }

    public void applyMove(Move move) {

        match.applyMove(move);

        boolean terminal = match.getCurrentState().isTerminal();

        refreshUI();

        if (terminal) {
            int[] scores = match.evaluateBoard();
            SwingUtilities.invokeLater(() -> endGame(scores));
        }

        // Run next turn by itself as soon as possible. It should avoid async issues with other features (UI)
        if (!terminal) {
            CompletableFuture.runAsync(this::startTurn);
        }
    }

    private void refreshUI() {
        int[] scores = match.evaluateBoard();
        SwingUtilities.invokeLater(() -> {
            uiController.refresh(getPlayerTurn(), scores[0], scores[1]);
            uiController.repaintBoard();
        });
    }

    private void endGame(int[] scores) {
        String result
                = scores[0] > scores[1] ? "Vince il BIANCO!"
                        : scores[1] > scores[0] ? "Vince il NERO!"
                                : "PAREGGIO!";

        uiController.showGameOver(result, scores[0], scores[1]);
    }

    public boolean canSkip() {
        if (match == null) {
            return false;
        }
        for (Move m : getLegalMoves()) {
            if (m.skipMove()) {
                return true;
            }
        }
        return false;
    }

    public int[][] getBoard() {
        return match.getCurrentState().getBoard().getBoardMatrix();
    }

    public int getPlayerTurn() {
        return match.getCurrentPlayer();
    }

    @Override
    public void onMoveSelected(Move move) {

        AbstractAgent agent = agents[match.getCurrentPlayerIdx()];

        if (agent instanceof InputListenerAgent inputAgent) {
            inputAgent.onUserMove(move);
        }
    }

    public boolean saveState(String s){
        if(!s.endsWith(".txt")){
            s += ".txt";
        }
        Path path = Paths.get(s);
        int[][] board = this.getBoard();
        StringBuilder data = new StringBuilder();

        // prendo i nomi degli agenti
        data.append(agents[0].getAgentName()).append(" ").append(agents[1].getAgentName()).append("\n");
        data.append(this.getPlayerTurn()).append(" ").append(board.length).append("\n");
        data.append(match.getCurrentState().getLastMove()).append("\n");

        for (int[] row : board) {
            for (int slot : row) {
                data.append(slot).append(" ");
            }
            data.append("\n");
        }
        try {
            Files.write(path, data.toString().getBytes());
            return true; // successfully saved
        } catch (IOException e) {
            return false; // couldn't save
        }
    }

    public int loadState(String s){
        // the function return 0 if it worked properly, the saved game also begins
        // if the function returns -1 it means that the file could not be found
        // if the function returns -2 it means that the file contains wrongful data
        try {
            Scanner scanner = new Scanner(new File(s));
            String agent1Name = scanner.next();
            String agent2Name = scanner.next();


            int currentTurn = scanner.nextInt();
            if(currentTurn!=1 && currentTurn !=2){
                return -2;
            }
            int boardSize = scanner.nextInt();
            if(boardSize<4){
                return -2;
            }
            int x = scanner.nextInt();
            Move LastMove;
            if (x>=0){
                int y = scanner.nextInt();
                if (y >= boardSize || x >= boardSize){
                    return -2;
                }
                LastMove = new Move(x,y);
            }else if(x==-1){
                LastMove = new Move(true);
            }else {
                return -2;
            }

            int[][] board = new int[boardSize][boardSize];

            for(int i=0;i<boardSize;i++){
                for (int j=0;j<boardSize;j++){
                    board[i][j]= scanner.nextInt();
                    if (board[i][j]!= 0 && board[i][j] != 1 && board[i][j] != 2){
                        return -2;
                    }
                }
            }
            scanner.close();

            State newState = new State(new Board(board));

            int previousTurn = (currentTurn-1) - (currentTurn-2)*2;
            newState.applyMove(LastMove,previousTurn);

            AbstractAgent[] agentsArr = new AbstractAgent[]{
                    AgentFactory.create(agent1Name, 1),
                    AgentFactory.create(agent2Name, 2)
            };

            UIController uiController = UIController.getInstance();
            uiController.start(boardSize);

            this.uiController = uiController;
            this.agents = agentsArr;
            this.match = new Match(newState,currentTurn);

            uiController.setMoveInputListener(this);

            refreshUI();
            CompletableFuture.runAsync(this::startTurn);

        } catch (FileNotFoundException e) {
            return -1;
        } catch (InputMismatchException | IllegalArgumentException e){
            return -2;
        }


        return 0;
    }
}
