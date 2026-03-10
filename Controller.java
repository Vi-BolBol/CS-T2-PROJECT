import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import db.Database;
import models.Food;
import models.Order;
import models.Table;
import models.Transaction;
import models.payment.IPayment;
import models.payment.CashPayment;
import models.payment.QRPayment;
import models.payment.CardPayment;
import models.payment.OnlinePayment;

public class Controller {

    
    private static final String RESET  = "\u001B[0m";
    private static final String CYAN   = "\u001B[36m";
    private static final String GREEN  = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED    = "\u001B[31m";
    private static final String BLUE   = "\u001B[34m";
    private static final String GRAY   = "\u001B[90m";
    private static final String BOLD   = "\u001B[1m";

    private Database postgreSQL;
    private Scanner sc;

    private double discount = 20;
    
    private static final String MANAGER_USERNAME = "admin";
    private static final String MANAGER_PASSWORD = "manager123";

    Controller() {
        postgreSQL = new Database();
        sc = new Scanner(System.in);
    }

    
    public void addFood(Food food) {
        postgreSQL.create(food);
    }

    public void addTable(Table table) {
        postgreSQL.create(table);
    }

    private List<Food> getFoodFromDB() {
        List<Food> foods = postgreSQL.filterByType(Food.class);
        return foods.stream()
                .sorted(Comparator.comparingDouble(Food::getPrice))
                .toList();
    }

    private List<Table> getTableFromDB() {
        List<Table> tables = postgreSQL.filterByType(Table.class);
        return tables.stream()
                .sorted(Comparator.comparingDouble(Table::getPrice))
                .toList();
    }

    public List<Table> displayTable() {
        List<Table> tables = this.getTableFromDB();
        if (tables == null || tables.isEmpty()) {
            System.out.println(YELLOW + "No tables available." + RESET);
            return tables;
        }

        System.out.println(CYAN + BOLD + "══════════════════════════════════════════════════════════════" + RESET);
        System.out.println(CYAN + "                   AVAILABLE TABLES" + RESET);
        System.out.println(CYAN + "══════════════════════════════════════════════════════════════" + RESET);

        int i = 1;
        for (Table t : tables) {
            String statusLine = switch (t.getStatus().toLowerCase()) {
                case "available", "free"  -> GREEN + " Available" + RESET;
                case "reserved", "booked" -> YELLOW + " Reserved" + RESET;
                case "occupied", "in-use" -> RED + " Occupied" + RESET;
                default                   -> GRAY + t.getStatus() + RESET;
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
            System.out.println(YELLOW + "No foods found." + RESET);
            return null;
        }

        System.out.println(CYAN + "╔═════╦═════════════════════════════╦════════╦══════════════════════╦══════════╦═══════════╗" + RESET);
        System.out.printf(BLUE + "║ %-3s ║ %-27s ║ %6s ║ %-20s ║ %8s ║ %-9s ║%n" + RESET,
                "#", "Food Name", "Price", "Category", "Quantity", "Available");
        System.out.println(CYAN + "╠═════╬═════════════════════════════╬════════╬══════════════════════╬══════════╬═══════════╣" + RESET);

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
                    availColor, f.isAvailable() ? "Yes      " : "No       ", RESET);
        }

        System.out.println(CYAN + "╚═════╩═════════════════════════════╩════════╩══════════════════════╩══════════╩═══════════╝" + RESET);
        return sorted;
    }

    private String truncate(String str, int maxLength) {
        if (str == null) return "—";
        return str.length() <= maxLength ? str : str.substring(0, maxLength - 3) + "...";
    }

    private void updateFoodQuantity(Food food, int amount) {
        int newQuantity = food.getQuantity() - amount;
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Not enough food for sell!");
        }
        food.setQuantity(food.getQuantity() - amount);
        food.syncAvilable();
        postgreSQL.update(food.getId(), food);
    }

    public void orderFood() {
        List<Food> sorted = this.displayFood();
        List<Table> sortedTable = this.displayTable();

        Order order = new Order();

        Food order1 = sorted.get(0);
        Food order2 = sorted.get(1);
        Food order3 = sorted.get(2);
        Food order4 = sorted.get(3);

        updateFoodQuantity(order1, 5);
        updateFoodQuantity(order2, 1);
        updateFoodQuantity(order3, 1);
        updateFoodQuantity(order4, 1);

        order.addItem(order1, 5, order1.getPrice());
        order.addItem(order2, 1, order1.getPrice());
        order.addItem(order3, 1, order1.getPrice());
        order.addItem(order4, 1, order1.getPrice());

        order.setOrderType("Table");

        
        order.setOrderType("Table");  // Table order
        // for table order we need to connect that table to order, so that we know which table has occuper 
        
        Table tablePickByUser = sortedTable.get(2);
        tablePickByUser.setOccupied(true);
        postgreSQL.update(tablePickByUser.getId(), tablePickByUser);
        order.setTableId(tablePickByUser.getId());


        // when user Check out
        // display order info
        // display table info if user chose table service
        // process payment and print Transaction Info 

        System.out.println(order.getOrderSummary());
        tablePickByUser.TableInfo();

        // Transaction transaction = new Transaction(order.getId(), order.getTotalAmount(), tablePickByUser.getPrice(), 20, "QR");

        IPayment payment = new QRPayment();
        payment.pay(order.getTotalAmount() + tablePickByUser.getPrice());

        Transaction transaction = new Transaction(
                order.getId(), order.getTotalAmount(),
                tablePickByUser.getPrice(), 20, payment.getPaymentType());
        transaction.transactionInfo();
    }

    
    public void managerLogin() {
        System.out.println(CYAN + BOLD + "\n╔══════════════════════════════════╗");
        System.out.println(             "║       MANAGER LOGIN              ║");
        System.out.println(             "╚══════════════════════════════════╝" + RESET);
        System.out.print("  Username: ");
        String username = sc.nextLine().trim();
        System.out.print("  Password: ");
        String password = sc.nextLine().trim();

        if (username.equals(MANAGER_USERNAME) && password.equals(MANAGER_PASSWORD)) {
            System.out.println(GREEN + BOLD + "\n   Login successful! Welcome, Manager.\n" + RESET);
            showManagerMenu();
        } else {
            System.out.println(RED + "\n   Invalid username or password.\n" + RESET);
        }
    }

   

    private void showManagerMenu() {
        boolean running = true;
        while (running) {
            System.out.println(CYAN + BOLD + "\n╔══════════════════════════════════╗");
            System.out.println(             "║         MANAGER MENU             ║");
            System.out.println(             "╠══════════════════════════════════╣");
            System.out.println(             "║  [1] Food Management             ║");
            System.out.println(             "║  [2] Order Management            ║");
            System.out.println(             "║  [3] Logout                      ║");
            System.out.println(             "╚══════════════════════════════════╝" + RESET);
            System.out.print("  Choose: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1" -> showFoodMenu();
                case "2" -> showOrderMenu();
                case "3" -> { running = false; System.out.println(YELLOW + "\n   Logged out.\n" + RESET); }
                default  -> System.out.println(RED + "   Invalid option. Try again." + RESET);
            }
        }
    }

    

    private void showFoodMenu() {
        boolean running = true;
        while (running) {
            System.out.println(BLUE + BOLD + "\n┌──────────────────────────────────┐");
            System.out.println(            "│        FOOD MANAGEMENT           │");
            System.out.println(            "├──────────────────────────────────┤");
            System.out.println(            "│  [1] Add New Food                │");
            System.out.println(            "│  [2] View All Food               │");
            System.out.println(            "│  [3] Update Food                 │");
            System.out.println(            "│  [4] Delete Food                 │");
            System.out.println(            "│  [5] Back                        │");
            System.out.println(            "└──────────────────────────────────┘" + RESET);
            System.out.print("  Choose: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1" -> createFood();
                case "2" -> displayFood();
                case "3" -> updateFood();
                case "4" -> deleteFood();
                case "5" -> running = false;
                default  -> System.out.println(RED + "   Invalid option." + RESET);
            }
        }
    }

    
    private void createFood() {
        System.out.println(CYAN + BOLD + "\n [ ADD NEW FOOD ]" + RESET);

        String name     = promptFoodName();
        double price    = promptPrice();
        String category = promptCategory();
        int quantity    = promptQuantity();

        try {
            Food food = new Food(name, price, category, quantity > 0, quantity);
            postgreSQL.create(food);
            System.out.println(GREEN + "\n   Food '" + name + "' added successfully!" + RESET);
        } catch (IllegalArgumentException e) {
            System.out.println(RED + "   Error: " + e.getMessage() + RESET);
        }
    }

    
    private void updateFood() {
        System.out.println(CYAN + BOLD + "\n [ UPDATE FOOD ]" + RESET);
        List<Food> foods = displayFood();
        if (foods == null || foods.isEmpty()) return;

        System.out.print("\n  Enter food number from list (1 to " + foods.size() + "): ");
        int index = promptListIndex(foods.size());
        Food food = foods.get(index - 1);

        System.out.println(GRAY + "\n  Editing: " + BOLD + food.getName() + RESET + GRAY + " (press Enter to keep current value)" + RESET);

        System.out.print("  New Name [" + food.getName() + "]: ");
        String name = sc.nextLine().trim();
        if (!name.isEmpty()) {
            if (isValidName(name)) food.setName(name);
            else { System.out.println(RED + "   Invalid name. Update cancelled." + RESET); return; }
        }

        
        System.out.print("  New Price [" + food.getPrice() + "]: ");
        String priceStr = sc.nextLine().trim();
        if (!priceStr.isEmpty()) {
            try {
                double price = Double.parseDouble(priceStr);
                if (price > 0 && price <= 10000) food.setPrice(price);
                else { System.out.println(RED + "   Price must be between 0 and 10000. Update cancelled." + RESET); return; }
            } catch (NumberFormatException e) {
                System.out.println(RED + "   Invalid price format. Update cancelled." + RESET); return;
            }
        }

        
        System.out.print("  New Category [" + food.getCategory() + "]: ");
        String category = sc.nextLine().trim();
        if (!category.isEmpty()) {
            if (isValidName(category)) food.setCategory(category);
            else { System.out.println(RED + "   Invalid category. Update cancelled." + RESET); return; }
        }

        
        System.out.print("  New Quantity [" + food.getQuantity() + "]: ");
        String qtyStr = sc.nextLine().trim();
        if (!qtyStr.isEmpty()) {
            try {
                int qty = Integer.parseInt(qtyStr);
                if (qty >= 0) {
                    food.setQuantity(qty);
                    food.syncAvilable();
                } else { System.out.println(RED + "   Quantity cannot be negative. Update cancelled." + RESET); return; }
            } catch (NumberFormatException e) {
                System.out.println(RED + "   Invalid quantity. Update cancelled." + RESET); return;
            }
        }

        postgreSQL.update(food.getId(), food);
        System.out.println(GREEN + "\n   Food updated successfully!" + RESET);
    }

    
    private void deleteFood() {
        System.out.println(CYAN + BOLD + "\n [ DELETE FOOD ]" + RESET);
        List<Food> foods = displayFood();
        if (foods == null || foods.isEmpty()) return;

        System.out.print("\n  Enter food number to delete (1 to " + foods.size() + "): ");
        int index = promptListIndex(foods.size());
        Food food = foods.get(index - 1);

        System.out.println(YELLOW + "\n  Food to delete: " + BOLD + food.getName()
                + RESET + YELLOW + " | $" + food.getPrice() + " | Qty: " + food.getQuantity() + RESET);
        System.out.print("    Confirm delete? (yes/no): ");
        String confirm = sc.nextLine().trim().toLowerCase();

        if (confirm.equals("yes")) {
            postgreSQL.Delete(food.getId());
            System.out.println(GREEN + "   Food '" + food.getName() + "' deleted." + RESET);
        } else {
            System.out.println(GRAY + "   Deletion cancelled." + RESET);
        }
    }

    

    private void showOrderMenu() {
        boolean running = true;
        while (running) {
            System.out.println(BLUE + BOLD + "\n┌──────────────────────────────────┐");
            System.out.println(            "│        ORDER MANAGEMENT          │");
            System.out.println(            "├──────────────────────────────────┤");
            System.out.println(            "│  [1] Create Order                │");
            System.out.println(            "│  [2] View All Orders             │");
            System.out.println(            "│  [3] Delete Order                │");
            System.out.println(            "│  [4] Back                        │");
            System.out.println(            "└──────────────────────────────────┘" + RESET);
            System.out.print("  Choose: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1" -> createOrder();
                case "2" -> viewAllOrders();
                case "3" -> deleteOrder();
                case "4" -> running = false;
                default  -> System.out.println(RED + "   Invalid option." + RESET);
            }
        }
    }

    
    private void createOrder() {
        System.out.println(CYAN + BOLD + "\n [ CREATE NEW ORDER ]" + RESET);

        // Step 1: Order type
        System.out.println("\n  Order Type:");
        System.out.println("  [1] Table");
        System.out.println("  [2] Online");
        System.out.print("  Choose: ");
        String typeChoice = sc.nextLine().trim();
        String orderType = typeChoice.equals("1") ? "Table" : typeChoice.equals("2") ? "Online" : null;
        if (orderType == null) {
            System.out.println(RED + "   Invalid order type." + RESET); return;
        }

        Order order = new Order(orderType);

        
        Table selectedTable = null;
        if (orderType.equals("Table")) {
            List<Table> tables = displayTable();
            if (tables == null || tables.isEmpty()) {
                System.out.println(RED + "   No tables available." + RESET); return;
            }
            System.out.print("  Enter table number from list (1 to " + tables.size() + "): ");
            int tIndex = promptListIndex(tables.size());
            selectedTable = tables.get(tIndex - 1);

            if (selectedTable.isOccupied()) {
                System.out.println(RED + "   Table is already occupied." + RESET); return;
            }
            order.setTableId(selectedTable.getId());
        }

        
        List<Food> foods = displayFood();
        if (foods == null || foods.isEmpty()) {
            System.out.println(RED + "   No food available." + RESET); return;
        }

        boolean addingItems = true;
        while (addingItems) {
            System.out.print("\n  Enter food number to add (0 to finish): ");

            int fInput;

            try { fInput = Integer.parseInt(sc.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println(RED + "   Invalid input." + RESET); continue; }

            if (fInput == 0) {
                if (order.getItemCount() == 0) {
                    System.out.println(RED + "   Must add at least 1 item." + RESET); continue;
                }
                addingItems = false;
                continue;
            }

            if (fInput < 1 || fInput > foods.size()) {
                System.out.println(RED + "   Invalid food number." + RESET); continue;
            }

            Food selectedFood = foods.get(fInput - 1);
            if (!selectedFood.isAvailable() || selectedFood.getQuantity() <= 0) {
                System.out.println(RED + "   '" + selectedFood.getName() + "' is out of stock." + RESET); continue;
            }

            System.out.print("  Quantity for '" + selectedFood.getName() + "': ");
            int qty;
            try {
                qty = Integer.parseInt(sc.nextLine().trim());
                if (qty <= 0) { System.out.println(RED + "   Quantity must be > 0." + RESET); continue; }
                if (qty > selectedFood.getQuantity()) {
                    System.out.println(RED + "   Only " + selectedFood.getQuantity() + " in stock." + RESET); continue;
                }
            } catch (NumberFormatException e) {
                System.out.println(RED + "   Invalid quantity." + RESET); continue;
            }

            order.addItem(selectedFood, qty, selectedFood.getPrice());
            updateFoodQuantity(selectedFood, qty);
            System.out.println(GREEN + "   Added: " + selectedFood.getName() + " x" + qty
                    + " ($" + String.format("%.2f", selectedFood.getPrice() * qty) + ")" + RESET);
        }

        
        System.out.println(order.getOrderSummary());
        if(selectedTable != null){
            selectedTable.TableInfo();
        }
        
        
        System.out.println("  Payment Method:");
        System.out.println("  [1] Cash");
        System.out.println("  [2] Card");
        System.out.println("  [3] QR");
        System.out.println("  [4] Online");
        System.out.print("  Choose: ");
        String payChoice = sc.nextLine().trim();

        IPayment payment = switch (payChoice) {
            case "1" -> new CashPayment();
            case "2" -> {
                System.out.print("  Enter Card Number: ");
                String cardNum = sc.nextLine().trim();
                if (cardNum.isEmpty()) {
                    System.out.println(RED + "   Card number cannot be empty." + RESET);
                    yield null;
                }
                yield new CardPayment(cardNum);
            }
            case "3" -> new QRPayment();
            case "4" -> new OnlinePayment();
            default  -> null;
        };

        if (payment == null) {
            System.out.println(RED + "   Invalid payment. Order cancelled." + RESET);
            
            for (int i = 0; i < order.getItemCount(); i++) {
                Food f = order.getFoodItems().get(i);
                int q = order.getQuantities().get(i);
                f.setQuantity(f.getQuantity() + q);
                f.syncAvilable();
                postgreSQL.update(f.getId(), f);
            }
            return;
        }

        
        double tableCharge = selectedTable != null ? selectedTable.getPrice() : 0;
        double totalToPay  = order.getTotalAmount() + tableCharge;

        System.out.printf("\n  " + BOLD + "Sub-total: " + GREEN + "$%.2f" + RESET + "", totalToPay);
        System.out.printf("\n  " + BOLD + "Discount: " + GREEN + "%.2f" + RESET + " percent", discount);
        System.out.printf("\n  " + BOLD + "Total to pay: " + GREEN + "$%.2f" + RESET + "\n", totalToPay - (discount /100 * totalToPay));
        
        System.out.print("   Confirm order & payment? (yes/no): ");
        String confirm = sc.nextLine().trim().toLowerCase();

        if (!confirm.equals("yes")) {
            System.out.println(GRAY + "   Order cancelled." + RESET);
            
            for (int i = 0; i < order.getItemCount(); i++) {
                Food f = order.getFoodItems().get(i);
                int q = order.getQuantities().get(i);
                f.setQuantity(f.getQuantity() + q);
                f.syncAvilable();
                postgreSQL.update(f.getId(), f);
            }
            return;
        }

        boolean paid = payment.pay(totalToPay);
        if (!paid) {
            System.out.println(RED + "   Payment failed. Order cancelled." + RESET);
            return;
        }

        
        if (selectedTable != null) {
            selectedTable.setOccupied(true);
            postgreSQL.update(selectedTable.getId(), selectedTable);
            selectedTable.TableInfo();
        }

        
        order.setStatus("Completed");
        postgreSQL.create(order);

        Transaction transaction = new Transaction(order.getId(), order.getTotalAmount(), tableCharge, 20, payment.getPaymentType());
        transaction.transactionInfo();

        System.out.println(GREEN + BOLD + "\n   Order created successfully!" + RESET);
    }

    private void viewAllOrders() {
        System.out.println(CYAN + BOLD + "\n [ ALL ORDERS ]" + RESET);
        List<Order> orders = postgreSQL.filterByType(Order.class);

        if (orders == null || orders.isEmpty()) {
            System.out.println(YELLOW + "  No orders found." + RESET);
            return;
        }

        System.out.println(CYAN + "══════════════════════════════════════════════════════════" + RESET);
        System.out.printf(BLUE + "  %-5s %-12s %-10s %-10s %-12s%n" + RESET,
                "#", "Order ID", "Type", "Total($)", "Status");
        System.out.println(GRAY + "──────────────────────────────────────────────────────────" + RESET);

        int i = 1;
        for (Order o : orders) {
            String statusColor = switch (o.getStatus().toLowerCase()) {
                case "completed" -> GREEN;
                case "pending"   -> YELLOW;
                case "cancelled" -> RED;
                default          -> GRAY;
            };
            System.out.printf("  %-5d %-12s %-10s " + YELLOW + "%-10.2f" + RESET + " %s%-12s%s%n",
                    i++,
                    truncate(o.getId(), 12),
                    o.getOrderType() != null ? o.getOrderType() : "—",
                    o.getTotalAmount(),
                    statusColor, o.getStatus(), RESET);
        }
        System.out.println(CYAN + "══════════════════════════════════════════════════════════" + RESET);
        System.out.printf(GRAY + "  Total: %d orders" + RESET + "\n", orders.size());
    }

    
    private void deleteOrder() {
        System.out.println(CYAN + BOLD + "\n [ DELETE ORDER ]" + RESET);
        List<Order> orders = postgreSQL.filterByType(Order.class);

        if (orders == null || orders.isEmpty()) {
            System.out.println(YELLOW + "  No orders found." + RESET);
            return;
        }

        viewAllOrders();
        System.out.print("\n  Enter order number to delete (1 to " + orders.size() + "): ");
        int index = promptListIndex(orders.size());
        Order order = orders.get(index - 1);

        System.out.println(YELLOW + "\n  Order to delete: " + BOLD + order.getId()
                + RESET + YELLOW + " | Type: " + order.getOrderType()
                + " | Total: $" + String.format("%.2f", order.getTotalAmount()) + RESET);
        System.out.print("    Confirm delete? (yes/no): ");
        String confirm = sc.nextLine().trim().toLowerCase();

        if (confirm.equals("yes")) {
            postgreSQL.Delete(order.getId());
            System.out.println(GREEN + "   Order deleted." + RESET);
        } else {
            System.out.println(GRAY + "   Deletion cancelled." + RESET);
        }
    }

    

    private boolean isValidName(String name) {
        // Letters, numbers, spaces. Min 2 chars.
        return name != null && name.trim().length() >= 2 && name.matches("[a-zA-Z0-9 ]+");
    }

   

    private String promptFoodName() {
        while (true) {
            System.out.print("  Food Name (letters/numbers, min 2 chars): ");
            String val = sc.nextLine().trim();
            if (isValidName(val)) return val;
            System.out.println(RED + "   Invalid name. Letters and numbers only, min 2 chars." + RESET);
        }
    }

    private double promptPrice() {
        while (true) {
            System.out.print("  Price (0 < price ≤ 10000): ");
            try {
                double val = Double.parseDouble(sc.nextLine().trim());
                if (val > 0 && val <= 10000) return val;
                System.out.println(RED + "   Price must be between 0 and 10000." + RESET);
            } catch (NumberFormatException e) {
                System.out.println(RED + "   Enter a valid number (e.g. 9.99)." + RESET);
            }
        }
    }

    private String promptCategory() {
        while (true) {
            System.out.print("  Category (e.g. Fastfood, Italian, Japanese): ");
            String val = sc.nextLine().trim();
            if (val.length() >= 2) return val;
            System.out.println(RED + "   Category must be at least 2 characters." + RESET);
        }
    }

    private int promptQuantity() {
        while (true) {
            System.out.print("  Quantity (0 or more): ");
            try {
                int val = Integer.parseInt(sc.nextLine().trim());
                if (val >= 0) return val;
                System.out.println(RED + "   Quantity cannot be negative." + RESET);
            } catch (NumberFormatException e) {
                System.out.println(RED + "   Enter a whole number (e.g. 10)." + RESET);
            }
        }
    }

    private int promptListIndex(int max) {
        while (true) {
            try {
                int val = Integer.parseInt(sc.nextLine().trim());
                if (val >= 1 && val <= max) return val;
                System.out.print(RED + "   Enter a number between 1 and " + max + ": " + RESET);
            } catch (NumberFormatException e) {
                System.out.print(RED + "   Invalid input. Enter a number: " + RESET);
            }
        }
    }
}
