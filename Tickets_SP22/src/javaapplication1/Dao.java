/*This file is the CRUD of the program and was designed 
 * as part of the final project of ITMD 411
 * Made by: Saad
 */
package javaapplication1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Dao {
	// instance fields
	static Connection connect = null;
	Statement statement = null;

	// constructor
	public Dao() {
	  
	}

	public Connection getConnection() {
		// Setup the connection with the DB
		try {
			connect = DriverManager
					.getConnection("jdbc:mysql://www.papademas.net:3307/tickets?autoReconnect=true&useSSL=false"
							+ "&user=fp411&password=411");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connect;
	}

	// CRUD implementation

	public void createTables() {
		// variables for SQL Query table creations
		final String createTicketsTable = "CREATE TABLE sahme_tickets(ticket_id INT AUTO_INCREMENT PRIMARY KEY, ticket_issuer VARCHAR(30), ticket_description VARCHAR(200), start_date DATE , end_date DATE)";
		final String createUsersTable = "CREATE TABLE sahme_users(uid INT AUTO_INCREMENT PRIMARY KEY, uname VARCHAR(30), upass VARCHAR(30), admin int)";

		try {

			// execute queries to create tables

			statement = getConnection().createStatement();

			statement.executeUpdate(createTicketsTable);
			statement.executeUpdate(createUsersTable);
			System.out.println("Created tables in given database...");

			// end create table
			// close connection/statement object
			statement.close();
			connect.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		// add users to user table
		addUsers();
	}

	public void addUsers() {
		// add list of users from userlist.csv file to users table

		// variables for SQL Query inserts
		String sql;

		Statement statement;
		BufferedReader br;
		List<List<String>> array = new ArrayList<>(); // list to hold (rows & cols)

		// read data from file
		try {
			br = new BufferedReader(new FileReader(new File("./userlist.csv")));

			String line;
			while ((line = br.readLine()) != null) {
				array.add(Arrays.asList(line.split(",")));
			}
		} catch (Exception e) {
			System.out.println("There was a problem loading the file");
		}

		try {

			// Setup the connection with the DB

			statement = getConnection().createStatement();

			// create loop to grab each array index containing a list of values
			// and PASS (insert) that data into your User table
			for (List<String> rowData : array) {

				sql = "insert into sahme_users(uname,upass,admin) " + "values('" + rowData.get(0) + "'," + " '"
						+ rowData.get(1) + "','" + rowData.get(2) + "');";
				statement.executeUpdate(sql);
			}
			System.out.println("Inserts completed in the given database...");

			// close statement object
			statement.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public int insertRecords(String user ,String ticketName, String ticketDesc) {
		int id = 0;
		try {
			statement = getConnection().createStatement();
			LocalDate getDate = LocalDate.now();
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	        String date = getDate.format(formatter);
			statement.executeUpdate("Insert into sahme_tickets" + "(ticket_issuer, ticket_description, start_date) values(" + " '"
					+ ticketName + "','" + ticketDesc + "','" + date +"')", Statement.RETURN_GENERATED_KEYS);

			// retrieve ticket id number newly auto generated upon record insertion
			ResultSet resultSet = null;
			resultSet = statement.getGeneratedKeys();
			if (resultSet.next()) {
				// retrieve first field in table
				id = resultSet.getInt(1);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;

	}

	public ResultSet readRecords( boolean isAdmin, String user) {

		ResultSet results = null;
		try {
			statement = getConnection().createStatement();
			//only allowing Admin to see all tickets and normal users see their own tickets
			if (isAdmin) {
				results = statement.executeQuery("SELECT * FROM sahme_tickets");
			}else {
				results = statement.executeQuery("SELECT * FROM sahme_tickets where ticket_issuer = '" +user+"'");
		}
			
			//connect.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return results;
	}
	//getting tickets by number
	public ResultSet searchNum( boolean isAdmin, String user, int ticketNum) {

		ResultSet results = null;
		try {
			statement = getConnection().createStatement();
			//only allowing Admin to see all tickets and normal users see their own tickets
			if (isAdmin ) {
				results = statement.executeQuery("SELECT * FROM sahme_tickets where ticket_id = "+ticketNum);
			}else {
				results = statement.executeQuery("SELECT * FROM sahme_tickets where ticket_id = "+ticketNum + " and ticket_issuer = '"+user+"'");
			}
			//making sure the user does not search wrong ticket number
			if (!results.isBeforeFirst() ) {    
				System.out.println("The Ticket: "+ticketNum + ", was not found");}
			else {
				System.out.println("The Ticket: "+ticketNum + ", was found");}
				//connect.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return results;
	}
	// updateRecords implementation
	public void updateRecords( boolean isAdmin, String user, int ticketNum, String desc) {

		try {
			statement = getConnection().createStatement();
			int update;
			//only allowing Admin to update all tickets 
			if (isAdmin)
				update = statement.executeUpdate("update sahme_tickets set ticket_description = '"+desc+"'" + " where ticket_id = "+ticketNum );
			else
				update = statement.executeUpdate("update sahme_tickets set ticket_description = '"+desc+"'" + " where ticket_issuer = '"+user+"'" + " and ticket_id = "+ticketNum);
			//making sure the update can happen
			if (update !=0) {
				System.out.println("The Ticket: "+ticketNum + "'s description has been udated");}
			else {
				System.out.println("The Ticket: "+ticketNum + ", does not exist");}
			//connect.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	

	//coding for deleteRecords implementation
	public void deleteRecords(int ticketNum) {
		//only Admins see delete
		try {
			statement = getConnection().createStatement();
			int delete = statement.executeUpdate("delete from sahme_tickets where ticket_id = "+ticketNum); 
			//making sure delete can happen and wont result in error
			if (delete !=0) {
				System.out.println("The Ticket: "+ticketNum + ", has been deleted");}
			else {
				System.out.println("The Ticket: "+ticketNum + ", does not exist");}
			//connect.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	//coding for closeRecords implementation
	public void closeRecords(int ticketNum) {
		//only Admins see closing of tickets
		try {
			statement = getConnection().createStatement();
			//inserting current date at the close
			LocalDate getDate = LocalDate.now();
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	        String date = getDate.format(formatter);
			int close = statement.executeUpdate("update sahme_tickets set end_date = '"+date+"'" +" where ticket_id = "+ticketNum );
			//making sure close can happen
			if (close !=0) {
				System.out.println("The Ticket: "+ticketNum + ", has been closed");}
			else {
				System.out.println("The Ticket: "+ticketNum + ", does not exist");}
			//connect.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	
}
