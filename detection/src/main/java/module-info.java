module detection {
    requires org.objectweb.asm;

    requires instructions;
    requires parsing;

    exports fr.project.detection.methodVisitor;
    exports fr.project.detection.observers;
    exports fr.project.detection.visitor;
    exports fr.project.detection.classVisitor;
}