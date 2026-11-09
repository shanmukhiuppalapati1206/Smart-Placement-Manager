import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class DataManager {

    // 1. Load Students from CSV
    public static List<Student> loadStudents() {
        List<Student> studentList = new ArrayList<>();
        File file = new File("students.csv");
        if (!file.exists()) return studentList;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine(); // Skip header row
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(",");
                // Expecting: ID, Name, CGPA, Skills
                Student s = new Student(data[0], data[1], Double.parseDouble(data[2]), data[3]);
                studentList.add(s);
            }
        } catch (Exception e) {
            System.out.println("Error reading students.csv: " + e.getMessage());
        }
        return studentList;
    }

    // 2. Load Companies from CSV
    public static List<Company> loadCompanies() {
        List<Company> companyList = new ArrayList<>();
        File file = new File("companies.csv");
        if (!file.exists()) return companyList;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine(); // Skip header row
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(",");
                // Expecting: CompanyName, MinCGPA, RequiredSkill
                Company c = new Company(data[0], Double.parseDouble(data[1]), data[2]);
                companyList.add(c);
            }
        } catch (Exception e) {
            System.out.println("Error reading companies.csv: " + e.getMessage());
        }
        return companyList;
    }

    // 3. Load Questions from CSV
    public static List<Question> loadQuestions() {
        List<Question> questionList = new ArrayList<>();
        File file = new File("questions.csv");
        if (!file.exists()) return questionList;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine(); // Skip header row
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(",");
                // Expecting: Type, Question, A, B, C, D, CorrectAnswer
                Question q = new Question(data[0], data[1], data[2], data[3], data[4], data[5], data[6]);
                questionList.add(q);
            }
        } catch (Exception e) {
            System.out.println("Error reading questions.csv: " + e.getMessage());
        }
        return questionList;
    }

    // 4. Save a new Student to CSV
    public static void saveStudent(Student s) {
        // 'true' inside FileWriter enables appending so it doesn't overwrite existing data
        try (FileWriter fw = new FileWriter("students.csv", true);
             PrintWriter pw = new PrintWriter(fw)) {
            
            pw.println(s.getId() + "," + s.getName() + "," + s.getCgpa() + "," + s.getSkills());
            System.out.println("Student saved successfully.");
            
        } catch (Exception e) {
            System.out.println("Error saving student: " + e.getMessage());
        }
    }
}