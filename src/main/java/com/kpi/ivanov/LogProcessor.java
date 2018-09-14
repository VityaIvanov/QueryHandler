package com.kpi.ivanov;

import java.io.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Handler for processing input date
 */
final class LogProcessor {
    private static final String QUERY_RECORD = "D";
    private static final String RESPONSE_RECORD = "C";

    private static final int NUMBER_OF_ELEMENTS_IN_THE_RESPONSE_RECORD = 6;
    private static final int NUMBER_OF_ELEMENTS_IN_THE_QUERY_RECORD = 5;

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final Function<Set<ResponseEntry>, String> resultComputer;

    LogProcessor(Function<Set<ResponseEntry>, String> resultComputer) {
        this.resultComputer = resultComputer;
    }

    List<String> process(InputStream in) throws IOException {
        List<String> result = new ArrayList<>();
        ResponsesQueryEngine responsesQueryEngine = new ResponsesQueryEngine();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            int numOfRecords = parseCountOfRecords(reader.readLine());
            for (int i = 0; i < numOfRecords; i++) {
                String record = reader.readLine();

                if (record == null) {
                    throw new RuntimeException("Expected more records");
                }

                List<String> tokens = splitToTokens(record);
                if (tokens.get(0).equals(QUERY_RECORD)) {
                    result.add(resultComputer.apply(responsesQueryEngine.query(parseQueryEntry(tokens))));
                } else {
                    responsesQueryEngine.add(parseResponseEntry(tokens));
                }
            }
        }

        return result;
    }

    private static int parseCountOfRecords(String record) {
        if (record == null) {
            throw new RuntimeException("Empty input stream");
        }

        int numOfRecords = Integer.parseInt(record);

        if (numOfRecords < 0) {
            throw new RuntimeException("Number of records must be positive " + record);
        }

        return numOfRecords;
    }

    private static QueryEntry parseQueryEntry(List<String> tokens) {
        try {
            if (tokens.size() != NUMBER_OF_ELEMENTS_IN_THE_QUERY_RECORD) {
                throw new IllegalArgumentException("Invalid query record " + tokens);
            }

            QueryEntry.Builder builder = new QueryEntry.Builder().
                    setResponseType(parseResponseType(tokens.get(3)));

            if (!tokens.get(1).equals("*")) {
                builder.setService(parseService(parseNumericTokens(tokens.get(1))));
            }

            if (!tokens.get(2).equals("*")) {
                builder.setQuestion(parseQuestion(parseNumericTokens(tokens.get(2))));
            }

            List<LocalDate> period = createDates(tokens.get(4));
            builder.setFromDate(period.get(0));

            if (period.size() == 2) {
                builder.setToDate(period.get(1));
            }

            return builder.build();
        } catch (RuntimeException e) {
            throw new RuntimeException("Invalid query record " + tokens, e);
        }
    }

    private static ResponseEntry parseResponseEntry(List<String> tokens) {
        try {
            if (tokens.size() != NUMBER_OF_ELEMENTS_IN_THE_RESPONSE_RECORD) {
                throw new IllegalArgumentException("Invalid response record " + tokens);
            }

            if (!tokens.get(0).equals(RESPONSE_RECORD)) {
                throw new RuntimeException("Invalid symbol " + tokens.get(0) + " in " + tokens);
            }

            return new ResponseEntry.Builder()
                    .setService(parseService(parseNumericTokens(tokens.get(1))))
                    .setQuestion(parseQuestion(parseNumericTokens(tokens.get(2))))
                    .setResponseType(parseResponseType(tokens.get(3)))
                    .setDate(parseDate(tokens.get(4)))
                    .setDuration(parseDuration(tokens.get(5)))
                    .build();
        } catch (RuntimeException e) {
            throw new RuntimeException("Invalid response record " + tokens, e);
        }
    }

    private static Service parseService(List<Integer> numbers) {
        if (numbers.size() > 2) {
            throw new IllegalArgumentException("To much elements for service" + numbers);
        }

        if (numbers.size() == 1) {
            return new Service(numbers.get(0));
        }

        return new Service(numbers.get(0), numbers.get(1));
    }

    private static Question parseQuestion(List<Integer> numbers) {
        if (numbers.size() > 3) {
            throw new IllegalArgumentException("To much elements for question" + numbers);
        }

        if (numbers.size() == 1) {
            return new Question(numbers.get(0));
        }

        if (numbers.size() == 2) {
            return new Question(numbers.get(0), new Question.QuestionCategory(numbers.get(1)));
        }

        return new Question(numbers.get(0), new Question.QuestionCategory(numbers.get(1), numbers.get(2)));
    }

    private static ResponseType parseResponseType(String token) {
        if (token.equals("P")) {
            return ResponseType.FIRST_ANSWER;
        }

        if (token.equals("N")) {
            return ResponseType.NEXT_ANSWER;
        }

        throw new RuntimeException("Invalid symbol + " + token);
    }

    private static Duration parseDuration(String token) {
        return Duration.ofMinutes(Integer.parseInt(token));
    }

    private static List<LocalDate> createDates(String token) {
        return Arrays.stream(token.split("-")).map(LogProcessor::parseDate).collect(Collectors.toList());
    }

    private static LocalDate parseDate(String token) {
        return LocalDate.parse(token, formatter);
    }

    private static List<Integer> parseNumericTokens(String token) {
        return Arrays.stream(token.split("\\.")).map(Integer::parseInt).collect(Collectors.toList());
    }

    private static List<String> splitToTokens(String record) {
        return Arrays.stream(record.split(" ")).filter(token -> !token.equals("")).collect(Collectors.toList());
    }
}