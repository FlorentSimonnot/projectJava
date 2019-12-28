package fr.project.parsing.files;

import org.objectweb.asm.ClassReader;

/**
 * 
 * @author SIMONNOT Florent
 * An Interface that represents different types of files (directory, .class, .jar)
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
     */
    public int getVersion();
    
    /**
     * Gets the ClassReader of the FileInterface.
     * @return the ClassReader of the FileInterface
     */
    public ClassReader getClassReader();
    
    /**
     * Gets the full path of the FileInterface.
     * @return a String corresponding to the full path of th FileInterface
     */
    public String getPath();
}
