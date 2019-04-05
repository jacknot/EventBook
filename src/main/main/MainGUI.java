package main.main;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;

import main.commands.CommandsHandler;
import main.commands.CommandsHistory;
import main.commands.InOutStream;
import utility.StringConstant;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;


import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.border.TitledBorder;

public class MainGUI {
	
	private static CommandsHandler handler;
	private JFrame frame;
	private JTextArea textArea;
	private JTextField textFieldCommands;
	private CommandsHistory commandsHistory;
	private JButton btnSend;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(()->{
				try {
					MainGUI window = new MainGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
	}
	
	/**
	 * Create the application.
	 * @wbp.parser.entryPoint
	 */
	public MainGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 815, 564);
		frame.setTitle("EventBook");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		commandsHistory = new CommandsHistory();
		
		JPanel panelScroll = new JPanel();
		panelScroll.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		frame.getContentPane().add(panelScroll, BorderLayout.CENTER);
		panelScroll.setLayout(new BorderLayout(0, 0));
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textArea);
		panelScroll.add(scrollPane);
		
		JPanel panelWelcome = new JPanel();
		frame.getContentPane().add(panelWelcome, BorderLayout.NORTH);
		
		JLabel lblWelcome = new JLabel("EventBook");
		lblWelcome.setFont(new Font("Tahoma", Font.BOLD, 18));
		panelWelcome.add(lblWelcome);

		JPanel panelCommands = new JPanel();
		panelCommands.setBorder(new TitledBorder(null, "Comandi", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		frame.getContentPane().add(panelCommands, BorderLayout.SOUTH);
		panelCommands.setLayout(new BorderLayout(0, 0));
		
		textFieldCommands = new JTextField();		
		textFieldCommands.setColumns(10);
		panelCommands.add(textFieldCommands);	
		
		btnSend = new JButton("Invia");
		panelCommands.add(btnSend, BorderLayout.EAST);
		
		GUIStream guis = new GUIStream();
		
		handler = CommandsHandler.getInstance(guis);	
		
		textFieldCommands.addActionListener(event -> sendCommand());
		
		btnSend.addActionListener(event -> sendCommand());
		
		textFieldCommands.addKeyListener(new KeyAdapter() { 

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_UP){
					textFieldCommands.setText(commandsHistory.previousCommand());
				} else if(e.getKeyCode() == KeyEvent.VK_DOWN){
					textFieldCommands.setText(commandsHistory.nextCommand());
				} else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
					String hint = handler.hint(textFieldCommands.getText());
					textFieldCommands.setText(hint);
				} else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
					textFieldCommands.setText(StringConstant.EMPTY_STRING);
				}
			}
		
		});
		
		frame.addWindowListener(new WindowAdapter() { //Intercetta chiusura
			public void windowClosing(WindowEvent e) {
				try {
					handler.close();
					guis.writeln(StringConstant.EXITMSG);
					guis.close();
				} catch (IOException exc) {
					JOptionPane.showConfirmDialog(frame, "Errore", exc.toString(), JOptionPane.WARNING_MESSAGE);
				}			
			}			  
		});
		
		textFieldCommands.requestFocus();
	}
	
	private void sendCommand() {
		String command = textFieldCommands.getText().trim();
		if(command != null) {
			textArea.append(command + "\n");
			commandsHistory.add(command);
			handler.run(command);
		}
		textFieldCommands.setText("");
		textFieldCommands.requestFocus();
	}
	
	 class GUIStream implements InOutStream{
					
			/* (non-Javadoc)
			 * @see command.InOutStream#read(java.lang.String)
			 */
			@Override
			public String read(String str) {
				String input = JOptionPane.showInputDialog(frame, str);
				if(input == null)
					input = "";
				else write(input);
				return input;
			}

			/* (non-Javadoc)
			 * @see command.InOutStream#write(java.lang.String)
			 */
			@Override
			public void write(String str) {
			textArea.append(str);
				
			}

			/* (non-Javadoc)
			 * @see command.InOutStream#writeln(java.lang.String)
			 */
			@Override
			public void writeln(String str) {
				textArea.append(str + "\n");			
			}

			/* (non-Javadoc)
			 * @see command.InOutStream#close()
			 */
			@Override
			public void close() {
				textFieldCommands.setText("");
			}
		}

}