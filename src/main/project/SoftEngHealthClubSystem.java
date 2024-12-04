package main.project;

import main.project.modules.Guest;
import main.project.pages.HealthClubHomePage;

import javax.swing.*;

public class SoftEngHealthClubSystem {

    public Guest user;

    public SoftEngHealthClubSystem(Guest g) {

        user = g;
        try {UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");}
        catch (Exception e) {System.out.println("Look and Feel not set");}

        HealthClubHomePage homePage = new HealthClubHomePage(null, user);
        homePage.welcomeText.setText("Welcome " + user.getName());
    }
}