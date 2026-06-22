package sdm.freedom.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.io.File;
import java.util.Set;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import sdm.freedom.FileLoaderResult;
import sdm.freedom.GameController;
import sdm.freedom.UIController;
import sdm.freedom.agents.AbstractAgent;
import sdm.freedom.agents.AgentFactory;

public class MenuGUI extends JFrame {

    private static final Color BG_DARK = new Color(49, 46, 43);
    private static final Color BG_PANEL = new Color(60, 57, 54);
    private static final Color ACCENT = new Color(181, 136, 99);
    private static final Color TEXT_LIGHT = new Color(220, 220, 220);
    private static final Color BTN_FG = new Color(49, 46, 43);
    private final JSpinner boardSizeSpinner;
    private final JComboBox<String> agent1Combo;
    private final JComboBox<String> agent2Combo;
    private final JLabel failLoadMessage;


    public MenuGUI() {
        super("Freedom - Menu");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(BG_DARK);

        // main layout
        JPanel root = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS)); // vertical layout
        root.setBackground(BG_DARK);
        root.setBorder(new EmptyBorder(30, 40, 30, 40));

        // title and subtitle
        JLabel title = styledLabel("Freedom", new Font("Serif", Font.BOLD, 36), ACCENT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        root.add(title);
        root.add(Box.createRigidArea(new Dimension(0, 8)));

        JLabel subtitle = styledLabel("Configure your game", new Font("SansSerif", Font.PLAIN, 14), TEXT_LIGHT);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        root.add(subtitle);
        root.add(Box.createRigidArea(new Dimension(0, 24)));

        // config form
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(BG_PANEL);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT, 1, true),
                new EmptyBorder(20, 24, 20, 24)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // row 0 - board dimensions
        gbc.gridx = 0;
        gbc.gridy = 0;
        form.add(formLabel("Board Dimension:"), gbc);

        boardSizeSpinner = new JSpinner(new SpinnerNumberModel(8, 4, 20, 1));
        boardSizeSpinner.setFont(new Font("SansSerif", Font.PLAIN, 14));
        boardSizeSpinner.setPreferredSize(new Dimension(180, 32));
        gbc.gridx = 1;
        form.add(boardSizeSpinner, gbc);

        // row 1 - player 1 (white)
        Set<String> agents = AgentFactory.availableAgents();
        String[] agentNames = agents.toArray(new String[0]);

        gbc.gridx = 0;
        gbc.gridy = 1;
        form.add(formLabel("Player 1 (White):"), gbc);

        agent1Combo = styledCombo(agentNames);
        gbc.gridx = 1;
        form.add(agent1Combo, gbc);

        // row 2 - Player 2 (Black)
        gbc.gridx = 0;
        gbc.gridy = 2;
        form.add(formLabel("Player 2 (Black):"), gbc);

        agent2Combo = styledCombo(agentNames);
        gbc.gridx = 1;
        form.add(agent2Combo, gbc);

        form.setAlignmentX(Component.CENTER_ALIGNMENT);
        root.add(form);
        root.add(Box.createRigidArea(new Dimension(0, 20)));

        // play button
        JButton playButton = getJButton("Play");
        playButton.addActionListener(e -> onPlay());

        root.add(playButton);

        root.add(new JLabel(" "));

        // load game button
        JButton loadButton = getJButton("Load Game");
        failLoadMessage = styledLabel(" ", new Font("Serif", Font.PLAIN, 16), new Color(255, 10, 10));
        failLoadMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
        loadButton.addActionListener(e -> selectGameToLoad());
        root.add(loadButton);
        root.add(new JLabel(" "));
        root.add(failLoadMessage);
        root.add(new JLabel(" "));
        setContentPane(root);
        pack();
        setLocationRelativeTo(null);
    }

    private JButton getJButton(String buttonName) {
        // method used to create and style the load and play buttons

        JButton playButton = new JButton(buttonName) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = getModel().isPressed() ? ACCENT.darker()
                        : getModel().isRollover() ? ACCENT.brighter() : ACCENT;
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        playButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        playButton.setForeground(BTN_FG);
        playButton.setContentAreaFilled(false);
        playButton.setFocusPainted(false);
        playButton.setBorderPainted(false);
        playButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        playButton.setPreferredSize(new Dimension(200, 48));
        playButton.setMaximumSize(new Dimension(200, 48));
        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        return playButton;
    }

    private void selectGameToLoad() {
        // method to select a savefile to load
        String home = System.getProperty("user.home"); // users home directory

        File saveDir = new File(home, ".freedom/saves");// save file directory

        JFileChooser fileChooser = new JFileChooser(saveDir); // start the file chooser
        fileChooser.setFileFilter(
                new javax.swing.filechooser.FileNameExtensionFilter(
                        "Save Files (*.dat)", "dat"
                )
        );
        // get the chooser response
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            // try to load the game
            FileLoaderResult message = GameController.getInstance().loadState(file.getAbsolutePath());

            switch (message) {
                case SUCCESS:
                    // dispose of the menuUI if loading finished successfully
                    dispose();
                    break;
                case FILE_NOT_FOUND:
                    failLoadMessage.setText("File not found");
                    break;
                case PARSE_ERROR:
                    failLoadMessage.setText("File could not be interpreted");
                    break;
            }
        }
    }

    // start the game using the selected configurations, then closes the menu
    private void onPlay() {
        int boardSize = (int) boardSizeSpinner.getValue();
        String type1 = (String) agent1Combo.getSelectedItem();
        String type2 = (String) agent2Combo.getSelectedItem();

        AbstractAgent[] agentsArr = new AbstractAgent[]{
                AgentFactory.create(type1, 1),
                AgentFactory.create(type2, 2)
        };

        // close the menu
        dispose();

        // start the game
        UIController uiController = UIController.getInstance();
        uiController.start(boardSize);

        // delay the initialization of the GameController to ensure that the UI is ready
        SwingUtilities.invokeLater(()
                -> GameController.getInstance().initialize(boardSize, uiController, agentsArr)
        );

    }

    private JLabel formLabel(String text) {
        return styledLabel(text, new Font("SansSerif", Font.BOLD, 14), TEXT_LIGHT);
    }

    private JLabel styledLabel(String text, Font font, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(font);
        l.setForeground(color);
        return l;
    }

    private JComboBox<String> styledCombo(String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        combo.setPreferredSize(new Dimension(180, 32));
        return combo;
    }
}