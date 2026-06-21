package sdm.freedom.gui;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import sdm.freedom.GameController;
import sdm.freedom.Move;
import sdm.freedom.UIController;

public class InfoPanel extends JPanel {

    private final JLabel turnLabel;
    private final JLabel whiteScoreLabel;
    private final JLabel blackScoreLabel;
    private final JLabel resultLabel;
    private final JTextField saveName;
    private final JButton saveButton;
    private final JLabel saveResultLabel;
    private final JButton skipButton;
    private final JButton menuButton;


    public InfoPanel() {
        // metto gli elementi uno sotto l'altro
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // estetics del pannello laterale
        setBackground(Color.LIGHT_GRAY);
        setPreferredSize(new Dimension(250, 0));
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

        String saveText = "test";
        saveName = new JTextField(saveText);
        saveName.setFont(new Font("SansSerif", Font.PLAIN, 14));
        saveName.setPreferredSize(new Dimension(320, 32));
        saveName.setMaximumSize(saveName.getPreferredSize());
        saveButton = getJButton("Save");

        saveButton.addActionListener(e -> this.tryToSaveState());
        
        saveResultLabel= new JLabel("");

        // bottone Skip -> appare solo quando è l'ultima cella
        // fix bottone Mac-> creo bottone custom che si dipinge da solo per evitare il look nativo bianco del Mac
        skipButton = new JButton("Skip Move") {
            @Override
            protected void paintComponent(Graphics g) {
                // colore scuso se pushed
                if (getModel().isPressed()) {
                    g.setColor(getBackground().darker());
                } else {
                    g.setColor(getBackground());
                }

                g.fillRect(0, 0, getWidth(), getHeight());

                super.paintComponent(g);
            }
        };

        skipButton.setFont(new Font("Arial", Font.BOLD, 16));
        skipButton.setBackground(new Color(200, 80, 80));
        skipButton.setForeground(Color.WHITE);

        // disattiva stile nativo Mac
        skipButton.setContentAreaFilled(false);
        skipButton.setFocusPainted(false);
        skipButton.setBorderPainted(false);
        skipButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        skipButton.setPreferredSize(new Dimension(160, 45));  // dimensione fissa
        skipButton.setMaximumSize(new Dimension(160, 45));
        skipButton.setVisible(false); // nascosto finché non serve

        // push -> Singleton esegue skip
        skipButton.addActionListener(e -> UIController.getInstance().userClickedForMove(new Move(true)));

        // bottone Menu -> torna al menu principale
        menuButton = getJButton("Menu");

        menuButton.addActionListener(e -> UIController.getInstance().backToMenu());

        add(turnLabel);
        add(new JLabel(" "));
        add(new JLabel(" "));
        add(whiteScoreLabel);
        add(new JLabel(" "));
        add(blackScoreLabel);
        add(new JLabel(" "));
        add(new JLabel(" "));
        add(resultLabel);
        add(new JLabel(" "));
        add(saveName);
        add(new JLabel(" "));
        add(saveButton);
        add(new JLabel(" "));
        add(saveResultLabel);
        add(new JLabel(" "));
        add(Box.createVerticalGlue()); // bottone in fondo pannello
        add(skipButton);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(menuButton);
        add(Box.createRigidArea(new Dimension(0, 20))); // margine sotto bottone
    }

    private JButton getJButton(String buttonText) {
        JButton saveButton = new JButton(buttonText){
            @Override
            protected void paintComponent(Graphics g) {
                if (getModel().isPressed()) {
                    g.setColor(getBackground().darker());
                } else {
                    g.setColor(getBackground());
                }
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        saveButton.setFont(new Font("Arial", Font.BOLD, 16));
        saveButton.setBackground(new Color(80, 80, 200));
        saveButton.setForeground(Color.WHITE);
        saveButton.setContentAreaFilled(false);
        saveButton.setFocusPainted(false);
        saveButton.setBorderPainted(false);
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveButton.setPreferredSize(new Dimension(160, 45));
        saveButton.setMaximumSize(new Dimension(160, 45));
        return saveButton;
    }

    private void tryToSaveState(){
        String fileName = saveName.getText();
        if(!fileName.endsWith(".txt")){
            fileName += ".txt";
        }
        if (GameController.getInstance().saveState(fileName)){
            saveResultLabel.setText("File " + fileName + " saved successfully");
            saveResultLabel.setForeground(new Color(10,150,10));
        }else {
            saveResultLabel.setText("File " + fileName + " couldn't be saved");
            saveResultLabel.setForeground(new Color(150,10,10));
        }
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

    // mostra o nasconde il bottone skip
    public void setSkipVisible(boolean visible) {
        skipButton.setVisible(visible);
    }

    public void showGameOver(String risultato, int whiteScore, int blackScore) {
        turnLabel.setText("FINE PARTITA");
        turnLabel.setForeground(new Color(180, 0, 0));
        whiteScoreLabel.setText("Bianchi: " + whiteScore);
        blackScoreLabel.setText("Neri: " + blackScore);
        skipButton.setVisible(false);
        resultLabel.setText(risultato);
        resultLabel.setVisible(true);
    }
}
