package projectTest.testParser;

import com.project.parser.DirectoryParser;
import com.project.parser.ParserFactory;
import com.project.parser.FileParser;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class FileParserTest {

    @Test
    void shouldReturnADirectoryWhenADirectoryIsPassedEvenIfHisNameIsJar(){
        assertAll(
                () -> {
                    assertTrue(ParserFactory.createParser(Paths.get("src/tests/resources/test.jar")) instanceof DirectoryParser);
                },
                () -> {
                    assertTrue(ParserFactory.createParser(Paths.get("src/tests/resources/test.class")) instanceof DirectoryParser);
                }
        );
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenFileIsNotSupported(){
        assertAll(
                () -> {
                    assertThrows(IllegalArgumentException.class, () -> ParserFactory.createParser(Paths.get("src/tests/resources/rental.zip")));
                },
                () -> {
                    assertThrows(IllegalArgumentException.class, () -> ParserFactory.createParser(Paths.get("src/tests/resources/lol.docx")));
                },
                () -> {
                    assertThrows(IllegalArgumentException.class, () -> ParserFactory.createParser(Paths.get("src/tests/resources/noAClassFile.txt")));
                }
        );
    }
}