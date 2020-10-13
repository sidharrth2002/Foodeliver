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

}
