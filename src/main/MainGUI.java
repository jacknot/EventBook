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
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;

public class MainGUI {
	
	private static final String NEW_LINE = "\n";
	private static final String WELCOME = "Welcome to EventBook";

	private static CommandsHandler handler;
	private JFrame frame;
	private JTextField textFieldCommands;
	private JTextArea textArea;
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
		
		JPanel panelCommands = new JPanel();
		panelCommands.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		frame.getContentPane().add(panelCommands, BorderLayout.SOUTH);
		panelCommands.setLayout(new BorderLayout(0, 0));
		
		textFieldCommands = new JTextField();
		panelCommands.add(textFieldCommands);
		textFieldCommands.setColumns(10);
		
		btnSend = new JButton("Invia");
		panelCommands.add(btnSend, BorderLayout.EAST);
		
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
		
		handler.setStringRedirector(new StringRedirector() {

			@Override
			public InputStream getIn() {
				return new TextFieldInputStream(textFieldCommands);
			}

			@Override
			public PrintStream getOut() {
				return new PrintStream(new TextAreaOutputStream(textArea));
			}
		
		});		
		
		handler.load();
		
		btnSend.addActionListener(event -> {
			String command = textFieldCommands.getText().trim();
			textArea.append(command + "\n");
			handler.run(command);
		});
		
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

	class TextFieldInputStream extends InputStream {
		
	    private JTextField textField; 
	    private String str = null;
	    private int pos = 0;
	    private int readValue = 0;

	    public TextFieldInputStream(JTextField textField) {
	        this.textField = textField;
	        btnSend.addActionListener(event -> {
	        	 str = textField.getText() + "\n";
	             pos = 0;
	             textField.setText("");
	             synchronized (this) {
	                 //maybe this should only notify() as multiple threads may
	                 //be waiting for input and they would now race for input
	                 this.notifyAll();
	             }
			});
	    }

	   
		@Override
		public int read() throws IOException {
			StreamWorker worker = new StreamWorker();
			worker.execute();
			return readValue;
		}
		
		class StreamWorker extends SwingWorker<Integer, String>{

			@Override
			protected Integer doInBackground() throws Exception {
				 //test if the available input has reached its end
		        //and the EOS should be returned 
		        if(str != null && pos == str.length()){
		            str =null;
		            //this is supposed to return -1 on "end of stream"
		            //but I'm having a hard time locating the constant
		            return java.io.StreamTokenizer.TT_EOF;
		        }
		        //no input available, block until more is available because that's
		        //the behavior specified in the Javadocs
		        while (str == null || pos >= str.length()) {
		            try {
		                //according to the docs read() should block until new input is available
		                synchronized (this) {
		                    this.wait();
		                }
		            } catch (InterruptedException ex) {
		                ex.printStackTrace();
		            }
		        }
		        //read an additional character, return it and increment the index
		        return (int) str.charAt(pos++);
			}
			
			@Override
			protected void done() {
				try {
					readValue = get();
				} catch (InterruptedException | ExecutionException e) {
					JOptionPane.showMessageDialog(frame, e.getStackTrace());
				}
			}
			
		}
	}
}
