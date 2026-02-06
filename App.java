public class App {
    public static void main(String[] args) {
        Food sushi = new Food("1", "Sushi", 10, "Sea food", true);
        Food pizza = new Food("2", "Pizza", 12, "Italian", true);
        Food burger = new Food("3", "Cheeseburger", 8, "Fast food", true);
        Food tacos = new Food("5", "Tacos", 7, "Mexican", true);
        Food ramen = new Food("6", "Ramen", 11, "Japanese", true);
        Food pasta_carbonara = new Food("7", "Pasta Carbonara", 13, "Italian", true);
        Food falafel = new Food("8", "Falafel", 6, "Middle East", true);
        Food butter_chicken = new Food("9", "Butter Chicken", 12, "Indian", true);
        Food pho = new Food("10", "Pho", 9, "Vietnamese", true);


        Inventory inventory = new Inventory("12345");
        inventory.addFood(sushi, 2);
        inventory.addFood(pizza, 2);
        inventory.addFood(burger, 2);
        inventory.addFood(tacos, 2);
        inventory.addFood(ramen, 2);
        inventory.addFood(pasta_carbonara, 2);
        inventory.addFood(falafel, 2);
        inventory.addFood(butter_chicken, 2);
        inventory.addFood(pho, 2);
        
        

        Order order1 = new Order("123","Online");
        order1.addOrder(sushi.foodId, 2, sushi.price);
        inventory.reduceStock(sushi.foodId,2);

        inventory.displayInventory();

        Transaction transaction = new Transaction("123", order1.getOrderId(), order1.getTotalAmount(), "QR");
        System.out.println(transaction.getTransactionReceipt());
    }
}
