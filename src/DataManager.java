import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class DataManager {

    // --- READ METHODS ---
    public static List<Student> loadStudents() {
        List<Student> studentList = new ArrayList<>();
        File file = new File("students.csv");
        if (!file.exists()) return studentList;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine(); 
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(",");
                studentList.add(new Student(data[0], data[1], Double.parseDouble(data[2]), data[3]));
            }
        } catch (Exception e) { System.out.println(e.getMessage()); }
        return studentList;
    }

    public static List<Company> loadCompanies() {
        List<Company> companyList = new ArrayList<>();
        File file = new File("companies.csv");
        if (!file.exists()) return companyList;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine(); 
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(",");
                companyList.add(new Company(data[0], Double.parseDouble(data[1]), data[2]));
            }
        } catch (Exception e) { System.out.println(e.getMessage()); }
        return companyList;
    }

    public static List<Question> loadQuestions() {
        List<Question> questionList = new ArrayList<>();
        File file = new File("questions.csv");
        if (!file.exists()) return questionList;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine(); 
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(",");
                questionList.add(new Question(data[0], data[1], data[2], data[3], data[4], data[5], data[6]));
            }
        } catch (Exception e) { System.out.println(e.getMessage()); }
        return questionList;
    }

    // --- WRITE & DELETE METHODS ---
    
    public static void saveStudent(Student s) {
        try (FileWriter fw = new FileWriter("students.csv", true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println(s.getId() + "," + s.getName() + "," + s.getCgpa() + "," + s.getSkills());
        } catch (Exception e) { System.out.println(e.getMessage()); }
    }

    public static void deleteStudent(String studentId) {
        List<Student> students = loadStudents();
        students.removeIf(s -> s.getId().equals(studentId));
        
        // Overwrite file
        try (PrintWriter pw = new PrintWriter(new FileWriter("students.csv", false))) {
            pw.println("ID,Name,CGPA,Skills");
            for (Student s : students) {
                pw.println(s.getId() + "," + s.getName() + "," + s.getCgpa() + "," + s.getSkills());
            }
        } catch (Exception e) { System.out.println(e.getMessage()); }
    }

    public static void saveCompany(Company c) {
        try (FileWriter fw = new FileWriter("companies.csv", true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println(c.getCompanyName() + "," + c.getMinCgpa() + "," + c.getRequiredSkill());
        } catch (Exception e) { System.out.println(e.getMessage()); }
    }

    public static void deleteCompany(String companyName) {
        List<Company> companies = loadCompanies();
        companies.removeIf(c -> c.getCompanyName().equalsIgnoreCase(companyName));
        
        // Overwrite file
        try (PrintWriter pw = new PrintWriter(new FileWriter("companies.csv", false))) {
            pw.println("CompanyName,MinCGPA,RequiredSkill");
            for (Company c : companies) {
                pw.println(c.getCompanyName() + "," + c.getMinCgpa() + "," + c.getRequiredSkill());
            }
        } catch (Exception e) { System.out.println(e.getMessage()); }
    }
}