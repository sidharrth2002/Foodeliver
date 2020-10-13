import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Rider {
    private String code;
    private String name;
    private String phone;
    private static int count = 1;

    public Rider() {}
    public Rider(String name, String phone) {
        this.name = name;
        this.phone = phone;
        this.code = "d" + count++;
    }

    public String toCSVString() {
        return code + ",," + name + ",," + phone;
    }

    //saving Rider data to file
    private static void saveRidersToFile(ArrayList<Rider> riders) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < riders.size(); i++) {
            sb.append(riders.get(i).toCSVString() + "\n");
        }
        Files.write(Paths.get("./files/rider/riders.csv"), sb.toString().getBytes());
    }

    public static ArrayList<Restaurant> getRidersFromFile() throws IOException{
        ArrayList<Restaurant> riders = new ArrayList<>();

        // read students.csv into a list of lines.
        List<String> lines = Files.readAllLines(Paths.get("./files/rider/riders.csv"));
        for (int i = 0; i < lines.size(); i++) {
            // split a line by comma
            String[] dataInFile = lines.get(i).split(",,");
            String code = dataInFile[0];
            String name = dataInFile[1];
            String phone = dataInFile[2];

            riders.add(new Restaurant(name, phone));
        }
        return riders;
    }
}
