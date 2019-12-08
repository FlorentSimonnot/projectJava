package projectTest.testParser;

import com.project.parser.DirectoryParser;
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
    void shouldThrowIllegalArgumentExceptionWhenIsNotDirectory(){
        assertThrows(IllegalArgumentException.class, () -> {
           new DirectoryParser().parseMyFile("j13.class");
        });
    }
}