package com.kpi.ivanov;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Handler for processing input date
 */
final class LogProcessor {
    private static final String QUERY_RECORD = "D";
    private static final String RESPONSE_RECORD = "C";

    private static final String FIRST_ANSWER = "P";
    private static final String NEXT_ANSWER = "N";

    private static final int NUMBER_OF_ELEMENTS_IN_THE_RESPONSE_RECORD = 6;
    private static final int NUMBER_OF_ELEMENTS_IN_THE_QUERY_RECORD = 5;

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final Function<Set<ResponseEntry>, String> resultComputer;

    LogProcessor(Function<Set<ResponseEntry>, String> resultComputer) {
        this.resultComputer = resultComputer;
    }

    void process(InputStream in, OutputStream out){
        ResponsesQueryEngine responsesQueryEngine = new ResponsesQueryEngine();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out))) {

            int countOfRecords = parseCountOfRecords(reader.readLine());
            for (int i = 0; i < countOfRecords; i++) {
                String record = reader.readLine();

                if (record == null) {
                    throw new RuntimeException("Expected more records");
                }

                List<String> tokens = splitToTokens(record);
                if (isQueryRecord(tokens.get(0))) {
                    writer.write(resultComputer.apply(responsesQueryEngine.query(parseQueryEntry(tokens))));
                    writer.newLine();
                } else if (isResponseRecord(tokens.get(0))) {
                    responsesQueryEngine.add(parseResponseEntry(tokens));
                } else {
                    throw new RuntimeException("Invalid record type " + tokens.get(0) + " in record " + record);
                }
            }
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private static boolean isQueryRecord(String token) {
        return token.equals(QUERY_RECORD);
    }

    private static boolean isResponseRecord(String token) {
        return token.equals(RESPONSE_RECORD);
    }

    private static int parseCountOfRecords(String record) {
        int numOfRecords = Integer.parseInt(record);
        if (numOfRecords < 0) {
            throw new RuntimeException("Records counter must be positive " + record);
        }

        return numOfRecords;
    }

    private static QueryEntry parseQueryEntry(List<String> tokens) {
        try {
            if (tokens.size() != NUMBER_OF_ELEMENTS_IN_THE_QUERY_RECORD) {
                throw new RuntimeException("Invalid query record" + tokens);
            }

            QueryEntry.Builder builder = new QueryEntry.Builder().
                    setResponseType(parseResponseType(tokens.get(3)));

            if (!tokens.get(1).equals("*")) {
                builder.setService(parseService(parseNumericTokens(tokens.get(1))));
            }

            if (!tokens.get(2).equals("*")) {
                builder.setQuestion(parseQuestion(parseNumericTokens(tokens.get(2))));
            }

            List<LocalDate> period = parseDates(tokens.get(4));
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
                throw new RuntimeException("Invalid response record " + tokens);
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
        if (numbers.size() == 1) {
            return new Service(numbers.get(0));
        } else if (numbers.size() == 2) {
            return new Service(numbers.get(0), numbers.get(1));
        }

        throw new RuntimeException("To much elements for service" + numbers);
    }

    private static Question parseQuestion(List<Integer> numbers) {
        if (numbers.size() == 1) {
            return new Question(numbers.get(0));
        } else if (numbers.size() == 2) {
            return new Question(numbers.get(0), new Question.QuestionCategory(numbers.get(1)));
        } else if (numbers.size() == 3) {
            return new Question(numbers.get(0), new Question.QuestionCategory(numbers.get(1), numbers.get(2)));
        }

        throw new RuntimeException("To much elements for question" + numbers);
    }

    private static ResponseType parseResponseType(String token) {
        if (token.equals(FIRST_ANSWER)) {
            return ResponseType.FIRST_ANSWER;
        }

        if (token.equals(NEXT_ANSWER)) {
            return ResponseType.NEXT_ANSWER;
        }

        throw new RuntimeException("Invalid response type + " + token);
    }

    private static Duration parseDuration(String token) {
        return Duration.ofMinutes(Integer.parseInt(token));
    }

    private static List<LocalDate> parseDates(String token) {
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