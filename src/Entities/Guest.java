package Entities;
import java.time.LocalDate;

public class Guest extends Person {

    public long id_number;
    public Status guest_status;
    public String password;

    public Guest(){
        super();
    }
    public Guest(String firstName, String lastName, String emailAddress, LocalDate birthDate, long phoneNumber) {
        super(firstName, lastName, emailAddress, birthDate, phoneNumber);
    }

    public void setIdNumber(long number) {id_number = number;}
    public void setPassword(String p) {password = p;}
    public void setEnum(Status s) {guest_status = s;}
    public boolean setEnum(String s) {
        try{
            guest_status = Status.valueOf(s);
        }catch (Exception e) {
            return false;
        }
        return true;
    }

    public long getIDNumber() {return id_number;}
    public Status getStatus() {return guest_status;}

    public String toString(){
        return guest_status + ": " + getName() + " -> {\n\t"
                + "ID Number: " + id_number + "\n\t"
                + "Email Address: " + email_address + "\n}";
    }
}