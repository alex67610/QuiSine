/**
 * Created by alex on 02/11/2017.
 */

public class UserInformation {
    String FirstName;
    String LastName;
    Integer Age;
    String Address;
    String Biography;

    public UserInformation(String firstName, String lastName, Integer age, String address, String biography) {
        FirstName = firstName;
        LastName = lastName;
        Age = age;
        Address = address;
        Biography = biography;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public Integer getAge() {
        return Age;
    }

    public String getAddress() {
        return Address;
    }

    public String getBiography() {
        return Biography;
    }
}
