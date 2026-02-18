
import java.util.ArrayList;
import java.util.Scanner;
import controllers.Controller;

public class App {

    public void takeOrder( Controller controller,Scanner scanner ){

        boolean isOrdered = false;
        String tableNumber = null;

        while(true){
            controller.displayInventory();

            System.out.println("═══════════════════════ FOOD ID ═══════════════════════");
            System.out.println("Enter Food ID for ordering !:");
            System.out.println("Q/q for Quit & cancel order");

            boolean cartHasItem = controller.getCartSize() > 0;

            if(cartHasItem){
                System.out.println("O/o for see Ordere!");

                if(tableNumber != null){
                    System.out.println("[Table Number "+tableNumber+"] " + "T/t for change Table number.");
                }

                else{
                    System.out.println("T/t for choose Table");
                }
                System.out.println("C/c for Checkout!");

            }

            String id = scanner.nextLine();

            if(cartHasItem && id.equalsIgnoreCase("O")){
                controller.displayOrderedCart();
                continue;
            }

            if(cartHasItem && id.equalsIgnoreCase("C")){
                isOrdered = true;
                break;
            }

            if(id.equalsIgnoreCase("T")){

                while(true){
                    tableNumber = this.choosingTable(controller, scanner);
                    if(tableNumber != null){
                        break;
                    }
                }
                continue;
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
                controller.addToCart(id,Quantity);

            }catch(IllegalArgumentException e){
                System.out.println(e);
            }

        }
        
        //process order
        if(isOrdered){
            String payment = this.paymentMethod(scanner);

            //go back to the previous stage
            if(payment.equalsIgnoreCase("goBack")){
                this.takeOrder(controller, scanner);
                return;
            }

            else{
                controller.displayOrderedCart();

                if(tableNumber != null){
                    controller.order("1", "Table", payment,tableNumber);
                }
                else{
                    controller.order("1", "Online", payment,tableNumber);
                }
            }

        }
        //cancell order
        else{
            controller.cancelOrder();
        }

        System.out.println("Thank you! have a good day!!!");
        System.out.println(" ");
        System.out.println(" ");
        System.out.println(" ");
        System.out.println(" ");
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

    public String choosingTable(Controller controller, Scanner scanner){

        controller.displayTableInventory();
        System.out.println("═══════════════════════ Quantity ═══════════════════════");
        System.out.println("Enter Table Number: ");
        String tableIndex = scanner.nextLine();

        if(!controller.validateTableIndex(tableIndex)){
            System.out.println("Please, enter a valid choice!");
            return null;
        }

        return tableIndex;
    }

    public static void main(String[] args) {

        Controller controller = new Controller("2343");

        // Food
        controller.addFood("Sushi", 10, "Sea food", true,2);
        controller.addFood("Falafel", 6, "Middle East", true,11);
        controller.addFood("Pizza", 12, "Italian", true,20);
        controller.addFood("Cheeseburger", 8, "Fast food", true,10);
        controller.addFood("Tacos", 7, "Mexican", true,10);
        controller.addFood("Pasta Carbonara", 13, "Italian", true,12);
        controller.addFood("Falafel", 6, "Middle East", true,11);
        controller.addFood("Butter Chicken", 12, "Indian", true,8);

        // Standard Seating
        controller.addTable(4, false, false, "Main Hall", 0);
        controller.addTable(2, true, false, "Main Hall", 0);
        controller.addTable(6, false, false, "Terrace", 0);
        controller.addTable(4, false, false, "Terrace", 5.50);

        // VIP Seating
        controller.addTable(2, false, true, "Window Side", 25.0);
        controller.addTable(8, true, true, "Private Room", 100.0);
        controller.addTable(4, false, true, "Balcony", 45.0);

        // Bar & Casual
        controller.addTable(1, false, false, "Bar Counter", 0);
        controller.addTable(1, true, false, "Bar Counter", 0);

        // Special Large Table
        controller.addTable(12, false, true, "Grand Suite", 150.0);

        controller.displayTableInventory();

        Scanner scanner = new Scanner(System.in);

        App myApp = new App();

        while(true){
            System.out.println("═══════════════════════ SUN FLOWER ═══════════════════════");
            System.out.println("1 For  Order Service");
            System.out.println("2 For  Order Service");
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

