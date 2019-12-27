package projectTest.testOptions;

import com.project.options.Option;
import com.project.options.OptionFactory;
import com.project.options.OptionsParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OptionsParserTest {
    private final OptionFactory optionFactory = new OptionFactory();

    @BeforeEach
    void setUp() {
        optionFactory.register("--help", new Option(Option.OptionEnum.HELP));
        optionFactory.register("--info", new Option(Option.OptionEnum.INFO));
        optionFactory.register("--target", new Option(Option.OptionEnum.TARGET));
        optionFactory.register("--features", new Option(Option.OptionEnum.FEATURES));
    }

    @Test
    void shouldCreateAListWithFourOption(){
        var args = new String[]{
            "--help",
            "--info",
            "--target",
            "7",
            "--features",
            "[try-with-resources, lambda]"
        };
        assertEquals(4, OptionsParser.parseOptions(args, optionFactory).getOptions().size());
    }

    @Test
    void shouldThrowIllegalStateExceptionWhenAnOptionIsNotRecognized(){
        var args = new String[]{
                "--help",
                "--info",
                "--target",
                "7",
                "--feat",
                "[try-with-resources, lambda]"
        };
        assertThrows(IllegalStateException.class, () -> OptionsParser.parseOptions(args, optionFactory));
    }

    @Test
    void shouldCreateOptionsListWithNoOptionWhenWeHaveOnlyOneArgumentWithoutOption(){
        var args = new String[]{
                "7",
                "[try-with-resources, lambda]"
        };
        assertEquals(0, OptionsParser.parseOptions(args, optionFactory).getOptions().size());
    }

    @Test
    void shouldThrowIllegalStateExceptionWhenWePassedArgumentToAnOptionWhichDoesntTakeArgument(){
        var args = new String[]{
                "--help",
                "7",
                "--feat",
                "[try-with-resources, lambda]"
        };
        assertThrows(IllegalStateException.class, () -> OptionsParser.parseOptions(args, optionFactory));
    }

    @Test
    void shouldThrowIllegalStateExceptionWhenOptionWaitingArgumentButDoesntReceiveIt(){
        var args = new String[]{"--help", "--info", "--target", "--features"};
        var args2 = new String[]{"--help", "--info", "--features"};
        assertAll(
            () -> {
                assertThrows(IllegalStateException.class, () -> {
                    OptionsParser.parseOptions(args, optionFactory);
                });
            },
            () -> {
                assertThrows(IllegalStateException.class, () -> {
                    OptionsParser.parseOptions(args2, optionFactory);
                });
            }
        );
    }

    @Test
    void shouldThrowNullPointerExceptionWhenNullArgumentIsPassed(){
        var args = new String[]{"--help", "--info", "--target", "--features"};
        assertAll(
                () -> {
                    assertThrows(NullPointerException.class, () -> OptionsParser.parseOptions(null, optionFactory));
                },
                () -> {
                    assertThrows(NullPointerException.class, () -> OptionsParser.parseOptions(null, null));
                },
                () -> {
                    assertThrows(NullPointerException.class, () -> OptionsParser.parseOptions(args, null));
                }
        );
    }
}