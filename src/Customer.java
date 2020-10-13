import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Customer extends Entity {
    String code;
    private String phone;
    private static int count = 1;

    //static queue to assign riders

    public Customer() {}

    public Customer(String name, String address, String phone) {
        super(name, address);
        this.phone = phone;
        this.code = "c" + count++;
    }

    //not needed for stage 1 but maybe later
    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getCode() {
        return code;
    }

    public String toCSVString() {
        return name + ",," + address + ",," + phone;
    }

    private static void saveCustomersToFile(ArrayList<Customer> customers) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < customers.size(); i++) {
            sb.append(customers.get(i).toCSVString() + "\n");
        }
        Files.write(Paths.get("./files/customer/customers.csv"), sb.toString().getBytes());
    }

    public static ArrayList<Customer> getCustomersFromFile() throws IOException {
        ArrayList<Customer> customers = new ArrayList<>();

        // read students.csv into a list of lines.
        List<String> lines = Files.readAllLines(Paths.get("./files/customer/customers.csv"));
        for (int i = 0; i < lines.size(); i++) {
            // split a line by comma
            String[] dataInFile = lines.get(i).split(",,");

            String name = dataInFile[0];
            String address = dataInFile[1];
            String phone = dataInFile[2];

            customers.add(new Customer(name, address, phone));
        }
        return customers;
    }

    public static void main(String[] args) {
        ArrayList<Customer> customers = new ArrayList<>();
        customers.add(new Customer("Sid", "2, Jalan Villa", "013456789"));
        customers.add(new Customer("Ahmed", "4, Jalan Villa", "435345"));
        customers.add(new Customer("John", "5, Jalan Villa", "435455676"));

        try {
            saveCustomersToFile(customers);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            ArrayList<Customer> customers2 = getCustomersFromFile();
            System.out.println(customers2.get(1).getAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
