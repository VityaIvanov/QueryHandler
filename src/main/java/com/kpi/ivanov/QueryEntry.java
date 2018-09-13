package com.kpi.ivanov;

import java.time.LocalDate;
import java.util.Optional;

public final class QueryEntry {
    private Service service;
    private Question question;
    private ResponseType responseType;
    private LocalDate fromDate;
    private LocalDate toDate;

    public static class Builder {
        private Service service;
        private Question question;
        private ResponseType responseType;
        private LocalDate fromDate;
        private LocalDate toDate;

        public Builder setService(Service service) {
            this.service = service;
            return this;
        }

        public Builder setQuestion(Question question) {
            this.question = question;
            return this;
        }

        public Builder setResponseType(ResponseType responseType) {
            this.responseType = responseType;
            return this;
        }

        public Builder setFromDate(LocalDate fromDate) {
            this.fromDate = fromDate;
            return this;
        }

        public Builder setToDate(LocalDate toDate) {
            this.toDate = toDate;
            return this;
        }

        public QueryEntry build() {
            return new QueryEntry(this);
        }
    }

    private QueryEntry(Builder builder) {
        this.service = builder.service;
        this.question = builder.question;
        this.responseType = builder.responseType;
        this.fromDate = builder.fromDate;
        this.toDate = builder.toDate;
    }

    Optional<Service> getService() {
        if (service == null) {
            return Optional.empty();
        }

        return Optional.of(service);
    }

    Optional<Question> getQuestion() {
        if (question == null) {
            return Optional.empty();
        }

        return Optional.of(question);
    }

    ResponseType getResponseType() {
        return responseType;
    }

    LocalDate getFromDate() {
        return fromDate;
    }

    Optional<LocalDate> getToDate() {
        if (toDate == null) {
            return Optional.empty();
        }

        return Optional.of(toDate);
    }
}
