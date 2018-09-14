package com.kpi.ivanov;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.OptionalDouble;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        try {
            new LogProcessor(Main::resultComputer)
                    .process(new FileInputStream(new File("src/main/resources/records")),
                            new FileOutputStream(new File("src/main/resources/statistics")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String resultComputer(Set<ResponseEntry> responseEntries) {
        OptionalDouble averageTime = responseEntries.stream()
                .mapToDouble(entry -> entry.getResponseTime().toMinutes()).average();

        if (!averageTime.isPresent()) {
            return "-";
        }

        return Long.toString(Math.round(averageTime.getAsDouble()));
    }
}
