import java.io.*;
import java.util.*;

public class DataManager {
    
    // --- STUDENTS CRUD ---
    public static List<Student> loadStudents() {
        List<Student> students = new ArrayList<>();
        File file = new File("students.csv");
        if (!file.exists()) return students;
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    students.add(new Student(parts[0].trim(), parts[1].trim(), Double.parseDouble(parts[2].trim()), parts[3].trim()));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return students;
    }

    public static void saveStudent(Student s) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("students.csv", true))) {
            bw.write(s.getId() + "," + s.getName() + "," + s.getCgpa() + "," + s.getSkills());
            bw.newLine();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static void deleteStudent(String id) {
        List<Student> students = loadStudents();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("students.csv"))) {
            for (Student s : students) {
                if (!s.getId().equals(id)) {
                    bw.write(s.getId() + "," + s.getName() + "," + s.getCgpa() + "," + s.getSkills());
                    bw.newLine();
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    // --- COMPANIES CRUD ---
    public static List<Company> loadCompanies() {
        List<Company> companies = new ArrayList<>();
        File file = new File("companies.csv");
        if (!file.exists()) return companies;
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    companies.add(new Company(parts[0].trim(), Double.parseDouble(parts[1].trim()), parts[2].trim()));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return companies;
    }

    public static void saveCompany(Company c) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("companies.csv", true))) {
            bw.write(c.getCompanyName() + "," + c.getMinCgpa() + "," + c.getRequiredSkill());
            bw.newLine();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static void deleteCompany(String name) {
        List<Company> companies = loadCompanies();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("companies.csv"))) {
            for (Company c : companies) {
                if (!c.getCompanyName().equalsIgnoreCase(name)) {
                    bw.write(c.getCompanyName() + "," + c.getMinCgpa() + "," + c.getRequiredSkill());
                    bw.newLine();
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    // --- QUESTIONS LOADER ---
    public static List<Question> loadQuestions() {
        List<Question> questions = new ArrayList<>();
        File file = new File("questions.csv");
        if (!file.exists()) return questions;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    questions.add(new Question(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim(), parts[4].trim(), parts[5].trim()));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return questions;
    }
}