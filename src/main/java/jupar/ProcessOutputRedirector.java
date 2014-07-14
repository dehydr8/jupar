package jupar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;

public class ProcessOutputRedirector {
	
	private final Process process;
	private final String prefix;
	public ProcessOutputRedirector(Process process, String prefix) {
		this.process = process;
		this.prefix = prefix;
	}
	
	public void redirect() throws IOException {
		try {
			this.process.waitFor();
			String line;

			BufferedReader error = new BufferedReader(new InputStreamReader(this.process.getErrorStream()));
			while((line = error.readLine()) != null){
			    System.out.println("[JUPAR] " + this.prefix + " >> " + line);
			}
			error.close();

			BufferedReader input = new BufferedReader(new InputStreamReader(this.process.getInputStream()));
			while((line=input.readLine()) != null){
			    System.out.println("[JUPAR] " + this.prefix + " >> " + line);
			}
			input.close();
			OutputStream outputStream = this.process.getOutputStream();
			PrintStream printStream = new PrintStream(outputStream);
			printStream.println();
			printStream.flush();
			printStream.close();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
