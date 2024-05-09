import javafx.scene.control.ListCell;

public class FoodListCell extends ListCell<Food> {
    @Override
    protected void updateItem(Food food, boolean empty) {
        super.updateItem(food, empty);

        if (empty || food == null) {
            setText(null);
        } else {
            String vegetarianLabel = food.isVegetarian() ? "Vegetarian" : "Non-Vegetarian";
            setText(String.format("%s - $%.2f     Type - %s", food.getName(), food.getPrice(), vegetarianLabel));
        }
    }
}
