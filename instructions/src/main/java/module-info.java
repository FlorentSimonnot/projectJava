/**
 * Represents all the instructions in java bytecode (simple or features).
 * @author CHU Jonathan
 *
 */
open module instructions {
    requires org.objectweb.asm;
    exports fr.project.instructions.simple;
    exports fr.project.instructions.features;
}
