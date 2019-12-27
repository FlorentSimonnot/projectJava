import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TestTryWithResources {

	static String readFirstLineFromFile(String path) throws IOException {
	    try(var br = new BufferedReader(new FileReader(path))){
	        return br.readLine();
	    }catch(IOException e){
		throw e;
	    }
	}

	static String readFirstLineFromFile12(String path) throws IOException {
	    var sb = new StringBuilder();
	    try(var br = new BufferedReader(new FileReader(path))){
	        sb.append(br.readLine());
		BufferedReader br2 = null;
		try{
			br2 = new BufferedReader(new FileReader(path));
			sb.append(br2.readLine());
		 	try(var br3 = new BufferedReader(new FileReader(path))){
				sb.append(br3.readLine());
				return sb.toString();			

			}catch(IOException e){
				throw e;
			}
		}catch(IOException e){
			throw e;
		}
	    }catch(IOException e){
		throw e;
	    }
	}

	static String readFirstLineFromFile2(String path) throws IOException {
	    BufferedReader br = null;
	    try{
		br = new BufferedReader(new FileReader(path));
	        return br.readLine();
	    }catch(IOException e){
		throw e;
	    }
	    finally{
		br.close();
		}
	}

	public static void main(String[] args) throws IOException{
		System.out.println(new TestTryWithResources().readFirstLineFromFile("src/tests/resources/TestTryWithResources.java"));
	}

}
