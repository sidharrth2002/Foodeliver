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

    public String getPhone() {
        return phone;
    }

    //added toString
    public String toString() {
        return "Rider Code: " + code + "\nRider Name " + name + "\nRider Phone " + phone + "\nOrder Count " + this.getOrderHistory().size();
    }


    public String toCSVString() {
        return code + ",," + name + ",," + phone;
    }

    public void setOrderStatus(int orderIndex, String newStatus) {
        orderHistory.get(orderIndex).setOrderStatus(newStatus);
    }

    //saving Rider data to file
    public static void saveRidersToFile(LinkedList<Rider> riders) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < riders.size(); i++) {
            sb.append(riders.get(i).toCSVString() + "\n");
        }
        Files.write(Paths.get("./files/rider/riders.csv"), sb.toString().getBytes());
    }

    public static LinkedList<Rider> getRidersFromFile() throws IOException{
        LinkedList<Rider> riders = new LinkedList<>();

        // read students.csv into a list of lines.
        List<String> lines = Files.readAllLines(Paths.get("./files/rider/riders.csv"));
        for (int i = 0; i < lines.size(); i++) {
            // split a line by comma
            String[] dataInFile = lines.get(i).split(",,");
            String code = dataInFile[0];
            String name = dataInFile[1];
            String phone = dataInFile[2];

            riders.add(new Rider(name, phone));
        }
        return riders;
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        ArrayList<Item> items = new ArrayList<>();
        ArrayList<Restaurant> restaurants = new ArrayList<>();
        ArrayList<Customer> customers = new ArrayList<>();
        ArrayList<Order> orders = new ArrayList<>();
        LinkedList<Rider> riders = new LinkedList<>();

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
                        if (orders.get(i).rider.equals(riders.get(j))) {
                            riders.get(j).addOrder(orders.get(i));
                        }
                    }
                }

                if (orders.get(i).getCode().substring(3, 5).equals("r1")) {
                    restaurants.get(0).addOrder(orders.get(i));
                } else if (orders.get(i).getCode().substring(3, 5).equals("r2")) {
                    restaurants.get(1).addOrder(orders.get(i));
                } else if (orders.get(i).getCode().substring(3, 5).equals("r3")) {
                    restaurants.get(2).addOrder(orders.get(i));
                }

            }


        } catch (IOException e) {
            e.printStackTrace();
        }


        Rider rider = null;
        do {
            System.out.println("Welcome to Foodeliver Rider Interface");
            System.out.println("=======================");
            for (int i = 0; i < riders.size(); i++) {
                System.out.println(i + " - " + riders.get(i).getName());
            }

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


                // take index to update order status to delivering
                int countCurrentOrders = 0;
                if (rider.getOrderHistory().size() != 0) {
                    for (int i =0 ; i< rider.getOrderHistory().size(); i++) {
                        if (rider.getOrderHistory().get(i).getOrderStatus().equals("Delivering")) {
                            System.out.println("Order #" + i);
                            rider.getOrderHistory().get(i).describe();
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
                        do {
                            try {
                                System.out.println("Input: ");
                                orderIndexInput = input.nextLine();
                                orderIndex = Integer.parseInt(orderIndexInput);
                                checkIfValidOrder = rider.getOrderHistory().get(orderIndex);
                                orderIndexLoopCheck2 = true;
                            } catch (NumberFormatException e) {
                                System.out.println("You should have entered a valid number. Try again.");
                            } catch (IndexOutOfBoundsException e) {
                                System.out.println("The index you entered was not in the options.");
                            } catch(Exception e) {
                                System.out.println("Invalid input.");
                            }
                        } while(!orderIndexLoopCheck2);


                        String newStatus = "Delivered";
                        rider.setOrderStatus(orderIndex, newStatus);
                        System.out.println("===================");
                        System.out.println("Order status updated successfully");


                    }else{
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
//                        if (rider.getOrderHistory().get(i).getOrderStatus().equals("Delivered")) {
                            System.out.println("Order #" + i);
                            order.describe();
//                        }
                        i++;
                    }

                }
            }

            System.out.println();
            System.out.println("Enter 1 to go back to main menu, where you can change to different rider");
            System.out.println("Enter anything else to terminate program.");
            System.out.println("Input:");
            String terminateFlag1 = input.nextLine();

            if (terminateFlag1.equals("1")) {
                continue;
            } else {
                //save everything to file before exiting
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