package services;

public class User {
    public String name;
    public String lastName;
    public String area;
    public String phone;

    public User(String first, String last, String area, String phone) {
        this.name = first;
        this.lastName = last;
        this.area = area;
        this.phone = phone;
    }
}
