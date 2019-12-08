package projectTest.testParser;

import com.project.parser.FileFactory;
import com.project.parser.FileParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileParserTest {

    @Test
    void shouldThrowNullPointerExceptionWhenFactoryIsNull(){
        assertThrows(NullPointerException.class, () -> {
           FileParser.parseFile("j13.class", null);
        });
    }

    @Test
    void shouldThrowNullPointerExceptionWhenNameIsNull(){
        assertThrows(NullPointerException.class, () -> {
            FileParser.parseFile(null, new FileFactory());
        });
    }

    @Test
    void shouldThrowNullPointerException(){
        assertThrows(NullPointerException.class, () -> {
            FileParser.parseFile(null, new FileFactory());
        });
    }

}