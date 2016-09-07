package com.lenkp.asteriskmonitor.gui;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.lenkp.asteriskmonitor.service.ServiceLocator;

@SuppressWarnings("serial")
public class EditPTUFrame extends JFrame {

	private JPanel contentPane;

	public EditPTUFrame() {
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(120, 180, 300, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, " PTU Info " , TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(60, 30, 160, 350);
		contentPane.add(panel);
		
		((FlowLayout) panel.getLayout()).setAlignment(FlowLayout.LEFT);
		contentPane.add(panel);
		
		JLabel hostLabel = new JLabel("Host");
		hostLabel.setBounds(33, 47, 70, 15);
		panel.add(hostLabel);
		
		JTextField hostTextField = new JTextField();
		hostTextField.setFont(new Font("Dialog", Font.PLAIN, 12));
		hostTextField.setBounds(39, 70, 170, 20);
		hostTextField.setText(ServiceLocator.getRemoteSshService().getPTULinphoneHost());
		panel.add(hostTextField);
		hostTextField.setColumns(10);
		
		JLabel usernameLabel = new JLabel("Username");
		usernameLabel.setBounds(33, 47, 70, 15);
		panel.add(usernameLabel);
		
		JTextField usernameTextField = new JTextField();
		usernameTextField.setFont(new Font("Dialog", Font.PLAIN, 12));
		usernameTextField.setBounds(39, 100, 170, 20);
		usernameTextField.setText(ServiceLocator.getRemoteSshService().getPTULinphoneUsername());
		panel.add(usernameTextField);
		usernameTextField.setColumns(10);
		
		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setBounds(33, 70, 70, 15);
		panel.add(passwordLabel);
		
		JTextField passwordField = new JTextField();
		passwordField.setFont(new Font("Dialog", Font.PLAIN, 12));
		passwordField.setBounds(39, 70, 170, 20);
		passwordField.setText(ServiceLocator.getRemoteSshService().getPTULinphonePassword());
		panel.add(passwordField);
		passwordField.setColumns(10);

		
		JButton btnDone = new JButton("Done");
		btnDone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ServiceLocator.getRemoteSshService().replacePTULinphoneInfo(usernameTextField.getText(), passwordField.getText(), hostTextField.getText());
				closeJFrame();
			}
		});
		btnDone.setBounds(39, 210, 117, 25);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeJFrame();
			}
		});
		
		btnCancel.setBounds(170, 210, 117, 25);
		
		panel.add(btnDone);
		panel.add(btnCancel);
		
	}
	
	public void closeJFrame() {
		super.dispose();
	}
	
}
