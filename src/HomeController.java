import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HomeController {
    private Stage stage;

    public HomeController(Stage stage) {
        this.stage = stage;
    }
    

    public Scene getScene() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Welcome to Food Ordering App");
        Button displayHotelInfoButton = new Button("Display Hotel Info");
        Button displayFoodMenuButton = new Button("Display Food Items Menu");
        Label orderLabel = new Label("Want to order foods?");

        root.getChildren().addAll(titleLabel, displayHotelInfoButton, displayFoodMenuButton, orderLabel);

        displayHotelInfoButton.setOnAction(this::handleDisplayHotelInfo);
        displayFoodMenuButton.setOnAction(this::handleDisplayFoodMenu);

        return new Scene(root, 400, 300);
    }

    private void handleDisplayHotelInfo(ActionEvent event) {
        HotelInfoScreen hotelInfoScreen = new HotelInfoScreen(stage);
        hotelInfoScreen.display();
    }

    private void handleDisplayFoodMenu(ActionEvent event) {
        FoodMenuController foodMenuController = new FoodMenuController(stage);
        stage.setScene(foodMenuController.getScene());
    }
}

class HotelInfoScreen {
    private Stage stage;

    public HotelInfoScreen(Stage stage) {
        this.stage = stage;
    }

    public void display() {
        Stage hotelInfoStage = new Stage();
        hotelInfoStage.initModality(Modality.APPLICATION_MODAL);
        hotelInfoStage.setTitle("Hotel Information");

        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        Label hotelInfoLabel = new Label("Welcome to Our Hotel!");
        Label details = new Label("We have a good rating of 5 for our hotel.We have both vegitarian and non Vegitarian food in our hotel...");
        Label addressLabel = new Label("Address: 123 Main Street");
        Label contactLabel = new Label("Contact: 555-1234");

        root.getChildren().addAll(hotelInfoLabel, addressLabel, contactLabel,details);

        Scene scene = new Scene(root, 700, 200);
        hotelInfoStage.setScene(scene);
        hotelInfoStage.show();
    }
}
