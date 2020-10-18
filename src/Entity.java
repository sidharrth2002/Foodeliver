import java.util.ArrayList;

//acts as the superclass for restaurant and customer
public class Entity implements Comparable<Entity> {
    protected String name;
    protected String address;
    protected String code;

    protected ArrayList<Order> orderHistory = new ArrayList<>();

    //both restaurant and customer have an orderHistory arraylist that they maintain separately
    //once a new order is made, it will be added to both of their histories
//    protected ArrayList<Order> orderHistory = new ArrayList<Order>();

    public Entity() {}

    //called by both Restaurant and Customer to load their information
    public Entity(String name, String address) {
        this.name = name;
        this.address = address;
    }

    //used by rider to load name
    public Entity(String name) {
        this.name = name;
    }

    //getter
    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public int compareTo(Entity entity) {
        if (entity.getOrderHistory().size() == this.getOrderHistory().size()) {
            return 0;
        } else if (this.getOrderHistory().size() < entity.getOrderHistory().size()) {
            return 1;
        } else {
            return -1;
        }
    }

    // used by the makeOrder function in Foodeliver to add a new order to the restaurant's and customer's order History
    public void addOrder (Order newOrder) {
        orderHistory.add(newOrder);
    }

    // order history arraylist
    public ArrayList<Order> getOrderHistory() {
        return orderHistory;
    }
}