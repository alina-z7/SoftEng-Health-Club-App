import Pages.LoginPage;

import Entities.Guest;
import javax.swing.*;

public class LogIn {
    public Guest logIn() {
        try {UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");}
        catch (Exception e) {System.out.println("Look and Feel not set");}

        LoginPage loginPage = new LoginPage(null);
        Guest guest = loginPage.guest;

        if(guest != null) {
            System.out.println("<Successful login>\n\t" +
                    guest.getName() +
                    "\n\tID: " + guest.id_number +
                    "\n\tPassword: " + guest.password +
                    "\n\tStatus: " + guest.guest_status);
        }else{
            System.out.println("Authentication Cancelled");
        }
        return guest;
    }
}