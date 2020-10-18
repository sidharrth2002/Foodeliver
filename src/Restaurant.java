import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Restaurant extends Entity {
    //records order history for each restaurant
    public Restaurant() {}

    public Restaurant(String code, String name, String address) {
        super(name, address);
        this.code = code;
    }

    public void addItem(ArrayList<Item> items, String name, double price, String description) {
        Item newItem = new Item(name, price, description, this);
        items.add(newItem);
    }

    public void setOrderStatus(int orderIndex, String newStatus) {
        orderHistory.get(orderIndex).setOrderStatus(newStatus);
    }

    private String toCSVString() {
        return code + ",," + name + ",," + address;
    }

    //saving restaurants data to file
    private static void saveRestaurantsToFile(ArrayList<Restaurant> restaurants) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < restaurants.size(); i++) {
            sb.append(restaurants.get(i).toCSVString() + "\n");
        }
        Files.write(Paths.get("./files/restaurant/restaurants.csv"), sb.toString().getBytes());
    }

    //saving items data to file


    public static ArrayList<Restaurant> getRestaurantsFromFile() throws IOException {
        ArrayList<Restaurant> restaurants = new ArrayList<>();

        // read students.csv into a list of lines.
        List<String> lines = Files.readAllLines(Paths.get("./files/restaurant/restaurants.csv"));
        for (int i = 0; i < lines.size(); i++) {
            // split a line by comma
            String[] dataInFile = lines.get(i).split(",,");
            String code = dataInFile[0];
            String name = dataInFile[1];
            String address = dataInFile[2];

            restaurants.add(new Restaurant(code, name, address));
        }
        return restaurants;
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        //arraylists to be used in the program
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
            //add to each restaurant's order histories
            for (int i = 0; i < orders.size(); i++) {
                if (orders.get(i).getCode().substring(3,5).equals("r1")) {
                    restaurants.get(0).addOrder(orders.get(i));
                } else if (orders.get(i).getCode().substring(3,5).equals("r2")) {
                    restaurants.get(1).addOrder(orders.get(i));
                } else if (orders.get(i).getCode().substring(3,5).equals("r3")) {
                    restaurants.get(2).addOrder(orders.get(i));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        do {
            System.out.println("Welcome to the Foodeliver Restaurant Interface");
            System.out.println("=======================");
            System.out.println("Choose which restaurant you are.");
            System.out.println("1- " + restaurants.get(0).getName());
            System.out.println("2- " + restaurants.get(1).getName());
            System.out.println("3- " + restaurants.get(2).getName());
            System.out.println("Note: Any other inputs will go to option 3");
            System.out.println("=======================");
            String inputR = input.nextLine();

            Restaurant restaurant = null;
            // setting restaurant id
            if (inputR.equals("1")) {
                restaurant = restaurants.get(0);
            } else if (inputR.equals("2")) {
                restaurant = restaurants.get(1);
            } else {
                restaurant = restaurants.get(2);
            }

            System.out.println("=======================");
            System.out.println("Select 1 to view and edit Menu.");
            System.out.println("Select 2 to update the status of any of your current orders.");
            System.out.println("Select anything else to just view order history");
            System.out.println("=======================");
            System.out.println("Input:");
            String inputR2 = input.nextLine();

            // view and edit menu
            if (inputR2.equals("1")) {
                //printing restaurant menu
                for (int i = 0; i < items.size(); i++) {

                    //gets the code of the items and makes sure that the first 2 characters are the restaurant code
                    if (items.get(i).getCode().substring(0, 2).equals(restaurant.getCode())) {
                        System.out.println(i + ":");
                        items.get(i).describe();
                    }

                }

                // retaurant menu options
                boolean loopcheck = false;
                while (loopcheck == false) {
                    System.out.println("=======================");
                    System.out.println("Select from below: ");
                    System.out.println("1- Edit item from menu");
                    System.out.println("2- Add item to menu");
                    System.out.println("3- Delete item from menu");
                    System.out.println("=======================");
                    System.out.println("Input:");
                    String inputR3 = input.nextLine();

                    // edit menu item
                    if (inputR3.equals("1")) {
                        loopcheck = true;
                        // updated data
                        boolean newItemLoopCheck = false;
                        Item item = null;
                        String index;

                        // check user input
                        do {
                            try {
                                System.out.println("Enter item index: ");
                                index = input.nextLine();
                                item = items.get(Integer.parseInt(index));
                                //makes sure that this item is owned by the restaurant
                                //we don't want one restaurant modifying the items of another restaurant
                                if (!item.getCode().substring(0, 2).equals(restaurant.getCode())) {
                                    System.out.println("That is not a valid option. Try again.");
                                    continue;
                                }
                                newItemLoopCheck = true;
                            } catch(NumberFormatException e) {
                                System.out.println("You should have entered a valid number. Try again.");
                            } catch(IndexOutOfBoundsException e) {
                                System.out.println("Out of bounds. Try again.");
                            } catch(Exception e) {
                                System.out.println("Invalid input.");
                            }
                        } while (!newItemLoopCheck);

                        // editing item user inputs -------------------

                        //item name
                        System.out.println("Enter new name:");
                        String newName = input.nextLine();
                        item.setName(newName);

                        boolean newPriceLoopCheck = false;

                        double newPrice;

                        //error-handling for entry of price
                        //to make sure input follows format of a double
                        do {
                            try {
                                // item price and checking user input if it contains any errors
                                System.out.println("Enter new price:");
                                newPrice = input.nextDouble();
                                input.nextLine();
                                item.setPrice(newPrice);
                                newPriceLoopCheck = true;
                            } catch(InputMismatchException e) {
                                System.out.println("You should have entered a valid price. Try again.");
                                input.nextLine();
                            } catch(Exception e) {
                                System.out.println("Invalid input.");
                                input.nextLine();
                            }
                        } while (!newPriceLoopCheck);
                        // item description and checking user input if it contains any errors
                        System.out.println("Enter new description:");
                        String newDisc = input.nextLine();
                        item.setDescription(newDisc);

                        System.out.println("The item has been updated successfully.");
                        System.out.println();

                        // add menu item
                    } else if (inputR3.equals("2")) {
                        loopcheck = true;

                        // new data

                        //item name
                        System.out.println("Enter item name:");
                        String itemName = input.nextLine();

                        boolean newPriceLoopCheck2 = false;

                        double itemPrice = 0;

                        //uses error handling to make sure price entered follows format of a double
                        do {
                            try {
                                System.out.println("Enter item price:");
                                itemPrice = input.nextDouble();
                                input.nextLine();
                                newPriceLoopCheck2 = true;
                            } catch(InputMismatchException e) {
                                System.out.println("You should have entered a valid price. Try again.");
                                input.nextLine();
                            } catch(Exception e) {
                                System.out.println("Invalid input.");
                                input.nextLine();
                            }
                        } while (!newPriceLoopCheck2);

                        //item description
                        System.out.println("Enter item description:");
                        String itemDesc = input.nextLine();

                        // passing to arraylist
                        restaurant.addItem(items, itemName, itemPrice, itemDesc);

                        System.out.println("The item has been added successfully.");
                        System.out.println();
                    }
                    // remove item from menu
                    else if (inputR3.equals("3")) {
                        loopcheck = true;

                        // print restaurant menu
                        // uses format of restaurant code to find items belonging to the restaurant
                        for (int i = 0; i < items.size(); i++) {
                            if (items.get(i).getCode().substring(0, 2).equals(restaurant.getCode())) {
                                System.out.println(i);
                                items.get(i).describe();
                            }

                        }

                        boolean indexLoopCheck = false;
                        do {
                            try {
                                // item index and checking user input
                                System.out.println("Enter item index:");
                                String index = input.nextLine();
                                //makes sure that this item is owned by the restaurant
                                //we don't want one restaurant modifying the items of another restaurant
                                if (!items.get(Integer.parseInt(index)).getCode().substring(0, 2).equals(restaurant.getCode())) {
                                    System.out.println("That is not a valid option. Try again.");
                                    continue;
                                }
                                //remove from arraylist
                                items.remove(Integer.parseInt(index));
                                indexLoopCheck = true;
                            } catch(NumberFormatException e) {
                                System.out.println("You should have entered a valid number. Try again.");

                            } catch(IndexOutOfBoundsException e) {
                                System.out.println("That's not a valid index. Try again.");
                            } catch(Exception e) {
                                System.out.println("Invalid input.");
                            }
                        } while (!indexLoopCheck);


                        System.out.println("The item has been removed successfully.");
                        System.out.println();

                        // error message for menu selection
                    } else {

                        System.out.println("===================");
                        System.out.println("Invalid entry");
                        System.out.println("===================");
                    }
                }

                // update orders
            } else if (inputR2.equals("2")) {

                // print order history for restaurant
                int countCurrentOrders = 0;
                if (restaurant.getOrderHistory().size() != 0) {
                    for (int i = 0; i < restaurant.getOrderHistory().size(); i++) {
                        //if the order has already been collected, don't display under current orders
                        if (restaurant.getOrderHistory().get(i).getOrderStatus().equals("Collected")) {
                            continue;
                        }
                        System.out.println("Order #" + i);
                        restaurant.getOrderHistory().get(i).describe();
                        countCurrentOrders += 1;
                    }
                    if (countCurrentOrders >= 1) {
                        System.out.println("Please choose index to update status");
                        System.out.println("Ex: Order#1 input : 1 ");
                        System.out.println("----------------- ");

                        String orderIndexInput;
                        int orderIndex = 0;
                        Order checkIfValidOrder;

                        boolean orderIndexLoopCheck2 = false;

                        //check user input to make sure the index entered is valid and among the list of options
                        do {
                            try {
                                System.out.println("Input: ");
                                orderIndexInput = input.nextLine();
                                orderIndex = Integer.parseInt(orderIndexInput);
                                //loads into a dummy variable to check that the order index actually leads to a valid order
                                checkIfValidOrder = restaurant.getOrderHistory().get(orderIndex);
                                orderIndexLoopCheck2 = true;
                            } catch (NumberFormatException e) {
                                System.out.println("You should have entered a valid number. Try again.");
                            } catch (IndexOutOfBoundsException e) {
                                System.out.println("The index you entered was not in the options.");
                            } catch(Exception e) {
                                System.out.println("Invalid input.");
                            }
                        } while(!orderIndexLoopCheck2);

                        // updating order status
                        boolean loopcheck2 = false;
                        while (loopcheck2 == false) {
                            System.out.println("================================");
                            System.out.println("Please choose new status:");
                            System.out.println("1- Preparing");
                            System.out.println("2- Ready");
                            System.out.println("3- Delivering");
                            System.out.println("4- Collected");
                            System.out.println("================================");
                            System.out.println("Input:");
                            String orderStatus = input.nextLine();

                            // preparing
                            if (orderStatus.equals("1")) {
                                loopcheck2 = true;
                                String newStatus = "Preparing";
                                restaurant.setOrderStatus(orderIndex, newStatus);
                                System.out.println("===================");
                                System.out.println("Order status updated successfully");

                                // ready
                            } else if (orderStatus.equals("2")) {
                                loopcheck2 = true;
                                String newStatus = "Ready";
                                restaurant.setOrderStatus(orderIndex, newStatus);
                                System.out.println("===================");
                                System.out.println("Order status updated successfully");

                                // collected
                            } else if (orderStatus.equals("3")) {
                                loopcheck2 = true;
                                String newStatus = "Delivering";
                                restaurant.setOrderStatus(orderIndex, newStatus);
                                System.out.println("===================");
                                System.out.println("Order status updated successfully");

                            } else if (orderStatus.equals("4")) {
                                loopcheck2 = true;
                                String newStatus = "Collected";
                                restaurant.setOrderStatus(orderIndex, newStatus);
                                System.out.println("===================");
                                System.out.println("Order status updated successfully");

                                // error message for menu selection
                            } else {
                                System.out.println("===================");
                                System.out.println("Invalid entry");
                                System.out.println("===================");
                            }
                        }
                        // all orders collected
                    } else {
                        System.out.println("All your orders have already been collected.");
                    }
                    // no orders to view
                } else {
                    System.out.println("There are no orders for you currently.");
                }

            } else {
                // print order history
                if (restaurant.getOrderHistory().size() != 0) {
                    System.out.println("Here's your order history.");
                    for (int i = 0; i < restaurant.getOrderHistory().size(); i++) {
                        System.out.println("Order #" + i);
                        restaurant.getOrderHistory().get(i).describe();
                    }
                } else {
                    //if there are no orders for the restaurant, print this instead of printing nothing
                    System.out.println("There are no past orders for you.");
                }
            }

            // option to go back to main menu or exit
            System.out.println();
            System.out.println("Enter 1 to go back to main menu, where you can change view to a different restaurant.");
            System.out.println("Enter anything else to terminate program.");
            System.out.println("Input:");
            String terminateFlag2 = input.nextLine();

            if (terminateFlag2.equals("1")) {
                continue;
            } else {
                //save everything to file before exiting
                try {
                    Item.saveItemsToFile(items);
                    Restaurant.saveRestaurantsToFile(restaurants);
                    orders.clear();
                    orders.addAll(restaurants.get(0).getOrderHistory());
                    orders.addAll(restaurants.get(1).getOrderHistory());
                    orders.addAll(restaurants.get(2).getOrderHistory());
                    Order.saveOrdersToFile(orders);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                System.exit(0);
            }
        } while(true);





//        ArrayList<Restaurant> restaurants = new ArrayList<>();
//        restaurants.add(new Restaurant("Burger King", "2, Persiaran Multimedia"));
//        restaurants.add(new Restaurant("Pizza Hut", "3, Jalan Villa"));
//        restaurants.add(new Restaurant("Glaze Eatery", "4, Persiaran Apec"));
//
//        try {
//            saveRestaurantsToFile(restaurants);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//        try {
//            ArrayList<Restaurant> restaurants2 = getRestaurantsFromFile();
//            System.out.println(restaurants2.get(1).address);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        ArrayList<Item> items = new ArrayList<>();
//        items.add(new Item("Item1", 35.50, "Tasty food", restaurants.get(0).getCode(), restaurants.get(0).getName()));
//        items.add(new Item("Item2", 35.60, "Tasty food", restaurants.get(1).getCode(), restaurants.get(1).getName()));
//        items.add(new Item("Item3", 35.50, "Tasty food", restaurants.get(1).getCode(),restaurants.get(1).getName()));
//
//        try {
//            saveItemsToFile(items);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        ArrayList<Item> items2 = new ArrayList<>();
//
//        try {
//            items2 = Item.getItemsFromFile();
//            System.out.println(items.get(2).getRestaurantName());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
