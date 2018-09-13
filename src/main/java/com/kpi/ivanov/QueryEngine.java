package com.kpi.ivanov;

import java.util.*;
import java.util.stream.Collectors;

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

    public void add(ResponseEntry responseLogEntry) {
        responseRecords.get(responseLogEntry.getResponseType()).add(responseLogEntry);
    }

    public Set<ResponseEntry> query(QueryEntry queryEntry) {
        return filterAllRecordsByServicesAndQuestions(
                filterByDate(responseRecords.get(queryEntry.getResponseType()), queryEntry) , queryEntry);
    }

    private Set<ResponseEntry> filterByDate(Set<ResponseEntry> responseLogEntries, QueryEntry queryEntry) {
        ResponseEntry fromResponseLogEntry = new ResponseEntry.Builder().setDate(queryEntry.getFromDate()).build();

        ResponseEntry toResponseLogEntry;
        if (queryEntry.getToDate().isPresent()) {
            toResponseLogEntry = new ResponseEntry.Builder().setDate(queryEntry.getToDate().get()).setId().build();
        } else {
            toResponseLogEntry = ((TreeSet<ResponseEntry>)responseLogEntries).last();
        }

        if (fromResponseLogEntry.getDate().compareTo(toResponseLogEntry.getDate()) > 0) {
            return new TreeSet<>();
        }

        return ((TreeSet<ResponseEntry>)responseLogEntries).subSet(
                fromResponseLogEntry,
                true,
                toResponseLogEntry,
                true);
    }

    private Set<ResponseEntry> filterAllRecordsByServicesAndQuestions(
            Set<ResponseEntry> responseEntries,
            QueryEntry queryEntry) {

        return responseEntries.stream().
                filter(responseEntry -> isMatches(responseEntry, queryEntry)).
                collect(Collectors.toCollection(() -> new TreeSet<>(entryComparator)));
    }

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
