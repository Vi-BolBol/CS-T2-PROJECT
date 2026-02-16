package models;

public class Food_quantity extends Food {
    private int quantity;
    public Food_quantity(String name, double price, String category, boolean available,int quantity){
        super(name,price,category,available);
        setQuantity(quantity);
    }

    private void setQuantity(int quantity){
        if(quantity <= 0){
            throw new IllegalArgumentException("No valid quantity!");
        }
        this.quantity = quantity;
    }
    public int getQuantity(){
        return quantity;
    }
}
