// MainApp.java
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        HomeController homeController = new HomeController(primaryStage);
        primaryStage.setScene(homeController.getScene());
        primaryStage.setTitle("Food Ordering App");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
