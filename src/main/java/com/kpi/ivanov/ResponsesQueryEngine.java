package com.kpi.ivanov;

import java.time.LocalDate;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Container for all response records.
 * Also contains a tools for filtering records.
 */
final class ResponsesQueryEngine {
    private final Map<ResponseType, ResponseEntriesByDate> entriesByType =
            EnumSet.allOf(ResponseType.class)
                    .stream()
                    .collect(Collectors.toMap(Function.identity(), type -> new ResponseEntriesByDate()));

    void add(ResponseEntry responseEntry) {
        entriesByType.get(responseEntry.getResponseType()).add(responseEntry);
    }

    Set<ResponseEntry> query(QueryEntry queryEntry) {
        ResponseEntriesByDate entries = entriesByType.get(queryEntry.getResponseType());
        if (queryEntry.getToDate().isPresent()) {
            return entries.findAll(queryEntry.getFromDate(), queryEntry.getToDate().get(), queryEntry::isMatches);
        }

        return entries.findAll(queryEntry.getFromDate(), queryEntry::isMatches);
    }

    private static final class ResponseEntriesByDate {
        private final NavigableMap<LocalDate, Set<ResponseEntry>> entriesByDate = new TreeMap<>();

        void add(ResponseEntry responseEntry) {
            Set<ResponseEntry> entries = entriesByDate.computeIfAbsent(responseEntry.getDate(), key -> new HashSet<>());

            entries.add(responseEntry);
        }

        Set<ResponseEntry> findAll(LocalDate fromDate, LocalDate toDate, Predicate<ResponseEntry> predicate) {
            return findAll(entriesByDate.subMap(fromDate, true, toDate, true), predicate);
        }

        Set<ResponseEntry> findAll(LocalDate fromDate, Predicate<ResponseEntry> predicate) {
            return findAll(entriesByDate.tailMap(fromDate, true), predicate);
        }

        private Set<ResponseEntry> findAll(Map<LocalDate, Set<ResponseEntry>> entries,
                                           Predicate<ResponseEntry> predicate) {
            return entries.values()
                    .stream()
                    .flatMap(Collection::stream)
                    .filter(predicate)
                    .collect(Collectors.toSet());
        }
    }
}
