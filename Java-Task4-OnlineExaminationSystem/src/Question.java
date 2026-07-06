/**
 * Represents a single multiple-choice question with 4 options.
 */
public class Question {

    private final String questionText;
    private final String[] options; // exactly 4 options
    private final int correctOptionIndex; // 0-3

    public Question(String questionText, String[] options, int correctOptionIndex) {
        this.questionText = questionText;
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String[] getOptions() {
        return options;
    }

    public int getCorrectOptionIndex() {
        return correctOptionIndex;
    }
}