module com.fu.ch08 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;      // T5
    requires javafx.media;    // T5

    // FXMLLoader cần reflection vào controller (@FXML private field/method)
    opens com.fu.ch08.b4 to javafx.fxml;

    exports com.fu.ch08.common;
    exports com.fu.ch08.b1;
    exports com.fu.ch08.b2;
    exports com.fu.ch08.b3;
    exports com.fu.ch08.b4;
}
