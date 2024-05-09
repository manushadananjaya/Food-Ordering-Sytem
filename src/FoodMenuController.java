import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.util.function.Consumer;

public class FoodMenuController {
    private Stage stage;
    private ObservableList<Food> selectedFoods;
    private ObservableList<Food> addedFoods;
    private Consumer<ObservableList<Food>> foodSelectionCallback;

    public FoodMenuController(Stage stage) {
        this.stage = stage;
        selectedFoods = FXCollections.observableArrayList();
        addedFoods = FXCollections.observableArrayList();
    }

    public Scene getScene() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        ListView<Food> foodListView = new ListView<>();
        ObservableList<Food> foodItems = FXCollections.observableArrayList(
                new Food("Burger", 5.99, false),
                new Food("Pizza", 8.99, true),
                new Food("Salad", 4.99, true),
                new Food("Fried Rice", 5.99, true),
                new Food("Biriyani", 8.99, false),
                new Food("Kottu", 4.99, false)
        );
        foodListView.setItems(foodItems);

        // Enable multiple selections
        foodListView.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);

        // Customize how the items are displayed in the ListView
        foodListView.setCellFactory(lv -> new FoodListCell());

        Button confirmButton = new Button("Add Food Items");
        confirmButton.setOnAction(this::handleConfirm);

        Button showAddedItemsButton = new Button("Show Added Items");
        showAddedItemsButton.setOnAction(this::showAddedItems);
        
        Button backButton = new Button("Back");
        backButton.setOnAction(this::goBack);

        root.getChildren().addAll(foodListView, confirmButton, showAddedItemsButton, backButton);

        return new Scene(root, 400, 300);
    }

    private void handleConfirm(ActionEvent event) {
        // Clear the selected items list
        selectedFoods.clear();
    
        // Add selected food items to the selectedFoods list
        ListView<Food> foodListView = (ListView<Food>) stage.getScene().getRoot().getChildrenUnmodifiable().get(0);
        ObservableList<Food> selectedItems = foodListView.getSelectionModel().getSelectedItems();
        selectedFoods.addAll(selectedItems);
        
        // Add selected items to the addedFoods list
        addedFoods.addAll(selectedItems);
    
        // Open a separate window for selecting quantity
        openQuantityWindow(selectedItems);
    
        // Invoke the callback with the selected items
        if (foodSelectionCallback != null) {
            foodSelectionCallback.accept(selectedItems);
        }
    }
    

    private void openQuantityWindow(ObservableList<Food> selectedItems) {
        Stage quantityStage = new Stage();
        quantityStage.setTitle("Select Quantity");

        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        ObservableList<TextField> quantityFields = FXCollections.observableArrayList(); // Store quantity fields for later retrieval

        for (Food food : selectedItems) {
            Button nameLabel = new Button(food.getName());
            Button priceLabel = new Button(String.format("$%.2f", food.getPrice()));

            // Input field for quantity
            TextField quantityField = new TextField();
            quantityField.setPromptText("Enter quantity");
            quantityFields.add(quantityField); // Add quantity field to the list

            root.getChildren().addAll(nameLabel, priceLabel, quantityField);
        }

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            // Save the quantity for each food item
            for (int i = 0; i < selectedItems.size(); i++) {
                Food food = selectedItems.get(i);
                int quantity = Integer.parseInt(quantityFields.get(i).getText()); // Retrieve quantity from the corresponding field
                food.setQuantity(quantity);
            }

            quantityStage.close();

            // Invoke the callback with the selected items
            if (foodSelectionCallback != null) {
                foodSelectionCallback.accept(selectedItems);
            }
        });

        root.getChildren().add(saveButton);

        Scene scene = new Scene(root, 300, 200);
        quantityStage.setScene(scene);
        quantityStage.show();
    }

    private void showAddedItems(ActionEvent event) {
        Stage addedItemsStage = new Stage();
        addedItemsStage.setTitle("Added Items");
    
        ListView<String> addedItemsListView = new ListView<>(); // Use ListView<String> instead of ListView<Food>
        ObservableList<String> addedItems = FXCollections.observableArrayList(); // Use ObservableList<String> instead of ObservableList<Food>
    
        // Populate the added items list
        for (Food food : addedFoods) {
            addedItems.add(food.getName() + " - Quantity: " + food.getQuantity()); // Display name and quantity
        }
    
        addedItemsListView.setItems(addedItems);
    
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
    
        Button confirmButton = new Button("Confirm");
        confirmButton.setOnAction(e -> {
            // Proceed to the food cart controller screen
            FoodCartController foodCartController = new FoodCartController(stage, addedFoods);
            stage.setScene(foodCartController.getScene());
            addedItemsStage.close();
        });
    
        root.getChildren().addAll(new Label("Added Items"), addedItemsListView, confirmButton);
    
        Scene scene = new Scene(root, 300, 200);
        addedItemsStage.setScene(scene);
        addedItemsStage.initModality(Modality.APPLICATION_MODAL);
        addedItemsStage.show();
    }
    
    private void goBack(ActionEvent event) {
        HomeController home = new HomeController(stage);
        stage.setScene(home.getScene());
    }

    public void setFoodSelectionCallback(Consumer<ObservableList<Food>> callback) {
        this.foodSelectionCallback = callback;
    }

    public void setInitialSelectedAndAdded( ObservableList<Food> addedFoods) {
        
        this.addedFoods = addedFoods;
        
    }
}



