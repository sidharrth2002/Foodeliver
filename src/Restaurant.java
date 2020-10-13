import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Restaurant extends Entity {
    private String code;
    private static int count = 1;

    public Restaurant() {}

    public Restaurant(String name, String address) {
        super(name, address);
        this.code = "r" + count++;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

//    public void addItem(String name, double price, String description) {
//        Item newItem = new Item(name, price, description, this);
//        Foodeliver.FoodItems.add(newItem);
//    }
//
//    public void setOrderStatus(int orderIndex, String newStatus) {
//        orderHistory.get(orderIndex).setOrderStatus(newStatus);
//    }

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
    private static void saveItemsToFile(ArrayList<Item> items) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            sb.append(items.get(i).toCSVString() + "\n");
        }
        Files.write(Paths.get("./files/item/items.csv"), sb.toString().getBytes());
    }

    public static ArrayList<Restaurant> getRestaurantsFromFile() throws IOException{
        ArrayList<Restaurant> restaurants = new ArrayList<>();

        // read students.csv into a list of lines.
        List<String> lines = Files.readAllLines(Paths.get("./files/restaurant/restaurants.csv"));
        for (int i = 0; i < lines.size(); i++) {
            // split a line by comma
            String[] dataInFile = lines.get(i).split(",,");
            String code = dataInFile[0];
            String name = dataInFile[1];
            String address = dataInFile[2];

            restaurants.add(new Restaurant(name, address));
        }
        return restaurants;
    }

    public static void main(String[] args) {
        ArrayList<Restaurant> restaurants = new ArrayList<>();
        restaurants.add(new Restaurant("Burger King", "2, Persiaran Multimedia"));
        restaurants.add(new Restaurant("Pizza Hut", "3, Jalan Villa"));
        restaurants.add(new Restaurant("Glaze Eatery", "4, Persiaran Apec"));

        try {
            saveRestaurantsToFile(restaurants);
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            ArrayList<Restaurant> restaurants2 = getRestaurantsFromFile();
            System.out.println(restaurants2.get(1).address);

        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item("Item1", 35.50, "Tasty food", restaurants.get(0).getCode(), restaurants.get(0).getName()));
        items.add(new Item("Item2", 35.60, "Tasty food", restaurants.get(1).getCode(), restaurants.get(1).getName()));
        items.add(new Item("Item3", 35.50, "Tasty food", restaurants.get(1).getCode(),restaurants.get(1).getName()));

        try {
            saveItemsToFile(items);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<Item> items2 = new ArrayList<>();

        try {
            items2 = Item.getItemsFromFile();
            System.out.println(items.get(2).getRestaurantName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
