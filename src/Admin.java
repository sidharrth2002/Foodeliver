import java.io.IOException;
import java.util.*;

public abstract class Admin {
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
            items = Item.getItemsFromFile(restaurants);
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

                if (orders.get(i).getPickupType().equals("Delivery")) {
                    for (int k = 0; k < riders.size(); k++) {
                        if (orders.get(i).getRider().getCode().equals(riders.get(k).getCode())) {
                            riders.get(k).addOrder(orders.get(i));
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        do {

            System.out.println("Welcome to Foodeliver Admin Interface");
            System.out.println("=======================");
            System.out.println("What do you want to do?");
            System.out.println("1- Add New Rider to System");
            System.out.println("2- View Rider Queue");
            System.out.println("3- View all consolidated order histories");
            System.out.println("4- View system statistics");
            System.out.println("Note: Any other inputs will go to option 4");
            System.out.println("=======================");
            String inputa1 = input.nextLine();

            if (inputa1.equals("1")) {
                System.out.println("Enter name of new rider: ");
                String ridername = input.nextLine();
                System.out.println("Enter phone number of rider: ");
                String riderphone = input.nextLine();
                Rider newRider = new Rider(ridername, riderphone);
                riders.add(newRider);
                System.out.println("New Rider added successfully");
            } else if (inputa1.equals("2")) {
                System.out.println("Here is the current queue of riders: ");
                for (Rider rider : riders) {
                    System.out.println(rider);
                    System.out.println("-----------------------------------");
                }
            } else if (inputa1.equals("3")) {
                System.out.println("Here is the list of consolidated orders: ");
                for (Order order : orders) {
                    System.out.println();
                    System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    order.describe();
                    System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    System.out.println();
                }
            } else {
                System.out.println();
                System.out.println();
                System.out.println("==========================================");
                System.out.println("RIDERS SORTED BY HIGHEST DELIVERY COUNT");
                //making copy of riders list so as to not mess up the order of the queue
                LinkedList<Rider> ridersCopy = new LinkedList<>();
                for (Rider rider : riders) {
                    ridersCopy.add(rider);
                }
                //sorts by comparable
                Collections.sort(ridersCopy);
                for (Rider rider : ridersCopy) {
                    System.out.println(rider);
                    System.out.println("-----------------------------------");
                }
                System.out.println();
                System.out.println();
                System.out.println("==========================================");
                System.out.println("RESTAURANTS SORTED BY HIGHEST ORDER COUNT");
                ArrayList<Restaurant> restaurantsCopy = new ArrayList<>(restaurants);
                Collections.sort(restaurantsCopy);

                for (Restaurant restaurant : restaurantsCopy) {
                    System.out.println(restaurant);
                    System.out.println("-----------------------------------");
                }
                System.out.println();
                System.out.println();
                System.out.println("==========================================");
                System.out.println("CUSTOMERS SORTED BY HIGHEST ORDER COUNT");
                ArrayList<Customer> customersCopy = new ArrayList<>(customers);
                Collections.sort(customersCopy);

                for (Customer customer : customersCopy) {
                    System.out.println(customer);
                    System.out.println("-----------------------------------");
                }
            }

            System.out.println("Press 1 to go back to main menu. Anything else to terminate.");
            String again = input.nextLine();
            if (again.equals("1")) {
                continue;
            } else {
                try {
                    Rider.saveRidersToFile(riders);
                    System.exit(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } while (true);
    }
}
