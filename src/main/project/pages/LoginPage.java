package main.project.pages;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import main.project.modules.Guest;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginPage extends JDialog implements jdbcValues {
    private JFormattedTextField ftfUserId;
    private JPasswordField pfPassword;
    private JButton loginButton;
    private JButton cancelButton;
    private JPanel loginPanel;

    public Guest guest;

    public LoginPage(JFrame parent){
        super(parent);
        setTitle("Login");
        setContentPane(loginPanel);
        setMinimumSize(new Dimension(450,474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    long id = Long.parseLong(ftfUserId.getText());
                    String password = String.valueOf(pfPassword.getPassword());

                    guest = getAuthenticatedGuest(id, password);

                    if(guest != null) dispose();
                    else throw new Exception();

                } catch (Exception i) {JOptionPane.showMessageDialog(LoginPage.this, "User ID and/or Password is INVALID", "Try Again", JOptionPane.ERROR_MESSAGE);}
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        setVisible(true);
    }



    public Guest getAuthenticatedGuest(long id, String password){
        Guest guest = null;
        try{
            Connection conn = DriverManager.getConnection(jdbcValues.DB_URL, jdbcValues.DB_USERNAME, jdbcValues.DB_PASSWORD);
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM employees WHERE user_id=? AND password=?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setLong(1, id);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                guest = new Guest();
                guest.id_number = resultSet.getLong("user_id");
                guest.password = resultSet.getString("password");
                guest.first_name = resultSet.getString("first_name");
                guest.last_name = resultSet.getString("last_name");
                guest.setEnum(resultSet.getString("status"));
            }
            preparedStatement.close();
            stmt.close();
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        return guest;
    }

    public AbstractButton getLoginButton() {
        return cancelButton;
    }

    public JPasswordField getPasswordField() {
        return pfPassword;
    }

    public JFormattedTextField getFtfUserId() {
        return ftfUserId;

    }

    public Object getCancelButton() {
        return cancelButton;
    }
}


