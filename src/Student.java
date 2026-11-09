public class Student {
    private String id;
    private String name;
    private double cgpa;
    private String skills;

    // Constructor
    public Student(String id, String name, double cgpa, String skills) {
        this.id = id;
        this.name = name;
        this.cgpa = cgpa;
        this.skills = skills;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getCgpa() {
        return cgpa;
    }

    public String getSkills() {
        return skills;
    }

    // Setters (Ivi lekapovadam valla error ochindi)
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCgpa(double cgpa) {
        this.cgpa = cgpa;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }
}