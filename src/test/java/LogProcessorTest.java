import com.kpi.ivanov.*;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LogProcessorTest {

    private Function<Set<ResponseEntry>, String> function;

    @Before
    public void setIn() {
        function = (responseEntries) -> {

            if (responseEntries.size() == 0) {
                return "-";
            }

            Duration duration = Duration.ofMinutes(0);
            for (ResponseEntry responseEntry: responseEntries) {
                duration = duration.plusMinutes(responseEntry.getResponseTime().toMinutes());
            }

            duration = duration.dividedBy(responseEntries.size());

            return  Integer.toString((int) duration.toMinutes());
        };
    }

    @Test
    public void test0() throws IOException {
        File input = new File("src/main/resources/testsFile/input/input0");
        File output = new File("src/main/resources/testsFile/output/output0");
        File answer = new File("src/main/resources/answers/rightAnswer0");

        LogProcessor logProcessor;
        try {
            logProcessor = new LogProcessor(
                    new FileInputStream(input),
                    new FileOutputStream(output),
                    function);

            logProcessor.process();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        byte[] f1 = Files.readAllBytes(output.toPath());
        byte[] f2 = Files.readAllBytes(answer.toPath());

        assertArrayEquals(f1, f2);
    }


    @Test
    public void test1() throws IOException {
        File input = new File("src/main/resources/testsFile/input/input1");
        File output = new File("src/main/resources/testsFile/output/output1");
        File answer = new File("src/main/resources/answers/rightAnswer1");

        LogProcessor logProcessor;
        try {
            logProcessor = new LogProcessor(
                    new FileInputStream(input),
                    new FileOutputStream(output),
                    function);

            logProcessor.process();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        byte[] f1 = Files.readAllBytes(output.toPath());
        byte[] f2 = Files.readAllBytes(answer.toPath());

        assertArrayEquals(f1, f2);
    }

    @Test
    public void test2() throws IOException {
        File input = new File("src/main/resources/testsFile/input/input2");
        File output = new File("src/main/resources/testsFile/output/output2");
        File answer = new File("src/main/resources/answers/rightAnswer2");

        LogProcessor logProcessor;
        try {
            logProcessor = new LogProcessor(
                    new FileInputStream(input),
                    new FileOutputStream(output),
                    function);

            logProcessor.process();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        byte[] f1 = Files.readAllBytes(output.toPath());
        byte[] f2 = Files.readAllBytes(answer.toPath());

        assertArrayEquals(f1, f2);
    }

    @Test
    public void test3() throws IOException {
        File input = new File("src/main/resources/testsFile/input/input3");
        File output = new File("src/main/resources/testsFile/output/output3");
        File answer = new File("src/main/resources/answers/rightAnswer3");

        LogProcessor logProcessor;
        try {
            logProcessor = new LogProcessor(
                    new FileInputStream(input),
                    new FileOutputStream(output),
                    function);

            logProcessor.process();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        byte[] f1 = Files.readAllBytes(output.toPath());
        byte[] f2 = Files.readAllBytes(answer.toPath());

        assertArrayEquals(f1, f2);
    }

    @Test
    public void test4() throws IOException {
        File input = new File("src/main/resources/testsFile/input/input4");
        File output = new File("src/main/resources/testsFile/output/output4");
        File answer = new File("src/main/resources/answers/rightAnswer4");

        LogProcessor logProcessor;
        try {
            logProcessor = new LogProcessor(
                    new FileInputStream(input),
                    new FileOutputStream(output),
                    function);

            logProcessor.process();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        byte[] f1 = Files.readAllBytes(output.toPath());
        byte[] f2 = Files.readAllBytes(answer.toPath());

        assertArrayEquals(f1, f2);
    }

    @Test
    public void test5() {

    }
 }
