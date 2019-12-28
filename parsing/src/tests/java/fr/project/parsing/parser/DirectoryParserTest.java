package fr.project.parsing.parser;

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

    @Test
    void shouldThrowParserExceptionWhenIsNotDirectory(){
        assertThrows(ParserException.class, () -> {
           new DirectoryParser().parseMyFile("j13.class");
        });
    }
}