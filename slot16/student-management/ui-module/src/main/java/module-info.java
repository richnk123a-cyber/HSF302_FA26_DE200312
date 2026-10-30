module vonguyenkhanh.example.uimodule {

    requires javafx.controls;
    requires javafx.fxml;
    requires vonguyenkhanh.example.datamodule;

    opens vonguyenkhanh.example.uimodule to javafx.fxml;
    exports vonguyenkhanh.example.uimodule;
}