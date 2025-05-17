package model.view;

import model.Game;
import model.GameCycle;
import model.event.GameEvent;
import model.event.GameListener;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GamePanel extends JFrame {

    private static final int HEIGHT = 560;
    private static final int WIDTH = 680;

    private final JLabel _hpLabel;
    private final JLabel _timerLabel;
    private final Game _game = new Game();
    private final FieldPanel _fieldPanel = new FieldPanel(_game);
    private final GameCycle _cycle;
    private JButton _pauseButton;
    private JPanel _buttonPanel;

    public GamePanel() {
        setTitle("Bomberman");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        _cycle = new GameCycle(_game, this);
        _hpLabel = new JLabel("Жизни: -");
        _timerLabel = new JLabel("Время: -");

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(buildStatisticPanel(), BorderLayout.NORTH);
        mainPanel.add(buildGameLayeredPane(), BorderLayout.CENTER);

        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
        _fieldPanel.requestFocusInWindow();

        _game.addListener(new GameObserver());
    }

    private JPanel buildStatisticPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(WIDTH, 40));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT));
        left.add(_hpLabel);

        JPanel center = new JPanel();
        center.add(_timerLabel);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        _pauseButton = createButton("Pause", e -> togglePause());
        _pauseButton.setVisible(false);
        right.add(_pauseButton);

        panel.add(left, BorderLayout.WEST);
        panel.add(center, BorderLayout.CENTER);
        panel.add(right, BorderLayout.EAST);

        return panel;
    }

    private JLayeredPane buildGameLayeredPane() {
        _fieldPanel.addKeyListener(_game.getField().getBomberman().getInput());
        _fieldPanel.setFocusable(true);

        Dimension size = _fieldPanel.getPreferredSize();
        _fieldPanel.setBounds(0, 0, size.width, size.height);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(size);
        layeredPane.add(_fieldPanel, JLayeredPane.DEFAULT_LAYER);

        _buttonPanel = createMenuPanel();
        layeredPane.add(_buttonPanel, JLayeredPane.PALETTE_LAYER);

        return layeredPane;
    }

    private JPanel createMenuPanel() {
        return createButtonPanel(List.of(
                new ButtonConfig("Start", e -> startGame()),
                new ButtonConfig("Exit", e -> exitGame())
        ));
    }

    private JPanel createPausePanel() {
        return createButtonPanel(List.of(
                new ButtonConfig("Continue", e -> continueGame()),
                new ButtonConfig("Menu", e -> toggleMenu())
        ));
    }

    private JPanel createEndGamePanel(String message) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(200, 200, 200));
        panel.setOpaque(true);

        JLabel label = new JLabel(message);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setForeground(Color.BLACK);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton retry = createButton("Retry", e -> {
            _game.restart();
            _cycle.start();
            _fieldPanel.requestFocusInWindow();
            removePanel();
            _pauseButton.setVisible(true);
        });

        JButton menu = createButton("Menu", e -> {
            removePanel();
            toggleMenu();
        });

        panel.add(Box.createVerticalGlue());
        panel.add(label);
        panel.add(Box.createVerticalStrut(20));
        panel.add(retry);
        panel.add(Box.createVerticalStrut(10));
        panel.add(menu);
        panel.add(Box.createVerticalGlue());

        panel.setBounds(centerBounds(120, 150));
        return panel;
    }

    private JPanel createButtonPanel(List<ButtonConfig> buttons) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        for (ButtonConfig config : buttons) {
            JButton button = createButton(config.text, config.listener);
            panel.add(button);
            panel.add(Box.createVerticalStrut(10));
        }

        panel.setBounds(centerBounds(panel.getPreferredSize()));
        return panel;
    }

    private JButton createButton(String text, java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        Dimension buttonSize = new Dimension(120, 25);
        button.setPreferredSize(buttonSize);
        button.setMaximumSize(buttonSize);
        button.setMinimumSize(buttonSize);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(action);
        return button;
    }

    private Rectangle centerBounds(int width, int height) {
        Dimension size = _fieldPanel.getPreferredSize();
        int x = (size.width - width) / 2;
        int y = (size.height - height) / 2;
        return new Rectangle(x, y, width, height);
    }

    private Rectangle centerBounds(Dimension panelSize) {
        return centerBounds(panelSize.width, panelSize.height);
    }

    private void toggleMenu() {
        _game.exit();
        switchPanel(createMenuPanel());
        _pauseButton.setVisible(false);
        repaint();
    }

    private void continueGame() {
        _game.continueGame();
        _fieldPanel.requestFocusInWindow();
        removePanel();
        _pauseButton.setVisible(true);
    }

    private void togglePause() {
        _game.stop();
        switchPanel(createPausePanel());
        _pauseButton.setVisible(false);
    }

    private void startGame() {
        _fieldPanel.requestFocusInWindow();
        _game.start();
        _cycle.start();
        removePanel();
        _pauseButton.setVisible(true);
    }

    private void exitGame() {
        _cycle.stop();
        _game.exit();
        System.exit(0);
    }

    private void switchPanel(JPanel newPanel) {
        removePanel();
        _buttonPanel = newPanel;
        _fieldPanel.getParent().add(_buttonPanel, JLayeredPane.PALETTE_LAYER);
        _buttonPanel.revalidate();
        _buttonPanel.repaint();
    }

    private void removePanel() {
        if (_buttonPanel != null) {
            _fieldPanel.getParent().remove(_buttonPanel);
            _buttonPanel = null;
        }
    }

    public void updateStatistic() {
        _hpLabel.setText("Жизни: " + _game.getField().getBomberman().getHealthPoint());
        _timerLabel.setText("Время: " + _game.getTimer());
    }

    private void showEndGamePanel(String message) {
        _cycle.stop();
        _game.stop();
        switchPanel(createEndGamePanel(message));
        _pauseButton.setVisible(false);
    }

    public FieldPanel getFieldPanel() {
        return _fieldPanel;
    }

    private class GameObserver implements GameListener {
        @Override
        public void onVictory(GameEvent event) {
            showEndGamePanel("Victory!");
        }

        @Override
        public void onDefeat(GameEvent event) {
            showEndGamePanel("Defeat!");
        }
    }

    private record ButtonConfig(String text, java.awt.event.ActionListener listener) {}
}
