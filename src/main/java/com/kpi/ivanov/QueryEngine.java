package com.kpi.ivanov;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represent a container for all response records.
 * Also contains tools for filtering set of responses on a particular query record.
 * Data structure for store records is Map that contains two set.
 * Keys for map is response type of records.
 * This data structure was chosen to increase the performance of match search
 */
public final class QueryEngine {
    private Map<ResponseType, Set<ResponseEntry>> responseRecords;
    private Comparator<ResponseEntry> entryComparator;

    public QueryEngine() {
        entryComparator = (first, second) -> {
            int dateCompare = first.getDate().compareTo(second.getDate());
            if(dateCompare != 0) {
                return dateCompare;
            }
          return first.getId() - second.getId();
        };

        responseRecords = new TreeMap<>();
        responseRecords.put(ResponseType.FIRST_ANSWER, new TreeSet<>(entryComparator));
        responseRecords.put(ResponseType.NEXT_ANSWER, new TreeSet<>(entryComparator));
    }

    /**
     * Method for adding record
     * @param responseEntry element for adding
     */
    public void add(ResponseEntry responseEntry) {
        responseRecords.get(responseEntry.getResponseType()).add(responseEntry);
    }

    /**
     * Method for filtering set of responses.
     * First of all, the set is filtered by date, and then by service and question.
     * @param queryEntry filtering condition
     * @return filtered set
     */
    public Set<ResponseEntry> query(QueryEntry queryEntry) {
        return filterAllRecordsByServicesAndQuestions(
                filterByDate(responseRecords.get(queryEntry.getResponseType()), queryEntry) , queryEntry);
    }

    /**
     * Method for filtering set by date
     * @param responseEntries all response records
     * @param queryEntry query record
     * @return filtered set by date
     */
    private Set<ResponseEntry> filterByDate(Set<ResponseEntry> responseEntries, QueryEntry queryEntry) {
        ResponseEntry fromResponseLogEntry = new ResponseEntry.Builder().setDate(queryEntry.getFromDate()).build();

        ResponseEntry toResponseLogEntry;
        if (queryEntry.getToDate().isPresent()) {
            toResponseLogEntry = new ResponseEntry.Builder().setDate(queryEntry.getToDate().get()).setId().build();
        } else {
            toResponseLogEntry = ((TreeSet<ResponseEntry>)responseEntries).last();
        }

        if (fromResponseLogEntry.getDate().compareTo(toResponseLogEntry.getDate()) > 0) {
            return new TreeSet<>();
        }

        return ((TreeSet<ResponseEntry>)responseEntries).subSet(
                fromResponseLogEntry,
                true,
                toResponseLogEntry,
                true);
    }

    /**
     * Method for filtering set by service and question
     * @param responseEntries filtered set of response records by date
     * @param queryEntry query record
     * @return filtered set by date, service and question
     */
    private Set<ResponseEntry> filterAllRecordsByServicesAndQuestions(
            Set<ResponseEntry> responseEntries,
            QueryEntry queryEntry) {

        return responseEntries.stream().
                filter(responseEntry -> isMatches(responseEntry, queryEntry)).
                collect(Collectors.toCollection(() -> new TreeSet<>(entryComparator)));
    }

    /**
     * @param responseLogEntry response record
     * @param queryEntry query record
     * @return if parameters is matches returns true otherwise false
     */
    private  boolean isMatches(ResponseEntry responseLogEntry, QueryEntry queryEntry) {
        boolean isServiceMatches = true;
        boolean isQuestionMatches = true;

        if (queryEntry.getService().isPresent()) {
            isServiceMatches = responseLogEntry.getService().isMatches(queryEntry.getService().get());
        }

        if (queryEntry.getQuestion().isPresent()) {
            isQuestionMatches = responseLogEntry.getQuestion().isMatches(queryEntry.getQuestion().get());
        }

        return isServiceMatches && isQuestionMatches;
    }
}
