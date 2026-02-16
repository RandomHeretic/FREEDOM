package sdm.freedom.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class InfoPanel extends JPanel {

    private final JLabel turnLabel;
    private final JLabel whiteScoreLabel;
    private final JLabel blackScoreLabel;
    private final JLabel resultLabel;

    public InfoPanel() {
        // metto gli elementi uno sotto l'altro
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // estetics del pannello laterale
        setBackground(Color.LIGHT_GRAY);
        setPreferredSize(new Dimension(200, 0));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // etichette label
        Font fontTitolo = new Font("Arial", Font.BOLD, 18);
        Font fontTesto = new Font("Arial", Font.PLAIN, 16);
        Font fontRisultato = new Font("Arial", Font.BOLD, 20);

        turnLabel = new JLabel("Turno: BIANCO");
        turnLabel.setFont(fontTitolo);
        turnLabel.setForeground(Color.BLUE);

        whiteScoreLabel = new JLabel("Bianchi: 0");
        whiteScoreLabel.setFont(fontTesto);

        blackScoreLabel = new JLabel("Neri: 0");
        blackScoreLabel.setFont(fontTesto);

        // label risultato, nascosta finché la partita non finisce
        resultLabel = new JLabel("");
        resultLabel.setFont(fontRisultato);
        resultLabel.setForeground(new Color(180, 0, 0));
        resultLabel.setVisible(false);

        add(turnLabel);
        add(new JLabel(" "));
        add(new JLabel(" "));
        add(whiteScoreLabel);
        add(new JLabel(" "));
        add(blackScoreLabel);
        add(new JLabel(" "));
        add(new JLabel(" "));
        add(resultLabel);
    }

    // per aggiornare i testi
    public void updateInfo(int currentPlayer, int whiteScore, int blackScore) {
        if (currentPlayer == 1) {
            turnLabel.setText("Turno: BIANCO");
            turnLabel.setForeground(Color.BLACK);
        } else {
            turnLabel.setText("Turno: NERO");
            turnLabel.setForeground(Color.RED);
        }

        whiteScoreLabel.setText("Bianchi: " + whiteScore);
        blackScoreLabel.setText("Neri: " + blackScore);
    }

    public void showGameOver(String risultato, int whiteScore, int blackScore) {
        turnLabel.setText("FINE PARTITA");
        turnLabel.setForeground(new Color(180, 0, 0));
        whiteScoreLabel.setText("Bianchi: " + whiteScore);
        blackScoreLabel.setText("Neri: " + blackScore);
        resultLabel.setText(risultato);
        resultLabel.setVisible(true);
    }
}
