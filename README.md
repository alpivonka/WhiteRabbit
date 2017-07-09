![alt text](https://github.com/OHDSI/WhiteRabbit/blob/master/src/org/ohdsi/whiteRabbit/WhiteRabbit64.png) WhiteRabbit
===========
AL Pivonka's Fork and Functionality
===========================================
--Rabbit In A Hat Functionality:
Added the following abilities to extract from the ETL :
    a) mapping Pseudocode SQL, which represents the mapped source to target table to attribute mappings.
    b) Source Database and generate Pseudocode SQL of table/attribute fill rates obtained from White Rabbit.
    c) Target Database and generate Pseudocode SQL of table/attribute fill rates.

--White Rabbit
Added the ability to obtain the source table/attribute fill rates to be used within Rabbit In A Hat.

================================================================================================================================

Introduction
========
WhiteRabbit is a small application that can be used to analyse the structure and contents of a database as preparation for designing an ETL. It comes with RabbitInAHat, an application for interactive design of an ETL to the OMOP Common Data Model with the help of the the scan report generated by White Rabbit. 

Features
========
- Can scan databases in SQL Server, Oracle, PostgreSQL, MySQL, MS Access, Amazon RedShift, and CSV files
- The scan report contains information on tables, fields, and frequency distributions of values
- Cutoff on the minimum frequency of values to protect patient privacy
- WhiteRabbit can be run with a graphical user interface or from the command prompt
- Interactive tool (Rabbit in a Hat) for designing the ETL using the scan report as basis
- Rabbit in a Hat generates ETL specification document according to OMOP template

Screenshots
===========
<table border = "">
<tr valign="top">
<td width = 50%>
  <img src="https://github.com/OHDSI/WhiteRabbit/blob/master/man/WRScreenshot.png" alt="White Rabbit" title="White Rabbit" />
</td>
<td width = 50%>
 <img src="https://github.com/OHDSI/WhiteRabbit/blob/master/man/RIAHScreenshot.png" alt="Rabbit in a Hat" title="Rabbit in a Hat" />
</td>
</tr><tr>
<td>White Rabbit</td><td>Rabbit in a Hat</td>
</tr>
</table>

Technology
============
White Rabbit and Rabbit in a Hat are pure Java applications. Both applications use [Apache's POI Java libraries](http://poi.apache.org/) to read and write Word and Excel files. White Rabbit uses JDBC to connect to the respective databases.

System Requirements
============
Requires Java 1.7 or higher, and read access to the database to be scanned. Java can be downloaded from
<a href="http://www.java.com" target="_blank">http://www.java.com</a>.

Dependencies
============
 * There are no dependencies.

Getting Started
===============
WhiteRabbit

1. Under the [Releases](https://github.com/OHDSI/WhiteRabbit/releases) tab, download WhiteRabbit*.zip
2. Unzip the download
3. Double-click on WhiteRabbit.jar to start White Rabbit.

(See the [Wiki](http://www.ohdsi.org/web/wiki/doku.php?id=documentation:software:whiterabbit#running_from_the_command_line) for details on how to run from the command prompt instead)

Rabbit-In-A-Hat

1. Using the files downloaded for WhiteRabbit, double-click on RabbitInAHat.jar to start Rabbit-In-A-Hat.

Getting Involved
=============
* User guide and Help: <a href="http://www.ohdsi.org/web/wiki/doku.php?id=documentation:software:whiterabbit">WhiteRabbit Wiki</a>
* Developer questions/comments/feedback: <a href="http://forums.ohdsi.org/c/developers">OHDSI Forum</a>
* We use the <a href="../../issues">GitHub issue tracker</a> for all bugs/issues/enhancements

License
=======
WhiteRabbit is licensed under Apache License 2.0

Development
===========
White Rabbit and Rabbit in a Hat are being developed in Eclipse. Contributions are welcome.

### Development status

Production. This program is being used by many people.
