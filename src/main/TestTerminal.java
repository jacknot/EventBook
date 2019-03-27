package main;

import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import main.MainGUI.TextAreaOutputStream;

//TODO: Keep a global StringBuilder to decrease memory footprint

public class TestTerminal {
	
	private static final String NEW_LINE = "\n";
	private static final String WELCOME = "Welcome to EventBook";



	
    public static void main(String[] args) {
    	System.out.println(WELCOME + NEW_LINE);
	
        Terminal term = Terminal.getInstance();
        term.open(0, 0, 700, 700);
    }
}

class Terminal {
    private JFrame frm = new JFrame("Terminal");
    private JTextArea txtArea = new JTextArea();
    private JScrollPane scrollPane = new JScrollPane();
    private CommandProcessor processor = CommandProcessor.getInstance(txtArea);
    private final String LINE_SEPARATOR = System.lineSeparator();
    private Font font = new Font("SansSerif", Font.BOLD, 15);

    private Terminal() {

		
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frm.getContentPane().add(scrollPane);
        scrollPane.setViewportView(txtArea);
        txtArea.addKeyListener(new KeyListener());
        txtArea.setFont(font);
        disableArrowKeys(txtArea.getInputMap());
    }

    private void disableArrowKeys(InputMap inputMap) {
        String[] keystrokeNames = { "UP", "DOWN", "LEFT", "RIGHT", "HOME" };
        for (int i = 0; i < keystrokeNames.length; ++i)
            inputMap.put(KeyStroke.getKeyStroke(keystrokeNames[i]), "none");
    }

    public void open(final int xLocation, final int yLocation, final int width,
            final int height) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                frm.setBounds(xLocation, yLocation, width, height);
                frm.setVisible(true);
                showPrompt();
            }
        });
    }

    public void close() {
        frm.dispose();
    }

    public void clear() {
        txtArea.setText("");
        showPrompt();
    }

    private void showPrompt() {
        txtArea.setText(txtArea.getText() + "> ");
    }

    private void showNewLine() {
        txtArea.setText(txtArea.getText() + LINE_SEPARATOR);
    }

    private class KeyListener extends KeyAdapter {
        private final int ENTER_KEY = KeyEvent.VK_ENTER;
        private final int BACK_SPACE_KEY = KeyEvent.VK_BACK_SPACE;
        private final String BACK_SPACE_KEY_BINDING = getKeyBinding(
                txtArea.getInputMap(), "BACK_SPACE");
        private final int INITIAL_CURSOR_POSITION = 2;

        private boolean isKeysDisabled;
        private int minCursorPosition = INITIAL_CURSOR_POSITION;

        private String getKeyBinding(InputMap inputMap, String name) {
            return (String) inputMap.get(KeyStroke.getKeyStroke(name));
        }

        public void keyPressed(KeyEvent evt) {
            int keyCode = evt.getKeyCode();
            if (keyCode == BACK_SPACE_KEY) {
                int cursorPosition = txtArea.getCaretPosition();
                if (cursorPosition == minCursorPosition && !isKeysDisabled) {
                    disableBackspaceKey();
                } else if (cursorPosition > minCursorPosition && isKeysDisabled) {
                    enableBackspaceKey();
                }
            } else if (keyCode == ENTER_KEY) {
                disableTerminal();
                String command = extractCommand();
                executeCommand(command);
                showNewLine();
                showPrompt();
                enableTerminal();
            }
        }

        public void keyReleased(KeyEvent evt) {
            int keyCode = evt.getKeyCode();
            if (keyCode == ENTER_KEY) {
                txtArea.setCaretPosition(txtArea.getCaretPosition() - 1);
                setMinCursorPosition();
            }
        }

        private void disableBackspaceKey() {
            isKeysDisabled = true;
            txtArea.getInputMap().put(KeyStroke.getKeyStroke("BACK_SPACE"),
                    "none");
        }

        private void enableBackspaceKey() {
            isKeysDisabled = false;
            txtArea.getInputMap().put(KeyStroke.getKeyStroke("BACK_SPACE"),
                    BACK_SPACE_KEY_BINDING);
        }

        private void setMinCursorPosition() {
            minCursorPosition = txtArea.getCaretPosition();
        }
    }

    public void enableTerminal() {
        txtArea.setEnabled(true);
    }

    public void disableTerminal() {
        txtArea.setEnabled(false);
    }

    private void executeCommand(String command) {
        processor.processCmd(command);
    }

    private String extractCommand() {
        removeLastLineSeparator();
        String newCommand = stripPreviousCommands();
        return newCommand;
    }

    private void removeLastLineSeparator() {
        String terminalText = txtArea.getText();
        terminalText = terminalText.substring(0, terminalText.length() - 1);
        txtArea.setText(terminalText);
    }

    private String stripPreviousCommands() {
        String terminalText = txtArea.getText();
        int lastPromptIndex = terminalText.lastIndexOf('>') + 2;
        if (lastPromptIndex < 0 || lastPromptIndex >= terminalText.length())
            return "";
        else
            return terminalText.substring(lastPromptIndex);
    }

    public static Terminal getInstance() {
        return TerminalHolder.INSTANCE;
    }

    private static final class TerminalHolder {
        static final Terminal INSTANCE = new Terminal();
    }
}

class CommandProcessor {
	private static CommandsHandler handler;
	private static CommandProcessor instance;
	
    private CommandProcessor(JTextArea txtArea) {
    	handler = CommandsHandler.getInstance();
    	handler.setStringRedirector(new StringRedirector(){

			@Override
			public PrintStream getOut() {
				return new PrintStream(new TextAreaOutputStream(txtArea));
			}
    	
    	});
		handler.load();
    }

    public void processCmd(String command) {
        handler.run(command);
    }

    public static CommandProcessor getInstance(JTextArea txtArea) {
    	if(instance == null)
    		instance = new CommandProcessor(txtArea);
        return instance;
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