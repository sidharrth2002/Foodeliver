import java.util.ArrayList;

//acts as the superclass for restaurant and customer
public class Entity {
    protected String name;
    protected String address;

    //both restaurant and customer have an orderHistory arraylist that they maintain separately
    //once a new order is made, it will be added to both of their histories
//    protected ArrayList<Order> orderHistory = new ArrayList<Order>();

    public Entity() {}

    //called by both Restaurant and Customer to load their information
    public Entity(String name, String address) {
        this.name = name;
        this.address = address;
    }

    //getter
    public String getName() {
        return name;
    }

    //used by the makeOrder function in Foodeliver to add a new order to the restaurant's and customer's order History
//    public void addOrder (Order newOrder) {
//        orderHistory.add(newOrder);
//    }

    // order history arraylist
//    public ArrayList<Order> getOrderHistory() {
//        return orderHistory;
//    }
}