public class Company {
    private String companyName;
    private double minCgpa;
    private String requiredSkill;

    public Company(String companyName, double minCgpa, String requiredSkill) {
        this.companyName = companyName;
        this.minCgpa = minCgpa;
        this.requiredSkill = requiredSkill;
    }

    public String getCompanyName() { return companyName; }
    public double getMinCgpa() { return minCgpa; }
    public String getRequiredSkill() { return requiredSkill; }
}