package com.lenkp.asteriskmonitor.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import com.lenkp.asteriskmonitor.service.AsteriskManager;
import com.lenkp.asteriskmonitor.service.ServiceLocator;

import ch.loway.oss.ari4java.generated.Bridge;
import ch.loway.oss.ari4java.generated.Channel;
import ch.loway.oss.ari4java.generated.Endpoint;

public class Main {
	// FIXME (Bagus) Clean code
	private final static short MAIN_FRAME_X = 100;
	private final static short MAIN_FRAME_Y = 100;
	private final static short MAIN_FRAME_WIDTH = 890;
	private final static short MAIN_FRAME_HEIGHT = 576;
	
	private final static short TABBED_PANE_X = 12;
	private final static short TABBED_PANE_Y = 57;
	private final static short TABBED_PANE_WIDTH = 860;
	private final static short TABBED_PANE_HEIGHT = 480;

	protected static JFrame mainFrame;
	private static JTable primaryTable;
	private static JTable secondaryTable;
	private static JPanel panel;

	public DefaultTableModel resultTable = null;
	public static String stringSelectedBridge = null;
	public static DefaultTableModel bridgesChannelsModel;
	public static int rowSelected = -1;
	public static JTable tableSelected;
	public static int previousBridgeTotal = 0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try 
		{
			UIManager.setLookAndFeel(
					"com.sun.java.swing.plaf.gtk.GTKLookAndFeel"); 
		}
		catch(Exception e){ 
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					initialiseMainFrame();
					mainFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings("serial")
	protected static void initialiseMainFrame() {
		readAsteriskConfigProperties();
		
		mainFrame = new JFrame();
		mainFrame.setBounds(MAIN_FRAME_X, MAIN_FRAME_Y, MAIN_FRAME_WIDTH, MAIN_FRAME_HEIGHT);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.getContentPane().setLayout(null);
		
		initialiseMenuBar();
		
		initialiseSettingButton();
		
		/***Tab section***/
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(TABBED_PANE_X, TABBED_PANE_Y, TABBED_PANE_WIDTH, TABBED_PANE_HEIGHT);
		mainFrame.getContentPane().add(tabbedPane);
		
		
		//===== First Tab =====//
		panel = new JPanel();
		tabbedPane.addTab("Endpoint", null, panel, null);
		panel.setLayout(null);
		panel.setBackground(Color.WHITE);
		
		JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
		separator.setBounds(615, 15, 5, 420);
		separator.setBackground(Color.BLUE);
		separator.setForeground(Color.BLUE);
		
		JLabel sipLabel = new JLabel();
        sipLabel.setBounds(625, 15, 400, 30);
        
        JLabel hostSipLabel = new JLabel();
        hostSipLabel.setBounds(625, 50, 400, 30);
        
        JButton button = new JButton();
		button.setBounds(625, 90, 100, 30);
		button.setBackground(Color.BLACK);
		button.setVisible(false);
		
		DefaultTableModel tableModelEndPoints = new DefaultTableModel(new String[] { "Name","Host","State","Action"},0);
		DefaultTableModel tableModelBridges = new DefaultTableModel(new String[] { "Name", "Action" },0);
		DefaultTableModel bridgesChannelsModel = new DefaultTableModel(new String[] { "ID","Name","State","Action"},0);
		
		List<DefaultTableModel> bridgesChannelsModelList = new ArrayList<DefaultTableModel>();
		
		Integer[] previousbridgesChannelElementTotal = {0};
		
		primaryTable = new JTable(){
		    @Override
		    public boolean isCellEditable(int row, int column) {
		        return this.getColumnName(column).equals("Action") ? true : false;
		    }
		};
		
        primaryTable.setRowHeight(25);
        
		JScrollPane scrollPane = new JScrollPane(primaryTable);
		scrollPane.setBounds(12, 12, 600, 420);
		
		panel.add(scrollPane);
		panel.add(sipLabel);
		panel.add(hostSipLabel);
		panel.add(button);
		panel.add(separator);
		
		//===== Second Tab =====//
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Bridge", null, panel_1, null);
		panel_1.setLayout(null);
		panel_1.setBackground(Color.WHITE);
		//First Panel
		secondaryTable = new JTable();
		secondaryTable.setRowHeight(25);
		
		JTable bridgeChannelsTable =  new JTable(bridgesChannelsModel);
		bridgeChannelsTable.setRowHeight(25);
		
		JScrollPane scrollPane_2 = new JScrollPane(bridgeChannelsTable);
        scrollPane_2.setBounds(295, 12, 550, 420);
		scrollPane_2.setVisible(true);
		
		JScrollPane scrollPane_1 = new JScrollPane(secondaryTable);
		scrollPane_1.setBounds(12, 12, 270, 420);
		scrollPane_1.setVisible(true);
		
		panel_1.add(scrollPane_1);
		panel_1.add(scrollPane_2);
		
		Timer timer = new Timer(100, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//start data table listener 
				tableModelEndPoints.setRowCount(0);
				tableModelBridges.setRowCount(0);
				
				int indexListElement = 0;
				
				for (Endpoint endpoint : ServiceLocator.getEndpointService().getEndpoints()) {
					tableModelEndPoints.addRow(new Object[]{endpoint.getResource(),endpoint.getTechnology(),endpoint.getState(),"Info"});
				}
				
				bridgesChannelsModelList.clear();
		
				if (ServiceLocator.getBridgeService().getBridges().size() > 0) {
					previousBridgeTotal = ServiceLocator.getBridgeService().getBridges().size();
					for (Bridge bridge : ServiceLocator.getBridgeService().getBridges()) {
						tableModelBridges.addRow(new Object[]{bridge.getId(), "Show channel"});
						
						bridgesChannelsModelList.add(new DefaultTableModel(new String[] { "Bridge's ID","Channel's ID","SIP","Action"},0));
						bridgesChannelsModelList.get(indexListElement).setRowCount(0);
						
						for (String channelId : bridge.getChannels()) {
							for (Channel channel : ServiceLocator.getChannelService().getChannels()) {
								if (channel.getId().equals(channelId)) {
									bridgesChannelsModelList.get(indexListElement).addRow(new Object[]{bridge.getId(),channel.getId(),channel.getName(),"kick"});
								}
							}
						}
						
					}
				}
				
				if (previousBridgeTotal > 0 && ServiceLocator.getBridgeService().getBridges().size() == 0) {
					int rows = bridgeChannelsTable.getRowCount();
					for(int i=0;i<rows;i++)
						((DefaultTableModel)bridgeChannelsTable.getModel()).removeRow(i);
					rowSelected = -1;
				}
				
				//insert data Ari SIP into table
				primaryTable.setModel(tableModelEndPoints);
				primaryTable.getColumnModel().getColumn(2).setCellRenderer(new StatusColumnCellRenderer());
				
				Action getSIPInfo = new AbstractAction()
		        {
		            public void actionPerformed(ActionEvent e)
		            {
		                JTable table = (JTable)e.getSource();
		                int modelRow = Integer.valueOf( e.getActionCommand() );
		                System.out.println(table.getModel().getValueAt(modelRow, 0).toString());
		                
		        		sipLabel.setText("SIP : " + table.getModel().getValueAt(modelRow, 0).toString());
		        		
		        		sipLabel.setFont(new Font("Sans", Font.BOLD, 12));
		        		
		        		hostSipLabel.setText("Host : " + table.getModel().getValueAt(modelRow, 1).toString());
		        		
		        		hostSipLabel.setFont(new Font("Sans", Font.BOLD, 12));
		        		
		        		button.setText("Edit");
		        		button.setVisible(true);
		            }
		        };
		        new ButtonColumn(primaryTable, getSIPInfo, 3);
		        
		        secondaryTable.setModel(tableModelBridges);
		        
		        Action getBridgeChannels = new AbstractAction()
		        {
		            public void actionPerformed(ActionEvent e)
		            {
		                rowSelected = Integer.valueOf( e.getActionCommand());
		                tableSelected = (JTable)e.getSource();
		            }          
		          		            
		        };
		        
		        getBridgeChannels.setEnabled(true);
		        new ButtonColumn(secondaryTable, getBridgeChannels, 1);
		        
		        if (rowSelected > -1) {
		        	if (!previousbridgesChannelElementTotal[rowSelected].equals(bridgesChannelsModelList.get(rowSelected).getRowCount())) {
		        		for (int i=0; i < bridgesChannelsModelList.size(); i++) {
		        			if (bridgesChannelsModelList.get(rowSelected).getRowCount() > 0 && tableSelected.getRowCount() > 0) {
		        				if (tableSelected .getValueAt(rowSelected, 0).toString().equals(bridgesChannelsModelList.get(i).getValueAt(0, 0).toString())) {
									bridgeChannelsTable.setModel(bridgesChannelsModelList.get(i));
								}
		        			}
						}
		        		previousbridgesChannelElementTotal[rowSelected] = bridgesChannelsModelList.get(rowSelected).getRowCount();
		        	}
		        	
		        	Action kickBridgesChannel = new AbstractAction()
	                {
	                    public void actionPerformed(ActionEvent e)
	                    {
	                        JTable table = (JTable)e.getSource();
	                        int modelRow = Integer.valueOf( e.getActionCommand() );
	                        ServiceLocator.getChannelService().hangUpChannel(table.getValueAt(modelRow, 1).toString());
	                    }
	                };
	                new ButtonColumn(bridgeChannelsTable, kickBridgesChannel, 3);
		        }
			}
		});
		
		timer.start();
	}

	private static void initialiseMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		
		menuBar.setBounds(0, 0, 890, 25);
		
		JMenu mnFile = new JMenu("File");
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mainFrame.dispose();
			}
		});
		
		mnFile.add(mntmExit);
		menuBar.add(mnFile);
		
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);
		
		mainFrame.getContentPane().add(menuBar);
	}

	private static void initialiseSettingButton() {
		JButton settingButton = new JButton("Setting");
		
		settingButton.setBounds(12, 27, 117, 28);
		settingButton.setVisible(true);
		
		settingButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							mainFrame.dispose();
							
							AsteriskConnectionSettings settingFrame = new AsteriskConnectionSettings();
							
							settingFrame.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
		
		mainFrame.getContentPane().add(settingButton);
	}

	private static void readAsteriskConfigProperties() {
		Properties prop = new Properties();
		InputStream input = null;

		try {
			input = new FileInputStream("config.properties");

			// Load a properties file
			prop.load(input);

			// Get the property value
			AsteriskManager.createConnection(
					prop.getProperty("host"),
					prop.getProperty("port"),
					prop.getProperty("username"),
					prop.getProperty("password"));
		} catch (IOException ex) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						mainFrame.dispose();
						
						AsteriskConnectionSettings settingFrame = new AsteriskConnectionSettings();
						
						settingFrame.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
// FIXME (Bagus) One class one file
@SuppressWarnings("serial")
class StatusColumnCellRenderer extends DefaultTableCellRenderer {
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
	
		Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col); 
		
		if(table.getModel().getValueAt(row, 2).toString().equals("online"))
			c.setBackground(Color.GREEN);
		else
			c.setBackground(Color.RED);
	
		// Return the Component which renders the cell.
		return c;
	}
  
}
//FIXME (Bagus) One class one file
@SuppressWarnings("serial")
class ButtonColumn extends AbstractCellEditor implements TableCellRenderer, TableCellEditor, ActionListener, MouseListener {
	
	private JTable table;
	private Action action;
	private int mnemonic;
	private Border originalBorder;
	private Border focusBorder;

	private JButton renderButton;
	private JButton editButton;
	private Object editorValue;
	private boolean isButtonColumnEditor;

	/**
	 *  Create the ButtonColumn to be used as a renderer and editor. The
	 *  renderer and editor will automatically be installed on the TableColumn
	 *  of the specified column.
	 *
	 *  @param table the table containing the button renderer/editor
	 *  @param action the Action to be invoked when the button is invoked
	 *  @param column the column to which the button renderer/editor is added
	 */
	public ButtonColumn(JTable table, Action action, int column) {
		this.table = table;
		this.action = action;

		renderButton = new JButton();
		editButton = new JButton();
		editButton.setFocusPainted(false);
		editButton.addActionListener(this);
		originalBorder = editButton.getBorder();
		setFocusBorder(new LineBorder(Color.BLUE));

		TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(column).setCellRenderer(this);
		columnModel.getColumn(column).setCellEditor( this );
		table.addMouseListener(this);
	}


	/**
	 *  Get foreground color of the button when the cell has focus
	 *
	 *  @return the foreground color
	 */
	public Border getFocusBorder() {
		return focusBorder;
	}

	/**
	 *  The foreground color of the button when the cell has focus
	 *
	 *  @param focusBorder the foreground color
	 */
	public void setFocusBorder(Border focusBorder) {
		this.focusBorder = focusBorder;
		editButton.setBorder(focusBorder);
	}

	public int getMnemonic() {
		return mnemonic;
	}

	/**
	 *  The mnemonic to activate the button when the cell has focus
	 *
	 *  @param mnemonic the mnemonic
	 */
	public void setMnemonic(int mnemonic) {
		this.mnemonic = mnemonic;
		renderButton.setMnemonic(mnemonic);
		editButton.setMnemonic(mnemonic);
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {   
		if (value == null) {
			editButton.setText("");
			editButton.setIcon(null);
		}
		else if (value instanceof Icon) {
			editButton.setText("");
			editButton.setIcon((Icon)value);
		}
		else {
			editButton.setText(value.toString());
			editButton.setIcon(null);
		}

		this.editorValue = value;
		return editButton;
	}

	@Override
	public Object getCellEditorValue() {
		return editorValue;
	}

//
//  Implement TableCellRenderer interface
//
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {   
		if (isSelected) {
			renderButton.setForeground(table.getSelectionForeground());
	 		renderButton.setBackground(table.getSelectionBackground());
		}
		else {
			renderButton.setForeground(table.getForeground());
			renderButton.setBackground(UIManager.getColor("Button.background"));
		}

		if (hasFocus) {
			renderButton.setBorder(focusBorder);
		}
		else {
			renderButton.setBorder(originalBorder);
		}
		
		if (value == null) {
			renderButton.setText("");
			renderButton.setIcon(null);
		}
		else if (value instanceof Icon) {
			renderButton.setText("");
			renderButton.setIcon((Icon)value);
		}
		else {
			renderButton.setText(value.toString());
			renderButton.setIcon(null);
		}

		return renderButton;
	}

//
//  Implement ActionListener interface
//
	/*
	 *	The button has been pressed. Stop editing and invoke the custom Action
	 */
	public void actionPerformed(ActionEvent e) {
		int row = table.convertRowIndexToModel(table.getEditingRow());
		fireEditingStopped();

		//  Invoke the Action

		ActionEvent event = new ActionEvent(
			table,
			ActionEvent.ACTION_PERFORMED,
			"" + row);
		action.actionPerformed(event);
	}

//
//  Implement MouseListener interface
//
	/*
	 *  When the mouse is pressed the editor is invoked. If you then then drag
	 *  the mouse to another cell before releasing it, the editor is still
	 *  active. Make sure editing is stopped when the mouse is released.
	 */
    public void mousePressed(MouseEvent e) {
    	if (table.isEditing()
		&&  table.getCellEditor() == this)
			isButtonColumnEditor = true;
    }

    public void mouseReleased(MouseEvent e) {
    	if (isButtonColumnEditor
    	&&  table.isEditing())
    		table.getCellEditor().stopCellEditing();

		isButtonColumnEditor = false;
    }

    public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    
}