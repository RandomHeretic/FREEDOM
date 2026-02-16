package sdm.freedom;

import javax.swing.SwingUtilities;

import sdm.freedom.gui.GameGUI;

public class UI {

    private static UI instance;

    private Match currentMatch; // logica del gioco
    private GameGUI gameWindow; // finestra di gioco
    private boolean gameOver = false; // flag per bloccare i click a fine partita

    private UI() {
    }

    public static UI getInstance() {
        if (instance == null) {
            instance = new UI();
        }
        return instance;
    }

    // quanto grande disegnare la griglia
    public void startGUI(int n) {
        // inizializza la partita
        currentMatch = new Match(n);
        gameOver = false;
        // assicura che la grafica venga creata nel Thread dedicato alla grafica
        SwingUtilities.invokeLater(() -> {
            gameWindow = new GameGUI(n);
            gameWindow.setVisible(true);
        });
    }

    public int getPieceAt(int row, int col) {
        if (currentMatch == null) {
            return 0;
        }
        return currentMatch.giveCurrentState().giveBoardPosition(row, col);
    }

    // restituisce le mosse legali per evidenziarle nella GUI
    public Move[] getLegalMoves() {
        if (currentMatch == null) {
            return new Move[0];
        }
        return currentMatch.giveCurrentState().getLegalSuccessors();
    }

    // restituisce il giocatore corrente (1=bianco, 2=nero)
    public int getCurrentPlayer() {
        if (currentMatch == null) {
            return 1;
        }
        return currentMatch.getCurrentPlayer();
    }

    // la partita è finita?
    public boolean isGameOver() {
        return gameOver;
    }

    // quando l'utente clicca
    public void tryMove(int row, int col) {
        if (currentMatch == null || gameOver) {
            return;
        }

        Move move = new Move(row, col);

        if (currentMatch.checkValidMove(move)) {

            currentMatch.applyAMove(move);

            // calcola i punteggi -> metodo Board.java
            int[] scores = currentMatch.evaluateBoard();
            int whiteScore = scores[0];
            int blackScore = scores[1];

            // di chi è il turno ora
            int currentPlayer = currentMatch.getCurrentPlayer();

            // aggiorna la GUI
            gameWindow.refresh(currentPlayer, whiteScore, blackScore);

            // controlla se la partita è finita
            if (currentMatch.giveCurrentState().isTerminal()) {
                gameOver = true;
                String risultato;
                if (whiteScore > blackScore) {
                    risultato = "Vince il BIANCO!";
                } else if (blackScore > whiteScore) {
                    risultato = "Vince il NERO!";
                } else {
                    risultato = "PAREGGIO!";
                }
                gameWindow.showGameOver(risultato, whiteScore, blackScore);
            }

        } else {
            System.out.println("Mossa non valida!");
        }
    }
}
