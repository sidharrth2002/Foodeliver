import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Item {
    private String code;
    private String name;
    private double price;
    private String description;
    private Restaurant restaurant;
    public static int count1, count2, count3;

    public Item() {}

    //constructor used if added by the user
    public Item(String name, double price, String description, Restaurant restaurant) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.restaurant = restaurant;

        // gets the customer ID and generates a Item Code automatically using the following format:
        // Item Code = (Restaurant ID)-(current number of items for restaurant)
        // e.g. r2-1, r3-2
        String restaurantCode = restaurant.getCode();
        switch(restaurantCode) {
            case "r1":
                count1++;
                code = restaurantCode + "-" + count1;
                break;
            case "r2":
                count2++;
                code = restaurantCode + "-" + count2;
                break;
            case "r3":
                count3++;
                code = restaurantCode + "-" + count3;
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
    //Name
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
        System.out.print(restaurant.getName() + " \t " + name + " \t " + description + " \t " + price );
        /*System.out.println("Item Name: " + name);
        System.out.println("Item Description: " + description);
        System.out.println("Item Price: " + price);
        System.out.println("Restaurant Serving this Item: " + restaurant.getName());*/
        System.out.print("\n-----------------------------------------------------------------------------------------------\n " );
    }

    //sorting functions----------
    //Alphabetically
    public static void sortAlphabetically(ArrayList<Item> itemsToSort) {
        if (itemsToSort.size() > 0) {
            //overrides the collections.sort by passing in a custom comparator to sort by particular field in an object
            Collections.sort(itemsToSort, new Comparator< Item >() {
                @Override
                public int compare(final Item item1, final Item item2) {
                    return item1.getName().compareTo(item2.getName());
                }
            });
        }

        //prints sorted items
        for (int i = 0; i < itemsToSort.size(); i++) {
            System.out.print("Item #" + i + ": ");
            itemsToSort.get(i).describe();
            System.out.println();
        }
    }

    //By Price
    public static void sortByPrice(ArrayList<Item> itemsToSort) {
        System.out.println("We get here");
        if (itemsToSort.size() > 0) {
            //overriding Collections.sort with another custom comparator
            Collections.sort(itemsToSort, new Comparator < Item > () {
                @Override
                public int compare(final Item item1, final Item item2) {
                    if (item1.getPrice() == item2.getPrice()) {
                        return 0;
                    }
                    return item1.getPrice() < item2.getPrice() ? -1 : 1;
                }
            });
        }

        for (int i = 0; i < itemsToSort.size(); i++) {
            System.out.print("Item #" + i + ": ");
            itemsToSort.get(i).describe();
            System.out.println();
        }
    }


    public String toCSVString() {
        return code + ",," + name + ",," + price + ",," + description;
    }

    // writing to file
    public static void saveItemsToFile(ArrayList<Item> items) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            sb.append(items.get(i).toCSVString() + "\n");
        }
        Files.write(Paths.get("./files/item/items.csv"), sb.toString().getBytes());
    }

    // Reading from file
    public static ArrayList<Item> getItemsFromFile(ArrayList<Restaurant> restaurants) throws IOException {
        ArrayList<Item> items = new ArrayList<>();

        // read items.csv into a list of lines.

        List<String> lines = Files.readAllLines(Paths.get("./files/item/items.csv"));
        for (int i = 0; i < lines.size(); i++) {
            // split a line by comma
            String[] dataInFile = lines.get(i).split(",,");
            String code = dataInFile[0];
            String name = dataInFile[1];
            double price = Double.parseDouble(dataInFile[2]);
            String description = dataInFile[3];
            String restaurantCode = code.substring(0,2);
            Restaurant restaurant = null;

            for (int j = 0; j < restaurants.size(); j++) {
                if (restaurants.get(j).getCode().equals(restaurantCode)) {
                    restaurant = restaurants.get(j);
                }
            }

            items.add(new Item(name, price, description, restaurant));
        }
        return items;
    }
}