package com.kpi.ivanov;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertArrayEquals;

@RunWith(Parameterized.class)
public class LogProcessorTest {
    @Parameterized.Parameters
    public static Collection input() {
        return Arrays.asList(
                new Object[][]{
                        {new File("src/test/resources/input/input0"),
                                new File("src/test/resources/logProcessorTestAnswers/rightAnswer0")},
                        {new File("src/test/resources/input/input1"),
                                new File("src/test/resources/logProcessorTestAnswers/rightAnswer1")},
                        {new File("src/test/resources/input/input2"),
                                new File("src/test/resources/logProcessorTestAnswers/rightAnswer2")},
                        {new File("src/test/resources/input/input3"),
                                new File("src/test/resources/logProcessorTestAnswers/rightAnswer3")},
                        {new File("src/test/resources/input/input4"),
                                new File("src/test/resources/logProcessorTestAnswers/rightAnswer4")},
                        {new File("src/test/resources/input/input5"),
                                new File("src/test/resources/logProcessorTestAnswers/rightAnswer5")},
                }
        );
    }

    private File in;
    private File answer;

    public LogProcessorTest(File in, File answer) {
        this.in = in;
        this.answer = answer;
    }

    @Test
    public void test() {
        try (InputStream inputStream = new FileInputStream(in);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            new LogProcessor(responseEntries -> {
                long result = responseEntries.stream().map(ResponseEntry::toString).count();
                if (result == 0) {
                    return "-";
                }

                return Long.toString(result);
            }).process(inputStream, outputStream);

            assertArrayEquals(outputStream.toByteArray(), Files.readAllBytes(answer.toPath()));

        } catch (IOException exception) {
            System.out.println("Exception during the test " + exception);
        }
    }
}
