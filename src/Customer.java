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

    public String getPhone() {
        return phone;
    }

    public String toString() {
        return "Customer Code: " + code + "\nCustomer Name: " + name + "\nCustomer Phone: " + phone + "\nOrder Count: " + getOrderHistory().size();
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

    //method creates an order and passes it to both restaurant and customer objects
    private static void makeOrder(Restaurant restaurant, Customer customer, Cqueue<Rider> riders, ArrayList <Item> itemsToOrder, ArrayList <Integer> quantityOfItems, String pickupType) {
        // make new order and pass it to 3 user types
        if (pickupType.equals("Delivery")) {
            // riders turn
            Rider crider = riders.getHead(); // current rider
            riders.removeFirst(); // delete current rider
            riders.add(crider); // add current rider to the end
            Order newOrder = new Order(restaurant, customer, crider, itemsToOrder, quantityOfItems, pickupType, "Preparing");
            crider.addOrder(newOrder);
            restaurant.addOrder(newOrder);
            customer.addOrder(newOrder);
        } else {
            Order newOrder = new Order(restaurant, customer, itemsToOrder, quantityOfItems, pickupType, "Preparing");
            restaurant.addOrder(newOrder);
            customer.addOrder(newOrder);
        }
    }

    //Stores all the food items from all the restaurants


    //main ---------------------------------------------------------------
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        //arraylists to be used in the program
        ArrayList<Item> items = new ArrayList<>();
        ArrayList<Restaurant> restaurants = new ArrayList<>();
        ArrayList<Customer> customers = new ArrayList<>();
        ArrayList<Order> orders = new ArrayList<>();
        Cqueue<Rider> riders = new Cqueue<>();

        try {
            restaurants = Restaurant.getRestaurantsFromFile();
            items= Item.getItemsFromFile(restaurants);
            customers = Customer.getCustomersFromFile();
            riders = Rider.getRidersFromFile();
            orders = Order.getOrdersFromFile(items, restaurants, customers, riders);

            //add to each restaurant's order histories
            for (int i = 0; i < orders.size(); i++) {
                if (orders.get(i).getRestaurant().getCode().equals("r1")) {
                    restaurants.get(0).addOrder(orders.get(i));
                } else if (orders.get(i).getRestaurant().getCode().equals("r2")) {
                    restaurants.get(1).addOrder(orders.get(i));
                } else if (orders.get(i).getRestaurant().getCode().equals("r3")) {
                    restaurants.get(2).addOrder(orders.get(i));
                }

                for (int j = 0; j < customers.size(); j++) {
                    if (customers.get(j).getCode().equals(orders.get(i).getCustomer().getCode())) {
                        customers.get(j).addOrder(orders.get(i));
                    }
                }
            }


            System.out.println(restaurants.get(0).getCode());

        } catch (IOException e) {
            e.printStackTrace();
        }

        // adds riders queue to Customer class
//        try {
//            Customer.ridersq = Rider.getRidersFromFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        Restaurant restaurant;
        Customer customer = null;
        String pickupType;

        do {
            System.out.println("Welcome to Foodeliver Customer Interface");
            System.out.println("=======================");
            System.out.println("Are you an existing customer or a new customer?");
            System.out.println("Select 1 if existing, anything else if new.");
            System.out.println("=======================");
            String inputc1 = input.nextLine();

            // Existing customer - input checking
            if (inputc1.equals("1")) {

                if (customers.size() == 0) {
                    System.out.println("There are no existing customers.");
                    System.exit(0);
                }

                for (int i = 0; i < customers.size(); i++) {
                    System.out.println(i + " - " + customers.get(i).getName());
                }

                // user input
                // load to a customer variable
                boolean custLoopCheck = false;
                do {
                    try {
                        System.out.println("====================");
                        System.out.println("Select the number that comes before your name: ");
                        String inputC2 = input.nextLine();
                        customer = customers.get(Integer.parseInt(inputC2));
                        custLoopCheck = true;
                    } catch(NumberFormatException e) {
                        System.out.println("You should have entered a valid number.");
                    } catch(IndexOutOfBoundsException e) {
                        System.out.println("Out of bounds.");
                    } catch(Exception e) {
                        System.out.println("Invalid input.");
                    }
                } while (!custLoopCheck);

            }else{// create new customer

                String name, phoneNumber, address;
                System.out.println("================================");
                System.out.println("Please enter your name: ");
                name = input.nextLine();

                System.out.println("Please enter your address: ");
                address = input.nextLine();

                System.out.println("Please enter your phone number: ");
                phoneNumber = input.nextLine();

                // create customer object
                customer = new Customer(name, address, phoneNumber);
                //add to the customer arraylist
                customers.add(customer);
            }


            // menu 1  for customer -------------------------
            System.out.println("================================");
            System.out.println("1- Show Menu sorted Alphabetically To Order");
            System.out.println("2- Show Menu sorted by Price to Order");
            System.out.println("3- See Order History");
            System.out.println("================================");
            System.out.println("Input:");
            String inputC3 = input.nextLine();


            if (inputC3.equals("1") || inputC3.equals("2")) {
                // created to sort items
                ArrayList<Item> itemsToSort = new ArrayList<>(items);

                if (inputC3.equals("1")) {
                    System.out.println();
                    System.out.println();
                    System.out.println("Sorted Alphabetically:");
                    System.out.println("+++++++++++++++++++++++++++++++++++");
                    Item.sortAlphabetically(itemsToSort);
                    System.out.println("+++++++++++++++++++++++++++++++++++");
                } else if (inputC3.equals("2")) {
                    System.out.println();
                    System.out.println();
                    System.out.println("Sorted by Price:");
                    System.out.println("+++++++++++++++++++++++++++++++++++");
                    Item.sortByPrice(itemsToSort);
                    System.out.println("+++++++++++++++++++++++++++++++++++");
                }
                // adding order
                ArrayList<Item> itemsToOrder = new ArrayList<>();
                ArrayList<Integer> quantityOfItems = new ArrayList<>();

                // adding items to cart
                System.out.println();
                System.out.println();
                System.out.println("========================================");
                System.out.println("Please enter item index to add to cart. Here's an example: ");
                System.out.println("----------------");
                System.out.println("Item #0:  ");
                System.out.println("Name: Juice, Price: 5.0, Description: Tasty juice, Item Code: r3-1");
                System.out.println();
                System.out.println("Input: ");
                System.out.println("0");
                System.out.println("Juice is added to cart");
                System.out.println("========================================");

                Item foodItem = null;
                String inputC4, inputC5;
                boolean itemLoopCheck = false;

                //error-handling loop
                do {
                    try {
                        System.out.println("Input:");
                        inputC4 = input.nextLine();
                        foodItem = itemsToSort.get(Integer.parseInt(inputC4));
                        System.out.println("\nEnter the quantity of this item that you want to order:");
                        inputC5 = input.nextLine();
                        // adding item and quantity
                        quantityOfItems.add(Integer.parseInt(inputC5));
                        itemsToOrder.add(foodItem);
                        itemLoopCheck = true;
                    } catch(NumberFormatException e) {
                        System.out.println("You should have entered a valid number. Try the whole thing again.");
                    } catch(IndexOutOfBoundsException e) {
                        System.out.println("Out of bounds. Try again.");
                    } catch(Exception e) {
                        System.out.println("Invalid input.");
                    }
                } while (!itemLoopCheck);


                String currentRestaurantID = foodItem.getCode().substring(0, 2);


                System.out.println("=============================================");
                System.out.println("Do you want to add more items?");
                System.out.println("Enter 1 to add more and anything else to stop:");
                String inputC6 = input.nextLine();
                boolean continueAdding = false;
                if (inputC6.equals("1")) {
                    continueAdding = true;
                }

                while (continueAdding) {
                    // same sort as previous
                    if (inputC3.equals("1")) {
                        System.out.println("sorted alphabetically");
                        Item.sortAlphabetically(itemsToSort);
                    } else if (inputC3.equals("2")) {
                        System.out.println("sorted by price");
                        Item.sortByPrice(itemsToSort);
                    }

                    System.out.println("========================================");
                    System.out.println("Please enter item index to add to cart");
                    System.out.println("========================================");

                    boolean itemRepeatLoopCheck = false;
                    do {
                        try {
                            System.out.println("Input:");
                            inputC4 = input.nextLine();
                            foodItem = itemsToSort.get(Integer.parseInt(inputC4));

                            // making sure that all items added to cart are from same restaurant
                            if (!(foodItem.getCode().substring(0, 2).equals(currentRestaurantID))) {
                                System.out.println("You cannot choose from different restaurants. Try again: ");
                                continue;
                            }

                            //checks to see if that item is already in the order
                            if (itemsToOrder.contains(foodItem)) {
                                System.out.println("This item is already in your cart. Try again.");
                                continue;
                            }

                            // item quantity
                            System.out.println("\nEnter the quantity of this item that you want to order: ");
                            inputC5 = input.nextLine();

                            quantityOfItems.add(Integer.parseInt(inputC5));
                            itemsToOrder.add(foodItem);
                            itemRepeatLoopCheck = true;

                            // user input check
                        } catch(NumberFormatException e) {
                            System.out.println("You should have entered a valid number. Try the whole thing again.");
                        } catch(IndexOutOfBoundsException e) {
                            System.out.println("Out of bounds. Try again.");
                        }
                    } while (!itemRepeatLoopCheck);


                    System.out.println("Enter 1 to add more and anything else to stop.");
                    inputC6 = input.nextLine();

                    if (!(inputC6.equals("1"))) {
                        continueAdding = false;
                    }
                }

                // pickup type
                System.out.println("Please select Order pickup type.");
                System.out.println("Select 1 - for Self Pickup , anything else to Delivery ");
                pickupType = input.nextLine();

                if (pickupType.equals("1")) {
                    pickupType ="Self-pickup";
                } else {
                    pickupType ="Delivery";
                }


                // setting restaurant id
                if (currentRestaurantID.equals("r1")) {
                    restaurant = restaurants.get(0);
                } else if (currentRestaurantID.equals("r2")) {
                    restaurant = restaurants.get(1);
                } else {
                    restaurant = restaurants.get(2);
                }


                makeOrder(restaurant , customer , riders , itemsToOrder , quantityOfItems , pickupType);

                System.out.println("======================================================================");
                System.out.println("Your order has been made successfully. Please check your order history for more updates.");


            }else{
                //print order history
                if (customer.getOrderHistory().size() == 0) {
                    System.out.println("There are no past orders.");
                    System.out.println("Let's go back to main menu.");
                } else {
                    //loops through customer's order history and prints
                    for (int i = 0; i < customer.getOrderHistory().size(); i++) {
                        System.out.println();
                        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                        System.out.println("Order #" + i);
                        customer.getOrderHistory().get(i).describe();
                        //if ready to be collected, inform customer
                        if (customer.getOrderHistory().get(i).getOrderStatus().equals("Ready")) {
                            System.out.println("THIS ORDER IS READY TO BE COLLECTED.");
                        }
                        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                        System.out.println();
                    }
                }

            }

            System.out.println();
            System.out.println("Enter 1 to go back to main menu, where you can change to different customer");
            System.out.println("Enter anything else to terminate program.");
            System.out.println("Input:");
            String terminateFlag1 = input.nextLine();

            if (terminateFlag1.equals("1")) {
                continue;
            } else {
                try {
                    saveCustomersToFile(customers);
                    orders.clear();
                    orders.addAll(restaurants.get(0).getOrderHistory());
                    orders.addAll(restaurants.get(1).getOrderHistory());
                    orders.addAll(restaurants.get(2).getOrderHistory());
                    Order.saveOrdersToFile(orders);
                    Rider.saveRidersToFile(riders);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.exit(0);
            }

        } while ( true );



    }


}
