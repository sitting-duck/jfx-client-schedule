# Title: jfx-client-schedule
Note: more documentation coming soon. Just been busy with the holidays. This is a final project for CRUD apps with mySQL and Java at WGU. It Basically mimics something like what a business app would be like for making appointments, checking to see if two appointments overlap, adding people and their contact info and all kinds of stuff like that into a database. The SQL in here isn't super complicated and we don't even go into merging tables or any advanced concepts, but I do like how they approached teaching the JDBC database connection stuff, also some code conventions and good project structure and things like this. 

# Purpose
CRUD app for creating Customers and Appointments.

# How to Run
In IntelliJ IDEA Creator click the 'Run' or 'Debug' button next to the configuration settings

# Additional Report
I added a reports button under the customer table that shows all the appointments scheduled for the selected customer.

# MySQL Connector Driver Version Number
mysql-connector-java-8.0.29

# Author
Ashley Gelwix

# Contact
ashley.tharp@gmail.com

# App Version
1.0.0

# Date:
12/19/2022

# IDE
IntelliJ IDEA 2022.2.4 (Community Edition)
Build #IC-222.4459.24, built on November 22, 2022
Runtime version: 17.0.5+7-b469.71 amd64
VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o.
Windows 11 10.0
GC: G1 Young Generation, G1 Old Generation
Memory: 2032M
Cores: 12
Non-Bundled Plugins:
    com.intellij.properties.bundle.editor (222.4459.16)

Kotlin: 222-1.7.10-release-334-IJ4459.24

## User Roles
### User
    Schedules and prepares for the meeting, but does not have to attend the meeting. 
    User will enter their username and password on the login form to use the application. 

### Customer
    Comes to the meeting. Must be there. 

### Contact
    Affiliated with the meeting but does not have to attend. 
    Probably gets paid if the meeting happens or not, roles like this.  

Only the Customer must be at the meeting/appointment. 
