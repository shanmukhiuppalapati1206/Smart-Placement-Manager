import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.List;

public class MainApp extends Application {

    private Stage window;
    private Scene loginScene;

    // Professional Theme Styles
    String sidebarStyle = "-fx-background-color: #1E293B; -fx-padding: 20;";
    String headerStyle = "-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #1E293B;";
    String btnStyle = "-fx-background-color: #2563EB; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;";
    String sidebarBtnStyle = "-fx-background-color: transparent; -fx-text-fill: #94A3B8; -fx-font-size: 15px; -fx-alignment: center-left; -fx-padding: 10 0; -fx-cursor: hand;";
    String cardStyle = "-fx-background-color: white; -fx-padding: 30; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 5);";

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        window.setTitle("Smart College Placement & Recruitment Manager");

        // --- HOME / LOGIN SCREEN ---
        VBox loginBox = new VBox(20);
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setStyle(cardStyle);
        loginBox.setMaxWidth(420);

        Label titleLabel = new Label("Placement & Recruitment");
        titleLabel.setStyle(headerStyle);
        Label subtitle = new Label("Smart College Portal");
        subtitle.setStyle("-fx-text-fill: #64748B; -fx-font-size: 14px; -fx-padding: 0 0 10 0;");

        Button studentLoginBtn = new Button("Student Portal");
        Button coordinatorLoginBtn = new Button("Placement Coordinator");
        Button registerMenuBtn = new Button("Register New Profile");
        
        studentLoginBtn.setStyle(btnStyle); studentLoginBtn.setMinWidth(260);
        coordinatorLoginBtn.setStyle(btnStyle); coordinatorLoginBtn.setMinWidth(260);
        registerMenuBtn.setStyle("-fx-background-color: #475569; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;"); 
        registerMenuBtn.setMinWidth(260);

        studentLoginBtn.setOnAction(e -> {
            Scene studentScene = createStudentDashboard();
            if (studentScene != null) {
                window.setScene(studentScene);
            }
        });
        coordinatorLoginBtn.setOnAction(e -> window.setScene(createCoordinatorDashboard()));
        registerMenuBtn.setOnAction(e -> window.setScene(createRegistrationScreen()));

        loginBox.getChildren().addAll(titleLabel, subtitle, studentLoginBtn, coordinatorLoginBtn, registerMenuBtn);
        
        StackPane root = new StackPane(loginBox);
        root.setStyle("-fx-background-color: #F8FAFC; -fx-padding: 50;");
        loginScene = new Scene(root, 950, 650);

        window.setScene(loginScene);
        window.show();
    }

    // ==========================================
    // --- COORDINATOR DASHBOARD ---
    // ==========================================
    private Scene createCoordinatorDashboard() {
        BorderPane layout = new BorderPane();
        
        VBox sidebar = new VBox(15);
        sidebar.setStyle(sidebarStyle);
        sidebar.setPrefWidth(240);
        Label logo = new Label("Coordinator Hub");
        logo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white; -fx-padding: 0 0 25 0;");
        
        Button dashBtn = new Button("Eligibility Reports");
        Button manageStudBtn = new Button("Manage Students");
        Button manageCompBtn = new Button("Manage Companies");
        Button logoutBtn = new Button("Sign Out");
        
        dashBtn.setStyle(sidebarBtnStyle); manageStudBtn.setStyle(sidebarBtnStyle); 
        manageCompBtn.setStyle(sidebarBtnStyle); logoutBtn.setStyle(sidebarBtnStyle);
        
        sidebar.getChildren().addAll(logo, dashBtn, manageStudBtn, manageCompBtn, logoutBtn);

        VBox reportsPanel = getReportsPanel();
        VBox manageStudentsPanel = getManageStudentsPanel();
        VBox manageCompaniesPanel = getManageCompaniesPanel();

        dashBtn.setOnAction(e -> layout.setCenter(reportsPanel));
        manageStudBtn.setOnAction(e -> layout.setCenter(manageStudentsPanel));
        manageCompBtn.setOnAction(e -> layout.setCenter(manageCompaniesPanel));
        logoutBtn.setOnAction(e -> window.setScene(loginScene));

        layout.setLeft(sidebar);
        layout.setCenter(reportsPanel);
        return new Scene(layout, 950, 650);
    }

    private VBox getReportsPanel() {
        VBox content = new VBox(20);
        content.setStyle("-fx-background-color: #F8FAFC; -fx-padding: 40;");
        Label title = new Label("Company Eligibility & Recruitment Reports");
        title.setStyle(headerStyle);
        
        Button viewReportsBtn = new Button("Generate Live Report");
        viewReportsBtn.setStyle(btnStyle);
        
        Label reportOutput = new Label("Click button to load eligibility data...");
        reportOutput.setStyle(cardStyle + "-fx-font-family: 'Consolas'; -fx-font-size: 13px;");
        reportOutput.setMinSize(600, 320);

        viewReportsBtn.setOnAction(e -> {
            try {
                List<Student> students = DataManager.loadStudents();
                List<Company> companies = DataManager.loadCompanies();
                StringBuilder sb = new StringBuilder();
                sb.append("========================================\n");
                sb.append("   PLACEMENT ELIGIBILITY STATUS REPORT\n");
                sb.append("========================================\n\n");
                for (Company c : companies) {
                    sb.append("🏢 Company: ").append(c.getCompanyName())
                      .append(" [Min CGPA: ").append(c.getMinCgpa()).append(" | Req: ").append(c.getRequiredSkill()).append("]\n");
                    List<Student> eligible = PlacementLogic.getEligibleStudents(c, students);
                    if(eligible.isEmpty()) {
                        sb.append("   ⚠️ No eligible students found.\n");
                    } else {
                        for (Student s : eligible) {
                            sb.append("   ✔️ ").append(s.getName()).append(" (ID: ").append(s.getId()).append(" - CGPA: ").append(s.getCgpa()).append(")\n");
                        }
                    }
                    sb.append("\n");
                }
                reportOutput.setText(sb.toString());
            } catch (Exception ex) { reportOutput.setText("Error generating report."); }
        });
        content.getChildren().addAll(title, viewReportsBtn, reportOutput);
        return content;
    }

    private VBox getManageStudentsPanel() {
        VBox content = new VBox(20);
        content.setStyle("-fx-background-color: #F8FAFC; -fx-padding: 40;");
        Label title = new Label("Manage Student Records");
        title.setStyle(headerStyle);

        VBox card = new VBox(15);
        card.setStyle(cardStyle);
        card.setMaxWidth(400);

        Label lbl = new Label("Remove Student Profile");
        lbl.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        TextField deleteIdField = new TextField(); deleteIdField.setPromptText("Enter Student ID (e.g., 101)");
        Button deleteBtn = new Button("Delete Student");
        deleteBtn.setStyle("-fx-background-color: #DC2626; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;");
        Label status = new Label();

        deleteBtn.setOnAction(e -> {
            DataManager.deleteStudent(deleteIdField.getText());
            status.setStyle("-fx-text-fill: #16A34A; -fx-font-weight: bold;");
            status.setText("Student record removed successfully.");
            deleteIdField.clear();
        });

        card.getChildren().addAll(lbl, deleteIdField, deleteBtn, status);
        content.getChildren().addAll(title, card);
        return content;
    }

    private VBox getManageCompaniesPanel() {
        VBox content = new VBox(20);
        content.setStyle("-fx-background-color: #F8FAFC; -fx-padding: 40;");
        Label title = new Label("Manage Company Criteria");
        title.setStyle(headerStyle);

        VBox card = new VBox(12);
        card.setStyle(cardStyle);
        card.setMaxWidth(420);

        Label addLbl = new Label("Add Recruiting Company");
        addLbl.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        TextField compName = new TextField(); compName.setPromptText("Company Name");
        TextField minCgpa = new TextField(); minCgpa.setPromptText("Minimum CGPA");
        TextField reqSkill = new TextField(); reqSkill.setPromptText("Required Skill (e.g., Java)");
        Button addBtn = new Button("Add Company");
        addBtn.setStyle(btnStyle);
        
        Separator sep = new Separator();

        Label delLbl = new Label("Remove Company");
        delLbl.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        TextField delCompField = new TextField(); delCompField.setPromptText("Company Name to Remove");
        Button delBtn = new Button("Delete Company");
        delBtn.setStyle("-fx-background-color: #DC2626; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;");
        Label status = new Label();

        addBtn.setOnAction(e -> {
            try {
                Company c = new Company(compName.getText(), Double.parseDouble(minCgpa.getText()), reqSkill.getText());
                DataManager.saveCompany(c);
                status.setStyle("-fx-text-fill: #16A34A; -fx-font-weight: bold;");
                status.setText("Company added successfully.");
                compName.clear(); minCgpa.clear(); reqSkill.clear();
            } catch (Exception ex) { status.setText("Error in inputs."); status.setStyle("-fx-text-fill: #DC2626;"); }
        });

        delBtn.setOnAction(e -> {
            DataManager.deleteCompany(delCompField.getText());
            status.setStyle("-fx-text-fill: #16A34A; -fx-font-weight: bold;");
            status.setText("Company removed successfully.");
            delCompField.clear();
        });

        card.getChildren().addAll(addLbl, compName, minCgpa, reqSkill, addBtn, sep, delLbl, delCompField, delBtn, status);
        content.getChildren().addAll(title, card);
        return content;
    }

    // ==========================================
    // --- STUDENT DASHBOARD WITH ID LOGIN ---
    // ==========================================
    private Scene createStudentDashboard() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Student Portal Login");
        dialog.setHeaderText("Enter Your Student ID to Login");
        dialog.setContentText("Student ID (e.g., 101):");

        java.util.Optional<String> result = dialog.showAndWait();
        if (!result.isPresent() || result.get().trim().isEmpty()) {
            return null; 
        }

        String inputId = result.get().trim();
        List<Student> allStudents = DataManager.loadStudents();
        Student loggedInStudent = null;

        for (Student s : allStudents) {
            if (s.getId().equals(inputId)) {
                loggedInStudent = s;
                break;
            }
        }

        if (loggedInStudent == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText("Student ID Not Found!");
            alert.setContentText("Please register your profile first using 'Register New Profile'.");
            alert.showAndWait();
            return null;
        }

        BorderPane layout = new BorderPane();
        
        VBox sidebar = new VBox(15);
        sidebar.setStyle(sidebarStyle);
        sidebar.setPrefWidth(240);
        Label logo = new Label("Portal: " + loggedInStudent.getName());
        logo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white; -fx-padding: 0 0 25 0;");
        
        Button testBtn = new Button("Aptitude & Tech Practice");
        Button logoutBtn = new Button("Sign Out");
        testBtn.setStyle(sidebarBtnStyle); logoutBtn.setStyle(sidebarBtnStyle);
        sidebar.getChildren().addAll(logo, testBtn, logoutBtn);

        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER_RIGHT);
        topBar.setStyle("-fx-background-color: white; -fx-padding: 15 30; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.03), 5, 0, 0, 2);");
        
        Button profileBtn = new Button("👤 My Profile & Details");
        profileBtn.setStyle("-fx-background-color: #E2E8F0; -fx-text-fill: #1E293B; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 6; -fx-cursor: hand;");
        topBar.getChildren().add(profileBtn);

        VBox content = new VBox(20);
        content.setStyle("-fx-background-color: #F8FAFC; -fx-padding: 30;");
        content.setAlignment(Pos.TOP_CENTER);
        
        Label studentTitle = new Label("Live Practice Test & Assessment");
        studentTitle.setStyle(headerStyle);
        
        Label timerLabel = new Label("Time Left: 30:00");
        timerLabel.setStyle("-fx-text-fill: #DC2626; -fx-font-size: 16px; -fx-font-weight: bold;");
        
        Button startTestBtn = new Button("Start Timer & Begin Test");
        startTestBtn.setStyle(btnStyle);
        
        VBox quizCard = new VBox(15);
        quizCard.setStyle(cardStyle);
        quizCard.setAlignment(Pos.CENTER_LEFT);
        quizCard.setMaxWidth(600);
        
        Label questionLabel = new Label("Click 'Start Timer & Begin Test' to start practicing questions.");
        questionLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
        
        Button optA = new Button(); Button optB = new Button();
        Button optC = new Button(); Button optD = new Button();
        String optStyle = "-fx-background-color: #F1F5F9; -fx-text-fill: #1E293B; -fx-padding: 10; -fx-font-size: 14px; -fx-cursor: hand; -fx-alignment: center-left;";
        optA.setStyle(optStyle); optB.setStyle(optStyle); optC.setStyle(optStyle); optD.setStyle(optStyle);
        optA.setMinWidth(500); optB.setMinWidth(500); optC.setMinWidth(500); optD.setMinWidth(500);
        
        VBox optionsBox = new VBox(10, optA, optB, optC, optD);
        optionsBox.setVisible(false); 
        
        Label resultLabel = new Label();
        resultLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #16A34A;");

        quizCard.getChildren().addAll(questionLabel, optionsBox, resultLabel);
        
        int[] currentQ = {0}; int[] score = {0};
        
        startTestBtn.setOnAction(e -> {
            List<Question> qList = DataManager.loadQuestions();
            if (qList.isEmpty()) {
                questionLabel.setText("No questions found in questions.csv!");
                return;
            }
            startTestBtn.setVisible(false); optionsBox.setVisible(true);
            
            startTimerThread(timerLabel);
            loadQuestionUI(qList, currentQ[0], questionLabel, optA, optB, optC, optD);
            
            javafx.event.EventHandler<javafx.event.ActionEvent> answerHandler = event -> {
                Button clicked = (Button) event.getSource();
                String selected = clicked.getText().substring(0, 1); 
                if (selected.equals(qList.get(currentQ[0]).getCorrectOption())) score[0]++;
                
                currentQ[0]++;
                if (currentQ[0] < qList.size()) {
                    loadQuestionUI(qList, currentQ[0], questionLabel, optA, optB, optC, optD);
                } else {
                    optionsBox.setVisible(false);
                    questionLabel.setText("Test Successfully Completed!");
                    resultLabel.setText("Final Score: " + score[0] + " / " + qList.size() + " (" + ((score[0]*100)/qList.size()) + "%)");
                }
            };
            optA.setOnAction(answerHandler); optB.setOnAction(answerHandler);
            optC.setOnAction(answerHandler); optD.setOnAction(answerHandler);
        });

        // PROFILE MODAL FOR LOGGED IN STUDENT
        final Student activeStudent = loggedInStudent;
        profileBtn.setOnAction(e -> {
            Stage profileStage = new Stage();
            profileStage.setTitle("Student Profile: " + activeStudent.getName());
            
            VBox pRoot = new VBox(15);
            pRoot.setStyle(cardStyle);
            pRoot.setAlignment(Pos.CENTER);
            
            Label pTitle = new Label("Student Academic Profile");
            pTitle.setStyle(headerStyle);
            
            TextField pId = new TextField(activeStudent.getId()); pId.setPromptText("ID");
            TextField pName = new TextField(activeStudent.getName()); pName.setPromptText("Name");
            TextField pCgpa = new TextField(String.valueOf(activeStudent.getCgpa())); pCgpa.setPromptText("CGPA");
            TextField pSkills = new TextField(activeStudent.getSkills()); pSkills.setPromptText("Skills");
            
            Button updateBtn = new Button("Update & Save Details");
            updateBtn.setStyle(btnStyle);
            Label pStatus = new Label();
            
            updateBtn.setOnAction(ex -> {
                try {
                    String oldId = activeStudent.getId();
                    activeStudent.setId(pId.getText());
                    activeStudent.setName(pName.getText());
                    activeStudent.setCgpa(Double.parseDouble(pCgpa.getText()));
                    activeStudent.setSkills(pSkills.getText());
                    
                    DataManager.deleteStudent(oldId);
                    DataManager.saveStudent(activeStudent);
                    
                    pStatus.setStyle("-fx-text-fill: #16A34A; -fx-font-weight: bold;");
                    pStatus.setText("Profile updated & saved to CSV!");
                } catch(Exception err) {
                    pStatus.setStyle("-fx-text-fill: #DC2626;");
                    pStatus.setText("Invalid entry format.");
                }
            });
            
            pRoot.getChildren().addAll(pTitle, new Label("Student ID:"), pId, new Label("Full Name:"), pName, new Label("CGPA:"), pCgpa, new Label("Primary Skills:"), pSkills, updateBtn, pStatus);
            Scene pScene = new Scene(pRoot, 400, 500);
            profileStage.setScene(pScene);
            profileStage.show();
        });
        
        logoutBtn.setOnAction(e -> window.setScene(loginScene));
        content.getChildren().addAll(studentTitle, timerLabel, startTestBtn, quizCard);
        
        BorderPane centerPane = new BorderPane();
        centerPane.setTop(topBar);
        centerPane.setCenter(content);
        
        layout.setLeft(sidebar);
        layout.setCenter(centerPane);
        return new Scene(layout, 950, 650);
    }

    // ==========================================
    // --- REGISTRATION SCREEN ---
    // ==========================================
    private Scene createRegistrationScreen() {
        VBox regBox = new VBox(15);
        regBox.setAlignment(Pos.CENTER);
        regBox.setStyle(cardStyle);
        regBox.setMaxWidth(420);

        Label regTitle = new Label("Student Registration");
        regTitle.setStyle(headerStyle);
        
        TextField idField = new TextField(); idField.setPromptText("Student ID (e.g., 104)");
        TextField nameField = new TextField(); nameField.setPromptText("Full Name");
        TextField cgpaField = new TextField(); cgpaField.setPromptText("CGPA (e.g., 8.5)");
        
        ComboBox<String> skillsCombo = new ComboBox<>();
        skillsCombo.getItems().addAll("Java", "C++", "Python", "Java-React");
        skillsCombo.setPromptText("Select Primary Skill");
        skillsCombo.setMinWidth(250);
        
        Button submitRegBtn = new Button("Register Profile");
        submitRegBtn.setStyle(btnStyle);
        Button backBtn = new Button("Back to Home");
        backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #64748B; -fx-cursor: hand;");
        
        Label regStatus = new Label();

        submitRegBtn.setOnAction(e -> {
            try {
                Student s = new Student(idField.getText(), nameField.getText(), Double.parseDouble(cgpaField.getText()), skillsCombo.getValue());
                DataManager.saveStudent(s); 
                regStatus.setStyle("-fx-text-fill: #16A34A; -fx-font-weight: bold;");
                regStatus.setText("Registration Successful!");
                idField.clear(); nameField.clear(); cgpaField.clear(); skillsCombo.getSelectionModel().clearSelection();
            } catch (Exception ex) {
                regStatus.setStyle("-fx-text-fill: #DC2626; -fx-font-weight: bold;");
                regStatus.setText("Error: Please check inputs.");
            }
        });
        backBtn.setOnAction(e -> window.setScene(loginScene));

        regBox.getChildren().addAll(regTitle, idField, nameField, cgpaField, skillsCombo, submitRegBtn, regStatus, backBtn);
        StackPane root = new StackPane(regBox);
        root.setStyle("-fx-background-color: #F8FAFC; -fx-padding: 50;");
        return new Scene(root, 950, 650);
    }

    private void loadQuestionUI(List<Question> qList, int index, Label qLabel, Button a, Button b, Button c, Button d) {
        Question q = qList.get(index);
        qLabel.setText("Q" + (index + 1) + ". " + q.getQuestionText());
        a.setText("A) " + q.getOptA()); b.setText("B) " + q.getOptB());
        c.setText("C) " + q.getOptC()); d.setText("D) " + q.getOptD());
    }

    private void startTimerThread(Label timerLabel) {
        Thread timerThread = new Thread(() -> {
            try {
                int secondsLeft = 1800; 
                while (secondsLeft > 0) {
                    final int displayTime = secondsLeft;
                    Platform.runLater(() -> timerLabel.setText(String.format("Time Left: %02d:%02d", displayTime / 60, displayTime % 60)));
                    Thread.sleep(1000);
                    secondsLeft--;
                }
                Platform.runLater(() -> timerLabel.setText("Time's Up!"));
            } catch (InterruptedException ignored) {}
        });
        timerThread.setDaemon(true);
        timerThread.start();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}