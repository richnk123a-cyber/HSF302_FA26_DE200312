package vonguyenkhanh.example.uimodule;

import vonguyenkhanh.example.StudentManagementApplication;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class JavaFxApplication extends Application {

    private ConfigurableApplicationContext context;

    @Override
    public void init() {
        // TODO 4.3a: context = new SpringApplicationBuilder(StudentManagementApplication.class)
        //                .web(WebApplicationType.NONE).headless(false).run(...)
        context = new SpringApplicationBuilder(StudentManagementApplication.class)
                .web(WebApplicationType.NONE)
                .headless(false)
                .run();
    }

    @Override
    public void start(Stage stage) throws Exception {
        // TODO 4.3b: FXMLLoader "/student-view.fxml" + loader.setControllerFactory(context::getBean)
        // TODO 4.3c: Scene 1000x600 + "/style.css"; title "Student Management System"; min 900x500; show
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/student-view.fxml"));
        loader.setControllerFactory(context::getBean);
        Parent root = loader.load();

        Scene scene = new Scene(root, 1000, 600);
        var cssUrl = getClass().getResource("/style.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }

        stage.setTitle("Student Management System");
        stage.setMinWidth(900);
        stage.setMinHeight(500);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        // TODO 4.3d: context.close(); Platform.exit();
        if (context != null) {
            context.close();
        }
        Platform.exit();
    }
}
