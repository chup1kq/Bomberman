package model.view;

import javax.swing.*;
import java.awt.*;

import model.Game;
import model.field.Cell;

public class FieldPanel extends JPanel {

    private static final int CELL_SIZE = (int)Cell.getDefaultSize().getWidth();

    private Game _game;

    public FieldPanel(Game game) {
        _game = game;
        setPreferredSize(new Dimension(17 * CELL_SIZE, 13 * CELL_SIZE));
        setBackground(Color.BLACK);
        repaint();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (_game == null) return;
        Graphics2D g2 = (Graphics2D) g;
        _game.render(g2);
    }
}
