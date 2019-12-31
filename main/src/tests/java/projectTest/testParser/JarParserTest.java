package projectTest.testParser;

import project.parser.DirectoryParser;
import project.parser.JarParser;
import project.parser.ParserException;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class JarParserTest {

    @Test
    void shouldGetSameSizeForJarAndDirectory(){
        var srcJar = "src/tests/resources/testJar.jar";
        var srcRental = "src/tests/resources/rental";

        assertAll( () -> {
            assertEquals(new DirectoryParser().parseMyFile(srcRental).getSize(), new JarParser().parseMyFile(srcJar).getSize());
        });
    }

    @Test
    void shouldThrowNullPointerExceptionWhenPathNameIsNull(){
        assertThrows(NullPointerException.class, () -> {
           new JarParser().parseMyFile(null);
        });
    }

    @Test
    void shouldThrowParserExceptionWhenJarFileDoesntExists(){
        assertThrows(ParserException.class, () -> {
           new JarParser().parseMyFile("");
        });
    }

    @Test
    void shouldThrowParserExceptionWhenPathNameIsNotAJarFile(){
        assertThrows(ParserException.class, () -> {
            new JarParser().parseMyFile("src/tests/resources/j13.class");
        });
    }
}