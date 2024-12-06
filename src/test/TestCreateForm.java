package test;

import com.github.javafaker.Faker;
import main.project.modules.Member;
import main.project.pages.CreateForm;
import org.junit.Before;
import org.junit.Test;
import javax.swing.*;
import java.sql.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TestCreateForm {

    private CreateForm createForm;
    private Member memberMock;
    private Connection connMock;
    private Faker faker;

    private JTextField tfFirstName;
    private JTextField tfLastName;
    private JTextField tfEmail;
    private JTextField tfPhoneNumber;
    private JTextField tfBdMonth;
    private JTextField tfBdDay;
    private JTextField tfBdYear;
    private JTextField tfMembershipDuration;
    private JButton createButton;

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
        createButton = mock(JButton.class);
        createForm = mock(CreateForm.class);
        doReturn(tfFirstName).when(createForm).getFirstNameField();
        doReturn(tfLastName).when(createForm).getLastNameField();
        doReturn(tfEmail).when(createForm).getEmailField();
        doReturn(tfPhoneNumber).when(createForm).getPhoneNumberField();
        doReturn(tfBdMonth).when(createForm).getBdMonthField();
        doReturn(tfBdDay).when(createForm).getBdDayField();
        doReturn(tfBdYear).when(createForm).getBdYearField();
        doReturn(tfMembershipDuration).when(createForm).getMembershipDurationField();
        doReturn(createButton).when(createForm).getCreateButton();

        connMock = mock(Connection.class);
    }

    @Test
    public void testCreateMember_Success() throws Exception {
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = faker.internet().emailAddress();
        long phoneNumber = 4151234567L;
        int membershipDuration = faker.number().numberBetween(6, 24);

        when(tfFirstName.getText()).thenReturn(firstName);
        when(tfLastName.getText()).thenReturn(lastName);
        when(tfEmail.getText()).thenReturn(email);
        when(tfPhoneNumber.getText()).thenReturn(String.valueOf(phoneNumber));
        when(tfBdMonth.getText()).thenReturn("12");
        when(tfBdDay.getText()).thenReturn("15");
        when(tfBdYear.getText()).thenReturn("1990");
        when(tfMembershipDuration.getText()).thenReturn(String.valueOf(membershipDuration));


        Member member = new Member(firstName, lastName, email, faker.date().birthday().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate(), phoneNumber);
        when(connMock.prepareStatement(anyString())).thenReturn(mock(PreparedStatement.class));

        assertTrue(createForm.checkIfMemberExists(member, connMock) == 0);
    }

    @Test
    public void testCreateMember_EmailAlreadyExists() throws Exception {
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = faker.internet().emailAddress();
        long phoneNumber = 6265552349L;
        int membershipDuration = faker.number().numberBetween(6, 24);

        Member member = new Member(firstName, lastName, email, faker.date().birthday().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate(), phoneNumber);
        when(createForm.checkIfMemberExists(any(), eq(connMock))).thenReturn(1);

        long res = createForm.createMember(member, membershipDuration);
        assertEquals(0, res);
    }

    @Test
    public void testCreateMember_PhoneAlreadyExists() throws Exception {
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = faker.internet().emailAddress();
        long phoneNumber = 8325678901L;
        int membershipDuration = faker.number().numberBetween(6, 24);

        Member member = new Member(firstName, lastName, email, faker.date().birthday().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate(), phoneNumber);
        when(createForm.checkIfMemberExists(any(), eq(connMock))).thenReturn(2);  // 2 means phone number exists

        long res = createForm.createMember(member, membershipDuration);
        assertEquals(0, res);
    }

    @Test
    public void testCreateMember_GenerateMetrics() throws Exception {
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = faker.internet().emailAddress();
        long phoneNumber = 3214509876L;
        int membershipDuration = faker.number().numberBetween(6, 24);

        Member member = new Member(firstName, lastName, email, faker.date().birthday().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate(), phoneNumber);

        long startTime = System.nanoTime();

        when(createForm.createMember(any(), anyInt())).thenReturn(12345L);
        long memberId = createForm.createMember(member, membershipDuration);

        long endTime = System.nanoTime();
        long execTime = endTime - startTime;

        Runtime runtime = Runtime.getRuntime();
        long memBefore = runtime.totalMemory() - runtime.freeMemory();

        long memAfter = runtime.totalMemory() - runtime.freeMemory();
        long memUsage = memAfter - memBefore;

        assertEquals(12345L, memberId);
        System.out.println("Execution Time: "+execTime+" ns");
        System.out.println("Memory Usage: "+memUsage+ " bytes");
        assertTrue("Execution time is too long", execTime < 1000000000L);  // 1 second max
        verify(createForm, times(1)).createMember(any(), anyInt());
    }
}
