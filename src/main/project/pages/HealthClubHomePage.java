package main.project.pages;

import main.project.modules.Guest;
import main.project.modules.Member;
import main.project.modules.Status;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.text.SimpleDateFormat;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;
import java.util.Vector;

public class HealthClubHomePage extends JFrame implements jdbcValues {

    final Vector<String> cols = new Vector<>(Arrays.asList("ID", "First Name", "Last Name", "Birthday", "Email", "Phone Number"));
    private Guest user;

    private JTable memberTable;
    private JPanel systemPane;
    private JButton createButton;
    private JTextField tfID;
    private JTextField tfFirstName;
    private JTextField tfLastName;
    private JTextField tfEmail;
    private JTextField tfPhoneNumber;
    private JButton searchButton;
    public JLabel welcomeText;
    private JButton refreshButton;
    private JButton deleteButton;
    private JButton inactiveMemberListButton;
    private JTextField miFirstName;
    private JTextField miLastName;
    private JTextField miEmail;
    private JTextField miPhoneNumber;
    private JLabel miID;
    private JLabel miCreationDate;
    private JLabel miExpirationDate;
    private JLabel miBirthDate;
    private JButton renewButton;
    private JButton updateButton;
    private JLabel miLastCheckedIn;

    public HealthClubHomePage(JFrame parent , Guest u) {
        super("Health Club Home Page");
        user = u;
        setContentPane(systemPane);
        setMinimumSize(new Dimension(1300,800));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initializeTable();
        setLocationRelativeTo(parent);

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateForm cf = new CreateForm(HealthClubHomePage.this);
                initializeTable();
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Member m = new Member();
                try {
                    m.id_number = Objects.equals(tfID.getText(), "") || tfID.getText().contains("-")? -1 : Long.parseLong(tfID.getText());
                    m.first_name = String.valueOf(tfFirstName.getText());
                    m.last_name = String.valueOf(tfLastName.getText());
                    m.email_address = String.valueOf(tfEmail.getText());
                    m.phone_number = Objects.equals(tfPhoneNumber.getText(), "") || tfPhoneNumber.getText().contains("-") ? -1 : Long.parseLong(tfPhoneNumber.getText());
                    searchTable(m);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(HealthClubHomePage.this, "INVALID Search Input", "Try Again", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initializeTable();
            }
        });
        memberTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = memberTable.getSelectedRow();
                DefaultTableModel model = (DefaultTableModel) memberTable.getModel();
                miID.setText(model.getValueAt(row, 0).toString());
                miFirstName.setText(model.getValueAt(row,1).toString());
                miLastName.setText(model.getValueAt(row,2).toString());
                miBirthDate.setText(new SimpleDateFormat("yyyy-M-d").format(model.getValueAt(row,3)));
                miEmail.setText(model.getValueAt(row,4).toString());
                miPhoneNumber.setText(model.getValueAt(row,5).toString());
                Member member = getMember((Long)model.getValueAt(row,0));
                miCreationDate.setText(member.creation_date.getYear() + "-" + member.creation_date.getMonthValue() + "-" + member.creation_date.getDayOfMonth());
                miExpirationDate.setText(member.expiration_date.getYear() + "-" + member.expiration_date.getMonthValue() + "-" + member.expiration_date.getDayOfMonth());
                miLastCheckedIn.setText(member.last_checked_in.getYear() + "-" + member.last_checked_in.getMonthValue() + "-" + member.last_checked_in.getDayOfMonth());
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    if (miID.getText().equals("N/A")) {
                        JOptionPane.showMessageDialog(HealthClubHomePage.this, "No Member Was Selected", "Membership Update", JOptionPane.WARNING_MESSAGE);
                    }else{
                        Member member = getMember(Long.parseLong(miID.getText()));
                        member.first_name = miFirstName.getText();
                        member.last_name = miLastName.getText();
                        member.email_address = miEmail.getText();
                        member.phone_number = Long.parseLong(miPhoneNumber.getText());
                        boolean updated = updateMembership(member);
                        if(updated) {
                            JOptionPane.showMessageDialog(HealthClubHomePage.this, "Update was Successful", "Membership Update", JOptionPane.INFORMATION_MESSAGE);
                        }else{
                            JOptionPane.showMessageDialog(HealthClubHomePage.this, "Update Was Unsuccessful", "Membership Update", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(HealthClubHomePage.this, "Invalid Input", "Membership Update", JOptionPane.WARNING_MESSAGE);
                }
                initializeTable();
            }
        });
        renewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (miID.getText().equals("N/A")) {
                        JOptionPane.showMessageDialog(HealthClubHomePage.this, "No Member Was Selected", "Membership Renewal", JOptionPane.WARNING_MESSAGE);
                    } else {
                        int md = Integer.parseInt(JOptionPane.showInputDialog(HealthClubHomePage.this,
                                "How many more months does " + miFirstName.getText() + " " + miLastName.getText() + " want added onto their membership",
                                "Membership Renewal", JOptionPane.QUESTION_MESSAGE));
                        boolean renewed = renewMembership(Long.parseLong(miID.getText()), md);
                        if(renewed) {
                            miExpirationDate.setText(getMember(Long.parseLong(miID.getText())).expiration_date.getYear() + "-" + getMember(Long.parseLong(miID.getText())).expiration_date.getMonthValue() + "-" + getMember(Long.parseLong(miID.getText())).expiration_date.getDayOfMonth());
                            miLastCheckedIn.setText(getMember(Long.parseLong(miID.getText())).last_checked_in.getYear() + "-" + getMember(Long.parseLong(miID.getText())).last_checked_in.getMonthValue() + "-" + getMember(Long.parseLong(miID.getText())).last_checked_in.getDayOfMonth());
                            JOptionPane.showMessageDialog(HealthClubHomePage.this, "Successfully Renewed " + miFirstName.getText() + " " + miLastName.getText() +
                                            "\nOld Expiration Date: " + miExpirationDate.getText() + "\nNew Expiration Date: " + getMember(Long.parseLong(miID.getText())).expiration_date,
                                    "Membership Renewal", JOptionPane.INFORMATION_MESSAGE);
                        }else{
                            JOptionPane.showMessageDialog(HealthClubHomePage.this, "Renewal Was Unsuccessful", "Membership Renewal", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                }catch (Exception ex){
                    JOptionPane.showMessageDialog(HealthClubHomePage.this, "Invalid Input", "Membership Renewal", JOptionPane.WARNING_MESSAGE);
                }
                initializeTable();
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(user.getStatus() != Status.CLUB_MANAGER) {
                    JOptionPane.showMessageDialog(HealthClubHomePage.this, "You do not have permission\nto delete members", "Unauthorized Access", JOptionPane.ERROR_MESSAGE);
                }else{
                    int row = memberTable.getSelectedRow();
                    DefaultTableModel model = (DefaultTableModel) memberTable.getModel();
                    Member member = getMember((Long)model.getValueAt(row,0));
                    int result = JOptionPane.showConfirmDialog(HealthClubHomePage.this, "You sure you want to delete this Member?\n" +
                            "\n\tID: " + member.id_number + "\n\tName: " + member.getName() +
                            "\n\tBirthday: " + member.date_of_birth, "Confirm Deletion", JOptionPane.YES_NO_OPTION);

                    switch (result) {
                        case JOptionPane.NO_OPTION -> {
                            JOptionPane.showMessageDialog(HealthClubHomePage.this, "Deletion was unsuccessful", "Member Deletion", JOptionPane.INFORMATION_MESSAGE);
                        }
                        case JOptionPane.YES_OPTION -> {
                            boolean deleted = deleteMember(member.id_number);
                            JOptionPane.showMessageDialog(HealthClubHomePage.this,
                                    "The deletion of this member was " + (deleted ? "successful" : "unsuccessful") + "\n\tID: " + member.id_number + "\n\tName: " + member.getName(),
                                    "Member Deletion", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
                initializeTable();
            }
        });
        inactiveMemberListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(user.getStatus() != Status.CLUB_MANAGER){
                    JOptionPane.showMessageDialog(HealthClubHomePage.this, "You do not have permission\nto access the inactive member list", "Unauthorized Access", JOptionPane.ERROR_MESSAGE);
                }else {
                    InactiveMemberList iml = new InactiveMemberList(HealthClubHomePage.this);
                    iml.welcome.setText("Welcome Club Manager " + user.getName());
                }
            }
        });
        setVisible(true);
    }

    public void initializeTable(){
        Vector<Vector<Object>> data = new Vector<>();
        try{
            Connection conn = DriverManager.getConnection(jdbcValues.DB_URL, jdbcValues.DB_USERNAME, jdbcValues.DB_PASSWORD);
            Statement stmt = conn.createStatement();
            String query = "SELECT id_number, first_name, last_name, birth_date, email_address, phone_number FROM members";
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                Vector<Object> tmp = new Vector<>();
                tmp.add(rs.getLong(1));
                tmp.add(rs.getString(2));
                tmp.add(rs.getString(3));
                tmp.add(rs.getDate(4));
                tmp.add(rs.getString(5));
                tmp.add(rs.getLong(6));
                data.add(tmp);
            }
            stmt.close();
            conn.close();
        } catch (Exception e) {e.printStackTrace();}
        memberTable.setRowSelectionAllowed(true);
        DefaultTableModel dtm = new DefaultTableModel(data, cols){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        memberTable.setModel(dtm);
        memberTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        memberTable.getTableHeader().setReorderingAllowed(false);
        memberTable.revalidate();
        memberTable.repaint();
        JScrollPane scrollPane = (JScrollPane) memberTable.getParent().getParent();
        scrollPane.revalidate();
        scrollPane.repaint();
    }

    public void searchTable(Member member) {
        Vector<Vector<Object>> data = new Vector<>();
        try{
            Connection conn = DriverManager.getConnection(jdbcValues.DB_URL, jdbcValues.DB_USERNAME, jdbcValues.DB_PASSWORD);
            String query = "SELECT id_number, first_name, last_name, birth_date, email_address, phone_number " +
                    "FROM members " +
                    "WHERE id_number LIKE ? " +
                    "AND first_name LIKE ? " +
                    "AND last_name LIKE ? " +
                    "AND email_address LIKE ? " +
                    "AND phone_number LIKE ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, member.id_number == -1 ? "%%" : "%" + member.id_number + "%");
            preparedStatement.setString(2, "%" + member.first_name + "%");
            preparedStatement.setString(3, "%" + member.last_name + "%");
            preparedStatement.setString(4, "%" + member.email_address + "%");
            preparedStatement.setString(5, member.phone_number == -1 ? "%%" : "%" + member.phone_number + "%");
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()) {
                Vector<Object> tmp = new Vector<>();
                tmp.add(rs.getLong(1));
                tmp.add(rs.getString(2));
                tmp.add(rs.getString(3));
                tmp.add(rs.getDate(4));
                tmp.add(rs.getString(5));
                tmp.add(rs.getLong(6));
                data.add(tmp);
            }
            conn.close();
        } catch (Exception e) {e.printStackTrace();}
        memberTable.setModel(new DefaultTableModel(data, cols));
        memberTable.revalidate();
        memberTable.repaint();
        JScrollPane scrollPane = (JScrollPane) memberTable.getParent().getParent();
        scrollPane.revalidate();
        scrollPane.repaint();
    }

    public Member getMember(long id) {
        Member member = null;
        try{
            Connection conn = DriverManager.getConnection(jdbcValues.DB_URL, jdbcValues.DB_USERNAME, jdbcValues.DB_PASSWORD);
            String query = "SELECT * FROM members WHERE id_number=?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setLong(1,id);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                member = new Member();
                member.id_number = rs.getLong(1);
                member.first_name = rs.getString(2);
                member.last_name = rs.getString(3);
                member.date_of_birth = rs.getDate(4).toLocalDate();
                member.email_address = rs.getString(5);
                member.phone_number = rs.getLong(6);
                member.creation_date = rs.getDate(7).toLocalDate();
                member.expiration_date = rs.getDate(8).toLocalDate();
                member.last_checked_in = rs.getDate(9).toLocalDate();
            }
            preparedStatement.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return member;
    }

    public boolean deleteMember(long id) {
        boolean deleted = false;
        try{
            Connection conn = DriverManager.getConnection(jdbcValues.DB_URL, jdbcValues.DB_USERNAME, jdbcValues.DB_PASSWORD);
            String query = "DELETE FROM members WHERE id_number=?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setLong(1,id);
            int executed = preparedStatement.executeUpdate();
            deleted = executed != PreparedStatement.EXECUTE_FAILED;
            preparedStatement.close();
            conn.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return deleted;
    }

    public boolean renewMembership(long id, int md){
        boolean renewed = false;
        try {
            Connection conn = DriverManager.getConnection(jdbcValues.DB_URL, jdbcValues.DB_USERNAME, jdbcValues.DB_PASSWORD);
            String query = "UPDATE members SET expiration_date = DATE_ADD(expiration_date, INTERVAL ? MONTH) , last_checked_in_date = ? WHERE id_number = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, md);
            preparedStatement.setDate(2, Date.valueOf(LocalDate.now()));
            preparedStatement.setLong(3, id);
            int r = preparedStatement.executeUpdate();
            conn.close();
            if (r != 0) renewed = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return renewed;
    }

    public boolean updateMembership(Member member) {
        boolean updated = false;
        try{
            Connection conn = DriverManager.getConnection(jdbcValues.DB_URL, jdbcValues.DB_USERNAME, jdbcValues.DB_PASSWORD);
            String query = "UPDATE members SET first_name=?, last_name=?, email_address=?, phone_number=? WHERE id_number=?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1,member.first_name);
            preparedStatement.setString(2,member.last_name);
            preparedStatement.setString(3,member.email_address);
            preparedStatement.setLong(4,member.phone_number);
            preparedStatement.setLong(5,member.id_number);
            int u = preparedStatement.executeUpdate();
            conn.close();
            if(u != 0) updated = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return updated;
    }
}