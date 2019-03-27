package main;

import java.io.InputStream;
import java.io.PrintStream;

interface StringRedirector {

	//public InputStream getIn();
	
	public PrintStream getOut();
	
}
