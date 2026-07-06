import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Exam screen: shows one question at a time with 4 radio-button options,
 * a live countdown timer, Previous/Next navigation, and a Submit button.
 */
@SuppressWarnings("serial")
public class ExamPanel extends JPanel {

    private final ExamFrame frame;

    private final JLabel timerLabel;
    private final JLabel questionNumberLabel;
    private final JLabel questionTextLabel;
    private final JRadioButton[] optionButtons;
    private final ButtonGroup buttonGroup;
    private final ModernButton prevButton;
    private final ModernButton nextButton;
    private final ModernButton submitButton;

    public ExamPanel(ExamFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(25, 30, 25, 30));
        setOpaque(false); // Let GradientPanel show through

        // ---- Top: countdown timer ----
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        
        timerLabel = new JLabel("Time Remaining: 30:00", SwingConstants.RIGHT);
        timerLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        timerLabel.setForeground(new Color(255, 100, 100)); // Lighter red for dark background
        topPanel.add(timerLabel, BorderLayout.EAST);
        
        add(topPanel, BorderLayout.NORTH);

        // ---- Center: question + options ----
        GlassPanel centerPanel = new GlassPanel(20, 240); // 240 alpha white
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        questionNumberLabel = new JLabel("Question 1 of N");
        questionNumberLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        questionNumberLabel.setForeground(new Color(80, 80, 80));
        questionNumberLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        questionTextLabel = new JLabel("<html></html>");
        questionTextLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        questionTextLabel.setForeground(new Color(30, 60, 100));
        questionTextLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        questionTextLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 25, 0));

        buttonGroup = new ButtonGroup();
        optionButtons = new JRadioButton[4];
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        optionsPanel.setOpaque(false);

        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JRadioButton();
            optionButtons[i].setFont(new Font("Segoe UI", Font.PLAIN, 18));
            optionButtons[i].setForeground(new Color(40, 40, 40));
            optionButtons[i].setAlignmentX(Component.LEFT_ALIGNMENT);
            optionButtons[i].setOpaque(false);
            optionButtons[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
            buttonGroup.add(optionButtons[i]);
            optionsPanel.add(optionButtons[i]);
            optionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        centerPanel.add(questionNumberLabel);
        centerPanel.add(questionTextLabel);
        centerPanel.add(optionsPanel);
        add(centerPanel, BorderLayout.CENTER);

        // ---- Bottom: navigation buttons ----
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setOpaque(false);
        
        prevButton = new ModernButton("Previous", new Color(108, 117, 125), new Color(90, 100, 110));
        prevButton.setPreferredSize(new Dimension(130, 45));
        
        nextButton = new ModernButton("Next", new Color(0, 123, 255), new Color(0, 100, 210));
        nextButton.setPreferredSize(new Dimension(130, 45));
        
        submitButton = new ModernButton("Submit Exam", new Color(220, 53, 69), new Color(200, 40, 50));
        submitButton.setPreferredSize(new Dimension(140, 45));

        prevButton.addActionListener(e -> {
            saveCurrentSelection();
            frame.goToPrevious();
        });
        nextButton.addActionListener(e -> {
            saveCurrentSelection();
            frame.goToNext();
        });
        submitButton.addActionListener(e -> {
            saveCurrentSelection();
            frame.requestManualSubmit();
        });

        bottomPanel.add(prevButton);
        bottomPanel.add(nextButton);
        bottomPanel.add(submitButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private void saveCurrentSelection() {
        int selected = -1;
        for (int i = 0; i < optionButtons.length; i++) {
            if (optionButtons[i].isSelected()) {
                selected = i;
                break;
            }
        }
        frame.saveAnswer(frame.getCurrentQuestionIndex(), selected);
    }

    /** Loads and displays the question at the given index, restoring any previously saved answer. */
    public void loadQuestion(int index) {
        Question q = frame.getQuestion(index);
        questionNumberLabel.setText("Question " + (index + 1) + " of " + frame.getTotalQuestions());
        questionTextLabel.setText("<html><body style='width: 500px;'>" + q.getQuestionText() + "</body></html>");

        buttonGroup.clearSelection();
        String[] options = q.getOptions();
        for (int i = 0; i < 4; i++) {
            optionButtons[i].setText(options[i]);
        }

        int savedAnswer = frame.getSavedAnswer(index);
        if (savedAnswer != -1) {
            optionButtons[savedAnswer].setSelected(true);
        }

        prevButton.setEnabled(index > 0);
        nextButton.setEnabled(index < frame.getTotalQuestions() - 1);
    }

    public void updateTimerLabel(int secondsRemaining) {
        int clamped = Math.max(secondsRemaining, 0);
        int minutes = clamped / 60;
        int seconds = clamped % 60;
        timerLabel.setText(String.format("Time Remaining: %02d:%02d", minutes, seconds));

        timerLabel.setForeground(secondsRemaining <= 60 ? new Color(255, 50, 50) : new Color(255, 180, 180));
    }
}