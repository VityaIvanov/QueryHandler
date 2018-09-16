package com.kpi.ivanov;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.OptionalDouble;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Invalid number of paths to files. Must be 2 paths to files");
            return;
        }

        try(InputStream in = Files.newInputStream(Paths.get(args[0]));
            OutputStream out = Files.newOutputStream(Paths.get(args[1]))){
            new LogProcessor(Main::calculateAverageResponseTime).process(in, out);
        } catch (ParseException exception) {
            System.out.println("Exception during parsing " + exception.getMessage());
        } catch (IOException exception) {
            System.out.println("Exception during reading or writing data " + exception);
        } catch (Exception exception) {
            System.out.println("Unexpected exception" + exception);
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
