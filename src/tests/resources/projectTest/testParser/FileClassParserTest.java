package projectTest.testParser;

import com.project.parser.FileClassParser;
import com.project.parser.ParserException;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class FileClassParserTest {

    @Test
    void shouldReturnOneFile(){
        assertAll (
                () -> assertEquals(1, new FileClassParser().parseMyFile("src/tests/resources/j13.class").getSize())
        );
    }

    @Test
    void shouldThrowNullPointerExceptionWhenFileIsNull(){
        assertThrows(NullPointerException.class, () -> {
           new FileClassParser().parseMyFile(null);
        });
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenFileIsNotAClassFile(){
        assertThrows(ParserException.class, () -> {
            new FileClassParser().parseMyFile("notAClassFile.txt");
        });
    }
}