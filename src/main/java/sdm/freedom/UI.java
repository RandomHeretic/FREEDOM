package sdm.freedom;

import javax.swing.SwingUtilities;

public class UI {
    private static UI instance;

    private UI(){}

    public static UI getInstance(){
        if(instance == null){
            instance = new UI();
        }
        return instance;
    }

    // Riferimento alla finestra di gioco
    private GameGUI gameWindow;

    // Prende 'n' per sapere quanto grande disegnare la griglia
    public void startGUI(int n) {
        // Assicura che la grafica venga creata nel "Thread" dedicato alla grafica
        SwingUtilities.invokeLater(() -> {
            gameWindow = new GameGUI(n);
            gameWindow.setVisible(true);
        });
    }
}