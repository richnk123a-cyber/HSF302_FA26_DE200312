package vonguyenkhanh.example.uimodule.controller;

import vonguyenkhanh.example.datamodule.pojo.Student;
import vonguyenkhanh.example.datamodule.service.StudentService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.stereotype.Component;

@Component
public class StudentController {

    @FXML private TableView<Student> tableView;
    @FXML private TextField searchField, idField, nameField, emailField, ageField;
    @FXML private Button btnUpdate, btnDelete;
    @FXML private Label messageLabel, statusLabel;

    private final StudentService studentService;

    // TODO 5.1: ObservableList
    private final ObservableList<Student> data = FXCollections.observableArrayList();

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // TODO 5.2: initialize
    @FXML
    public void initialize() {
        tableView.setItems(data);
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                fillForm(newVal);
            }
        });
        btnUpdate.disableProperty().bind(tableView.getSelectionModel().selectedItemProperty().isNull());
        btnDelete.disableProperty().bind(tableView.getSelectionModel().selectedItemProperty().isNull());
        loadData();
    }

    // TODO 5.3: loadData
    private void loadData() {
        try {
            data.setAll(studentService.findAll());
            statusLabel.setText("Loaded " + data.size() + " students");
        } catch (Exception e) {
            showError("Cannot load data: " + e.getMessage());
            statusLabel.setText("Database error");
        }
    }

    // TODO 5.4: handleSearch
    @FXML
    private void handleSearch() {
        try {
            String kw = searchField.getText();
            data.setAll(studentService.search(kw));
            statusLabel.setText("Found " + data.size() + " students");
        } catch (Exception e) {
            showError("Search failed: " + e.getMessage());
        }
    }

    // TODO 5.4: handleRefresh
    @FXML
    private void handleRefresh() {
        searchField.clear();
        loadData();
    }

    // TODO 5.5: fillForm
    private void fillForm(Student s) {
        if (s != null) {
            idField.setText(s.getId() == null ? "" : String.valueOf(s.getId()));
            nameField.setText(s.getName() == null ? "" : s.getName());
            emailField.setText(s.getEmail() == null ? "" : s.getEmail());
            ageField.setText(s.getAge() == null ? "" : String.valueOf(s.getAge()));
            messageLabel.setText("");
        }
    }

    // TODO 5.5: showInfo
    private void showInfo(String msg) {
        messageLabel.setText(msg);
        messageLabel.setStyle("-fx-text-fill: #27ae60;");
    }

    // TODO 5.5: showError
    private void showError(String msg) {
        messageLabel.setText(msg);
        messageLabel.setStyle("-fx-text-fill: #e74c3c;");
    }

    @FXML private void handleAdd()     { }
    @FXML private void handleUpdate()  { }
    @FXML private void handleDelete()  { }
    @FXML private void handleClear()   { }
}
