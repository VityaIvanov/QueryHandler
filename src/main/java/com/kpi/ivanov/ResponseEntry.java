package com.kpi.ivanov;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a response record (C)
 * Field id added for correct work of set,
 * to prevent appearing of same objects
 */
public final class ResponseEntry {
    private static int ID = 1;

    private final Service service;
    private final Question question;
    private final ResponseType responseType;
    private final LocalDate date;
    private final Duration responseTime;

    private int id;

    private ResponseEntry(Builder builder) {
        this.service = builder.service;
        this.question = builder.question;
        this.responseType = builder.responseType;
        this.date = builder.date;
        this.responseTime = builder.responseTime;
        this.id = builder.id;
    }

    Service getService() {
        return service;
    }

    Question getQuestion() {
        return question;
    }

    ResponseType getResponseType() {
        return responseType;
    }

    LocalDate getDate() {
        return date;
    }

    public Duration getResponseTime() {
        return responseTime;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof ResponseEntry)) {
            return false;
        }

        ResponseEntry responseEntry = (ResponseEntry) obj;

        return this.service.equals(responseEntry.service) &&
                this.question.equals(responseEntry.question) &&
                this.responseType.equals(responseEntry.responseType) &&
                this.date.equals(responseEntry.date) &&
                this.responseTime.equals(responseEntry.responseTime) &&
                this.id == responseEntry.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(service, question, responseType, date, responseTime, id);
    }

    /**
     * Builder for creating instance of ResponseEntry class
     */
    public static class Builder {
        private Service service;
        private Question question;
        private ResponseType responseType;
        private LocalDate date;
        private Duration responseTime;
        private int id;

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

        public Builder setDate(LocalDate date) {
            this.date = date;
            return this;
        }

        public Builder setDuration(Duration responseTime) {
            this.responseTime = responseTime;
            return this;
        }

        public Builder setId() {
            id = ID++;
            return this;
        }

        public ResponseEntry build() {
            return new ResponseEntry(this);
        }
    }
}
