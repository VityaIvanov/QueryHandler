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
import java.util.OptionalDouble;

import static org.junit.Assert.assertArrayEquals;

@RunWith(Parameterized.class)
public class MainTest {
    @Parameterized.Parameters
    public static Collection input() {
        return Arrays.asList(
                new Object[][]{
                        {new File("src/test/resources/input/input0"),
                                new File("src/test/resources/mainTestAnswers/rightAnswer0")},
                        {new File("src/test/resources/input/input1"),
                                new File("src/test/resources/mainTestAnswers/rightAnswer1")},
                        {new File("src/test/resources/input/input2"),
                                new File("src/test/resources/mainTestAnswers/rightAnswer2")},
                        {new File("src/test/resources/input/input3"),
                                new File("src/test/resources/mainTestAnswers/rightAnswer3")},
                        {new File("src/test/resources/input/input4"),
                                new File("src/test/resources/mainTestAnswers/rightAnswer4")},
                        {new File("src/test/resources/input/input5"),
                                new File("src/test/resources/mainTestAnswers/rightAnswer5")},
                }
        );
    }

    private File in;
    private File answer;

    public MainTest(File in, File answer) {
        this.in = in;
        this.answer = answer;
    }

    @Test
    public void test() {
        try (InputStream inputStream = new FileInputStream(in);
             ByteArrayOutputStream out = new ByteArrayOutputStream()){
            new LogProcessor(responseEntries -> {
                OptionalDouble averageTime = responseEntries.stream()
                        .mapToDouble(entry -> entry.getResponseTime().toMinutes()).average();

                if (!averageTime.isPresent()) {
                    return "-";
                }

                return Long.toString(Math.round(averageTime.getAsDouble()));
            }).process(inputStream, out);

            assertArrayEquals(out.toByteArray(), Files.readAllBytes(answer.toPath()));
        } catch (IOException exception) {
            System.out.println("Exception during the test");
        }
    }
}
