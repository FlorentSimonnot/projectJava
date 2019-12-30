/**
 * Writes java bytecode into a .class file.
 * @author CHU Jonathan
 *
 */
module writing {
    exports fr.project.writer;
    requires org.objectweb.asm;
    requires instructions;
}