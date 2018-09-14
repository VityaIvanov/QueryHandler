package com.kpi.ivanov;

import java.io.*;
import java.util.List;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.function.Function;

/**
 * Main class
 */
public class Main {

    private static Function<Set<ResponseEntry>, String> function = responseEntries -> {
        OptionalDouble averageTime = responseEntries.stream()
                .mapToDouble(entry -> entry.getResponseTime().toMinutes()).average();

        if (!averageTime.isPresent()) {
            return "-";
        }

        return Long.toString(Math.round(averageTime.getAsDouble()));
    };

    public static void main(String[] args) {
        try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("src/main/resources/statistics"))))) {
            List<String> results = new LogProcessor(function).process(new FileInputStream(new File("src/main/resources/records")));

            for (String result: results) {
                writer.write(result);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
