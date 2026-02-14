
import java.util.ArrayList;
import java.util.Scanner;
import controllers.Controller;

public class App {

    public void takeOrder( Controller controller,Scanner scanner ){

        ArrayList<String> foodId = new ArrayList<>();
        ArrayList<Integer> quantities = new ArrayList<>();

        boolean isOrdered = false;
        while(true){
            controller.displayInventory();

            System.out.println("═══════════════════════ FOOD ID ═══════════════════════");
            System.out.println("Enter Food id for ordering !:");
            System.out.println("Q/q for stop & cancel order");

            boolean cartHasItem = controller.getCartSize() > 0;

            if(cartHasItem){
                System.out.println("O/o for see Ordere!");
                System.out.println("C/c for Checkout!");
            }

            String id = scanner.nextLine();

            if(cartHasItem && id.equalsIgnoreCase("O")){
                controller.displayOrderedCart();
                continue;
            }

            if(cartHasItem && id.equalsIgnoreCase("c")){
                isOrdered = true;
                break;
            }

            if(id.equalsIgnoreCase("q")){
                break;
            }

            if(id.trim().equals("")){
                System.out.println(id + "Please enter a Valid ID!");
                continue;
            }



             if(!controller.isIDExist(id) ){ // id validation
                 System.out.println(id + " Does not exist!");
                 continue;
            }

            String quantity = null;
            while(true){
                System.out.println("═══════════════════════ Quantity ═══════════════════════");
                System.out.println("Enter Food quantity: ");
                quantity = scanner.nextLine();

                if(!quantity.matches("\\d+")){
                    System.out.println("Please enter a Valid amount!");
                    continue;
                }

                break;
            }

            int Quantity = Integer.parseInt(quantity);

            try{
                //add to cart to update stock level
                controller.addToCart(id,Quantity);

                quantities.add(Quantity);
                foodId.add(id);

            }catch(IllegalArgumentException e){
                System.out.println(e);
            }

        }
        
        String[] foodIdArray = foodId.toArray(new String[0]);
        int[] quantitiesArray = quantities.stream().mapToInt(i -> i).toArray(); // or .mapToInt(Integer::intValue)
        
        //process order
        if(isOrdered){
            controller.displayOrderedCart();
            controller.order("234", "Online", foodIdArray, quantitiesArray);
        }
        //cancell order
        else{
            controller.cancelOrder(foodIdArray, quantitiesArray);
        }

        System.out.println("Tank you! have a good day!!!");
    }

    public static void main(String[] args) {

        Controller controller = new Controller("2343");
        controller.addFood("1", "Sushi", 10, "Sea food", true,2);
        controller.addFood("2", "Falafel", 6, "Middle East", true,11);
        controller.addFood("3", "Pizza", 12, "Italian", true,20);
        controller.addFood("4", "Cheeseburger", 8, "Fast food", true,10);
        controller.addFood("5", "Tacos", 7, "Mexican", true,10);
        controller.addFood("6", "Pasta Carbonara", 13, "Italian", true,12);
        controller.addFood("7", "Falafel", 6, "Middle East", true,11);
        controller.addFood("8", "Butter Chicken", 12, "Indian", true,8);

        Scanner scanner = new Scanner(System.in);

        App myApp = new App();

        while(true){
            System.out.println("═══════════════════════ SUN FLOWER ═══════════════════════");
            System.out.println("1 For take Online order");
            System.out.println("2 For take order");
            System.out.println("3 Go back");

            String input = scanner.nextLine();
            if(input.equalsIgnoreCase("3")){
                break;
            }

            else if(input.equals("1")){
                myApp.takeOrder(controller, scanner);
            }
            else if(input.equals("2")){
                myApp.takeOrder(controller,scanner);
                continue;
            }
        }

        scanner.close();
    }
}
