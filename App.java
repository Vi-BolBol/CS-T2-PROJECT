
import models.Food;
import models.Table;

public class App {
    public static void main(String[] args) {

        Food food2 = new Food("sushi", 12, "Fastfood", true,1);
        Food food3 = new Food("Margherita Pizza", 11.99, "Italian", true,2);
        Food food4 = new Food("Caesar Salad", 8.75, "Salad", false,3);
        Food food5 = new Food("Beef Ramen", 13.5, "Japanese", true,4);
        Food food6 = new Food("Falafel Wrap", 7.8, "Middle Eastern", true,5);
        Food food7 = new Food("Butter Chicken", 14.2, "Indian", true,6);
        Food food8 = new Food("Pho Bo", 10.99, "Vietnamese", true,2);
        Food food9 = new Food("Grilled Salmon", 16.5, "Seafood", false,1);
        Food food10 = new Food("Veggie Burger", 9.25, "Fastfood", true,2);

        Table table1 = new Table(2, false, "Second floor", 10, "Near the window with beautiful view from high tower");
        Table table2 = new Table(4, true, "Ground floor", 8, "Next to the main entrance, good for quick meals");
        Table table3 = new Table(6, false, "First floor", 12, "Corner table with extra space and privacy");
        Table table4 = new Table(2, true, "Outdoor terrace", 15, "Romantic spot under string lights");
        Table table5 = new Table(8, false, "Third floor", 18, "Large family table near the bar counter");
        Table table6 = new Table(4, true, "Mezzanine level", 9, "Quiet area with city skyline view");
        Table table7 = new Table(10, false, "Private room", 20, "Fully enclosed VIP room with projector");
        Table table8 = new Table(2, true, "Rooftop section", 22, "Best sunset view, very popular in evenings");


        Controller controller = new Controller();
        controller.addFood(food2);
        controller.addFood(food3);
        controller.addFood(food4);
        controller.addFood(food5);
        controller.addFood(food6);
        controller.addFood(food7);
        controller.addFood(food8);
        controller.addFood(food9);
        controller.addFood(food10);

        controller.addTable(table1);
        controller.addTable(table2);
        controller.addTable(table3);
        controller.addTable(table4);
        controller.addTable(table5);
        controller.addTable(table6);
        controller.addTable(table7);
        controller.addTable(table8);

        controller.orderFood();

        controller.displayFood();

    }
}
