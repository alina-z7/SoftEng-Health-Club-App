package main.project.pages;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Vector;

public class InactiveMemberList extends JDialog implements jdbcValues{

    final Vector<String> cols = new Vector<>(Arrays.asList("ID", "First Name", "Last Name", "Birthday", "Email", "Phone Number", "Creation Date", "Expiration Date", "Last Checked In"));

    public JTable inactiveMemberTable;
    public JButton downloadButton;
    public JLabel welcome;
    private JPanel InactiveMemberListPanel;

    public InactiveMemberList(JFrame parent) {
        super(parent);
        setContentPane(InactiveMemberListPanel);
        setMinimumSize(new Dimension(1000,600));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initializeTable();
        downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveTableToFile(inactiveMemberTable);
            }
        });
        setVisible(true);
    }

    public void initializeTable(){
        Vector<Vector<Object>> data = new Vector<>();
        try{
            Connection conn = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);
            Statement stmt = conn.createStatement();
            String query = "SELECT id_number, first_name, last_name, birth_date, email_address, phone_number, creation_date, expiration_date, last_checked_in_date " +
                    "FROM members WHERE last_checked_in_date <= DATE_SUB(CURDATE(), INTERVAL 30 DAY)";
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                Vector<Object> tmp = new Vector<>();
                tmp.add(rs.getLong(1));
                tmp.add(rs.getString(2));
                tmp.add(rs.getString(3));
                tmp.add(rs.getDate(4));
                tmp.add(rs.getString(5));
                tmp.add(rs.getLong(6));
                tmp.add(rs.getDate(7));
                tmp.add(rs.getDate(8));
                tmp.add(rs.getDate(9));
                data.add(tmp);
            }
            stmt.close();
            conn.close();
        } catch (Exception e) {e.printStackTrace();}
        DefaultTableModel dtm = new DefaultTableModel(data, cols){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        inactiveMemberTable.setModel(dtm);
        inactiveMemberTable.getTableHeader().setReorderingAllowed(false);
        inactiveMemberTable.revalidate();
        inactiveMemberTable.repaint();
        JScrollPane scrollPane = (JScrollPane) inactiveMemberTable.getParent().getParent();
        scrollPane.revalidate();
        scrollPane.repaint();
    }

    public static void saveTableToFile(JTable table){
        JFileChooser jfc = new JFileChooser();
        jfc.setPreferredSize(new Dimension(600,500));
        jfc.setDialogTitle("Specify a file to save");

        // Set filter for text files
        FileNameExtensionFilter f = new FileNameExtensionFilter("Text Files", "txt");
        jfc.setFileFilter(f);

        File defaultFile = new File("Pages.InactiveMemberList.txt");
        jfc.setSelectedFile(defaultFile);

        // Set file selection mode to display both files and directories
        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        int userSelection = jfc.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = jfc.getSelectedFile();

            // Ensure the file has a .txt extension
            if (!fileToSave.getName().toLowerCase().endsWith(".txt")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".txt");
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileToSave))) {
                TableModel model = table.getModel();

                // Write column headers
                for (int i = 0; i < model.getColumnCount(); i++) {
                    bw.write(model.getColumnName(i) + "\t");
                }
                bw.newLine();

                // Write rows data
                for (int i = 0; i < model.getRowCount(); i++) {
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        bw.write(model.getValueAt(i, j).toString() + "\t");
                    }
                    bw.newLine();
                }

                System.out.println("Data saved to " + fileToSave.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}