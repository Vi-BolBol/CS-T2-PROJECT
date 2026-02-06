package model;

public class Food {
    int FoodId;
    String name;
    double price;
    boolean available;
    int Instocks;
    int sold;

    public Food(int FoodID, String name, double price, boolean available, int Instocks){
        this.FoodId = FoodID;
        this.name = name;
        this.price = price;
        this.available = available;
        this.Instocks = Instocks;
        this.sold = 0;
    }

    @Override
    public String toString(){
        return "Food: name = '" + name + "', ID = " + FoodId + ", price = " + price + ", In stocks = " + Instocks + ", sold = " + sold + ", Avaiable" + (available ? "Available" : "Out of Stock");
    }
}
