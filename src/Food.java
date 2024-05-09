public class Food {
    private String name;
    private double price;
    private boolean isVegetarian;
    private int quantity; // New property for quantity

    public Food(String name, double price, boolean isVegetarian) {
        this.name = name;
        this.price = price;
        this.isVegetarian = isVegetarian;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isVegetarian() {
        return isVegetarian;
    }

    public void setVegetarian(boolean vegetarian) {
        isVegetarian = vegetarian;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
