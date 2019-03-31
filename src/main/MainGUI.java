package main;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JButton;

public class MainGUI {
	
	private static final String WELCOME = "Welcome to EventBook";

	private static CommandsHandler handler;
	private JFrame frame;
	private JTextArea textArea;
	private JTextField textFieldCommands;
	private JButton btnSend;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainGUI window = new MainGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 550, 350);
		frame.setTitle("EventBook");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panelScroll = new JPanel();
		panelScroll.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		frame.getContentPane().add(panelScroll, BorderLayout.CENTER);
		panelScroll.setLayout(new BorderLayout(0, 0));
		
		textArea = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(textArea);
		panelScroll.add(scrollPane);
		
		JPanel panelWelcome = new JPanel();
		frame.getContentPane().add(panelWelcome, BorderLayout.NORTH);
		
		JLabel lblWelcome = new JLabel(WELCOME);
		lblWelcome.setFont(new Font("Tahoma", Font.BOLD, 18));
		panelWelcome.add(lblWelcome);
		
		handler = CommandsHandler.getInstance();	
		
		JPanel panelCommands = new JPanel();
		panelCommands.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		frame.getContentPane().add(panelCommands, BorderLayout.SOUTH);
		panelCommands.setLayout(new BorderLayout(0, 0));
		
		btnSend = new JButton("Invia");
		panelCommands.add(btnSend, BorderLayout.EAST);
		
		textFieldCommands = new JTextField();
		panelCommands.add(textFieldCommands);
		textFieldCommands.setColumns(10);
		
		handler.setStream(new GUIStream());
		
		handler.load();

		btnSend.addActionListener(event -> {
			String command = textFieldCommands.getText().trim();
			textArea.append(command + "\n");
			handler.run(command);
			textFieldCommands.setText("");
			textFieldCommands.requestFocus();
		});
		
		
	}
	
	 class GUIStream implements InOutStream{
					
			@Override
			public String read() {
				String input = JOptionPane.showInputDialog(frame, "Inserisci valore: (annullando il campo non verrà compilato)");
				if(input==null)
					input = "";
				else write(input);
				return input;
			}

			@Override
			public void write(String str) {
			textArea.append(str);
				
			}

			@Override
			public void writeln(String str) {
				textArea.append(str + "\n");			
			}

			@Override
			public void close() {
				// TODO Auto-generated method stub		
			}
			
		}

}
