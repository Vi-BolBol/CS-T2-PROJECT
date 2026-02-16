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
        System.out.println();
        System.out.println("═══════════════════════ ORDERED ════════════════════════");
        System.out.println("────────────────────────────────────────────────────────");
        System.out.println("Name                  Price    Qty     Total  ");
        System.out.println("────────────────────────────────────────────────────────");

        double total = 0;

        for (Food_quantity item : ordered) {
            double lineTotal = item.getPrice() * item.getQuantity();
            total += lineTotal;

            System.out.printf("%-20s   $%6.2f   %3d    $%6.2f%n",
                    item.getName(),
                    item.getPrice(),
                    item.getQuantity(),
                    lineTotal);
        }

        System.out.println("────────────────────────────────────────────────────────");
        System.out.printf("                            Sub-Total: $%8.2f%n", total);
        System.out.println();
    }
}
