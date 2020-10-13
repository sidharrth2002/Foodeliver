import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Order {
    private String code;
    private String restaurantName;
    private String restaurantCode;
    private String customerName;
    private String customerCode;
    private String orderStatus = "Preparing";
    //contains the item objects chosen
    private ArrayList<String> itemsToOrder;
    //contains the quantity of corresponding item object- both act as parallel arrays
    private ArrayList<Integer> quantityOfItems;
    private double totalPrice;

    private static int count = 1;

    public Order() {}

    public Order(String restaurantName, String restaurantCode, String customerName, String customerCode, ArrayList<String> itemsToOrder, ArrayList<Integer> quantityOfItems) {
        this.restaurantName = restaurantName;
        this.restaurantCode = restaurantCode;
        this.customerName = customerName;
        this.customerCode = customerCode;

        this.itemsToOrder = new ArrayList<>(itemsToOrder);
        this.quantityOfItems = new ArrayList<>(quantityOfItems);
//        this.totalPrice = calculateTotalPrice();

        this.code = "o" + count++ + "-" + restaurantCode + "-" + customerCode;
    }

    //uses item price and quantity arraylists to calculate the total price of the order
//    private double calculateTotalPrice() {
//        double total = 0;
//        for (int i = 0; i < itemsToOrder.size(); i++) {
//            total += (itemsToOrder.get(i).getPrice() * quantityOfItems.get(i));
//        }
//        return total;
//    }

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
        System.out.println("Customer making the Order: " + customerName);
        System.out.println("Restaurant taking the Order: " + restaurantName);
        System.out.println("The total price: " + totalPrice);
        System.out.println("The status of this order is: " + orderStatus);
        System.out.println("The items are: ");
        System.out.println();
        // calling the item's describe method for each item in the order
        for (int i = 0; i < itemsToOrder.size(); i++) {
//            itemsToOrder.get(i).describe();
            System.out.println("Quantity : " + quantityOfItems.get(i));
        }
        System.out.println("=======================================================================");
        System.out.println();
    }

    public String toCSVString() {
        StringBuilder sb = new StringBuilder();

        sb.append(code).append(",,").append(restaurantName).append(",,").append(customerName);

        StringBuilder sb2 = new StringBuilder();
        for (int i = 0; i < itemsToOrder.size(); i++) {
            if (i != itemsToOrder.size() - 1) {
                sb2.append(itemsToOrder.get(i)).append(",");
            } else {
                sb2.append(itemsToOrder.get(i));
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

    public static ArrayList<Order> getOrdersFromFile() throws IOException {
        ArrayList<Order> orders = new ArrayList<>();

        // read students.csv into a list of lines.
        List<String> lines = Files.readAllLines(Paths.get("./files/order/orders.csv"));
        for (int i = 0; i < lines.size(); i++) {
            // split a line by comma
            String[] dataInFile = lines.get(i).split(",,");
            String code = dataInFile[0];
            String restaurantName = dataInFile[1];
            String customerName = dataInFile[2];

            String[] items = dataInFile[3].split(",");
            ArrayList<String> itemsToOrder = new ArrayList<>(Arrays.asList(items));

//            for (int i = 0; i < items.length; i++) {
//                itemsToOrder.add(items[i]);
//            }

            ArrayList<Integer> quantityOfItems = new ArrayList<>();
            String[] quantity = dataInFile[4].split(",");
            for (String s : quantity) {
                quantityOfItems.add(Integer.parseInt(s));
            }

//            for (int i = 0; i < quantity.length; i++) {
//                quantityOfItems.add(Integer.parseInt(quantity[i]));
//            }

            Order order = new Order(restaurantName, code.substring(3,5), customerName, code.substring(6,8), itemsToOrder, quantityOfItems);
            orders.add(order);
            }
        return orders;
    }

    public static void main(String[] args) {
        ArrayList<Order> orders = new ArrayList<>();

        ArrayList<String> itemsToOrder = new ArrayList<>(Arrays.asList("r1-1", "r1-2", "r1-3"));
        ArrayList<Integer> quantityOfItems = new ArrayList<>(Arrays.asList(1, 2, 3));
        orders.add(new Order("KFC", "r1", "Sid", "c1", itemsToOrder, quantityOfItems));
        orders.add(new Order("McD", "r1", "Sid", "c1", itemsToOrder, quantityOfItems));
        orders.add(new Order("Burger King", "r1", "Sid", "c1", itemsToOrder, quantityOfItems));

        try {
            saveOrdersToFile(orders);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            ArrayList<Order> orders2 = getOrdersFromFile();
            System.out.println(orders2.get(2).restaurantName);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
