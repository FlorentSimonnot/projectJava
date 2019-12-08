package projectTest.testFiles;

import com.project.files.FileClass;
import com.project.files.FilesCollector;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.StringJoiner;

import static org.junit.jupiter.api.Assertions.*;

class FilesCollectorTest {
    private final int[] versions = {10, 11, 12, 13};

    @Test
    void shouldAddSimplyFileWithSuccess(){
        assertTrue(new FilesCollector().addFile(new FileClass("src/tests/resources/j13.class")));
    }

    @Test
    void shouldAddSeveralFilesWithSuccess(){
        var collector = new FilesCollector();
        Arrays.stream(versions).forEach(v -> {
            assertTrue(collector.addFile(new FileClass("src/tests/resources/j"+v+".class")));
        });
    }

    @Test
    void testGetSizeWithOneFileAdding(){
        var collector = new FilesCollector();
        collector.addFile(new FileClass("src/tests/resources/j13.class"));
        assertEquals(1, collector.getSize());
    }

    @Test
    void testGetSizeWithManyFilesAdding(){
        var collector = new FilesCollector();
        Arrays.stream(versions).forEach(v -> {
            collector.addFile(new FileClass("src/tests/resources/j"+v+".class"));
        });
        assertEquals(versions.length, collector.getSize());
    }

    @Test
    void testGetVersionsWithNoFileAdding(){
        assertEquals("No files in the collector", new FilesCollector().getVersions());
    }

    @Test
    void testGetVersionsWithOnFileAdding(){
        var collector = new FilesCollector();
        collector.addFile(new FileClass("src/tests/resources/j13.class"));
        assertEquals("src/tests/resources/j13.class - Java 13", collector.getVersions());
    }

    @Test
    void testGetVersionsWithManyFilesAdding(){
        var collector = new FilesCollector();
        Arrays.stream(versions).forEach(v -> {
            collector.addFile(new FileClass("src/tests/resources/j"+v+".class"));
        });

        var joiner = new StringJoiner("\n");
        Arrays.stream(versions).forEach(v ->{
            joiner.add("src/tests/resources/j"+v+".class"+" - Java "+v);
        });
        assertEquals(joiner.toString(), collector.getVersions());
    }

    @Test
    void testForEachOnACollector(){
        /* ADD FILES */
        var collector = new FilesCollector();
        Arrays.stream(versions).forEach(v -> {
            collector.addFile(new FileClass("src/tests/resources/j"+v+".class"));
        });

        /* Compute the sum of versions in array */
        var joinerExcpected = new StringJoiner("\n");
        Arrays.stream(versions).forEach(v ->{
            joinerExcpected.add(v+"");
        });

        /* Compute the sum of versions in array */
        var joinerResult = new StringJoiner("\n");
        collector.forEach(v ->{
            joinerResult.add(v.getVersion()+"");
        });
        assertEquals(joinerExcpected.toString(), joinerResult.toString());
    }
}