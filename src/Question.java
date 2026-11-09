public class Question {
    private String type; // "Aptitude" or "Technical"
    private String questionText;
    private String optA, optB, optC, optD;
    private String correctOption;

    public Question(String type, String questionText, String optA, String optB, String optC, String optD, String correctOption) {
        this.type = type;
        this.questionText = questionText;
        this.optA = optA;
        this.optB = optB;
        this.optC = optC;
        this.optD = optD;
        this.correctOption = correctOption;
    }

    // Getters
    public String getType() { return type; }
    public String getQuestionText() { return questionText; }
    public String getOptA() { return optA; }
    public String getOptB() { return optB; }
    public String getOptC() { return optC; }
    public String getOptD() { return optD; }
    public String getCorrectOption() { return correctOption; }
}