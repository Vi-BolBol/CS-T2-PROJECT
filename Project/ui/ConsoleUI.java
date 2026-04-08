package ui;

import models.Food;
import models.Order;
import models.Order.OrderStatus;
import models.Table;

import java.util.List;

/**
 * ABSTRACTION: hides all CLI formatting details behind clean print methods.
 * The Controller talks to ConsoleUI; it never builds ANSI strings itself.
 *
 * ENCAPSULATION: colour constants are private; only the print methods are public.
 */
public final class ConsoleUI {

    // ── ANSI colours ──────────────────────────────────────────────────────────
    public static final String RESET  = "\u001B[0m";
    public static final String CYAN   = "\u001B[36m";
    public static final String GREEN  = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RED    = "\u001B[31m";
    public static final String BLUE   = "\u001B[34m";
    public static final String GRAY   = "\u001B[90m";
    public static final String BOLD   = "\u001B[1m";

    // Utility class – no instances needed
    private ConsoleUI() {}

    // ── Food ──────────────────────────────────────────────────────────────────

    public static void printFoodTable(List<Food> foods) {
        if (foods == null || foods.isEmpty()) {
            System.out.println(YELLOW + "  No foods found." + RESET);
            return;
        }
        System.out.println(CYAN + "╔═════╦═════════════════════════════╦════════╦══════════════════════╦══════════╦═══════════╗" + RESET);
        System.out.printf(BLUE  + "║ %-3s ║ %-27s ║ %6s ║ %-20s ║ %8s ║ %-9s ║%n" + RESET,
                "#", "Food Name", "Price", "Category", "Quantity", "Available");
        System.out.println(CYAN + "╠═════╬═════════════════════════════╬════════╬══════════════════════╬══════════╬═══════════╣" + RESET);

        for (int i = 0; i < foods.size(); i++) {
            Food f = foods.get(i);
            String avail = f.isAvailable() ? GREEN + "Yes      " + RESET : RED + "No       " + RESET;
            String price = f.getPrice() >= 15 ? YELLOW + String.format("$%5.2f", f.getPrice()) + RESET
                                              : String.format("$%5.2f", f.getPrice());
            System.out.printf("║ %3d ║ %-27s ║ %s ║ %-20s ║ %8d ║ %s ║%n",
                    i + 1, trunc(f.getName(), 27), price,
                    trunc(f.getCategory(), 20), f.getQuantity(), avail);
        }
        System.out.println(CYAN + "╚═════╩═════════════════════════════╩════════╩══════════════════════╩══════════╩═══════════╝" + RESET);
    }

    // ── Tables ────────────────────────────────────────────────────────────────

    public static void printTableList(List<Table> tables) {
        if (tables == null || tables.isEmpty()) {
            System.out.println(YELLOW + "  No tables available." + RESET);
            return;
        }
        System.out.println(CYAN + BOLD + "══════════════════════════════════════════════════════════════" + RESET);
        System.out.println(CYAN + "                   AVAILABLE TABLES" + RESET);
        System.out.println(CYAN + "══════════════════════════════════════════════════════════════" + RESET);

        for (int i = 0; i < tables.size(); i++) {
            Table t = tables.get(i);
            String statusDisplay = switch (t.getStatus().toLowerCase()) {
                case "available" -> GREEN  + " Available" + RESET;
                case "occupied"  -> RED    + " Occupied"  + RESET;
                default          -> YELLOW + " Reserved"  + RESET;
            };
            System.out.printf(BOLD + "%2d." + RESET + "  %d seats   " + YELLOW + "$%.2f" + RESET + "   %s%n",
                    i + 1, t.getCapacity(), t.getPrice(), statusDisplay);
            System.out.println("   " + GRAY + t.getLocation() + RESET + " > " + t.getDescription());
            System.out.println(GRAY + "───────────────────────────────────────────────────────────────" + RESET);
        }
        System.out.println(CYAN + "══════════════════════════════════════════════════════════════" + RESET);
        System.out.printf(GRAY  + "  Total: %d tables%n%n" + RESET, tables.size());
    }

    // ── Orders ────────────────────────────────────────────────────────────────

    public static void printOrderList(List<Order> orders) {
        System.out.println(CYAN + "══════════════════════════════════════════════════════════" + RESET);
        System.out.printf(BLUE  + "  %-5s %-12s %-10s %-10s %-12s%n" + RESET,
                "#", "Order ID", "Type", "Total($)", "Status");
        System.out.println(GRAY + "──────────────────────────────────────────────────────────" + RESET);

        for (int i = 0; i < orders.size(); i++) {
            Order o = orders.get(i);
            String sc = switch (o.getStatus()) {
                case COMPLETED -> GREEN;
                case CANCELLED -> RED;
                default        -> YELLOW;
            };
            System.out.printf("  %-5d %-12s %-10s " + YELLOW + "%-10.2f" + RESET + " %s%-12s%s%n",
                    i + 1, trunc(o.getId(), 12), o.getOrderType(),
                    o.getTotalAmount(), sc, o.getStatus(), RESET);
        }
        System.out.println(CYAN + "══════════════════════════════════════════════════════════" + RESET);
        System.out.printf(GRAY  + "  Total: %d orders%n" + RESET, orders.size());
    }

    // ── Sales report ──────────────────────────────────────────────────────────

    public static void printSalesReport(List<Order> orders) {
        long   total      = orders.size();
        long   completed  = orders.stream().filter(o -> o.getStatus() == OrderStatus.COMPLETED).count();
        long   pending    = orders.stream().filter(o -> o.getStatus() == OrderStatus.PENDING).count();
        long   preparing  = orders.stream().filter(o -> o.getStatus() == OrderStatus.PREPARING).count();
        long   ready      = orders.stream().filter(o -> o.getStatus() == OrderStatus.READY).count();
        long   cancelled  = orders.stream().filter(o -> o.getStatus() == OrderStatus.CANCELLED).count();
        double revenue    = orders.stream()
                .filter(o -> o.getStatus() == OrderStatus.COMPLETED)
                .mapToDouble(Order::getTotalAmount).sum();
        long   tableOrders  = orders.stream().filter(Order::isTableOrder).count();
        long   onlineOrders = orders.stream().filter(Order::isOnlineOrder).count();

        System.out.println(CYAN + BOLD
                + "\n╔══════════════════════════════════════════════════════════════╗"
                + "\n║                     SALES REPORT                            ║"
                + "\n╠══════════════════════════════════════════════════════════════╣" + RESET);
        System.out.printf(BLUE + "║  %-30s" + RESET + BOLD + " %28d " + RESET + CYAN + "║%n" + RESET,
                "Total Orders",  total);
        System.out.printf(BLUE + "║  %-30s" + RESET + BOLD + " %28d " + RESET + CYAN + "║%n" + RESET,
                "Table Orders",  tableOrders);
        System.out.printf(BLUE + "║  %-30s" + RESET + BOLD + " %28d " + RESET + CYAN + "║%n" + RESET,
                "Online Orders", onlineOrders);
        System.out.println(CYAN + "╠══════════════════════════════════════════════════════════════╣" + RESET);
        System.out.printf(YELLOW + "║  %-30s" + RESET + " %28d " + CYAN + "║%n" + RESET, "Pending",   pending);
        System.out.printf(YELLOW + "║  %-30s" + RESET + " %28d " + CYAN + "║%n" + RESET, "Preparing", preparing);
        System.out.printf(YELLOW + "║  %-30s" + RESET + " %28d " + CYAN + "║%n" + RESET, "Ready",     ready);
        System.out.printf(GREEN  + "║  %-30s" + RESET + " %28d " + CYAN + "║%n" + RESET, "Completed", completed);
        System.out.printf(RED    + "║  %-30s" + RESET + " %28d " + CYAN + "║%n" + RESET, "Cancelled", cancelled);
        System.out.println(CYAN + "╠══════════════════════════════════════════════════════════════╣" + RESET);
        System.out.printf(BOLD + GREEN
                + "║  %-30s" + RESET + BOLD + GREEN + " %27s  " + RESET + CYAN + "║%n" + RESET,
                "Total Revenue (completed)", String.format("$%.2f", revenue));
        System.out.println(CYAN + "╚══════════════════════════════════════════════════════════════╝" + RESET);
    }

    // ── Menus ─────────────────────────────────────────────────────────────────

    public static void printManagerMenu() {
        System.out.println(CYAN + BOLD + "\n╔══════════════════════════════════╗");
        System.out.println(             "║         MANAGER MENU             ║");
        System.out.println(             "╠══════════════════════════════════╣");
        System.out.println(             "║  [1] Food Management             ║");
        System.out.println(             "║  [2] Table Management            ║");
        System.out.println(             "║  [3] Order Management            ║");
        System.out.println(             "║  [4] Logout                      ║");
        System.out.println(             "╚══════════════════════════════════╝" + RESET);
    }

    public static void printTableMenu() {
        System.out.println(CYAN + BOLD + "\n┌──────────────────────────────────┐");
        System.out.println(            "│        TABLE MANAGEMENT          │");
        System.out.println(            "├──────────────────────────────────┤");
        System.out.println(            "│  [1] View All Tables             │");
        System.out.println(            "│  [2] Release Occupied Table      │");
        System.out.println(            "│  [3] Release All Tables          │");
        System.out.println(            "│  [4] Back                        │");
        System.out.println(            "└──────────────────────────────────┘" + RESET);
    }

    public static void printFoodMenu() {
        System.out.println(BLUE + BOLD + "\n┌──────────────────────────────────┐");
        System.out.println(            "│        FOOD MANAGEMENT           │");
        System.out.println(            "├──────────────────────────────────┤");
        System.out.println(            "│  [1] Add New Food                │");
        System.out.println(            "│  [2] View All Food               │");
        System.out.println(            "│  [3] Update Food                 │");
        System.out.println(            "│  [4] Delete Food                 │");
        System.out.println(            "│  [5] Back                        │");
        System.out.println(            "└──────────────────────────────────┘" + RESET);
    }

    public static void printOrderMenu() {
        System.out.println(BLUE + BOLD + "\n┌──────────────────────────────────┐");
        System.out.println(            "│        ORDER MANAGEMENT          │");
        System.out.println(            "├──────────────────────────────────┤");
        System.out.println(            "│  [1] Create Order                │");
        System.out.println(            "│  [2] View All Orders             │");
        System.out.println(            "│  [3] Update Order Status         │");
        System.out.println(            "│  [4] Sales Report                │");
        System.out.println(            "│  [5] Delete Order                │");
        System.out.println(            "│  [6] Back                        │");
        System.out.println(            "└──────────────────────────────────┘" + RESET);
    }

    // ── Generic helpers ───────────────────────────────────────────────────────

    public static void success(String msg) { System.out.println(GREEN + "   ✓ " + msg + RESET); }
    public static void error(String msg)   { System.out.println(RED   + "   ✗ " + msg + RESET); }
    public static void warn(String msg)    { System.out.println(YELLOW + "   ! " + msg + RESET); }
    public static void info(String msg)    { System.out.println(GRAY   + "   " + msg + RESET); }

    public static void prompt(String text) { System.out.print("  " + text + ": "); }

    /** Truncates a string to maxLen, appending "…" if needed. */
    public static String trunc(String s, int maxLen) {
        if (s == null) return "—";
        return s.length() <= maxLen ? s : s.substring(0, maxLen - 3) + "...";
    }
}
