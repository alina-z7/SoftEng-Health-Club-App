package Entities;

import java.time.LocalDate;

public class Person {
    public String first_name;
    public String last_name;
    public String email_address;
    public LocalDate date_of_birth;
    public long phone_number;

    public Person(){}
    public Person(String firstName, String lastName, String emailAddress, LocalDate birthDate, long phoneNumber) {
        first_name = firstName.toLowerCase();
        last_name = lastName.toLowerCase();
        email_address = emailAddress.toLowerCase();
        date_of_birth = birthDate;
        phone_number = phoneNumber;
    }

    public String getFirstName(){return first_name;}
    public String getLastName(){return last_name;}
    public String getName(){return first_name.toUpperCase().charAt(0)+first_name.substring(1) + " " + last_name.toUpperCase().charAt(0)+last_name.substring(1);}
    public String getEmailAddress(){return email_address;}
    public LocalDate getBirthDate(){return date_of_birth;}
    public long getPhoneNumber(){return phone_number;}

    public String toString(){
        return getName() + " -> {\n\t"
                + date_of_birth.toString() + "\n\t"
                + "Email: " + email_address + "\n\t"
                + "Phone Number" + phone_number + "\n}";
    }
}