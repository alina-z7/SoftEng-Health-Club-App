import Entities.*;

public class Main {
    public static void main(String[] args) {
        LogIn login = new LogIn();
        Guest guest = login.logIn();
        SoftEngHealthClubSystem softEngHealthClubSystem = new SoftEngHealthClubSystem(guest);
    }
}
