package com.kpi.ivanov;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.Duration;
import java.util.Set;
import java.util.function.Function;

public class Main {
    public static void main(String[] args) {
        Function<Set<ResponseEntry>, String> function = (responseEntries) -> {

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

        LogProcessor logProcessor;
        try {
            logProcessor = new LogProcessor(
                    new FileInputStream(new File("src/main/resources/records")),
                    new FileOutputStream(new File("src/main/resources/statistics")),
                    function);

            logProcessor.process();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
