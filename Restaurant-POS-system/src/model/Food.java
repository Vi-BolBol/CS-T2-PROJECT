package model;

public class Food {
    String name;
    double price;
    boolean available;
    int Instocks;
    int sold;

    public Food(String name, double price, boolean available, int Instocks){
        this.name = name;
        this.price = price;
        this.available = available;
        this.Instocks = Instocks;
        this.sold = 0;
    }

    @Override
    public String toString(){
        return "Menu: Item {name = '" + name + "', price = " + price + ", available = " + available + ", In stocks = " + Instocks + ", Sold = " + sold;
    }
}
