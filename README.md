# SoftEng Health Club Membership Management System

## Table of Contents
- [About the App](#about-the-app)
- [Technologies Used](#technologies-used)
- [Implementation Details](#implementation-details)
- [Testing Details](#testing-details)
- [Known Bugs](#known-bugs)
- [About the Team](#about-the-team)
- [Contact Information](#contact-information)

## About the App
The SoftEng Health Club is known for its commitment to member satisfaction. It aims to enhance its services with a new membership management system. As the club grows, it seeks to streamline operations and improve communication with its members. This system will help SoftEng efficiently manage memberships, handle renewals, and support better member engagement. With a focus on maintaining its reputation for excellent service. This new system will ensure both staff and members enjoy a smooth experience. The system will be designed to handle various membership durations, notify members of upcoming renewals, and allow for on-the-spot membership renewals and sign-up. Additionally, it will offer tools for the club’s management to monitor member activity and generate reports for operational decisions. The following document outlines the complete design needed to meet these objectives and ensure smooth operation for both members and staff. The SoftEng Health Club's suggested membership management system is made to specifically address the demands of both the club and its customers.  This solution will increase membership satisfaction by emphasizing automation, user experience, and data insights through specialized latent tasks.

## Technologies Used
- **Java**: Primary programming language for backend development.
- **MySQL**: Database management system for storing and retrieving all membership data.

## Implementation Details

The SoftEng Health Club Membership Management System comprises several Java classes and interfaces, each responsible for specific functionalities that ensure robust operation and maintainability. Here's an overview of the key components:

### Classes and Components:

1. **`Member.java`**:
   - Inherits from the `Person` class.
   - Attributes include `id_number`, `creation_date`, `expiration_date`, and `last_checked_in`.
   - Methods for setting ID number, creation date, updating check-in times, and expiration dates, which aid in tracking membership validity and usage.

2. **`Person.java`**:
   - Base class for all person-type entities in the system.
   - Contains personal information like name, email address, date of birth, and phone number.
   - Methods to retrieve and update personal information, ensuring encapsulation and data integrity.

3. **`Status.java`**:
   - An enumeration that defines roles such as `STAFF` and `CLUB_MANAGER`, utilized for role-based access control within the system.

4. **`LoginPage.java`**:
   - A Java Swing dialog that handles user authentication.
   - Integrates with the system's backend to validate user credentials against stored data in the MySQL database.
   - Provides GUI elements for user interaction and input validation.

5. **`jdbcValues.java`**:
   - An interface that stores JDBC database connection parameters such as DB URL, username, and password, centralizing the database configuration for easy management.

6. **`LoginPage.form`**:
   - An XML configuration for designing the GUI layout of the login page, specifying grid layouts, fonts, and other UI components.

7. **`SoftEngHealthClubSystem.java`**:
   - The main driver class that initiates the application.
   - Handles session management post-login, displaying the home page and passing user details throughout the system.

8. **`LogIn.java`**:
   - Facilitates the interaction with the `LoginPage` class.
   - Handles the outcome of the authentication process, either logging the user in or handling login failures.

### Database Integration:

- Utilizes MySQL for data storage, managing members' data, session logs, and authentication details.
- Connection and query executions are managed through standard JDBC API calls, ensuring reliable data transactions and security against SQL injection via prepared statements.

### User Interface:

- Designed using Java Swing, providing a native look and feel that is familiar to users.
- The login page is created with a combination of labels, text fields, and buttons laid out using a `GridLayoutManager` to ensure a responsive layout across different screen resolutions.


## Testing Details
[To be completed]

## Known Bugs
[To be noted]

## About the Team
This project is developed by a dedicated team of software engineering students from the COMP330 course. The team has embraced the challenge of developing a robust membership management system and has shown exceptional commitment to continuous improvement and clear communication.

## Contact Information
For more details about the project, or to reach out with questions or suggestions, please contact the following team members:
- **Nahum Gessesse** - Developer - Email: [nahumguess@gmail.com](mailto:nahumguess@gmail.com)
- **Xander Estevez** - Developer - Email: [xandereste633@gmail.com](mailto:xandereste633@gmail.com)
- **Manali Deb** - QA/Technical Writer - Email: [debook4@gmail.com](mailto:debook4@gmail.com)
- **Alina Zacaria** - Tech Lead - Email: [alinazac9@gmail.com](mailto:alinazac9@gmail.com)
- **Andrew Do** - Developer - Email: [Andrewydo@gmail.com](mailto:Andrewydo@gmail.com)
- **Nick Calhoun** - Developer - Email: [nickfilms32@gmail.com](mailto:nickfilms32@gmail.com)
- **Jai Fischer** - Developer - Email: [jaifischer@gmail.com](mailto:jaifischer@gmail.com)
