package com.kpi.ivanov;

import java.io.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Represent handler of query records
 * Contains variable fields in, out and function
 * in - represent input stream, it can be file, console etc.
 * out - represent output stream, it can be file, console etc.
 * function - represent function for handle query records.
 * Using Functional Interface allow user to create custom algorithms of handling.
 * For example user can designed function for counting number of response records
 */
public class LogProcessor {
    private static final String QUERY_RECORD = "D";
    private static final String RESPONSE_RECORD = "C";

    private static final int NUMBER_OF_ELEMENTS_IN_THE_RESPONSE_RECORD = 6;
    private static final int NUMBER_OF_ELEMENTS_IN_THE_QUERY_RECORD = 5;

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final InputStream in;
    private final OutputStream out;
    private final Function<Set<ResponseEntry>, String> function;
    private final QueryEngine queryEngine;

    public LogProcessor(InputStream in, OutputStream out, Function<Set<ResponseEntry>, String> function) {
        this.in = in;
        this.out = out;
        this.function = function;
        queryEngine = new QueryEngine();
    }

    /**
     * Method for handling input date and create output date
     */
    public void process() {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out))) {

            String firstLine = reader.readLine();
            int numOfRecords;

            try {
                numOfRecords = Integer.parseInt(firstLine);
                if (numOfRecords < 0) {
                    throw new IllegalArgumentException();
                }

            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid number of records");
            }

            for (int i = 0; i < numOfRecords; i++) {
                String record = reader.readLine();

                if (record == null) {
                    throw new IllegalArgumentException("Expected more records");
                }

                List<String> tokens = getTokens(record);
                if (isQueryRecord(tokens.get(0))) {
                    writer.write(handleQueryRecord(queryEntryParser(tokens)));
                    writer.newLine();
                } else {
                    queryEngine.add(responseEntryParser(tokens));
                }
            }
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private String handleQueryRecord(QueryEntry queryEntry) {
        return function.apply(queryEngine.query(queryEntry));
    }

    private QueryEntry queryEntryParser(List<String> tokens) {
        if (tokens.size() != NUMBER_OF_ELEMENTS_IN_THE_QUERY_RECORD) {
            throw new IllegalArgumentException("Invalid response record " + tokens);
        }

        QueryEntry.Builder builder = new QueryEntry.Builder().
                setResponseType(createResponseType(tokens.get(3)));

        if (!tokens.get(1).equals("*")) {
            builder.setService(createService(parseNumericTokens(tokens.get(1))));
        }

        if (!tokens.get(2).equals("*")) {
            builder.setQuestion(createQuestion(parseNumericTokens(tokens.get(2))));
        }

        List<LocalDate> period = createDates(tokens.get(4));
        builder.setFromDate(period.get(0));

        if (period.size() == 2) {
            builder.setToDate(period.get(1));
        }

        return builder.build();
    }

    private ResponseEntry responseEntryParser(List<String> tokens) {
        if (tokens.size() != NUMBER_OF_ELEMENTS_IN_THE_RESPONSE_RECORD) {
            throw new IllegalArgumentException("Invalid response record " + tokens);
        }

        if (!tokens.get(0).equals(RESPONSE_RECORD)) {
            throw new IllegalArgumentException("Invalid symbol " + tokens.get(0) + " in " + tokens);
        }

        return new ResponseEntry.Builder().
                setService(createService(parseNumericTokens(tokens.get(1)))).
                setQuestion(createQuestion(parseNumericTokens(tokens.get(2)))).
                setResponseType(createResponseType(tokens.get(3))).
                setDate(createDate(tokens.get(4))).
                setDuration(createDuration(tokens.get(5))).
                setId().
                build();
    }

    private Service createService(List<Integer> numbers) {
        if (numbers.size() > 2) {
            throw new IllegalArgumentException("To much elements for service" + numbers);
        }

        if (numbers.get(0) <= 0 || numbers.get(0) > 10) {
            throw new IllegalArgumentException("Service type can be only from 1 to 10. " + numbers);
        }

        if (numbers.size() == 1) {
            return new Service(numbers.get(0));
        }

        if (numbers.get(1) <= 0 || numbers.get(1) > 3) {
            throw new IllegalArgumentException("Service supType can be only from 1 to 3. " + numbers);
        }

        return new Service(numbers.get(0), numbers.get(1));
    }

    private Question createQuestion(List<Integer> numbers) {
        if (numbers.size() > 3) {
            throw new IllegalArgumentException("To much elements for question" + numbers);
        }

        if (numbers.get(0) <= 0 || numbers.get(0) > 10) {
            throw new IllegalArgumentException("Question type can be only from 1 to 10. " + numbers);
        }

        if (numbers.size() == 1) {
            return new Question(numbers.get(0));
        }

        if (numbers.get(1) <= 0 || numbers.get(1) > 20) {
            throw new IllegalArgumentException("Question category can be only from 1 to 20. " + numbers);
        }

        if (numbers.size() == 2) {
            return new Question(numbers.get(0), numbers.get(1));
        }

        if (numbers.get(2) <= 0 || numbers.get(2) > 5) {
            throw new IllegalArgumentException("Question subCategory can be only from 1 to 5. " + numbers);
        }

        return new Question(numbers.get(0), numbers.get(1), numbers.get(2));
    }

    private ResponseType createResponseType(String token) {
        if (token.equals("P")) {
            return ResponseType.FIRST_ANSWER;
        }

        if (token.equals("N")) {
            return ResponseType.NEXT_ANSWER;
        }

        throw new IllegalArgumentException("Invalid symbol + " + token);
    }

    private List<LocalDate> createDates(String token) {
        String[] dates = token.split("-");

        if (dates.length > 2) {
            throw new IllegalArgumentException("Invalid period of dates " + token);
        }

        List<LocalDate> localDates = new ArrayList<>();

        for (String date : dates) {
            localDates.add(createDate(date));
        }

        return localDates;
    }

    private LocalDate createDate(String token) {
        String[] numbers = token.split("\\.");
        if (numbers[0].length() == 1) {
            token = "0" + token;
        }

        LocalDate date;
        try {
            date = LocalDate.parse(token, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("invalid date " + token);
        }
        return date;
    }

    private Duration createDuration(String token) {
        Duration duration;
        try {
            long number = Long.parseLong(token);

            if (number < 0) {
                throw new IllegalArgumentException("Negative duration " + token);
            }

            duration = Duration.ofMinutes(number);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid symbol in duration. Must be a number " + token);
        }

        return duration;
    }

    private List<Integer> parseNumericTokens(String token) {
        List<Integer> integers = new ArrayList<>();
        String[] numbers = token.split("\\.");

        try {
            for (String number: numbers) {
                integers.add(Integer.parseInt(number));
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid symbol in " + token);
        }

        return integers;
    }

    private boolean isQueryRecord(String token) {
        return token.equals(QUERY_RECORD);
    }

    private List<String> getTokens(String record) {
        String[] tokens = record.split(" ");

        if (tokens.length == 0) {
            throw new IllegalArgumentException("Found an empty record");
        }

        return Arrays.stream(tokens).filter((token) -> !token.equals("")).collect(Collectors.toList());
    }

}