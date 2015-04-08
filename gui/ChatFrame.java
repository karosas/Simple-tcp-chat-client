package com.chatroom.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.GridLayout;
import java.awt.GridBagLayout;

import javax.swing.JScrollPane;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

import com.chatroom.client.ChatClient;

public class ChatFrame extends JFrame {

	private ChatFrame that = this;
	
	private JPanel contentPane;
	private JTable table;
	private JTextField msgInput;
	
	private ChatClient client;
	private LoginMenu loginMenu;

	/**
	 * Create the frame.
	 */
	public ChatFrame(ChatClient client, LoginMenu menu) {
		this.client = client;
		this.loginMenu = menu;
		addElements();
	}
	/*
	 * Ugly method creating all Swing objects
	 */
	public void addElements() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0};
		gbl_contentPane.rowHeights = new int[]{183, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
	
		DefaultTableModel model = new DefaultTableModel(1,1);
		table = new JTable(model);
		table.setShowGrid(false);
		table.setTableHeader(null);
		JScrollPane scrollPane = new JScrollPane(table);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		contentPane.add(scrollPane, gbc_scrollPane);
		scrollPane.setViewportView(table);
		
		
		JPanel bottomPane = new JPanel();
		GridBagConstraints gbc_bottomPane = new GridBagConstraints();
		gbc_bottomPane.fill = GridBagConstraints.BOTH;
		gbc_bottomPane.gridx = 0;
		gbc_bottomPane.gridy = 1;
		contentPane.add(bottomPane, gbc_bottomPane);
		bottomPane.setLayout(new GridLayout(1, 2, 50, 20));
		
		msgInput = new JTextField();
		bottomPane.add(msgInput);
		msgInput.setColumns(10);
		
		createListenerThread();
		
		JButton btnSend = new JButton("Send");
		addSendListener(btnSend);
		bottomPane.add(btnSend);
		
		/*
		 * Cuts connection with server when quiting the application
		 */
		that.addWindowListener(new java.awt.event.WindowAdapter() {
	        public void windowClosing(WindowEvent winEvt) {
	        	try {
					client.logout();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            System.exit(0);
	        }
	    });
	}
	/*
	 * Create listener for send button
	 */
	public void addSendListener(JButton btn) {
		btn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(msgInput.getText().length() > 0) {
					try {
						client.sendMsg(msgInput.getText());
						msgInput.setText("");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		
		
		msgInput.addKeyListener(
	            new KeyListener(){

	                public void keyPressed(KeyEvent e){
	                	// Ignoring, waiting for key to be released.
	                }

					@Override
					public void keyReleased(KeyEvent e) {
						 if(e.getKeyChar() == KeyEvent.VK_ENTER){
							 try {
								client.sendMsg(msgInput.getText());
								msgInput.setText("");
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
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
	
	
	
	public void receiveMessage(String message) {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.setRowCount(model.getRowCount()+1);
		model.insertRow(table.getRowCount()-1, new Object[] { message } );
		model.fireTableRowsInserted(0, 1);
		//table.scrollRectToVisible(table.getCellRect(table.getRowCount()+1, table.getColumnCount()+1, true));
		scrollToVisible(table, table.getRowCount(), table.getColumnCount());
	}
	
	public void scrollToVisible(final JTable table, final int rowIndex, final int vColIndex) {
	    SwingUtilities.invokeLater(new Runnable() {
	        @Override
	        public void run() {
	            table.scrollRectToVisible(table.getCellRect(rowIndex, vColIndex, false));
	        }
	    });
	}
	
	public void createListenerThread() {
		
	Thread thread = new Thread(){
	    public void run(){
	    	 BufferedReader inFromServer = null;

	    	  try{
	    	  inFromServer = new BufferedReader(new InputStreamReader(client.getSocket().getInputStream()));
	    	  
	    	  while(true) {
	    		  String data =inFromServer.readLine();
	    		  if(!data.isEmpty() && !data.equals("SERVER: OK")) {
	    	    		receiveMessage(data);
	    	    		System.out.println("RECEIVED: " + data);
	    	    	}
	    	  }
	    	  

	    	  }
	    	  catch (Exception e){
	    		  System.out.println("Error in receiving message");
	    	  }
	    }
	  };

	  thread.start();
	}
	
	
	

}
