package fr.project.main;

import fr.project.detection.classVisitor.MyClassVisitor;
import fr.project.detection.observers.*;
import fr.project.detection.visitor.MyVisitor;
import fr.project.options.Option;
import fr.project.options.OptionFactory;
import fr.project.options.OptionsParser;
import fr.project.parsing.parser.FileParser;
import fr.project.parsing.parser.ParserException;
import fr.project.writer.MyWriter;
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
					var cv2 = mv2.getClassVisitor();
					mv2.getClassReader().accept(cv2, 0);
				} catch (IOException | ParserException e) {
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
		/*switch(version){
			case 5 : return Opcodes.V1_5;
			case 6 : return Opcodes.V1_6;
			case 7 : return Opcodes.V1_7;
			case 8 : return Opcodes.V1_8;
			case 9 : return Opcodes.V9;
			case 10 : return Opcodes.V10;
			case 11 : return Opcodes.V11;
			case 12 : return Opcodes.V12;
			case 13 : return Opcodes.V13;
			default: throw new IllegalArgumentException("The version is incorrect");
		}*/
	}

}
