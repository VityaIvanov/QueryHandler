package com.kpi.ivanov;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Query for customer response records.
 */
final class QueryEntry {
    private final Service service;
    private final Question question;
    private final ResponseType responseType;
    private final LocalDate fromDate;
    private final LocalDate toDate;

    private QueryEntry(Builder builder) {
        this.service = builder.service;
        this.question = builder.question;
        this.responseType = builder.responseType;
        this.fromDate = builder.fromDate;
        this.toDate = builder.toDate;
    }

    ResponseType getResponseType() {
        return responseType;
    }

    LocalDate getFromDate() {
        return fromDate;
    }

    Optional<LocalDate> getToDate() {
        return Optional.ofNullable(toDate);
    }

    boolean isMatches(ResponseEntry responseEntry) {
        boolean isServiceMatches = true;
        boolean isQuestionMatches = true;

        if (service != null) {
            isServiceMatches = responseEntry.getService().isMatches(service);
        }

        if (question != null) {
            isQuestionMatches = responseEntry.getQuestion().isMatches(question);
        }

        return isServiceMatches && isQuestionMatches;
    }

    /**
     * Builder for QueryEntry.
     */
    static class Builder {
        private Service service;
        private Question question;
        private ResponseType responseType;
        private LocalDate fromDate;
        private LocalDate toDate;

        Builder setService(Service service) {
            this.service = service;
            return this;
        }

        Builder setQuestion(Question question) {
            this.question = question;
            return this;
        }

        Builder setResponseType(ResponseType responseType) {
            this.responseType = responseType;
            return this;
        }

        Builder setFromDate(LocalDate fromDate) {
            this.fromDate = fromDate;
            return this;
        }

        Builder setToDate(LocalDate toDate) {
            this.toDate = toDate;
            return this;
        }

        QueryEntry build() {
            return new QueryEntry(this);
        }
    }
}
