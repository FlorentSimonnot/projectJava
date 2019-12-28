import java.lang.*;
import java.net.*; 
import java.io.*;

public class TryWithResourcesMultiple{

	static void test(){
		try (Socket socket = new Socket();
		     InputStream input = new DataInputStream(socket.getInputStream());
		     OutputStream output = new DataOutputStream(socket.getOutputStream());) {
		} catch (IOException e) {
			System.err.println(e.getMessage());
		} 

	}

	public static void main(String[] args){
		new TryWithResourcesMultiple().test();
	}

}
