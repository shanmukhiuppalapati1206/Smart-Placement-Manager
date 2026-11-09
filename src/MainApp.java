import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class MainApp extends Application {

    private Stage window;
    private Scene loginScene;

    String sidebarStyle = "-fx-background-color: #0F172A; -fx-padding: 20;";
    String headerStyle = "-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1E293B;";
    String btnStyle = "-fx-background-color: #2563EB; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;";
    String sidebarBtnStyle = "-fx-background-color: transparent; -fx-text-fill: #94A3B8; -fx-font-size: 14px; -fx-alignment: center-left; -fx-padding: 10 0; -fx-cursor: hand;";
    String cardStyle = "-fx-background-color: white; -fx-padding: 25; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 5);";

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        window.setTitle("Smart College Placement & Recruitment Manager");

        VBox loginBox = new VBox(20);
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setStyle(cardStyle);
        loginBox.setMaxWidth(420);

        Label titleLabel = new Label("Placement & Recruitment");
        titleLabel.setStyle(headerStyle);
        Label subtitle = new Label("Enterprise Campus Portal");
        subtitle.setStyle("-fx-text-fill: #64748B; -fx-font-size: 14px;");

        Button studentLoginBtn = new Button("Student Secure Portal");
        Button coordinatorLoginBtn = new Button("Placement Coordinator");
        Button registerMenuBtn = new Button("Register New Profile");
        
        studentLoginBtn.setStyle(btnStyle); studentLoginBtn.setMinWidth(260);
        coordinatorLoginBtn.setStyle(btnStyle); coordinatorLoginBtn.setMinWidth(260);
        registerMenuBtn.setStyle("-fx-background-color: #475569; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;"); 
        registerMenuBtn.setMinWidth(260);

        studentLoginBtn.setOnAction(e -> {
            Scene studentScene = createStudentDashboard();
            if (studentScene != null) window.setScene(studentScene);
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

    private Scene createCoordinatorDashboard() {
        BorderPane layout = new BorderPane();
        VBox sidebar = new VBox(15);
        sidebar.setStyle(sidebarStyle);
        sidebar.setPrefWidth(240);
        Label logo = new Label("Coordinator Hub");
        logo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white; -fx-padding: 0 0 25 0;");
        
        Button dashBtn = new Button("📊 Eligibility Reports");
        Button manageStudBtn = new Button("👥 Manage Students");
        Button manageCompBtn = new Button("🏢 Manage Companies");
        Button logoutBtn = new Button("🚪 Sign Out");
        
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
        content.setStyle("-fx-background-color: #F8FAFC; -fx-padding: 30;");
        content.setAlignment(Pos.CENTER);
        Label title = new Label("Company Eligibility & Recruitment Reports");
        title.setStyle(headerStyle);
        
        Button viewReportsBtn = new Button("Generate Live Report");
        viewReportsBtn.setStyle(btnStyle);
        
        Label reportOutput = new Label("Click button to load eligibility data...");
        reportOutput.setStyle(cardStyle + "-fx-font-family: 'Consolas'; -fx-font-size: 12px;");
        reportOutput.setMinSize(650, 340);

        viewReportsBtn.setOnAction(e -> {
            try {
                List<Student> students = DataManager.loadStudents();
                List<Company> companies = DataManager.loadCompanies();
                StringBuilder sb = new StringBuilder();
                sb.append("==================================================\n");
                sb.append("         PLACEMENT ELIGIBILITY STATUS REPORT      \n");
                sb.append("==================================================\n\n");
                
                // HACKATHON DEMO FALLBACK: If CSV empty, load default data
                if (companies == null || companies.isEmpty()) {
                    sb.append("🏢 Company: TCS Ninja [Min CGPA: 7.0 | Req: Java]\n");
                    sb.append("   ✔️ Mahathi (ID: 101 - CGPA: 8.5)\n");
                    sb.append("   ✔️ Sneha (ID: 103 - CGPA: 9.1)\n\n");
                    
                    sb.append("🏢 Company: Infosys [Min CGPA: 8.0 | Req: Python]\n");
                    sb.append("   ✔️ Rahul (ID: 102 - CGPA: 8.2)\n");
                    sb.append("   ✔️ Anjali (ID: 107 - CGPA: 8.8)\n\n");
                    
                    sb.append("🏢 Company: Tech Mahindra [Min CGPA: 6.5 | Req: C++]\n");
                    sb.append("   ✔️ Karthik (ID: 104 - CGPA: 6.8)\n\n");
                } else {
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
        content.setAlignment(Pos.CENTER);
        Label title = new Label("Manage Student Records");
        title.setStyle(headerStyle);

        VBox card = new VBox(15);
        card.setStyle(cardStyle);
        card.setMaxWidth(400);

        Label lbl = new Label("Remove Student Profile");
        lbl.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
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
        content.setStyle("-fx-background-color: #F8FAFC; -fx-padding: 30;");
        content.setAlignment(Pos.CENTER);
        Label title = new Label("Manage Company Criteria");
        title.setStyle(headerStyle);

        VBox card = new VBox(12);
        card.setStyle(cardStyle);
        card.setMaxWidth(420);

        Label addLbl = new Label("Add Recruiting Company");
        addLbl.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        TextField compName = new TextField(); compName.setPromptText("Company Name");
        TextField minCgpa = new TextField(); minCgpa.setPromptText("Minimum CGPA");
        TextField reqSkill = new TextField(); reqSkill.setPromptText("Required Skill (e.g., Java)");
        Button addBtn = new Button("Add Company");
        addBtn.setStyle(btnStyle);
        
        Separator sep = new Separator();

        Label delLbl = new Label("Remove Company");
        delLbl.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
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

    private Scene createStudentDashboard() {
        Dialog<javafx.util.Pair<String, String>> loginDialog = new Dialog<>();
        loginDialog.setTitle("Secure Student Portal Login");
        loginDialog.setHeaderText("Enter Your Student ID and Secure Password");

        ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        loginDialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        TextField idField = new TextField(); idField.setPromptText("Student ID (e.g., 101)");
        PasswordField passwordField = new PasswordField(); passwordField.setPromptText("Password (default: 1234)");

        grid.add(new Label("Student ID:"), 0, 0); grid.add(idField, 1, 0);
        grid.add(new Label("Password:"), 0, 1); grid.add(passwordField, 1, 1);

        loginDialog.getDialogPane().setContent(grid);
        loginDialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new javafx.util.Pair<>(idField.getText().trim(), passwordField.getText().trim());
            }
            return null;
        });

        java.util.Optional<javafx.util.Pair<String, String>> result = loginDialog.showAndWait();
        if (!result.isPresent()) return null;

        String inputId = result.get().getKey();
        String inputPassword = result.get().getValue();

        List<Student> allStudents = DataManager.loadStudents();
        Student loggedInStudent = null;
        for (Student s : allStudents) {
            if (s.getId().trim().equalsIgnoreCase(inputId.trim())) { loggedInStudent = s; break; }
        }

        if (loggedInStudent == null && inputPassword.equals("1234")) {
            loggedInStudent = new Student(inputId, "Demo Student", 8.5, "Java");
        }

        if (!inputPassword.equals("1234")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Authentication Failed");
            alert.setHeaderText("Invalid Password!");
            alert.setContentText("Default password for demo accounts is '1234'.");
            alert.showAndWait();
            return null;
        }

        BorderPane layout = new BorderPane();
        VBox sidebar = new VBox(15);
        sidebar.setStyle(sidebarStyle);
        sidebar.setPrefWidth(240);
        Label logo = new Label("Portal: " + loggedInStudent.getName());
        logo.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: white; -fx-padding: 0 0 20 0;");
        
        Button aptTestBtn = new Button("💡 Aptitude Assessment");
        Button techTestBtn = new Button("💻 Technical Assessment");
        Button compPrepBtn = new Button("🏢 Company Prep Hub");
        Button logoutBtn = new Button("🚪 Sign Out");
        
        aptTestBtn.setStyle(sidebarBtnStyle); techTestBtn.setStyle(sidebarBtnStyle); 
        compPrepBtn.setStyle(sidebarBtnStyle); logoutBtn.setStyle(sidebarBtnStyle);
        sidebar.getChildren().addAll(logo, aptTestBtn, techTestBtn, compPrepBtn, logoutBtn);

        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER_RIGHT);
        topBar.setStyle("-fx-background-color: white; -fx-padding: 15 30; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.03), 5, 0, 0, 2);");
        
        Button profileBtn = new Button("👤 My Profile & Resume Sync");
        profileBtn.setStyle("-fx-background-color: #E2E8F0; -fx-text-fill: #1E293B; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 6; -fx-cursor: hand;");
        topBar.getChildren().add(profileBtn);

        VBox aptitudePanel = getAptitudeTestPanel();
        VBox technicalPanel = getTechnicalTestPanel();
        VBox companyPrepPanel = getCompanyPrepPanel();

        aptTestBtn.setOnAction(e -> layout.setCenter(getCenterWithTopBar(topBar, aptitudePanel)));
        techTestBtn.setOnAction(e -> layout.setCenter(getCenterWithTopBar(topBar, technicalPanel)));
        compPrepBtn.setOnAction(e -> layout.setCenter(getCenterWithTopBar(topBar, companyPrepPanel)));

        final Student activeStudent = loggedInStudent;
        profileBtn.setOnAction(e -> {
            Stage profileStage = new Stage();
            profileStage.setTitle("Student Profile: " + activeStudent.getName());
            VBox pRoot = new VBox(15);
            pRoot.setStyle(cardStyle); pRoot.setAlignment(Pos.CENTER);
            Label pTitle = new Label("Student Academic Profile");
            pTitle.setStyle(headerStyle);
            
            TextField pId = new TextField(activeStudent.getId());
            TextField pName = new TextField(activeStudent.getName());
            TextField pCgpa = new TextField(String.valueOf(activeStudent.getCgpa()));
            TextField pSkills = new TextField(activeStudent.getSkills());
            
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
                    pStatus.setText("Profile updated successfully!");
                } catch(Exception err) {
                    pStatus.setStyle("-fx-text-fill: #DC2626;");
                    pStatus.setText("Invalid entry format.");
                }
            });
            pRoot.getChildren().addAll(pTitle, new Label("Student ID:"), pId, new Label("Full Name:"), pName, new Label("CGPA:"), pCgpa, new Label("Primary Skills:"), pSkills, updateBtn, pStatus);
            profileStage.setScene(new Scene(pRoot, 400, 500));
            profileStage.show();
        });
        
        logoutBtn.setOnAction(e -> window.setScene(loginScene));
        layout.setLeft(sidebar);
        layout.setCenter(getCenterWithTopBar(topBar, aptitudePanel));
        return new Scene(layout, 950, 650);
    }

    private BorderPane getCenterWithTopBar(HBox topBar, VBox contentPanel) {
        BorderPane bp = new BorderPane();
        bp.setTop(topBar);
        bp.setCenter(contentPanel);
        return bp;
    }

    private VBox getAptitudeTestPanel() {
        VBox content = new VBox(20);
        content.setStyle("-fx-background-color: #F8FAFC; -fx-padding: 30;");
        content.setAlignment(Pos.CENTER);
        
        Label title = new Label("Quantitative & Logical Aptitude Round");
        title.setStyle(headerStyle);
        Label timerLabel = new Label("Time Left: 30:00");
        timerLabel.setStyle("-fx-text-fill: #DC2626; -fx-font-size: 14px; -fx-font-weight: bold;");
        
        Button startBtn = new Button("Start Aptitude Test");
        startBtn.setStyle(btnStyle);
        
        VBox quizCard = new VBox(15);
        quizCard.setStyle(cardStyle);
        quizCard.setAlignment(Pos.CENTER_LEFT);
        quizCard.setMaxWidth(650);
        
        Label qLbl = new Label("Click 'Start Aptitude Test' to begin.");
        qLbl.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        
        Button a = new Button(); Button b = new Button(); Button c = new Button(); Button d = new Button();
        String optStyle = "-fx-background-color: #F1F5F9; -fx-text-fill: #1E293B; -fx-padding: 10; -fx-font-size: 13px; -fx-cursor: hand; -fx-alignment: center-left;";
        a.setStyle(optStyle); b.setStyle(optStyle); c.setStyle(optStyle); d.setStyle(optStyle);
        a.setMinWidth(550); b.setMinWidth(550); c.setMinWidth(550); d.setMinWidth(550);
        
        VBox options = new VBox(10, a, b, c, d);
        options.setVisible(false);
        Label res = new Label();
        res.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #16A34A;");

        quizCard.getChildren().addAll(qLbl, options, res);
        
        int[] idx = {0}; int[] score = {0};
        startBtn.setOnAction(e -> {
            List<Question> tempQList = DataManager.loadQuestions();
            if(tempQList == null || tempQList.isEmpty()) {
                tempQList = new ArrayList<>();
                tempQList.add(new Question("What is 15% of 240?", "32", "36", "40", "48", "B"));
                tempQList.add(new Question("Find the next number in the series: 2, 6, 12, 20, 30, ?", "40", "42", "46", "50", "B"));
                tempQList.add(new Question("A man walks 5 km south, turns left and walks 3 km, then turns left again and walks 5 km. How far is he from the starting point?", "3 km", "5 km", "8 km", "10 km", "A"));
                tempQList.add(new Question("If a car travels 60 km in 1 hour 30 minutes, what is its average speed?", "40 km/h", "45 km/h", "50 km/h", "60 km/h", "A"));
                tempQList.add(new Question("What is the time complexity of binary search in a sorted array?", "O(1)", "O(n)", "O(log n)", "O(n log n)", "C"));
            }
            final List<Question> qList = tempQList; 
            startBtn.setVisible(false); options.setVisible(true);
            startTimerThread(timerLabel);
            loadQ(qList, idx[0], qLbl, a, b, c, d);
            
            javafx.event.EventHandler<javafx.event.ActionEvent> handler = ev -> {
                Button btn = (Button) ev.getSource();
                if(btn.getText().substring(0, 1).equals(qList.get(idx[0]).getCorrectOption())) score[0]++;
                idx[0]++;
                if(idx[0] < qList.size()) loadQ(qList, idx[0], qLbl, a, b, c, d);
                else { options.setVisible(false); qLbl.setText("Aptitude Assessment Completed!"); res.setText("Final Score: " + score[0] + "/" + qList.size()); }
            };
            a.setOnAction(handler); b.setOnAction(handler); c.setOnAction(handler); d.setOnAction(handler);
        });

        content.getChildren().addAll(title, timerLabel, startBtn, quizCard);
        return content;
    }

    private VBox getTechnicalTestPanel() {
        VBox content = new VBox(20);
        content.setStyle("-fx-background-color: #F8FAFC; -fx-padding: 30;");
        content.setAlignment(Pos.CENTER);
        
        Label title = new Label("Core Java & DSA Technical Assessment");
        title.setStyle(headerStyle);
        Label timerLabel = new Label("Time Left: 30:00");
        timerLabel.setStyle("-fx-text-fill: #DC2626; -fx-font-size: 14px; -fx-font-weight: bold;");
        
        Button startBtn = new Button("Start Technical Test");
        startBtn.setStyle(btnStyle);
        
        VBox quizCard = new VBox(15);
        quizCard.setStyle(cardStyle);
        quizCard.setAlignment(Pos.CENTER_LEFT);
        quizCard.setMaxWidth(650);
        
        Label qLbl = new Label("Click 'Start Technical Test' to begin.");
        qLbl.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        
        Button a = new Button(); Button b = new Button(); Button c = new Button(); Button d = new Button();
        String optStyle = "-fx-background-color: #F1F5F9; -fx-text-fill: #1E293B; -fx-padding: 10; -fx-font-size: 13px; -fx-cursor: hand; -fx-alignment: center-left;";
        a.setStyle(optStyle); b.setStyle(optStyle); c.setStyle(optStyle); d.setStyle(optStyle);
        a.setMinWidth(550); b.setMinWidth(550); c.setMinWidth(550); d.setMinWidth(550);
        
        VBox options = new VBox(10, a, b, c, d);
        options.setVisible(false);
        Label res = new Label();
        res.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #16A34A;");

        quizCard.getChildren().addAll(qLbl, options, res);
        
        List<Question> techList = new ArrayList<>();
        techList.add(new Question("Which data structure is ideal for implementing Breadth-First Search (BFS)?", "Stack", "Queue", "Priority Queue", "Tree", "B"));
        techList.add(new Question("Which of the following features is not supported by Java?", "Dynamic Dispatch", "Multiple Inheritance with Classes", "Anonymous Classes", "Encapsulation", "B"));
        techList.add(new Question("What is the default value of a boolean variable in Java?", "true", "false", "null", "0", "B"));
        techList.add(new Question("What is the parent class of all classes in Java?", "System", "Object", "Class", "String", "B"));
        techList.add(new Question("Which collection class allows duplicate elements and maintains insertion order?", "HashSet", "TreeSet", "ArrayList", "HashMap", "C"));

        int[] idx = {0}; int[] score = {0};
        startBtn.setOnAction(e -> {
            startBtn.setVisible(false); options.setVisible(true);
            startTimerThread(timerLabel);
            loadQ(techList, idx[0], qLbl, a, b, c, d);
            
            javafx.event.EventHandler<javafx.event.ActionEvent> handler = ev -> {
                Button btn = (Button) ev.getSource();
                if(btn.getText().substring(0, 1).equals(techList.get(idx[0]).getCorrectOption())) score[0]++;
                idx[0]++;
                if(idx[0] < techList.size()) loadQ(techList, idx[0], qLbl, a, b, c, d);
                else { options.setVisible(false); qLbl.setText("Technical Assessment Completed!"); res.setText("Final Score: " + score[0] + "/" + techList.size()); }
            };
            a.setOnAction(handler); b.setOnAction(handler); c.setOnAction(handler); d.setOnAction(handler);
        });

        content.getChildren().addAll(title, timerLabel, startBtn, quizCard);
        return content;
    }

    private VBox getCompanyPrepPanel() {
        VBox content = new VBox(20);
        content.setStyle("-fx-background-color: #F8FAFC; -fx-padding: 30;");
        content.setAlignment(Pos.CENTER);
        
        Label title = new Label("Target Company Preparation Hub");
        title.setStyle(headerStyle);
        Label sub = new Label("View hiring criteria, interview guides, and practice materials.");
        sub.setStyle("-fx-text-fill: #64748B; -fx-font-size: 13px;");

        VBox card = new VBox(15);
        card.setStyle(cardStyle);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setMaxWidth(650);

        Label info = new Label("🏢 Active Recruiting Partner Guidelines:\n\n" +
                "• Tech Mahindra: Focus on OOPs, SQL queries, and Verbal Reasoning.\n" +
                "• TCS Ninja: Coding questions on Strings, Arrays & Quantitative Aptitude.\n" +
                "• Infosys Power Programmer: Advanced DSA, DP, and System Design rounds.\n\n" +
                "💡 Tip: Ensure your CGPA and primary skills match company criteria for automated interview scheduling.");
        info.setWrapText(true);
        info.setStyle("-fx-font-size: 13px; -fx-line-spacing: 5;");

        card.getChildren().addAll(info);
        content.getChildren().addAll(title, sub, card);
        return content;
    }

    private void loadQ(List<Question> qList, int i, Label qL, Button a, Button b, Button c, Button d) {
        Question q = qList.get(i);
        qL.setText("Q" + (i + 1) + ". " + q.getQuestionText());
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
                regStatus.setText("Registration Successful! Default password: 1234");
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

    public static void main(String[] args) {
        launch(args);
    }
}