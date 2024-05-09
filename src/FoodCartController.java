import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FoodCartController {
    private Stage stage;
    private ObservableList<Food> addedFoods;

    public FoodCartController(Stage stage, ObservableList<Food> addedFoods) {
        this.stage = stage;
        this.addedFoods = addedFoods;
    }

    public Scene getScene() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        ListView<String> cartListView = new ListView<>();
        updateListView(cartListView); // Update ListView initially

        Button proceedToPaymentButton = new Button("Proceed to Payment");
        Button deleteButton = new Button("Delete Food Items");
        Button addMoreFoodsButton = new Button("Add more Foods");
        Button backButton = new Button("Back to Food Menu");

        proceedToPaymentButton.setOnAction(this::proceedToPayment);
        deleteButton.setOnAction(this::deleteFoodItems);
        addMoreFoodsButton.setOnAction(this::addMoreFoods);
        backButton.setOnAction(this::goBack);

        root.getChildren().addAll(cartListView, proceedToPaymentButton, deleteButton, addMoreFoodsButton, backButton);

        return new Scene(root, 400, 300);
    }

    private void proceedToPayment(ActionEvent event) {
        Scene previousScene = stage.getScene(); // Get the current scene

        // Create the PaymentScreen object with the stage, addedFoods, and previousScene
        PaymentScreen paymentScreen = new PaymentScreen(stage, addedFoods, previousScene);
        stage.setScene(paymentScreen.getScene());
    }

    private void deleteFoodItems(ActionEvent event) {
        // Remove selected items from the added food list
        ListView<String> cartListView = (ListView<String>) stage.getScene().getRoot().getChildrenUnmodifiable().get(0);
        ObservableList<String> selectedItems = cartListView.getSelectionModel().getSelectedItems();
        for (String selectedItem : selectedItems) {
            // Extract the name from the string and find the corresponding food item to remove
            String name = selectedItem.split(" - Quantity: ")[0];
            for (Food food : addedFoods) {
                if (food.getName().equals(name)) {
                    addedFoods.remove(food);
                    break;
                }
            }
        }
        // Update the ListView
        updateListView(cartListView);
    }

    private void addMoreFoods(ActionEvent event) {
        FoodMenuController foodMenuController = new FoodMenuController(stage);
        foodMenuController.setFoodSelectionCallback(this::handleFoodSelection);
        stage.setScene(foodMenuController.getScene());
    }

    private void goBack(ActionEvent event) {
        goBackToFoodMenu();
    }

    private void handleFoodSelection(ObservableList<Food> selectedItems) {
        // Add newly selected food items to the addedFoods list
        addedFoods.addAll(selectedItems);

        // Redirect to the FoodCartController with both selected and added foods
        FoodCartController foodCartController = new FoodCartController(stage, addedFoods);
        stage.setScene(foodCartController.getScene());
    }

    private void updateListView(ListView<String> listView) {
        ObservableList<String> items = FXCollections.observableArrayList();
        for (Food food : addedFoods) {
            items.add(food.getName() + " - Quantity: " + food.getQuantity());
        }
        listView.getItems().clear(); // Clear the ListView before updating
        listView.setItems(items);
    }

    private void goBackToFoodMenu() {
        FoodMenuController foodMenuController = new FoodMenuController(stage);
        stage.setScene(foodMenuController.getScene());
    }
}
