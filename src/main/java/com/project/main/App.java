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

/**
 * 
 * @author SIMONNOT Florent
 * A class that represents the project Retro.
 * It can take a directory, a .class file or a .jar file as input and detect all the features detected on it according to the option [--features].
 * [--features] contains a list of features that can be try-with-resources, lambda, nest mates, concatenation or record.
 * If [--features] is absent, then all the features mentioned above are asked.
 * It has to have a [--target] version of java which you want to write your new bytecode files.
 * The option [--info] displays all the features detected for each file.
 * The option [--force] forces the writing of new bytecode files even if the java version is not compatible.
 *
 */
public class App {

	public static void main(String[] args) throws IOException, ParserException {
		List<MyClassVisitor> visitors = new ArrayList<>();

		System.out.println("bonjour");
		var optionFactory = createOptionFactory();
		var observersFactory = createObserverFactory();

		var options = OptionsParser.parseOptions(args, optionFactory);

		//        var lambda = "src/tests/resources/ForaxTests/TestLambda.class";
		//        var tryWithResources = "src/tests/resources/ForaxTests/TestTryWithResource.class";
		//        var concat = "src/tests/resources/ForaxTests/TestConcat.class";

		var jar = "src/tests/resources/dirTest/testJar.jar";

		var observers = FeaturesManager.createObservers(options.getArgsOption(Option.OptionEnum.FEATURES), observersFactory);

		var jarParser = new JarParser();
		var files = jarParser.parseMyFile(jar);
		
		files.forEach(f -> {
			try {
				System.out.println(f.getName());
				FileParser.parseFile(f.getName()).forEach(k -> {
					var mv = new MyVisitor(f, observers);
					var cv = mv.getClassVisitor();
					mv.getClassReader().accept(cv, 0);
					visitors.add(cv);
				});;
			} catch (IOException | ParserException e) {
				e.printStackTrace();
			}
		});
		
		//        FileParser.parseFile(lambda).forEach(f -> {
		//            var mv = new MyVisitor(f, observers);
		//            var cv = mv.getClassVisitor();
		//            mv.getClassReader().accept(cv, 0);
		//            visitors.add(cv);
		//        });
		//        
		//        FileParser.parseFile(tryWithResources).forEach(f -> {
		//            var mv = new MyVisitor(f, observers);
		//            var cv = mv.getClassVisitor();
		//            mv.getClassReader().accept(cv, 0);
		//            visitors.add(cv);
		//        });
		//        
		//        FileParser.parseFile(concat).forEach(f -> {
		//            var mv = new MyVisitor(f, observers);
		//            var cv = mv.getClassVisitor();
		//            mv.getClassReader().accept(cv, 0);
		//            visitors.add(cv);
		//        });


		observers.forEach(FeatureObserver::showFeatures);


		/*      ************************************************     */
		/*        CREATE A NEW CLASS FILE WITH A NEW VERSION         */
		/*      ************************************************     */

		visitors.forEach(cv -> {
			var myWriter = new MyWriter(cv.getMyClass(), Opcodes.V1_7);
			System.out.println(cv.getMyClass());
			myWriter.createClass();
			myWriter.writeFields();
			myWriter.writeConstructors();
			myWriter.writeMethods();
			System.out.println("App.java");
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
