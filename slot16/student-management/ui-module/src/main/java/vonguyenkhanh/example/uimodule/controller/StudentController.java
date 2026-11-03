package vonguyenkhanh.example.uimodule.controller;

import vonguyenkhanh.example.datamodule.exception.StudentNotFoundException;
import vonguyenkhanh.example.datamodule.exception.ValidationException;
import vonguyenkhanh.example.datamodule.pojo.Student;
import vonguyenkhanh.example.datamodule.service.StudentService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.stereotype.Component;

import java.util.Optional;

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

    // TODO 6.1: readForm
    private Student readForm() {
        String name = nameField.getText();
        String email = emailField.getText();
        String ageStr = ageField.getText();

        if (ageStr == null || ageStr.isBlank()) {
            throw new ValidationException("Age is required");
        }

        int age;
        try {
            age = Integer.parseInt(ageStr.trim());
        } catch (NumberFormatException e) {
            throw new ValidationException("Age must be a number");
        }

        return new Student(name, email, age);
    }

    // TODO 6.2: handleAdd
    @FXML
    private void handleAdd() {
        try {
            Student s = readForm();
            Student created = studentService.create(s);
            showInfo("Added: " + created.getName());
            handleClear();
            loadData();
        } catch (ValidationException | StudentNotFoundException e) {
            showError(e.getMessage());
        } catch (Exception e) {
            showError("System error: " + e.getMessage());
        }
    }

    // TODO 6.3: handleUpdate
    @FXML
    private void handleUpdate() {
        Student selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }

        try {
            Student edit = readForm();
            edit.setId(selected.getId());
            Student updated = studentService.update(edit);
            showInfo("Updated: " + updated.getName());
            handleClear();
            loadData();
        } catch (ValidationException | StudentNotFoundException e) {
            showError(e.getMessage());
        } catch (Exception e) {
            showError("System error: " + e.getMessage());
        }
    }

    // TODO 6.4: handleDelete
    @FXML
    private void handleDelete() {
        Student selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText(null);
        alert.setContentText("Delete student \"" + selected.getName() + "\"?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                studentService.delete(selected.getId());
                showInfo("Deleted: " + selected.getName());
                handleClear();
                loadData();
            } catch (ValidationException | StudentNotFoundException e) {
                showError(e.getMessage());
            } catch (Exception e) {
                showError("System error: " + e.getMessage());
            }
        }
    }

    // TODO 6.5: handleClear
    @FXML
    private void handleClear() {
        idField.clear();
        nameField.clear();
        emailField.clear();
        ageField.clear();
        tableView.getSelectionModel().clearSelection();
        nameField.requestFocus();
    }
}
