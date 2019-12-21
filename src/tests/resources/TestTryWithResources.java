import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TestTryWithResources {

	static String readFirstLineFromFile(String path) throws IOException {
		BufferedReader br = null;
	    try{
	    	br = new BufferedReader(new FileReader(path));
	        return br.readLine();
	    }finally {
	    	if(br != null) {
	    		br.close();
	    	}
	    }
	}

}
