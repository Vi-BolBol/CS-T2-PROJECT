package models.inventory;
import models.Food_quantity;
import java.util.ArrayList;

public class CartInventory {
    private ArrayList<Food_quantity> ordered ;

    public CartInventory(){
        this.ordered = new ArrayList<>();
    }

    //get orders 
    public ArrayList<Food_quantity> getOrder(){
        return ordered;
    }

    public int getCartSize(){
        return ordered.size();
    }

    // add food to order
    public void addFood(Food_quantity food){
        this.ordered.add(food);
    }

    //clear item in cart
    public void clearCartItem(){
        ordered.clear();
    }

    // print order list
    public void printOrderList() {
    if (ordered == null || ordered.isEmpty()) {
        System.out.println("No items in order.");
        return;
    }

    System.out.println();
    System.out.println("══════════════════════ ORDERED ══════════════════════");
    System.out.println("─────────────────────────────────────────────────────");
    System.out.println("Item Name              Price      Qty   Total    ");
    System.out.println("─────────────────────────────────────────────────────");

    double subTotal = 0.0;

    for (Food_quantity item : ordered) {
        double lineTotal = item.getPrice() * item.getQuantity();
        subTotal += lineTotal;

        // %-22s → name up to ~22 chars (most food names fit nicely)
        // %7.2f   → right-aligned price with $ sign
        // %5d     → quantity (up to 999 is fine)
        // %8.2f   → right-aligned line total
        System.out.printf("%-22s $%6.2f  %4d   $%7.2f%n",
                item.getName(),
                item.getPrice(),
                item.getQuantity(),
                lineTotal);
    }

    System.out.println("─────────────────────────────────────────────────────");
    System.out.printf("                            Sub-Total: $%7.2f%n", subTotal);
    System.out.println();
}
}
