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

    public String toCSVString() {
        return code + ",," + name + ",," + phone;
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
            items= Item.getItemsFromFile(restaurants);
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
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



        do {
            System.out.println("Welcome to Foodeliver Rider Interface");
            System.out.println("=======================");
            System.out.println("Choose which rider you are.");
            System.out.println("1- " + riders.get(0).getName());
            System.out.println("2- " +  riders.get(1).getName());
            System.out.println("3- " +  riders.get(2).getName());
            System.out.println("Note: Any other inputs will go to option 3");
            System.out.println("=======================");
            String inputr1 = input.nextLine();

            Rider rider = null;
            // setting restaurant id
            if (inputr1.equals("1")) {
                rider = riders.get(0);
            } else if (inputr1.equals("2")) {
                rider = riders.get(1);
            } else {
                rider = riders.get(2);
            }

            System.out.println("=======================");
            System.out.println("Select 1 to view assigned orders");
            System.out.println("Select anything else to view delivered order history");
            System.out.println("=======================");
            System.out.println("Input:");
            String inputr2 = input.nextLine();

            // rider view assigned orders
            if (inputr2.equals("1")) {

                // check if its rider turn to see orders
                if (riders.get(0).getCode().equals(rider.getCode()) ){
                    if (rider.getOrderHistory().size() == 0) {
                        System.out.println("There are no orders to deliver.");
                    }else{
                        int i = 0;
                        for (Order order : rider.getOrderHistory()) {
                            System.out.println("Order #" + i);
                            order.describe();
                            i++;
                        }
                        // print assigned orders
                    }

                }else{ // not rider turn
                    int t =0;
                    while ( !(rider.getCode().equals(riders.get(t).getCode())) ){
                        t++;
                    }

                    System.out.println("PLease wait for your turn, you have " + t + " more rider(s) to start delivering");

                }

            }else{// print rider order history
                for (int i = 0; i < rider.orderHistory.size(); i++) {
                    System.out.println("Order #" + i);
                    rider.getOrderHistory().get(i).describe();
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
                System.exit(0);
            }

        } while (true);

    }


}