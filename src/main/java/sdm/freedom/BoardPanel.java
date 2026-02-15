package sdm.freedom;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

// JPanel è una superficie vuota su cui disegnare
public class BoardPanel extends JPanel {
    
    private final int boardSize; // dimensione della griglia
    private final int CELL_SIZE = 80; // quanto è grande ogni quadrato in pixel
    private final int MARGIN = 100; // spazio ai bordi

    public BoardPanel(int n) {
        this.boardSize = n;
        
        // calcoliamo quanto deve essere grande il pannello in pixel
        int pixelWidth = (n * CELL_SIZE) + (MARGIN * 2);
        int pixelHeight = (n * CELL_SIZE) + (MARGIN * 2);
        
        // impostiamo la dimensione preferita (x pack())
        setPreferredSize(new Dimension(pixelWidth, pixelHeight));
        
        // impostiamo un colore di sfondo (marroncino tipo legno -> 220, 180, 130)
        setBackground(new Color(220, 180, 130)); 
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Pulisce il disegno precedente

        // Impostiamo il colore per le linee (Nero)
        g.setColor(Color.BLACK);

        // Disegniamo la griglia
        for (int i = 0; i < boardSize; i++) {
            // Disegna linee orizzontali
            g.drawLine(
                MARGIN,                     // x inizio
                MARGIN + (i * CELL_SIZE),   // y inizio
                MARGIN + ((boardSize - 1) * CELL_SIZE), // x fine
                MARGIN + (i * CELL_SIZE)    // y fine
            );

            // Disegna linee verticali
            g.drawLine(
                MARGIN + (i * CELL_SIZE),   // x inizio
                MARGIN,                     // y inizio
                MARGIN + (i * CELL_SIZE),   // x fine
                MARGIN + ((boardSize - 1) * CELL_SIZE) // y fine
            );
        }
    }
}