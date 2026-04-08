import db.Database;
import models.Food;
import models.Order;
import models.Order.OrderStatus;
import models.Order.OrderType;
import models.Table;
import models.Transaction;
import models.payment.*;
import ui.ConsoleUI;
import ui.InputReader;

import java.util.Comparator;
import java.util.List;

/**
 * Orchestrates user interactions and delegates to the Database and UI layers.
 *
 * ENCAPSULATION:  private helpers; public surface is minimal (seed methods + managerLogin).
 * POLYMORPHISM:   payment is handled through the IPayment interface – the controller
 *                 never checks which concrete type it has.
 */
public class Controller {

    // ── Constants ─────────────────────────────────────────────────────────────

    private static final String MANAGER_USER = "admin";
    private static final String MANAGER_PASS = "manager123";
    private static final double DISCOUNT_PCT = 20.0;

    // ── Dependencies ──────────────────────────────────────────────────────────

    private final Database    db;
    private final InputReader input;

    // ── Constructor ───────────────────────────────────────────────────────────

    public Controller() {
        this.db    = new Database();
        this.input = new InputReader();
    }

    // ── Seed helpers (called from App) ────────────────────────────────────────

    public void addFood(Food food)   { db.create(food); }
    public void addTable(Table table){ db.create(table); }

    // ── Sorted DB reads ───────────────────────────────────────────────────────

    private List<Food> getSortedFoods() {
        return db.filterByType(Food.class).stream()
                .sorted(Comparator.comparingDouble(Food::getPrice))
                .toList();
    }

    private List<Table> getSortedTables() {
        return db.filterByType(Table.class).stream()
                .sorted(Comparator.comparingDouble(Table::getPrice))
                .toList();
    }

    // ── Entry point ───────────────────────────────────────────────────────────

    public void managerLogin() {
        System.out.println(ConsoleUI.CYAN + ConsoleUI.BOLD
                + "\n╔══════════════════════════════════╗"
                + "\n║       MANAGER LOGIN              ║"
                + "\n╚══════════════════════════════════╝" + ConsoleUI.RESET);

        ConsoleUI.prompt("Username");
        String user = input.readLine();
        ConsoleUI.prompt("Password");
        String pass = input.readLine();

        if (MANAGER_USER.equals(user) && MANAGER_PASS.equals(pass)) {
            ConsoleUI.success("Login successful! Welcome, Manager.");
            showManagerMenu();
        } else {
            ConsoleUI.error("Invalid username or password.");
        }
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  MANAGER MENU
    // ═════════════════════════════════════════════════════════════════════════

    private void showManagerMenu() {
        boolean running = true;
        while (running) {
            ConsoleUI.printManagerMenu();
            ConsoleUI.prompt("Choose");
            switch (input.readLine()) {
                case "1" -> showFoodMenu();
                case "2" -> showTableMenu();
                case "3" -> showOrderMenu();
                case "4" -> { running = false; ConsoleUI.warn("Logged out."); }
                default  -> ConsoleUI.error("Invalid option – try again.");
            }
        }
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  FOOD MANAGEMENT
    // ═════════════════════════════════════════════════════════════════════════

    private void showFoodMenu() {
        boolean running = true;
        while (running) {
            ConsoleUI.printFoodMenu();
            ConsoleUI.prompt("Choose");
            switch (input.readLine()) {
                case "1" -> createFood();
                case "2" -> { List<Food> f = getSortedFoods(); ConsoleUI.printFoodTable(f); }
                case "3" -> updateFood();
                case "4" -> deleteFood();
                case "5" -> running = false;
                default  -> ConsoleUI.error("Invalid option.");
            }
        }
    }

    private void createFood() {
        System.out.println(ConsoleUI.CYAN + ConsoleUI.BOLD + "\n [ ADD NEW FOOD ]" + ConsoleUI.RESET);

        ConsoleUI.prompt("Food Name (letters/numbers, min 2 chars)");
        String name = readValidName();

        ConsoleUI.prompt("Price (0 < price ≤ 10000)");
        double price = input.readPositiveDouble(10_000);

        ConsoleUI.prompt("Category");
        String category = input.readNonBlank("Category");

        ConsoleUI.prompt("Quantity (0 or more)");
        int qty = input.readInt(0, Integer.MAX_VALUE);

        try {
            db.create(new Food(name, price, category, qty));
            ConsoleUI.success("Food '" + name + "' added.");
        } catch (IllegalArgumentException e) {
            ConsoleUI.error(e.getMessage());
        }
    }

    private void updateFood() {
        System.out.println(ConsoleUI.CYAN + ConsoleUI.BOLD + "\n [ UPDATE FOOD ]" + ConsoleUI.RESET);
        List<Food> foods = getSortedFoods();
        ConsoleUI.printFoodTable(foods);
        if (foods.isEmpty()) return;

        int idx = input.readListIndex(foods.size());
        Food food = foods.get(idx - 1);
        ConsoleUI.info("Editing: " + food.getName() + " (press Enter to keep current value)");

        // Name
        ConsoleUI.prompt("New Name [" + food.getName() + "]");
        String name = input.readLine();
        if (!name.isEmpty()) {
            if (isValidName(name)) food.setName(name);
            else { ConsoleUI.error("Invalid name – update cancelled."); return; }
        }

        // Price
        ConsoleUI.prompt("New Price [" + food.getPrice() + "]");
        String priceStr = input.readLine();
        if (!priceStr.isEmpty()) {
            try {
                double p = Double.parseDouble(priceStr);
                if (p > 0 && p <= 10_000) food.setPrice(p);
                else { ConsoleUI.error("Price out of range – update cancelled."); return; }
            } catch (NumberFormatException e) {
                ConsoleUI.error("Invalid price – update cancelled."); return;
            }
        }

        // Category
        ConsoleUI.prompt("New Category [" + food.getCategory() + "]");
        String cat = input.readLine();
        if (!cat.isEmpty() && cat.length() >= 2) food.setCategory(cat);

        // Quantity
        ConsoleUI.prompt("New Quantity [" + food.getQuantity() + "]");
        String qtyStr = input.readLine();
        if (!qtyStr.isEmpty()) {
            try {
                int q = Integer.parseInt(qtyStr);
                if (q >= 0) food.setQuantity(q);
                else { ConsoleUI.error("Quantity cannot be negative – update cancelled."); return; }
            } catch (NumberFormatException e) {
                ConsoleUI.error("Invalid quantity – update cancelled."); return;
            }
        }

        db.update(food.getId(), food);
        ConsoleUI.success("Food updated.");
    }

    private void deleteFood() {
        System.out.println(ConsoleUI.CYAN + ConsoleUI.BOLD + "\n [ DELETE FOOD ]" + ConsoleUI.RESET);
        List<Food> foods = getSortedFoods();
        ConsoleUI.printFoodTable(foods);
        if (foods.isEmpty()) return;

        int idx = input.readListIndex(foods.size());
        Food food = foods.get(idx - 1);

        ConsoleUI.warn("About to delete: " + food.getName()
                + " | $" + food.getPrice() + " | Qty: " + food.getQuantity());
        ConsoleUI.prompt("Confirm delete? (yes/no)");

        if (input.readConfirm()) {
            db.delete(food.getId());
            ConsoleUI.success("Food '" + food.getName() + "' deleted.");
        } else {
            ConsoleUI.info("Deletion cancelled.");
        }
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  TABLE MANAGEMENT
    // ═════════════════════════════════════════════════════════════════════════

    private void showTableMenu() {
        boolean running = true;
        while (running) {
            ConsoleUI.printTableMenu();
            ConsoleUI.prompt("Choose");
            switch (input.readLine()) {
                case "1" -> viewAllTables();
                case "2" -> releaseOccupiedTable();
                case "3" -> releaseAllTables();
                case "4" -> running = false;
                default  -> ConsoleUI.error("Invalid option.");
            }
        }
    }

    /** Shows every table with its current occupancy status. */
    private void viewAllTables() {
        System.out.println(ConsoleUI.CYAN + ConsoleUI.BOLD + "\n [ ALL TABLES ]" + ConsoleUI.RESET);
        List<Table> tables = getSortedTables();
        if (tables.isEmpty()) { ConsoleUI.warn("No tables found."); return; }
        ConsoleUI.printTableList(tables);
    }

    /** Lets the manager pick one occupied table and free it manually. */
    private void releaseOccupiedTable() {
        System.out.println(ConsoleUI.CYAN + ConsoleUI.BOLD + "\n [ RELEASE TABLE ]" + ConsoleUI.RESET);

        // Filter to only occupied tables so the manager isn't shown available ones
        List<Table> occupied = getSortedTables().stream()
                .filter(Table::isOccupied)
                .toList();

        if (occupied.isEmpty()) {
            ConsoleUI.info("No tables are currently occupied.");
            return;
        }

        ConsoleUI.printTableList(occupied);
        int idx = input.readListIndex(occupied.size());
        Table table = occupied.get(idx - 1);

        ConsoleUI.warn("About to release: " + table.getCapacity() + " seats | "
                + table.getLocation() + " | " + table.getDescription());
        ConsoleUI.prompt("Confirm release? (yes/no)");

        if (input.readConfirm()) {
            releaseTable(table.getId());
        } else {
            ConsoleUI.info("Release cancelled.");
        }
    }

    /** Releases every occupied table at once — useful at closing time. */
    private void releaseAllTables() {
        System.out.println(ConsoleUI.CYAN + ConsoleUI.BOLD + "\n [ RELEASE ALL TABLES ]" + ConsoleUI.RESET);

        List<Table> occupied = getSortedTables().stream()
                .filter(Table::isOccupied)
                .toList();

        if (occupied.isEmpty()) {
            ConsoleUI.info("No tables are currently occupied.");
            return;
        }

        ConsoleUI.warn(occupied.size() + " occupied table(s) will be released.");
        ConsoleUI.prompt("Confirm release all? (yes/no)");

        if (input.readConfirm()) {
            for (Table t : occupied) {
                releaseTable(t.getId());
            }
            ConsoleUI.success("All tables are now available.");
        } else {
            ConsoleUI.info("Release cancelled.");
        }
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  ORDER MANAGEMENT
    // ═════════════════════════════════════════════════════════════════════════

    private void showOrderMenu() {
        boolean running = true;
        while (running) {
            ConsoleUI.printOrderMenu();
            ConsoleUI.prompt("Choose");
            switch (input.readLine()) {
                case "1" -> createOrder();
                case "2" -> viewAllOrders();
                case "3" -> updateOrderStatus();
                case "4" -> viewSalesReport();
                case "5" -> deleteOrder();
                case "6" -> running = false;
                default  -> ConsoleUI.error("Invalid option.");
            }
        }
    }

    private void createOrder() {
        System.out.println(ConsoleUI.CYAN + ConsoleUI.BOLD + "\n [ CREATE NEW ORDER ]" + ConsoleUI.RESET);

        // Step 1 – order type
        System.out.println("\n  Order Type:\n  [1] Table\n  [2] Online");
        ConsoleUI.prompt("Choose");
        String typeInput = input.readLine();
        OrderType orderType = switch (typeInput) {
            case "1" -> OrderType.TABLE;
            case "2" -> OrderType.ONLINE;
            default  -> null;
        };
        if (orderType == null) { ConsoleUI.error("Invalid order type."); return; }

        Order  order         = new Order(orderType);
        Table  selectedTable = null;

        // Step 2 – pick table (if TABLE order)
        if (orderType == OrderType.TABLE) {
            List<Table> tables = getSortedTables();
            ConsoleUI.printTableList(tables);
            if (tables.isEmpty()) { ConsoleUI.error("No tables available."); return; }

            int tIdx = input.readListIndex(tables.size());
            selectedTable = tables.get(tIdx - 1);

            if (selectedTable.isOccupied()) {
                ConsoleUI.error("That table is already occupied."); return;
            }
            order.setTableId(selectedTable.getId());
        }

        // Step 3 – add food items
        List<Food> foods = getSortedFoods();
        ConsoleUI.printFoodTable(foods);
        if (foods.isEmpty()) { ConsoleUI.error("No food available."); return; }

        boolean addingItems = true;
        while (addingItems) {
            ConsoleUI.prompt("Food number to add (0 to finish)");
            int fInput;
            try { fInput = Integer.parseInt(input.readLine()); }
            catch (NumberFormatException e) { ConsoleUI.error("Invalid input."); continue; }

            if (fInput == 0) {
                if (order.getItemCount() == 0) { ConsoleUI.error("Add at least 1 item."); continue; }
                addingItems = false;
                continue;
            }
            if (fInput < 1 || fInput > foods.size()) { ConsoleUI.error("Invalid food number."); continue; }

            Food f = foods.get(fInput - 1);
            if (!f.isAvailable()) { ConsoleUI.error("'" + f.getName() + "' is out of stock."); continue; }

            ConsoleUI.prompt("Quantity for '" + f.getName() + "'");
            int qty;
            try {
                qty = Integer.parseInt(input.readLine());
                if (qty <= 0) { ConsoleUI.error("Quantity must be > 0."); continue; }
                if (qty > f.getQuantity()) {
                    ConsoleUI.error("Only " + f.getQuantity() + " in stock."); continue;
                }
            } catch (NumberFormatException e) { ConsoleUI.error("Invalid quantity."); continue; }

            order.addItem(f, qty, f.getPrice());
            deductFoodStock(f, qty);
            ConsoleUI.success("Added: " + f.getName() + " x" + qty
                    + " ($" + String.format("%.2f", f.getPrice() * qty) + ")");
        }

        // Step 4 – display summary
        System.out.println(order.buildSummary());
        if (selectedTable != null) selectedTable.printTableInfo();

        // Step 5 – payment method  (POLYMORPHISM: picked at runtime)
        IPayment payment = pickPaymentMethod();
        if (payment == null) {
            ConsoleUI.error("Invalid payment – order cancelled.");
            restoreStock(order); return;
        }

        // Step 6 – show totals & confirm
        double tableCharge = selectedTable != null ? selectedTable.getPrice() : 0;
        double subtotal    = order.getTotalAmount() + tableCharge;
        double discount    = subtotal * DISCOUNT_PCT / 100;
        double finalTotal  = subtotal - discount;

        System.out.printf("%n  " + ConsoleUI.BOLD + "Sub-total:    " + ConsoleUI.GREEN + "$%.2f%n" + ConsoleUI.RESET, subtotal);
        System.out.printf("  " + ConsoleUI.BOLD + "Discount:     " + ConsoleUI.GREEN + "%.0f%%%n"  + ConsoleUI.RESET, DISCOUNT_PCT);
        System.out.printf("  " + ConsoleUI.BOLD + "Total to pay: " + ConsoleUI.GREEN + "$%.2f%n"  + ConsoleUI.RESET, finalTotal);
        ConsoleUI.prompt("Confirm order & payment? (yes/no)");

        if (!input.readConfirm()) {
            ConsoleUI.info("Order cancelled.");
            restoreStock(order); return;
        }

        // Step 7 – charge  (POLYMORPHISM: pay() dispatches to the right subtype)
        if (!payment.pay(finalTotal)) {
            ConsoleUI.error("Payment failed – order cancelled.");
            restoreStock(order); return;
        }

        // Step 8 – persist  (order starts PENDING, staff advance it via Update Status)
        if (selectedTable != null) {
            selectedTable.setOccupied(true);
            db.update(selectedTable.getId(), selectedTable);
        }
        order.setStatus(OrderStatus.PENDING);
        db.create(order);

        Transaction tx = new Transaction(
                order.getId(), order.getTotalAmount(), tableCharge, DISCOUNT_PCT, payment.getPaymentType());
        System.out.println(tx.buildReceipt());
        ConsoleUI.success("Order created successfully!");

        // Step 9 – offer immediate table release after payment
        if (selectedTable != null) {
            ConsoleUI.prompt("Release table now that payment is done? (yes/no)");
            if (input.readConfirm()) {
                releaseTable(selectedTable.getId());
            } else {
                ConsoleUI.info("Table stays occupied. Release it later via Table Management.");
            }
        }
    }

    private void viewAllOrders() {
        System.out.println(ConsoleUI.CYAN + ConsoleUI.BOLD + "\n [ ALL ORDERS ]" + ConsoleUI.RESET);
        List<Order> orders = db.filterByType(Order.class);
        if (orders.isEmpty()) { ConsoleUI.warn("No orders found."); return; }
        ConsoleUI.printOrderList(orders);
    }

    // ── Feature: Update Order Status ──────────────────────────────────────────

    private void updateOrderStatus() {
        System.out.println(ConsoleUI.CYAN + ConsoleUI.BOLD + "\n [ UPDATE ORDER STATUS ]" + ConsoleUI.RESET);
        List<Order> orders = db.filterByType(Order.class);
        if (orders.isEmpty()) { ConsoleUI.warn("No orders found."); return; }

        ConsoleUI.printOrderList(orders);
        int idx   = input.readListIndex(orders.size());
        Order ord = orders.get(idx - 1);

        if (ord.peekNextStatus() == null) {
            ConsoleUI.warn("Order is already " + ord.getStatus() + " — cannot advance further.");
            return;
        }

        ConsoleUI.info("Current status : " + ord.getStatus());
        ConsoleUI.info("Next status    : " + ord.peekNextStatus());
        ConsoleUI.prompt("Advance status? (yes/no)");

        if (!input.readConfirm()) { ConsoleUI.info("Status unchanged."); return; }

        ord.advanceStatus();

        // When order reaches COMPLETED, release its table so it can be reused
        if (ord.getStatus() == OrderStatus.COMPLETED && ord.isTableOrder()) {
            releaseTable(ord.getTableId());
        }

        db.update(ord.getId(), ord);
        ConsoleUI.success("Order status updated to: " + ord.getStatus());
    }

    // ── Feature: Sales Report ─────────────────────────────────────────────────

    private void viewSalesReport() {
        System.out.println(ConsoleUI.CYAN + ConsoleUI.BOLD + "\n [ SALES REPORT ]" + ConsoleUI.RESET);
        List<Order> orders = db.filterByType(Order.class);
        if (orders.isEmpty()) { ConsoleUI.warn("No orders recorded yet."); return; }
        ConsoleUI.printSalesReport(orders);
    }

    // ── Feature: Delete Order (with table release + stock restore) ────────────

    private void deleteOrder() {
        System.out.println(ConsoleUI.CYAN + ConsoleUI.BOLD + "\n [ DELETE ORDER ]" + ConsoleUI.RESET);
        List<Order> orders = db.filterByType(Order.class);
        if (orders.isEmpty()) { ConsoleUI.warn("No orders found."); return; }

        ConsoleUI.printOrderList(orders);
        int idx   = input.readListIndex(orders.size());
        Order ord = orders.get(idx - 1);

        ConsoleUI.warn("About to delete Order " + ord.getId()
                + " | " + ord.getOrderType() + " | $" + String.format("%.2f", ord.getTotalAmount()));
        ConsoleUI.prompt("Confirm? (yes/no)");
        if (input.readConfirm()) {
            // Release the table so it becomes available again
            if (ord.isTableOrder() && ord.getTableId() != null) {
                releaseTable(ord.getTableId());
            }
            // Restore food stock only for orders that were not yet completed
            if (ord.getStatus() != OrderStatus.COMPLETED) {
                restoreStock(ord);
            }
            db.delete(ord.getId());
            ConsoleUI.success("Order deleted.");
        } else {
            ConsoleUI.info("Deletion cancelled.");
        }
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  PRIVATE HELPERS
    // ═════════════════════════════════════════════════════════════════════════

    /** POLYMORPHISM: returns an IPayment whose runtime type is decided by user input. */
    private IPayment pickPaymentMethod() {
        System.out.println("\n  Payment Method:\n  [1] Cash\n  [2] Card\n  [3] QR\n  [4] Online");
        ConsoleUI.prompt("Choose");
        return switch (input.readLine()) {
            case "1" -> new CashPayment();
            case "2" -> {
                ConsoleUI.prompt("Card Number");
                String num = input.readLine();
                yield num.isEmpty() ? null : new CardPayment(num);
            }
            case "3" -> new QRPayment();
            case "4" -> new OnlinePayment();
            default  -> null;
        };
    }

    private void deductFoodStock(Food food, int qty) {
        food.setQuantity(food.getQuantity() - qty);
        db.update(food.getId(), food);
    }

    /** Marks the table with the given ID as available again and persists the change. */
    private void releaseTable(String tableId) {
        Table table = db.readById(tableId, Table.class);
        if (table != null) {
            table.setOccupied(false);
            db.update(tableId, table);
            ConsoleUI.info("Table released and now available.");
        }
    }

    /** Restores stock for all items in a cancelled order. */
    private void restoreStock(Order order) {
        List<Food>    items = order.getFoodItems();
        List<Integer> qtys  = order.getQuantities();
        for (int i = 0; i < items.size(); i++) {
            Food f = items.get(i);
            f.setQuantity(f.getQuantity() + qtys.get(i));
            db.update(f.getId(), f);
        }
    }

    /** Letters, numbers and spaces; minimum 2 characters. */
    private boolean isValidName(String name) {
        return name != null && name.trim().length() >= 2 && name.matches("[a-zA-Z0-9 ]+");
    }

    private String readValidName() {
        while (true) {
            String val = input.readLine();
            if (isValidName(val)) return val;
            ConsoleUI.error("Letters and numbers only, min 2 chars.");
            ConsoleUI.prompt("Food Name");
        }
    }
}
