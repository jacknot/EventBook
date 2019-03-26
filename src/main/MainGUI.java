package main;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;

public class MainGUI {

	private JFrame frame;
	private JTextField textFieldCommands;
	private JTextArea textArea;

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
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panelCommands = new JPanel();
		frame.getContentPane().add(panelCommands, BorderLayout.SOUTH);
		panelCommands.setLayout(new BorderLayout(0, 0));
		
		textFieldCommands = new JTextField();
		panelCommands.add(textFieldCommands);
		textFieldCommands.setColumns(10);
		
		textArea = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(textArea);
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		
	}

}
