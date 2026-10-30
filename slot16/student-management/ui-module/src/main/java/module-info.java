module vonguyenkhanh.example.uimodule {
    requires javafx.controls;
    requires javafx.fxml;


    opens vonguyenkhanh.example.uimodule to javafx.fxml;
    exports vonguyenkhanh.example.uimodule;
}