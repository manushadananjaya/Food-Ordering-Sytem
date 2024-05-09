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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class FoodMenuController {
    private Stage stage;
    private ObservableList<Food> selectedFoods;
    private ObservableList<Food> addedFoods;
    private Consumer<ObservableList<Food>> foodSelectionCallback;
    private ListView<Food> foodListView;
    private ObservableList<Food> originalFoodItems;

    public FoodMenuController(Stage stage) {
        this.stage = stage;
        this.selectedFoods = FXCollections.observableArrayList();
        this.addedFoods = FXCollections.observableArrayList();
        this.originalFoodItems = FXCollections.observableArrayList(
                new Food("Burger", 5.99, false),
                new Food("Pizza", 8.99, true),
                new Food("Salad", 4.99, true),
                new Food("Fried Rice", 5.99, true),
                new Food("Biriyani", 8.99, false),
                new Food("Kottu", 4.99, false)
        );
    }

    public Scene getScene() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        // Search TextField
        TextField searchField = new TextField();
        searchField.setPromptText("Search Foods");

        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> filterFoods(searchField.getText()));

        HBox searchBox = new HBox(10);
        searchBox.getChildren().addAll(searchField, searchButton);

        foodListView = new ListView<>();
        foodListView.setItems(originalFoodItems);

        // Enable multiple selections
        foodListView.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);

        // Customize how the items are displayed in the ListView
        foodListView.setCellFactory(lv -> new FoodListCell());

        Button confirmButton = new Button("Add Selected Food Items");
        confirmButton.setOnAction(this::handleConfirm);

        Button showAddedItemsButton = new Button("Show Added Items");
        showAddedItemsButton.setOnAction(this::showAddedItems);

        Button backButton = new Button("Back");
        backButton.setOnAction(this::goBack);

        Button sortLowToHighButton = createStyledButton("Sort by Price (Low to High)");
        sortLowToHighButton.setOnAction(e -> {
            originalFoodItems.sort(FoodComparators.sortByPriceLowToHigh());
        });

        Button sortHighToLowButton = createStyledButton("Sort by Price (High to Low)");
        sortHighToLowButton.setOnAction(e -> {
            originalFoodItems.sort(FoodComparators.sortByPriceHighToLow());
        });

        Button sortVegetarianButton = createStyledButton("Sort by Vegetarian");
        sortVegetarianButton.setOnAction(e -> {
            originalFoodItems.sort(FoodComparators.sortByVegetarian());
        });

        Button sortNonVegetarianButton = createStyledButton("Sort by Non-Vegetarian");
        sortNonVegetarianButton.setOnAction(e -> {
            originalFoodItems.sort(FoodComparators.sortByNonVegetarian());
        });

        HBox sortingButtons = new HBox(10);
        sortingButtons.getChildren().addAll(sortLowToHighButton, sortHighToLowButton, sortVegetarianButton, sortNonVegetarianButton);

        root.getChildren().addAll(searchBox, foodListView, sortingButtons, confirmButton, showAddedItemsButton, backButton);

        return new Scene(root, 800, 800);
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 5px;");
        return button;
    }

    private void handleConfirm(ActionEvent event) {
        // Clear the selected items list
        selectedFoods.clear();

        // Add selected food items to the selectedFoods list
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

    public void setInitialSelectedAndAdded(ObservableList<Food> addedFoods) {
        this.addedFoods = addedFoods;
    }

    private void filterFoods(String searchText) {
        ObservableList<Food> filteredList = FXCollections.observableArrayList();
        for (Food food : originalFoodItems) {
            if (food.getName().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(food);
            }
        }
        foodListView.setItems(filteredList);
    }
}
