package projectTest.testFiles;

import fr.retro.parser.FileClass;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileClassTest {

    @Test
    void shouldThrowNullPointerExceptionWhenNameFileIsNull(){
        assertThrows(NullPointerException.class, () -> {
           new FileClass(null);
        });
    }

    @Test
    void testToStringMethod(){
        assertEquals("src/tests/resources/j13.class", new FileClass("src/tests/resources/j13.class").toString());
    }

    @Test
    void testGetNameMethod(){
        assertEquals("src/tests/resources/j13.class", new FileClass("src/tests/resources/j13.class").getName());
    }

    @Test
    void testGetVersionMethod(){
        assertEquals(13, new FileClass("src/tests/resources/j13.class").getVersion());
    }

}