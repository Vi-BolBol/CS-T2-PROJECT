import java.util.Comparator;
import java.util.List;

import db.Database;
import models.Food;
import models.Order;
import models.Table;
import models.Transaction;

public class Controller{
    private Database postgreSQL;

    Controller(){
        postgreSQL = new Database();
    }

    public void addFood(Food food){
        postgreSQL.create(food);
    }

    public void addTable(Table table){
        postgreSQL.create(table);
    }

    private List<Food> getFoodFromDB(){
        List<Food> foods = postgreSQL.filterByType(Food.class);

        List<Food> sorted = foods.stream()
                .sorted(Comparator.comparingDouble(Food::getPrice))
                .toList();

        return sorted;
    }

    private List<Table> getTableFromDB(){
        List<Table> foods = postgreSQL.filterByType(Table.class);

        List<Table> sorted = foods.stream()
                .sorted(Comparator.comparingDouble(Table::getPrice))
                .toList();

        return sorted;
    }

    public List<Table> displayTable() {
        List<Table> tables = this.getTableFromDB();
        if (tables == null || tables.isEmpty()) {
            System.out.println("\u001B[33mNo tables available.\u001B[0m");
            return tables;
        }

        String RESET  = "\u001B[0m";
        String CYAN   = "\u001B[36m";
        String GREEN  = "\u001B[32m";
        String YELLOW = "\u001B[33m";
        String RED    = "\u001B[31m";
        String GRAY   = "\u001B[90m";
        String BOLD   = "\u001B[1m";

        System.out.println(CYAN + BOLD + "══════════════════════════════════════════════════════════════" + RESET);
        System.out.println(CYAN + "                   AVAILABLE TABLES" + RESET);
        System.out.println(CYAN + "══════════════════════════════════════════════════════════════" + RESET);

        int i = 1;
        for (Table t : tables) {
            String statusLine = switch (t.getStatus().toLowerCase()) {
                case "available", "free"   -> GREEN + " Available" + RESET;
                case "reserved", "booked"  -> YELLOW + " Reserved" + RESET;
                case "occupied", "in-use"  -> RED + " Occupied" + RESET;
                default                    -> GRAY + t.getStatus() + RESET;
            };

            System.out.printf(BOLD + "%2d." + RESET + "  %d seats   " + YELLOW + "$%.2f" + RESET + "   %s\n",
                    i++, t.getCapacity(), t.getPrice(), statusLine);

            System.out.println("   " + GRAY + t.getLocation() + RESET + " > " + t.getDescription());
            System.out.println();

            System.out.println(GRAY + "───────────────────────────────────────────────────────────────" + RESET);
        }

    System.out.println(CYAN + "══════════════════════════════════════════════════════════════" + RESET);
    System.out.printf(GRAY + "Total: %d tables" + RESET + "\n\n", tables.size());

    return tables;
}
   
    public List<Food> displayFood() {

        List<Food> sorted = this.getFoodFromDB();

        if (sorted.isEmpty()) {
            System.out.println("\u001B[33mNo foods found.\u001B[0m");
            return null;
        }

        String RESET  = "\u001B[0m";
        String CYAN   = "\u001B[36m";
        String GREEN  = "\u001B[32m";
        String YELLOW = "\u001B[33m";
        String BLUE   = "\u001B[34m";
        String RED    = "\u001B[31m";
        String BOLD   = "\u001B[1m";

        // Header
        System.out.println(CYAN + "╔═════╦═════════════════════════════╦════════╦══════════════════════╦══════════╦═══════════╗" + RESET);
        System.out.printf(BLUE + "║ %-3s ║ %-27s ║ %6s ║ %-20s ║ %8s ║ %-9s ║%n" + RESET,
                "#", "Food Name", "Price", "Category", "Quantity", "Available");
        System.out.println(CYAN + "╠═════╬═════════════════════════════╬════════╬══════════════════════╬══════════╬═══════════╣" + RESET);

        // Rows
        for (int i = 0; i < sorted.size(); i++) {
            Food f = sorted.get(i);
            String availColor = f.isAvailable() ? GREEN : RED;
            String priceColor = f.getPrice() >= 15 ? YELLOW : "";

            System.out.printf("║ %3d ║ %-27s ║ %s$%5.2f%s ║ %-20s ║ %8d ║ %s%-9s%s ║%n",
                    i + 1,
                    truncate(f.getName(), 27),
                    priceColor, f.getPrice(), RESET,
                    truncate(f.getCategory(), 20),
                    f.getQuantity(),
                    availColor, f.isAvailable() ? "Yes      " : "No       ", RESET
            );
        }

        System.out.println(CYAN + "╚═════╩═════════════════════════════╩════════╩══════════════════════╩══════════╩═══════════╝" + RESET);
        
        return sorted;
    }

    private String truncate(String str, int maxLength) {
        if (str == null) return "—";
        return str.length() <= maxLength ? str : str.substring(0, maxLength - 3) + "...";
    }
    
    private void updateFoodQuantity(Food food,int amount){
        int newQuantity = food.getQuantity() - amount;
        if(newQuantity < 0){
            throw new IllegalArgumentException("Not enought food for sell!");
        }
        System.out.println(food.getQuantity() + amount + newQuantity + "hahh");
        food.setQuantity(food.getQuantity() - amount); // require to validate 
        food.syncAvilable();

        postgreSQL.update(food.getId(), food);
    }

    public void orderFood(){
        List<Food> sorted = this.displayFood();
        List<Table> sortedTable = this.displayTable();

        Order order = new Order();

        Food order1 = sorted.get(0);
        Food order2 = sorted.get(1);
        Food order3 = sorted.get(2);
        Food order4 = sorted.get(3);

        //could lead to error, Need validation
        updateFoodQuantity(order1, 5);
        updateFoodQuantity(order2, 1);
        updateFoodQuantity(order3, 1);
        updateFoodQuantity(order4, 1);

        order.addItem(order1, 5, order1.getPrice());
        order.addItem(order2, 1, order1.getPrice());
        order.addItem(order3, 1, order1.getPrice());
        order.addItem(order4, 1, order1.getPrice());

        //order.setOrderType("Online"); // online order

        
        order.setOrderType("Table");  // Table order
        // for table order we need to connect that table to order, so that we know which table has occuper 
        
        Table tablePickByUser = sortedTable.get(2);
        tablePickByUser.setOccupied(true);
        postgreSQL.update(tablePickByUser.getId(), tablePickByUser);

        //connect dedicated table to order
        order.setTableId(tablePickByUser.getId());


        // when user Check out
        // display order info
        // display table info if user chose table service
        // process payment and print Transaction Info 

        System.out.println(order.getOrderSummary());

        tablePickByUser.TableInfo();

        Transaction transaction = new Transaction(order.getId(), order.getTotalAmount(), tablePickByUser.getPrice(), 20, "QR");
        transaction.transactionInfo();

    }
    
}

// transaction process 

// total food price
// total table price 
// subtotal 
// discount
// total for pay
