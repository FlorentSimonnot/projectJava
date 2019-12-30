package fr.project.main;

import fr.project.detection.classVisitor.MyClassVisitor;
import fr.project.detection.observers.*;
import fr.project.detection.visitor.MyVisitor;
import fr.project.optionsCommand.Option;
import fr.project.optionsCommand.OptionFactory;
import fr.project.optionsCommand.OptionsParser;
import fr.project.parsing.parser.FileParser;
import fr.project.parsing.parser.ParserException;
import fr.project.writer.MyWriter;
import org.objectweb.asm.Opcodes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * A class that represents the project Retro.
 * It can take a directory, a .class file or a .jar file as input and detect all the features detected on it according to the option [--features].
 * [--features] contains a list of features that can be try-with-resources, lambda, nest mates, concatenation or record.
 * If [--features] is absent, then all the features mentioned above are asked.
 * It has to have a [--target] version of java which you want to write your new bytecode files.
 * The option [--info] displays all the features detected for each file.
 * The option [--force] forces the writing of new bytecode files even if the java version is not compatible.
 * @author SIMONNOT Florent
 *
 */
public class App {

	public static void main(String[] args) throws IOException, ParserException {
		List<MyClassVisitor> visitors = new ArrayList<>();

		var optionFactory = createOptionFactory();
		var observersFactory = createObserverFactory();

		var options = OptionsParser.parseOptions(args, optionFactory);

		System.out.println(options.helpIsDemanding());

		if(options.helpIsDemanding()){
			//Call help
			return;
		}

		var observers = FeaturesManager.createObservers(options.getArgsOption(Option.OptionEnum.FEATURES), observersFactory);

		FileParser.parseFile(options.getFile()).forEach(f -> {
			try {
				var mv = new MyVisitor(f, observers);
				var cv = mv.getClassVisitor();
				mv.getClassReader().accept(cv, 0);
				visitors.add(cv);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		if(options.infoIsDemanding())
			observers.forEach(FeatureObserver::showFeatures);


		/*      ************************************************     */
		/*        CREATE A NEW CLASS FILE WITH A NEW VERSION         */
		/*      ************************************************     */

		if(options.rewritingIsDemanding()){
			var version = convertTargetToOpcode(options.getArgsOption(Option.OptionEnum.TARGET));
			visitors.forEach(cv -> {
				var myWriter = new MyWriter(cv.getMyClass(), version);
				myWriter.createClass();
				myWriter.writeFields();
				myWriter.writeLambdaInnerClasses();
				myWriter.writeConstructors();
				myWriter.writeMethods();

				String res = null;
				try {
					res = myWriter.createFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}

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

	private static int convertTargetToOpcode(String s){
		return Integer.parseInt(s)+44;

	}

}
