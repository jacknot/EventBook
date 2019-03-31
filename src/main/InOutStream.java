package main;

interface InOutStream {

	public String read();
	
	public void write(String str);
	
	public void writeln(String str);

	public void close();

}
