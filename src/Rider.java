import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Rider extends Entity {
    private String phone;
    private static int count = 1;
    public Rider() {}
    public Rider(String name, String phone) {
        super(name);
        this.phone = phone;
        this.code = "d" + count++;
    }

    public Rider(String code, String name, String phone) {
        super(name);
        this.phone = phone;
        this.code = code;
    }

    public String getPhone() {
        return phone;
    }

    // method returns rider details for admin
    public String toString() {
        return "Rider Code: " + code + "\nRider Name " + name + "\nRider Phone " + phone + "\nOrder Count " + this.getOrderHistory().size();
    }

    // file handling-----

    public String toCSVString() {
        return code + ",," + name + ",," + phone;
    }

    // change order status to delivered
    public void setOrderStatus(int orderIndex, String newStatus) {
        orderHistory.get(orderIndex).setOrderStatus(newStatus);
    }

    //Writing Rider data to file
    public static void saveRidersToFile(Cqueue<Rider> riders) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < riders.size(); i++) {
            sb.append(riders.get(i).toCSVString() + "\n");
        }
        Files.write(Paths.get("./files/rider/riders.csv"), sb.toString().getBytes());
    }

    //Reading Rider data to file
    public static Cqueue<Rider> getRidersFromFile() throws IOException{
        Cqueue<Rider> riders = new Cqueue<>();

        // read riders.csv into a list of lines.
        List<String> lines = Files.readAllLines(Paths.get("./files/rider/riders.csv"));
        Rider.count = lines.size() + 1;
        for (int i = 0; i < lines.size(); i++) {
            // split a line by comma
            String[] dataInFile = lines.get(i).split(",,");
            String code = dataInFile[0];
            String name = dataInFile[1];
            String phone = dataInFile[2];

            riders.add(new Rider(code, name, phone));
        }
        return riders;
    }

    //main ---------------------------------------------------------------
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        //arraylists to be used in the program
        ArrayList<Item> items = new ArrayList<>();
        ArrayList<Restaurant> restaurants = new ArrayList<>();
        ArrayList<Customer> customers = new ArrayList<>();
        ArrayList<Order> orders = new ArrayList<>();
        Cqueue<Rider> riders = new Cqueue<>();

        // Reading from files
        try {
            restaurants = Restaurant.getRestaurantsFromFile();
            items = Item.getItemsFromFile(restaurants);
            customers = Customer.getCustomersFromFile();
            riders = Rider.getRidersFromFile();
            orders = Order.getOrdersFromFile(items, restaurants, customers, riders);

            //add to each rider's order histories
            for (int i = 0; i < orders.size(); i++) {
                if (orders.get(i).getPickupType().equals("Delivery")) {
                    for (int j = 0; j < riders.size(); j++) {
                        if (orders.get(i).getRider().getCode().equals(riders.get(j).getCode())) {
                            riders.get(j).addOrder(orders.get(i));
                        }
                    }
                }
                //add to each restaurant's order histories
                if (orders.get(i).getRestaurant().getCode().equals("r1")) {
                    restaurants.get(0).addOrder(orders.get(i));
                } else if (orders.get(i).getRestaurant().getCode().equals("r2")) {
                    restaurants.get(1).addOrder(orders.get(i));
                } else if (orders.get(i).getRestaurant().getCode().equals("r3")) {
                    restaurants.get(2).addOrder(orders.get(i));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("One of the files required for this program has not been found");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // welcome screen
        Rider rider = null;
        do {
            System.out.println("Welcome to Foodeliver Rider Interface");
            System.out.println("=======================");
            // printing riders
            for (int i = 0; i < riders.size(); i++) {
                System.out.println(i + " - " + riders.get(i).getName());
            }

            //input error-handling loop
            boolean custLoopCheck = false;
            do {
                try {
                    System.out.println("====================");
                    System.out.println("Select the number that comes before your name: ");
                    String inputr1 = input.nextLine();
                    rider = riders.get(Integer.parseInt(inputr1));
                    custLoopCheck = true;
                } catch (NumberFormatException e) {
                    System.out.println("You should have entered a valid number.");
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("Out of bounds.");
                } catch (Exception e) {
                    System.out.println("Invalid input.");
                }
            } while (!custLoopCheck);


            System.out.println("=======================");
            System.out.println("Select 1 to view your turn and assigned orders");
            System.out.println("Select anything else to view delivered order history");
            System.out.println("=======================");
            System.out.println("Input:");
            String inputr2 = input.nextLine();

            // rider view assigned orders
            if (inputr2.equals("1")) {
                // check if its rider turn to see orders
                if (riders.get(0).getCode().equals(rider.getCode())) {
                    System.out.println("--------------------");
                    System.out.println("It's your turn");
                    System.out.println("--------------------");

                } else { // not rider turn
                    int t = 0;
                    while (!(rider.getCode().equals(riders.get(t).getCode()))) {
                        t++;
                    }
                    System.out.println("--------------------");
                    System.out.println("PLease wait for your turn, you have " + t + " more rider(s)");
                    System.out.println("in order to take a new order.");
                    System.out.println("--------------------");

                }


                // take index to update order status to delivered
                int countCurrentOrders = 0;
                if (rider.getOrderHistory().size() != 0) {
                    for (int i =0 ; i< rider.getOrderHistory().size(); i++) {
                        if (rider.getOrderHistory().get(i).getOrderStatus().equals("Delivering")) {
                            System.out.println();
                            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                            System.out.println("Order #" + i);
                            rider.getOrderHistory().get(i).describe();
                            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                            System.out.println();
                            countCurrentOrders += 1;
                        }
                    }

                    if (countCurrentOrders >= 1) {
                        System.out.println("Please choose index to update status to Delivered");
                        System.out.println("Ex: Order#1 input : 1 ");
                        System.out.println("----------------- ");

                        String orderIndexInput;
                        int orderIndex = 0;

                        Order checkIfValidOrder;
                        boolean orderIndexLoopCheck2 = false;

                        //input error-handling loop
                        do {
                            try {
                                System.out.println("Input: ");
                                orderIndexInput = input.nextLine();
                                orderIndex = Integer.parseInt(orderIndexInput);
                                checkIfValidOrder = rider.getOrderHistory().get(orderIndex);
                                if (!checkIfValidOrder.getOrderStatus().equals("Delivering")) {
                                    System.out.println("That is not a valid order. Try again");
                                } else {
                                    orderIndexLoopCheck2 = true;
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("You should have entered a valid number. Try again.");
                            } catch (IndexOutOfBoundsException e) {
                                System.out.println("The index you entered was not in the options.");
                            } catch(Exception e) {
                                System.out.println("Invalid input.");
                            }
                        } while(!orderIndexLoopCheck2);

                        // updating order status
                        String newStatus = "Delivered";
                        rider.setOrderStatus(orderIndex, newStatus);
                        System.out.println("===================");
                        System.out.println("Order status updated successfully");


                    }else{ // no orders to deliver
                        System.out.println("There are no orders to deliver.");
                    }
                }else{ // no orders to deliver
                    System.out.println("There are no orders to deliver.");
                }


            } else{// print rider order history
                if (rider.getOrderHistory().size() == 0) {
                    System.out.println("There are no orders to deliver.");
                } else {
                    int i = 0;
                    for (Order order : rider.getOrderHistory()) {

                        System.out.println();
                        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                        System.out.println("Order #" + i);
                        order.describe();
                        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                        System.out.println();


                        i++;
                    }

                }
            }
            // go back to main menu
            System.out.println();
            System.out.println("Enter 1 to go back to main menu, where you can change to different rider");
            System.out.println("Enter anything else to terminate program.");
            System.out.println("Input:");
            String terminateFlag1 = input.nextLine();

            if (terminateFlag1.equals("1")) {
                continue;
            } else {
                //saves files and exit program
                try {

                    orders.clear();
                    for (int i=0; i < restaurants.size(); i++){
                        orders.addAll(restaurants.get(i).getOrderHistory());
                    }

                    Order.saveOrdersToFile(orders);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                System.exit(0);
            }

        } while (true);

    }


}
