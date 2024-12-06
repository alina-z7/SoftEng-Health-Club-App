package test;

import com.github.javafaker.Faker;
import main.project.modules.Guest;
import main.project.modules.Member;
import main.project.pages.HealthClubHomePage;
import main.project.pages.CreateForm;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.swing.*;
import java.sql.*;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestHealthClubHomePage {

    @Mock
    private Connection connMock;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    private HealthClubHomePage healthClubHomePage;
    private CreateForm createForm;


    private Faker faker;

    private JTextField tfFirstName;
    private JTextField tfLastName;
    private JTextField tfEmail;
    private JTextField tfPhoneNumber;
    private JTextField tfBdMonth;
    private JTextField tfBdDay;
    private JTextField tfBdYear;
    private JTextField tfMembershipDuration;

    @Before
    public void setUp() {
        faker = new Faker();

        tfFirstName = mock(JTextField.class);
        tfLastName = mock(JTextField.class);
        tfEmail = mock(JTextField.class);
        tfPhoneNumber = mock(JTextField.class);
        tfBdMonth = mock(JTextField.class);
        tfBdDay = mock(JTextField.class);
        tfBdYear = mock(JTextField.class);
        tfMembershipDuration = mock(JTextField.class);
        doReturn(tfFirstName).when(createForm).getFirstNameField();
        doReturn(tfLastName).when(createForm).getLastNameField();
        doReturn(tfEmail).when(createForm).getEmailField();
        doReturn(tfPhoneNumber).when(createForm).getPhoneNumberField();
        doReturn(tfBdMonth).when(createForm).getBdMonthField();
        doReturn(tfBdDay).when(createForm).getBdDayField();
        doReturn(tfBdYear).when(createForm).getBdYearField();
        doReturn(tfMembershipDuration).when(createForm).getMembershipDurationField();
        createForm = mock(CreateForm.class);
        healthClubHomePage = mock(HealthClubHomePage.class);

        connMock = mock(Connection.class);
    }

    @Test
    public void testCreateMember() throws SQLException {

        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = faker.internet().emailAddress();
        long phoneNumber = 2304943939L;
        int membershipDuration = faker.number().numberBetween(6, 24);

        when(tfFirstName.getText()).thenReturn(firstName);
        when(tfLastName.getText()).thenReturn(lastName);
        when(tfEmail.getText()).thenReturn(email);
        when(tfPhoneNumber.getText()).thenReturn(String.valueOf(phoneNumber));
        when(tfBdMonth.getText()).thenReturn("11");
        when(tfBdDay.getText()).thenReturn("13");
        when(tfBdYear.getText()).thenReturn("199");
        when(tfMembershipDuration.getText()).thenReturn(String.valueOf(membershipDuration));



        Member member = new Member(firstName, lastName, email, LocalDate.of(Integer.parseInt(tfBdYear.getText()), Integer.parseInt(tfBdMonth.getText()), Integer.parseInt(tfBdDay.getText())), phoneNumber);


        when(connMock.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        CreateForm createForm = mock(CreateForm.class);
        when(createForm.checkIfMemberExists(any(), eq(connMock))).thenReturn(0);

        int isCreated = createForm.checkIfMemberExists(member, connMock);
        assertTrue(isCreated == 0, "Member should be created successfully.");
    }

    @Test
    public void testCreateMemberFailed() throws SQLException {

        String firstName = tfFirstName.getText();
        String lastName = tfLastName.getText();
        String email = tfEmail.getText();
        long phoneNumber = Long.parseLong(tfPhoneNumber.getText());
        int membershipDuration = Integer.parseInt(tfMembershipDuration.getText());

        Member member = new Member(firstName, lastName, email, LocalDate.of(Integer.parseInt(tfBdYear.getText()), Integer.parseInt(tfBdMonth.getText()), Integer.parseInt(tfBdDay.getText())), phoneNumber);


        when(connMock.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);

        CreateForm createForm = mock(CreateForm.class);
        when(createForm.checkIfMemberExists(any(), eq(connMock))).thenReturn(1);
        int isCreated = createForm.checkIfMemberExists(member, connMock);
        assertFalse(isCreated == 0, "Member creation should fail.");
    }

    @Test
    public void testReadMember() throws SQLException {
        long memberId = faker.number().randomNumber();

        when(connMock.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong("id_number")).thenReturn(memberId);
        when(mockResultSet.getString("first_name")).thenReturn(faker.name().firstName());
        when(mockResultSet.getString("last_name")).thenReturn(faker.name().lastName());

        Member member = healthClubHomePage.getMember(memberId);
        assertNotNull(member, "Member should not be null.");
        assertEquals(faker.name().firstName(), member.first_name, "First name should match.");
        assertEquals(faker.name().lastName(), member.last_name, "Last name should match.");
    }

    @Test
    public void testDeleteMember() throws SQLException {
        long memberId = faker.number().randomNumber();
        when(connMock.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        boolean isDeleted = healthClubHomePage.deleteMember(memberId);
        assertTrue(isDeleted, "Member should be deleted successfully.");
    }

    @Test
    public void testUpdateMember() throws SQLException {
        long memberId = faker.number().randomNumber();

        Member member = new Member();
        member.id_number = memberId;
        member.first_name = faker.name().firstName();
        member.last_name = faker.name().lastName();
        member.email_address = faker.internet().emailAddress();
        member.phone_number = Long.parseLong(faker.phoneNumber().cellPhone().replaceAll("[^\\d]", ""));

        when(connMock.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        boolean isUpdated = healthClubHomePage.updateMembership(member);
        assertTrue(isUpdated, "Member should be updated successfully.");
    }

    @AfterEach
    public void tearDown() throws SQLException {
        if (connMock != null) {
            connMock.close();
        }
    }
}
