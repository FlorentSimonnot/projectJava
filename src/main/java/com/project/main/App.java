package com.project.main;

import com.project.featuresObserver.*;
import com.project.options.Option;
import com.project.options.OptionFactory;
import com.project.options.OptionsParser;
import com.project.parser.*;
import com.project.visitor.MyClassVisitor;
import com.project.visitor.MyVisitor;
import com.project.writer.MyWriter;
import org.objectweb.asm.Opcodes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App {

    public static void main(String[] args) throws IOException, ParserException {
        List<MyClassVisitor> visitors = new ArrayList<>();

        var optionFactory = createOptionFactory();
        var observersFactory = createObserverFactory();

        var options = OptionsParser.parseOptions(args, optionFactory);

        var file = "src/tests/resources/TestRecord.class";
        var observers = FeaturesManager.createObservers(options.getArgsOption(Option.OptionEnum.FEATURES), observersFactory);

        FileParser.parseFile(file).forEach(f -> {
            var mv = new MyVisitor(f, observers);
            var cv = mv.getClassVisitor();
            mv.getClassReader().accept(cv, 0);
            visitors.add(cv);
        });


        observers.forEach(FeatureObserver::showFeatures);


        /*      ************************************************     */
        /*        CREATE A NEW CLASS FILE WITH A NEW VERSION         */
        /*      ************************************************     */

        visitors.forEach(cv -> {
            var myWriter = new MyWriter(cv.getMyClass(), Opcodes.V1_7);
            myWriter.createClass();
            myWriter.writeFields();
            myWriter.writeConstructors();
            myWriter.writeMethods();
            String res = null;
            try {
                res = myWriter.createFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            MyVisitor mv2 = null;
            try {
                mv2 = new MyVisitor(FileParser.parseFile(res).get(0), observers);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserException e) {
                e.printStackTrace();
            }
            var cv2 = mv2.getClassVisitor();
            mv2.getClassReader().accept(cv2, 0);
        });


    }

    private static OptionFactory createOptionFactory(){
        var optionFactory = new OptionFactory();
        optionFactory.register("--help", new Option(Option.OptionEnum.HELP));
        optionFactory.register("--info", new Option(Option.OptionEnum.INFO));
        optionFactory.register("--target", new Option(Option.OptionEnum.TARGET));
        optionFactory.register("--features", new Option(Option.OptionEnum.FEATURES));
        return optionFactory;
    }

    private static FeaturesObserverFactory createObserverFactory(){
        var observersFactory = new FeaturesObserverFactory();
        observersFactory.register("try-with-resources", new TryWithResourcesObserver());
        observersFactory.register("nestMember", new NestMemberObserver());
        observersFactory.register("concatenation", new ConcatenationObserver());
        observersFactory.register("lambda", new LambdaObserver());
        observersFactory.register("record", new RecordObserver());
        return observersFactory;
    }


}
