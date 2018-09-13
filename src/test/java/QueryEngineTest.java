import com.kpi.ivanov.*;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class QueryEngineTest {

    private QueryEngine queryEngine;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private ResponseEntry responseLogEntry0;
    private ResponseEntry responseLogEntry1;
    private ResponseEntry responseLogEntry2;
    private ResponseEntry responseLogEntry3;

    private ResponseEntry responseLogEntry4;
    private ResponseEntry responseLogEntry5;
    private ResponseEntry responseLogEntry6;

    @Before
    public void setIn() {

        responseLogEntry0 = new ResponseEntry.Builder().
                setService(new Service(1, 1)).
                setQuestion(new Question(8, 15, 1)).
                setResponseType(ResponseType.FIRST_ANSWER).
                setDate(LocalDate.parse("15.10.2012", formatter)).
                setDuration(Duration.ofMinutes(83)).
                build();

        responseLogEntry1 = new ResponseEntry.Builder().
                setService(new Service(1)).
                setQuestion(new Question(10, 1)).
                setResponseType(ResponseType.FIRST_ANSWER).
                setDate(LocalDate.parse("01.12.2012", formatter)).
                setDuration(Duration.ofMinutes(65)).
                build();


        responseLogEntry2 = new ResponseEntry.Builder().
                setService(new Service(1, 1)).
                setQuestion(new Question(5, 5, 1)).
                setResponseType(ResponseType.FIRST_ANSWER).
                setDate(LocalDate.parse("01.11.2012", formatter)).
                setDuration(Duration.ofMinutes(117)).
                build();


        responseLogEntry3 = new ResponseEntry.Builder().
                setService(new Service(3)).
                setQuestion(new Question(10, 2)).
                setResponseType(ResponseType.NEXT_ANSWER).
                setDate(LocalDate.parse("02.10.2012", formatter)).
                setDuration(Duration.ofMinutes(100)).
                build();

        responseLogEntry4 = new ResponseEntry.Builder().
                setService(new Service(4, 3)).
                setQuestion(new Question(2, 20)).
                setResponseType(ResponseType.FIRST_ANSWER).
                setDate(LocalDate.parse("10.09.2012", formatter)).
                setDuration(Duration.ofMinutes(834)).
                build();


        responseLogEntry5 = new ResponseEntry.Builder().
                setService(new Service(4, 1)).
                setQuestion(new Question(2, 20, 5)).
                setResponseType(ResponseType.FIRST_ANSWER).
                setDate(LocalDate.parse("12.10.2012", formatter)).
                setDuration(Duration.ofMinutes(23)).
                build();

        responseLogEntry6 = new ResponseEntry.Builder().
                setService(new Service(4)).
                setQuestion(new Question(2, 1)).
                setResponseType(ResponseType.FIRST_ANSWER).
                setDate(LocalDate.parse("15.08.2012", formatter)).
                setDuration(Duration.ofMinutes(94)).
                build();


        queryEngine = new QueryEngine();
        queryEngine.add(responseLogEntry0);
        queryEngine.add(responseLogEntry1);
        queryEngine.add(responseLogEntry2);
        queryEngine.add(responseLogEntry3);

        queryEngine.add(responseLogEntry4);
        queryEngine.add(responseLogEntry5);
        queryEngine.add(responseLogEntry6);
    }


    @Test
    public void test() {
        QueryEntry queryEntry = new QueryEntry.Builder().
                setService(new Service(1, 1)).
                setQuestion(new Question(8)).
                setResponseType(ResponseType.FIRST_ANSWER).
                setFromDate(LocalDate.parse("01.01.2012", formatter)).
                setToDate(LocalDate.parse("01.12.2012", formatter)).
                build();


        Set<ResponseEntry> filtered = queryEngine.query(queryEntry);

        List<ResponseEntry> answer = new ArrayList<>();
        answer.add(responseLogEntry0);

        assertEquals(filtered.size(), answer.size());
        for (int i = 0; i < answer.size(); i++) {
            assertTrue(filtered.contains(answer.get(i)));
            answer.remove(i);
            i--;
        }

        //assertNotEquals(1, 2);
    }


    @Test
    public void test2() {

        QueryEntry queryEntry = new QueryEntry.Builder().
                setService(new Service(1)).
                setResponseType(ResponseType.FIRST_ANSWER).
                setFromDate(LocalDate.parse("08.10.2012", formatter)).
                setToDate(LocalDate.parse("20.11.2012", formatter)).
                build();


        Set<ResponseEntry> filtered = queryEngine.query(queryEntry);

        List<ResponseEntry> answer = new ArrayList<>();
        answer.add(responseLogEntry0);
        answer.add(responseLogEntry2);

        assertEquals(filtered.size(), answer.size());
        for (int i = 0; i < answer.size(); i++) {
            assertTrue(filtered.contains(answer.get(i)));
            answer.remove(i);
            i--;
        }
    }

    @Test
    public void test3() {
        QueryEntry queryEntry = new QueryEntry.Builder().
                setService(new Service(3)).
                setQuestion(new Question(10)).
                setResponseType(ResponseType.FIRST_ANSWER).
                setFromDate(LocalDate.parse("01.12.2012", formatter)).
                build();


        Set<ResponseEntry> filtered = queryEngine.query(queryEntry);

        List<ResponseEntry> answer = new ArrayList<>();

        assertEquals(filtered.size(), answer.size());
        for (int i = 0; i < answer.size(); i++) {
            assertTrue(filtered.contains(answer.get(i)));
            answer.remove(i);
            i--;
        }
    }

    @Test
    public void test4() {
        QueryEntry queryEntry = new QueryEntry.Builder().
                setResponseType(ResponseType.FIRST_ANSWER).
                setFromDate(LocalDate.parse("01.12.2012", formatter)).
                build();


        Set<ResponseEntry> filtered = queryEngine.query(queryEntry);

        List<ResponseEntry> answer = new ArrayList<>();
        answer.add(responseLogEntry1);
        assertEquals(filtered.size(), answer.size());
        for (int i = 0; i < answer.size(); i++) {
            assertTrue(filtered.contains(answer.get(i)));
            answer.remove(i);
            i--;
        }
    }

    @Test
    public void test5() {
        QueryEntry queryEntry = new QueryEntry.Builder().
                setService(new Service(4)).
                setQuestion(new Question(2)).
                setResponseType(ResponseType.FIRST_ANSWER).
                setFromDate(LocalDate.parse("01.08.2012", formatter)).
                build();


        Set<ResponseEntry> filtered = queryEngine.query(queryEntry);

        List<ResponseEntry> answer = new ArrayList<>();
        answer.add(responseLogEntry4);
        answer.add(responseLogEntry5);
        answer.add(responseLogEntry6);

        assertEquals(filtered.size(), answer.size());
        for (int i = 0; i < answer.size(); i++) {
            assertTrue(filtered.contains(answer.get(i)));
            answer.remove(i);
            i--;
        }
    }

    @Test
    public void test6() {
        QueryEntry queryEntry = new QueryEntry.Builder().
                setService(new Service(4)).
                setQuestion(new Question(2, 20)).
                setResponseType(ResponseType.FIRST_ANSWER).
                setFromDate(LocalDate.parse("09.09.2012", formatter)).
                setToDate(LocalDate.parse("12.09.2012", formatter)).
                build();

        Set<ResponseEntry> filtered = queryEngine.query(queryEntry);

        List<ResponseEntry> answer = new ArrayList<>();
        answer.add(responseLogEntry4);

        assertEquals(filtered.size(), answer.size());
        for (int i = 0; i < answer.size(); i++) {
            assertTrue(filtered.contains(answer.get(i)));
            answer.remove(i);
            i--;
        }
    }

    @Test
    public void test7() {
        QueryEntry queryEntry = new QueryEntry.Builder().
                setResponseType(ResponseType.FIRST_ANSWER).
                setFromDate(LocalDate.parse("09.09.2011", formatter)).
                setToDate(LocalDate.parse("12.09.2013", formatter)).
                build();

        Set<ResponseEntry> filtered = queryEngine.query(queryEntry);

        List<ResponseEntry> answer = new ArrayList<>();
        answer.add(responseLogEntry0);
        answer.add(responseLogEntry1);
        answer.add(responseLogEntry2);
        answer.add(responseLogEntry4);
        answer.add(responseLogEntry5);
        answer.add(responseLogEntry6);

        assertEquals(filtered.size(), answer.size());
        for (int i = 0; i < answer.size(); i++) {
            assertTrue(filtered.contains(answer.get(i)));
            answer.remove(i);
            i--;
        }
    }
}
