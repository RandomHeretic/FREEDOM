package sdm.freedom; // Assicurati che il package sia corretto

import javax.swing.JFrame;
import javax.swing.WindowConstants;

// eredita tutte le caratteristiche di JFrame (finestra)
public class GameGUI extends JFrame {
    
    public GameGUI(int n) {
        super("Freedom"); 

        // chiude tutto il programma alla chiusura della finestra
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // passiamo n perché il pannello deve sapere quante righe/colonne disegnare
        BoardPanel panel = new BoardPanel(n);
        this.add(panel);

        // serve per restringere la finestra di dimensione almeno uguale al contenuto 
        // TODO forse potrei toglierlo
        pack(); 
        
        // centra la finestra
        setLocationRelativeTo(null);
    }
}