package sdm.freedom.gui;

import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    private final JLabel saveResultLabel;
    private final JButton skipButton;


    public InfoPanel() {
        // putting the elements one under the other
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // side panel estetics
        setBackground(Color.LIGHT_GRAY);
        setPreferredSize(new Dimension(250, 0));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        //  labels
        Font fontTitolo = new Font("Arial", Font.BOLD, 18);
        Font fontTesto = new Font("Arial", Font.PLAIN, 16);
        Font fontRisultato = new Font("Arial", Font.BOLD, 20);

        turnLabel = new JLabel("Turn: WHITE");
        turnLabel.setFont(fontTitolo);
        turnLabel.setForeground(Color.BLUE);

        whiteScoreLabel = new JLabel("White: 0");
        whiteScoreLabel.setFont(fontTesto);

        blackScoreLabel = new JLabel("Black: 0");
        blackScoreLabel.setFont(fontTesto);

        // result label, hidden until the game ends
        resultLabel = new JLabel("");
        resultLabel.setFont(fontRisultato);
        resultLabel.setForeground(new Color(180, 0, 0));
        resultLabel.setVisible(false);

        // automatic of the savefile
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmm");
        String timestamp = LocalDateTime.now().format(formatter);
        String saveText = "Save"+timestamp;
        // savefile textbox to change the name if needed
        saveName = new JTextField(saveText);
        saveName.setFont(new Font("SansSerif", Font.PLAIN, 14));
        saveName.setPreferredSize(new Dimension(320, 32));
        saveName.setMaximumSize(saveName.getPreferredSize());

        // save button -> save the current state, in case of failure display such
        JButton saveButton = getJButton("Save");

        saveButton.addActionListener(e -> this.tryToSaveState());
        
        saveResultLabel= new JLabel("");

        // Skip button -> appears only when it's possible to skip
        // fix button Mac-> custom button that colours itself if on MACOS to replace native white colour
        skipButton = new JButton("Skip Move") {
            @Override
            protected void paintComponent(Graphics g) {
                // darker colour if pushed
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

        // deactivates native Mac style
        skipButton.setContentAreaFilled(false);
        skipButton.setFocusPainted(false);
        skipButton.setBorderPainted(false);
        skipButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        skipButton.setPreferredSize(new Dimension(160, 45));  // fixed dimension
        skipButton.setMaximumSize(new Dimension(160, 45));
        skipButton.setVisible(false); // hidden until needed

        // push -> Singleton executes skip
        skipButton.addActionListener(e -> UIController.getInstance().userClickedForMove(new Move(true)));

        // Menu button -> goes back to main menu
        JButton menuButton = getJButton("Menu");

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
        add(Box.createVerticalGlue()); // button on the bottom of the panel
        add(skipButton);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(menuButton);
        add(Box.createRigidArea(new Dimension(0, 20))); // bottom margin
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
        String fileName = saveName.getText(); // get the savefile name from the form
        if(!fileName.endsWith(".dat")){
            fileName += ".dat";
        }

        // get the directory where I'm going to save the files
        String home = System.getProperty("user.home");
        File saveDir = new File(home, ".freedom/saves");
        boolean saveResult;
        try{
            Files.createDirectories(saveDir.toPath());// used to make sure the directory exists
            saveResult = GameController.getInstance().saveState(fileName,saveDir); // try to save
        }catch (Exception e){
            saveResult = false; // if something goes wrong I don't have the directory thus I can't save
        }

        if (saveResult){// display a message according to success or failure
            saveResultLabel.setText("File " + fileName + " saved successfully");
            saveResultLabel.setForeground(new Color(10,150,10));
        }else {
            saveResultLabel.setText("File " + fileName + " couldn't be saved");
            saveResultLabel.setForeground(new Color(150,10,10));
        }
    }

    // update texts
    public void updateInfo(int currentPlayer, int whiteScore, int blackScore) {
        if (currentPlayer == 1) {
            turnLabel.setText("Turn: WHITE");
            turnLabel.setForeground(Color.BLACK);
        } else {
            turnLabel.setText("Turn: BLACK");
            turnLabel.setForeground(Color.RED);
        }

        whiteScoreLabel.setText("White: " + whiteScore);
        blackScoreLabel.setText("Black: " + blackScore);
    }

    // hides or shows the skip button
    public void setSkipVisible(boolean visible) {
        skipButton.setVisible(visible);
    }

    public void showGameOver(String risultato, int whiteScore, int blackScore) {
        turnLabel.setText("END OF THE GAME");
        turnLabel.setForeground(new Color(180, 0, 0));
        whiteScoreLabel.setText("White: " + whiteScore);
        blackScoreLabel.setText("Black: " + blackScore);
        skipButton.setVisible(false);
        resultLabel.setText(risultato);
        resultLabel.setVisible(true);
    }
}
