package com.kpi.ivanov;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Represents a query record (D)
 * Field toDate optional field and getter method return Optional type
 * Service and question fields have special value "*" and to represent
 * this value used Optional type that returns with getters.
 * Special value "*" it means query match all services/question types
 */
public final class QueryEntry {
    private Service service;
    private Question question;
    private ResponseType responseType;
    private LocalDate fromDate;
    private LocalDate toDate;

    /**
     * Builder for creating instance of QueryEntry class
     */
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

    /**
     * If service contains null value it means that special value present
     * @return Optional view of service
     */
    Optional<Service> getService() {
        if (service == null) {
            return Optional.empty();
        }

        return Optional.of(service);
    }

    /**
     * If service contains null value it means that special value present
     * @return Optional view of question
     */
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

    /**
     * If service contains null value it means that special value present
     * @return Optional view of toDate
     */
    Optional<LocalDate> getToDate() {
        if (toDate == null) {
            return Optional.empty();
        }

        return Optional.of(toDate);
    }
}
