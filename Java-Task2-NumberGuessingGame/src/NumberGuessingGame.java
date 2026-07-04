import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.Random;

public class NumberGuessingGame extends JFrame {

    // Game variables
    private int numberToGuess;
    private int attempts;
    private int maxAttempts;
    private int maxRange;
    private int totalScore = 0;
    private int roundsPlayed = 0;
    private Random random = new Random();

    // Palette
    private final Color GRAD_TOP = new Color(102, 84, 220);
    private final Color GRAD_BOTTOM = new Color(60, 130, 220);
    private final Color GLASS_FILL = new Color(255, 255, 255, 60);
    private final Color GLASS_BORDER = new Color(255, 255, 255, 110);
    private final Color TEXT_LIGHT = new Color(255, 255, 255);
    private final Color TEXT_MUTED = new Color(235, 235, 250, 200);
    private final Color ACCENT_GREEN = new Color(76, 209, 129);
    private final Color ACCENT_ORANGE = new Color(255, 159, 67);
    private final Color ACCENT_RED = new Color(255, 90, 95);
    private final Color ACCENT_BLUE = new Color(88, 145, 255);

    // Components
    private JLabel titleLabel, subtitleLabel, resultLabel, attemptsLabel, scoreLabel;
    private JTextField guessField;
    private JButton startButton, guessButton, playAgainButton;
    private JComboBox<String> difficultyBox;
    private GlassProgressBar attemptsBar;

    public NumberGuessingGame() {
        setTitle("Number Guessing Game");
        setSize(460, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        GradientBackgroundPanel background = new GradientBackgroundPanel();
        background.setLayout(new GridBagLayout());
        setContentPane(background);

        GlassPanel card = buildGlassCard();
        GridBagConstraints gbc = new GridBagConstraints();
        background.add(card, gbc);

        setVisible(true);
    }

    // ---------- BACKGROUND ----------
    private class GradientBackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint gp = new GradientPaint(0, 0, GRAD_TOP, getWidth(), getHeight(), GRAD_BOTTOM);
            g2.setPaint(gp);
            g2.fillRect(0, 0, getWidth(), getHeight());

            // soft decorative circles for depth
            g2.setColor(new Color(255, 255, 255, 25));
            g2.fillOval(-80, -80, 260, 260);
            g2.fillOval(getWidth() - 150, getHeight() - 150, 260, 260);
            g2.dispose();
        }
    }

    // ---------- GLASS CARD ----------
    private class GlassPanel extends JPanel {
        GlassPanel() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Drop shadow layers
            for (int i = 6; i > 0; i--) {
                g2.setColor(new Color(0, 0, 0, 6));
                g2.fill(new RoundRectangle2D.Float(i, i + 3, getWidth() - i * 2, getHeight() - i * 2, 32, 32));
            }

            // Frosted glass fill
            g2.setColor(GLASS_FILL);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 32, 32));

            // Subtle top highlight (glass shine)
            GradientPaint shine = new GradientPaint(0, 0, new Color(255, 255, 255, 90), 0, getHeight() / 2f, new Color(255, 255, 255, 0));
            g2.setPaint(shine);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight() / 2f, 32, 32));

            // Border
            g2.setColor(GLASS_BORDER);
            g2.setStroke(new BasicStroke(1.4f));
            g2.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, 32, 32));

            g2.dispose();
            super.paintComponent(g);
        }
    }

    private GlassPanel buildGlassCard() {
        GlassPanel card = new GlassPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(28, 30, 28, 30));
        card.setPreferredSize(new Dimension(380, 520));

        titleLabel = new JLabel("🎯  NUMBER GUESSING GAME");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 19));
        titleLabel.setForeground(TEXT_LIGHT);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        subtitleLabel = new JLabel("Select difficulty and click Start");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(TEXT_MUTED);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitleLabel.setBorder(new EmptyBorder(4, 0, 20, 0));

        JLabel diffLabel = smallLabel("DIFFICULTY");
        diffLabel.setBorder(new EmptyBorder(0, 0, 6, 0));

        difficultyBox = new JComboBox<>(new String[]{"Easy  (1–50)", "Medium  (1–100)", "Hard  (1–200)"});
        difficultyBox.setSelectedIndex(1);
        difficultyBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        difficultyBox.setMaximumSize(new Dimension(400, 38));
        difficultyBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        difficultyBox.setFocusable(false);
        difficultyBox.setBorder(new EmptyBorder(4, 8, 4, 8));

        startButton = glassButton("Start New Game", ACCENT_BLUE);
        startButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        startButton.setMaximumSize(new Dimension(400, 44));
        startButton.setBorder(new EmptyBorder(16, 0, 0, 0));
        startButton.addActionListener(e -> startNewGame());

        JLabel guessLabel = smallLabel("YOUR GUESS");
        guessLabel.setBorder(new EmptyBorder(22, 0, 6, 0));

        guessField = new JTextField();
        guessField.setFont(new Font("Segoe UI", Font.BOLD, 20));
        guessField.setHorizontalAlignment(JTextField.CENTER);
        guessField.setMaximumSize(new Dimension(400, 46));
        guessField.setAlignmentX(Component.LEFT_ALIGNMENT);
        guessField.setOpaque(false);
        guessField.setBackground(new Color(255, 255, 255, 60));
        guessField.setForeground(TEXT_LIGHT);
        guessField.setCaretColor(TEXT_LIGHT);
        guessField.setBorder(BorderFactory.createCompoundBorder(
                new GlassFieldBorder(),
                new EmptyBorder(6, 12, 6, 12)
        ));
        guessField.setEnabled(false);
        guessField.addActionListener(e -> checkGuess());

        guessButton = glassButton("Guess", ACCENT_GREEN);
        guessButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        guessButton.setMaximumSize(new Dimension(400, 44));
        guessButton.setBorder(new EmptyBorder(12, 0, 0, 0));
        guessButton.setEnabled(false);
        guessButton.addActionListener(e -> checkGuess());

        resultLabel = new JLabel(" ");
        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        resultLabel.setForeground(TEXT_LIGHT);
        resultLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        resultLabel.setBorder(new EmptyBorder(20, 0, 8, 0));

        attemptsLabel = smallLabel("Attempts left: —");

        attemptsBar = new GlassProgressBar();
        attemptsBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        attemptsBar.setMaximumSize(new Dimension(400, 10));
        attemptsBar.setPreferredSize(new Dimension(400, 10));
        attemptsBar.setBorder(new EmptyBorder(6, 0, 0, 0));

        playAgainButton = glassButton("Play Again", ACCENT_ORANGE);
        playAgainButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        playAgainButton.setMaximumSize(new Dimension(400, 44));
        playAgainButton.setBorder(new EmptyBorder(22, 0, 0, 0));
        playAgainButton.setEnabled(false);
        playAgainButton.addActionListener(e -> startNewGame());

        scoreLabel = new JLabel("🏆 Score: 0    |    Rounds: 0");
        scoreLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        scoreLabel.setForeground(TEXT_MUTED);
        scoreLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        scoreLabel.setBorder(new EmptyBorder(14, 0, 0, 0));

        card.add(titleLabel);
        card.add(subtitleLabel);
        card.add(diffLabel);
        card.add(difficultyBox);
        card.add(startButton);
        card.add(guessLabel);
        card.add(guessField);
        card.add(guessButton);
        card.add(resultLabel);
        card.add(attemptsLabel);
        card.add(attemptsBar);
        card.add(playAgainButton);
        card.add(scoreLabel);

        return card;
    }

    private JLabel smallLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));
        label.setForeground(TEXT_MUTED);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    // ---------- GLASS BUTTON ----------
    private JButton glassButton(String text, Color accent) {
        JButton button = new JButton(text) {
            private boolean hover = false;

            {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        hover = true;
                        repaint();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        hover = false;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Always solid, prominent fill (slightly dimmer when disabled, never faint)
                int alphaFill = hover ? 255 : (isEnabled() ? 230 : 170);
                Color fill = new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), alphaFill);
                g2.setColor(fill);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));

                // glass shine on top half
                GradientPaint shine = new GradientPaint(0, 0, new Color(255, 255, 255, 80), 0, getHeight(), new Color(255, 255, 255, 0));
                g2.setPaint(shine);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight() / 2f, 16, 16));

                // Solid border always visible
                g2.setColor(new Color(255, 255, 255, 160));
                g2.setStroke(new BasicStroke(1.4f));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth() - 1, getHeight() - 1, 16, 16));

                // Manually paint the text ourselves so it stays bright white
                // even when the button is disabled (Swing normally fades disabled text to gray)
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                String text = getText();
                int textWidth = fm.stringWidth(text);
                int textX = (getWidth() - textWidth) / 2;
                int textY = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();

                g2.setColor(isEnabled() ? Color.WHITE : new Color(255, 255, 255, 220));
                g2.drawString(text, textX, textY);

                g2.dispose();
            }
        };
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    // ---------- GLASS TEXTFIELD BORDER ----------
    private class GlassFieldBorder extends AbstractBorder {
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(255, 255, 255, 130));
            g2.setStroke(new BasicStroke(1.4f));
            g2.draw(new RoundRectangle2D.Float(x, y, width - 1, height - 1, 12, 12));
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(4, 8, 4, 8);
        }
    }

    // ---------- GLASS PROGRESS BAR (custom) ----------
    private class GlassProgressBar extends JComponent {
        private int max = 7;
        private int value = 7;
        private Color fillColor = ACCENT_GREEN;

        void setMaximum(int m) {
            max = m;
        }

        void setValue(int v) {
            value = v;
            repaint();
        }

        void setForegroundColor(Color c) {
            fillColor = c;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(255, 255, 255, 60));
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));

            float ratio = max == 0 ? 0 : (float) value / max;
            int fillWidth = Math.round(getWidth() * Math.max(ratio, 0));
            g2.setColor(fillColor);
            g2.fill(new RoundRectangle2D.Float(0, 0, fillWidth, getHeight(), 8, 8));

            g2.dispose();
        }
    }

    // ---------- GAME LOGIC ----------
    private void startNewGame() {
        String selected = (String) difficultyBox.getSelectedItem();
        if (selected.startsWith("Easy")) {
            maxRange = 50;
            maxAttempts = 7;
        } else if (selected.startsWith("Medium")) {
            maxRange = 100;
            maxAttempts = 7;
        } else {
            maxRange = 200;
            maxAttempts = 10;
        }

        numberToGuess = random.nextInt(maxRange) + 1;
        attempts = 0;

        subtitleLabel.setText("Guess a number between 1 and " + maxRange);
        resultLabel.setText(" ");
        attemptsLabel.setText("Attempts left: " + maxAttempts);
        attemptsBar.setMaximum(maxAttempts);
        attemptsBar.setValue(maxAttempts);
        attemptsBar.setForegroundColor(ACCENT_GREEN);
        guessField.setText("");
        guessField.setEnabled(true);
        guessButton.setEnabled(true);
        playAgainButton.setEnabled(false);
        guessField.requestFocus();
    }

    private void checkGuess() {
        String input = guessField.getText().trim();

        if (input.isEmpty()) {
            resultLabel.setText("⚠ Please enter a number!");
            resultLabel.setForeground(ACCENT_RED);
            return;
        }

        int guess;
        try {
            guess = Integer.parseInt(input);
        } catch (NumberFormatException ex) {
            resultLabel.setText("⚠ Enter a valid number!");
            resultLabel.setForeground(ACCENT_RED);
            return;
        }

        attempts++;

        if (guess == numberToGuess) {
            resultLabel.setText("🎉 Correct! It was " + numberToGuess);
            resultLabel.setForeground(ACCENT_GREEN);

            int roundScore = (maxAttempts - attempts + 1) * 10;
            totalScore += roundScore;
            roundsPlayed++;
            scoreLabel.setText("🏆 Score: " + totalScore + "    |    Rounds: " + roundsPlayed);

            endRound();
        } else if (guess < numberToGuess) {
            resultLabel.setText("📉 Too Low! Try higher");
            resultLabel.setForeground(TEXT_LIGHT);
        } else {
            resultLabel.setText("📈 Too High! Try lower");
            resultLabel.setForeground(TEXT_LIGHT);
        }

        int attemptsLeft = maxAttempts - attempts;
        attemptsLabel.setText("Attempts left: " + attemptsLeft);
        attemptsBar.setValue(Math.max(attemptsLeft, 0));

        if (attemptsLeft <= 2 && attemptsLeft > 0) {
            attemptsBar.setForegroundColor(ACCENT_ORANGE);
        }
        if (attemptsLeft <= 0 && guess != numberToGuess) {
            attemptsBar.setForegroundColor(ACCENT_RED);
            resultLabel.setText("😢 You Lost! It was " + numberToGuess);
            resultLabel.setForeground(ACCENT_RED);
            roundsPlayed++;
            scoreLabel.setText("🏆 Score: " + totalScore + "    |    Rounds: " + roundsPlayed);
            endRound();
        }

        guessField.setText("");
    }

    private void endRound() {
        guessField.setEnabled(false);
        guessButton.setEnabled(false);
        playAgainButton.setEnabled(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(NumberGuessingGame::new);
    }
}