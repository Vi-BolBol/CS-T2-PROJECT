import java.util.Objects;

public class App {
    public static void main(String[] args) {
        Food sushi = new Food("1", "Sushi", 10, "Sea food", true);
        Food pizza = new Food("2", "Pizza", 12, "Italian", true);
        Food burger = new Food("3", "Cheeseburger", 8, "Fast food", true);
        Food tacos = new Food("5", "Tacos", 7, "Mexican", true);
        Food pasta_carbonara = new Food("7", "Pasta Carbonara", 13, "Italian", true);
        Food falafel = new Food("8", "Falafel", 6, "Middle East", true);
        Food butter_chicken = new Food("9", "Butter Chicken", 12, "Indian", true);
        Food pho = new Food("10", "Pho", 9, "Vietnamese", true);


        Inventory inventory = new Inventory("12345");
        inventory.addFood(sushi, 2);
        inventory.addFood(pizza, 2);
        inventory.addFood(burger, 2);
        inventory.addFood(tacos, 2);
        inventory.addFood(pasta_carbonara, 2);
        inventory.addFood(falafel, 2);
        inventory.addFood(butter_chicken, 2);
        inventory.addFood(pho, 2);

        inventory.displayInventory();

        Order order1 = new Order("123","Online");
        System.out.println("═══════════════════════════════════════════════════════ ORDER ═══════════════════════════════════════════════════════");
        order1.addOrder(sushi.foodId, 1, sushi.price);
        inventory.reduceStock(sushi.foodId, 1);

        
        System.out.println(order1.getOrderSummary());
        
        Transaction transaction2 = new Transaction("234", order1.orderId, order1.totalAmount,"QR");
        System.out.println(transaction2.getTransactionReceipt());


        System.out.println("");
        System.out.println("═════════════════════════════════════════════════ MEETING REQUIREMENT ════════════════════════════════════════════════");
        System.out.println("Primitive vs Reference Behavior Demo \n");

        // F1 — Primitive copy
        // Copying a primitive creates an independent value
        System.out.println("F1 - Primitive copy");
        double foodPrice = 4.0;
        double foodPriceCopy = foodPrice;    // independent copy

        foodPrice = 2.5;                     // modifying original

        System.out.println("  Original price    : " + foodPrice);
        System.out.println("  Copied price      : " + foodPriceCopy);
        System.out.println("  → Original changed, copy remains unchanged\n");

        // F2 — Reference copy
        // Copying a reference points to the same object
        System.out.println("F2 - Reference copy");
        Food ramen = new Food("6", "Ramen", 11, "Japanese", true);
        System.out.println("  Original ramen    : " + ramen);

        Food ramenCopy = ramen;              // same object, not a new one
        ramenCopy.price = 14;                // modifying through copy affects original

        System.out.println("  Modified via copy : " + ramen);
        System.out.println("  → Both variables show the updated price\n");

        // F3 — Array stores references
        // Changes to objects in array are visible everywhere
        System.out.println("F3 - Array stores references");
        Food mySushi = inventory.findFood(sushi.foodId);   

        if (mySushi != null) {
            System.out.println("  Before change     : " + sushi);
            sushi.price = 20;
            System.out.println("  After change      : " + sushi);

            System.out.println("\n  Inventory after change:");
            inventory.displayInventory();
        } 

        System.out.println();

        // F4 — Snapshot behavior (value copied at the time)
        // Once a value is copied, it doesn't follow later changes
        System.out.println("F4 - Snapshot behavior");
        Order order = new Order("23456", "Online");
        order.addOrder(burger.foodId, 1, burger.price);
        inventory.reduceStock(burger.foodId, 1);

        Transaction transaction = new Transaction("2345", order.orderId, order.totalAmount, "Cash");

        double amountBefore = transaction.amount;
        System.out.println("  Order total (snapshot) : $" + amountBefore);

        burger.price = 12;   // changing the food price afterwards
        System.out.println("  Burger price changed to: $" + burger.price);

        double amountAfter = transaction.amount;
        System.out.println("  Order total (still)    : $" + amountAfter);
        System.out.println("  Transaction keeps the original captured amount\n");

    }
}
