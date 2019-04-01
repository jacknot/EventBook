package main;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;

import main.commands.CommandHandler;
import main.commands.CommandsHistory;
import main.commands.InOutStream;
import utility.StringConstant;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;

import javax.swing.JButton;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;

public class MainGUI {
	
	private static CommandHandler handler;
	private JFrame frame;
	private JTextArea textArea;
	private JTextField textFieldCommands;
	private CommandsHistory commandsHistory;

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
		panelCommands.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		frame.getContentPane().add(panelCommands, BorderLayout.SOUTH);
		panelCommands.setLayout(new BorderLayout(0, 0));
		
		textFieldCommands = new JTextField();		
		textFieldCommands.setColumns(10);
		panelCommands.add(textFieldCommands);	
		textFieldCommands.requestFocus();
		
		GUIStream guis = new GUIStream();
		
		handler = CommandHandler.getInstance(guis);	
		
		textFieldCommands.addActionListener(event -> {
				String command = textFieldCommands.getText().trim();
				if(command != null) {
					textArea.append(command + "\n");
					commandsHistory.add(command);
					handler.run(command);
				}
				textFieldCommands.setText("");
				textFieldCommands.requestFocus();
		});
		
		textFieldCommands.addKeyListener(new KeyAdapter() { 

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_UP){
					textFieldCommands.setText(commandsHistory.previousCommand());
				} else if(e.getKeyCode() == KeyEvent.VK_DOWN){
					textFieldCommands.setText(commandsHistory.nextCommand());
				} else if(e.getKeyCode() == KeyEvent.VK_RIGHT){ //Autocompletamento
					String hint = handler.hint(textFieldCommands.getText());
					textFieldCommands.setText(hint);
				}
			}
		
		});
		
		frame.addWindowListener(new WindowAdapter() {
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
				// TODO Auto-generated method stub		
			}
		}

}
