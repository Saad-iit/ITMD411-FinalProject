/*This file is the login of the program and was designed 
 * as part of the final project of ITMD 411
 * Made by: Saad
 */
package javaapplication1;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
//useful for layouts
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


//controls-label text fields, button
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


@SuppressWarnings("serial")
public class Login extends JFrame {

	Dao conn;

	public Login() {

		super("IIT HELP DESK LOGIN");
		conn = new Dao();
		conn.createTables();
		setSize(400, 420);
		setLayout(new BorderLayout());
		setLocationRelativeTo(null); // centers window
		setLayout(null); //letting me place outside of borderLayout
		//setting iit icon 
		Image icon = Toolkit.getDefaultToolkit().getImage("iiteditedlogo.png"); 
		setIconImage(icon);
		// SET UP CONTROLS
		JLabel lblUsername = new JLabel("Username");
		JLabel lblPassword = new JLabel("Password");
		JLabel lblStatus = new JLabel();
		//JLabel lblSpacer = new JLabel(" ");

		JTextField txtUname = new JTextField(10);
		JPasswordField txtPassword = new JPasswordField();
		JButton btn = new JButton("Submit");
		JButton btnExit = new JButton("Exit");

		// constraints for bounds
		lblUsername.setBounds(60,130,70,25);
		txtUname.setBounds(128,130,190,30);
		lblPassword.setBounds(60,160,75,70);
		txtPassword.setBounds(128,180,190,30);
		lblStatus.setBounds(120,300,300,25);
		btn.setBounds(60,240,120,25);
		btnExit.setBounds(220,240,120,25);
		
		//setting and changing colors
		Color TextColor = new Color(114, 137, 218);
		Color ButtonColor = new Color(35, 39, 42);
		lblUsername.setForeground(TextColor);
		lblPassword.setForeground(TextColor);
		lblStatus.setForeground(TextColor);
		btn.setForeground(ButtonColor);
		btnExit.setForeground(ButtonColor);
		btn.setBackground(new Color(255, 255, 255));
		btnExit.setBackground(new Color(122, 6, 249));
		this.getContentPane().setBackground( new Color(44, 47, 51) );
		
		lblStatus.setToolTipText("Contact help desk to unlock password");
 
		// ADD OBJECTS TO FRAME
		add(lblUsername);  // 1st row filler
		add(txtUname);
		add(lblPassword);  // 2nd row
		add(txtPassword);
		add(btn);          // 3rd row
		add(btnExit);
		add(lblStatus);    // 4th row
		
		
		btn.addActionListener(new ActionListener() {
			int count = 0; // count agent

			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean admin = false;
				count = count + 1;
				// verify credentials of user (MAKE SURE TO CHANGE TO YOUR TABLE NAME BELOW)

				String query = "SELECT * FROM sahme_users WHERE uname = ? and upass = ?;";
				try (PreparedStatement stmt = conn.getConnection().prepareStatement(query)) {
					stmt.setString(1, txtUname.getText());
					stmt.setString(2, txtPassword.getText());
					ResultSet rs = stmt.executeQuery();
					if (rs.next()) {
						admin = rs.getBoolean("admin"); // get table column value
						new Tickets(admin, txtUname.getText()); //open Tickets file / GUI interface
						setVisible(false); // HIDE THE FRAME
						dispose(); // CLOSE OUT THE WINDOW
					} else
						lblStatus.setText("Try again! " + (3 - count) + " / 3 attempt(s) left");
						//if more than 3 fails it shuts down program
						if (count == 3) {
							System.out.println("3 wrong attempts, Program closing");
							System.exit(0);
						}
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
 			 
			}
		});
		btnExit.addActionListener(e -> System.exit(0));

		setVisible(true); // SHOW THE FRAME
	}


	public static void main(String[] args) {

		new Login();
	}
}
