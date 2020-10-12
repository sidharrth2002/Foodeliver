public class Item {
    private String code;
    private String name;
    private double price;
    private String description;
    private Restaurant restaurant;
    private static int count1, count2, count3;

    public Item() {}

    public Item(String name, double price, String description, Restaurant restaurant) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.restaurant = restaurant;

        // gets the customer ID and generates a Item Code automatically using the following format:
        // Item Code = (Restaurant ID)-(current number of items for restaurant)
        // e.g. r2-1, r3-2
        String restaurantID = restaurant.getCode();
        switch(restaurantID) {
            case "r1":
                count1++;
                code = restaurantID + "-" + count1;
                break;
            case "r2":
                count2++;
                code = restaurantID + "-" + count2;
                break;
            case "r3":
                count3++;
                code = restaurantID + "-" + count3;
                break;
        }
    }

    //getters
    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public double getPrice() {
        return price;
    }

    //setters
    //name
    public void setName(String name) {
        this.name = name;
    }
    //Description
    public void setDescription(String description) {
        this.description = description;
    }
    // Price
    public void setPrice(double price) {
        this.price = price;
    }

    // Function called whenever an item needs to be described
    // prints out the fields in a format
    public void describe() {
        System.out.println("----------------------------------" );
        System.out.println("Item Name: " + name);
        System.out.println("Item Description: " + description);
        System.out.println("Item Price: " + price);
        System.out.println("Restaurant Serving this Item: " + restaurant.getName());
        System.out.println("----------------------------------" );
    }

    public String toCSVString() {
        return code + " " + name + " " + price + " " + description;
    }
}