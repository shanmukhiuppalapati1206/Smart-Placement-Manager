public class Student extends Person {
    private double cgpa;
    private String skills;
    private int testScore;

    public Student(String id, String name, double cgpa, String skills) {
        super(id, name); // Calls the Person constructor
        this.cgpa = cgpa;
        this.skills = skills;
        this.testScore = 0; // Default score before taking the test
    }

    public double getCgpa() { return cgpa; }
    public String getSkills() { return skills; }
    public int getTestScore() { return testScore; }
    public void setTestScore(int testScore) { this.testScore = testScore; }
}