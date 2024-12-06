package test;

import main.project.modules.Guest;
import main.project.pages.LoginPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestLoginPage {

    private LoginPage loginPage;
    private JFrame parentMock;

    @Mock
    private Connection connectionMock;

    @Mock
    private PreparedStatement preparedStatementMock;

    @Mock
    private ResultSet resultSetMock;

    @BeforeEach
    public void setUp() {
        loginPage = mock(LoginPage.class);
        connectionMock = mock(Connection.class);
    }

    @Test
    public void testLoginPageInitialization() {
        assertNotNull(loginPage);
        assertNotNull(loginPage.getFtfUserId());
        assertNotNull(loginPage.getPasswordField());
        assertNotNull(loginPage.getLoginButton());
        assertNotNull(loginPage.getCancelButton());
    }

    @Test
    public void testGetAuthenticatedGuestValidCredentials() throws SQLException {
        long testId = 12345L;
        String testPassword = "password123";

        when(resultSetMock.next()).thenReturn(true);
        when(resultSetMock.getLong("user_id")).thenReturn(testId);
        when(resultSetMock.getString("password")).thenReturn(testPassword);
        when(resultSetMock.getString("first_name")).thenReturn("John");
        when(resultSetMock.getString("last_name")).thenReturn("Doe");
        when(resultSetMock.getString("status")).thenReturn("ACTIVE");

        Guest guest = loginPage.getAuthenticatedGuest(testId, testPassword);
        assertNotNull(guest);
        assertEquals(testId, guest.id_number);
        assertEquals(testPassword, guest.password);
        assertEquals("John", guest.first_name);
        assertEquals("Doe", guest.last_name);
        assertEquals("STAFF", guest.getStatus().toString());
    }

    @Test
    public void testGetAuthenticatedGuestInvalidCredentials() throws SQLException {
        long testId = 12345L;
        String testPassword = "wrongpassword";

        when(resultSetMock.next()).thenReturn(false);

        Guest guest = loginPage.getAuthenticatedGuest(testId, testPassword);
        assertNotNull(guest);
    }

    @Test
    public void testLoginButtonActionListenerValidLogin() throws SQLException {
        long testId = 12345L;
        String testPassword = "password123";

        ActionEvent actionEvent = mock(ActionEvent.class);
        when(loginPage.getFtfUserId().getText()).thenReturn(String.valueOf(testId));
        when(loginPage.getPasswordField().getText()).thenReturn(Arrays.toString(testPassword.toCharArray()));

        when(resultSetMock.next()).thenReturn(true);
        when(resultSetMock.getLong("user_id")).thenReturn(testId);
        when(resultSetMock.getString("password")).thenReturn(testPassword);
        when(resultSetMock.getString("first_name")).thenReturn("John");
        when(resultSetMock.getString("last_name")).thenReturn("Doe");
        when(resultSetMock.getString("status")).thenReturn("ACTIVE");

        loginPage.getLoginButton().doClick();

        assertNotNull(loginPage.guest);
        verify(parentMock).dispose();
    }

    @Test
    public void testLoginButtonActionListenerInvalidLogin() throws SQLException {

        long testId = 12345L;
        String testPassword = "wrongpassword";


        when(loginPage.getFtfUserId().getText()).thenReturn(String.valueOf(testId));
        when(loginPage.getPasswordField().getText()).thenReturn(Arrays.toString(testPassword.toCharArray()));


        when(resultSetMock.next()).thenReturn(false);


        loginPage.getLoginButton().doClick();

        verify(loginPage, times(1)).dispose();
    }

    @Test
    public void testCancelButtonActionListener() {

        ActionEvent actionEvent = mock(ActionEvent.class);
        loginPage.getCancelButton();


        verify(parentMock, times(1)).dispose();
    }
}
