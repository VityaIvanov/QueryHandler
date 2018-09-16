package com.kpi.ivanov;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
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
 * Handler for processing input data.
 * Setting custom algorithm allow customers create own algorithms for handling data.
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

    /**
     * Input stream is used for handling data without putting it all in computer memory.
     * Throws ParseException in case of Parsing exception.
     * Method does not close input and output streams.
     */
    void process(InputStream in, OutputStream out) throws IOException, ParseException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));

        ResponsesQueryEngine responsesQueryEngine = new ResponsesQueryEngine();

        int countOfRecords = parseCountOfRecords(reader.readLine());
        for (int i = 0; i < countOfRecords; i++) {
            processOneRecord(reader, writer, responsesQueryEngine);
        }

        writer.flush();
    }

    private void processOneRecord(BufferedReader reader,
                                  BufferedWriter writer,
                                  ResponsesQueryEngine responsesQueryEngine) throws IOException, ParseException {
        String record = reader.readLine();

        if (record == null) {
            throw new ParseException("Expected more records");
        }

        try {
            List<String> tokens = splitToTokens(record);
            switch (tokens.get(0)) {
                case QUERY_RECORD:
                    writer.write(resultComputer.apply(responsesQueryEngine.query(parseQueryEntry(tokens))));
                    writer.newLine();
                    break;
                case RESPONSE_RECORD:
                    responsesQueryEngine.add(parseResponseEntry(tokens));
                    break;
                default:
                    throw new ParseException("Invalid record type " + tokens.get(0));
            }
        } catch (ParseException exception) {
            throw new ParseException("Invalid record " + record, exception);
        }
    }

    private static int parseCountOfRecords(String record) throws ParseException {
        int numOfRecords = Integer.parseInt(record);
        if (numOfRecords < 0) {
            throw new ParseException("Records counter must be positive " + record);
        }

        return numOfRecords;
    }

    private static QueryEntry parseQueryEntry(List<String> tokens) throws ParseException {
        if (tokens.size() != NUMBER_OF_ELEMENTS_IN_THE_QUERY_RECORD) {
            throw new ParseException("Invalid query record. Number of elements is incorrect. ");
        }

        QueryEntry.Builder builder = new QueryEntry.Builder()
                .setResponseType(parseResponseType(tokens.get(3)));

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
    }

    private static ResponseEntry parseResponseEntry(List<String> tokens) throws ParseException {
        if (tokens.size() != NUMBER_OF_ELEMENTS_IN_THE_RESPONSE_RECORD) {
            throw new ParseException("Invalid response record. Number of elements is incorrect. ");
        }

        return new ResponseEntry.Builder()
                .setService(parseService(parseNumericTokens(tokens.get(1))))
                .setQuestion(parseQuestion(parseNumericTokens(tokens.get(2))))
                .setResponseType(parseResponseType(tokens.get(3)))
                .setDate(parseDate(tokens.get(4)))
                .setDuration(parseDuration(tokens.get(5)))
                .build();
    }

    private static Service parseService(List<Integer> numbers) throws ParseException {
        if (numbers.size() == 1) {
            return new Service(numbers.get(0));
        } else if (numbers.size() == 2) {
            return new Service(numbers.get(0), numbers.get(1));
        }

        throw new ParseException("To much elements for service " + numbers);
    }

    private static Question parseQuestion(List<Integer> numbers) throws ParseException {
        if (numbers.size() == 1) {
            return new Question(numbers.get(0));
        } else if (numbers.size() == 2) {
            return new Question(numbers.get(0), new Question.QuestionCategory(numbers.get(1)));
        } else if (numbers.size() == 3) {
            return new Question(numbers.get(0), new Question.QuestionCategory(numbers.get(1), numbers.get(2)));
        }

        throw new ParseException("To much elements for question " + numbers);
    }

    private static ResponseType parseResponseType(String token) throws ParseException {
        if (token.equals(FIRST_ANSWER)) {
            return ResponseType.FIRST_ANSWER;
        }

        if (token.equals(NEXT_ANSWER)) {
            return ResponseType.NEXT_ANSWER;
        }

        throw new ParseException("Invalid response type + " + token);
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