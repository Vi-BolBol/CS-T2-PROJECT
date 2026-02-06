public class Food {
    String foodId;
    String name;
    double price;
    String category;
    boolean available;

    public Food(String foodId, String name, double price, String category, boolean available) {
        this.foodId = foodId;
        this.name = name;
        this.price = price;
        this.category = category;
        this.available = available;
    }

    public String toString(){
        String availability = (available) ? "Available":"Sold Out";
        return  foodId +" "+ name +" $"+ price +" "+ category +" "+ availability;
    }
}