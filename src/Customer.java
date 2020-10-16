import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Customer extends Entity {
    private String phone;
    private static int count = 1;

    //static queue to assign riders

    public Customer() {}

    public Customer(String name, String address, String phone) {
        super(name, address);
        this.phone = phone;
        this.code = "c" + count++;
    }

    //not needed for stage 1 but maybe later
    public String getPhone() {
        return phone;
    }

    public String toCSVString() {
        return name + ",," + address + ",," + phone;
    }

    private static void saveCustomersToFile(ArrayList<Customer> customers) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < customers.size(); i++) {
            sb.append(customers.get(i).toCSVString() + "\n");
        }
        Files.write(Paths.get("./files/customer/customers.csv"), sb.toString().getBytes());
    }

    public static ArrayList<Customer> getCustomersFromFile() throws IOException {
        ArrayList<Customer> customers = new ArrayList<>();

        // read students.csv into a list of lines.
        List<String> lines = Files.readAllLines(Paths.get("./files/customer/customers.csv"));
        for (int i = 0; i < lines.size(); i++) {
            // split a line by comma
            String[] dataInFile = lines.get(i).split(",,");

            String name = dataInFile[0];
            String address = dataInFile[1];
            String phone = dataInFile[2];

            customers.add(new Customer(name, address, phone));
        }
        return customers;
    }

    //sorting functions----------
    //Alphabetically
    private static void sortAlphabetically(ArrayList<Item> itemsToSort) {
        if (itemsToSort.size() > 0) {
            //overrides the collections.sort by passing in a custom comparator to sort by particular field in an object
            Collections.sort(itemsToSort, new Comparator< Item >() {
                @Override
                public int compare(final Item item1, final Item item2) {
                    return item1.getName().compareTo(item2.getName());
                }
            });
        }

        //prints sorted items
        for (int i = 0; i < itemsToSort.size(); i++) {
            System.out.println("Item #" + i + ": ");
            itemsToSort.get(i).describe();
            System.out.println();
        }
    }

    //By Price
    private static void sortByPrice(ArrayList<Item> itemsToSort) {
        System.out.println("We get here");
        if (itemsToSort.size() > 0) {
            //overriding Collections.sort with another custom comparator
            Collections.sort(itemsToSort, new Comparator < Item > () {
                @Override
                public int compare(final Item item1, final Item item2) {
                    if (item1.getPrice() == item2.getPrice()) {
                        return 0;
                    }
                    return item1.getPrice() < item2.getPrice() ? -1 : 1;
                }
            });
        }

        for (int i = 0; i < itemsToSort.size(); i++) {
            System.out.println("Item #" + i + ": ");
            itemsToSort.get(i).describe();
            System.out.println();
        }
    }

    //method creates an order and passes it to both restaurant and customer objects
//    private static void makeOrder(Restaurant restaurant, Customer customer, LinkedList<Rider> riders, ArrayList <Item> itemsToOrder, ArrayList <Integer> quantityOfItems, String pickupType) {
//        Order newOrder = new Order(restaurant, customer, rider, itemsToOrder, quantityOfItems);
//        restaurant.addOrder(newOrder);
//        customer.addOrder(newOrder);
    //    rider.addOrder(newOrder);
//    }


    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        //arraylists to be used in the program
        ArrayList<Item> items = new ArrayList<>();
        ArrayList<Restaurant> restaurants = new ArrayList<>();
        ArrayList<Customer> customers = new ArrayList<>();
        ArrayList<Order> orders = new ArrayList<>();

        try {
            restaurants = Restaurant.getRestaurantsFromFile();
            items= Item.getItemsFromFile(restaurants);
            customers = Customer.getCustomersFromFile();
            orders = Order.getOrdersFromFile(items, restaurants, customers);

            //add to each restaurant's order histories
            for (int i = 0; i < orders.size(); i++) {
                if (orders.get(i).getCode().substring(3,5).equals("r1")) {
                    restaurants.get(0).getOrderHistory().add(orders.get(i));
                } else if (orders.get(i).getCode().substring(3,5).equals("r2")) {
                    restaurants.get(1).getOrderHistory().add(orders.get(i));
                } else if (orders.get(i).getCode().substring(3,5).equals("r3")) {
                    restaurants.get(2).getOrderHistory().add(orders.get(i));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        //when sorting, need to make copy of the items first to not mess up original order of items
        ArrayList<Item> itemsToSort = new ArrayList<>(items);
        System.out.println("sorted alphabetically");
        sortAlphabetically(itemsToSort);
        System.out.println();
        System.out.println("sorted by price");
        sortByPrice(itemsToSort);
        System.out.println();

//        ArrayList<Customer> customers = new ArrayList<>();
//        customers.add(new Customer("Sid", "2, Jalan Villa", "013456789"));
//        customers.add(new Customer("Ahmed", "4, Jalan Villa", "435345"));
//        customers.add(new Customer("John", "5, Jalan Villa", "435455676"));
//
//        try {
//            saveCustomersToFile(customers);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            ArrayList<Customer> customers2 = getCustomersFromFile();
//            System.out.println(customers2.get(1).getAddress());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }
}
