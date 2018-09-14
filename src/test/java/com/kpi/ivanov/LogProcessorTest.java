package com.kpi.ivanov;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.*;
import java.nio.file.Files;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;

@RunWith(Parameterized.class)
public class LogProcessorTest {
    private static LogProcessor logProcessor = new LogProcessor((responseEntries) -> {

        if (responseEntries.size() == 0) {
            return "-";
        }

        Duration duration = Duration.ofMinutes(0);
        for (ResponseEntry responseEntry : responseEntries) {
            duration = duration.plusMinutes(responseEntry.getResponseTime().toMinutes());
        }

        duration = duration.dividedBy(responseEntries.size());

        return Integer.toString((int) duration.toMinutes());
    });

    private File in;
    private File out;
    private File answer;
    private Handler handler;

    public LogProcessorTest(File in, File out, File answer) {
        this.in = in;
        this.out = out;
        this.answer = answer;
    }

    @Before
    public void setIn() {
        handler = new Handler();
    }

    @Test
    public void test() throws IOException {
        handler.process(in, out);
        byte[] f1 = Files.readAllBytes(out.toPath());
        byte[] f2 = Files.readAllBytes(answer.toPath());

        assertArrayEquals(f1, f2);
    }

    @Parameterized.Parameters
    public static Collection input() {
        return Arrays.asList(
                new Object[][]{
                        {new File("src/main/resources/testsFile/input/input0"),
                                new File("src/main/resources/testsFile/output/output0"),
                                new File("src/main/resources/answers/rightAnswer0")},
                        {new File("src/main/resources/testsFile/input/input1"),
                                new File("src/main/resources/testsFile/output/output1"),
                                new File("src/main/resources/answers/rightAnswer1")},
                        {new File("src/main/resources/testsFile/input/input2"),
                                new File("src/main/resources/testsFile/output/output2"),
                                new File("src/main/resources/answers/rightAnswer2")},
                        {new File("src/main/resources/testsFile/input/input3"),
                                new File("src/main/resources/testsFile/output/output3"),
                                new File("src/main/resources/answers/rightAnswer3")},
                        {new File("src/main/resources/testsFile/input/input4"),
                                new File("src/main/resources/testsFile/output/output4"),
                                new File("src/main/resources/answers/rightAnswer4")}
                }
        );
    }

    private static class Handler {
        void process(File in, File out) {
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out)))) {
                List<String> results = logProcessor.process(new FileInputStream(in));

                for (String result : results) {
                    writer.write(result);
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
