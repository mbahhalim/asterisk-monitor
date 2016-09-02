package com.lenkp.asteriskmonitor.gui;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class AsteriskConnectionSettings extends JFrame {
	// FIXME (Reza) Clean code
	private JPanel contentPane;

	public AsteriskConnectionSettings() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(120, 180, 300, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, " Setting " , TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(60, 30, 160, 350);
		contentPane.add(panel);
		
		((FlowLayout) panel.getLayout()).setAlignment(FlowLayout.LEFT);
		contentPane.add(panel);
		
		JLabel hostLabel = new JLabel("HostId");
		hostLabel.setBounds(33, 47, 70, 15);
		panel.add(hostLabel);
		
		JTextField hostTextField = new JTextField();
		hostTextField.setFont(new Font("Dialog", Font.PLAIN, 12));
		hostTextField.setBounds(39, 70, 170, 20);
		panel.add(hostTextField);
		hostTextField.setColumns(10);
		
		JLabel portLabel = new JLabel("Port");
		portLabel.setBounds(33, 47, 70, 15);
		panel.add(portLabel);
		
		JTextField portTextField = new JTextField();
		portTextField.setFont(new Font("Dialog", Font.PLAIN, 12));
		portTextField.setBounds(39, 100, 170, 20);
		panel.add(portTextField);
		portTextField.setColumns(10);
		
		JLabel usernameLabel = new JLabel("Username");
		usernameLabel.setBounds(33, 47, 70, 15);
		panel.add(usernameLabel);
		
		JTextField usernameTextField = new JTextField();
		usernameTextField.setFont(new Font("Dialog", Font.PLAIN, 12));
		usernameTextField.setBounds(39, 130, 170, 20);
		panel.add(usernameTextField);
		usernameTextField.setColumns(10);
		
		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setBounds(33, 47, 70, 15);
		panel.add(passwordLabel);
		
		JPasswordField passwordField = new JPasswordField();
		passwordField.setFont(new Font("Dialog", Font.PLAIN, 12));
		passwordField.setBounds(39, 160, 170, 20);
		panel.add(passwordField);
		passwordField.setColumns(10);

		/*
			cara mengambil password
			new String(passwordField.getPassword())
		*/
		

		
		JButton btnDone = new JButton("Done");
		btnDone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Properties prop = new Properties();
				OutputStream output = null;

				try {

					output = new FileOutputStream("config.properties");

					// set the properties value
					prop.setProperty("host", hostTextField.getText());
					prop.setProperty("port", portTextField.getText());
					prop.setProperty("username", usernameTextField.getText());
					prop.setProperty("password", new String(passwordField.getPassword()));

					// save properties to project root folder
					prop.store(output, null);

				} catch (IOException io) {
					io.printStackTrace();
				} finally {
					if (output != null) {
						try {
							output.close();
						} catch (IOException ex) {
							ex.printStackTrace();
						}
					}

				}
				
				Main.initialiseMainFrame();
				Main.mainFrame.setVisible(true);
				closeJFrame();
				
			}
		});
		btnDone.setBounds(39, 210, 117, 25);
		panel.add(btnDone);
		
	}
	
	public void closeJFrame() {
		super.dispose();
	}
	
}
