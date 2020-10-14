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
    private String orderStatus = "Preparing";
    private String code;
    private String pickupType;

    //contains item objects chosen
    public ArrayList<Item> itemsToOrder;
    //contains the quantity of corresponding item object- both act as parallel arrays
    private ArrayList<Integer> quantityOfItems;
    private double totalPrice;
    private static int count = 1;

    public Order() {}

//    public Order(String restaurantName, String restaurantCode, String customerName, String customerCode, ArrayList<String> itemCodes, ArrayList<Integer> quantityOfItems, String pickupType, ArrayList<Item> itemList) {
//
//        this.code = "o" + count++ + "-" + restaurantCode + "-" + customerCode;
//    }

    public Order(Restaurant restaurant, Customer customer, ArrayList<Item> itemsToOrder, ArrayList<Integer> quantityOfItems, String pickupType) {
        this.restaurant = restaurant;
        this.customer = customer;
        this.itemsToOrder = itemsToOrder;
        this.quantityOfItems = quantityOfItems;
        this.totalPrice = calculateTotalPrice();

        this.code = "o" + count++ + "-" + restaurant.getCode() + "-" + customer.getCode();
    }

    public Order(Restaurant restaurant, Customer customer, ArrayList<Item> itemsToOrder, ArrayList<Integer> quantityOfItems, String pickupType, String orderStatus) {
        this.restaurant = restaurant;
        this.customer = customer;
        this.itemsToOrder = itemsToOrder;
        this.quantityOfItems = quantityOfItems;
        this.totalPrice = calculateTotalPrice();
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

    //setters
    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    // method which prints order details
    public void describe() {
        System.out.println("=======================================================================");
        System.out.println("Customer making the Order: " + customer.getName());
        System.out.println("Restaurant taking the Order: " + restaurant.getName());
        System.out.println("The total price: " + totalPrice);
        System.out.println("The status of this order is: " + orderStatus);
        System.out.println("The items are: ");
        System.out.println();
        // calling the item's describe method for each item in the order
        for (int i = 0; i < itemsToOrder.size(); i++) {
            itemsToOrder.get(i).describe();
            System.out.println("Quantity : " + quantityOfItems.get(i));
        }
        System.out.println("=======================================================================");
        System.out.println();
    }

    public String toCSVString() {
        StringBuilder sb = new StringBuilder();

        sb.append(code).append(",,").append(restaurant.getCode()).append(",,").append(customer.getCode()).append(",,").append(orderStatus).append(",,").append(pickupType);

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

    public static ArrayList<Order> getOrdersFromFile(ArrayList<Item> itemList, ArrayList<Restaurant> restaurantList, ArrayList<Customer> customerList) throws IOException {
        ArrayList<Order> orders = new ArrayList<>();

        // read students.csv into a list of lines.
        List<String> lines = Files.readAllLines(Paths.get("./files/order/orders.csv"));
        for (int i = 0; i < lines.size(); i++) {
            // split a line by comma
            String[] dataInFile = lines.get(i).split(",,");
            String code = dataInFile[0];
            String restaurantCode = dataInFile[1];
            String customerCode = dataInFile[2];
            String orderStatus = dataInFile[3];
            String pickupType = dataInFile[4];
            String[] items = dataInFile[5].split(",");

            ArrayList<Integer> quantityOfItems = new ArrayList<>();
            //store quantity of items
            String[] quantity = dataInFile[6].split(",");
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
            Order order = new Order(restaurant, customer, itemsToOrder, quantityOfItems, pickupType, orderStatus);
            orders.add(order);
        }
        return orders;
    }

    //based on the item code given, will find and return Item object
//    public static Item getItemFromItemCode(String itemCode, ArrayList<Item> itemList) {
//        for (int i = 0; i < itemList.size(); i++) { ;
////            System.out.println(itemList.get(i).getCode());
////            System.out.println(Item.count1);
////            System.out.println(Item.count2);
////            System.out.println(Item.count3);
//            //since everytime a new item is created, the static count increases, have to decrease to make sure it doesn't mess about codes
////            if (itemList.get(i).getCode().substring(0, 2).equals("r1")) {
////                Item.count1 -= 1;
////            } else if (itemList.get(i).getCode().substring(0,2).equals("r2")) {
////                Item.count2 -= 1;
////            } else {
////                Item.count3 -= 1;
////            }
//
////            System.out.println(item.getCode());
//            if (itemList.get(i).getCode().equals(itemCode)) {
//                return itemList.get(i);
//            }
//        }
//        return null;
//    }

    public static void main(String[] args) throws IOException {

        ArrayList<Item> itemsList = Item.getItemsFromFile();

        for (Item item : itemsList) {
            item.describe();
        }

        System.out.println(Item.count1);
        System.out.println(Item.count2);
        System.out.println(Item.count3);

//        for (Item item : itemsList2) {
//            item.describe();
//        }
//
//        System.out.println(Item.count1);
//        System.out.println(Item.count2);
//        System.out.println(Item.count3);

//        try {
////            Item.count1 = Item.count2 = Item.count3 = 0;
//            ArrayList<Order> orders2 = getOrdersFromFile(itemsList);
//            System.out.println(orders2.get(1).quantityOfItems);
//            System.out.println(orders2.get(2).itemsToOrder.get(1));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println(Item.count1);
//        System.out.println(Item.count2);
//        System.out.println(Item.count3);

//        System.out.println("Count after fetching from file");

//
//        ArrayList<Order> orders = new ArrayList<>();
//        ArrayList<String> itemsToOrder = new ArrayList<>(Arrays.asList("r3-1", "r2-1", "r3-2"));
//        ArrayList<Integer> quantityOfItems = new ArrayList<>(Arrays.asList(1, 3, 3));

//        orders.add(new Order("KFC", "r1", "Sid", "c1", itemsToOrder, quantityOfItems, "Delivery", itemsList));
//        orders.add(new Order("McD", "r1", "Sid", "c1", itemsToOrder, quantityOfItems, "Takeaway",itemsList));
//        orders.add(new Order("Burger King", "r1", "Sid", "c1", itemsToOrder, quantityOfItems, "Delivery", itemsList));

//        System.out.println(Item.count1);
//        System.out.println(Item.count2);
//        System.out.println(Item.count3);
//
//        //
//
//        try {
//            saveOrdersToFile(orders);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println("final");
//        System.out.println(Item.count1);
//        System.out.println(Item.count2);
//        System.out.println(Item.count3);


        //
//        System.out.println("final");

    }
}
