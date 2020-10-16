import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Rider extends Entity {
    private String phone;
    private static int count = 1;

    public Rider() {}
    public Rider(String name, String phone) {
        super(name);
        this.phone = phone;
        this.code = "d" + count++;
    }

    public String getPhone() {
        return phone;
    }

    public String toCSVString() {
        return code + ",," + name + ",," + phone;
    }

    //saving Rider data to file
    private static void saveRidersToFile(LinkedList<Rider> riders) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < riders.size(); i++) {
            sb.append(riders.get(i).toCSVString() + "\n");
        }
        Files.write(Paths.get("./files/rider/riders.csv"), sb.toString().getBytes());
    }

    public static LinkedList<Rider> getRidersFromFile() throws IOException{
        LinkedList<Rider> riders = new LinkedList<>();

        // read students.csv into a list of lines.
        List<String> lines = Files.readAllLines(Paths.get("./files/rider/riders.csv"));
        for (int i = 0; i < lines.size(); i++) {
            // split a line by comma
            String[] dataInFile = lines.get(i).split(",,");
            String code = dataInFile[0];
            String name = dataInFile[1];
            String phone = dataInFile[2];

            riders.add(new Rider(name, phone));
        }
        return riders;
    }

    public static void main(String[] args) {
        LinkedList<Rider> riders = new LinkedList<>();
        riders.add(new Rider("Ahmed","0113456789"));
        riders.add(new Rider("Mohamed", "0123456789"));
        riders.add(new Rider("Khaled", "0133456789"));

        try {
            saveRidersToFile(riders);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            LinkedList<Rider> ridersload = getRidersFromFile();
            System.out.println(ridersload.get(1).getName());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}