package projectTest.testParser;

import project.parser.DirectoryParser;
import project.parser.ParserException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DirectoryParserTest {

    @Test
    void shouldFindSixFilesInDirectory() {
        var src = "src/tests/resources/rental";
        assertAll(
                () -> {
                    assertEquals(6, new DirectoryParser().parseMyFile(src).getSize());
                }
        );
    }

    @Test
    void shouldThrowNullPointerExceptionWhenNameIsNull(){
        assertThrows(NullPointerException.class, () -> {
            new DirectoryParser().parseMyFile(null);
        });
    }

    /*@Test
    void shouldThrowIllegalArgumentExceptionWhenDirectoryIsEmpty(){
        assertThrows(NullPointerException.class, () -> {
            new DirectoryParser().parseMyFile("src/tests/resources/EmptyDirectory");
        });
    }*/

    @Test
    void shouldThrowParserExceptionWhenIsNotDirectory(){
        assertThrows(ParserException.class, () -> {
           new DirectoryParser().parseMyFile("j13.class");
        });
    }
}