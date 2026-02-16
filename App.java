
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
                isOrdered = false;
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

            if(!controller.isFoodAvailable(id)){
                System.out.println("Sorry ! " + id + " is unavilable for now");
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
            String payment = this.paymentMethod(scanner);

            //go back to the previous stage
            if(payment.equalsIgnoreCase("goBack")){
                this.takeOrder(controller, scanner);
            }
            else{
                controller.displayOrderedCart();
                controller.order("1", "Online", foodIdArray, quantitiesArray, payment);
            }

        }
        //cancell order
        else{
            controller.cancelOrder(foodIdArray, quantitiesArray);
        }

        System.out.println("Tank you! have a good day!!!");
    }
    
    public String paymentMethod(Scanner scanner){

        String[] paymentMethod = {"QR","Cash","Card","Online"};

        while(true){
            System.out.println("═══════════════════════ PAYMENT METHOD ═══════════════════════");
            System.out.println("1 QR");
            System.out.println("2 Cash");
            System.out.println("3 Credit Card");
            System.out.println("4 Online");
            System.out.println("5 Back");
            String payment = scanner.nextLine();

            if(payment.equals("1") || payment.equals("2") || payment.equals("3") || payment.equals("4")){
                return paymentMethod[Integer.parseInt(payment) - 1];
            }
            else if(payment.equals("5")){
                return "goBack";
            }
            else{
                System.out.println("Invalid choice!");
            }
        }

    }

    public static void main(String[] args) {

        Controller controller = new Controller("2343");
        controller.addFood("Sushi", 10, "Sea food", true,2);
        controller.addFood("Falafel", 6, "Middle East", true,11);
        controller.addFood("Pizza", 12, "Italian", true,20);
        controller.addFood("Cheeseburger", 8, "Fast food", true,10);
        controller.addFood("Tacos", 7, "Mexican", true,10);
        controller.addFood("Pasta Carbonara", 13, "Italian", true,12);
        controller.addFood("Falafel", 6, "Middle East", true,11);
        controller.addFood("Butter Chicken", 12, "Indian", true,8);


        Scanner scanner = new Scanner(System.in);

        App myApp = new App();

        while(true){
            System.out.println("═══════════════════════ SUN FLOWER ═══════════════════════");
            System.out.println("1 For Online Order Service");
            System.out.println("2 For Table Order Service");
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
            else{
                System.out.println("Invalid choice!");
            }
        }

        scanner.close();
    }
}

// let user chose payment method 
// support dinning table 