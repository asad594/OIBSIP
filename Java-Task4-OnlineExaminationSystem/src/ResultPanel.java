import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Result screen: shows the final score, time taken, and a full breakdown
 * of correct/incorrect answers for each question.
 */
@SuppressWarnings("serial")
public class ResultPanel extends JPanel {

    private final ExamFrame frame;

    private final JLabel scoreLabel;
    private final JLabel timeLabel;
    private final JLabel autoSubmitLabel;
    private final JTextArea breakdownArea;

    public ResultPanel(ExamFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setOpaque(false); // Let GradientPanel show through

        GlassPanel centerGlass = new GlassPanel(25, 230); // semi-transparent white
        centerGlass.setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setOpaque(false);

        JLabel title = new JLabel("Exam Result");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(new Color(30, 60, 100));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        scoreLabel = new JLabel(" ");
        scoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        scoreLabel.setForeground(new Color(40, 167, 69)); // Success green
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        timeLabel = new JLabel(" ");
        timeLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        timeLabel.setForeground(new Color(80, 80, 80));
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        autoSubmitLabel = new JLabel(" ");
        autoSubmitLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        autoSubmitLabel.setForeground(new Color(220, 53, 69)); // Red for warning
        autoSubmitLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        topPanel.add(title);
        topPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        topPanel.add(scoreLabel);
        topPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        topPanel.add(timeLabel);
        topPanel.add(autoSubmitLabel);
        centerGlass.add(topPanel, BorderLayout.NORTH);

        breakdownArea = new JTextArea();
        breakdownArea.setEditable(false);
        breakdownArea.setFont(new Font("Consolas", Font.PLAIN, 15));
        breakdownArea.setMargin(new Insets(15, 15, 15, 15));
        breakdownArea.setBackground(new Color(255, 255, 255, 200)); // Slightly transparent
        breakdownArea.setForeground(new Color(40, 40, 40));
        
        JScrollPane scrollPane = new JScrollPane(breakdownArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        centerGlass.add(scrollPane, BorderLayout.CENTER);

        add(centerGlass, BorderLayout.CENTER);

        ModernButton logoutButton = new ModernButton("Logout", new Color(108, 117, 125), new Color(90, 100, 110));
        logoutButton.setPreferredSize(new Dimension(140, 45));
        logoutButton.addActionListener(e -> frame.logout());
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.add(logoutButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void showResult(int correctCount, int totalQuestions, long elapsedSeconds, int[] userAnswers, boolean autoSubmitted) {
        scoreLabel.setText("Score: " + correctCount + " out of " + totalQuestions);

        long minutes = elapsedSeconds / 60;
        long seconds = elapsedSeconds % 60;
        timeLabel.setText(String.format("Time Taken: %02d:%02d", minutes, seconds));

        autoSubmitLabel.setText(autoSubmitted ? "\u23F0 Time ran out - exam was auto-submitted." : " ");

        StringBuilder sb = new StringBuilder();
        sb.append("Answer Breakdown:\n");
        sb.append("------------------------------------------------------------------\n\n");

        for (int i = 0; i < totalQuestions; i++) {
            Question q = frame.getQuestion(i);
            int userAnswer = userAnswers[i];
            int correctAnswer = q.getCorrectOptionIndex();
            String userAnswerText = (userAnswer == -1) ? "Not Answered" : q.getOptions()[userAnswer];
            String correctAnswerText = q.getOptions()[correctAnswer];
            boolean isCorrect = (userAnswer == correctAnswer);

            sb.append("Q").append(i + 1).append(": ").append(q.getQuestionText()).append("\n");
            sb.append("   Your Answer: ").append(userAnswerText).append("\n");
            if (!isCorrect) {
                sb.append("   Correct Answer: ").append(correctAnswerText).append("\n");
            }
            sb.append("   Status: ").append(isCorrect ? "Correct" : "Incorrect").append("\n");
            sb.append("------------------------------------------------------------------\n\n");
        }

        breakdownArea.setText(sb.toString());
        breakdownArea.setCaretPosition(0);
    }
}