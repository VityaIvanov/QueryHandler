package com.kpi.ivanov;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.OptionalDouble;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        try {
            new LogProcessor(Main::calculateAverageResponseTime)
                    .process(new FileInputStream(new File(args[0])),
                            new FileOutputStream(new File(args[1])));
        } catch (Exception exception) {
            System.out.println("Exception during parsing " + exception);
        }
    }

    private static String calculateAverageResponseTime(Set<ResponseEntry> responseEntries) {
        OptionalDouble averageTime = responseEntries.stream()
                .mapToDouble(entry -> entry.getResponseTime().toMinutes()).average();

        if (!averageTime.isPresent()) {
            return "-";
        }

        return Long.toString(Math.round(averageTime.getAsDouble()));
    }
}
