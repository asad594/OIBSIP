import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main application window. Manages navigation between the Login, Profile,
 * Exam, and Result screens using CardLayout, plus all shared exam state
 * (current user, question bank, answers, countdown timer).
 */
@SuppressWarnings("serial")
public class ExamFrame extends JFrame {

    public static final int TOTAL_EXAM_SECONDS = 30 * 60; // 30 minutes

    private final CardLayout cardLayout;
    private final JPanel mainPanel;

    private final Map<String, User> users;
    private User currentUser;

    private final List<Question> masterQuestionBank;
    private List<Question> currentExamQuestions;
    private int[] userAnswers;
    private int currentQuestionIndex;

    private int secondsRemaining;
    private Timer examTimer;
    private long examStartMillis;
    private boolean examInProgress = false;

    private final LoginPanel loginPanel;
    private final ProfilePanel profilePanel;
    private final ExamPanel examPanel;
    private final ResultPanel resultPanel;

    public ExamFrame() {
        setTitle("Online Examination System");
        setSize(700, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        users = new HashMap<>();
        users.put("student1", new User("student1", "pass123", "Ali Raza"));
        users.put("student2", new User("student2", "pass456", "Fatima Noor"));

        masterQuestionBank = buildQuestionBank();

        cardLayout = new CardLayout();
        mainPanel = new GradientPanel();
        mainPanel.setLayout(cardLayout);

        loginPanel = new LoginPanel(this);
        profilePanel = new ProfilePanel(this);
        examPanel = new ExamPanel(this);
        resultPanel = new ResultPanel(this);

        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(profilePanel, "PROFILE");
        mainPanel.add(examPanel, "EXAM");
        mainPanel.add(resultPanel, "RESULT");

        add(mainPanel);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleWindowClose();
            }
        });

        setVisible(true);
    }

    private void handleWindowClose() {
        if (examInProgress) {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to quit? Your exam progress will be lost.",
                    "Confirm Quit",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            if (choice == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        } else {
            System.exit(0);
        }
    }

    private List<Question> buildQuestionBank() {
        List<Question> list = new ArrayList<>();
        list.add(new Question("Which keyword is used to inherit a class in Java?", new String[]{"implements", "extends", "inherits", "super"}, 1));
        list.add(new Question("Which collection does NOT allow duplicate elements?", new String[]{"ArrayList", "LinkedList", "HashSet", "Vector"}, 2));
        list.add(new Question("What is the default value of a boolean in Java?", new String[]{"true", "false", "0", "null"}, 1));
        list.add(new Question("Which method is the entry point of a Java program?", new String[]{"start()", "run()", "main()", "init()"}, 2));
        list.add(new Question("Which of these is used for exception handling?", new String[]{"try-catch", "if-else", "for-loop", "switch-case"}, 0));
        list.add(new Question("Which access modifier makes a member visible only within its own class?", new String[]{"public", "protected", "private", "default"}, 2));
        list.add(new Question("Which of the following is NOT a Java primitive type?", new String[]{"int", "boolean", "String", "char"}, 2));
        list.add(new Question("Which operator is used to compare two values for equality?", new String[]{"=", "==", "!=", "==="}, 1));
        list.add(new Question("What is the size of an int variable in Java?", new String[]{"8 bit", "16 bit", "32 bit", "64 bit"}, 2));
        list.add(new Question("Which of the following is a reserved keyword in Java?", new String[]{"object", "strictfp", "main", "system"}, 1));
        list.add(new Question("What is the extension of Java code files?", new String[]{".js", ".txt", ".class", ".java"}, 3));
        list.add(new Question("Which component is used to compile, debug and execute Java programs?", new String[]{"JRE", "JIT", "JDK", "JVM"}, 2));
        list.add(new Question("Which environment variable is used to set the java path?", new String[]{"MAVEN_PATH", "JavaPATH", "JAVA", "JAVA_HOME"}, 3));
        list.add(new Question("Which exception is thrown when a variable is accessed which is not assigned a value?", new String[]{"NullPointerException", "ArithmeticException", "ArrayIndexOutOfBoundsException", "NumberFormatException"}, 0));
        list.add(new Question("What is the extension of a compiled Java class?", new String[]{".txt", ".java", ".class", ".cpp"}, 2));
        list.add(new Question("Which of these cannot be used for a variable name in Java?", new String[]{"identifier", "keyword", "identifier & keyword", "none of the mentioned"}, 1));
        list.add(new Question("Which statement is used to stop a loop execution in Java?", new String[]{"exit", "break", "stop", "return"}, 1));
        list.add(new Question("What is the return type of the hashCode() method in the Object class?", new String[]{"Object", "int", "long", "void"}, 1));
        list.add(new Question("Which package contains the Random class?", new String[]{"java.util package", "java.lang package", "java.awt package", "java.io package"}, 0));
        list.add(new Question("An interface with no fields or methods is known as a ______.", new String[]{"Runnable Interface", "Marker Interface", "Abstract Interface", "CharSequence Interface"}, 1));
        list.add(new Question("Which of the following is an immediate subclass of the Panel class?", new String[]{"Applet class", "Window class", "Frame class", "Dialog class"}, 0));
        list.add(new Question("In which memory are local variables stored in Java?", new String[]{"Heap", "Stack", "ROM", "Cache"}, 1));
        list.add(new Question("Which of these classes are the direct subclasses of the Throwable class?", new String[]{"RuntimeException and Error class", "Exception and VirtualMachineError class", "Error and Exception class", "IOException and VirtualMachineError class"}, 2));
        list.add(new Question("What is the use of the final keyword in Java?", new String[]{"To restrict the user", "To make a class immutable", "To prevent inheritance", "All of the above"}, 3));
        list.add(new Question("Which of the following is a mutable class in Java?", new String[]{"java.lang.String", "java.lang.Byte", "java.lang.Short", "java.lang.StringBuilder"}, 3));
        list.add(new Question("Which keyword is used to call a superclass constructor?", new String[]{"this", "super", "parent", "base"}, 1));
        list.add(new Question("Which data type is used to create a variable that should store text?", new String[]{"String", "myString", "Txt", "string"}, 0));
        list.add(new Question("How do you create a variable with the numeric value 5?", new String[]{"num x = 5", "float x = 5;", "int x = 5;", "x = 5;"}, 2));
        list.add(new Question("How do you create a variable with the floating number 2.8?", new String[]{"int x = 2.8;", "byte x = 2.8;", "x = 2.8;", "float x = 2.8f;"}, 3));
        list.add(new Question("Which method can be used to find the length of a string?", new String[]{"getSize()", "length()", "size()", "getLength()"}, 1));
        return list;
    }

    // ---------- Screen navigation ----------
    public void showLogin() {
        cardLayout.show(mainPanel, "LOGIN");
    }

    public void showProfile() {
        profilePanel.refreshFields();
        cardLayout.show(mainPanel, "PROFILE");
    }

    public void startExam() {
        // Shuffle the master question bank and pick 20 questions
        java.util.Collections.shuffle(masterQuestionBank);
        currentExamQuestions = new ArrayList<>(masterQuestionBank.subList(0, Math.min(20, masterQuestionBank.size())));
        
        userAnswers = new int[currentExamQuestions.size()];
        Arrays.fill(userAnswers, -1);
        currentQuestionIndex = 0;
        secondsRemaining = TOTAL_EXAM_SECONDS;
        examStartMillis = System.currentTimeMillis();
        examInProgress = true;

        examPanel.loadQuestion(currentQuestionIndex);
        examPanel.updateTimerLabel(secondsRemaining);

        examTimer = new Timer(1000, e -> {
            secondsRemaining--;
            examPanel.updateTimerLabel(secondsRemaining);
            if (secondsRemaining <= 0) {
                examTimer.stop();
                submitExam(true);
            }
        });
        examTimer.start();

        cardLayout.show(mainPanel, "EXAM");
    }

    // ---------- Question navigation ----------
    public int getTotalQuestions() {
        return currentExamQuestions.size();
    }

    public Question getQuestion(int index) {
        return currentExamQuestions.get(index);
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public void saveAnswer(int questionIndex, int selectedOption) {
        userAnswers[questionIndex] = selectedOption;
    }

    public int getSavedAnswer(int questionIndex) {
        return userAnswers[questionIndex];
    }

    public void goToNext() {
        if (currentQuestionIndex < currentExamQuestions.size() - 1) {
            currentQuestionIndex++;
            examPanel.loadQuestion(currentQuestionIndex);
        }
    }

    public void goToPrevious() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            examPanel.loadQuestion(currentQuestionIndex);
        }
    }

    // ---------- Submission ----------
    public void requestManualSubmit() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to submit the exam?",
                "Confirm Submit",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        if (choice == JOptionPane.YES_OPTION) {
            if (examTimer != null) {
                examTimer.stop();
            }
            submitExam(false);
        }
    }

    private void submitExam(boolean autoSubmitted) {
        examInProgress = false;

        int correctCount = 0;
        for (int i = 0; i < currentExamQuestions.size(); i++) {
            if (userAnswers[i] == currentExamQuestions.get(i).getCorrectOptionIndex()) {
                correctCount++;
            }
        }

        long elapsedSeconds = (System.currentTimeMillis() - examStartMillis) / 1000;

        resultPanel.showResult(correctCount, currentExamQuestions.size(), elapsedSeconds, userAnswers, autoSubmitted);
        cardLayout.show(mainPanel, "RESULT");
    }

    // ---------- User / session management ----------
    public User authenticate(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void logout() {
        currentUser = null;
        examInProgress = false;
        showLogin();
    }
}