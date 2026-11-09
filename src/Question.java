public class Question {
    private String questionText;
    private String optA;
    private String optB;
    private String optC;
    private String optD;
    private String correctOption;

    // Correct 6-parameter constructor
    public Question(String questionText, String optA, String optB, String optC, String optD, String correctOption) {
        this.questionText = questionText;
        this.optA = optA;
        this.optB = optB;
        this.optC = optC;
        this.optD = optD;
        this.correctOption = correctOption;
    }

    // Getters
    public String getQuestionText() { return questionText; }
    public String getOptA() { return optA; }
    public String getOptB() { return optB; }
    public String getOptC() { return optC; }
    public String getOptD() { return optD; }
    public String getCorrectOption() { return correctOption; }
}