package main.project;

import main.project.modules.Guest;

public class Main {
    public static void main(String[] args) {
        LogIn login = new LogIn();
        Guest guest = login.logIn();
        SoftEngHealthClubSystem softEngHealthClubSystem = new SoftEngHealthClubSystem(guest);
    }
}
