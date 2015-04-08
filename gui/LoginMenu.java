package com.chatroom.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.GridLayout;

import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.Dimension;

import javax.swing.JLabel;

import com.chatroom.client.ChatClient;

import java.awt.FlowLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.UnknownHostException;

public class LoginMenu extends JFrame {

	private LoginMenu that = this;
	private JPanel contentPane;
	private JTextField nickInput;
	private ChatClient client;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginMenu frame = new LoginMenu();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LoginMenu() {
		
		try {
			client = new ChatClient();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(that, "Unknown host exception");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(that, "IOException");
			e.printStackTrace();
		}
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(3, 1, 0, 0));
		
		
		JPanel upperEmptyPanel = new JPanel();
		contentPane.add(upperEmptyPanel);
		
		
		
		JPanel loginPane = new JPanel();
		contentPane.add(loginPane);
		loginPane.setLayout(new GridLayout(2, 1, 0, 0));
		
		
		
		JPanel loginTextPanel = new JPanel();
		loginTextPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		FlowLayout flowLayout = (FlowLayout) loginTextPanel.getLayout();
		flowLayout.setVgap(15);
		flowLayout.setAlignment(FlowLayout.LEFT);
		loginPane.add(loginTextPanel);
		
		
		
		JLabel nicknameLabel = new JLabel("Please enter your nickname");
		loginTextPanel.add(nicknameLabel);
		
		
		
		JPanel LoginDataPane = new JPanel();
		loginPane.add(LoginDataPane);
		LoginDataPane.setLayout(new GridLayout(0, 2, 0, 0));
		
		
		
		nickInput = new JTextField();
		LoginDataPane.add(nickInput);
		nickInput.setColumns(10);
		addEnterListener();
		
		
		
		JButton btnLogin = new JButton("Login");
		addLoginListener(btnLogin);
		btnLogin.setMinimumSize(new Dimension(50, 12));
		btnLogin.setMaximumSize(new Dimension(50, 12));
		LoginDataPane.add(btnLogin);
		
	
		
	}
	
	
	public void addEnterListener() {
		nickInput.addKeyListener(
	            new KeyListener(){

	                public void keyPressed(KeyEvent e){
	                	// Ignoring, waiting for key to be released.
	                }

					@Override
					public void keyReleased(KeyEvent e) {
						 if(e.getKeyChar() == KeyEvent.VK_ENTER){
							 if(nickInput.getText().length() > 0) {
									
									try {
										if(client.login(nickInput.getText())) {
											ChatFrame chat = new ChatFrame(client, that);
											chat.setVisible(true);
											that.setVisible(false);
										}
									} catch (IOException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									
								} else {
									JOptionPane.showMessageDialog(that, "Nickname is empty!");
								}
							
						 }   
						
					}

					@Override
					public void keyTyped(KeyEvent e) {
						// Ignoring
					}
	            }
	        );
	}
	
	public void addLoginListener(JButton btn) {
		btn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(nickInput.getText().length() > 0) {
					
					try {
						if(client.login(nickInput.getText())) {
							ChatFrame chat = new ChatFrame(client, that);
							chat.setVisible(true);
							that.setVisible(false);
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				} else {
					JOptionPane.showMessageDialog(that, "Nickname is empty!");
				}
			}
		});
	}
	
	
	

}
