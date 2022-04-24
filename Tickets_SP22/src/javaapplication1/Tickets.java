/*This file is the gui and front end of the program and was designed 
 * as part of the final project of ITMD 411
 * Made by: Saad
 */
package javaapplication1;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

@SuppressWarnings("serial")
public class Tickets extends JFrame implements ActionListener {

	// class level member objects
	Dao dao = new Dao(); // for CRUD operations
	Boolean chkIfAdmin = null;
	String userN;
	// Main menu object items
	private JMenu mnuFile = new JMenu("File");
	private JMenu mnuAdmin = new JMenu("Admin");
	private JMenu mnuTickets = new JMenu("Tickets");

	// Sub menu item objects for all Main menu item objects
	JMenuItem mnuItemExit;
	JMenuItem mnuItemUpdate;
	JMenuItem mnuItemDelete;
	JMenuItem mnuItemOpenTicket;
	JMenuItem mnuItemViewTicket;
	JMenuItem mnuItemsearchNum; //view by number
	JMenuItem mnuItemCloseTicket; // close ticket
	JMenuItem mnuItemDescUpdateTicket; //update description

	public Tickets(Boolean isAdmin, String user) {

		chkIfAdmin = isAdmin;
		userN = user;
		createMenu();
		prepareGUI();
	}

	private void createMenu() {

		/* Initialize sub menu items **************************************/

		// initialize sub menu item for File main menu
		mnuItemExit = new JMenuItem("Exit");
		// add to File main menu item
		mnuFile.add(mnuItemExit);

		// initialize first sub menu items for Admin main menu 
		//Added update description for all instead
		//mnuItemUpdate = new JMenuItem("Update Ticket");
		// add to Admin main menu item
		//mnuAdmin.add(mnuItemUpdate);

		// initialize second sub menu items for Admin main menu
		mnuItemDelete = new JMenuItem("Delete Ticket");
		// add to Admin main menu item
		mnuAdmin.add(mnuItemDelete);

		// initialize first sub menu item for Tickets main menu
		mnuItemOpenTicket = new JMenuItem("Open Ticket");
		// add to Ticket Main menu item
		mnuTickets.add(mnuItemOpenTicket);

		// initialize second sub menu item for Tickets main menu
		mnuItemViewTicket = new JMenuItem("View Ticket");
		// add to Ticket Main menu item
		mnuTickets.add(mnuItemViewTicket);

		// initialize any more desired sub menu items below
		//View by ticket number
		mnuItemsearchNum = new JMenuItem("View Ticket by Number");
		mnuTickets.add(mnuItemsearchNum);
		
		//update ticket description
		mnuItemDescUpdateTicket = new JMenuItem("Update Ticket by Description");
		mnuTickets.add(mnuItemDescUpdateTicket);
		
		//close tickets
		mnuItemCloseTicket = new JMenuItem("Close Ticket");
		// add to Admin main menu item
		mnuAdmin.add(mnuItemCloseTicket);
		
		
		

		/* Add action listeners for each desired menu item *************/
		mnuItemExit.addActionListener(this);
		mnuItemDelete.addActionListener(this);
		mnuItemOpenTicket.addActionListener(this);
		mnuItemViewTicket.addActionListener(this);
		mnuItemsearchNum.addActionListener(this);
		mnuItemDescUpdateTicket.addActionListener(this);
		mnuItemCloseTicket.addActionListener(this);
		 /*
		  * continue implementing any other desired sub menu items (like 
		  * for update and delete sub menus for example) with similar 
		  * syntax & logic as shown above
		 */


	}

	private void prepareGUI() {

		// create JMenu bar
		JMenuBar bar = new JMenuBar();
		bar.add(mnuFile); // add main menu items in order, to JMenuBar
		if (chkIfAdmin) //make sure it's Admin before adding to gui
			bar.add(mnuAdmin);
		bar.add(mnuTickets);
		// add menu bar components to frame
		setJMenuBar(bar);
		//setting up iit logo to appear everywhere
		Image icon = Toolkit.getDefaultToolkit().getImage("iiteditedlogo.png");
		setIconImage(icon);
		addWindowListener(new WindowAdapter() {
			// define a window close operation
			public void windowClosing(WindowEvent wE) {
				System.exit(0);
			}
		});
		// set frame options
		setSize(400, 420);
		getContentPane().setBackground(Color.LIGHT_GRAY);
		setLocationRelativeTo(null);
		setVisible(true);
		try {

			// displaying table at start so user can see it instantly
			JTable jt = new JTable(ticketsJTable.buildTableModel(dao.readRecords(chkIfAdmin, userN)));
			jt.setBounds(30, 40, 200, 400);
			JScrollPane sp = new JScrollPane(jt);
			add(sp);
			setVisible(true); // refreshes or repaints frame on screen

		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// implement actions for sub menu items
		
		if (e.getSource() == mnuItemExit) {
			System.exit(0);
		} else if (e.getSource() == mnuItemOpenTicket) {

			// get ticket information
			String ticketName = userN;
			String ticketDesc = JOptionPane.showInputDialog(null, "Enter a ticket description");

			//if user cancels it does not result in crash
			if(ticketName == null|| ticketDesc == null) {
				System.out.println("Canceled Opening Ticket");
			}else {
				
				// insert ticket information to database
			int id = dao.insertRecords(userN, ticketName, ticketDesc); //auto entering user's name based on login name
			// display results if successful or not to console / dialog box
			if (id != 0) {
				System.out.println("Ticket ID : " + id + " created successfully!!!");
				JOptionPane.showMessageDialog(null, "Ticket id: " + id + " created");
			} else {
				System.out.println("Ticket cannot be created!!!");
				} 
			}
			/*
		 * continue implementing any other desired sub menu items (like for update and
		 * delete sub menus for example) with similar syntax & logic as shown above
		 */
		}else if (e.getSource() == mnuItemCloseTicket) {
			String ticketNum = JOptionPane.showInputDialog(null, "Enter ticket number to close");
			
			//if user cancels it does not result in crash
			if(ticketNum == null) {
				System.out.println("Canceled Closing Ticket");
			}else {
			int confirm = JOptionPane.showConfirmDialog(null,("Are you sure you want to close ticket: " + Integer.valueOf(ticketNum)), "Please Confirm",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			
			// display results if successful or not to console / dialog box
			if (confirm == JOptionPane.YES_OPTION) {
				dao.closeRecords(Integer.valueOf(ticketNum));
			} else {
				System.out.println("No Ticket Closed");} }
		}else if (e.getSource() == mnuItemDelete) {
			String ticketNum = JOptionPane.showInputDialog(null, "Enter ticket number to delete");
			
			//if user cancels it does not result in crash
			if(ticketNum == null) {
				System.out.println("Canceled Deleting Ticket");
			}else {
				int confirm = JOptionPane.showConfirmDialog(null,("Are you sure you want to delete ticket: " + Integer.valueOf(ticketNum)), "Please Confirm",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			
			// display results if successful or not to console / dialog box
			if (confirm == JOptionPane.YES_OPTION) {
				dao.deleteRecords(Integer.valueOf(ticketNum));
			} else {
				System.out.println("No Ticket Deleted");
				}}
			
		}else if (e.getSource() == mnuItemViewTicket) {

			// retrieve all tickets details for viewing in JTable
			try {
				
				// Use JTable built in functionality to build a table model and
				// display the table model off your result set!!!
				JTable jt = new JTable(ticketsJTable.buildTableModel(dao.readRecords(chkIfAdmin, userN)));
				jt.setBounds(30, 40, 200, 400);
				
				JScrollPane sp = new JScrollPane(jt);
				add(sp);
				setVisible(true); // refreshes or repaints frame on screen
				
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}else if (e.getSource() == mnuItemsearchNum) {
			// retrieve all tickets details for viewing in JTable
			try {
				String ticketNum = JOptionPane.showInputDialog(null, "Enter ticket number to view");
				
				//if user cancels it does not result in crash
				if(ticketNum == null) {
					System.out.println("Canceled Ticket Search");
				}else {
				// Use JTable built in functionality to build a table model and
				// display the table model off your result set!!!
				JTable jt = new JTable(ticketsJTable.buildTableModel(dao.searchNum(chkIfAdmin, userN, Integer.valueOf(ticketNum))));
				jt.setBounds(30, 40, 200, 400);
				JScrollPane sp = new JScrollPane(jt);
				add(sp);
				setVisible(true); // refreshes or repaints frame on screen

			}} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}else if (e.getSource() == mnuItemDescUpdateTicket) {
			String ticketNum = JOptionPane.showInputDialog(null, "Enter ticket number to update description");
			String ticketDesc = JOptionPane.showInputDialog(null, "Enter a ticket description to update");
			
			
			//if user cancels it does not result in crash
			if(ticketNum == null|| ticketDesc == null) {
				System.out.println("Canceled Ticket Update");
			}else {
			
			int confirm = JOptionPane.showConfirmDialog(null,("Are you sure you want to update decription: " + Integer.valueOf(ticketNum)), "Please Confirm",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			// display results if successful or not to console / dialog box
			if (confirm == JOptionPane.YES_OPTION) {
				dao.updateRecords(chkIfAdmin,userN, Integer.valueOf(ticketNum), ticketDesc);
			} else {
				System.out.println("No Ticket Updated");
				}
			
			}	
		}

	}

}
