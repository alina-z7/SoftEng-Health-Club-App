package Entities;

import java.time.LocalDate;

public class Member extends Person {

    public Long id_number;
    public LocalDate creation_date;
    public LocalDate expiration_date;
    public LocalDate last_checked_in;

    public Member(){super();}
    public Member(String firstName, String lastName, String emailAddress, LocalDate birthDate, long phoneNumber) {
        super(firstName, lastName, emailAddress, birthDate, phoneNumber);
    }

    public void setIdNumber(long number) {id_number = number;}
    public void setCreationDate(LocalDate date) {creation_date = date;}
    public void updateLastCheckedIn() {last_checked_in = LocalDate.now();}
    public void updateExpirationDate(LocalDate date) {expiration_date = date;}

    public long getIDNumber() {return id_number;}
    public LocalDate getCreationDate(){return creation_date;}
    public LocalDate getExpirationDate(){return expiration_date;}
    public LocalDate getLastCheckedInDate(){return last_checked_in;}

    public String toString(){
        return "MEMBER: " + getName() + " -> {\n\t"
                + "Id Number: " + id_number + "\n\t"
                + "Membership Created on: " + creation_date.toString() + "\n\t"
                + "Membership Expires on: " + expiration_date.toString() + "\n\t"
                + "Last Checked in on: " + last_checked_in.toString()  + "\n\t"
                + "Email: " + id_number + "\n}";
    }
}