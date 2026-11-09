import java.util.ArrayList;
import java.util.List;

public class PlacementLogic {
    
    // Compares student profiles against company eligibility requirements
    public static List<Student> getEligibleStudents(Company company, List<Student> allStudents) {
        List<Student> eligible = new ArrayList<>();
        
        for (Student s : allStudents) {
            // Check if student CGPA meets minimum AND if their skills contain the required skill
            if (s.getCgpa() >= company.getMinCgpa() && 
                s.getSkills().toLowerCase().contains(company.getRequiredSkill().toLowerCase())) {
                eligible.add(s);
            }
        }
        return eligible;
    }
}