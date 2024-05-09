import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PaymentScreen {
    private Stage stage;
    private ObservableList<Food> addedFoods;
    private Scene previousScene;
    private Label confirmationLabel; // Label to display the confirmation message

    public PaymentScreen(Stage stage, ObservableList<Food> addedFoods, Scene previousScene) {
        this.stage = stage;
        this.addedFoods = addedFoods;
        this.previousScene = previousScene;
    }

    public Scene getScene() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        // Calculate total bill
        double totalBill = calculateTotalBill();

        Label totalBillLabel = new Label("Total Bill: $" + String.format("%.2f", totalBill));

        // Detailed bill
        VBox detailedBillBox = new VBox(5);
        detailedBillBox.setAlignment(Pos.CENTER_LEFT);
        detailedBillBox.setPadding(new Insets(10));
        Label detailedBillLabel = new Label("Detailed Bill:");
        detailedBillBox.getChildren().add(detailedBillLabel);
        for (Food food : addedFoods) {
            Label itemLabel = new Label(food.getName() + " - $" + String.format("%.2f", food.getPrice()) + " x " + food.getQuantity());
            detailedBillBox.getChildren().add(itemLabel);
        }

        Label cardNumberLabel = new Label("Card Number:");
        TextField cardNumberField = new TextField();

        Label expiryDateLabel = new Label("Expiry Date:");
        TextField expiryDateField = new TextField();

        Label cvvLabel = new Label("CVV:");
        TextField cvvField = new TextField();

        Button confirmPaymentButton = new Button("Confirm Payment");
        confirmPaymentButton.setOnAction(event -> {
            // Placeholder logic for confirming payment
            confirmationLabel.setText("Payment confirmed!");
        });

        Button cancelPaymentButton = new Button("Cancel Payment");
        cancelPaymentButton.setOnAction(event -> {
            // Go back to the previous scene
            stage.setScene(previousScene);
        });

        // Label to display the confirmation message
        confirmationLabel = new Label();

        root.getChildren().addAll(totalBillLabel, detailedBillBox, cardNumberLabel, cardNumberField, expiryDateLabel, expiryDateField,
                cvvLabel, cvvField, confirmPaymentButton, cancelPaymentButton, confirmationLabel);

        return new Scene(root, 400, 400);
    }

    private double calculateTotalBill() {
        double total = 0.0;
        for (Food food : addedFoods) {
            total += food.getPrice() * food.getQuantity();
        }
        return total;
    }
}
