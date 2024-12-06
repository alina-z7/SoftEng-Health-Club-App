# SoftEng Health Club Membership Management System

## Table of Contents
- [About the App](#about-the-app)
- [Technologies Used](#technologies-used)
- [Implementation Details](#implementation-details)
- [Known Bugs](#known-bugs)
- [Setup and Installation](#setup-and-installation)
- [About the Team](#about-the-team)
- [Contact Information](#contact-information)

## About the App
The SoftEng Health Club is known for its commitment to member satisfaction. It aims to enhance its services with a new membership management system. As the club grows, it seeks to streamline operations and improve communication with its members. This system will help SoftEng efficiently manage memberships, handle renewals, and support better member engagement. With a focus on maintaining its reputation for excellent service, this new system will ensure both staff and members enjoy a smooth experience.

The system is designed to:
- Handle various membership durations.
- Notify members of upcoming renewals.
- Allow on-the-spot membership renewals and sign-ups.
- Provide tools for management to monitor member activity and generate reports for informed decision-making.

This document outlines the complete design needed to meet these objectives, ensuring seamless operation for both members and staff. The SoftEng Health Club's membership management system is crafted to address the demands of both the club and its members, emphasizing automation, user experience, and data insights.

## Technologies Used
- **Java**: Primary programming language for backend development.
- **MySQL**: Database management system for storing and retrieving all membership data.
- **XML**: Used for defining the GUI layouts of the application.

## Implementation Details

### Database Integration:
- Utilizes MySQL for data storage, managing members' data, session logs, and authentication details.
- Connection and queries are handled through the JDBC API, ensuring reliable data transactions.
- Uses prepared statements to safeguard against SQL injection.

### User Interface:
- Designed using Java Swing, providing a familiar native look and feel.
- The login page and other UI components utilize a `GridLayoutManager` to ensure responsive and user-friendly design on various screen resolutions.

## Known Bugs
- **Selection Issue After Sorting**: Selecting a member after sorting the table by a column currently selects the member previously at that row before sorting. This causes incorrect member actions.
- **Array Out of Bounds Error**: Clicking the delete button without selecting a member results in an ArrayOutOfBoundsException, potentially causing application crashes and data loss.

## Setup and Installation
1. **Install IntelliJ IDEA**  
   Download and install IntelliJ IDEA from [JetBrains](https://www.jetbrains.com/idea/download/).

2. **Install JavaFX Plugin**  
   In IntelliJ, go to `File > Settings > Plugins` and search for "JavaFX" to install the required plugin.

3. **Configure Source Directories**  
   After cloning the repository, right-click on the `src` and `res` directories in the Project pane and select:  
   `Mark Directory as > Source Directory`  
   This ensures IntelliJ recognizes them as part of the project's source structure.

4. **Set JDK to Temurin 17**  
   Go to `File > Project Structure > Project` and set the "Project SDK" to `temurin-17` (or another JDK 17 distribution you have installed).

## About the Team
This project is developed by a dedicated team of software engineering students from the COMP330 course, who have demonstrated commitment to building a reliable membership management system, emphasizing continuous improvement and open communication.

## Contact Information
For more details or inquiries, please contact:

- **Nahum Gessesse** - Developer  
  Email: [nahumguess@gmail.com](mailto:nahumguess@gmail.com)
  
- **Xander Estevez** - Developer  
  Email: [xandereste633@gmail.com](mailto:xandereste633@gmail.com)
  
- **Manali Deb** - QA/Technical Writer  
  Email: [debook4@gmail.com](mailto:debook4@gmail.com)
  
- **Alina Zacaria** - Tech Lead  
  Email: [alinazac9@gmail.com](mailto:alinazac9@gmail.com)
  
- **Andrew Do** - Developer  
  Email: [Andrewydo@gmail.com](mailto:Andrewydo@gmail.com)
  
- **Nick Calhoun** - Developer  
  Email: [nickfilms32@gmail.com](mailto:nickfilms32@gmail.com)
  
- **Jai Fischer** - Developer  
  Email: [jaifischer@gmail.com](mailto:jaifischer@gmail.com)
