import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Order {
    private Restaurant restaurant;
    private Customer customer;
    public Rider rider;
    private String orderStatus = "Preparing";
    private String code; //ordercode-restaurantcode-customercode-ridercode
    private String pickupType;

    //contains item objects chosen
    public ArrayList<Item> itemsToOrder;
    //contains the quantity of corresponding item object- both act as parallel arrays
    private ArrayList<Integer> quantityOfItems;
    private double totalPrice;
    private static int count = 1;

    public Order() {}

    //used for takeaway
    public Order(Restaurant restaurant, Customer customer, ArrayList<Item> itemsToOrder, ArrayList<Integer> quantityOfItems, String pickupType, String orderStatus) {
        this.restaurant = restaurant;
        this.customer = customer;
        this.itemsToOrder = itemsToOrder;
        this.quantityOfItems = quantityOfItems;
        this.totalPrice = calculateTotalPrice();
        this.orderStatus = orderStatus;
        this.pickupType = pickupType;
        this.code = "o" + count++ + "-" + restaurant.getCode() + "-" + customer.getCode();
    }

    //used for delivery
    public Order(Restaurant restaurant, Customer customer, Rider rider, ArrayList<Item> itemsToOrder, ArrayList<Integer> quantityOfItems, String pickupType, String orderStatus) {
        this.restaurant = restaurant;
        this.customer = customer;
        this.rider = rider;
        this.itemsToOrder = itemsToOrder;
        this.quantityOfItems = quantityOfItems;
        this.totalPrice = calculateTotalPrice();
        this.orderStatus = orderStatus;
        this.pickupType = pickupType;

        this.code = "o" + count++ + "-" + restaurant.getCode() + "-" + customer.getCode();
    }


    // uses item price and quantity arraylists to calculate the total price of the order
    private double calculateTotalPrice() {
        double total = 0;
        for (int i = 0; i < itemsToOrder.size(); i++) {
            total += (itemsToOrder.get(i).getPrice() * quantityOfItems.get(i));
        }
        return total;
    }

    public String getCode() {
        return code;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getPickupType() {
        return pickupType;
    }
    //setters

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    // method which prints order details
    public void describe() {
        System.out.println("---------------------------------------------------------------");
        System.out.println("Customer making the Order: " + customer.getName());
        System.out.println("Restaurant taking the Order: " + restaurant.getName());
       if (pickupType.equals("Delivery")) {
           System.out.println("Rider taking the Order: " + rider.getName() + " his phone number: " + rider.getPhone());
       }
        System.out.println("---------------------------------------------------------------");
        System.out.println("The total price: " + totalPrice );
        System.out.println("The status of this order is: " + orderStatus);

        System.out.println("The items are: ");
        System.out.println();
        // calling the item's describe method for each item in the order
        for (int i = 0; i < itemsToOrder.size(); i++) {
            System.out.println("Quantity : " + quantityOfItems.get(i));
            itemsToOrder.get(i).describe();
        }

    }

    public String toCSVString() {
        StringBuilder sb = new StringBuilder();

        sb.append(code).append(",,").append(restaurant.getCode()).append(",,").append(customer.getCode()).append(",,");

        if (pickupType.equals("Delivery")) {
            sb.append(rider.getCode()).append(",,").append(orderStatus).append(",,").append(pickupType);
        } else {
            sb.append("d0").append(",,").append(orderStatus).append(",,").append(pickupType);
        }

        StringBuilder sb2 = new StringBuilder();
        for (int i = 0; i < itemsToOrder.size(); i++) {
            if (i != itemsToOrder.size() - 1) {
                sb2.append(itemsToOrder.get(i).getCode()).append(",");
            } else {
                sb2.append(itemsToOrder.get(i).getCode());
            }
        }

        StringBuilder sb3 = new StringBuilder();
        for (int i = 0; i < quantityOfItems.size(); i++) {
            if (i != quantityOfItems.size() - 1) {
                sb3.append(quantityOfItems.get(i)).append(",");
            } else {
                sb3.append(quantityOfItems.get(i));
            }
        }

        sb.append(",,").append(sb2.toString()).append(",,").append(sb3.toString());

        return sb.toString();
    }

    public static void saveOrdersToFile(ArrayList<Order> orders) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < orders.size(); i++) {
            sb.append(orders.get(i).toCSVString() + "\n");
        }
        Files.write(Paths.get("./files/order/orders.csv"), sb.toString().getBytes());
    }

    public static ArrayList<Order> getOrdersFromFile(ArrayList<Item> itemList, ArrayList<Restaurant> restaurantList, ArrayList<Customer> customerList, Cqueue<Rider> riderList) throws IOException {
        ArrayList<Order> orders = new ArrayList<>();

        //read orders.csv from file
        List<String> lines = Files.readAllLines(Paths.get("./files/order/orders.csv"));

        for (int i = 0; i < lines.size(); i++) {
            // split a line by comma
            String[] dataInFile = lines.get(i).split(",,");
            String code = dataInFile[0];
            String restaurantCode = dataInFile[1];
            String customerCode = dataInFile[2];
            String riderCode = dataInFile[3];

            String orderStatus = dataInFile[4];
            String pickupType = dataInFile[5];
            String[] items = dataInFile[6].split(",");

            ArrayList<Integer> quantityOfItems = new ArrayList<>();
            //store quantity of items
            String[] quantity = dataInFile[7].split(",");
            for (String s : quantity) {
                quantityOfItems.add(Integer.parseInt(s));
            }

            //get item codes and get items based on codes
            ArrayList<String> itemCodes = new ArrayList<>(Arrays.asList(items));
            ArrayList<Item> itemsToOrder = new ArrayList<>();
            for (int j = 0; j < itemCodes.size(); j++) {
                for (int k = 0; k < itemList.size(); k++) {
                    if (itemCodes.get(j).equals(itemList.get(k).getCode())) {
                        itemsToOrder.add(itemList.get(k));
                    }
                }
            }

            //get restaurant object based on restaurant code of order
            Restaurant restaurant = null;
            for (int j = 0; j < restaurantList.size(); j++) {
                if (restaurantList.get(j).getCode().equals(restaurantCode)) {
                    restaurant = restaurantList.get(j);
                    break;
                }
            }

            //get customer object based on customer code of order
            Customer customer = null;
            for (int j = 0; j < customerList.size(); j++) {
                if (customerList.get(j).getCode().equals(customerCode)) {
                    customer = customerList.get(j);
                    break;
                }
            }

//            System.out.println(restaurant.getCode());
//            System.out.println(customer.getCode());
//            System.out.println(rider.getCode());

            if (pickupType.equals("Delivery")) {
                Rider rider = null;
                for (int j = 0; j < riderList.size(); j++) {
                    if (riderList.get(j).getCode().equals(riderCode)) {
                        rider = riderList.get(j);
                        break;
                    }
                }

                Order order = new Order(restaurant, customer, rider, itemsToOrder, quantityOfItems, pickupType, orderStatus);
                orders.add(order);
            } else {
                Order order = new Order(restaurant, customer, itemsToOrder, quantityOfItems, pickupType, orderStatus);
                orders.add(order);
            }
        }
        return orders;
    }
}
