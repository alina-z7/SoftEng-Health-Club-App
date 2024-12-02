package Pages;

import Entities.Member;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class CreateForm extends JDialog implements jdbcValues{
    private JPanel createPanel;
    private JTextField tfFirstName;
    private JTextField tfLastName;
    private JTextField tfEmail;
    private JTextField tfPhoneNumber;
    private JTextField bdMonth;
    private JTextField bdDay;
    private JTextField bdYear;
    private JButton createButton;
    private JTextField MembershipDuration;
    private JButton cancelButton;

    public CreateForm(JFrame parent) {
        super(parent);
        setTitle("Create Member");
        setContentPane(createPanel);
        setMinimumSize(new Dimension(600,500));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long newMemberId = -1;
                try {
                    String fn = String.valueOf(tfFirstName.getText());
                    String ln = String.valueOf(tfLastName.getText());
                    String em = String.valueOf(tfEmail.getText());
                    Long pn = Long.parseLong(tfPhoneNumber.getText());
                    int md = Integer.parseInt(String.valueOf(MembershipDuration.getText()));
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-M-d");
                    LocalDate bd = LocalDate.parse(Integer.parseInt(bdYear.getText()) + "-" + Integer.parseInt(bdMonth.getText()) + "-" + Integer.parseInt(bdDay.getText()),df);

                    Member m = new Member(fn, ln, em, bd, pn);
                    newMemberId = createMember(m, md);

                    if(newMemberId == -1) throw new Exception();

                    dispose();
                    JOptionPane.showMessageDialog(CreateForm.this, "New Gym Member\n\tName: " + m.getName() + "\n\tID: " + m.id_number, "Member has been added", JOptionPane.INFORMATION_MESSAGE);
                }catch (Exception o) {
                    JOptionPane.showMessageDialog(CreateForm.this, "Input Is INVALID", "Try Again", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {dispose();}
        });
        setVisible(true);
    }

    public long createMember(Member m, int md) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            switch (checkIfMemberExists(m, conn)) {
                case 1 -> throw new Exception("Email Already Exists In System");
                case 2 -> throw new Exception("Phone Number Already Exists In System");
            }
            m.id_number = createUniqueID(conn);
            String query = "INSERT INTO members " +
                    "(id_number, first_name, last_name, birth_date, email_address, phone_number, creation_date, expiration_date, last_checked_in_date) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setLong(1, m.id_number);
            preparedStatement.setString(2, m.first_name.substring(0,1).toUpperCase() + m.first_name.substring(1).toLowerCase());
            preparedStatement.setString(3, m.last_name.substring(0,1).toUpperCase() + m.last_name.substring(1).toLowerCase());
            preparedStatement.setDate(4, Date.valueOf(m.date_of_birth));
            preparedStatement.setString(5, m.email_address.toLowerCase());
            preparedStatement.setLong(6, m.phone_number);
            preparedStatement.setDate(7, Date.valueOf(LocalDate.now()));
            preparedStatement.setDate(8, Date.valueOf(LocalDate.now().plusMonths(md)));
            preparedStatement.setDate(9, Date.valueOf(LocalDate.now()));
            preparedStatement.executeUpdate();
            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(CreateForm.this, e, "Try Again", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
        return m.id_number;
    }

    public int checkIfMemberExists(Member member, Connection conn) {
        try {
            String query = "SELECT * FROM members WHERE email_address=? OR phone_number=?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, member.email_address);
            preparedStatement.setLong(2, member.phone_number);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()) {return Objects.equals(rs.getString(5), member.email_address) ? 1 : 2;}
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Long createUniqueID(Connection conn) {
        long id = 10000000000L;
        try{
            while(true){
                String query = "SELECT * FROM members WHERE id_number=?";
                PreparedStatement preparedStatement = conn.prepareStatement(query);
                preparedStatement.setLong(1, id);
                ResultSet rs = preparedStatement.executeQuery();
                if(!rs.next()) break;
                else id = (long)(Math.random() * (99999999999L - 10000000000L)) + 10000000000L;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }
}
