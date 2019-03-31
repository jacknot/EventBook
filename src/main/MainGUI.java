package main;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;

import command.CommandHandler;
import command.InOutStream;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;

import javax.swing.AbstractAction;
import javax.swing.JButton;

import java.awt.event.ActionEvent;

public class MainGUI {
	
	private static CommandHandler handler;
	private JFrame frame;
	private JTextArea textArea;
	private JTextField textFieldCommands;
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
		handler = CommandHandler.getInstance(new GUIStream());	
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 815, 564);
		frame.setTitle("EventBook");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
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
		
		btnSend = new JButton("Invia");
		panelCommands.add(btnSend, BorderLayout.EAST);
		
		textFieldCommands = new JTextField();
		textFieldCommands.addActionListener(new AbstractAction() {
		    @Override
		    public void actionPerformed(ActionEvent e)
		    {
				String command = textFieldCommands.getText().trim();
				if(command != null) {
					textArea.append(command + "\n");
					handler.run(command);
				}
				textFieldCommands.setText("");
				textFieldCommands.requestFocus();
		    }
		});
		panelCommands.add(textFieldCommands);
		textFieldCommands.setColumns(10);
		
		btnSend.addActionListener(event -> {
			String command = textFieldCommands.getText().trim();
			textArea.append(command + "\n");
			handler.run(command);
			textFieldCommands.setText("");
			textFieldCommands.requestFocus();
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
