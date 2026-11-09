import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.List;

public class MainApp extends Application {

    private Stage window;
    private Scene loginScene, studentScene, coordinatorScene;

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        window.setTitle("Smart College Placement Manager");

        // ==========================================
        // --- SCREEN 1: LOGIN DASHBOARD ---
        // ==========================================
        Label titleLabel = new Label("Placement Preparation System");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Button studentLoginBtn = new Button("Student Login");
        Button coordinatorLoginBtn = new Button("Coordinator Login");
        
        studentLoginBtn.setMinWidth(200);
        coordinatorLoginBtn.setMinWidth(200);

        studentLoginBtn.setOnAction(e -> window.setScene(studentScene));
        coordinatorLoginBtn.setOnAction(e -> window.setScene(coordinatorScene));

        VBox loginLayout = new VBox(20);
        loginLayout.setAlignment(Pos.CENTER);
        loginLayout.getChildren().addAll(titleLabel, studentLoginBtn, coordinatorLoginBtn);
        loginScene = new Scene(loginLayout, 600, 500);

        // ==========================================
        // --- SCREEN 2: STUDENT DASHBOARD (TEST) ---
        // ==========================================
        Label studentTitle = new Label("Student Dashboard - Practice Test");
        studentTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        Button startTestBtn = new Button("Start Timer & Take Test");
        Label timerLabel = new Label("Time Left: 30:00");
        timerLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px; -fx-font-weight: bold;");
        
        Label questionLabel = new Label("Click Start to begin.");
        questionLabel.setStyle("-fx-font-size: 14px;");
        
        Button optA = new Button(); Button optB = new Button();
        Button optC = new Button(); Button optD = new Button();
        optA.setMinWidth(300); optB.setMinWidth(300); optC.setMinWidth(300); optD.setMinWidth(300);
        
        VBox optionsBox = new VBox(10, optA, optB, optC, optD);
        optionsBox.setAlignment(Pos.CENTER);
        optionsBox.setVisible(false); // Hide until test starts
        
        Label resultLabel = new Label();
        resultLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: green;");
        Button backBtn1 = new Button("Back to Login");

        // Arrays used to allow modification inside lambda expressions
        int[] currentQ = {0};
        int[] score = {0};
        
        startTestBtn.setOnAction(e -> {
            List<Question> qList = DataManager.loadQuestions();
            if (qList.isEmpty()) {
                questionLabel.setText("Error: No questions found in questions.csv!");
                return;
            }
            
            startTestBtn.setVisible(false);
            optionsBox.setVisible(true);
            currentQ[0] = 0;
            score[0] = 0;
            resultLabel.setText("");
            
            startTimerThread(timerLabel);
            loadQuestionUI(qList, currentQ[0], questionLabel, optA, optB, optC, optD);
            
            // Logic for answering questions
            javafx.event.EventHandler<javafx.event.ActionEvent> answerHandler = event -> {
                Button clicked = (Button) event.getSource();
                String selected = clicked.getText().substring(0, 1); // Gets "A", "B", "C", or "D"
                
                if (selected.equals(qList.get(currentQ[0]).getCorrectOption())) {
                    score[0]++;
                }
                
                currentQ[0]++;
                if (currentQ[0] < qList.size()) {
                    loadQuestionUI(qList, currentQ[0], questionLabel, optA, optB, optC, optD);
                } else {
                    optionsBox.setVisible(false);
                    questionLabel.setText("Test Complete!");
                    resultLabel.setText("Final Score: " + score[0] + " out of " + qList.size());
                }
            };
            
            optA.setOnAction(answerHandler);
            optB.setOnAction(answerHandler);
            optC.setOnAction(answerHandler);
            optD.setOnAction(answerHandler);
        });
        
        backBtn1.setOnAction(e -> {
            startTestBtn.setVisible(true);
            optionsBox.setVisible(false);
            questionLabel.setText("Click Start to begin.");
            resultLabel.setText("");
            window.setScene(loginScene);
        });

        VBox studentLayout = new VBox(15);
        studentLayout.setAlignment(Pos.CENTER);
        studentLayout.getChildren().addAll(studentTitle, timerLabel, startTestBtn, questionLabel, optionsBox, resultLabel, backBtn1);
        studentScene = new Scene(studentLayout, 600, 500);

        // ==========================================
        // --- SCREEN 3: COORDINATOR DASHBOARD ---
        // ==========================================
        Label coordTitle = new Label("Coordinator Dashboard - Reports");
        coordTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        Button viewReportsBtn = new Button("View Eligibility Reports");
        Button backBtn2 = new Button("Back to Login");
        
        Label reportOutput = new Label("Click the button to load data...");
        reportOutput.setStyle("-fx-padding: 10px; -fx-border-color: gray; -fx-background-color: white;");
        reportOutput.setMinSize(300, 150);

        viewReportsBtn.setOnAction(e -> {
            try {
                List<Student> students = DataManager.loadStudents();
                List<Company> companies = DataManager.loadCompanies();
                
                StringBuilder sb = new StringBuilder();
                sb.append("--- ELIGIBILITY REPORT ---\n");
                
                if (companies.isEmpty() || students.isEmpty()) {
                    sb.append("No data found. Check CSV files.");
                } else {
                    for (Company c : companies) {
                        sb.append("Company: ").append(c.getCompanyName()).append("\n");
                        List<Student> eligible = PlacementLogic.getEligibleStudents(c, students);
                        for (Student s : eligible) {
                            sb.append("  -> ").append(s.getName()).append(" (CGPA: ").append(s.getCgpa()).append(")\n");
                        }
                        sb.append("\n");
                    }
                }
                reportOutput.setText(sb.toString());
            } catch (Exception ex) {
                reportOutput.setText("Error occurred. Check terminal.");
            }
        });

        backBtn2.setOnAction(e -> {
            reportOutput.setText("Click the button to load data...");
            window.setScene(loginScene);
        });

        VBox coordLayout = new VBox(20);
        coordLayout.setAlignment(Pos.CENTER);
        coordLayout.getChildren().addAll(coordTitle, viewReportsBtn, reportOutput, backBtn2);
        coordinatorScene = new Scene(coordLayout, 600, 500);

        window.setScene(loginScene);
        window.show();
    }

    // Helper method to update the UI text for each question
    private void loadQuestionUI(List<Question> qList, int index, Label qLabel, Button a, Button b, Button c, Button d) {
        Question q = qList.get(index);
        qLabel.setText("Q" + (index + 1) + ". " + q.getQuestionText() + " [" + q.getType() + "]");
        a.setText("A) " + q.getOptA());
        b.setText("B) " + q.getOptB());
        c.setText("C) " + q.getOptC());
        d.setText("D) " + q.getOptD());
    }

    // Multithreading Timer
    private void startTimerThread(Label timerLabel) {
        Thread timerThread = new Thread(() -> {
            try {
                int secondsLeft = 1800; 
                while (secondsLeft > 0) {
                    final int displayTime = secondsLeft;
                    Platform.runLater(() -> {
                        int minutes = displayTime / 60;
                        int seconds = displayTime % 60;
                        timerLabel.setText(String.format("Time Left: %02d:%02d", minutes, seconds));
                    });
                    Thread.sleep(1000);
                    secondsLeft--;
                }
                Platform.runLater(() -> timerLabel.setText("Time's Up!"));
            } catch (InterruptedException e) {
                System.out.println("Timer interrupted.");
            }
        });
        timerThread.setDaemon(true);
        timerThread.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}