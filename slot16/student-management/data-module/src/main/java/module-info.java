module vonguyenkhanh.example.datamodule {
    requires javafx.controls;
    requires javafx.fxml;


    opens vonguyenkhanh.example.datamodule to javafx.fxml;
    exports vonguyenkhanh.example.datamodule;
}