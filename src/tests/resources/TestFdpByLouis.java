import java.io.Closeable;
import java.io.*;
import java.net.*;

public class TestFdpByLouis implements AutoCloseable{

    @Override
    public void close() throws IOException {
        System.out.println("close");
    }

    private static void test1(){
        try(TestFdpByLouis t = new TestFdpByLouis()){
            System.out.println("it's working");
            try(TestFdpByLouis t2 = new TestFdpByLouis()){
                System.out.println("it's working");
            }catch(Throwable e){

            }
        }catch(Throwable e){

        }
    }

}