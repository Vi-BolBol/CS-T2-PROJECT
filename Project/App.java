import models.Food;
import models.Table;

/**
 * Application entry point.
 * Seeds the in-memory database, then hands control to the manager login flow.
 */
public class App {

    public static void main(String[] args) {

        Controller controller = new Controller();

        // ── Seed food ─────────────────────────────────────────────────────────
        controller.addFood(new Food("Sushi",            12.00, "Japanese",       1));
        controller.addFood(new Food("Margherita Pizza", 11.99, "Italian",        2));
        controller.addFood(new Food("Caesar Salad",      8.75, "Salad",          3));
        controller.addFood(new Food("Beef Ramen",        13.50, "Japanese",      4));
        controller.addFood(new Food("Falafel Wrap",       7.80, "Middle Eastern",5));
        controller.addFood(new Food("Butter Chicken",    14.20, "Indian",        6));
        controller.addFood(new Food("Pho Bo",            10.99, "Vietnamese",    2));
        controller.addFood(new Food("Grilled Salmon",    16.50, "Seafood",       1));
        controller.addFood(new Food("Veggie Burger",      9.25, "Fastfood",      2));

        // ── Seed tables ───────────────────────────────────────────────────────
        controller.addTable(new Table(2,  false, "Second floor",     10, "Near the window with beautiful view"));
        controller.addTable(new Table(4,  true,  "Ground floor",      8, "Next to the main entrance, quick meals"));
        controller.addTable(new Table(6,  false, "First floor",      12, "Corner table with extra space and privacy"));
        controller.addTable(new Table(2,  true,  "Outdoor terrace",  15, "Romantic spot under string lights"));
        controller.addTable(new Table(8,  false, "Third floor",      18, "Large family table near the bar"));
        controller.addTable(new Table(4,  true,  "Mezzanine level",   9, "Quiet area with city skyline view"));
        controller.addTable(new Table(10, false, "Private room",     20, "VIP room with projector"));
        controller.addTable(new Table(2,  true,  "Rooftop section",  22, "Best sunset view, popular in evenings"));

        // ── Start ─────────────────────────────────────────────────────────────
        controller.managerLogin();
    }
}
