package com.kpi.ivanov;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertArrayEquals;

@RunWith(Parameterized.class)
public class MainTest {
    @Parameterized.Parameters
    public static Collection input() {
        return Arrays.asList(
                new Object[][]{
                        {"src/test/resources/input/input0",
                                "src/test/resources/mainTest/output/output0",
                                "src/test/resources/mainTest/answers/rightAnswer0"},
                        {"src/test/resources/input/input1",
                                "src/test/resources/mainTest/output/output1",
                                "src/test/resources/mainTest/answers/rightAnswer1"},
                        {"src/test/resources/input/input2",
                                "src/test/resources/mainTest/output/output2",
                                "src/test/resources/mainTest/answers/rightAnswer2"},
                        {"src/test/resources/input/input3",
                                "src/test/resources/mainTest/output/output3",
                                "src/test/resources/mainTest/answers/rightAnswer3"},
                        {"src/test/resources/input/input4",
                                "src/test/resources/mainTest/output/output4",
                                "src/test/resources/mainTest/answers/rightAnswer4"},
                        {"src/test/resources/input/input5",
                                "src/test/resources/mainTest/output/output5",
                                "src/test/resources/mainTest/answers/rightAnswer5"},
                }
        );
    }

    private String in;
    private String out;
    private String answer;

    public MainTest(String in, String out, String answer) {
        this.in = in;
        this.out = out;
        this.answer = answer;
    }

    @Test
    public void testAverageResponseTime() throws IOException {
            Main.main(new String[] {in, out});
            assertArrayEquals(Files.readAllBytes(Paths.get(out)), Files.readAllBytes(Paths.get(answer)));
    }
}
