package test;

import main.project.pages.InactiveMemberList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.Arrays;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TestInactiveMemberList {

    @InjectMocks
    private InactiveMemberList inactiveMemberList;

    @Mock
    private JTable inactiveMemberTable;

    @Mock
    private JButton downloadButton;

    @Mock
    private JFileChooser fileChooser;

    @Mock
    private Connection connMock;

    @Mock
    private Statement stmtMock;

    @Mock
    private ResultSet resultSetMock;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        inactiveMemberList = new InactiveMemberList(new JFrame());
        downloadButton = inactiveMemberList.downloadButton;
        inactiveMemberTable = inactiveMemberList.inactiveMemberTable;
        when(inactiveMemberTable.getModel()).thenReturn(new DefaultTableModel());
    }

    @Test
    public void testInitializeTable() throws Exception {
        when(stmtMock.executeQuery(anyString())).thenReturn(resultSetMock);
        when(resultSetMock.next()).thenReturn(true).thenReturn(false);
        when(resultSetMock.getLong(1)).thenReturn(1L);
        when(resultSetMock.getString(2)).thenReturn("John");
        when(resultSetMock.getString(3)).thenReturn("Doe");
        when(resultSetMock.getDate(4)).thenReturn(java.sql.Date.valueOf("1990-01-01"));
        when(resultSetMock.getString(5)).thenReturn("john.doe@example.com");
        when(resultSetMock.getLong(6)).thenReturn(1234567890L);
        when(resultSetMock.getDate(7)).thenReturn(java.sql.Date.valueOf("2020-01-01"));
        when(resultSetMock.getDate(8)).thenReturn(java.sql.Date.valueOf("2021-01-01"));
        when(resultSetMock.getDate(9)).thenReturn(java.sql.Date.valueOf("2021-12-01"));

        inactiveMemberList.initializeTable();
        verify(inactiveMemberTable, times(1)).setModel(any(DefaultTableModel.class));
    }

    @Test
    public void testSaveTableToFile() throws Exception {
        JFileChooser mockFileChooser = mock(JFileChooser.class);
        when(mockFileChooser.showSaveDialog(any())).thenReturn(JFileChooser.APPROVE_OPTION);
        when(mockFileChooser.getSelectedFile()).thenReturn(new File("test.txt"));
        TableModel modelMock = mock(TableModel.class);
        when(inactiveMemberTable.getModel()).thenReturn(modelMock);
        when(modelMock.getColumnCount()).thenReturn(9);
        when(modelMock.getColumnName(anyInt())).thenReturn("Column");
        when(modelMock.getValueAt(anyInt(), anyInt())).thenReturn("Value");
        File fileToSave = new File("test.txt");


        InactiveMemberList.saveTableToFile(inactiveMemberTable);


        verify(mockFileChooser).showSaveDialog(any());
        verify(mockFileChooser).getSelectedFile();
    }

    @Test
    public void testSaveTableToFile_ErrorHandling() throws Exception {
        TableModel modelMock = mock(TableModel.class);
        when(inactiveMemberTable.getModel()).thenReturn(modelMock);
        when(modelMock.getColumnCount()).thenReturn(9);
        when(modelMock.getColumnName(anyInt())).thenReturn("Column");
        when(modelMock.getValueAt(anyInt(), anyInt())).thenReturn("Value");

        File fileToSave = new File("test.txt");
        BufferedWriter writerMock = mock(BufferedWriter.class);
        doThrow(new IOException("Error writing file")).when(writerMock).write(anyString());


        InactiveMemberList.saveTableToFile(inactiveMemberTable);


        verify(writerMock).write(anyString());
    }

    @Test
    public void testButtonAction() throws Exception {

        downloadButton.doClick();
        verify(downloadButton, times(1)).doClick();
    }

    @Test
    public void testTableInteraction() {
        Vector<String> columnNames = new Vector<>(Arrays.asList("ID", "Name"));
        Vector<Object> rowData = new Vector<>();
        rowData.add(1);
        rowData.add("John Doe");
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(columnNames);
        tableModel.addRow(rowData);
        inactiveMemberTable.setModel(tableModel);

        assertEquals(1, inactiveMemberTable.getRowCount());
        assertEquals("John Doe", inactiveMemberTable.getValueAt(0, 1));
    }
}
