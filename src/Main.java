package src;

import java.time.*;
import java.util.*;

// Class to represent basic person attributes
class Person {
    private String first_name;
    private String last_name;
    private LocalDate date_of_birth;
    private long phone_number;
    private String email_address;

    // Getters
    public String getFirstName() { return first_name; }
    public String getLastName() { return last_name; }
    public LocalDate getDOB() { return date_of_birth; }
    public long getPhoneNumber() { return phone_number; }
    public String getEmailAddress() { return email_address; }
}

// GymMember class extends Person
class GymMember extends Person {
    private long id_number; // Unique ID for the gym member
    private LocalDate date_of_membership_creation; // Start date of the membership
    private LocalDate date_of_last_membership_renewal; // Tracks the last renewal date
    private List<LocalDateTime> visitLog; // Log of member's visits to the gym
    private MembershipType membershipType; // Type of membership

    // Enum for different types of memberships
    public enum MembershipType {
        SIX_MONTHS(182), ONE_YEAR(365), THREE_YEARS(1095);

        private final int days;
        MembershipType(int days) { this.days = days; }
        public int getDays() { return days; }
    }

    // Constructor to initialize new gym member
    public GymMember(String firstName, String lastName, LocalDate dob,
                     long phone, String email, MembershipType type) {
        this.id_number = generateUniqueId();
        this.date_of_membership_creation = LocalDate.now();
        this.date_of_last_membership_renewal = LocalDate.now();
        this.membershipType = type;
        this.visitLog = new ArrayList<>();
    }

    public long getID() { return id_number; }
    public LocalDate getMemberStartDate() { return date_of_membership_creation; }
    public LocalDate getLastMemberShipRenewal() { return date_of_last_membership_renewal; }

    // Generates a unique ID using the current time (ms)
    private long generateUniqueId() {
        return System.currentTimeMillis() % 1000000000;
    }

    // Records a visit to the gym in the visit log
    public boolean isActive() {
        return LocalDate.now().isBefore(
                date_of_last_membership_renewal.plusDays(membershipType.getDays()));
    }

    // Records a visit to the gym in the visit log
    public void logVisit() {
        visitLog.add(LocalDateTime.now());
    }

    // Checks if the member is inactive based on their last visit
    public boolean isInactive() {
        if (visitLog.isEmpty()) return true;
        LocalDateTime lastVisit = visitLog.get(visitLog.size() - 1);
        return LocalDateTime.now().minusMonths(1).isAfter(lastVisit);
    }

    // Updates the last renewal date of the membership
    public void setLastMembershipRenewal(LocalDate date) {
        this.date_of_last_membership_renewal = date;
    }

}

// Staff class extends Person
class Staff extends Person {
    protected long employee_id_number;
    protected String employee_username;
    protected String employee_password;

    // Constructor to initialize staff members with username and password
    public Staff(String username, String password) {
        this.employee_username = username;
        this.employee_password = password;
        this.employee_id_number = generateEmployeeId();
    }

    // Generates a unique employee ID
    private long generateEmployeeId() {
        return System.currentTimeMillis() % 1000000;
    }

    // Method to change the employee's password
    public boolean change_password(String oldPassword, String newPassword) {
        if (employee_password.equals(oldPassword)) {
            employee_password = newPassword;
            return true;
        }
        return false;
    }

    // Method to change the employee's username
    public boolean change_username(String newUsername) {
        employee_username = newUsername;
        return true;
    }

    public long getID() { return employee_id_number; }
}

// ClubManager class extends Staff
class ClubManager extends Staff {
    public ClubManager(String username, String password) {
        super(username, password);
    }

    // Generates a list of members needing membership renewal
    public List<GymMember> generateMembershipRenewalList(Database db) {
        return db.retrieveMembersList().stream()
                .filter(member -> !member.isActive())
                .toList();
    }

    // Generates a report of inactive gym members
    public List<GymMember> generateInactiveMemberReport(Database db) {
        return db.retrieveMembersList().stream()
                .filter(GymMember::isInactive)
                .toList();
    }
}

// Database class to handle gym member data
class Database {
    private String DB_URL;
    private String USERNAME;
    private String PASSWORD;
    private List<GymMember> members;

    // Constructor to set up database connection (im assuming this is the format lol)
    public Database(String url, String username, String password) {
        this.DB_URL = url;
        this.USERNAME = username;
        this.PASSWORD = password;
        this.members = new ArrayList<>();
    }
    // Retrieves a copy of the list of gym members
    public List<GymMember> retrieveMembersList() {
        return new ArrayList<>(members);
    }

    // Searches for a gym member by ID
    public GymMember searchMember(long id_number) {
        return members.stream()
                .filter(member -> member.getID() == id_number)
                .findFirst()
                .orElse(null);
    }

    // Retrieves a list of inactive gym members
    public List<GymMember> retrieveInactiveMemberList() {
        return members.stream()
                .filter(GymMember::isInactive)
                .toList();
    }

    // Adds a new gym member
    public String addMember(GymMember member) {
        members.add(member);
        return "Member added successfully with ID: " + member.getID();
    }

    // Renews a gym membership
    public String renewMembership(long id_number) {
        GymMember member = searchMember(id_number);
        if (member != null) {
            member.setLastMembershipRenewal(LocalDate.now());
            return "Membership renewed successfully";
        }
        return "Member not found";
    }

    // Updates gym member information
    public String updateMemberInformation(long id_number, GymMember updatedMember) {
        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).getID() == id_number) {
                members.set(i, updatedMember);
                return "Member information updated successfully";
            }
        }
        return "Member not found";
    }

    // Deletes a gym member
    public String deleteMember(long id_number) {
        members.removeIf(member -> member.getID() == id_number);
        return "Member deleted successfully";
    }

    // Automated emailer for inactive members
    public String memberEmailer() {
        StringBuilder report = new StringBuilder();
        retrieveInactiveMemberList().forEach(member -> {
            report.append("Sending email to: ").append(member.getEmailAddress())
                    .append(" - We miss you at the gym!\n");
        });
        return report.toString();
    }
}