import java.util.Comparator;

public class FoodComparators {
    public static Comparator<Food> sortByPriceLowToHigh() {
        return Comparator.comparingDouble(Food::getPrice);
    }

    public static Comparator<Food> sortByPriceHighToLow() {
        return Comparator.comparingDouble(Food::getPrice).reversed();
    }

    public static Comparator<Food> sortByVegetarian() {
        return Comparator.comparing(Food::isVegetarian);
    }

    public static Comparator<Food> sortByNonVegetarian() {
        return Comparator.comparing(food -> !food.isVegetarian());
    }
}
