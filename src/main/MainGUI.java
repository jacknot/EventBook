package main;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.border.BevelBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;

public class MainGUI {
	
	private static final String NEW_LINE = "\n";
	private static final String WELCOME = "Welcome to EventBook";
	private static final String WAITING = "> ";

	private static CommandsHandler handler;
	private JFrame frame;
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
		frame.setBounds(100, 100, 550, 350);
		frame.setTitle("EventBook");
		frame.setAlwaysOnTop(true);
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
		
		handler.setOut( new PrintStream(new TextAreaOutputStream(textArea)));		
		
		handler.load();

		Thread t = new Thread(() -> {
			Scanner in = new Scanner(System.in);	
			do {
				String command = in.nextLine().trim();
				System.out.println(command);
				handler.run(command);
			}while(true);
			
		});
		t.start();
	}
	
	class TextAreaOutputStream extends OutputStream {
		
	    private JTextArea textArea;

	    public TextAreaOutputStream(JTextArea textArea) {
	        this.textArea = textArea;
	    }

	    @Override
	    public void write(int b) throws IOException {
	        // redirects data to the text area
	    	textArea.setText(textArea.getText() + String.valueOf((char)b));
	        // scrolls the text area to the end of data
	        textArea.setCaretPosition(textArea.getDocument().getLength());
	        // keeps the textArea up to date
	        textArea.update(textArea.getGraphics());
	    }
	}


}
