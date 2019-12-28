package com.project.files;

import java.io.IOException;

import org.objectweb.asm.ClassReader;

/**
 * 
 * @author SIMONNOT Florent
 * An Interface that represents different types of files (directory, .class, .jar)
 * It allows to check what type of argument you give to the project Retro when you run it.
 *
 */
public interface FileInterface {

	/**
	 * Gets the name of the FileInterface.
	 * @return a String corresponding to the name of the FileInterface
	 */
    public String getName();
    
    /**
     * Gets the java version of the FileInterface.
     * @return an int corresponding to the java version of the FileInterface
     * @throws IOException 
     */
    public int getVersion() throws IOException;
    
    /**
     * Gets the ClassReader of the FileInterface.
     * @return the ClassReader of the FileInterface
     * @throws IOException 
     */
    public ClassReader getClassReader() throws IOException;
    
    /**
     * Gets the full path of the FileInterface.
     * @return a String corresponding to the full path of th FileInterface
     */
    public String getPath();
}
